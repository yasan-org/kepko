package glass.yasan.crowdin.screenshots

private val placeholder = Regex("""%(\d+\$)?[-#+ 0,(]*\d*(\.\d+)?[sdfx]""")
private val androidEscape = Regex("""\\(u[0-9a-fA-F]{4}|.)""")
private val whitespace = Regex("""\s+""")

/** Resolves Android string escapes (`\n`, `\'`, `…`, ...) and surrounding quotes. */
internal fun unescape(source: String): String {
    val unquoted = if (source.length >= 2 && source.startsWith('"') && source.endsWith('"')) {
        source.substring(1, source.length - 1)
    } else {
        source
    }
    return androidEscape.replace(unquoted) { match ->
        val escape = match.groupValues[1]
        when {
            escape.length == UNICODE_ESCAPE_LENGTH && escape.startsWith('u') ->
                escape.drop(1).toInt(HEX).toChar().toString()
            escape == "n" -> "\n"
            escape == "t" -> "\t"
            else -> escape
        }
    }
}

internal fun normalize(text: String): String = text.trim().split(whitespace).joinToString(" ").lowercase()

/**
 * Returns a predicate for normalized on-screen texts showing [source], ignoring case and matching
 * any text for format placeholders such as `%1$s`, or `null` if [source] has no literal text.
 */
internal fun textMatcher(source: String): ((String) -> Boolean)? {
    val literals = normalize(unescape(source)).split(placeholder)
    if (literals.none { it.isNotBlank() }) return null
    if (literals.size == 1) return { text -> text == literals.single() }
    val pattern = Regex(literals.joinToString(".+") { Regex.escape(it) })
    return { text -> pattern.matches(text) }
}

/** Tags every key the preview read, on each box showing its text, or without a position. */
internal fun tagsFor(
    context: ScreenshotContext,
    strings: Map<String, SourceString>,
    log: (String) -> Unit,
): List<Tag> {
    val texts = context.texts.map { normalize(it.text) to it }
    val tags = mutableListOf<Tag>()
    for (key in context.keys) {
        val string = strings[key]
        if (string == null) {
            log("  $key: not in Crowdin yet, skipped")
            continue
        }
        val matches = textMatcher(string.text)
        val boxes = if (matches == null) emptyList() else texts.filter { (text, _) -> matches(text) }.map { it.second }
        if (boxes.isEmpty()) {
            tags += Tag(stringId = string.id)
            log("  $key: no box, tagged without position")
        } else {
            boxes.map { Tag(stringId = string.id, position = it.position) }.forEach { if (it !in tags) tags += it }
            log("  $key: ${boxes.size} box(es)")
        }
    }
    return tags
}

private const val UNICODE_ESCAPE_LENGTH = 5
private const val HEX = 16
