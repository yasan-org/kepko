package glass.yasan.kepko.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Indication
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import glass.yasan.kepko.foundation.border.borderStrokeFor
import glass.yasan.kepko.foundation.color.contentColorFor
import glass.yasan.kepko.foundation.color.getLayerColors
import glass.yasan.kepko.foundation.theme.KepkoTheme
import androidx.compose.material3.ButtonDefaults as Material3ButtonDefaults

public object ButtonPrimitiveDefaults {
    public val ContentPadding: PaddingValues = Material3ButtonDefaults.ContentPadding

    @Composable
    public fun contentPadding(
        contentPadding: PaddingValues = ContentPadding,
    ): PaddingValues = contentPadding

    public val containerColor: Color
        @Composable
        @ReadOnlyComposable
        get() = KepkoTheme.colors.foreground

    @Composable
    public fun contentColor(containerColor: Color): Color = contentColorFor(containerColor)

    public val disabledContentColor: Color
        @Composable
        @ReadOnlyComposable
        get() = KepkoTheme.colors.contentDisabled

    @Composable
    public fun disabledContainerColor(containerColor: Color): Color =
        if (containerColor in KepkoTheme.colors.getLayerColors()) {
            containerColor
        } else {
            KepkoTheme.colors.foreground
        }

    public val shape: Shape
        @Composable
        @ReadOnlyComposable
        get() = KepkoTheme.shapes.extraLarge

    @Composable
    public fun border(containerColor: Color): BorderStroke? = borderStrokeFor(containerColor)

    public val indication: Indication
        @Composable
        @ReadOnlyComposable
        get() = LocalIndication.current
}
