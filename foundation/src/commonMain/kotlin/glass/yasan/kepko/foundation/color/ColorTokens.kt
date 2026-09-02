package glass.yasan.kepko.foundation.color

import androidx.compose.ui.graphics.Color
import glass.yasan.kepko.foundation.theme.ColorPalette

public object ColorTokens {

    private val foregroundLight: Color = Color(0xFFFFFFFF)
    private val midgroundLight: Color = Color(0xFFEEEEEE)
    private val backgroundLight: Color = Color(0xFFD8D8D8)

    private val contentLight: Color = Color(0xFF212121)
    private val contentSubtleLight: Color = Color(0xFF666666)
    private val contentDisabledLight: Color = Color(0xFFB3B3B3)

    private val foregroundDark: Color = Color(0xFF1F1F1F)
    private val midgroundDark: Color = Color(0xFF121212)
    private val backgroundDark: Color = Color(0xFF000000)

    private val foregroundBlack: Color = Color(0xFF000000)
    private val midgroundDarkBlack: Color = Color(0xFF000000)
    private val backgroundDarkBlack: Color = Color(0xFF121212)

    private val outlineLight: Color = backgroundLight
    private val outlineDark: Color = backgroundDark
    private val outlineDarkBlack: Color = Color(0xFF505050)

    private val foregroundLightSolarized: Color = Color(0xFFfdf6e3)
    private val midgroundLightSolarized: Color = Color(0xFFeee8d5)
    private val backgroundLightSolarized: Color = Color(0xFFddd6c3)
    private val outlineLightSolarized: Color = backgroundLightSolarized

    private val foregroundDarkSolarized: Color = Color(0xFF073642)
    private val midgroundDarkSolarized: Color = Color(0xFF002b36)
    private val backgroundDarkSolarized: Color = Color(0xFF001f26)
    private val outlineDarkSolarized: Color = backgroundDarkSolarized

    private val contentDark: Color = Color(0xFFE0E0E0)
    private val contentSubtleDark: Color = Color(0xFFA0A0A0)
    private val contentDisabledDark: Color = Color(0xFF606060)
    private val contentDisabledBlack: Color = Color(0xFF505050)

    private val contentLightSolarized: Color = Color(0xFF657b83)
    private val contentSubtleLightSolarized: Color = Color(0xFF93a1a1)
    private val contentDisabledLightSolarized: Color = Color(0xFFC8CCC2)

    private val contentDarkSolarized: Color = Color(0xFF839496)
    private val contentSubtleDarkSolarized: Color = Color(0xFF586e75)
    private val contentDisabledDarkSolarized: Color = Color(0xFF30525C)

    private val foregroundCatppuccinLatte: Color = Color(0xFFEFF1F5)
    private val midgroundCatppuccinLatte: Color = Color(0xFFE6E9EF)
    private val backgroundCatppuccinLatte: Color = Color(0xFFDCE0E8)
    private val outlineCatppuccinLatte: Color = Color(0xFFCCD0DA)
    private val contentCatppuccinLatte: Color = Color(0xFF4C4F69)
    private val contentSubtleCatppuccinLatte: Color = Color(0xFF6C6F85)
    private val contentDisabledCatppuccinLatte: Color = Color(0xFF9CA0B0)

    private val foregroundCatppuccinFrappe: Color = Color(0xFF303446)
    private val midgroundCatppuccinFrappe: Color = Color(0xFF292C3C)
    private val backgroundCatppuccinFrappe: Color = Color(0xFF232634)
    private val outlineCatppuccinFrappe: Color = Color(0xFF414559)
    private val contentCatppuccinFrappe: Color = Color(0xFFC6D0F5)
    private val contentSubtleCatppuccinFrappe: Color = Color(0xFFA5ADCE)
    private val contentDisabledCatppuccinFrappe: Color = Color(0xFF838BA7)

