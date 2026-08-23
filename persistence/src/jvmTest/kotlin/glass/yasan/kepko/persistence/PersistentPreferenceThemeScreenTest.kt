package glass.yasan.kepko.persistence

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.v2.runDesktopComposeUiTest
import com.russhwolf.settings.MapSettings
import glass.yasan.kepko.component.Text
import glass.yasan.kepko.foundation.annotation.ExperimentalKepkoApi
import glass.yasan.kepko.foundation.theme.ColorPalette
import glass.yasan.kepko.foundation.theme.ColorPalette.BLACK
import glass.yasan.kepko.foundation.theme.ColorPalette.DARK
import glass.yasan.kepko.foundation.theme.ColorPalette.LIGHT
import glass.yasan.kepko.foundation.theme.ColorPalette.SOLARIZED_DARK
import glass.yasan.kepko.foundation.theme.ColorPalette.SOLARIZED_LIGHT
import glass.yasan.kepko.persistence.internal.PersistenceManagerImpl
import glass.yasan.kepko.resource.Icons
import glass.yasan.kepko.resource.Strings
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class, ExperimentalKepkoApi::class)
internal class PersistentPreferenceThemeScreenTest {

    @Test
    fun givenDefaultSettings_whenRendered_thenSystemIsSelected() {
        runDesktopComposeUiTest {
            val persistenceManager = PersistenceManagerImpl(MapSettings())
            lateinit var dynamicString: String
            setContent {
                PersistentKepkoTheme(persistenceManager = persistenceManager) {
                    dynamicString = Strings.preferencePaletteModeDynamic
                    PersistentPreferenceThemeScreen(onBackClick = {})
                }
            }

            waitForIdle()

            onNodeWithTag(PersistentPreferenceThemeScreenSemantics.SCREEN).assertExists()
            onNodeWithTag(PersistentPreferenceThemeScreenSemantics.PALETTE_MODE).assertExists()
            onNodeWithText(dynamicString, ignoreCase = true).assertExists()
            onNodeWithTag(PersistentPreferenceThemeScreenSemantics.LIGHT_PICKER).assertExists()
            onNodeWithTag(PersistentPreferenceThemeScreenSemantics.DARK_PICKER).assertExists()
            onNodeWithTag(PersistentPreferenceThemeScreenSemantics.PALETTE_PICKER).assertDoesNotExist()
            assertNull(persistenceManager.getPalettePrimary(null))
        }
    }

    @Test
    fun givenSystemSetting_whenRendered_thenLightAndDarkOptionsAreVisible() {
        runDesktopComposeUiTest {
            val persistenceManager = PersistenceManagerImpl(MapSettings())
            persistenceManager.setPalettePrimary(null, null)
            setContent {
                PersistentKepkoTheme(persistenceManager = persistenceManager) {
                    PersistentPreferenceThemeScreen(onBackClick = {})
                }
            }

            waitForIdle()
            assertNull(persistenceManager.getPalettePrimary(null))
            onNodeWithTag(PersistentPreferenceThemeScreenSemantics.LIGHT_PICKER).assertExists()
            onNodeWithTag(PersistentPreferenceThemeScreenSemantics.DARK_PICKER).assertExists()
        }
    }

    @Test
    fun givenStaticSetting_whenRendered_thenMatchingPrimaryPaletteIsVisibleAndSubPickersHidden() {
        runDesktopComposeUiTest {
            val persistenceManager = PersistenceManagerImpl(MapSettings())
            persistenceManager.setPalettePrimary(null, SOLARIZED_DARK)
            lateinit var solarizedDarkString: String
            setContent {
                PersistentKepkoTheme(persistenceManager = persistenceManager) {
                    solarizedDarkString = Strings.colorPaletteDarkSolarized
                    PersistentPreferenceThemeScreen(onBackClick = {})
                }
            }

            waitForIdle()
            assertEquals(ColorPalette.SOLARIZED_DARK, persistenceManager.getPalettePrimary(null))
            onNodeWithTag(PersistentPreferenceThemeScreenSemantics.PALETTE_MODE).assertExists()
            onNodeWithTag(PersistentPreferenceThemeScreenSemantics.PALETTE_PICKER)
                .assertTextContains(solarizedDarkString, ignoreCase = true)
            onNodeWithTag(PersistentPreferenceThemeScreenSemantics.LIGHT_PICKER).assertDoesNotExist()
            onNodeWithTag(PersistentPreferenceThemeScreenSemantics.DARK_PICKER).assertDoesNotExist()
        }
    }

