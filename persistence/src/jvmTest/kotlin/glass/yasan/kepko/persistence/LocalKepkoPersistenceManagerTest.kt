package glass.yasan.kepko.persistence

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.v2.runDesktopComposeUiTest
import glass.yasan.kepko.foundation.annotation.ExperimentalKepkoApi
import glass.yasan.kepko.persistence.internal.PersistenceManagerImpl
import glass.yasan.kepko.persistence.internal.SingletonPersistenceManager
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertSame

@OptIn(ExperimentalTestApi::class, ExperimentalKepkoApi::class)
internal class LocalKepkoPersistenceManagerTest {

    @BeforeTest
    fun useInMemoryDataStore() {
        SingletonPersistenceManager.resetForTesting { InMemoryPreferencesDataStore() }
    }

    @AfterTest
    fun restoreDefaultDataStore() {
        SingletonPersistenceManager.resetForTesting()
    }

    @Test
    fun givenDefaultPersistentTheme_whenReadingManager_thenReturnsPublicDefaultInstance() {
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
        assertSame(SingletonPersistenceManager.instanceOrBlockingLoad(), current)
    }

    @Test
    fun givenALoadedInstance_whenAwaited_thenReturnsTheSameInstance() = runBlocking {
        // Given
        val awaited = SingletonPersistenceManager.awaitInstance()

        // Then
        assertSame(awaited, SingletonPersistenceManager.instanceOrBlockingLoad())
        assertSame(awaited, SingletonPersistenceManager.awaitInstance())
    }

    @Test
    fun givenPreloaded_whenInstanceRequested_thenReturnsIt() {
        // When
        SingletonPersistenceManager.preload()

        // Then
        assertNotNull(SingletonPersistenceManager.instanceOrBlockingLoad())
    }

    @Test
    fun givenAManagerThatLoadsLater_whenRendered_thenComposesNothingUntilItArrives() {
        // Given
        val manager = PersistenceManagerImpl(testPreferencesStore())
        val pending = CompletableDeferred<PersistenceManager>()
        var current: PersistenceManager? = null

        runDesktopComposeUiTest {
            setContent {
                current = rememberLoadedPersistenceManager(loadOrNull = { null }, await = pending::await)
            }
            waitForIdle()

            // Then
            assertNull(current)

            // When
            pending.complete(manager)
            waitForIdle()

            // Then
            assertSame(manager, current)
        }
    }

    @Test
    fun givenAManagerAvailableImmediately_whenRendered_thenNeverAwaits() {
        // Given
        val manager = PersistenceManagerImpl(testPreferencesStore())
        var current: PersistenceManager? = null

        // When
        runDesktopComposeUiTest {
            setContent {
                current = rememberLoadedPersistenceManager(
                    loadOrNull = { manager },
                    await = { error("Must not await a manager that is already loaded.") },
                )
            }
            waitForIdle()
        }

        // Then
        assertSame(manager, current)
    }

    @Test
    fun givenNoProvider_whenAccessingLocalKepkoPersistenceManager_thenThrowsHelpfulError() {
        // When
        val exception = assertFailsWith<IllegalStateException> {
            runDesktopComposeUiTest {
                setContent {
                    LocalKepkoPersistenceManager.current
                }
            }
        }

        // Then
        assertContains(exception.message.orEmpty(), "PersistenceManager is not provided")
    }

    @Test
    fun givenProvidedManager_whenReadingLocalKepkoPersistenceManager_thenReturnsSameInstance() {
        // Given
        var provided: PersistenceManager? = null
        var current: PersistenceManager? = null

        // When
        runDesktopComposeUiTest {
            setContent {
                val manager = PersistenceManagerImpl(testPreferencesStore())
                provided = manager

                CompositionLocalProvider(LocalKepkoPersistenceManager provides manager) {
                    current = LocalKepkoPersistenceManager.current
                }
            }

            waitForIdle()
        }

        // Then
        assertNotNull(provided)
        assertSame(provided, current)
    }
}
