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
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlin.time.Duration.Companion.seconds

@InternalKepkoApi
public object SingletonPersistenceManager {

    private val loader = MutableStateFlow(Loader(::createPlatformDataStore))

    public suspend fun awaitInstance(): PersistenceManager = loader.value.awaitInstance()

    /**
     * Loads the instance if needed, or returns null on web until [awaitInstance] has loaded it.
     */
    public fun instanceOrBlockingLoad(): PersistenceManager? = loader.value.instanceOrBlockingLoad()

    /**
     * Starts loading the preferences in the background, so the first composition has less to wait for.
     */
    internal fun preload() {
        loader.value.preload()
    }

    /**
     * Drops the loaded instance and loads the next one from [createDataStore], so a test never touches the
     * device's real preferences, whatever ran before it. Call it without arguments to restore the default.
     */
    internal fun resetForTesting(createDataStore: () -> DataStore<Preferences> = ::createPlatformDataStore) {
        loader.getAndUpdate { Loader(createDataStore) }.close()
    }
}

private class Loader(
    private val createDataStore: () -> DataStore<Preferences>,
) {

    private companion object {
        val EXIT_FLUSH_TIMEOUT = 2.seconds
    }

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val store: Deferred<PreferencesStore> = scope.async(start = CoroutineStart.LAZY) {
        PreferencesStore.load(dataStore = createDataStore(), scope = scope).also { store ->
            flushOnExit { store.flush(EXIT_FLUSH_TIMEOUT) }
        }
    }

    private val instance = MutableStateFlow<PersistenceManager?>(null)

    suspend fun awaitInstance(): PersistenceManager = instance.value ?: create(store.await())

    fun instanceOrBlockingLoad(): PersistenceManager? =
        instance.value ?: blockingOrNull { store.await() }?.let(::create)

    fun preload() {
        store.start()
    }

    fun close() {
        scope.cancel()
    }

    // Runs on the caller's thread: a composition can't read state created by another thread after it started.
    private fun create(store: PreferencesStore): PersistenceManager {
        instance.compareAndSet(expect = null, update = PersistenceManagerImpl(store))
        return checkNotNull(instance.value)
    }
}
