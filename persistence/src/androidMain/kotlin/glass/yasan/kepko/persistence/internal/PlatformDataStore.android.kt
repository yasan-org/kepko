package glass.yasan.kepko.persistence.internal

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.runBlocking
import java.io.File

internal actual fun createPlatformDataStore(): DataStore<Preferences> {
    val context = checkNotNull(applicationContext) {
        "Kepko persistence isn't initialized. Keep androidx.startup's InitializationProvider in the merged manifest."
    }
    // The file multiplatform-settings' no-arg Settings() wrote to.
    val legacyPreferences = context.getSharedPreferences("${context.packageName}_preferences", Context.MODE_PRIVATE)

    return PreferenceDataStoreFactory.create(
        corruptionHandler = resetOnCorruption(),
        migrations = listOf(LegacySettingsMigration(SharedPreferencesLegacySource(legacyPreferences))),
        produceFile = { File(context.filesDir, "datastore/$STORE_FILE_NAME") },
    )
}

internal actual fun <T> blockingOrNull(block: suspend () -> T): T? = runBlocking { block() }

// Android gives no exit callback; DataStore writes finish within milliseconds of a change.
internal actual fun flushOnExit(flush: suspend () -> Unit): Unit = Unit

private class SharedPreferencesLegacySource(
    private val preferences: SharedPreferences,
) : LegacySettingsSource {

    // getAll() copies the whole file's map, so it's read once. The migration runs once per process.
    private val all: Map<String, *> by lazy { preferences.all }

    override fun keys(): Set<String> = all.keys

    override fun getString(key: String): String? = all[key] as? String

    override fun getBoolean(key: String): Boolean? = all[key] as? Boolean

    override fun getFloat(key: String): Float? = all[key] as? Float

    override fun remove(keys: Set<String>) {
        val editor = preferences.edit()
        keys.forEach { editor.remove(it) }
        editor.apply()
    }
}
