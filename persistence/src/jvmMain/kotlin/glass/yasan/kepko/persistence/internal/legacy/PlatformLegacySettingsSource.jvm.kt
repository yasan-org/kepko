package glass.yasan.kepko.persistence.internal.legacy

import java.util.prefs.Preferences

internal actual fun createPlatformLegacySettingsSource(): LegacySettingsSource =
    JavaPreferencesLegacySource(Preferences::userRoot)
