package glass.yasan.crowdin.screenshots

import java.io.File
import java.security.MessageDigest

private const val HASH_LENGTH = 12

/**
 * Uploads every `<name>.png` with a `<name>.json` next to it in [directories] and tags it with the
 * strings the preview is linked to. With no [api], only logs what it would tag.
 *
 * Screenshots are named `<namespace>.<name>@<hash>.png` after the PNG's content, so an unchanged
 * image is never re-uploaded and only its tags are synced. Screenshots in [namespace] that no
 * longer match a local PNG are deleted, but only after every upload succeeded. Other screenshots,
 * including ones uploaded by hand or by another namespace, are never touched.
 *
 * @param strict fail on keys missing from [strings] instead of skipping them, to catch typos.
 * @return whether everything succeeded.
 */
internal fun syncScreenshots(
    directories: List<File>,
    strings: Map<String, SourceString>,
    api: CrowdinApi?,
    namespace: String,
    strict: Boolean,
    log: (String) -> Unit,
): Boolean {
    val contextFiles = contextFiles(directories)
    val managedName = Regex("""^${Regex.escape(namespace)}\..+@[0-9a-f]{$HASH_LENGTH}\.png$""")
    val managed = api?.screenshots().orEmpty().filter { managedName.matches(it.name) }
    val keepIds = mutableSetOf<Long>()
    var failed = false

    for (contextFile in contextFiles) {
        val context = json.decodeFromString(ScreenshotContext.serializer(), contextFile.readText())
        val unknownKeys = context.keys.filter { it !in strings }
        if (strict && unknownKeys.isNotEmpty()) {
            log("$contextFile: unknown keys ${unknownKeys.joinToString()}")
            failed = true
        }
        try {
            syncScreenshot(contextFile, context, strings, api, namespace, managed, log)?.let { keepIds += it }
        } catch (error: CrowdinException) {
            log("  failed: ${error.message}")
            failed = true
        }
    }

    if (failed) {
        log("Some screenshots failed; stale screenshots were left in place.")
        return false
    }
    for (screenshot in managed.filter { it.id !in keepIds }) {
        log("Deleting ${screenshot.name}")
        try {
            api?.deleteScreenshot(screenshot.id)
        } catch (error: CrowdinException) {
            log("  failed: ${error.message}")
            failed = true
        }
    }
    return !failed
}

/**
 * Lists the `.json` files in [directories]. Fails on a missing directory, no files at all or the
 * same name in two directories, any of which would otherwise delete or mix up screenshots.
 */
private fun contextFiles(directories: List<File>): List<File> {
    val missing = directories.filterNot { it.isDirectory }
    val files = directories.flatMap { directory ->
        directory.listFiles { file -> file.extension == "json" }.orEmpty().sorted()
    }
    val duplicates = files.groupBy { it.name }.filterValues { it.size > 1 }.keys
    val problem = when {
        missing.isNotEmpty() -> "Snapshot directories don't exist: ${missing.joinToString()}"
        files.isEmpty() -> "No snapshot .json files in ${directories.joinToString()}"
        duplicates.isNotEmpty() -> "Same snapshot names in several directories: ${duplicates.joinToString()}"
        else -> null
    }
    if (problem != null) throw CrowdinException(problem)
    return files
}

/** Uploads or re-tags one snapshot and returns its screenshot ID, or `null` if nothing was uploaded. */
private fun syncScreenshot(
    contextFile: File,
    context: ScreenshotContext,
    strings: Map<String, SourceString>,
    api: CrowdinApi?,
    namespace: String,
    managed: List<Screenshot>,
    log: (String) -> Unit,
): Long? {
    val png = contextFile.resolveSibling("${contextFile.nameWithoutExtension}.png")
    if (!png.exists()) {
        log("$contextFile: no PNG next to it, skipped")
        return null
    }
    val bytes = png.readBytes()
    val name = "$namespace.${png.nameWithoutExtension}@${sha256(bytes).take(HASH_LENGTH)}.png"
    log(name)
    val tags = tagsFor(context, strings, log)
    if (tags.isEmpty()) log("  no strings, not uploaded")
    if (tags.isEmpty() || api == null) return null

    val existing = managed.firstOrNull { it.name == name }
    val screenshot = when {
        existing == null -> api.createScreenshot(name, bytes).also {
            api.replaceTags(it.id, tags)
            log("  uploaded")
        }
        existing.tags != null && existing.tags.toSet() == tags.toSet() -> existing.also { log("  up to date") }
        else -> existing.also {
            api.replaceTags(it.id, tags)
            log("  tags updated")
        }
    }
    return screenshot.id
}

private fun sha256(bytes: ByteArray): String =
    MessageDigest.getInstance("SHA-256").digest(bytes).joinToString("") { "%02x".format(it) }
