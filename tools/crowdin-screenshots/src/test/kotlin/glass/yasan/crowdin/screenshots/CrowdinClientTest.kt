package glass.yasan.crowdin.screenshots

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpServer
import java.net.InetSocketAddress
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class CrowdinClientTest {

    private val requests = mutableListOf<String>()
    private var failuresLeft = 0
    private val server = HttpServer.create(InetSocketAddress("127.0.0.1", 0), 0).apply {
        createContext("/") { exchange -> handle(exchange) }
        start()
    }
    private val client = CrowdinClient(
        token = "token",
        projectId = "1",
        baseUrl = "http://127.0.0.1:${server.address.port}/api/v2/",
        sourceFile = "resource/values/strings.xml",
        branch = null,
        log = {},
    )

    @AfterTest
    fun tearDown() {
        server.stop(0)
    }

    @Test
    fun `reads plain strings of the source file only`() {
        val strings = client.sourceStrings()

        assertEquals(mapOf("back" to SourceString(11, "back", "Back")), strings)
        assert(requests.any { it.startsWith("GET /api/v2/projects/1/strings?fileId=7") })
    }

    @Test
    fun `retries server errors`() {
        failuresLeft = 1

        client.replaceTags(5, listOf(Tag(11)))

        val request = "PUT /api/v2/projects/1/screenshots/5/tags [{\"stringId\":11}]"
        assertEquals(2, requests.count { it == request })
    }

    @Test
    fun `creates screenshots through storage`() {
        val screenshot = client.createScreenshot("Screen@0123456789ab.png", byteArrayOf(1, 2))

        assertEquals(Screenshot(20, "Screen@0123456789ab.png", emptyList()), screenshot)
        val body = """{"storageId":55,"name":"Screen@0123456789ab.png","autoTag":false}"""
        assert("POST /api/v2/projects/1/screenshots $body" in requests)
    }

    @Test
    fun `does not retry failed POSTs`() {
        failuresLeft = 1

        assertFailsWith<CrowdinException> { client.createScreenshot("Screen@0123456789ab.png", byteArrayOf(1)) }

        assertEquals(1, requests.count { it.startsWith("POST") })
    }

    @Test
    fun `fails on client errors without retrying`() {
        assertFailsWith<CrowdinException> { client.deleteScreenshot(404) }
        assertEquals(1, requests.count { it.startsWith("DELETE") })
    }

    private fun handle(exchange: HttpExchange) {
        val body = exchange.requestBody.readBytes()
        val path = exchange.requestURI.toString()
        val isJson = exchange.requestHeaders.getFirst("Content-Type") == "application/json"
        val text = if (isJson) " ${body.decodeToString()}" else ""
        requests += "${exchange.requestMethod} $path$text"
        val (status, response) = when {
            failuresLeft > 0 -> {
                failuresLeft--
                503 to "{}"
            }
            path.contains("/files?") -> 200 to page(
                """{"id": 7, "path": "/resource/values/strings.xml"}""",
                """{"id": 8, "path": "/sample/values/strings.xml"}""",
            )
            path.contains("/strings?") -> 200 to page(
                """{"id": 11, "identifier": "back", "text": "Back"}""",
                """{"id": 12, "identifier": "items", "text": {"one": "item"}}""",
            )
            path.endsWith("/storages") -> 201 to """{"data": {"id": 55}}"""
            exchange.requestMethod == "POST" ->
                201 to """{"data": {"id": 20, "name": "Screen@0123456789ab.png", "tags": []}}"""
            path.endsWith("/404") -> 404 to """{"error": "not found"}"""
            else -> 200 to """{"data": []}"""
        }
        val bytes = response.toByteArray()
        exchange.sendResponseHeaders(status, bytes.size.toLong())
        exchange.responseBody.use { it.write(bytes) }
    }

    private fun page(vararg items: String) = """{"data": [${items.joinToString { """{"data": $it}""" }}]}"""
}