    @Test
    fun givenDynamicSetting_whenModeToggledAndPaletteSelected_thenStaticSelectionReflectedOnScreen() {
        runDesktopComposeUiTest(width = 1024, height = 1200) {
            val persistenceManager = PersistenceManagerImpl(MapSettings())
            persistenceManager.setPalettePrimary(null, null)
            persistenceManager.paletteLight = LIGHT
            persistenceManager.paletteDark = DARK
            lateinit var blackString: String
            setContent {
                PersistentKepkoTheme(persistenceManager = persistenceManager) {
                    blackString = Strings.colorPaletteBlack
                    PersistentPreferenceThemeScreen(
                        onBackClick = {},
                        isSystemInDarkTheme = true,
                    )
                }
            }

            waitForIdle()
            assertNull(persistenceManager.getPalettePrimary(null))
            onNodeWithTag(PersistentPreferenceThemeScreenSemantics.PALETTE_PICKER).assertDoesNotExist()
            onNodeWithText(blackString, ignoreCase = true).assertDoesNotExist()

            onNodeWithTag(PersistentPreferenceThemeScreenSemantics.PALETTE_MODE).performClick()
            waitUntil { persistenceManager.getPalettePrimary(null) != null }
            onNodeWithTag(PersistentPreferenceThemeScreenSemantics.PALETTE_PICKER).performClick()
            waitForIdle()
            onNodeWithText(blackString, ignoreCase = true).assertExists()
            onNodeWithText(blackString, ignoreCase = true).performClick()
            waitUntil { persistenceManager.getPalettePrimary(null) == ColorPalette.BLACK }

            assertEquals(ColorPalette.BLACK, persistenceManager.getPalettePrimary(null))
            onNodeWithTag(PersistentPreferenceThemeScreenSemantics.PALETTE_PICKER)
                .assertTextContains(blackString, ignoreCase = true)
            onNodeWithTag(PersistentPreferenceThemeScreenSemantics.LIGHT_PICKER).assertDoesNotExist()
            onNodeWithTag(PersistentPreferenceThemeScreenSemantics.DARK_PICKER).assertDoesNotExist()
        }
    }

    @Test
    fun givenStaticSetting_whenModeToggled_thenDynamicOptionsAreVisible() {
        runDesktopComposeUiTest {
            val persistenceManager = PersistenceManagerImpl(MapSettings())
            persistenceManager.setPalettePrimary(null, SOLARIZED_DARK)
            setContent {
                PersistentKepkoTheme(persistenceManager = persistenceManager) {
                    PersistentPreferenceThemeScreen(onBackClick = {})
                }
            }

            waitForIdle()
            assertEquals(ColorPalette.SOLARIZED_DARK, persistenceManager.getPalettePrimary(null))
            onNodeWithTag(PersistentPreferenceThemeScreenSemantics.PALETTE_PICKER).assertExists()

            onNodeWithTag(PersistentPreferenceThemeScreenSemantics.PALETTE_MODE).performClick()
            waitForIdle()

            assertNull(persistenceManager.getPalettePrimary(null))
            onNodeWithTag(PersistentPreferenceThemeScreenSemantics.PALETTE_PICKER).assertDoesNotExist()
            onNodeWithTag(PersistentPreferenceThemeScreenSemantics.LIGHT_PICKER).assertExists()
            onNodeWithTag(PersistentPreferenceThemeScreenSemantics.DARK_PICKER).assertExists()
        }
    }

