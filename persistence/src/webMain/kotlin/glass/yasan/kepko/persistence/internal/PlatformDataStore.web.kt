package glass.yasan.kepko.persistence.internal

import androidx.datastore.core.DataMigration
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.core.okio.WebLocalStorage
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferencesSerializer

internal actual fun createPlatformDataStore(
    corruptionHandler: ReplaceFileCorruptionHandler<Preferences>,
    migrations: List<DataMigration<Preferences>>,
): DataStore<Preferences> =
    PreferenceDataStoreFactory.create(
        // Not createWithPath: on web it uses sessionStorage, which is cleared when the tab closes.
        storage = WebLocalStorage(serializer = PreferencesSerializer, name = KepkoDataStore.NAME),
        corruptionHandler = corruptionHandler,
        migrations = migrations,
    )

internal actual fun <T> blockingOrNull(block: suspend () -> T): T? = null
