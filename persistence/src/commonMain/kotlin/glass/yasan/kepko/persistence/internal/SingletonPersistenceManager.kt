package glass.yasan.kepko.persistence.internal

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import glass.yasan.kepko.foundation.annotation.InternalKepkoApi
import glass.yasan.kepko.persistence.PersistenceManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.concurrent.Volatile

@InternalKepkoApi
public object SingletonPersistenceManager {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    /**
     * Replaced by tests before the first load, so they never touch the device's real preferences.
     */
    @Volatile
    internal var createDataStore: () -> DataStore<Preferences> = KepkoDataStore::create

    private val store: Deferred<PreferencesStore> = scope.async(start = CoroutineStart.LAZY) {
        PreferencesStore.load(dataStore = createDataStore(), scope = scope)
    }

    private val instance = MutableStateFlow<PersistenceManager?>(null)

    public suspend fun awaitInstance(): PersistenceManager = instance.value ?: create(store.await())

    /**
     * Loads the instance if needed, or returns null on web until [awaitInstance] has loaded it.
     */
    public fun instanceOrBlockingLoad(): PersistenceManager? =
        instance.value ?: blockingOrNull { store.await() }?.let(::create)

    /**
     * Starts loading the preferences in the background, so the first composition has less to wait for.
     */
    internal fun preload() {
        store.start()
    }

    // Runs on the caller's thread: a composition can't read state created by another thread after it started.
    private fun create(store: PreferencesStore): PersistenceManager {
        instance.compareAndSet(expect = null, update = PersistenceManagerImpl(store))
        return checkNotNull(instance.value)
    }
}
