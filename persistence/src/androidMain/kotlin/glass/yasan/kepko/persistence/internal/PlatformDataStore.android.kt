package glass.yasan.kepko.persistence.internal

import androidx.datastore.core.DataMigration
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.runBlocking
import java.io.File

internal actual fun createPlatformDataStore(
    corruptionHandler: ReplaceFileCorruptionHandler<Preferences>,
    migrations: List<DataMigration<Preferences>>,
): DataStore<Preferences> {
    val context = KepkoPersistenceInitializer.requireContext()

    return PreferenceDataStoreFactory.create(
        corruptionHandler = corruptionHandler,
        migrations = migrations,
        produceFile = { File(context.filesDir, "datastore/${KepkoDataStore.FILE_NAME}") },
    )
}

internal actual fun <T> blockingOrNull(block: suspend () -> T): T? = runBlocking { block() }
