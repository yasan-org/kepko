package glass.yasan.kepko.buildlogic.icons

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import org.gradle.work.DisableCachingByDefault
import java.io.File
import java.io.IOException
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration
import java.util.concurrent.Callable
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executors

/**
 * Syncs the icons listed in the manifest into the module.
 *
 * In sync mode it downloads every Material Symbol, writes them as `ic_<name>.xml` vector
 * drawables, deletes `ic_*.xml` drawables that are no longer listed and regenerates the
 * `Icons` object and the `NamedIcon` enum.
 *
 * With [checkOnly] set it changes nothing and fails if anything is out of sync. The check
 * works offline and covers the generated sources and the set of drawables; with [download]
 * it also downloads every icon and compares the drawable contents.
 */
@DisableCachingByDefault(because = "Writes into the source tree and may download icons")
public abstract class SyncIconsTask : DefaultTask() {

    @get:InputFile
    @get:PathSensitive(PathSensitivity.RELATIVE)
    public abstract val manifest: RegularFileProperty

    @get:Internal
    public abstract val drawableDirectory: DirectoryProperty

    @get:Internal
    public abstract val sourceDirectory: DirectoryProperty

    @get:Input
    public abstract val packageName: Property<String>

    @get:Input
    public abstract val iconsName: Property<String>

    @get:Input
    public abstract val namedIconName: Property<String>

    @get:Input
    public abstract val checkOnly: Property<Boolean>

    @get:Input
    @get:Option(option = "download", description = "checkIcons only: also download every icon and compare the drawable contents.")
    public abstract val download: Property<Boolean>

    private val rootDirectory: File = project.rootDir

    @TaskAction
    public fun sync() {
        val manifestFile = manifest.get().asFile
        val icons = IconManifest.read(manifestFile)
        val drawables = drawableDirectory.get().asFile
        val sources = sourceDirectory.get().asFile
        val check = checkOnly.get()
        if (!check && !download.get()) {
            throw GradleException("--no-download only applies to checkIcons. syncIcons always downloads every icon.")
        }

        val expected = linkedMapOf<File, String>()
        if (!check || download.get()) {
            download(icons).forEach { (icon, xml) -> expected[drawables.resolve(icon.fileName)] = xml }
        }
        IconsSourceGenerator.generate(packageName.get(), iconsName.get(), namedIconName.get(), icons).forEach {
            expected[sources.resolve(it.relativePath)] = it.toString()
        }

        val listed = icons.map { drawables.resolve(it.fileName) }.toSet()
        val missing = listed.filterNot { it.exists() || it in expected }
        val changed = expected.filter { (file, text) -> !file.exists() || file.readText().normalizeLineEndings() != text }
            .keys
        val stale = drawables.listFiles { file -> file.name.startsWith("ic_") && file.name.endsWith(".xml") }
            .orEmpty()
            .filter { it !in listed }
            .sorted()
        val outdated = (missing + changed).sorted()

        if (check) {
            outdated.forEach { logger.lifecycle("outdated: ${it.relativeTo(rootDirectory)}") }
            stale.forEach { logger.lifecycle("unlisted: ${it.relativeTo(rootDirectory)}") }
            if (outdated.isNotEmpty() || stale.isNotEmpty()) {
                throw GradleException("Icons are out of sync with ${manifestFile.name}. Run the syncIcons task.")
            }
        } else {
            outdated.forEach {
                it.parentFile.mkdirs()
                it.writeText(expected.getValue(it))
                logger.lifecycle("updated: ${it.relativeTo(rootDirectory)}")
            }
            stale.forEach {
                if (!it.delete()) throw GradleException("Could not delete ${it.relativeTo(rootDirectory)}")
                logger.lifecycle("removed: ${it.relativeTo(rootDirectory)}")
            }
        }
        if (outdated.isEmpty() && stale.isEmpty()) logger.lifecycle("${icons.size} icons in sync")
    }

    private fun download(icons: List<Icon>): List<Pair<Icon, String>> =
        HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(TIMEOUT_SECONDS))
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build()
            .use { client -> download(client, icons) }

    private fun download(client: HttpClient, icons: List<Icon>): List<Pair<Icon, String>> {
        val executor = Executors.newFixedThreadPool(MAX_PARALLEL_DOWNLOADS)
        try {
            val downloads = icons.map { icon -> icon to executor.submit(Callable { fetch(client, icon) }) }
            val errors = mutableListOf<String>()
            val results = downloads.mapNotNull { (icon, future) ->
                runCatching { icon to VectorDrawableConverter.convert(future.get(), temporaryDir, icon.name) }
                    .onFailure {
                        val cause = if (it is ExecutionException) it.cause ?: it else it
                        errors += "${icon.name}: ${cause.message} (${icon.url})"
                    }
                    .getOrNull()
            }
            if (errors.isNotEmpty()) {
                throw GradleException("Failed to sync icons:\n" + errors.sorted().joinToString("\n") { "  $it" })
            }
            return results
        } finally {
            executor.shutdownNow()
        }
    }

    /** Downloads one icon, retrying connection failures, throttling and server errors with a growing delay. */
    private fun fetch(client: HttpClient, icon: Icon): String {
        val request = HttpRequest.newBuilder(URI.create(icon.url))
            .timeout(Duration.ofSeconds(TIMEOUT_SECONDS))
            .header("User-Agent", "kepko-sync-icons")
            .build()
        fun send() = runCatching { client.send(request, HttpResponse.BodyHandlers.ofString()) }
        var result = send()
        var attempt = 1
        while (attempt < MAX_ATTEMPTS && result.isRetryable()) {
            Thread.sleep(RETRY_DELAY_MILLIS * attempt)
            attempt++
            result = send()
        }
        val response = result.getOrThrow()
        return when (response.statusCode()) {
            HTTP_OK -> response.body()
            HTTP_NOT_FOUND -> error("symbol '${icon.symbol}' not found")
            else -> error("HTTP ${response.statusCode()}")
        }
    }

    private fun Result<HttpResponse<*>>.isRetryable(): Boolean {
        val status = getOrNull()?.statusCode() ?: return exceptionOrNull() is IOException
        return status == HTTP_TOO_MANY_REQUESTS || status >= HTTP_SERVER_ERROR
    }

    private companion object {
        const val TIMEOUT_SECONDS = 30L
        const val MAX_PARALLEL_DOWNLOADS = 16
        const val MAX_ATTEMPTS = 3
        const val RETRY_DELAY_MILLIS = 1_000L
        const val HTTP_OK = 200
        const val HTTP_NOT_FOUND = 404
        const val HTTP_TOO_MANY_REQUESTS = 429
        const val HTTP_SERVER_ERROR = 500
    }
}
