package glass.yasan.kepko.markdown

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle

@Immutable
public data class TextMarkdownTypography(
    val text: TextStyle,
    val heading1: TextStyle,
    val heading2: TextStyle,
    val heading3: TextStyle,
    val heading4: TextStyle,
    val heading5: TextStyle,
    val heading6: TextStyle,
    val code: TextStyle,
    val quote: TextStyle,
)
