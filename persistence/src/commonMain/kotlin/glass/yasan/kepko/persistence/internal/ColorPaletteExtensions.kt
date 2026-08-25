package glass.yasan.kepko.persistence.internal

import glass.yasan.kepko.component.Badge
import glass.yasan.kepko.component.PreferenceRadioGroupItem
import glass.yasan.kepko.foundation.theme.ColorPalette

internal fun ColorPalette.asPreferenceRadioGroupItem(
    segment: Int = 0,
    isDefault: Boolean = false,
): PreferenceRadioGroupItem = PreferenceRadioGroupItem(
    id = id,
    segment = segment,
    badge = when {
        isDefault -> Badge.default
        else -> null
    },
) {
    title()
}
