package glass.yasan.kepko.persistence.internal

import androidx.datastore.core.DataMigration
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CancellationException

/**
 * Prefix of every key Kepko stored with multiplatform-settings.
 */
internal const val LEGACY_KEY_PREFIX: String = "glass.yasan.kepko."

/**
 * How far the legacy migration has got: [LEGACY_COPIED] or [LEGACY_CLEAN]. Underscores keep it outside
 * [LEGACY_KEY_PREFIX].
 */
internal const val LEGACY_MIGRATION_STATE_KEY: String = "glass_yasan_kepko.legacy_migration_state"

/**
 * The legacy values are in DataStore, but their keys may still be in the legacy store.
 */
internal const val LEGACY_COPIED: Int = 1

/**
 * A launch found no legacy keys left, so the legacy store isn't read again.
 */
internal const val LEGACY_CLEAN: Int = 2

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
 * Moves Kepko's keys from [source] into DataStore, then deletes them from [source]. The first launch copies
 * and cleans up; later launches only retry the clean-up, until one finds no legacy keys left. After that
 * [source] isn't read again.
 *
 * Values already in DataStore win and unparseable values are dropped. Keys this migration doesn't know,
 * with or without [LEGACY_KEY_PREFIX], are left alone.
 */
internal class LegacySettingsMigration(
    private val source: LegacySettingsSource,
) : DataMigration<Preferences> {

    override suspend fun shouldMigrate(currentData: Preferences): Boolean =
        state(currentData) < LEGACY_CLEAN

    override suspend fun migrate(currentData: Preferences): Preferences {
        val existing = currentData.asMap().keys.mapTo(mutableSetOf()) { it.name }
        val keys = legacyKeys()
        migratedKeys = keys

        return currentData.toMutablePreferences().apply {
            // Copies once: copying again would bring back a value the user has since cleared.
            if (state(currentData) < LEGACY_COPIED) {
                keys
                    .filterNot { it in existing }
                    .forEach { key -> copy(key) }
                this[stateKey] = LEGACY_COPIED
            } else if (keys.isEmpty()) {
                this[stateKey] = LEGACY_CLEAN
            }
        }.toPreferences()
    }

    /**
     * Removes only the keys [migrate] saw, so a key another tab or app adds meanwhile isn't deleted unread.
     * A failure doesn't fail the load, since the keys are already in DataStore; the next launch retries.
     */
    @Suppress("TooGenericExceptionCaught") // Browser storage errors aren't Exceptions on web.
    override suspend fun cleanUp() {
        try {
            source.remove(migratedKeys)
        } catch (e: CancellationException) {
            throw e
        } catch (_: Throwable) {
            // Retried on the next launch.
        }
    }

    private var migratedKeys: Set<String> = emptySet()

    private val stateKey = intPreferencesKey(LEGACY_MIGRATION_STATE_KEY)

    private fun state(data: Preferences): Int = data[stateKey] ?: 0

    private fun legacyKeys(): Set<String> = source.keys().filterTo(mutableSetOf()) { it.isLegacyKey() }

    private fun String.isLegacyKey(): Boolean =
        startsWith(LEGACY_STYLE_PREFIX) || startsWith(LEGACY_GRAYSCALE_PREFIX) || startsWith(LEGACY_DIMENSION_PREFIX)

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
