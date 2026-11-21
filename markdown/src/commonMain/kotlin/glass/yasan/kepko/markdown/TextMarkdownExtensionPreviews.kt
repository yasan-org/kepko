package glass.yasan.kepko.markdown

import androidx.compose.runtime.Composable

@PreviewWithTest
@Composable
internal fun TextMarkdownTablesPreview() {
    TextMarkdownPreviewContent(samples = TextMarkdownPreviewSamples.TABLES)
}

@PreviewWithTest
@Composable
internal fun TextMarkdownTableFormsPreview() {
    TextMarkdownPreviewContent(samples = TextMarkdownPreviewSamples.TABLE_FORMS)
}

@PreviewWithTest
@Composable
internal fun TextMarkdownTaskListsPreview() {
    TextMarkdownPreviewContent(samples = TextMarkdownPreviewSamples.TASK_LISTS)
}

@PreviewWithTest
@Composable
internal fun TextMarkdownGithubExtensionsPreview() {
    TextMarkdownPreviewContent(samples = TextMarkdownPreviewSamples.GITHUB_EXTENSIONS)
}

@PreviewWithTest
@Composable
internal fun TextMarkdownAlertsPreview() {
    TextMarkdownPreviewContent(samples = TextMarkdownPreviewSamples.ALERTS)
}

@PreviewWithTest
@Composable
internal fun TextMarkdownAlertFormsPreview() {
    TextMarkdownPreviewContent(samples = TextMarkdownPreviewSamples.ALERT_FORMS)
}

@PreviewWithTest
@Composable
internal fun TextMarkdownBeyondInlinePreview() {
    TextMarkdownPreviewContent(samples = TextMarkdownPreviewSamples.BEYOND_INLINE)
}

@PreviewWithTest
@Composable
internal fun TextMarkdownBeyondBlocksPreview() {
    TextMarkdownPreviewContent(samples = TextMarkdownPreviewSamples.BEYOND_BLOCKS)
}

@PreviewWithTest
@Composable
internal fun TextMarkdownUnclosedPreview() {
    TextMarkdownPreviewContent(samples = TextMarkdownPreviewSamples.UNCLOSED)
}

@PreviewWithTest
@Composable
internal fun TextMarkdownLiteralsPreview() {
    TextMarkdownPreviewContent(samples = TextMarkdownPreviewSamples.LITERALS)
}

@PreviewWithTest
@Composable
internal fun TextMarkdownEverythingPreview() {
    TextMarkdownPreviewContent(samples = TextMarkdownPreviewSamples.EVERYTHING)
}

@PreviewWithTest
@Composable
internal fun TextMarkdownAlertsDarkPreview() {
    TextMarkdownPreviewContent(samples = TextMarkdownPreviewSamples.ALERTS, palette = DARK)
}
