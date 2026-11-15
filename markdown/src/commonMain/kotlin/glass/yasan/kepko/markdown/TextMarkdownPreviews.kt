package glass.yasan.kepko.markdown

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import glass.yasan.kepko.foundation.theme.ColorPalette
import glass.yasan.kepko.foundation.theme.KepkoTheme

@PreviewWithTest
@Composable
internal fun TextMarkdownLightPreview() {
    TextMarkdownPreviewContent(samples = TextMarkdownPreviewSamples.OVERVIEW, palette = LIGHT)
}

@PreviewWithTest
@Composable
internal fun TextMarkdownDarkPreview() {
    TextMarkdownPreviewContent(samples = TextMarkdownPreviewSamples.OVERVIEW, palette = DARK)
}

@PreviewWithTest
@Composable
internal fun TextMarkdownBlackPreview() {
    TextMarkdownPreviewContent(samples = TextMarkdownPreviewSamples.OVERVIEW, palette = BLACK)
}

@PreviewWithTest
@Composable
internal fun TextMarkdownSolarizedLightPreview() {
    TextMarkdownPreviewContent(samples = TextMarkdownPreviewSamples.OVERVIEW, palette = SOLARIZED_LIGHT)
}

@PreviewWithTest
@Composable
internal fun TextMarkdownSolarizedDarkPreview() {
    TextMarkdownPreviewContent(samples = TextMarkdownPreviewSamples.OVERVIEW, palette = SOLARIZED_DARK)
}

@PreviewWithTest
@Composable
internal fun TextMarkdownCatppuccinLattePreview() {
    TextMarkdownPreviewContent(samples = TextMarkdownPreviewSamples.OVERVIEW, palette = CATPPUCCIN_LATTE)
}

@PreviewWithTest
@Composable
internal fun TextMarkdownCatppuccinFrappePreview() {
    TextMarkdownPreviewContent(samples = TextMarkdownPreviewSamples.OVERVIEW, palette = CATPPUCCIN_FRAPPE)
}

@PreviewWithTest
@Composable
internal fun TextMarkdownCatppuccinMacchiatoPreview() {
    TextMarkdownPreviewContent(samples = TextMarkdownPreviewSamples.OVERVIEW, palette = CATPPUCCIN_MACCHIATO)
}

@PreviewWithTest
@Composable
internal fun TextMarkdownCatppuccinMochaPreview() {
    TextMarkdownPreviewContent(samples = TextMarkdownPreviewSamples.OVERVIEW, palette = CATPPUCCIN_MOCHA)
}

@PreviewWithTest
@Composable
internal fun TextMarkdownGruvboxLightPreview() {
    TextMarkdownPreviewContent(samples = TextMarkdownPreviewSamples.OVERVIEW, palette = GRUVBOX_LIGHT)
}

@PreviewWithTest
@Composable
internal fun TextMarkdownGruvboxDarkPreview() {
    TextMarkdownPreviewContent(samples = TextMarkdownPreviewSamples.OVERVIEW, palette = GRUVBOX_DARK)
}

@PreviewWithTest
@Composable
internal fun TextMarkdownTokyoNightPreview() {
    TextMarkdownPreviewContent(samples = TextMarkdownPreviewSamples.OVERVIEW, palette = TOKYO_NIGHT)
}

@PreviewWithTest
@Composable
internal fun TextMarkdownTokyoDayPreview() {
    TextMarkdownPreviewContent(samples = TextMarkdownPreviewSamples.OVERVIEW, palette = TOKYO_DAY)
}

@PreviewWithTest
@Composable
internal fun TextMarkdownBabyPinkPreview() {
    TextMarkdownPreviewContent(samples = TextMarkdownPreviewSamples.OVERVIEW, palette = BABY_PINK)
}

@PreviewWithTest
@Composable
internal fun TextMarkdownDarkPinkPreview() {
    TextMarkdownPreviewContent(samples = TextMarkdownPreviewSamples.OVERVIEW, palette = DARK_PINK)
}

@PreviewWithTest
@Composable
internal fun TextMarkdownHotPinkPreview() {
    TextMarkdownPreviewContent(samples = TextMarkdownPreviewSamples.OVERVIEW, palette = HOT_PINK)
}

/**
 * Each part of [samples] between lines of `%%` as a [TextMarkdown] of its own, one under another.
 */
@Composable
internal fun TextMarkdownPreviewContent(
    samples: String,
    palette: ColorPalette = LIGHT,
    imageDomains: List<String> = emptyList(),
) {
    val texts = samples.trimIndent().split(TextMarkdownPreviewSamples.separator).map { it.trim('\n') }

    KepkoTheme(palette = palette) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .background(KepkoTheme.colors.midground)
                .padding(16.dp)
                .width(328.dp),
        ) {
            texts.forEach { text -> TextMarkdown(text = text, imageDomains = imageDomains) }
        }
    }
}
