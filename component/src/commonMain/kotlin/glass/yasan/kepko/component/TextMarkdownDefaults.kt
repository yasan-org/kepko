package glass.yasan.kepko.component

import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import glass.yasan.kepko.foundation.theme.KepkoTheme

public object TextMarkdownDefaults {

    /** The space between two blocks, such as two paragraphs or a paragraph and a list. */
    public val BlockSpacing: Dp = 4.dp

    @Composable
    public fun colors(
        textColor: Color = LocalContentColor.current,
        linkColor: Color = KepkoTheme.colors.content,
        codeColor: Color = KepkoTheme.colors.content,
        codeContainerColor: Color = KepkoTheme.colors.background,
        dividerColor: Color = KepkoTheme.colors.outline,
    ): TextMarkdownColors = TextMarkdownColors(
        textColor = textColor,
        linkColor = linkColor,
        codeColor = codeColor,
        codeContainerColor = codeContainerColor,
        dividerColor = dividerColor,
    )
}
