package glass.yasan.kepko.buildlogic.icons

import org.gradle.api.GradleException
import org.snakeyaml.engine.v2.api.Load
import org.snakeyaml.engine.v2.api.LoadSettings
import java.io.File

internal enum class IconStyle(val key: String) {
    OUTLINED("outlined"),
    ROUNDED("rounded"),
    SHARP("sharp"),
}

internal data class Icon(
    /** Kepko name: drawable `ic_<name>`, `Icons` property and `NamedIcon` id. */
    val name: String,
    /** Material Symbols name. */
    val symbol: String,
    val style: IconStyle,
    val fill: Boolean,
    val weight: Int,
    val grade: Int,
    val size: Int,
) {
    val fileName: String get() = "ic_$name.xml"

    val resourceName: String get() = "ic_$name"

    val propertyName: String get() = snakeToCamelCase(name)

    val constantName: String get() = name.uppercase()

    val url: String
        get() {
            val variant = buildString {
                if (weight != DEFAULT_WEIGHT) append("wght$weight")
                append(GRADES.getValue(grade))
                if (fill) append("fill1")
            }.ifEmpty { "default" }
            return "https://fonts.gstatic.com/s/i/short-term/release/" +
                "materialsymbols${style.key}/$symbol/$variant/${size}px.svg"
        }

    companion object {
        const val DEFAULT_WEIGHT: Int = 400
        val WEIGHTS: Set<Int> = setOf(100, 200, 300, 400, 500, 600, 700)
        val GRADES: Map<Int, String> = mapOf(-25 to "gradN25", 0 to "", 200 to "grad200")
        val SIZES: Set<Int> = setOf(20, 24, 40, 48)

        fun snakeToCamelCase(name: String): String =
            name.replace(Regex("_([a-z0-9])")) { it.groupValues[1].uppercase() }
    }
}

/**
 * Reads the icon manifest:
 *
 * ```yaml
 * defaults:
 *   style: rounded
 *   fill: true
 *   weight: 400
 *   grade: 0
 *   size: 24
 * icons:
 *   - add
 *   - name: lightbulb
 *     symbol: lightbulb_2
 * ```
 */
internal object IconManifest {

    /** Kepko names become Kotlin identifiers and resource names, so they must start with a letter. */
    private val namePattern = Regex("^[a-z][a-z0-9_]*$")

    /** Material Symbols names may start with a digit, e.g. `3d_rotation` or `360`. */
    private val symbolPattern = Regex("^[a-z0-9][a-z0-9_]*$")

    private val topLevelKeys = setOf("defaults", "icons")
    private val settingKeys = setOf("style", "fill", "weight", "grade", "size")
    private val iconKeys = settingKeys + setOf("name", "symbol")

    fun read(file: File): List<Icon> {
        val settings = LoadSettings.builder().setAllowDuplicateKeys(false).build()
        val root = try {
            Load(settings).loadFromString(file.readText())
        } catch (e: RuntimeException) {
            fail(file, "invalid YAML: ${e.message}")
        } as? Map<*, *> ?: fail(file, "expected a mapping at the top level")

        (root.keys - topLevelKeys).firstOrNull()?.let {
            fail(file, "unknown top-level key '$it', expected one of $topLevelKeys")
        }

        val defaults = mutableMapOf<String, Any?>(
            "style" to IconStyle.ROUNDED.key,
            "fill" to true,
            "weight" to Icon.DEFAULT_WEIGHT,
            "grade" to 0,
            "size" to 24,
        )
        when (val declared = root["defaults"]) {
            null -> Unit
            is Map<*, *> -> declared.forEach { (key, value) ->
                if (key !in settingKeys) fail(file, "unknown default '$key', expected one of $settingKeys")
                defaults[key as String] = value
            }
            else -> fail(file, "'defaults' must be a mapping")
        }

        val entries = root["icons"] as? List<*> ?: fail(file, "'icons' must be a list")
        val icons = entries.map { entry -> parseIcon(file, entry, defaults) }

        icons.groupBy { it.name }.filterValues { it.size > 1 }.keys.firstOrNull()?.let {
            fail(file, "'$it' is listed more than once")
        }
        icons.groupBy { it.propertyName }.filterValues { it.size > 1 }.entries.firstOrNull()?.let { (property, clash) ->
            fail(file, "${clash.joinToString { "'${it.name}'" }} all map to the property '$property'")
        }
        return icons
    }

    @Suppress("CyclomaticComplexMethod")
    private fun parseIcon(file: File, entry: Any?, defaults: Map<String, Any?>): Icon {
        val fields: Map<*, *> = when (entry) {
            is String -> mapOf("name" to entry)
            is Map<*, *> -> entry
            else -> fail(file, "invalid icon entry '$entry', expected a name or a mapping")
        }
        val name = fields["name"] as? String ?: fail(file, "icon entry without a string name: $entry")
        (fields.keys - iconKeys).firstOrNull()?.let { fail(file, "$name: unknown key '$it'") }
        fun setting(key: String): Any? = if (fields.containsKey(key)) fields[key] else defaults[key]

        val symbol = when (val value = fields["symbol"]) {
            null -> name
            is String -> value
            // YAML reads symbols such as `360` as numbers.
            is Number -> value.toString()
            else -> fail(file, "$name: symbol must be a string")
        }
        if (!namePattern.matches(name)) {
            fail(file, "$name: names must be lowercase snake_case starting with a letter")
        }
        if (!symbolPattern.matches(symbol)) fail(file, "$name: invalid symbol '$symbol'")

        val style = IconStyle.entries.firstOrNull { it.key == setting("style") }
            ?: fail(file, "$name: style must be one of ${IconStyle.entries.map { it.key }}")
        val fill = setting("fill") as? Boolean ?: fail(file, "$name: fill must be true or false")
        val weight = (setting("weight") as? Int)?.takeIf { it in Icon.WEIGHTS }
            ?: fail(file, "$name: weight must be one of ${Icon.WEIGHTS}")
        val grade = (setting("grade") as? Int)?.takeIf { it in Icon.GRADES }
            ?: fail(file, "$name: grade must be one of ${Icon.GRADES.keys}")
        val size = (setting("size") as? Int)?.takeIf { it in Icon.SIZES }
            ?: fail(file, "$name: size must be one of ${Icon.SIZES}")

        return Icon(name, symbol, style, fill, weight, grade, size)
    }

    private fun fail(file: File, message: String): Nothing = throw GradleException("${file.name}: $message")
}
