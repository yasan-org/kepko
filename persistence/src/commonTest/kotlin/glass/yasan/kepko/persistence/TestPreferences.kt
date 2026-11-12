package glass.yasan.kepko.persistence

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import glass.yasan.kepko.persistence.internal.PreferencesStore
import glass.yasan.kepko.persistence.internal.TextLegacySettingsSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

/**
 * Builds [Preferences] from String, Boolean and Float values.
 */
internal fun testPreferences(vararg values: Pair<String, Any>): Preferences =
    emptyPreferences().toMutablePreferences().apply {
        values.forEach { (key, value) ->
            when (value) {
                is String -> this[stringPreferencesKey(key)] = value
                is Boolean -> this[booleanPreferencesKey(key)] = value
                is Float -> this[floatPreferencesKey(key)] = value
                else -> error("Unsupported test preference type: ${value::class}")
            }
        }
    }.toPreferences()

/**
 * A [PreferencesStore] holding [values], backed by an [InMemoryPreferencesDataStore] that is written immediately.
 */
internal fun testPreferencesStore(vararg values: Pair<String, Any>): PreferencesStore {
    val initial = testPreferences(*values)

    return PreferencesStore(
        dataStore = InMemoryPreferencesDataStore(initial),
        initial = initial,
        scope = CoroutineScope(Dispatchers.Unconfined),
    )
}

internal class InMemoryPreferencesDataStore(
    initial: Preferences = emptyPreferences(),
) : DataStore<Preferences> {

    private val state = MutableStateFlow(initial)

    val current: Preferences
        get() = state.value

    /**
     * How many times the stored value changed. Like DataStore, an update to an equal value isn't a write.
     */
    var writes: Int = 0
        private set

    var failReads: Boolean = false
    var failWrites: Boolean = false

    override val data: Flow<Preferences> = flow {
        check(!failReads) { "Unreadable" }
        emitAll(state)
    }

    /**
     * Runs right after a write is stored, before [updateData] returns to its caller.
     */
    var afterWrite: suspend () -> Unit = {}

    override suspend fun updateData(transform: suspend (t: Preferences) -> Preferences): Preferences {
        check(!failWrites) { "Disk full" }

        val updated = transform(state.value)
        if (updated != state.value) {
            state.value = updated
            writes++
        }
        afterWrite()

        return updated
    }
}

internal class FakeLegacySettingsSource(
    vararg values: Pair<String, String>,
) : TextLegacySettingsSource() {

    val values: MutableMap<String, String> = mutableMapOf(*values)

    /**
     * How many times the keys were listed.
     */
    var keyReads: Int = 0
        private set

    var failKeys: Boolean = false
    var failRemovals: Boolean = false

    override fun keys(): Set<String> {
        check(!failKeys) { "Unlistable" }
        keyReads++
        return values.keys.toSet()
    }

    override fun getText(key: String): String? = values[key]

    override fun remove(keys: Set<String>) {
        check(!failRemovals) { "Unremovable" }
        values -= keys
    }
}
