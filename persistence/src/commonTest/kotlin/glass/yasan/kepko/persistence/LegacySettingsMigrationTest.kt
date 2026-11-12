package glass.yasan.kepko.persistence

import androidx.datastore.preferences.core.emptyPreferences
import glass.yasan.kepko.foundation.theme.ColorPalette
import glass.yasan.kepko.persistence.internal.LEGACY_MIGRATION_PASSES_KEY
import glass.yasan.kepko.persistence.internal.LegacySettingsMigration
import glass.yasan.kepko.persistence.internal.LegacySettingsSource
import glass.yasan.kepko.persistence.internal.PersistenceManagerImpl.Companion.KEY_DARK_STYLE
import glass.yasan.kepko.persistence.internal.PersistenceManagerImpl.Companion.KEY_GRAYSCALE
import glass.yasan.kepko.persistence.internal.PersistenceManagerImpl.Companion.KEY_LIGHT_STYLE
import glass.yasan.kepko.persistence.internal.PersistenceManagerImpl.Companion.KEY_LIST_PANE_WIDTH
import glass.yasan.kepko.persistence.internal.PersistenceManagerImpl.Companion.KEY_OUTLINE
import glass.yasan.kepko.persistence.internal.PersistenceManagerImpl.Companion.KEY_ROUNDNESS
import glass.yasan.kepko.persistence.internal.PersistenceManagerImpl.Companion.KEY_STYLE
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@Suppress("VisibleForTests")
internal class LegacySettingsMigrationTest {

    private companion object {
        const val PROFILE_PALETTE_KEY = "glass.yasan.kepko.style.primary.profile.work"
        const val PROFILE_GRAYSCALE_KEY = "glass.yasan.kepko.grayscale.profile.work"
        const val APP_KEY = "app.theme"
    }

    @Test
    fun givenNothingMigratedYet_whenChecked_thenShouldMigrate() = runTest {
        // Given
        val migration = LegacySettingsMigration(FakeLegacySettingsSource(KEY_GRAYSCALE to "true"))

        // Then
        assertTrue(migration.shouldMigrate(emptyPreferences()))
    }

    @Test
    fun givenMigratedOnce_whenChecked_thenShouldMigrateAgain() = runTest {
        // Given
        val migration = LegacySettingsMigration(FakeLegacySettingsSource(KEY_GRAYSCALE to "true"))
        val migrated = migration.migrate(emptyPreferences())

        // Then
        assertTrue(migration.shouldMigrate(migrated))
    }

    @Test
    fun givenMigratedTwice_whenChecked_thenShouldNotMigrateOrReadTheSource() = runTest {
        // Given
        val source = FakeLegacySettingsSource(KEY_GRAYSCALE to "true")
        val migration = LegacySettingsMigration(source)
        val migrated = migration.migrate(migration.migrate(emptyPreferences()))
        val readsBefore = source.keyReads

        // Then
        assertFalse(migration.shouldMigrate(migrated))
        assertEquals(readsBefore, source.keyReads)
    }

    @Test
    fun givenACleanUpThatFailed_whenMigratedAgain_thenTheSecondCleanUpRemovesTheKeys() = runTest {
        // Given
        val source = FakeLegacySettingsSource(KEY_STYLE to ColorPalette.BLACK.id, APP_KEY to "dark")
        val first = LegacySettingsMigration(source)
        source.failRemovals = true
        val migratedOnce = first.migrate(emptyPreferences())
        first.cleanUp()
        source.failRemovals = false

        // When
        val second = LegacySettingsMigration(source)
        val migratedTwice = second.migrate(migratedOnce)
        second.cleanUp()

        // Then
        assertEquals(mapOf(APP_KEY to "dark"), source.values)
        assertEquals(
            testPreferences(KEY_STYLE to ColorPalette.BLACK.id, LEGACY_MIGRATION_PASSES_KEY to 2),
            migratedTwice,
        )
    }

    @Test
    fun givenEveryLegacyKey_whenMigrated_thenCopiesEachWithItsType() = runTest {
        // Given
        val migration = LegacySettingsMigration(
            FakeLegacySettingsSource(
                KEY_STYLE to ColorPalette.BLACK.id,
                KEY_LIGHT_STYLE to ColorPalette.SOLARIZED_LIGHT.id,
                KEY_DARK_STYLE to ColorPalette.SOLARIZED_DARK.id,
                KEY_GRAYSCALE to "true",
                KEY_OUTLINE to "1.5",
                KEY_ROUNDNESS to "0.5",
                // Web stored whole floats without a decimal point.
                KEY_LIST_PANE_WIDTH to "420",
                PROFILE_PALETTE_KEY to ColorPalette.LIGHT.id,
                PROFILE_GRAYSCALE_KEY to "false",
                APP_KEY to "dark",
            ),
        )

        // When
        val migrated = migration.migrate(emptyPreferences())

        // Then
        assertEquals(
            testPreferences(
                KEY_STYLE to ColorPalette.BLACK.id,
                KEY_LIGHT_STYLE to ColorPalette.SOLARIZED_LIGHT.id,
                KEY_DARK_STYLE to ColorPalette.SOLARIZED_DARK.id,
                KEY_GRAYSCALE to true,
                KEY_OUTLINE to 1.5f,
                KEY_ROUNDNESS to 0.5f,
                KEY_LIST_PANE_WIDTH to 420f,
                PROFILE_PALETTE_KEY to ColorPalette.LIGHT.id,
                PROFILE_GRAYSCALE_KEY to false,
                LEGACY_MIGRATION_PASSES_KEY to 1,
            ),
            migrated,
        )
    }

