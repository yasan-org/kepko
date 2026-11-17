package glass.yasan.kepko.persistence.internal.legacy

import android.content.Context
import glass.yasan.kepko.persistence.internal.KepkoPersistenceInitializer

internal actual fun createPlatformLegacySettingsSource(): LegacySettingsSource {
    val context = KepkoPersistenceInitializer.requireContext()

    return SharedPreferencesLegacySource {
        // The file multiplatform-settings' no-arg Settings() wrote to.
        context.getSharedPreferences("${context.packageName}_preferences", Context.MODE_PRIVATE)
    }
}
