package glass.yasan.kepko.persistence.internal

import androidx.datastore.core.DataStore
import androidx.datastore.core.okio.WebLocalStorage
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferencesSerializer
import kotlinx.browser.localStorage

internal actual fun createPlatformDataStore(): DataStore<Preferences> =
    PreferenceDataStoreFactory.create(
        // Not createWithPath: on web it uses sessionStorage, which is cleared when the tab closes.
        storage = WebLocalStorage(serializer = PreferencesSerializer, name = STORE_NAME),
        corruptionHandler = resetOnCorruption(),
        migrations = listOf(LegacySettingsMigration(LocalStorageLegacySource())),
    )

internal actual fun <T> blockingOrNull(block: suspend () -> T): T? = null

// A page can't wait for a coroutine while it unloads.
internal actual fun flushOnExit(flush: suspend () -> Unit): Unit = Unit

private class LocalStorageLegacySource : TextLegacySettingsSource() {

    override fun keys(): Set<String> = (0 until localStorage.length).mapNotNullTo(mutableSetOf()) { localStorage.key(it) }

    override fun getText(key: String): String? = localStorage.getItem(key)

    override fun remove(keys: Set<String>) {
        keys.forEach { localStorage.removeItem(it) }
    }
}
