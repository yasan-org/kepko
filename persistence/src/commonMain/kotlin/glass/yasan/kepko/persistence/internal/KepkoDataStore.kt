package glass.yasan.kepko.persistence.internal

import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import glass.yasan.kepko.persistence.internal.legacy.LegacySettingsMigration
import glass.yasan.kepko.persistence.internal.legacy.createPlatformLegacySettingsSource

/**
 * The DataStore Kepko keeps its preferences in.
 */
internal object KepkoDataStore {

    // Underscores, not dots, so the web localStorage key never looks like one multiplatform-settings stored.
    const val NAME: String = "glass_yasan_kepko"

    const val FILE_NAME: String = "$NAME.preferences_pb"

    /**
     * Creates the store, which resets itself if its file is corrupt and brings over the values
     * multiplatform-settings stored on first use.
     */
    fun create(): DataStore<Preferences> =
        createPlatformDataStore(
            corruptionHandler = ReplaceFileCorruptionHandler { emptyPreferences() },
            migrations = listOf(LegacySettingsMigration(createPlatformLegacySettingsSource())),
        )
}
