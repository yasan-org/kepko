package glass.yasan.kepko.persistence.internal

import androidx.datastore.core.DataMigration
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.Preferences

/**
 * Creates a DataStore at this platform's storage location.
 */
internal expect fun createPlatformDataStore(
    corruptionHandler: ReplaceFileCorruptionHandler<Preferences>,
    migrations: List<DataMigration<Preferences>>,
): DataStore<Preferences>

/**
 * Runs [block] on the calling thread, or returns null on platforms that can't block.
 */
internal expect fun <T> blockingOrNull(block: suspend () -> T): T?
