package glass.yasan.kepko.persistence.internal.legacy

import androidx.datastore.core.DataMigration
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CancellationException

/**
 * Moves Kepko's keys from [source] into DataStore on the first launch, then deletes them from [source].
 *
 * Best effort: it runs once, and whatever it can't read or delete is given up, since losing a theme
 * preference isn't worth failing the load for. Keys it doesn't know are left alone.
 */
internal class LegacySettingsMigration(
    private val source: LegacySettingsSource,
) : DataMigration<Preferences> {

    private companion object {

        // Underscores keep it apart from the dotted keys multiplatform-settings stored.
        const val MIGRATED_KEY = "glass_yasan_kepko.legacy_migrated"

        const val STYLE_PREFIX = "glass.yasan.kepko.style."
        const val GRAYSCALE_PREFIX = "glass.yasan.kepko.grayscale"
        const val DIMENSION_PREFIX = "glass.yasan.kepko.dimension."
    }

    private val migratedKey = booleanPreferencesKey(MIGRATED_KEY)

    private var legacyKeys: Set<String> = emptySet()

    override suspend fun shouldMigrate(currentData: Preferences): Boolean = currentData[migratedKey] != true

    // Catches Throwable: browser storage errors aren't Exceptions on web.
    override suspend fun migrate(currentData: Preferences): Preferences =
        currentData.toMutablePreferences().apply {
            try {
                legacyKeys = source.keys().filterTo(mutableSetOf()) { it.isLegacyKey() }
                legacyKeys.forEach { key -> copy(key) }
            } catch (e: CancellationException) {
                throw e
            } catch (_: Throwable) {
                // Given up: the app starts from whatever was copied.
            }
            this[migratedKey] = true
        }.toPreferences()

    override suspend fun cleanUp() {
        try {
            source.remove(legacyKeys)
        } catch (e: CancellationException) {
            throw e
        } catch (_: Throwable) {
            // The keys stay behind, where nothing reads them again.
        }
    }

    private fun String.isLegacyKey(): Boolean =
        startsWith(STYLE_PREFIX) || startsWith(GRAYSCALE_PREFIX) || startsWith(DIMENSION_PREFIX)

    private fun MutablePreferences.copy(key: String) {
        when {
            key.startsWith(STYLE_PREFIX) ->
                source.getString(key)?.let { this[stringPreferencesKey(key)] = it }

            key.startsWith(GRAYSCALE_PREFIX) ->
                source.getBoolean(key)?.let { this[booleanPreferencesKey(key)] = it }

            key.startsWith(DIMENSION_PREFIX) ->
                source.getFloat(key)?.let { this[floatPreferencesKey(key)] = it }
        }
    }
}
