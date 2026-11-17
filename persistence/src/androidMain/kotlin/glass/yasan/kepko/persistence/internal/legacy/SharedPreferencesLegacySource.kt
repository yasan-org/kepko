package glass.yasan.kepko.persistence.internal.legacy

import android.content.SharedPreferences

/**
 * [openPreferences] runs on first use, so the file isn't loaded once the migration is done.
 */
internal class SharedPreferencesLegacySource(
    openPreferences: () -> SharedPreferences,
) : LegacySettingsSource {

    private val preferences: SharedPreferences by lazy(openPreferences)

    // getAll() copies the whole file's map, so it's read once.
    private val all: Map<String, *> by lazy { preferences.all }

    override fun keys(): Set<String> = all.keys

    override fun getString(key: String): String? = all[key] as? String

    override fun getBoolean(key: String): Boolean? = all[key] as? Boolean

    override fun getFloat(key: String): Float? = all[key] as? Float

    override fun remove(keys: Set<String>) {
        val editor = preferences.edit()
        keys.forEach { editor.remove(it) }
        editor.apply()
    }
}
