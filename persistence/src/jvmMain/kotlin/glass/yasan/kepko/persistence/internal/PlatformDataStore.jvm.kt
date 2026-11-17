package glass.yasan.kepko.persistence.internal

import androidx.datastore.core.DataMigration
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import glass.yasan.kepko.foundation.annotation.ExperimentalKepkoApi
import glass.yasan.kepko.persistence.KepkoDesktopPersistence
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalKepkoApi::class)
internal actual fun createPlatformDataStore(
    corruptionHandler: ReplaceFileCorruptionHandler<Preferences>,
    migrations: List<DataMigration<Preferences>>,
): DataStore<Preferences> =
    PreferenceDataStoreFactory.create(
        corruptionHandler = corruptionHandler,
        migrations = migrations,
        produceFile = { KepkoDesktopPersistence.storeFile(home = System.getProperty("user.home")) },
    )

internal actual fun <T> blockingOrNull(block: suspend () -> T): T? = runBlocking { block() }
