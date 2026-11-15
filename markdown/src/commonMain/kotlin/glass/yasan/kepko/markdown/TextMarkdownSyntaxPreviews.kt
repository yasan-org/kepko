package glass.yasan.kepko.markdown

import androidx.compose.runtime.Composable

@PreviewWithTest
@Composable
internal fun TextMarkdownHeadingsPreview() {
    TextMarkdownPreviewContent(samples = TextMarkdownPreviewSamples.HEADINGS)
}

@PreviewWithTest
@Composable
internal fun TextMarkdownHeadingFormsPreview() {
    TextMarkdownPreviewContent(samples = TextMarkdownPreviewSamples.HEADING_FORMS)
}

@PreviewWithTest
@Composable
internal fun TextMarkdownParagraphsPreview() {
    TextMarkdownPreviewContent(samples = TextMarkdownPreviewSamples.PARAGRAPHS)
}

@PreviewWithTest
@Composable
internal fun TextMarkdownLineBreaksPreview() {
    TextMarkdownPreviewContent(samples = TextMarkdownPreviewSamples.LINE_BREAKS)
}

@PreviewWithTest
@Composable
internal fun TextMarkdownEmphasisPreview() {
    TextMarkdownPreviewContent(samples = TextMarkdownPreviewSamples.EMPHASIS)
}

@PreviewWithTest
@Composable
internal fun TextMarkdownStrikethroughPreview() {
    TextMarkdownPreviewContent(samples = TextMarkdownPreviewSamples.STRIKETHROUGH)
}

@PreviewWithTest
@Composable
internal fun TextMarkdownInlineCodePreview() {
    TextMarkdownPreviewContent(samples = TextMarkdownPreviewSamples.INLINE_CODE)
}

@PreviewWithTest
@Composable
internal fun TextMarkdownCodeBlocksPreview() {
    TextMarkdownPreviewContent(samples = TextMarkdownPreviewSamples.CODE_BLOCKS)
}

@PreviewWithTest
@Composable
internal fun TextMarkdownLinksPreview() {
    TextMarkdownPreviewContent(samples = TextMarkdownPreviewSamples.LINKS)
}

@PreviewWithTest
@Composable
internal fun TextMarkdownReferenceLinksAndAutolinksPreview() {
    TextMarkdownPreviewContent(samples = TextMarkdownPreviewSamples.REFERENCE_LINKS_AND_AUTOLINKS)
}

@PreviewWithTest
@Composable
internal fun TextMarkdownImagesPreview() {
    TextMarkdownPreviewContent(samples = TextMarkdownPreviewSamples.IMAGES)
}

@PreviewWithTest
@Composable
internal fun TextMarkdownImageFormsPreview() {
    TextMarkdownPreviewContent(samples = TextMarkdownPreviewSamples.IMAGE_FORMS)
}

/** Images from `example.com` and its subdomains are loaded; the snapshot test stands in for them. */
@PreviewWithTest
@Composable
internal fun TextMarkdownLoadedImagesPreview() {
    TextMarkdownPreviewContent(
        samples = TextMarkdownPreviewSamples.LOADED_IMAGES,
        imageDomains = listOf("example.com"),
    )
}