    private val foregroundCatppuccinMacchiato: Color = Color(0xFF24273A)
    private val midgroundCatppuccinMacchiato: Color = Color(0xFF1E2030)
    private val backgroundCatppuccinMacchiato: Color = Color(0xFF181926)
    private val outlineCatppuccinMacchiato: Color = Color(0xFF363A4F)
    private val contentCatppuccinMacchiato: Color = Color(0xFFCAD3F5)
    private val contentSubtleCatppuccinMacchiato: Color = Color(0xFFA5ADCB)
    private val contentDisabledCatppuccinMacchiato: Color = Color(0xFF8087A2)

    private val foregroundCatppuccinMocha: Color = Color(0xFF1E1E2E)
    private val midgroundCatppuccinMocha: Color = Color(0xFF181825)
    private val backgroundCatppuccinMocha: Color = Color(0xFF11111B)
    private val outlineCatppuccinMocha: Color = Color(0xFF313244)
    private val contentCatppuccinMocha: Color = Color(0xFFCDD6F4)
    private val contentSubtleCatppuccinMocha: Color = Color(0xFFA6ADC8)
    private val contentDisabledCatppuccinMocha: Color = Color(0xFF7F849C)

    private val foregroundGruvboxLight: Color = Color(0xFFfbf1c7)
    private val midgroundGruvboxLight: Color = Color(0xFFf2e5bc)
    private val backgroundGruvboxLight: Color = Color(0xFFebdbb2)
    private val outlineGruvboxLight: Color = Color(0xFFd5c4a1)
    private val contentGruvboxLight: Color = Color(0xFF3c3836)
    private val contentSubtleGruvboxLight: Color = Color(0xFF665c54)
    private val contentDisabledGruvboxLight: Color = Color(0xFFa89984)

    private val foregroundGruvboxDark: Color = Color(0xFF282828)
    private val midgroundGruvboxDark: Color = Color(0xFF1d2021)
    private val backgroundGruvboxDark: Color = Color(0xFF141414)
    private val outlineGruvboxDark: Color = Color(0xFF3c3836)
    private val contentGruvboxDark: Color = Color(0xFFebdbb2)
    private val contentSubtleGruvboxDark: Color = Color(0xFFa89984)
    private val contentDisabledGruvboxDark: Color = Color(0xFF665c54)

    private val successGruvboxLight: Color = Color(0xFF79740e)
    private val successGruvboxDark: Color = Color(0xFFb8bb26)
    private val informationGruvboxLight: Color = Color(0xFF076678)
    private val informationGruvboxDark: Color = Color(0xFF83a598)
    private val cautionGruvboxLight: Color = Color(0xFFb57614)
    private val cautionGruvboxDark: Color = Color(0xFFfabd2f)
    private val dangerGruvboxLight: Color = Color(0xFF9d0006)
    private val dangerGruvboxDark: Color = Color(0xFFfb4934)

    private val successCatppuccinLatte: Color = Color(0xFF40A02B)
    private val successCatppuccinFrappe: Color = Color(0xFFA6D189)
    private val successCatppuccinMacchiato: Color = Color(0xFFA6DA95)
    private val successCatppuccinMocha: Color = Color(0xFFA6E3A1)
    private val informationCatppuccinLatte: Color = Color(0xFF1E66F5)
    private val informationCatppuccinFrappe: Color = Color(0xFF8CAAEE)
    private val informationCatppuccinMacchiato: Color = Color(0xFF8AADF4)
    private val informationCatppuccinMocha: Color = Color(0xFF89B4FA)
    private val cautionCatppuccinLatte: Color = Color(0xFFDF8E1D)
    private val cautionCatppuccinFrappe: Color = Color(0xFFE5C890)
    private val cautionCatppuccinMacchiato: Color = Color(0xFFEED49F)
    private val cautionCatppuccinMocha: Color = Color(0xFFF9E2AF)
    private val dangerCatppuccinLatte: Color = Color(0xFFD20F39)
    private val dangerCatppuccinFrappe: Color = Color(0xFFE78284)
    private val dangerCatppuccinMacchiato: Color = Color(0xFFED8796)
    private val dangerCatppuccinMocha: Color = Color(0xFFF38BA8)

