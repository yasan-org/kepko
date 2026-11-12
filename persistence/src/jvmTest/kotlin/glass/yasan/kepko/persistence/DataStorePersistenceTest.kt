package glass.yasan.kepko.persistence

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import glass.yasan.kepko.foundation.theme.ColorPalette
import glass.yasan.kepko.persistence.internal.LegacySettingsMigration
import glass.yasan.kepko.persistence.internal.PersistenceManagerImpl
import glass.yasan.kepko.persistence.internal.PersistenceManagerImpl.Companion.KEY_DARK_STYLE
import glass.yasan.kepko.persistence.internal.PersistenceManagerImpl.Companion.KEY_ROUNDNESS
import glass.yasan.kepko.persistence.internal.PersistenceManagerImpl.Companion.KEY_STYLE
import glass.yasan.kepko.persistence.internal.PreferencesStore
import glass.yasan.kepko.persistence.internal.resetOnCorruption
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.job
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import java.io.File
import kotlin.io.path.createTempDirectory
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds

/**
 * Runs [PersistenceManagerImpl] against a real file DataStore, the way the JVM, Android and iOS apps do.
 */
@Suppress("VisibleForTests")
internal class DataStorePersistenceTest {

    private val directory = createTempDirectory("kepko-datastore").toFile()
    private val file = File(directory, "test.preferences_pb")

    @AfterTest
    fun deleteDirectory() {
        directory.deleteRecursively()
    }

    private fun createDataStore(
        scope: CoroutineScope,
        legacy: FakeLegacySettingsSource = FakeLegacySettingsSource(),
    ): DataStore<Preferences> = PreferenceDataStoreFactory.create(
        corruptionHandler = resetOnCorruption(),
        migrations = listOf(LegacySettingsMigration(legacy)),
        scope = scope,
        produceFile = { file },
    )

    @Test
    fun givenLegacyValues_whenChangedAndReopened_thenKeepsMigratedAndChangedValues() = runBlocking {
        // Given
        val legacy = FakeLegacySettingsSource(KEY_STYLE to ColorPalette.BLACK.id, "app.theme" to "dark")
        val firstScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        val firstDataStore = createDataStore(firstScope, legacy)
        val first = PersistenceManagerImpl(PreferencesStore.load(dataStore = firstDataStore, scope = firstScope))

        // When
        first.paletteDark = ColorPalette.SOLARIZED_DARK
        first.roundness = 0.5f
        withTimeout(5.seconds) {
            firstDataStore.data.first { preferences ->
                preferences[stringPreferencesKey(KEY_DARK_STYLE)] == ColorPalette.SOLARIZED_DARK.id &&
                    preferences[floatPreferencesKey(KEY_ROUNDNESS)] == 0.5f
            }
        }
        firstScope.coroutineContext.job.cancelAndJoin()

        val secondScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        val second = PersistenceManagerImpl(
            PreferencesStore.load(dataStore = createDataStore(secondScope), scope = secondScope),
        )

        // Then
        assertEquals(ColorPalette.BLACK, second.getPalettePrimary(null))
        assertEquals(ColorPalette.SOLARIZED_DARK, second.paletteDark)
        assertEquals(0.5f, second.roundness)
        assertEquals(mapOf("app.theme" to "dark"), legacy.values)
        secondScope.coroutineContext.job.cancelAndJoin()
    }

    @Test
    fun givenACorruptFile_whenLoaded_thenStartsFromDefaults() = runBlocking {
        // Given
        directory.mkdirs()
        file.writeBytes(byteArrayOf(0x7f, 0x01, 0x02, 0x03))
        val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

        // When
        val manager = PersistenceManagerImpl(
            PreferencesStore.load(dataStore = createDataStore(scope), scope = scope),
        )

        // Then
        assertNull(manager.getPalettePrimary(null))
        assertTrue(manager.isDefault)
        scope.coroutineContext.job.cancelAndJoin()
    }
}
