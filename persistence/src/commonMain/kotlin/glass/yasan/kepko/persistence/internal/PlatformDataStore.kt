package glass.yasan.kepko.persistence.internal

import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences

/**
 * Underscores, not dots, so the web localStorage key never matches [LEGACY_KEY_PREFIX].
 */
internal const val STORE_NAME: String = "glass_yasan_kepko"

internal const val STORE_FILE_NAME: String = "$STORE_NAME.preferences_pb"

/**
 * Creates Kepko's DataStore, migrating values stored by multiplatform-settings on first use.
 */
internal expect fun createPlatformDataStore(): DataStore<Preferences>

/**
 * Runs [block] on the calling thread, or returns null on platforms that can't block.
 */
internal expect fun <T> blockingOrNull(block: suspend () -> T): T?

/**
 * Runs [flush] before the process exits, on platforms that can wait for it. Elsewhere a change made in the
 * last moments before exit may not be saved.
 */
internal expect fun flushOnExit(flush: suspend () -> Unit)

internal fun resetOnCorruption(): ReplaceFileCorruptionHandler<Preferences> =
    ReplaceFileCorruptionHandler { emptyPreferences() }
