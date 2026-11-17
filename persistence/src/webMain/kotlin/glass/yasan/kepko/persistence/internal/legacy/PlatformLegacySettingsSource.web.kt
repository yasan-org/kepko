package glass.yasan.kepko.persistence.internal.legacy

internal actual fun createPlatformLegacySettingsSource(): LegacySettingsSource = LocalStorageLegacySource()