    private val successStandard: Color = Color(0xFF04B34F)
    private val onSuccessStandard: Color = Color(0xFFFFFFFF)
    private val informationStandard: Color = Color(0xFF0057B8)
    private val onInformationStandard: Color = Color(0xFFFFFFFF)
    private val cautionStandard: Color = Color(0xFFFF9900)
    private val onCautionStandard: Color = Color(0xFF000000)
    private val dangerStandard: Color = Color(0xFFD00036)
    private val onDangerStandard: Color = Color(0xFFFFFFFF)

    private val successSolarized: Color = Color(0xFF859900)
    private val onSuccessSolarized: Color = Color(0xFFfdf6e3)
    private val informationSolarized: Color = Color(0xFF2aa198)
    private val onInformationSolarized: Color = Color(0xFFfdf6e3)
    private val cautionSolarized: Color = Color(0xFFb58900)
    private val onCautionSolarized: Color = Color(0xFFfdf6e3)
    private val dangerSolarized: Color = Color(0xFFdc322f)
    private val onDangerSolarized: Color = Color(0xFFfdf6e3)

    internal fun success(palette: ColorPalette): Color = when (palette) {
        LIGHT, DARK, BLACK -> successStandard
        SOLARIZED_LIGHT, SOLARIZED_DARK -> successSolarized
        CATPPUCCIN_LATTE -> successCatppuccinLatte
        CATPPUCCIN_FRAPPE -> successCatppuccinFrappe
        CATPPUCCIN_MACCHIATO -> successCatppuccinMacchiato
        CATPPUCCIN_MOCHA -> successCatppuccinMocha
        GRUVBOX_LIGHT -> successGruvboxLight
        GRUVBOX_DARK -> successGruvboxDark
    }

    internal fun onSuccess(palette: ColorPalette): Color = when (palette) {
        LIGHT, DARK, BLACK -> onSuccessStandard
        SOLARIZED_LIGHT, SOLARIZED_DARK -> onSuccessSolarized
        CATPPUCCIN_LATTE -> foregroundCatppuccinLatte
        CATPPUCCIN_FRAPPE -> foregroundCatppuccinFrappe
        CATPPUCCIN_MACCHIATO -> foregroundCatppuccinMacchiato
        CATPPUCCIN_MOCHA -> foregroundCatppuccinMocha
        GRUVBOX_LIGHT -> foregroundGruvboxLight
        GRUVBOX_DARK -> foregroundGruvboxDark
    }

    internal fun information(palette: ColorPalette): Color = when (palette) {
        LIGHT, DARK, BLACK -> informationStandard
        SOLARIZED_LIGHT, SOLARIZED_DARK -> informationSolarized
        CATPPUCCIN_LATTE -> informationCatppuccinLatte
        CATPPUCCIN_FRAPPE -> informationCatppuccinFrappe
        CATPPUCCIN_MACCHIATO -> informationCatppuccinMacchiato
        CATPPUCCIN_MOCHA -> informationCatppuccinMocha
        GRUVBOX_LIGHT -> informationGruvboxLight
        GRUVBOX_DARK -> informationGruvboxDark
    }

    internal fun onInformation(palette: ColorPalette): Color = when (palette) {
        LIGHT, DARK, BLACK -> onInformationStandard
        SOLARIZED_LIGHT, SOLARIZED_DARK -> onInformationSolarized
        CATPPUCCIN_LATTE -> foregroundCatppuccinLatte
        CATPPUCCIN_FRAPPE -> foregroundCatppuccinFrappe
        CATPPUCCIN_MACCHIATO -> foregroundCatppuccinMacchiato
        CATPPUCCIN_MOCHA -> foregroundCatppuccinMocha
        GRUVBOX_LIGHT -> foregroundGruvboxLight
        GRUVBOX_DARK -> foregroundGruvboxDark
    }

