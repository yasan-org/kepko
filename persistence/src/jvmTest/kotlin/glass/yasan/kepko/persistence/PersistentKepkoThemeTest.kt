package glass.yasan.kepko.persistence

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.v2.runDesktopComposeUiTest
import glass.yasan.kepko.foundation.annotation.ExperimentalKepkoApi
import glass.yasan.kepko.foundation.theme.ColorPalette
import glass.yasan.kepko.foundation.theme.KepkoTheme
import glass.yasan.kepko.persistence.internal.PersistenceManagerImpl
import glass.yasan.kepko.persistence.internal.PersistenceManagerImpl.Companion.KEY_GRAYSCALE
import glass.yasan.kepko.persistence.internal.PersistenceManagerImpl.Companion.KEY_STYLE
import kotlin.test.Test
import androidx.compose.ui.graphics.Color
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertSame
import kotlin.test.assertContains

@Suppress("VisibleForTests")
@OptIn(ExperimentalTestApi::class, ExperimentalKepkoApi::class)
internal class PersistentKepkoThemeTest {

    @Test
    fun givenDefaultSettings_whenRendered_thenUsesSystemDefaultPalette() {
        // When
        var palette: ColorPalette? = null
        val provided = PersistenceManagerImpl(testPreferencesStore())
        runDesktopComposeUiTest {
            setContent {
                PersistentKepkoTheme(persistenceManager = provided) {
                    palette = KepkoTheme.colors.palette
                }
            }

            waitForIdle()
        }

        // Then
        assertContains(listOf(ColorPalette.defaultLight, ColorPalette.defaultDark), palette)
    }

    @Test
    fun givenPrimaryPaletteInSettings_whenRendered_thenAppliesThatPalette() {
        // When
        var palette: ColorPalette? = null
        val provided = PersistenceManagerImpl(
            testPreferencesStore(KEY_STYLE to ColorPalette.BLACK.id),
        )
        runDesktopComposeUiTest {
            setContent {
                PersistentKepkoTheme(persistenceManager = provided) {
                    palette = KepkoTheme.colors.palette
                }
            }

            waitForIdle()
        }

        // Then
        assertEquals(ColorPalette.BLACK, palette)
    }

    @Test
    fun givenGrayscaleEnabled_whenRendered_thenColorsAreGrayscale() {
        // When
        var grayscaleSuccess: Color? = null
        var normalSuccess: Color? = null
        val grayscaleManager = PersistenceManagerImpl(
            testPreferencesStore(KEY_STYLE to ColorPalette.LIGHT.id, KEY_GRAYSCALE to true),
        )
        runDesktopComposeUiTest {
            setContent {
                PersistentKepkoTheme(persistenceManager = grayscaleManager) {
                    grayscaleSuccess = KepkoTheme.colors.success
                }
            }

            waitForIdle()
        }
        val normalManager = PersistenceManagerImpl(
            testPreferencesStore(KEY_STYLE to ColorPalette.LIGHT.id),
        )
        runDesktopComposeUiTest {
            setContent {
                PersistentKepkoTheme(persistenceManager = normalManager) {
                    normalSuccess = KepkoTheme.colors.success
                }
            }

            waitForIdle()
        }

        // Then
        assertNotEquals(normalSuccess, grayscaleSuccess)
    }

    @Test
    fun givenPersistenceManagerOverload_whenRendered_thenUsesProvidedManager() {
        // Given
        var manager: PersistenceManager? = null
        val store = testPreferencesStore(KEY_STYLE to ColorPalette.SOLARIZED_DARK.id)
        val provided = PersistenceManagerImpl(store)

        // When
        runDesktopComposeUiTest {
            setContent {
                PersistentKepkoTheme(persistenceManager = provided) {
                    manager = LocalKepkoPersistenceManager.current
                }
            }

            waitForIdle()
        }

        // Then
        assertSame(provided, manager)
    }

    @Test
    fun givenPersistenceManagerOverload_whenRendered_thenAppliesManagerPalette() {
        // Given
        var palette: ColorPalette? = null
        val store = testPreferencesStore(KEY_STYLE to ColorPalette.SOLARIZED_DARK.id)
        val provided = PersistenceManagerImpl(store)

        // When
        runDesktopComposeUiTest {
            setContent {
                PersistentKepkoTheme(persistenceManager = provided) {
                    palette = KepkoTheme.colors.palette
                }
            }

            waitForIdle()
        }

        // Then
        assertEquals(ColorPalette.SOLARIZED_DARK, palette)
    }

    @Test
    fun givenPrimaryPalette_whenRendered_thenLocalKepkoColorPaletteMatchesActivePalette() {
        // Given
        var palette: ColorPalette? = null
        val provided = PersistenceManagerImpl(
            testPreferencesStore(KEY_STYLE to ColorPalette.SOLARIZED_DARK.id),
        )

        // When
        runDesktopComposeUiTest {
            setContent {
                PersistentKepkoTheme(persistenceManager = provided) {
                    palette = LocalKepkoColorPalette.current
                }
            }

            waitForIdle()
        }

        // Then
        assertEquals(ColorPalette.SOLARIZED_DARK, palette)
    }

    @Test
    fun givenProfilePaletteOverride_whenRenderedWithProfileId_thenAppliesProfilePalette() {
        // Given
        var palette: ColorPalette? = null
        val provided = PersistenceManagerImpl(
            testPreferencesStore(KEY_STYLE to ColorPalette.BLACK.id),
        )
        provided.profileManager.setProfilePalette("work", ColorPalette.SOLARIZED_DARK)

        // When
        runDesktopComposeUiTest {
            setContent {
                PersistentKepkoTheme(persistenceManager = provided, profileId = "work") {
                    palette = KepkoTheme.colors.palette
                }
            }

            waitForIdle()
        }

        // Then
        assertEquals(ColorPalette.SOLARIZED_DARK, palette)
    }

    @Test
    fun givenNoProfileOverride_whenRenderedWithProfileId_thenFallsBackToGlobalPalette() {
        // Given
        var palette: ColorPalette? = null
        val provided = PersistenceManagerImpl(
            testPreferencesStore(KEY_STYLE to ColorPalette.BLACK.id),
        )

        // When
        runDesktopComposeUiTest {
            setContent {
                PersistentKepkoTheme(persistenceManager = provided, profileId = "work") {
                    palette = KepkoTheme.colors.palette
                }
            }

            waitForIdle()
        }

        // Then
        assertEquals(ColorPalette.BLACK, palette)
    }

    @Test
    fun givenProfileGrayscaleOverride_whenRenderedWithProfileId_thenColorsAreGrayscale() {
        // When
        var grayscaleSuccess: Color? = null
        var normalSuccess: Color? = null
        val grayscaleManager = PersistenceManagerImpl(
            testPreferencesStore(KEY_STYLE to ColorPalette.LIGHT.id),
        )
        grayscaleManager.profileManager.setProfileGrayscale("work", true)
        runDesktopComposeUiTest {
            setContent {
                PersistentKepkoTheme(persistenceManager = grayscaleManager, profileId = "work") {
                    grayscaleSuccess = KepkoTheme.colors.success
                }
            }

            waitForIdle()
        }
        val normalManager = PersistenceManagerImpl(
            testPreferencesStore(KEY_STYLE to ColorPalette.LIGHT.id),
        )
        runDesktopComposeUiTest {
            setContent {
                PersistentKepkoTheme(persistenceManager = normalManager, profileId = "work") {
                    normalSuccess = KepkoTheme.colors.success
                }
            }

            waitForIdle()
        }

        // Then
        assertNotEquals(normalSuccess, grayscaleSuccess)
    }
}
