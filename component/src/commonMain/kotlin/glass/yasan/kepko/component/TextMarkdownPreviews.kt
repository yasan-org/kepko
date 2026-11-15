package glass.yasan.kepko.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import glass.yasan.kepko.foundation.theme.KepkoTheme

@PreviewWithTest
@Composable
internal fun TextMarkdownInlineLightPreview() {
    KepkoTheme(palette = LIGHT) { TextMarkdownPreviewContent(TextMarkdownPreviewSamples.INLINE) }
}

@PreviewWithTest
@Composable
internal fun TextMarkdownInlineDarkPreview() {
    KepkoTheme(palette = DARK) { TextMarkdownPreviewContent(TextMarkdownPreviewSamples.INLINE) }
}

@PreviewWithTest
@Composable
internal fun TextMarkdownListsLightPreview() {
    KepkoTheme(palette = LIGHT) { TextMarkdownPreviewContent(TextMarkdownPreviewSamples.LISTS) }
}

@PreviewWithTest
@Composable
internal fun TextMarkdownListsDarkPreview() {
    KepkoTheme(palette = DARK) { TextMarkdownPreviewContent(TextMarkdownPreviewSamples.LISTS) }
}

@PreviewWithTest
@Composable
internal fun TextMarkdownNestedListsLightPreview() {
    KepkoTheme(palette = LIGHT) { TextMarkdownPreviewContent(TextMarkdownPreviewSamples.NESTED_LISTS) }
}

@PreviewWithTest
@Composable
internal fun TextMarkdownNestedListsDarkPreview() {
    KepkoTheme(palette = DARK) { TextMarkdownPreviewContent(TextMarkdownPreviewSamples.NESTED_LISTS) }
}

@PreviewWithTest
@Composable
internal fun TextMarkdownBlocksLightPreview() {
    KepkoTheme(palette = LIGHT) { TextMarkdownPreviewContent(TextMarkdownPreviewSamples.BLOCKS) }
}

@PreviewWithTest
@Composable
internal fun TextMarkdownBlocksDarkPreview() {
    KepkoTheme(palette = DARK) { TextMarkdownPreviewContent(TextMarkdownPreviewSamples.BLOCKS) }
}

@PreviewWithTest
@Composable
internal fun TextMarkdownCodeAndTablesLightPreview() {
    KepkoTheme(palette = LIGHT) { TextMarkdownPreviewContent(TextMarkdownPreviewSamples.CODE_AND_TABLES) }
}

@PreviewWithTest
@Composable
internal fun TextMarkdownCodeAndTablesDarkPreview() {
    KepkoTheme(palette = DARK) { TextMarkdownPreviewContent(TextMarkdownPreviewSamples.CODE_AND_TABLES) }
}

@PreviewWithTest
@Composable
internal fun TextMarkdownExtrasLightPreview() {
    KepkoTheme(palette = LIGHT) { TextMarkdownPreviewContent(TextMarkdownPreviewSamples.EXTRAS) }
}

@PreviewWithTest
@Composable
internal fun TextMarkdownExtrasDarkPreview() {
    KepkoTheme(palette = DARK) { TextMarkdownPreviewContent(TextMarkdownPreviewSamples.EXTRAS) }
}

@PreviewWithTest
@Composable
internal fun TextMarkdownEdgeCasesLightPreview() {
    KepkoTheme(palette = LIGHT) { TextMarkdownPreviewContent(TextMarkdownPreviewSamples.EDGE_CASES) }
}

@PreviewWithTest
@Composable
internal fun TextMarkdownEdgeCasesDarkPreview() {
    KepkoTheme(palette = DARK) { TextMarkdownPreviewContent(TextMarkdownPreviewSamples.EDGE_CASES) }
}

/**
 * Each part of [samples] between lines of `%%` as a [TextMarkdown] of its own, laid out in up to
 * three columns so a long group still fits the snapshot.
 */
@Composable
internal fun TextMarkdownPreviewContent(samples: String) {
    val texts = samples.trimIndent().split(TextMarkdownPreviewSamples.separator).map { it.trim('\n') }
    val columns = texts.chunked(size = (texts.size + PREVIEW_COLUMNS - 1) / PREVIEW_COLUMNS)

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

private const val PREVIEW_COLUMNS = 3