    @Test
    fun givenSystemSetting_whenLightAndDarkPickersOpenedAndPalettesSelected_thenSelectionsReflectedOnScreen() {
        runDesktopComposeUiTest(width = 1024, height = 1200) {
            val persistenceManager = PersistenceManagerImpl(MapSettings())
            persistenceManager.setPalettePrimary(null, null)
            persistenceManager.paletteLight = BLACK
            persistenceManager.paletteDark = SOLARIZED_LIGHT
            lateinit var blackString: String
            lateinit var darkString: String
            lateinit var solarizedLightString: String
            setContent {
                PersistentKepkoTheme(persistenceManager = persistenceManager) {
                    blackString = Strings.colorPaletteBlack
                    darkString = Strings.colorPaletteDark
                    solarizedLightString = Strings.colorPaletteLightSolarized
                    PersistentPreferenceThemeScreen(onBackClick = {})
                }
            }

            waitForIdle()
            assertEquals(ColorPalette.BLACK, persistenceManager.paletteLight)
            assertEquals(ColorPalette.SOLARIZED_LIGHT, persistenceManager.paletteDark)
            onNodeWithTag(PersistentPreferenceThemeScreenSemantics.LIGHT_PICKER)
                .assertTextContains(blackString, ignoreCase = true)
            onNodeWithTag(PersistentPreferenceThemeScreenSemantics.DARK_PICKER)
                .assertTextContains(solarizedLightString, ignoreCase = true)
            onNodeWithText(darkString, ignoreCase = true).assertDoesNotExist()

            onNodeWithTag(PersistentPreferenceThemeScreenSemantics.LIGHT_PICKER).performClick()
            waitForIdle()
            onNodeWithText(darkString, ignoreCase = true).assertExists()
            onNodeWithText(darkString, ignoreCase = true).performClick()
            waitForIdle()

            assertEquals(ColorPalette.DARK, persistenceManager.paletteLight)
            onNodeWithTag(PersistentPreferenceThemeScreenSemantics.LIGHT_PICKER)
                .assertTextContains(darkString, ignoreCase = true)

            onNodeWithTag(PersistentPreferenceThemeScreenSemantics.DARK_PICKER).performClick()
            waitForIdle()
            onNodeWithText(blackString, ignoreCase = true).assertExists()
            onNodeWithText(blackString, ignoreCase = true).performClick()
            waitForIdle()

            assertEquals(ColorPalette.BLACK, persistenceManager.paletteDark)
            onNodeWithTag(PersistentPreferenceThemeScreenSemantics.DARK_PICKER)
                .assertTextContains(blackString, ignoreCase = true)
        }
    }

    @Test
    fun givenGrayscaleOff_whenToggled_thenPersistenceUpdated() {
        runDesktopComposeUiTest {
            val persistenceManager = PersistenceManagerImpl(MapSettings())
            setContent {
                PersistentKepkoTheme(persistenceManager = persistenceManager) {
                    PersistentPreferenceThemeScreen(onBackClick = {})
                }
            }

            waitForIdle()
            assertFalse(persistenceManager.isGrayscaleEnabled(null))
            onNodeWithTag(PersistentPreferenceThemeScreenSemantics.GRAYSCALE).performClick()
            waitForIdle()

            assertTrue(persistenceManager.isGrayscaleEnabled(null))
        }
    }

    @Test
    fun givenTargetProfile_whenRendered_thenOnlyProfileControlsVisible() {
        runDesktopComposeUiTest {
            val persistenceManager = PersistenceManagerImpl(MapSettings())
            setContent {
                PersistentKepkoTheme(persistenceManager = persistenceManager) {
                    PersistentPreferenceThemeScreen(onBackClick = {}, targetProfile = testProfile)
                }
            }

            waitForIdle()
            onNodeWithTag(PersistentPreferenceThemeScreenSemantics.PROFILE_HEADER).assertExists()
            onNodeWithTag(PersistentPreferenceThemeScreenSemantics.PROFILE_PALETTE_PICKER).assertExists()
            onNodeWithTag(PersistentPreferenceThemeScreenSemantics.PROFILE_GRAYSCALE_PICKER).assertExists()
            onNodeWithTag(PersistentPreferenceThemeScreenSemantics.PALETTE_MODE).assertDoesNotExist()
            onNodeWithTag(PersistentPreferenceThemeScreenSemantics.PALETTE_PICKER).assertDoesNotExist()
            onNodeWithTag(PersistentPreferenceThemeScreenSemantics.LIGHT_PICKER).assertDoesNotExist()
            onNodeWithTag(PersistentPreferenceThemeScreenSemantics.DARK_PICKER).assertDoesNotExist()
            onNodeWithTag(PersistentPreferenceThemeScreenSemantics.GRAYSCALE).assertDoesNotExist()
            onNodeWithTag(PersistentPreferenceThemeScreenSemantics.OUTLINE).assertDoesNotExist()
            onNodeWithTag(PersistentPreferenceThemeScreenSemantics.ROUNDNESS).assertDoesNotExist()
        }
    }

