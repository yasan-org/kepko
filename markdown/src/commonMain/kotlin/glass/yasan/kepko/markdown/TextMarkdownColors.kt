package glass.yasan.kepko.markdown

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
public data class TextMarkdownColors(
    val textColor: Color,
    val linkColor: Color,
    val codeColor: Color,
    val codeContainerColor: Color,
    val dividerColor: Color,
    val noteColor: Color,
    val tipColor: Color,
    val importantColor: Color,
    val warningColor: Color,
    val cautionColor: Color,
)
