package glass.yasan.kepko.markdown

import androidx.compose.runtime.Composable

@PreviewWithTest
@Composable
internal fun TextMarkdownBulletListsPreview() {
    TextMarkdownPreviewContent(samples = TextMarkdownPreviewSamples.BULLET_LISTS)
}

@PreviewWithTest
@Composable
internal fun TextMarkdownNumberedListsPreview() {
    TextMarkdownPreviewContent(samples = TextMarkdownPreviewSamples.NUMBERED_LISTS)
}

@PreviewWithTest
@Composable
internal fun TextMarkdownNestedListsPreview() {
    TextMarkdownPreviewContent(samples = TextMarkdownPreviewSamples.NESTED_LISTS)
}

@PreviewWithTest
@Composable
internal fun TextMarkdownListItemsPreview() {
    TextMarkdownPreviewContent(samples = TextMarkdownPreviewSamples.LIST_ITEMS)
}

@PreviewWithTest
@Composable
internal fun TextMarkdownBlockQuotesPreview() {
    TextMarkdownPreviewContent(samples = TextMarkdownPreviewSamples.BLOCK_QUOTES)
}

@PreviewWithTest
@Composable
internal fun TextMarkdownBlockQuoteContentPreview() {
    TextMarkdownPreviewContent(samples = TextMarkdownPreviewSamples.BLOCK_QUOTE_CONTENT)
}

@PreviewWithTest
@Composable
internal fun TextMarkdownThematicBreaksPreview() {
    TextMarkdownPreviewContent(samples = TextMarkdownPreviewSamples.THEMATIC_BREAKS)
}

@PreviewWithTest
@Composable
internal fun TextMarkdownEscapesPreview() {
    TextMarkdownPreviewContent(samples = TextMarkdownPreviewSamples.ESCAPES)
}

@PreviewWithTest
@Composable
internal fun TextMarkdownEntitiesPreview() {
    TextMarkdownPreviewContent(samples = TextMarkdownPreviewSamples.ENTITIES)
}

@PreviewWithTest
@Composable
internal fun TextMarkdownHtmlPreview() {
    TextMarkdownPreviewContent(samples = TextMarkdownPreviewSamples.HTML)
}
