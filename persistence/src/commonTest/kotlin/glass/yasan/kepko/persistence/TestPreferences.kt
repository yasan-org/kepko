package glass.yasan.kepko.persistence

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import glass.yasan.kepko.persistence.internal.PreferencesStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

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
