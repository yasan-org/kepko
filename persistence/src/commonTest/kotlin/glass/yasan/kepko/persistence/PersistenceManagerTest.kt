package glass.yasan.kepko.persistence

import androidx.compose.ui.unit.dp
import glass.yasan.kepko.foundation.annotation.ExperimentalKepkoApi
import glass.yasan.kepko.foundation.theme.ColorPalette
import glass.yasan.kepko.foundation.theme.ColorPalette.BLACK
import glass.yasan.kepko.foundation.theme.ColorPalette.SOLARIZED_DARK
import glass.yasan.kepko.foundation.theme.ColorPalette.SOLARIZED_LIGHT
import glass.yasan.kepko.persistence.internal.PersistenceManagerImpl
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalKepkoApi::class)
@Suppress("VisibleForTests")
internal class PersistenceManagerTest {

    private fun createManager(
        vararg keyValues: Pair<String, Any>,
    ): PersistenceManager = PersistenceManagerImpl(testPreferencesStore(*keyValues))

    @Test
    fun givenEmptySettings_whenCreated_thenReturnsGetDefaultSnapshot() {
        // Given
        val manager = createManager()

        // Then
        assertNull(manager.getPalettePrimary(null))
        assertEquals(ColorPalette.defaultLight, manager.paletteLight)
        assertEquals(ColorPalette.defaultDark, manager.paletteDark)
        assertFalse(manager.isGrayscaleEnabled(null))
    }

    @Test
    fun givenEmptySettings_whenPalettePrimarySet_thenPersistsAndReadsBack() {
        // Given
        val store = testPreferencesStore()
        val manager = PersistenceManagerImpl(store)

        // When
        manager.setPalettePrimary(null, SOLARIZED_DARK)

        // Then
        assertEquals(ColorPalette.SOLARIZED_DARK, manager.getPalettePrimary(null))
        assertEquals(
            ColorPalette.SOLARIZED_DARK.id,
            store.getStringOrNull(PersistenceManagerImpl.KEY_STYLE)
        )
    }

    @Test
    fun givenPalettePrimarySet_whenSetToNull_thenPersistsSystemId() {
        // Given
        val store = testPreferencesStore()
        val manager = PersistenceManagerImpl(store)
        manager.setPalettePrimary(null, BLACK)

        // When
        manager.setPalettePrimary(null, null)

        // Then
        assertNull(manager.getPalettePrimary(null))
        assertEquals(
            PersistenceManager.PALETTE_ID_SYSTEM,
            store.getStringOrNull(PersistenceManagerImpl.KEY_STYLE)
        )
    }

    @Test
    fun givenEmptySettings_whenPaletteLightSet_thenPersistsAndReadsBack() {
        // Given
        val store = testPreferencesStore()
        val manager = PersistenceManagerImpl(store)

        // When
        manager.paletteLight = SOLARIZED_LIGHT

        // Then
        assertEquals(ColorPalette.SOLARIZED_LIGHT, manager.paletteLight)
        assertEquals(
            ColorPalette.SOLARIZED_LIGHT.id,
            store.getStringOrNull(PersistenceManagerImpl.KEY_LIGHT_STYLE)
        )
    }

    @Test
    fun givenEmptySettings_whenPaletteDarkSet_thenPersistsAndReadsBack() {
        // Given
        val store = testPreferencesStore()
        val manager = PersistenceManagerImpl(store)

        // When
        manager.paletteDark = BLACK

        // Then
        assertEquals(ColorPalette.BLACK, manager.paletteDark)
        assertEquals(
            ColorPalette.BLACK.id,
            store.getStringOrNull(PersistenceManagerImpl.KEY_DARK_STYLE)
        )
    }

    @Test
    fun givenEmptySettings_whenGrayscaleSet_thenPersistsAndReadsBack() {
        // Given
        val store = testPreferencesStore()
        val manager = PersistenceManagerImpl(store)

        // When
        manager.setGrayscaleEnabled(null, true)

        // Then
        assertTrue(manager.isGrayscaleEnabled(null))
        assertEquals(
            true,
            store.getBooleanOrNull(PersistenceManagerImpl.KEY_GRAYSCALE)
        )
    }