    internal fun caution(palette: ColorPalette): Color = when (palette) {
        LIGHT, DARK, BLACK -> cautionStandard
        SOLARIZED_LIGHT, SOLARIZED_DARK -> cautionSolarized
        CATPPUCCIN_LATTE -> cautionCatppuccinLatte
        CATPPUCCIN_FRAPPE -> cautionCatppuccinFrappe
        CATPPUCCIN_MACCHIATO -> cautionCatppuccinMacchiato
        CATPPUCCIN_MOCHA -> cautionCatppuccinMocha
        GRUVBOX_LIGHT -> cautionGruvboxLight
        GRUVBOX_DARK -> cautionGruvboxDark
    }

    internal fun onCaution(palette: ColorPalette): Color = when (palette) {
        LIGHT, DARK, BLACK -> onCautionStandard
        SOLARIZED_LIGHT, SOLARIZED_DARK -> onCautionSolarized
        CATPPUCCIN_LATTE -> backgroundCatppuccinLatte
        CATPPUCCIN_FRAPPE -> foregroundCatppuccinFrappe
        CATPPUCCIN_MACCHIATO -> foregroundCatppuccinMacchiato
        CATPPUCCIN_MOCHA -> foregroundCatppuccinMocha
        GRUVBOX_LIGHT -> foregroundGruvboxLight
        GRUVBOX_DARK -> foregroundGruvboxDark
    }

    internal fun danger(palette: ColorPalette): Color = when (palette) {
        LIGHT, DARK, BLACK -> dangerStandard
        SOLARIZED_LIGHT, SOLARIZED_DARK -> dangerSolarized
        CATPPUCCIN_LATTE -> dangerCatppuccinLatte
        CATPPUCCIN_FRAPPE -> dangerCatppuccinFrappe
        CATPPUCCIN_MACCHIATO -> dangerCatppuccinMacchiato
        CATPPUCCIN_MOCHA -> dangerCatppuccinMocha
        GRUVBOX_LIGHT -> dangerGruvboxLight
        GRUVBOX_DARK -> dangerGruvboxDark
    }

    internal fun onDanger(palette: ColorPalette): Color = when (palette) {
        LIGHT, DARK, BLACK -> onDangerStandard
        SOLARIZED_LIGHT, SOLARIZED_DARK -> onDangerSolarized
        CATPPUCCIN_LATTE -> foregroundCatppuccinLatte
        CATPPUCCIN_FRAPPE -> foregroundCatppuccinFrappe
        CATPPUCCIN_MACCHIATO -> foregroundCatppuccinMacchiato
        CATPPUCCIN_MOCHA -> foregroundCatppuccinMocha
        GRUVBOX_LIGHT -> foregroundGruvboxLight
        GRUVBOX_DARK -> foregroundGruvboxDark
    }


    internal val scrim: Color = Color(0x4D000000)

    internal fun foreground(palette: ColorPalette): Color = when (palette) {
        LIGHT -> foregroundLight
        DARK -> foregroundDark
        BLACK -> foregroundBlack
        SOLARIZED_LIGHT -> foregroundLightSolarized
        SOLARIZED_DARK -> foregroundDarkSolarized
        CATPPUCCIN_LATTE -> foregroundCatppuccinLatte
        CATPPUCCIN_FRAPPE -> foregroundCatppuccinFrappe
        CATPPUCCIN_MACCHIATO -> foregroundCatppuccinMacchiato
        CATPPUCCIN_MOCHA -> foregroundCatppuccinMocha
        GRUVBOX_LIGHT -> foregroundGruvboxLight
        GRUVBOX_DARK -> foregroundGruvboxDark
    }

    internal fun midground(palette: ColorPalette): Color = when (palette) {
        LIGHT -> midgroundLight
        DARK -> midgroundDark
        BLACK -> midgroundDarkBlack
        SOLARIZED_LIGHT -> midgroundLightSolarized
        SOLARIZED_DARK -> midgroundDarkSolarized
        CATPPUCCIN_LATTE -> midgroundCatppuccinLatte
        CATPPUCCIN_FRAPPE -> midgroundCatppuccinFrappe
        CATPPUCCIN_MACCHIATO -> midgroundCatppuccinMacchiato
        CATPPUCCIN_MOCHA -> midgroundCatppuccinMocha
        GRUVBOX_LIGHT -> midgroundGruvboxLight
        GRUVBOX_DARK -> midgroundGruvboxDark
    }

