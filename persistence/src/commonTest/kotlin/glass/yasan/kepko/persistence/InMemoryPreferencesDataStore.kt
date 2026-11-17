package glass.yasan.kepko.persistence

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

internal class InMemoryPreferencesDataStore(
    initial: Preferences = emptyPreferences(),
) : DataStore<Preferences> {

    private val state = MutableStateFlow(initial)

    val current: Preferences
        get() = state.value

    var failReads: Boolean = false
    var failWrites: Boolean = false

    override val data: Flow<Preferences> = flow {
        check(!failReads) { "Unreadable" }
        emitAll(state)
    }

    override suspend fun updateData(transform: suspend (t: Preferences) -> Preferences): Preferences {
        check(!failWrites) { "Disk full" }

        return transform(state.value).also { state.value = it }
    }
}
