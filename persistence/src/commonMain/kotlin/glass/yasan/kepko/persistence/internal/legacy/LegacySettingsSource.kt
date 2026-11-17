package glass.yasan.kepko.persistence.internal.legacy

/**
 * The platform store multiplatform-settings wrote to. It may also hold the host app's own keys.
 */
internal interface LegacySettingsSource {

    fun keys(): Set<String>

    fun getString(key: String): String?

    fun getBoolean(key: String): Boolean?

    fun getFloat(key: String): Float?

    fun remove(keys: Set<String>)
}
