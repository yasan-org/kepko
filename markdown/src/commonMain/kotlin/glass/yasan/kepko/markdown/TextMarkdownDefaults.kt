package glass.yasan.kepko.markdown

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import glass.yasan.kepko.foundation.theme.KepkoTheme
import glass.yasan.kepko.foundation.typography.rubikMonoTypography
import glass.yasan.kepko.foundation.typography.rubikTypography

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

    /**
     * Styles from the theme's type scale: body text is the current text style, headings step down
     * the scale in bold, and code is body text in the theme's monospace typeface.
     */
    @Composable
    public fun typography(
        typography: Typography = rubikTypography(),
        monoTypography: Typography = rubikMonoTypography(),
        text: TextStyle = LocalTextStyle.current,
        heading1: TextStyle = typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
        heading2: TextStyle = typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
        heading3: TextStyle = typography.titleLarge.copy(fontWeight = FontWeight.Bold),
        heading4: TextStyle = typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        heading5: TextStyle = typography.titleSmall.copy(fontWeight = FontWeight.Bold),
        heading6: TextStyle = typography.labelMedium.copy(fontWeight = FontWeight.Bold),
        code: TextStyle = text.copy(fontFamily = monoTypography.bodyLarge.fontFamily),
        quote: TextStyle = text.copy(fontStyle = FontStyle.Italic),
    ): TextMarkdownTypography = TextMarkdownTypography(
        text = text,
        heading1 = heading1,
        heading2 = heading2,
        heading3 = heading3,
        heading4 = heading4,
        heading5 = heading5,
        heading6 = heading6,
        code = code,
        quote = quote,
    )
}
