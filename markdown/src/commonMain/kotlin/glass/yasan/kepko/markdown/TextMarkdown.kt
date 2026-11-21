package glass.yasan.kepko.markdown

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mikepenz.markdown.compose.Markdown
import com.mikepenz.markdown.compose.components.markdownComponents
import com.mikepenz.markdown.model.DefaultMarkdownColors
import com.mikepenz.markdown.model.DefaultMarkdownTypography
import com.mikepenz.markdown.model.MarkdownColors
import com.mikepenz.markdown.model.MarkdownPadding
import com.mikepenz.markdown.model.MarkdownTypography
import com.mikepenz.markdown.model.markdownAnnotator
import com.mikepenz.markdown.model.markdownPadding

/**
 * Text written in GitHub Flavored Markdown, rendered in the theme: emphasis, links, inline code,
 * headings, lists, quotes, alerts, rules, code blocks and tables.
 *
 * Text with no markdown in it reads as `Text` shows it. An image shows its description unless it
 * is loaded, see [imageDomains]. An alert (`> [!NOTE]`) is a `Button` in the colour of its kind, with
 * the kind's icon and the alert's text, without formatting.
 *
 * @param text The markdown to render.
 * @param typography The styles of body text, headings, code and quotes.
 * @param blockSpacing The space between two blocks, such as two paragraphs.
 * @param onLinkClick Called with a link's address when it is clicked. `null` opens the link with
 * the platform's handler.
 * @param imageDomains The domains images are loaded from, such as `example.com`. An image is
 * loaded when its address is `https` and its host is one of these or a subdomain of one. Images
 * are loaded with Coil, which needs one of its network artifacts in the app to load from the web.
 */
@Composable
public fun TextMarkdown(
    text: String,
    modifier: Modifier = Modifier,
    colors: TextMarkdownColors = TextMarkdownDefaults.colors(),
    typography: TextMarkdownTypography = TextMarkdownDefaults.typography(),
    blockSpacing: Dp = TextMarkdownDefaults.BlockSpacing,
    onLinkClick: ((String) -> Unit)? = null,
    imageDomains: List<String> = emptyList(),
) {
    val images = remember(imageDomains) { ImageDomains(imageDomains) }
    val platformUriHandler = LocalUriHandler.current
    val currentOnLinkClick = rememberUpdatedState(onLinkClick)
    val uriHandler = remember(platformUriHandler) {
        object : UriHandler {
            override fun openUri(uri: String) {
                currentOnLinkClick.value?.invoke(uri) ?: platformUriHandler.openUri(uri)
            }
        }
    }
    CompositionLocalProvider(LocalUriHandler provides uriHandler) {
        Markdown(
            content = text,
            colors = colors.toMarkdownColors(),
            typography = typography.toMarkdownTypography(colors),
            padding = textMarkdownPadding(blockSpacing = blockSpacing),
            annotator = remember(images) {
                markdownAnnotator { content, child -> annotateImageAsDescription(content, child, images) }
            },
            imageTransformer = remember(images) { DomainImageTransformer(images) },
            components = remember(colors) {
                markdownComponents(alert = { model -> TextMarkdownAlert(model = model, colors = colors) })
            },
            // Parsed during composition: parsing afterwards would show nothing for a frame, then
            // resize whatever the text sits in.
            immediate = true,
            // The renderer puts the block spacing above every block, the first one too.
            modifier = modifier.withoutSpaceAbove(blockSpacing),
        )
    }
}

private fun Modifier.withoutSpaceAbove(space: Dp): Modifier = layout { measurable, constraints ->
    val placeable = measurable.measure(constraints)
    val cut = space.roundToPx().coerceAtMost(placeable.height)
    layout(placeable.width, placeable.height - cut) {
        placeable.place(0, -cut)
    }
}

private fun TextMarkdownColors.toMarkdownColors(): MarkdownColors = DefaultMarkdownColors(
    text = textColor,
    codeBackground = codeContainerColor,
    inlineCodeBackground = codeContainerColor,
    dividerColor = dividerColor,
    tableBackground = Color.Transparent,
    highlightText = codeColor,
    highlightBackground = codeContainerColor,
)

@Composable
private fun textMarkdownPadding(blockSpacing: Dp): MarkdownPadding = markdownPadding(
    block = blockSpacing,
    list = 0.dp,
    listItemTop = 0.dp,
    listItemBottom = blockSpacing,
    blockQuoteText = PaddingValues(0.dp),
)

private fun TextMarkdownTypography.toMarkdownTypography(colors: TextMarkdownColors): MarkdownTypography {
    val text = text.copy(color = colors.textColor)
    val code = code.copy(color = colors.codeColor)
    return DefaultMarkdownTypography(
        h1 = heading1.copy(color = colors.textColor),
        h2 = heading2.copy(color = colors.textColor),
        h3 = heading3.copy(color = colors.textColor),
        h4 = heading4.copy(color = colors.textColor),
        h5 = heading5.copy(color = colors.textColor),
        h6 = heading6.copy(color = colors.textColor),
        text = text,
        code = code,
        inlineCode = code,
        quote = quote.copy(color = colors.textColor),
        paragraph = text,
        ordered = text,
        bullet = text,
        list = text,
        textLink = TextLinkStyles(
            style = SpanStyle(
                color = colors.linkColor,
                textDecoration = TextDecoration.Underline,
            ),
        ),
        table = text,
    )
}
