package glass.yasan.kepko.markdown

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.dp
import com.mikepenz.markdown.compose.components.MarkdownComponentModel
import com.mikepenz.markdown.model.MarkdownAlertType
import com.mikepenz.markdown.utils.findAlertType
import glass.yasan.kepko.component.Button
import glass.yasan.kepko.component.Icon
import glass.yasan.kepko.component.Text
import glass.yasan.kepko.resource.Icons
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.flavours.gfm.GFMTokenTypes
import org.intellij.markdown.html.entities.EntityConverter
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

/**
 * An alert, such as `> [!NOTE]`, as a [Button] in the colour of its kind, with the kind's icon and
 * the alert's text. The kind's name is not shown: it is the icon's description.
 *
 * The text is plain, so the alert's formatting is not shown. The alert is read as its kind and
 * text, not as a button, and takes no focus.
 */
@Composable
internal fun TextMarkdownAlert(
    model: MarkdownComponentModel,
    colors: TextMarkdownColors,
) {
    // The parser only makes an alert of one of the five kinds.
    val type = model.node.findAlertType(model.content) ?: MarkdownAlertType.NOTE
    val text = remember(model.node, model.content) { model.node.alertText(model.content) }
    val title = stringResource(type.title())

    Button(
        text = null,
        modifier = Modifier
            .clearAndSetSemantics { contentDescription = "$title. $text" }
            .focusProperties { canFocus = false },
        // An alert is read, not pressed.
        onClick = {},
        containerColor = colors.containerColorOf(type),
        hapticFeedbackEnabled = false,
        leadingContent = {
            Icon(
                painter = type.icon(),
                contentDescription = title,
                modifier = Modifier.padding(end = 12.dp),
            )
            Text(
                text = text,
                style = model.typography.text,
                modifier = Modifier.weight(1f),
            )
        },
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
    inCode: Boolean = false,
) {
    when (node.type) {
        in markupTokens -> Unit

        // A line break inside a paragraph does not end the line.
        MarkdownTokenTypes.EOL -> append(if (inParagraph) ' ' else '\n')

        MarkdownTokenTypes.HARD_LINE_BREAK -> append('\n')

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
            val leaf = content.subSequence(node.startOffset, node.endOffset)
            // Outside code, escapes such as `\*` and entities such as `&amp;` show as the character
            // they stand for.
            append(if (inCode) leaf else EntityConverter.replaceEntities(leaf, true, true))
        } else {
            node.children.forEach { child ->
                appendPlainText(
                    node = child,
                    content = content,
                    inParagraph = inParagraph || node.type == MarkdownElementTypes.PARAGRAPH,
                    inCode = inCode || node.type in codeElements,
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
    // The brackets of an autolink, `<https://example.com>`.
    MarkdownTokenTypes.LT,
    MarkdownTokenTypes.GT,
    MarkdownTokenTypes.HTML_TAG,
    GFMTokenTypes.TILDE,
)

private val codeElements = setOf(
    MarkdownElementTypes.CODE_SPAN,
    MarkdownElementTypes.CODE_FENCE,
    MarkdownElementTypes.CODE_BLOCK,
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
