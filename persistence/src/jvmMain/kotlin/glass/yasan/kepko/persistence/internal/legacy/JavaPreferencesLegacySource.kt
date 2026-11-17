package glass.yasan.kepko.persistence.internal.legacy

import java.util.prefs.Preferences

/**
 * [openPreferences] runs on first use, so the preferences aren't opened once the migration is done.
 */
internal class JavaPreferencesLegacySource(
    openPreferences: () -> Preferences,
) : TextLegacySettingsSource() {

    private val preferences: Preferences by lazy(openPreferences)

    override fun keys(): Set<String> = preferences.keys().toSet()

    override fun getText(key: String): String? = preferences.get(key, null)

    override fun remove(keys: Set<String>) {
        keys.forEach { preferences.remove(it) }
        preferences.flush()
    }
}
