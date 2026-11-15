package glass.yasan.kepko.markdown

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import glass.yasan.kepko.component.Text
import glass.yasan.kepko.foundation.theme.ColorPalette
import glass.yasan.kepko.foundation.theme.KepkoTheme

@PreviewWithTest
@Composable
internal fun TextMarkdownLightPreview() {
    TextMarkdownPreviewContent(palette = LIGHT)
}

@PreviewWithTest
@Composable
internal fun TextMarkdownDarkPreview() {
    TextMarkdownPreviewContent(palette = DARK)
}

@PreviewWithTest
@Composable
internal fun TextMarkdownBlackPreview() {
    TextMarkdownPreviewContent(palette = BLACK)
}

@PreviewWithTest
@Composable
internal fun TextMarkdownSolarizedLightPreview() {
    TextMarkdownPreviewContent(palette = SOLARIZED_LIGHT)
}

@PreviewWithTest
@Composable
internal fun TextMarkdownSolarizedDarkPreview() {
    TextMarkdownPreviewContent(palette = SOLARIZED_DARK)
}

@PreviewWithTest
@Composable
internal fun TextMarkdownCatppuccinLattePreview() {
    TextMarkdownPreviewContent(palette = CATPPUCCIN_LATTE)
}

@PreviewWithTest
@Composable
internal fun TextMarkdownCatppuccinFrappePreview() {
    TextMarkdownPreviewContent(palette = CATPPUCCIN_FRAPPE)
}

@PreviewWithTest
@Composable
internal fun TextMarkdownCatppuccinMacchiatoPreview() {
    TextMarkdownPreviewContent(palette = CATPPUCCIN_MACCHIATO)
}

@PreviewWithTest
@Composable
internal fun TextMarkdownCatppuccinMochaPreview() {
    TextMarkdownPreviewContent(palette = CATPPUCCIN_MOCHA)
}

@PreviewWithTest
@Composable
internal fun TextMarkdownGruvboxLightPreview() {
    TextMarkdownPreviewContent(palette = GRUVBOX_LIGHT)
}

@PreviewWithTest
@Composable
internal fun TextMarkdownGruvboxDarkPreview() {
    TextMarkdownPreviewContent(palette = GRUVBOX_DARK)
}

@PreviewWithTest
@Composable
internal fun TextMarkdownTokyoNightPreview() {
    TextMarkdownPreviewContent(palette = TOKYO_NIGHT)
}

@PreviewWithTest
@Composable
internal fun TextMarkdownTokyoDayPreview() {
    TextMarkdownPreviewContent(palette = TOKYO_DAY)
}

@PreviewWithTest
@Composable
internal fun TextMarkdownBabyPinkPreview() {
    TextMarkdownPreviewContent(palette = BABY_PINK)
}

@PreviewWithTest
@Composable
internal fun TextMarkdownDarkPinkPreview() {
    TextMarkdownPreviewContent(palette = DARK_PINK)
}

@PreviewWithTest
@Composable
internal fun TextMarkdownHotPinkPreview() {
    TextMarkdownPreviewContent(palette = HOT_PINK)
}

/**
 * Every group of [TextMarkdownPreviewSamples] under its name, each sample a [TextMarkdown] of its
 * own, so one snapshot of a palette shows how all of markdown is rendered in it.
 */
@Composable
private fun TextMarkdownPreviewContent(palette: ColorPalette) {
    KepkoTheme(palette = palette) {
        Column(
            verticalArrangement = Arrangement.spacedBy(32.dp),
            modifier = Modifier
                .background(KepkoTheme.colors.midground)
                .padding(24.dp),
        ) {
            TextMarkdownPreviewSamples.groups.forEach { (name, samples) ->
                TextMarkdownPreviewGroup(name = name, samples = samples)
            }
        }
    }
}

@Composable
private fun TextMarkdownPreviewGroup(
    name: String,
    samples: String,
) {
    val texts = samples.trimIndent().split(TextMarkdownPreviewSamples.separator).map { it.trim('\n') }
    val columns = texts.chunked(size = (texts.size + PREVIEW_COLUMNS - 1) / PREVIEW_COLUMNS)

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = name.uppercase(),
            color = KepkoTheme.colors.contentSubtle,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
        )
        Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
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
