package glass.yasan.crowdin.screenshots

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import java.io.IOException
import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpRequest.BodyPublishers
import java.net.http.HttpResponse.BodyHandlers
import java.net.http.HttpTimeoutException
import java.time.Duration

/** Crowdin API v2 client scoped to one project and its [sourceFile]'s strings. */
internal class CrowdinClient(
    private val token: String,
    projectId: String,
    baseUrl: String,
    private val sourceFile: String,
    private val branch: String?,
    private val log: (String) -> Unit,
) : CrowdinApi {

    private val baseUrl = baseUrl.trimEnd('/')
    private val projectUrl = "${this.baseUrl}/projects/$projectId"
    private val http = HttpClient.newBuilder().connectTimeout(TIMEOUT).build()

    override fun sourceStrings(): Map<String, SourceString> =
        paginate("/strings", StringDto.serializer(), mapOf("fileId" to sourceFileId().toString()))
            .mapNotNull { string ->
                val text = (string.text as? JsonPrimitive)?.takeIf { it.isString }?.content ?: return@mapNotNull null
                SourceString(id = string.id, identifier = string.identifier, text = text)
            }
            .associateBy { it.identifier }

    override fun screenshots(): List<Screenshot> = paginate("/screenshots", Screenshot.serializer())

    override fun createScreenshot(name: String, png: ByteArray): Screenshot {
        val storage = request(
            method = "POST",
            url = "$baseUrl/storages",
            body = BodyPublishers.ofByteArray(png),
            headers = mapOf("Crowdin-API-FileName" to name, "Content-Type" to "image/png"),
        )
        val storageId = json.decodeFromString(Data.serializer(StorageDto.serializer()), storage).data.id
        val body = json.encodeToString(
            CreateScreenshotDto.serializer(),
            CreateScreenshotDto(storageId = storageId, name = name, autoTag = false),
        )
        val created = request("POST", "$projectUrl/screenshots", jsonBody(body), JSON_HEADERS)
        return json.decodeFromString(Data.serializer(Screenshot.serializer()), created).data
    }

    override fun replaceTags(screenshotId: Long, tags: List<Tag>) {
        val body = json.encodeToString(ListSerializer(Tag.serializer()), tags)
        request("PUT", "$projectUrl/screenshots/$screenshotId/tags", jsonBody(body), JSON_HEADERS)
    }

    override fun deleteScreenshot(screenshotId: Long) {
        request("DELETE", "$projectUrl/screenshots/$screenshotId")
    }

    private fun sourceFileId(): Long {
        val files = paginate("/files", FileDto.serializer())
        var matching = files.filter { it.path.endsWith("/" + sourceFile.trimStart('/')) }
        if (branch != null) {
            val branchIds = paginate("/branches", BranchDto.serializer(), mapOf("name" to branch)).map { it.id }
            matching = matching.filter { it.branchId in branchIds }
        }
        return matching.singleOrNull()?.id ?: throw CrowdinException(
            "Expected one Crowdin file ending with '$sourceFile'" +
                (branch?.let { " in branch '$it'" } ?: "") +
                ", found ${matching.size}. Crowdin files: ${files.joinToString { it.path }.ifEmpty { "none" }}",
        )
    }

    private fun <T> paginate(
        path: String,
        serializer: KSerializer<T>,
        params: Map<String, String> = emptyMap(),
    ): List<T> {
        val items = mutableListOf<T>()
        var offset = 0
        while (true) {
            val query = (params + mapOf("limit" to PAGE_SIZE.toString(), "offset" to offset.toString()))
                .entries.joinToString("&") { (key, value) -> "$key=${URLEncoder.encode(value, Charsets.UTF_8)}" }
            val page = json.decodeFromString(Page.serializer(serializer), request("GET", "$projectUrl$path?$query"))
            items += page.data.map { it.data }
            if (page.data.size < PAGE_SIZE) return items
            offset += PAGE_SIZE
        }
    }

    /**
     * Sends a request and returns the response body. Retries 429 for every method; 5xx and network
     * errors only for idempotent ones, as a POST that failed that way may still have been applied.
     */
    private fun request(
        method: String,
        url: String,
        body: HttpRequest.BodyPublisher = BodyPublishers.noBody(),
        headers: Map<String, String> = emptyMap(),
    ): String {
        for (attempt in 1..ATTEMPTS) {
            val request = HttpRequest.newBuilder(URI.create(url))
                .method(method, body)
                .timeout(TIMEOUT)
                .header("Authorization", "Bearer $token")
                .apply { headers.forEach { (name, value) -> header(name, value) } }
                .build()
            val delaySeconds = try {
                val response = http.send(request, BodyHandlers.ofString())
                val status = response.statusCode()
                if (status in SUCCESS) return response.body()
                val retryable = status == TOO_MANY_REQUESTS || (status >= SERVER_ERROR && method != "POST")
                if (!retryable || attempt == ATTEMPTS) {
                    throw CrowdinException("$method $url failed with $status: ${response.body()}")
                }
                val retryAfter = response.headers().firstValue("Retry-After").map { it.toLongOrNull() }.orElse(null)
                retryAfter?.coerceIn(0, MAX_RETRY_DELAY_SECONDS) ?: backoff(attempt)
            } catch (error: IOException) {
                if (attempt == ATTEMPTS || method == "POST") throw CrowdinException("$method $url failed: $error")
                if (error !is HttpTimeoutException) log("  $method $url: $error")
                backoff(attempt)
            }
            log("  retrying $method $url in ${delaySeconds}s")
            Thread.sleep(Duration.ofSeconds(delaySeconds))
        }
        error("unreachable")
    }

    private fun backoff(attempt: Int): Long = 1L shl attempt

    private fun jsonBody(body: String) = BodyPublishers.ofString(body)

    @Serializable
    private data class Page<T>(val data: List<Data<T>>)

    @Serializable
    private data class Data<T>(val data: T)

    @Serializable
    private data class FileDto(val id: Long, val path: String, val branchId: Long? = null)

    @Serializable
    private data class BranchDto(val id: Long)

    @Serializable
    private data class StringDto(val id: Long, val identifier: String, val text: JsonElement)

    @Serializable
    private data class StorageDto(val id: Long)

    @Serializable
    private data class CreateScreenshotDto(val storageId: Long, val name: String, val autoTag: Boolean)

    private companion object {
        const val PAGE_SIZE = 500
        const val ATTEMPTS = 4
        const val TOO_MANY_REQUESTS = 429
        const val SERVER_ERROR = 500
        const val MAX_RETRY_DELAY_SECONDS = 60L
        val SUCCESS = 200..299
        val TIMEOUT: Duration = Duration.ofSeconds(60)
        val JSON_HEADERS = mapOf("Content-Type" to "application/json")
    }
}
