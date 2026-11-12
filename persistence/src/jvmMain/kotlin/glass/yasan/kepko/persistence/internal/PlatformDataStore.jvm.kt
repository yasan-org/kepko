package glass.yasan.kepko.persistence.internal

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import glass.yasan.kepko.foundation.annotation.ExperimentalKepkoApi
import glass.yasan.kepko.persistence.KepkoDesktopPersistence
import kotlinx.coroutines.runBlocking
import java.io.File
import java.util.prefs.BackingStoreException
import java.util.prefs.Preferences as JavaPreferences

@OptIn(ExperimentalKepkoApi::class)
internal actual fun createPlatformDataStore(): DataStore<Preferences> =
    createDesktopDataStore(home = System.getProperty("user.home"), legacyPreferences = JavaPreferences.userRoot())

/**
 * Creates the DataStore under [home], migrating the values multiplatform-settings stored in [legacyPreferences].
 */
@OptIn(ExperimentalKepkoApi::class)
internal fun createDesktopDataStore(home: String, legacyPreferences: JavaPreferences): DataStore<Preferences> =
    PreferenceDataStoreFactory.create(
        corruptionHandler = resetOnCorruption(),
        migrations = listOf(LegacySettingsMigration(JavaPreferencesLegacySource(legacyPreferences))),
        produceFile = { desktopStoreFile(home = home, appId = KepkoDesktopPersistence.appId) },
    )

/**
 * `<home>/.kepko/<appId>/`, or `<home>/.kepko/` shared by every app that sets no [appId].
 */
internal fun desktopStoreFile(home: String, appId: String?): File {
    val directory = File(home, ".kepko").let { root -> if (appId == null) root else File(root, appId) }
    return File(directory, STORE_FILE_NAME)
}

internal actual fun <T> blockingOrNull(block: suspend () -> T): T? = runBlocking { block() }

internal actual fun flushOnExit(flush: suspend () -> Unit) {
    Runtime.getRuntime().addShutdownHook(Thread { runBlocking { flush() } })
}

internal class JavaPreferencesLegacySource(
    private val preferences: JavaPreferences,
) : TextLegacySettingsSource() {

    override fun keys(): Set<String> = try {
        preferences.keys().toSet()
    } catch (_: BackingStoreException) {
        emptySet()
    }

    override fun getText(key: String): String? = preferences.get(key, null)

    override fun remove(keys: Set<String>) {
        keys.forEach { preferences.remove(it) }
        try {
            preferences.flush()
        } catch (_: BackingStoreException) {
            // The JVM still flushes the removals later.
        }
    }
}
