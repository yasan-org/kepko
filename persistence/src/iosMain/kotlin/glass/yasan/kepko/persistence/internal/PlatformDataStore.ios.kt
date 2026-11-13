package glass.yasan.kepko.persistence.internal

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.runBlocking
import okio.Path.Companion.toPath
import platform.Foundation.NSApplicationSupportDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDefaults
import platform.Foundation.NSUserDomainMask

internal actual fun createPlatformDataStore(): DataStore<Preferences> =
    PreferenceDataStoreFactory.createWithPath(
        corruptionHandler = resetOnCorruption(),
        migrations = listOf(LegacySettingsMigration(UserDefaultsLegacySource(NSUserDefaults.standardUserDefaults))),
        produceFile = { "${applicationSupportDirectory()}/$STORE_FILE_NAME".toPath() },
    )

internal actual fun <T> blockingOrNull(block: suspend () -> T): T? = runBlocking { block() }

// iOS gives no exit callback; DataStore writes finish within milliseconds of a change.
internal actual fun flushOnExit(flush: suspend () -> Unit): Unit = Unit

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

private class UserDefaultsLegacySource(
    private val defaults: NSUserDefaults,
) : LegacySettingsSource {

    override fun keys(): Set<String> = defaults.dictionaryRepresentation().keys.filterIsInstance<String>().toSet()

    override fun getString(key: String): String? = defaults.stringForKey(key)

    override fun getBoolean(key: String): Boolean? = if (hasKey(key)) defaults.boolForKey(key) else null

    override fun getFloat(key: String): Float? = if (hasKey(key)) defaults.floatForKey(key) else null

    override fun remove(keys: Set<String>) {
        keys.forEach { defaults.removeObjectForKey(it) }
    }

    private fun hasKey(key: String): Boolean = defaults.objectForKey(key) != null
}
