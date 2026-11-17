package glass.yasan.kepko.persistence.internal

import androidx.datastore.core.DataMigration
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.runBlocking
import okio.Path.Companion.toPath
import platform.Foundation.NSApplicationSupportDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

internal actual fun createPlatformDataStore(
    corruptionHandler: ReplaceFileCorruptionHandler<Preferences>,
    migrations: List<DataMigration<Preferences>>,
): DataStore<Preferences> =
    PreferenceDataStoreFactory.createWithPath(
        corruptionHandler = corruptionHandler,
        migrations = migrations,
        produceFile = { "${applicationSupportDirectory()}/${KepkoDataStore.FILE_NAME}".toPath() },
    )

internal actual fun <T> blockingOrNull(block: suspend () -> T): T? = runBlocking { block() }

// Not Documents, which apps may expose in the Files app.
@OptIn(ExperimentalForeignApi::class)
private fun applicationSupportDirectory(): String {
    val url = NSFileManager.defaultManager.URLForDirectory(
        directory = NSApplicationSupportDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = true,
        error = null,
    )
    return checkNotNull(url?.path) { "The Application Support directory is unavailable." }
}
