package glass.yasan.kepko.markdown

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mikepenz.markdown.compose.LocalMarkdownComponents
import com.mikepenz.markdown.compose.MarkdownElement
import com.mikepenz.markdown.compose.components.MarkdownComponentModel
import com.mikepenz.markdown.model.MarkdownAlertType
import com.mikepenz.markdown.utils.findAlertType
import glass.yasan.kepko.component.Icon
import glass.yasan.kepko.component.Text
import glass.yasan.kepko.resource.Icons
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.flavours.gfm.GFMTokenTypes
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

/**
 * An alert, such as `> [!NOTE]`: a card with an accent bar down its side, the icon and title of its
 * kind, and the alert's blocks under them.
 */
@Composable
internal fun TextMarkdownAlert(
    model: MarkdownComponentModel,
    colors: TextMarkdownColors,
    blockSpacing: Dp,
) {
    // The parser only makes an alert of one of the five kinds.
    val type = model.node.findAlertType(model.content) ?: MarkdownAlertType.NOTE
    val accent = colors.accentOf(type)
    val components = LocalMarkdownComponents.current

    Column(
        verticalArrangement = Arrangement.spacedBy(blockSpacing),
        modifier = Modifier
            .fillMaxWidth()
            .clip(TextMarkdownDefaults.AlertShape)
            .background(colors.alertContainerColor)
            .drawBehind { drawRect(color = accent, size = Size(BarWidth.toPx(), size.height)) }
            .padding(start = BarWidth + 12.dp, top = 8.dp, end = 12.dp, bottom = 8.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(painter = type.icon(), tint = accent, size = 16.dp)
            Text(
                text = stringResource(type.title()),
                style = model.typography.alertTitle,
                color = colors.textColor,
            )
        }
        model.node.blocks().forEach { block ->
            key(block.startOffset) {
                MarkdownElement(
                    node = block,
                    components = components,
                    content = model.content,
                    includeSpacer = false,
                )
            }
        }
    }
}

/**
 * The alert's blocks. The parser leaves the alert's title and the `> ` markers of its lines in
 * the tree: everything up to the title, and the markers after it, are not content.
 */
private fun ASTNode.blocks(): List<ASTNode> = children
    .dropWhile { it.type != GFMTokenTypes.ALERT_TITLE }
    .drop(1)
    .filterNot {
        it.type == MarkdownTokenTypes.EOL ||
            it.type == MarkdownTokenTypes.WHITE_SPACE ||
            it.type == MarkdownTokenTypes.BLOCK_QUOTE
    }

private fun TextMarkdownColors.accentOf(type: MarkdownAlertType): Color = when (type) {
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

private val BarWidth = 4.dp
