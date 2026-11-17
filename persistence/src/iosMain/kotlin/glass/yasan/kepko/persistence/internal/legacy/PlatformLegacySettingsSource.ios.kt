package glass.yasan.kepko.persistence.internal.legacy

import platform.Foundation.NSUserDefaults

internal actual fun createPlatformLegacySettingsSource(): LegacySettingsSource =
    UserDefaultsLegacySource(NSUserDefaults.standardUserDefaults)
