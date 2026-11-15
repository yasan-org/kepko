package glass.yasan.kepko.markdown

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import com.mikepenz.markdown.compose.components.MarkdownComponentModel
import com.mikepenz.markdown.model.MarkdownAlertType
import com.mikepenz.markdown.utils.findAlertType
import glass.yasan.kepko.component.Button
import glass.yasan.kepko.resource.Icons
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.flavours.gfm.GFMTokenTypes
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

/**
 * An alert, such as `> [!NOTE]`, as a [Button] in the colour of its kind: the kind's icon and
 * title, with the alert's text as the description.
 *
 * A description is plain text, so the alert's text is shown without its formatting.
 */
@Composable
internal fun TextMarkdownAlert(
    model: MarkdownComponentModel,
    colors: TextMarkdownColors,
) {
    // The parser only makes an alert of one of the five kinds.
    val type = model.node.findAlertType(model.content) ?: MarkdownAlertType.NOTE
    val description = remember(model.node, model.content) { model.node.alertText(model.content) }

    Button(
        text = stringResource(type.title()),
        description = description.ifEmpty { null },
        // An alert is read, not pressed.
        onClick = {},
        leadingIcon = type.icon(),
        containerColor = colors.containerColorOf(type),
        hapticFeedbackEnabled = false,
    )
}

/**
 * The alert's text. The parser leaves the alert's title and the `> ` markers of its lines in the
 * tree: everything up to the title, and the markers after it, are not content.
 */
private fun ASTNode.alertText(content: String): String {
    val blocks = children
        .dropWhile { it.type != GFMTokenTypes.ALERT_TITLE }
        .drop(1)
        .filterNot { it.type in lineTokens }
    return blocks
        .joinToString(separator = "\n") { block ->
            buildString { appendPlainText(node = block, content = content, inParagraph = false) }
        }
        .lines()
        .joinToString(separator = "\n") { line -> line.replace(spaces, " ").trim() }
        .trim()
}

private fun StringBuilder.appendPlainText(
    node: ASTNode,
    content: String,
    inParagraph: Boolean,
) {
    when (node.type) {
        in markupTokens -> Unit

        // A line break inside a paragraph does not end the line.
        MarkdownTokenTypes.EOL -> append(if (inParagraph) ' ' else '\n')

        MarkdownElementTypes.INLINE_LINK,
        MarkdownElementTypes.FULL_REFERENCE_LINK,
        MarkdownElementTypes.SHORT_REFERENCE_LINK,
        MarkdownElementTypes.IMAGE,
        -> node.children
            .firstOrNull { it.type in linkTexts || it.type in links }
            ?.let { appendPlainText(node = it, content = content, inParagraph = inParagraph) }

        // The text between the brackets.
        in linkTexts -> node.children.drop(1).dropLast(1).forEach { child ->
            appendPlainText(node = child, content = content, inParagraph = inParagraph)
        }

        else -> if (node.children.isEmpty()) {
            append(content, node.startOffset, node.endOffset)
        } else {
            node.children.forEach { child ->
                appendPlainText(
                    node = child,
                    content = content,
                    inParagraph = inParagraph || node.type == MarkdownElementTypes.PARAGRAPH,
                )
            }
        }
    }
}

private val lineTokens = setOf(
    MarkdownTokenTypes.EOL,
    MarkdownTokenTypes.WHITE_SPACE,
    MarkdownTokenTypes.BLOCK_QUOTE,
)

private val markupTokens = setOf(
    MarkdownTokenTypes.EMPH,
    MarkdownTokenTypes.BACKTICK,
    MarkdownTokenTypes.BLOCK_QUOTE,
    MarkdownTokenTypes.CODE_FENCE_START,
    MarkdownTokenTypes.CODE_FENCE_END,
    MarkdownTokenTypes.FENCE_LANG,
    GFMTokenTypes.TILDE,
)

private val links = setOf(
    MarkdownElementTypes.INLINE_LINK,
    MarkdownElementTypes.FULL_REFERENCE_LINK,
    MarkdownElementTypes.SHORT_REFERENCE_LINK,
)

private val linkTexts = setOf(
    MarkdownElementTypes.LINK_TEXT,
    MarkdownElementTypes.LINK_LABEL,
)

private val spaces = Regex(" {2,}")

private fun TextMarkdownColors.containerColorOf(type: MarkdownAlertType): Color = when (type) {
    MarkdownAlertType.NOTE -> noteColor
    MarkdownAlertType.TIP -> tipColor
    MarkdownAlertType.IMPORTANT -> importantColor
    MarkdownAlertType.WARNING -> warningColor
    MarkdownAlertType.CAUTION -> cautionColor
}

private fun MarkdownAlertType.title(): StringResource = when (this) {
    MarkdownAlertType.NOTE -> Res.string.text_markdown_alert_note
    MarkdownAlertType.TIP -> Res.string.text_markdown_alert_tip
    MarkdownAlertType.IMPORTANT -> Res.string.text_markdown_alert_important
    MarkdownAlertType.WARNING -> Res.string.text_markdown_alert_warning
    MarkdownAlertType.CAUTION -> Res.string.text_markdown_alert_caution
}

@Composable
private fun MarkdownAlertType.icon(): Painter = when (this) {
    MarkdownAlertType.NOTE -> Icons.info
    MarkdownAlertType.TIP -> Icons.lightbulb
    MarkdownAlertType.IMPORTANT -> Icons.chatInfo
    MarkdownAlertType.WARNING -> Icons.warning
    MarkdownAlertType.CAUTION -> Icons.report
}