    @Test
    fun givenMalformedOrUnknownValues_whenMigrated_thenDropsThem() = runTest {
        // Given
        val migration = LegacySettingsMigration(
            FakeLegacySettingsSource(
                KEY_GRAYSCALE to "maybe",
                KEY_ROUNDNESS to "round",
                "glass.yasan.kepko.unknown" to "value",
            ),
        )

        // When
        val migrated = migration.migrate(emptyPreferences())

        // Then
        assertEquals(testPreferences(LEGACY_MIGRATION_PASSES_KEY to 1), migrated)
    }

    @Test
    fun givenKeysWhoseValuesAreGone_whenMigrated_thenSkipsThem() = runTest {
        // Given
        val source = object : LegacySettingsSource {
            override fun keys(): Set<String> = setOf(KEY_STYLE, KEY_GRAYSCALE, KEY_ROUNDNESS)
            override fun getString(key: String): String? = null
            override fun getBoolean(key: String): Boolean? = null
            override fun getFloat(key: String): Float? = null
            override fun remove(keys: Set<String>) = Unit
        }

        // When
        val migrated = LegacySettingsMigration(source).migrate(emptyPreferences())

        // Then
        assertEquals(testPreferences(LEGACY_MIGRATION_PASSES_KEY to 1), migrated)
    }

    @Test
    fun givenAMissingKey_whenReadFromATextSource_thenReturnsNull() {
        // Given
        val source = FakeLegacySettingsSource()

        // Then
        assertNull(source.getString(KEY_STYLE))
        assertNull(source.getBoolean(KEY_GRAYSCALE))
        assertNull(source.getFloat(KEY_ROUNDNESS))
    }

    @Test
    fun givenAValueAlreadyInDataStore_whenMigrated_thenKeepsTheDataStoreValue() = runTest {
        // Given
        val migration = LegacySettingsMigration(
            FakeLegacySettingsSource(
                KEY_STYLE to ColorPalette.BLACK.id,
                KEY_GRAYSCALE to "true",
            ),
        )

        // When
        val migrated = migration.migrate(testPreferences(KEY_STYLE to ColorPalette.LIGHT.id))

        // Then
        assertEquals(
            testPreferences(
                KEY_STYLE to ColorPalette.LIGHT.id,
                KEY_GRAYSCALE to true,
                LEGACY_MIGRATION_PASSES_KEY to 1,
            ),
            migrated,
        )
    }

    @Test
    fun givenKepkoAndAppKeys_whenCleanedUp_thenRemovesOnlyKepkoKeys() = runTest {
        // Given
        val source = FakeLegacySettingsSource(
            KEY_STYLE to ColorPalette.BLACK.id,
            PROFILE_GRAYSCALE_KEY to "true",
            APP_KEY to "dark",
        )
        val migration = LegacySettingsMigration(source)
        migration.migrate(emptyPreferences())

        // When
        migration.cleanUp()

        // Then
        assertEquals(mapOf(APP_KEY to "dark"), source.values)
    }

    @Test
    fun givenAKepkoKeyTheMigrationDoesNotKnow_whenCleanedUp_thenLeavesIt() = runTest {
        // Given
        val source = FakeLegacySettingsSource(
            KEY_STYLE to ColorPalette.BLACK.id,
            "glass.yasan.kepko.unknown" to "value",
        )
        val migration = LegacySettingsMigration(source)
        migration.migrate(emptyPreferences())

        // When
        migration.cleanUp()

        // Then
        assertEquals(mapOf("glass.yasan.kepko.unknown" to "value"), source.values)
    }

    @Test
    fun givenAKeyAddedAfterMigrating_whenCleanedUp_thenLeavesIt() = runTest {
        // Given
        val source = FakeLegacySettingsSource(KEY_STYLE to ColorPalette.BLACK.id)
        val migration = LegacySettingsMigration(source)
        migration.migrate(emptyPreferences())
        source.values[KEY_GRAYSCALE] = "true"

        // When
        migration.cleanUp()

        // Then
        assertEquals(mapOf(KEY_GRAYSCALE to "true"), source.values)
    }

    @Test
    fun givenASourceThatCannotRemove_whenCleanedUp_thenDoesNotFail() = runTest {
        // Given
        val source = FakeLegacySettingsSource(KEY_STYLE to ColorPalette.BLACK.id).apply { failRemovals = true }
        val migration = LegacySettingsMigration(source)
        migration.migrate(emptyPreferences())

        // When
        migration.cleanUp()

        // Then
        assertEquals(mapOf(KEY_STYLE to ColorPalette.BLACK.id), source.values)
    }

    @Test
    fun givenASourceThatCannotBeListed_whenMigrated_thenFailsWithoutMarkingItDone() = runTest {
        // Given
        val source = FakeLegacySettingsSource(KEY_STYLE to ColorPalette.BLACK.id).apply { failKeys = true }

        // Then
        assertFailsWith<IllegalStateException> { LegacySettingsMigration(source).migrate(emptyPreferences()) }
    }
}
