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
            changes = emptyMap(),
            edits = 0,
        ),
    )

    init {
        scope.launch {
            var retryDelay = INITIAL_RETRY_DELAY

            while (true) {
                val pending = state.first { it.changes.isNotEmpty() }

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
        withTimeoutOrNull(timeout) { state.first { it.changes.isEmpty() } } != null

    private fun edit(key: String, value: Any?) {
        state.update { current ->
            State(
                values = if (value == null) current.values - key else current.values + (key to value),
                changes = current.changes + (key to current.edits),
                edits = current.edits + 1,
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
     * Forgets the edits [written] wrote, keeping any key edited again meanwhile, even back to the same value.
     * A written key isn't written again, so it can't overwrite a newer value from another tab or process.
     */
    private fun markPersisted(written: State) {
        state.update { current ->
            State(
                values = current.values,
                changes = current.changes.filter { (key, edit) -> written.changes[key] != edit },
                edits = current.edits,
            )
        }
    }

    /**
     * Only the keys in [changes] are written, so a store that failed to load can't overwrite the values it never
     * read. Each maps to the number of the edit that last changed it, counted by [edits].
     */
    private class State(
        val values: Map<String, Any>,
        val changes: Map<String, Long>,
        val edits: Long,
    )

    private fun Preferences.withChangesFrom(pending: State): Preferences =
        toMutablePreferences().apply {
            pending.changes.keys.forEach { name ->
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