    @Test
    fun givenTargetProfile_whenRendered_thenFollowGlobalIsDefaultSelection() {
        runDesktopComposeUiTest {
            val persistenceManager = PersistenceManagerImpl(MapSettings())
            lateinit var followGlobalString: String
            setContent {
                PersistentKepkoTheme(persistenceManager = persistenceManager) {
                    followGlobalString = Strings.preferenceFollowGlobalTitle
                    PersistentPreferenceThemeScreen(onBackClick = {}, targetProfile = testProfile)
                }
            }

            waitForIdle()
            assertNull(persistenceManager.profileManager.getProfilePalette(testProfile.id))
            assertNull(persistenceManager.profileManager.getProfileGrayscale(testProfile.id))
            onNodeWithTag(PersistentPreferenceThemeScreenSemantics.PROFILE_PALETTE_PICKER)
                .assertTextContains(followGlobalString, ignoreCase = true)
            onNodeWithTag(PersistentPreferenceThemeScreenSemantics.PROFILE_GRAYSCALE_PICKER)
                .assertTextContains(followGlobalString, ignoreCase = true)
        }
    }

    @Test
    fun givenTargetProfile_whenPaletteSelected_thenProfileOverrideSetAndGlobalUntouched() {
        runDesktopComposeUiTest(width = 1024, height = 1200) {
            val persistenceManager = PersistenceManagerImpl(MapSettings())
            lateinit var blackString: String
            setContent {
                PersistentKepkoTheme(persistenceManager = persistenceManager) {
                    blackString = ColorPalette.BLACK.title()
                    PersistentPreferenceThemeScreen(onBackClick = {}, targetProfile = testProfile)
                }
            }

            waitForIdle()
            onNodeWithTag(PersistentPreferenceThemeScreenSemantics.PROFILE_PALETTE_PICKER).performClick()
            waitForIdle()
            onNodeWithText(blackString, ignoreCase = true).performClick()
            waitUntil {
                persistenceManager.profileManager.getProfilePalette(testProfile.id) == ColorPalette.BLACK
            }

            assertNull(persistenceManager.getPalettePrimary(null))
            onNodeWithTag(PersistentPreferenceThemeScreenSemantics.PROFILE_PALETTE_PICKER)
                .assertTextContains(blackString, ignoreCase = true)
        }
    }

    @Test
    fun givenProfilePaletteOverride_whenFollowGlobalSelected_thenOverrideCleared() {
        runDesktopComposeUiTest(width = 1024, height = 1200) {
            val persistenceManager = PersistenceManagerImpl(MapSettings())
            persistenceManager.profileManager.setProfilePalette(testProfile.id, SOLARIZED_DARK)
            persistenceManager.profileManager.setProfileGrayscale(testProfile.id, true)
            lateinit var followGlobalString: String
            setContent {
                PersistentKepkoTheme(persistenceManager = persistenceManager) {
                    followGlobalString = Strings.preferenceFollowGlobalTitle
                    PersistentPreferenceThemeScreen(onBackClick = {}, targetProfile = testProfile)
                }
            }

            waitForIdle()
            onNodeWithTag(PersistentPreferenceThemeScreenSemantics.PROFILE_PALETTE_PICKER).performClick()
            waitForIdle()
            onNodeWithText(followGlobalString, ignoreCase = true).performClick()
            waitUntil {
                persistenceManager.profileManager.getProfilePalette(testProfile.id) == null
            }
        }
    }

    @Test
    fun givenTargetProfile_whenGrayscaleEnabledSelected_thenProfileOverrideSetAndGlobalUntouched() {
        runDesktopComposeUiTest(width = 1024, height = 1200) {
            val persistenceManager = PersistenceManagerImpl(MapSettings())
            lateinit var enabledString: String
            setContent {
                PersistentKepkoTheme(persistenceManager = persistenceManager) {
                    enabledString = Strings.enabled
                    PersistentPreferenceThemeScreen(onBackClick = {}, targetProfile = testProfile)
                }
            }

            waitForIdle()
            onNodeWithTag(PersistentPreferenceThemeScreenSemantics.PROFILE_GRAYSCALE_PICKER).performClick()
            waitForIdle()
            onNodeWithText(enabledString, ignoreCase = true).performClick()
            waitUntil {
                persistenceManager.profileManager.getProfileGrayscale(testProfile.id) == true
            }

            assertFalse(persistenceManager.isGrayscaleEnabled(null))
            onNodeWithTag(PersistentPreferenceThemeScreenSemantics.PROFILE_GRAYSCALE_PICKER)
                .assertTextContains(enabledString, ignoreCase = true)
        }
    }

