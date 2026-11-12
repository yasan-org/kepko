package glass.yasan.kepko.persistence

import androidx.datastore.preferences.core.stringPreferencesKey
import glass.yasan.kepko.foundation.annotation.ExperimentalKepkoApi
import glass.yasan.kepko.persistence.internal.JavaPreferencesLegacySource
import glass.yasan.kepko.persistence.internal.STORE_FILE_NAME
import glass.yasan.kepko.persistence.internal.createDesktopDataStore
import glass.yasan.kepko.persistence.internal.desktopStoreFile
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.io.File
import java.util.UUID
import java.util.prefs.Preferences
import kotlin.io.path.createTempDirectory
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalKepkoApi::class)
internal class DesktopPlatformDataStoreTest {

    // A private node, so the test never touches keys real apps stored in the root.
    private val node = Preferences.userRoot().node("kepko-test-${UUID.randomUUID()}")

    @AfterTest
    fun cleanUp() {
        node.removeNode()
        KepkoDesktopPersistence.appId = null
    }

    @Test
    fun givenNoAppId_whenStoreFileResolved_thenUsesTheSharedDirectory() {
        assertEquals(File("/home/me/.kepko/$STORE_FILE_NAME"), desktopStoreFile(home = "/home/me", appId = null))
    }

    @Test
    fun givenAnAppId_whenStoreFileResolved_thenUsesTheAppsOwnDirectory() {
        assertEquals(
            File("/home/me/.kepko/com.example.app/$STORE_FILE_NAME"),
            desktopStoreFile(home = "/home/me", appId = "com.example.app"),
        )
    }

    @Test
    fun givenAnAppIdThatCouldLeaveTheDirectory_whenSet_thenRejectsIt() {
        listOf("..", "../other", "a/b", ".hidden", "").forEach { appId ->
            assertFailsWith<IllegalArgumentException>(appId) { KepkoDesktopPersistence.appId = appId }
        }
        assertNull(KepkoDesktopPersistence.appId)
    }

    @Test
    fun givenValuesWrittenTheWayMultiplatformSettingsDid_whenRead_thenParsesEachType() {
        // Given
        node.put("glass.yasan.kepko.style.primary", "black")
        node.putBoolean("glass.yasan.kepko.grayscale", true)
        node.putFloat("glass.yasan.kepko.dimension.roundness", 0.5f)
        val source = JavaPreferencesLegacySource(node)

        // Then
        assertEquals(
            setOf(
                "glass.yasan.kepko.style.primary",
                "glass.yasan.kepko.grayscale",
                "glass.yasan.kepko.dimension.roundness",
            ),
            source.keys(),
        )
        assertEquals("black", source.getString("glass.yasan.kepko.style.primary"))
        assertEquals(true, source.getBoolean("glass.yasan.kepko.grayscale"))
        assertEquals(0.5f, source.getFloat("glass.yasan.kepko.dimension.roundness"))
        assertNull(source.getString("glass.yasan.kepko.missing"))
    }

    @Test
    fun givenLegacyValues_whenDesktopDataStoreRead_thenMigratesThemIntoTheAppsFile() = runBlocking {
        // Given
        val home = createTempDirectory("kepko-home").toFile()
        node.put("glass.yasan.kepko.style.primary", "black")
        node.put("app.theme", "dark")
        KepkoDesktopPersistence.appId = "com.example.app"

        try {
            // When
            val preferences = createDesktopDataStore(home = home.path, legacyPreferences = node).data.first()

            // Then
            assertEquals("black", preferences[stringPreferencesKey("glass.yasan.kepko.style.primary")])
            assertTrue(desktopStoreFile(home = home.path, appId = "com.example.app").exists())
            assertEquals(listOf("app.theme"), node.keys().toList())
        } finally {
            home.deleteRecursively()
        }
    }

    @Test
    fun givenStoredKeys_whenSomeRemoved_thenKeepsTheRest() {
        // Given
        node.put("glass.yasan.kepko.style.primary", "black")
        node.put("app.theme", "dark")
        val source = JavaPreferencesLegacySource(node)

        // When
        source.remove(setOf("glass.yasan.kepko.style.primary"))

        // Then
        assertEquals(setOf("app.theme"), source.keys())
    }
}
