package glass.yasan.kepko.persistence

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import glass.yasan.kepko.component.Button
import glass.yasan.kepko.foundation.annotation.ExperimentalKepkoApi
import glass.yasan.kepko.foundation.theme.ColorPalette
import glass.yasan.kepko.persistence.internal.PreviewPersistenceManager
import glass.yasan.kepko.resource.Icons
import glass.yasan.kepko.resource.Strings

/**
 * A ready-made navigation button, with the active theme preferences shown in the description.
 *
 * Passing a [description] replaces that summary; `null` keeps it.
 */
@ExperimentalKepkoApi
@Composable
public fun PersistentPreferenceThemeButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    persistence: PersistenceManager = LocalKepkoPersistenceManager.current,
    targetProfile: UserVisibleProfile? = null,
    text: String = Strings.preferenceThemeScreenTitle,
    description: String? = null,
) {
    Button(
        text = text,
        description = description ?: persistence.themeButtonDescription(targetProfile),
        onClick = onClick,
        leadingIcon = Icons.palette,
        trailingIcon = Icons.chevronForward,
        modifier = modifier,
    )
}

@ExperimentalKepkoApi
@Composable
public fun PersistenceManager.themeButtonDescription(
    targetProfile: UserVisibleProfile? = null,
): String {
    if (targetProfile != null) {
        val overrides = listOfNotNull(
            profileManager.getProfilePalette(targetProfile.id)?.title?.invoke(),
            profileManager.getProfileGrayscale(targetProfile.id)
                ?.let { if (it) Strings.grayscaleEnabled else Strings.grayscaleDisabled },
        )

        return if (overrides.isEmpty()) Strings.preferenceFollowGlobalTitle else overrides.joinToString(", ")
    }

    val paletteDescription = getPalettePrimary(null)?.title?.invoke()
        ?: "${paletteLight.title()} / ${paletteDark.title()}"
    val descriptions = listOf(paletteDescription) +
            listOfNotNull(Strings.preferenceGrayscaleTitle.takeIf { isGrayscaleEnabled(null) })

    return descriptions.joinToString(", ")
}

@ExperimentalKepkoApi
@PreviewWithTest
@Composable
internal fun PersistentPreferenceThemeButtonPreview() {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(16.dp),
    ) {
        PreviewPreferenceThemeButton()
        PreviewPreferenceThemeButton(
            isSystemInDarkTheme = true,
            configure = {
                setPalettePrimary(null, ColorPalette.SOLARIZED_DARK)
                paletteDark = ColorPalette.SOLARIZED_DARK
            },
        )
        PreviewPreferenceThemeButton(
            configure = {
                paletteLight = ColorPalette.SOLARIZED_LIGHT
                paletteDark = ColorPalette.BLACK
            },
        )
        PreviewPreferenceThemeButton(
            configure = { setGrayscaleEnabled(null, true) },
        )
        PreviewPreferenceThemeButton(targetProfile = previewProfile)
        PreviewPreferenceThemeButton(
            targetProfile = previewProfile,
            configure = { profileManager.setProfilePalette(previewProfile.id, ColorPalette.SOLARIZED_DARK) },
        )
        PreviewPreferenceThemeButton(
            targetProfile = previewProfile,
            configure = { profileManager.setProfileGrayscale(previewProfile.id, false) },
        )
        PreviewPreferenceThemeButton(
            targetProfile = previewProfile,
            configure = {
                profileManager.setProfilePalette(previewProfile.id, ColorPalette.SOLARIZED_DARK)
                profileManager.setProfileGrayscale(previewProfile.id, true)
            },
        )
        PreviewPreferenceThemeButton(description = "Following Yasan Launcher")
    }
}

@ExperimentalKepkoApi
@Composable
private fun PreviewPreferenceThemeButton(
    isSystemInDarkTheme: Boolean = false,
    targetProfile: UserVisibleProfile? = null,
    description: String? = null,
    configure: PreviewPersistenceManager.() -> Unit = {},
) {
    PreviewPersistentKepkoTheme(
        isSystemInDarkTheme = isSystemInDarkTheme,
        configure = configure,
    ) {
        PersistentPreferenceThemeButton(
            onClick = {},
            targetProfile = targetProfile,
            description = description,
        )
    }
}
