package glass.yasan.kepko.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import glass.yasan.kepko.foundation.theme.KepkoTheme

public object ButtonDefaults {
    public val ContentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 8.dp)

    public const val DescriptionAlpha: Float = 0.75f

    @Composable
    public fun shape(): Shape = KepkoTheme.shapes.extraLarge

    @Composable
    public fun contentPadding(
        contentPadding: PaddingValues = ContentPadding,
    ): PaddingValues = contentPadding
}