    @Test
    fun givenPrePopulatedSettings_whenCreated_thenRestoresValues() {
        // Given
        val manager = createManager(
            PersistenceManagerImpl.KEY_STYLE to ColorPalette.BLACK.id,
            PersistenceManagerImpl.KEY_LIGHT_STYLE to ColorPalette.SOLARIZED_LIGHT.id,
            PersistenceManagerImpl.KEY_DARK_STYLE to ColorPalette.SOLARIZED_DARK.id,
            PersistenceManagerImpl.KEY_GRAYSCALE to true,
        )

        // Then
        assertEquals(ColorPalette.BLACK, manager.getPalettePrimary(null))
        assertEquals(ColorPalette.SOLARIZED_LIGHT, manager.paletteLight)
        assertEquals(ColorPalette.SOLARIZED_DARK, manager.paletteDark)
        assertTrue(manager.isGrayscaleEnabled(null))
    }

    @Test
    fun givenInvalidPaletteIds_whenCreated_thenFallsBackToGetDefaultSnapshot() {
        // Given
        val manager = createManager(
            PersistenceManagerImpl.KEY_STYLE to "nonexistent",
            PersistenceManagerImpl.KEY_LIGHT_STYLE to "invalid",
            PersistenceManagerImpl.KEY_DARK_STYLE to "unknown",
        )

        // Then
        assertNull(manager.getPalettePrimary(null))
        assertEquals(ColorPalette.defaultLight, manager.paletteLight)
        assertEquals(ColorPalette.defaultDark, manager.paletteDark)
    }

    @Test
    fun givenSystemPrimaryPaletteId_whenCreated_thenRestoresNullPrimaryPalette() {
        // Given
        val manager =
            createManager(PersistenceManagerImpl.KEY_STYLE to PersistenceManager.PALETTE_ID_SYSTEM)

        // Then
        assertNull(manager.getPalettePrimary(null))
    }

    @Test
    fun givenGrayscaleTrue_whenSetToFalse_thenPersistsAndReadsBack() {
        // Given
        val store = testPreferencesStore(PersistenceManagerImpl.KEY_GRAYSCALE to true)
        val manager = PersistenceManagerImpl(store)

        // When
        manager.setGrayscaleEnabled(null, false)

        // Then
        assertFalse(manager.isGrayscaleEnabled(null))
        assertEquals(
            false,
            store.getBooleanOrNull(PersistenceManagerImpl.KEY_GRAYSCALE)
        )
    }

    @Test
    fun givenPrimaryPaletteSet_whenResolvedPalette_thenAlwaysUsesPrimaryPalette() {
        // Given
        val manager = createManager() as PersistenceManagerImpl
        manager.setPalettePrimary(null, BLACK)
        manager.paletteLight = SOLARIZED_LIGHT
        manager.paletteDark = SOLARIZED_DARK

        // When
        val lightResolved = manager.resolvedPalette(isSystemInDarkTheme = false)
        val darkResolved = manager.resolvedPalette(isSystemInDarkTheme = true)

        // Then
        assertEquals(ColorPalette.BLACK, lightResolved)
        assertEquals(ColorPalette.BLACK, darkResolved)
    }

    @Test
    fun givenNoPrimaryPalette_whenResolvedPalette_thenUsesLightOrDarkPaletteByDarkFlag() {
        // Given
        val manager = createManager() as PersistenceManagerImpl
        manager.setPalettePrimary(null, null)
        manager.paletteLight = SOLARIZED_LIGHT
        manager.paletteDark = SOLARIZED_DARK

        // When
        val lightResolved = manager.resolvedPalette(isSystemInDarkTheme = false)
        val darkResolved = manager.resolvedPalette(isSystemInDarkTheme = true)

        // Then
        assertEquals(ColorPalette.SOLARIZED_LIGHT, lightResolved)
        assertEquals(ColorPalette.SOLARIZED_DARK, darkResolved)
    }

    @Test
    fun givenNoProfileOverride_whenGetPalettePrimaryWithProfileId_thenFallsBackToGlobal() {
        // Given
        val manager = createManager()
        manager.setPalettePrimary(null, BLACK)

        // Then
        assertEquals(ColorPalette.BLACK, manager.getPalettePrimary("work"))
    }

    @Test
    fun givenProfileOverride_whenGetPalettePrimaryWithProfileId_thenOverrideWins() {
        // Given
        val manager = createManager()
        manager.setPalettePrimary(null, BLACK)

        // When
        manager.setPalettePrimary("work", SOLARIZED_DARK)

        // Then
        assertEquals(ColorPalette.SOLARIZED_DARK, manager.getPalettePrimary("work"))
        assertEquals(ColorPalette.BLACK, manager.getPalettePrimary(null))
        assertEquals(ColorPalette.BLACK, manager.getPalettePrimary("other"))
    }

