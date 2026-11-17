package glass.yasan.kepko.persistence.internal.legacy

/**
 * A [LegacySettingsSource] that stores every value as text.
 */
internal abstract class TextLegacySettingsSource : LegacySettingsSource {

    abstract fun getText(key: String): String?

    override fun getString(key: String): String? = getText(key)

    override fun getBoolean(key: String): Boolean? = getText(key)?.toBooleanStrictOrNull()

    override fun getFloat(key: String): Float? = getText(key)?.toFloatOrNull()
}
