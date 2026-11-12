package glass.yasan.kepko.persistence.internal

import androidx.datastore.core.DataMigration
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CancellationException

/**
 * Prefix of every key Kepko stored with multiplatform-settings.
 */
internal const val LEGACY_KEY_PREFIX: String = "glass.yasan.kepko."

/**
 * Set once the legacy store has been migrated, so later launches don't scan it again. Underscores keep it
 * outside [LEGACY_KEY_PREFIX].
 */
internal const val LEGACY_MIGRATED_KEY: String = "glass_yasan_kepko.legacy_migrated"

private const val LEGACY_STYLE_PREFIX = "${LEGACY_KEY_PREFIX}style."
private const val LEGACY_GRAYSCALE_PREFIX = "${LEGACY_KEY_PREFIX}grayscale"
private const val LEGACY_DIMENSION_PREFIX = "${LEGACY_KEY_PREFIX}dimension."

/**
 * The platform store multiplatform-settings wrote to. It may also hold the host app's own keys.
 */
internal interface LegacySettingsSource {

    /**
     * Throws if the store can't be listed, so the migration is retried on a later launch instead of being
     * recorded as done with nothing copied.
     */
    fun keys(): Set<String>

    fun getString(key: String): String?

    fun getBoolean(key: String): Boolean?

    fun getFloat(key: String): Float?

    fun remove(keys: Set<String>)
}

/**
 * A [LegacySettingsSource] that stores every value as text.
 */
internal abstract class TextLegacySettingsSource : LegacySettingsSource {

    abstract fun getText(key: String): String?

    override fun getString(key: String): String? = getText(key)

    override fun getBoolean(key: String): Boolean? = getText(key)?.toBooleanStrictOrNull()

    override fun getFloat(key: String): Float? = getText(key)?.toFloatOrNull()
}

/**
 * Moves Kepko's keys from [source] into DataStore, then deletes them from [source]. Runs once: afterwards
 * [LEGACY_MIGRATED_KEY] is set and [source] isn't read again.
 *
 * Values already in DataStore win, unparseable values are dropped, and keys without [LEGACY_KEY_PREFIX]
 * are left alone.
 */
internal class LegacySettingsMigration(
    private val source: LegacySettingsSource,
) : DataMigration<Preferences> {

    override suspend fun shouldMigrate(currentData: Preferences): Boolean = currentData[migratedKey] != true

    override suspend fun migrate(currentData: Preferences): Preferences {
        val existing = currentData.asMap().keys.mapTo(mutableSetOf()) { it.name }
        val keys = legacyKeys()
        migratedKeys = keys

        return currentData.toMutablePreferences().apply {
            keys
                .filterNot { it in existing }
                .forEach { key -> copy(key) }
            this[migratedKey] = true
        }.toPreferences()
    }

    /**
     * Removes only the keys [migrate] saw, so a key another tab or app adds meanwhile isn't deleted unread.
     * A failure leaves the keys behind rather than failing the load: they are already in DataStore.
     */
    @Suppress("TooGenericExceptionCaught") // Browser storage errors aren't Exceptions on web.
    override suspend fun cleanUp() {
        try {
            source.remove(migratedKeys)
        } catch (e: CancellationException) {
            throw e
        } catch (_: Throwable) {
            // Left behind; never read again once the migrated marker is set.
        }
    }

    private var migratedKeys: Set<String> = emptySet()

    private val migratedKey = booleanPreferencesKey(LEGACY_MIGRATED_KEY)

    private fun legacyKeys(): Set<String> = source.keys().filterTo(mutableSetOf()) { it.startsWith(LEGACY_KEY_PREFIX) }

    private fun MutablePreferences.copy(key: String) {
        when {
            key.startsWith(LEGACY_STYLE_PREFIX) ->
                source.getString(key)?.let { this[stringPreferencesKey(key)] = it }

            key.startsWith(LEGACY_GRAYSCALE_PREFIX) ->
                source.getBoolean(key)?.let { this[booleanPreferencesKey(key)] = it }

            key.startsWith(LEGACY_DIMENSION_PREFIX) ->
                source.getFloat(key)?.let { this[floatPreferencesKey(key)] = it }
        }
    }
}