    @Test
    fun givenProfileOverrides_whenResetClicked_thenOverridesCleared() {
        runDesktopComposeUiTest {
            val persistenceManager = PersistenceManagerImpl(MapSettings())
            persistenceManager.profileManager.setProfilePalette(testProfile.id, SOLARIZED_DARK)
            persistenceManager.profileManager.setProfileGrayscale(testProfile.id, true)
            lateinit var resetString: String
            setContent {
                PersistentKepkoTheme(persistenceManager = persistenceManager) {
                    resetString = Strings.reset
                    PersistentPreferenceThemeScreen(onBackClick = {}, targetProfile = testProfile)
                }
            }

            waitForIdle()
            onNodeWithContentDescription(resetString).performClick()
            waitUntil {
                persistenceManager.profileManager.getProfilePalette(testProfile.id) == null
            }

            assertNull(persistenceManager.profileManager.getProfileGrayscale(testProfile.id))
        }
    }

    @Test
    fun givenSlots_whenRendered_thenHostContentIsInsideTheScrollingColumn() {
        runDesktopComposeUiTest {
            val persistenceManager = PersistenceManagerImpl(MapSettings())
            setContent {
                PersistentKepkoTheme(persistenceManager = persistenceManager) {
                    PersistentPreferenceThemeScreen(
                        onBackClick = {},
                        topContent = { Text(TOP_SLOT) },
                        bottomContent = { Text(BOTTOM_SLOT) },
                    )
                }
            }

            waitForIdle()

            onNodeWithText(TOP_SLOT).assertExists()
            onNodeWithText(BOTTOM_SLOT).assertExists()
            onNodeWithTag(PersistentPreferenceThemeScreenSemantics.PALETTE_MODE).assertIsEnabled()
        }
    }

    @Test
    fun givenDisabled_whenRendered_thenControlsAreReadOnlyAndResetIsHidden() {
        runDesktopComposeUiTest {
            val persistenceManager = PersistenceManagerImpl(MapSettings())
            persistenceManager.setGrayscaleEnabled(null, true)
            lateinit var resetString: String
            setContent {
                PersistentKepkoTheme(persistenceManager = persistenceManager) {
                    resetString = Strings.reset
                    PersistentPreferenceThemeScreen(
                        onBackClick = {},
                        enabled = false,
                        topContent = { Text(TOP_SLOT) },
                    )
                }
            }

            waitForIdle()

            onNodeWithText(TOP_SLOT).assertExists()
            onNodeWithTag(PersistentPreferenceThemeScreenSemantics.PALETTE_MODE).assertIsNotEnabled()
            onNodeWithTag(PersistentPreferenceThemeScreenSemantics.LIGHT_PICKER).assertIsNotEnabled()
            onNodeWithTag(PersistentPreferenceThemeScreenSemantics.DARK_PICKER).assertIsNotEnabled()
            onNodeWithTag(PersistentPreferenceThemeScreenSemantics.GRAYSCALE).assertIsNotEnabled()
            onNodeWithTag(PersistentPreferenceThemeScreenSemantics.OUTLINE).assertIsNotEnabled()
            onNodeWithTag(PersistentPreferenceThemeScreenSemantics.ROUNDNESS).assertIsNotEnabled()
            onNodeWithContentDescription(resetString).assertDoesNotExist()
        }
    }

    @Test
    fun givenDisabled_whenControlClicked_thenPreferencesAreUnchanged() {
        runDesktopComposeUiTest {
            val persistenceManager = PersistenceManagerImpl(MapSettings())
            setContent {
                PersistentKepkoTheme(persistenceManager = persistenceManager) {
                    PersistentPreferenceThemeScreen(onBackClick = {}, enabled = false)
                }
            }

            waitForIdle()
            onNodeWithTag(PersistentPreferenceThemeScreenSemantics.GRAYSCALE).performClick()
            waitForIdle()

            assertFalse(persistenceManager.isGrayscaleEnabled(null))
        }
    }

    private companion object {
        const val TOP_SLOT = "top slot"
        const val BOTTOM_SLOT = "bottom slot"

        val testProfile = UserVisibleProfile(
            id = "work",
            name = { "Work" },
            icon = { Icons.person },
        )
    }
}