    internal fun background(palette: ColorPalette): Color = when (palette) {
        LIGHT -> backgroundLight
        DARK -> backgroundDark
        BLACK -> backgroundDarkBlack
        SOLARIZED_LIGHT -> backgroundLightSolarized
        SOLARIZED_DARK -> backgroundDarkSolarized
        CATPPUCCIN_LATTE -> backgroundCatppuccinLatte
        CATPPUCCIN_FRAPPE -> backgroundCatppuccinFrappe
        CATPPUCCIN_MACCHIATO -> backgroundCatppuccinMacchiato
        CATPPUCCIN_MOCHA -> backgroundCatppuccinMocha
        GRUVBOX_LIGHT -> backgroundGruvboxLight
        GRUVBOX_DARK -> backgroundGruvboxDark
    }

    internal fun outline(palette: ColorPalette): Color = when (palette) {
        LIGHT -> outlineLight
        DARK -> outlineDark
        BLACK -> outlineDarkBlack
        SOLARIZED_LIGHT -> outlineLightSolarized
        SOLARIZED_DARK -> outlineDarkSolarized
        CATPPUCCIN_LATTE -> outlineCatppuccinLatte
        CATPPUCCIN_FRAPPE -> outlineCatppuccinFrappe
        CATPPUCCIN_MACCHIATO -> outlineCatppuccinMacchiato
        CATPPUCCIN_MOCHA -> outlineCatppuccinMocha
        GRUVBOX_LIGHT -> outlineGruvboxLight
        GRUVBOX_DARK -> outlineGruvboxDark
    }

    internal fun content(palette: ColorPalette): Color = when (palette) {
        LIGHT -> contentLight
        DARK, BLACK -> contentDark
        SOLARIZED_LIGHT -> contentLightSolarized
        SOLARIZED_DARK -> contentDarkSolarized
        CATPPUCCIN_LATTE -> contentCatppuccinLatte
        CATPPUCCIN_FRAPPE -> contentCatppuccinFrappe
        CATPPUCCIN_MACCHIATO -> contentCatppuccinMacchiato
        CATPPUCCIN_MOCHA -> contentCatppuccinMocha
        GRUVBOX_LIGHT -> contentGruvboxLight
        GRUVBOX_DARK -> contentGruvboxDark
    }

    internal fun contentSubtle(palette: ColorPalette): Color = when (palette) {
        LIGHT -> contentSubtleLight
        DARK, BLACK -> contentSubtleDark
        SOLARIZED_LIGHT -> contentSubtleLightSolarized
        SOLARIZED_DARK -> contentSubtleDarkSolarized
        CATPPUCCIN_LATTE -> contentSubtleCatppuccinLatte
        CATPPUCCIN_FRAPPE -> contentSubtleCatppuccinFrappe
        CATPPUCCIN_MACCHIATO -> contentSubtleCatppuccinMacchiato
        CATPPUCCIN_MOCHA -> contentSubtleCatppuccinMocha
        GRUVBOX_LIGHT -> contentSubtleGruvboxLight
        GRUVBOX_DARK -> contentSubtleGruvboxDark
    }

    internal fun contentDisabled(palette: ColorPalette): Color = when (palette) {
        LIGHT -> contentDisabledLight
        DARK -> contentDisabledDark
        BLACK -> contentDisabledBlack
        SOLARIZED_LIGHT -> contentDisabledLightSolarized
        SOLARIZED_DARK -> contentDisabledDarkSolarized
        CATPPUCCIN_LATTE -> contentDisabledCatppuccinLatte
        CATPPUCCIN_FRAPPE -> contentDisabledCatppuccinFrappe
        CATPPUCCIN_MACCHIATO -> contentDisabledCatppuccinMacchiato
        CATPPUCCIN_MOCHA -> contentDisabledCatppuccinMocha
        GRUVBOX_LIGHT -> contentDisabledGruvboxLight
        GRUVBOX_DARK -> contentDisabledGruvboxDark
    }

}
