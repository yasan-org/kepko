package glass.yasan.kepko.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import glass.yasan.kepko.foundation.theme.KepkoTheme

public object PreferenceSegmentTitleDefaults {

    @Composable
    public fun colors(
        contentColor: Color = KepkoTheme.colors.contentSubtle,
    ): PreferenceSegmentTitleColors = PreferenceSegmentTitleColors(
        contentColor = contentColor,
    )
}
