package glass.yasan.kepko.persistence.internal.legacy

import platform.Foundation.NSNumber
import platform.Foundation.NSUserDefaults

internal class UserDefaultsLegacySource(
    private val defaults: NSUserDefaults,
) : LegacySettingsSource {

    override fun keys(): Set<String> = defaults.dictionaryRepresentation().keys.filterIsInstance<String>().toSet()

    // Reads the stored object itself: boolForKey and floatForKey turn a value of another type into false or 0.
    override fun getString(key: String): String? = defaults.objectForKey(key) as? String

    override fun getBoolean(key: String): Boolean? = (defaults.objectForKey(key) as? NSNumber)?.boolValue

    override fun getFloat(key: String): Float? = (defaults.objectForKey(key) as? NSNumber)?.floatValue

    override fun remove(keys: Set<String>) {
        keys.forEach { defaults.removeObjectForKey(it) }
    }
}
