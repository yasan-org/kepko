package glass.yasan.kepko.persistence.internal

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * Synchronous access to a [DataStore]: reads come from an in-memory snapshot, and changed keys are written in
 * the background, with rapid changes collapsed into one write. A failed write is retried.
 */
internal class PreferencesStore(
    private val dataStore: DataStore<Preferences>,
    initial: Preferences,
    scope: CoroutineScope,
) {

    internal companion object {

        private val INITIAL_RETRY_DELAY = 1.seconds
        private val MAX_RETRY_DELAY = 30.seconds

        /**
         * Reads [dataStore] once, starting empty if it can't be read.
         */
        @Suppress("TooGenericExceptionCaught") // Browser storage errors aren't Exceptions on web.
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

    private val state = MutableStateFlow(
        State(
            values = initial.asMap().mapKeys { (key, _) -> key.name },
            changedKeys = emptySet(),
        ),
    )

    init {
        scope.launch {
            var retryDelay = INITIAL_RETRY_DELAY

            while (true) {
                val pending = state.first { it.changedKeys.isNotEmpty() }

                if (persist(pending)) {
                    markPersisted(pending)
                    retryDelay = INITIAL_RETRY_DELAY
                } else {
                    // Retry after a pause, or as soon as something else changes.
                    withTimeoutOrNull(retryDelay) { state.first { it !== pending } }
                    retryDelay = (retryDelay * 2).coerceAtMost(MAX_RETRY_DELAY)
                }
            }
        }
    }

    fun getStringOrNull(key: String): String? = state.value.values[key] as? String

    fun getBooleanOrNull(key: String): Boolean? = state.value.values[key] as? Boolean

    fun getFloatOrNull(key: String): Float? = state.value.values[key] as? Float

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

    /**
     * Suspends until every change has been written, or returns false after [timeout].
     */
    suspend fun flush(timeout: Duration): Boolean =
        withTimeoutOrNull(timeout) { state.first { it.changedKeys.isEmpty() } } != null

    private fun edit(key: String, value: Any?) {
        state.update { current ->
            State(
                values = if (value == null) current.values - key else current.values + (key to value),
                changedKeys = current.changedKeys + key,
            )
        }
    }

    @Suppress("TooGenericExceptionCaught") // Browser storage errors aren't Exceptions on web.
    private suspend fun persist(pending: State): Boolean =
        try {
            dataStore.updateData { stored -> stored.withChangesFrom(pending) }
            true
        } catch (e: CancellationException) {
            throw e
        } catch (_: Throwable) {
            false
        }

    /**
     * Forgets the keys [written] wrote, unless they changed again meanwhile. A written key isn't written again,
     * so it can't overwrite a newer value from another tab or process.
     */
    private fun markPersisted(written: State) {
        state.update { current ->
            val stillChanged = current.changedKeys.filterTo(mutableSetOf()) { key ->
                key !in written.changedKeys || current.values[key] != written.values[key]
            }
            State(values = current.values, changedKeys = stillChanged)
        }
    }

    /**
     * Only [changedKeys] are written, so a store that failed to load can't overwrite the values it never read.
     */
    private class State(
        val values: Map<String, Any>,
        val changedKeys: Set<String>,
    )

    private fun Preferences.withChangesFrom(pending: State): Preferences =
        toMutablePreferences().apply {
            pending.changedKeys.forEach { name ->
                // Drops the entry whatever type it was stored as.
                asMap().keys.filter { it.name == name }.forEach { this -= it }
                pending.values[name]?.let { put(name, it) }
            }
        }.toPreferences()

    private fun MutablePreferences.put(name: String, value: Any) {
        when (value) {
            is String -> this[stringPreferencesKey(name)] = value
            is Boolean -> this[booleanPreferencesKey(name)] = value
            is Float -> this[floatPreferencesKey(name)] = value
        }
    }
}
