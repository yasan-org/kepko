package glass.yasan.kepko.persistence

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.v2.runDesktopComposeUiTest
import glass.yasan.kepko.foundation.annotation.ExperimentalKepkoApi
import glass.yasan.kepko.persistence.internal.SingletonPersistenceManager
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertSame

@OptIn(ExperimentalTestApi::class, ExperimentalKepkoApi::class)
internal class SingletonPersistenceManagerTest {

    init {
        SingletonPersistenceManager.createDataStore = { InMemoryPreferencesDataStore() }
    }

    @Test
    fun givenDefaultPersistentTheme_whenReadingManager_thenReturnsTheSingletonInstance() {
        // Given
        var current: PersistenceManager? = null

        // When
        runDesktopComposeUiTest {
            setContent {
                PersistentKepkoTheme {
                    current = LocalKepkoPersistenceManager.current
                }
            }
            waitForIdle()
        }

        // Then
        assertNotNull(current)
        assertSame(SingletonPersistenceManager.instanceOrBlockingLoad(), current)
    }

    @Test
    fun givenPreloaded_whenAwaited_thenReturnsTheSameInstanceEveryTime(): Unit = runBlocking {
        // When
        SingletonPersistenceManager.preload()
        val awaited = SingletonPersistenceManager.awaitInstance()

        // Then
        assertSame(awaited, SingletonPersistenceManager.instanceOrBlockingLoad())
        assertSame(awaited, SingletonPersistenceManager.awaitInstance())
    }
}
