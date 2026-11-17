package glass.yasan.kepko.persistence.internal.legacy

/**
 * Creates the source for the store multiplatform-settings wrote to on this platform.
 */
internal expect fun createPlatformLegacySettingsSource(): LegacySettingsSource