    @Test
    fun givenProfileOverride_whenClearedWithNull_thenFallsBackToGlobal() {
        // Given
        val manager = createManager()
        manager.setPalettePrimary(null, BLACK)
        manager.setPalettePrimary("work", SOLARIZED_DARK)

        // When
        manager.setPalettePrimary("work", null)

        // Then
        assertEquals(ColorPalette.BLACK, manager.getPalettePrimary("work"))
    }

    @Test
    fun givenNoProfileOverride_whenIsGrayscaleEnabledWithProfileId_thenFallsBackToGlobal() {
        // Given
        val manager = createManager()
        manager.setGrayscaleEnabled(null, true)

        // Then
        assertTrue(manager.isGrayscaleEnabled("work"))
    }

    @Test
    fun givenProfileGrayscaleFalse_whenGlobalTrue_thenOverrideWins() {
        // Given
        val manager = createManager()
        manager.setGrayscaleEnabled(null, true)

        // When
        manager.setGrayscaleEnabled("work", false)

        // Then
        assertFalse(manager.isGrayscaleEnabled("work"))
        assertTrue(manager.isGrayscaleEnabled(null))
        assertTrue(manager.isGrayscaleEnabled("other"))
    }

    @Test
    fun givenProfileGrayscaleTrue_whenGlobalFalse_thenOverrideWins() {
        // Given
        val manager = createManager()

        // When
        manager.setGrayscaleEnabled("work", true)

        // Then
        assertTrue(manager.isGrayscaleEnabled("work"))
        assertFalse(manager.isGrayscaleEnabled(null))
    }

    @Test
    fun givenProfileSetters_whenUsed_thenWriteThroughProfileManager() {
        // Given
        val store = testPreferencesStore()
        val manager = PersistenceManagerImpl(store)

        // When
        manager.setPalettePrimary("work", SOLARIZED_DARK)
        manager.setGrayscaleEnabled("work", true)

        // Then
        assertEquals(ColorPalette.SOLARIZED_DARK, manager.profileManager.getProfilePalette("work"))
        assertEquals(true, manager.profileManager.getProfileGrayscale("work"))
    }

    @Test
    fun givenProfileOverrides_whenSnapshotTaken_thenSnapshotOnlyReflectsGlobals() {
        // Given
        val manager = createManager()
        manager.setPalettePrimary("work", SOLARIZED_DARK)
        manager.setGrayscaleEnabled("work", true)

        // Then
        assertNull(manager.toSnapshot().palettePrimary)
        assertFalse(manager.toSnapshot().grayscale)
        assertTrue(manager.isDefault)
    }

    @Test
    fun givenEmptySettings_whenListPaneWidthSet_thenPersistsAndReadsBack() {
        // Given
        val store = testPreferencesStore()
        val manager = PersistenceManagerImpl(store)

        // When
        manager.listPaneWidth = 420.dp

        // Then
        assertEquals(420.dp, manager.listPaneWidth)
        assertEquals(420f, store.getFloatOrNull(PersistenceManagerImpl.KEY_LIST_PANE_WIDTH))
    }

    @Test
    fun givenAStoredListPaneWidth_whenCreated_thenRestoresIt() {
        // Given
        val manager = createManager(PersistenceManagerImpl.KEY_LIST_PANE_WIDTH to 420f)

        // Then
        assertEquals(420.dp, manager.listPaneWidth)
    }

    @Test
    fun givenAListPaneWidth_whenSetToNull_thenRemovesIt() {
        // Given
        val store = testPreferencesStore()
        val manager = PersistenceManagerImpl(store)
        manager.listPaneWidth = 420.dp

        // When
        manager.listPaneWidth = null

        // Then
        assertNull(manager.listPaneWidth)
        assertNull(store.getFloatOrNull(PersistenceManagerImpl.KEY_LIST_PANE_WIDTH))
    }

    @Test
    fun givenAListPaneWidth_whenReset_thenItIsKept() {
        // Given
        val manager = createManager()
        manager.listPaneWidth = 420.dp

        // When
        manager.reset()

        // Then
        assertEquals(420.dp, manager.listPaneWidth)
    }

    @Test
    fun givenStoredDimensions_whenCreated_thenReadsThemBack() {
        // Given
        val manager = createManager(
            PersistenceManagerImpl.KEY_OUTLINE to 2f,
            PersistenceManagerImpl.KEY_ROUNDNESS to 0.25f,
            PersistenceManagerImpl.KEY_LIST_PANE_WIDTH to 320f,
        )

        // Then
        assertEquals(2.dp, manager.outline)
        assertEquals(0.25f, manager.roundness)
        assertEquals(320.dp, manager.listPaneWidth)
    }
}
