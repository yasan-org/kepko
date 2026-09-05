package glass.yasan.kepko.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import glass.yasan.kepko.foundation.theme.KepkoTheme

@Composable
public fun PreferenceSegmentTitle(
    title: String,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(
        start = 16.dp,
        top = 24.dp,
        end = 16.dp,
        bottom = 4.dp,
    ),
    colors: PreferenceSegmentTitleColors = PreferenceSegmentTitleDefaults.colors(),
) {
    Text(
        text = title.uppercase(),
        color = colors.contentColor,
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        textAlign = TextAlign.Start,
        modifier = modifier
            .fillMaxWidth()
            .padding(contentPadding),
    )
}

@PreviewWithTest
@Composable
internal fun PreferenceSegmentTitleLightPreview() {
    KepkoTheme(palette = LIGHT) { PreviewContent() }
}

@PreviewWithTest
@Composable
internal fun PreferenceSegmentTitleDarkPreview() {
    KepkoTheme(palette = DARK) { PreviewContent() }
}

@PreviewWithTest
@Composable
internal fun PreferenceSegmentTitleBlackPreview() {
    KepkoTheme(palette = BLACK) { PreviewContent() }
}

@PreviewWithTest
@Composable
internal fun PreferenceSegmentTitleSolarizedLightPreview() {
    KepkoTheme(palette = SOLARIZED_LIGHT) { PreviewContent() }
}

@PreviewWithTest
@Composable
internal fun PreferenceSegmentTitleSolarizedDarkPreview() {
    KepkoTheme(palette = SOLARIZED_DARK) { PreviewContent() }
}

@Composable
private fun PreviewContent() {
    Column(
        modifier = Modifier
            .background(KepkoTheme.colors.midground)
            .padding(16.dp),
    ) {
        PreferenceSegmentTitle(title = "General")
        PreferenceSwitch(
            title = "PreferenceSwitch",
            checked = true,
            onCheckedChange = {},
        )
        PreferenceSegmentTitle(title = "Advanced")
        PreferenceSwitch(
            title = "PreferenceSwitch",
            checked = false,
            onCheckedChange = {},
        )
    }
}
