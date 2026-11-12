package glass.yasan.kepko.persistence

import glass.yasan.kepko.foundation.theme.ColorPalette
import glass.yasan.kepko.foundation.theme.ColorPalette.BLACK
import glass.yasan.kepko.foundation.theme.ColorPalette.SOLARIZED_DARK
import glass.yasan.kepko.persistence.internal.ProfilePersistenceManagerImpl
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class ProfilePersistenceManagerImplTest {

    private companion object {
        const val PROFILE_ID = "work"
        const val PALETTE_KEY = "glass.yasan.kepko.style.primary.profile.$PROFILE_ID"
        const val GRAYSCALE_KEY = "glass.yasan.kepko.grayscale.profile.$PROFILE_ID"
    }

    @Test
    fun givenEmptySettings_whenGetProfilePalette_thenReturnsNull() {
        // Given
        val manager = ProfilePersistenceManagerImpl(testPreferencesStore())

        // Then
        assertNull(manager.getProfilePalette(PROFILE_ID))
    }

    @Test
    fun givenPaletteSet_whenGetProfilePalette_thenReadsBackSamePalette() {
        // Given
        val manager = ProfilePersistenceManagerImpl(testPreferencesStore())

        // When
        manager.setProfilePalette(PROFILE_ID, SOLARIZED_DARK)

        // Then
        assertEquals(ColorPalette.SOLARIZED_DARK, manager.getProfilePalette(PROFILE_ID))
    }

    @Test
    fun givenPaletteSet_thenPersistsPaletteIdUnderProfileKey() {
        // Given
        val store = testPreferencesStore()
        val manager = ProfilePersistenceManagerImpl(store)

        // When
        manager.setProfilePalette(PROFILE_ID, SOLARIZED_DARK)

        // Then
        assertEquals(ColorPalette.SOLARIZED_DARK.id, store.getStringOrNull(PALETTE_KEY))
    }

    @Test
    fun givenPaletteSet_whenSetToNull_thenRemovesKeyAndReturnsNull() {
        // Given
        val store = testPreferencesStore()
        val manager = ProfilePersistenceManagerImpl(store)
        manager.setProfilePalette(PROFILE_ID, BLACK)

        // When
        manager.setProfilePalette(PROFILE_ID, null)

        // Then
        assertNull(manager.getProfilePalette(PROFILE_ID))
        assertNull(store.getStringOrNull(PALETTE_KEY))
    }

    @Test
    fun givenEmptySettings_whenGetProfileGrayscale_thenReturnsNull() {
        // Given
        val manager = ProfilePersistenceManagerImpl(testPreferencesStore())

        // Then
        assertNull(manager.getProfileGrayscale(PROFILE_ID))
    }

    @Test
    fun givenGrayscaleSet_whenGetProfileGrayscale_thenReadsBackSameValue() {
        // Given
        val store = testPreferencesStore()
        val manager = ProfilePersistenceManagerImpl(store)

        // When
        manager.setProfileGrayscale(PROFILE_ID, true)

        // Then
        assertEquals(true, manager.getProfileGrayscale(PROFILE_ID))
        assertEquals(true, store.getBooleanOrNull(GRAYSCALE_KEY))

        // When
        manager.setProfileGrayscale(PROFILE_ID, false)

        // Then
        assertEquals(false, manager.getProfileGrayscale(PROFILE_ID))
    }

    @Test
    fun givenGrayscaleSet_whenSetToNull_thenRemovesKeyAndReturnsNull() {
        // Given
        val store = testPreferencesStore()
        val manager = ProfilePersistenceManagerImpl(store)
        manager.setProfileGrayscale(PROFILE_ID, true)

        // When
        manager.setProfileGrayscale(PROFILE_ID, null)

        // Then
        assertNull(manager.getProfileGrayscale(PROFILE_ID))
        assertNull(store.getBooleanOrNull(GRAYSCALE_KEY))
    }

    @Test
    fun givenTwoProfiles_whenValuesSet_thenProfilesDoNotLeakIntoEachOther() {
        // Given
        val manager = ProfilePersistenceManagerImpl(testPreferencesStore())

        // When
        manager.setProfilePalette("work", SOLARIZED_DARK)
        manager.setProfilePalette("home", BLACK)
        manager.setProfileGrayscale("work", true)

        // Then
        assertEquals(ColorPalette.SOLARIZED_DARK, manager.getProfilePalette("work"))
        assertEquals(ColorPalette.BLACK, manager.getProfilePalette("home"))
        assertEquals(true, manager.getProfileGrayscale("work"))
        assertNull(manager.getProfileGrayscale("home"))
    }

    @Test
    fun givenInvalidPaletteIdInSettings_whenGetProfilePalette_thenReturnsNull() {
        // Given
        val store = testPreferencesStore(PALETTE_KEY to "nonexistent")
        val manager = ProfilePersistenceManagerImpl(store)

        // Then
        assertNull(manager.getProfilePalette(PROFILE_ID))
    }
}
