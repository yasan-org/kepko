package glass.yasan.kepko.foundation.color

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import glass.yasan.kepko.foundation.theme.ColorPalette

@Immutable
public class Colors internal constructor(
    public val palette: ColorPalette,
    private val grayscale: Boolean = false,
) {
    private val inversePalette: ColorPalette =
        when (palette) {
            LIGHT -> DARK
            DARK -> LIGHT
            BLACK -> LIGHT
            SOLARIZED_LIGHT -> SOLARIZED_DARK
            SOLARIZED_DARK -> SOLARIZED_LIGHT
            CATPPUCCIN_LATTE -> CATPPUCCIN_MOCHA
            CATPPUCCIN_FRAPPE -> CATPPUCCIN_LATTE
            CATPPUCCIN_MACCHIATO -> CATPPUCCIN_LATTE
            CATPPUCCIN_MOCHA -> CATPPUCCIN_LATTE
            GRUVBOX_LIGHT -> GRUVBOX_DARK
            GRUVBOX_DARK -> GRUVBOX_LIGHT
        }

    private fun Color.applyGrayscale(): Color {
        if (!grayscale) return this

        val luminance = 0.2126f * red + 0.7152f * green + 0.0722f * blue
        return Color(luminance, luminance, luminance, alpha)
    }

    public val success: Color = ColorTokens.success(palette).applyGrayscale()
    public val onSuccess: Color = ColorTokens.onSuccess(palette).applyGrayscale()

    public val information: Color = ColorTokens.information(palette).applyGrayscale()
    public val onInformation: Color = ColorTokens.onInformation(palette).applyGrayscale()

    public val caution: Color = ColorTokens.caution(palette).applyGrayscale()
    public val onCaution: Color = ColorTokens.onCaution(palette).applyGrayscale()

    public val danger: Color = ColorTokens.danger(palette).applyGrayscale()
    public val onDanger: Color = ColorTokens.onDanger(palette).applyGrayscale()

    public val content: Color = ColorTokens.content(palette).applyGrayscale()
    public val contentSubtle: Color = ColorTokens.contentSubtle(palette).applyGrayscale()
    public val contentDisabled: Color = ColorTokens.contentDisabled(palette).applyGrayscale()

    public val inverseContent: Color = ColorTokens.content(inversePalette).applyGrayscale()
    public val inverseContentSubtle: Color = ColorTokens.contentSubtle(inversePalette).applyGrayscale()
    public val inverseContentDisabled: Color = ColorTokens.contentDisabled(inversePalette).applyGrayscale()

    public val foreground: Color = ColorTokens.foreground(palette).applyGrayscale()
    public val midground: Color = ColorTokens.midground(palette).applyGrayscale()
    public val background: Color = ColorTokens.background(palette).applyGrayscale()

    public val outline: Color = ColorTokens.outline(palette).applyGrayscale()

    public val scrim: Color = ColorTokens.scrim.applyGrayscale()

    public val inverseForeground: Color = ColorTokens.foreground(inversePalette).applyGrayscale()
    public val inverseMidground: Color = ColorTokens.midground(inversePalette).applyGrayscale()
    public val inverseBackground: Color = ColorTokens.background(inversePalette).applyGrayscale()
}

internal val LocalColors: ProvidableCompositionLocal<Colors> =
    compositionLocalOf {
        Colors(
            palette = LIGHT,
        )
    }

@Composable
public fun Colors.getSemanticColors(): Array<Color> = arrayOf(
    success,
    information,
    caution,
    danger,
)

@Composable
public fun Colors.getLayerColors(): Array<Color> = arrayOf(
    foreground,
    midground,
    background,
)

@Composable
public fun Colors.getContentColors(): Array<Color> = arrayOf(
    content,
    contentSubtle,
    contentDisabled,
)
