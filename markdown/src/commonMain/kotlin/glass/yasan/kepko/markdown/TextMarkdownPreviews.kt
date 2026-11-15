package glass.yasan.kepko.markdown

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
 * Each part of [samples] between lines of `%%` as a [TextMarkdown] of its own, laid out in up to
 * four columns.
 */
@Composable
internal fun TextMarkdownPreviewContent(
    samples: String,
    palette: ColorPalette = LIGHT,
) {
    val texts = samples.trimIndent().split(TextMarkdownPreviewSamples.separator).map { it.trim('\n') }
    val columns = texts.chunked(size = (texts.size + PREVIEW_COLUMNS - 1) / PREVIEW_COLUMNS)

    KepkoTheme(palette = palette) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier
                .background(KepkoTheme.colors.midground)
                .padding(16.dp),
        ) {
            columns.forEach { column ->
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.width(296.dp),
                ) {
                    column.forEach { text -> TextMarkdown(text = text) }
                }
            }
        }
    }
}

private const val PREVIEW_COLUMNS = 4
