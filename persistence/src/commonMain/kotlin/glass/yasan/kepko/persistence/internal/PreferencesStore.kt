package glass.yasan.kepko.persistence.internal

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Synchronous access to a [DataStore]: reads come from an in-memory copy, and each change is written in the
 * background, in order. A write that fails is given up; the value stays in memory until the app exits.
 *
 * Failures are caught as Throwable: browser storage errors aren't Exceptions on web.
 */
internal class PreferencesStore(
    private val dataStore: DataStore<Preferences>,
    initial: Preferences,
    scope: CoroutineScope,
) {

    internal companion object {

        /**
         * Reads [dataStore] once, starting empty if it can't be read.
         */
        suspend fun load(
            dataStore: DataStore<Preferences>,
            scope: CoroutineScope,
        ): PreferencesStore {
            val initial = try {
                dataStore.data.first()
            } catch (e: CancellationException) {
                throw e
            } catch (_: Throwable) {
                emptyPreferences()
            }

            return PreferencesStore(dataStore = dataStore, initial = initial, scope = scope)
        }
    }

    private val values = MutableStateFlow<Map<String, Any>>(initial.asMap().mapKeys { (key, _) -> key.name })

    private val changes = Channel<Pair<String, Any?>>(Channel.UNLIMITED)

    init {
        scope.launch {
            for ((key, value) in changes) write(key, value)
        }
    }

    fun getStringOrNull(key: String): String? = values.value[key] as? String

    fun getBooleanOrNull(key: String): Boolean? = values.value[key] as? Boolean

    fun getFloatOrNull(key: String): Float? = values.value[key] as? Float

    fun putString(key: String, value: String) {
        edit(key, value)
    }

    fun putBoolean(key: String, value: Boolean) {
        edit(key, value)
    }

    fun putFloat(key: String, value: Float) {
        edit(key, value)
    }

    fun remove(key: String) {
        edit(key, value = null)
    }

    private fun edit(key: String, value: Any?) {
        values.update { if (value == null) it - key else it + (key to value) }
        changes.trySend(key to value)
    }

    private suspend fun write(name: String, value: Any?) {
        try {
            dataStore.updateData { stored ->
                stored.toMutablePreferences().apply {
                    // Drops the entry whatever type it was stored as.
                    asMap().keys.filter { it.name == name }.forEach { this -= it }
                    when (value) {
                        is String -> this[stringPreferencesKey(name)] = value
                        is Boolean -> this[booleanPreferencesKey(name)] = value
                        is Float -> this[floatPreferencesKey(name)] = value
                    }
                }.toPreferences()
            }
        } catch (e: CancellationException) {
            throw e
        } catch (_: Throwable) {
            // Given up: the value stays in memory.
        }
    }
}
