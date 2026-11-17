package glass.yasan.kepko.persistence

import glass.yasan.kepko.foundation.annotation.ExperimentalKepkoApi
import glass.yasan.kepko.persistence.internal.KepkoDataStore
import java.io.File
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

@OptIn(ExperimentalKepkoApi::class)
internal class KepkoDesktopPersistenceTest {

    @AfterTest
    fun clearAppId() {
        KepkoDesktopPersistence.appId = null
    }

    @Test
    fun givenNoAppId_whenStoreFileResolved_thenUsesTheSharedDirectory() {
        assertEquals(
            File("/home/me/.kepko/${KepkoDataStore.FILE_NAME}"),
            KepkoDesktopPersistence.storeFile(home = "/home/me"),
        )
    }

    @Test
    fun givenAnAppId_whenStoreFileResolved_thenUsesTheAppsOwnDirectory() {
        // Given
        KepkoDesktopPersistence.appId = "com.example.app"

        // Then
        assertEquals(
            File("/home/me/.kepko/com.example.app/${KepkoDataStore.FILE_NAME}"),
            KepkoDesktopPersistence.storeFile(home = "/home/me"),
        )
    }

    @Test
    fun givenAnAppIdThatCouldLeaveTheDirectory_whenSet_thenRejectsIt() {
        listOf("..", "../other", "a/b", ".hidden", "").forEach { appId ->
            assertFailsWith<IllegalArgumentException>(appId) { KepkoDesktopPersistence.appId = appId }
        }
        assertNull(KepkoDesktopPersistence.appId)
    }
}
