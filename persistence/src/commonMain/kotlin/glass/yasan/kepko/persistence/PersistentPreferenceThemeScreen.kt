package glass.yasan.kepko.persistence

import androidx.annotation.VisibleForTesting
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.unit.IntSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import glass.yasan.kepko.component.Badge
import glass.yasan.kepko.component.IconButton
import glass.yasan.kepko.component.PreferenceRadioGroupPicker
import glass.yasan.kepko.component.PreferenceSlider
import glass.yasan.kepko.component.PreferenceSwitch
import glass.yasan.kepko.component.Scaffold
import glass.yasan.kepko.component.Subtitle
import glass.yasan.kepko.foundation.annotation.ExperimentalKepkoApi
import glass.yasan.kepko.foundation.theme.ColorPalette
import glass.yasan.kepko.foundation.theme.ColorPalette.Companion.defaultDark
import glass.yasan.kepko.foundation.theme.ColorPalette.Companion.defaultLight
import glass.yasan.kepko.foundation.theme.isSystemInDarkTheme
import glass.yasan.kepko.persistence.internal.asPreferenceRadioGroupItem
import glass.yasan.kepko.resource.Icons
import glass.yasan.kepko.resource.Strings

/**
 * A theme preferences screen which allows easy integration when used with [PersistentKepkoTheme].
 *
 * This preference screen allows the user to customize the appearance of the theme using [PersistenceManager].
 *
 * @param activeProfile The profile that is actively being used to render the interface
 * @param targetProfile The profile that is being targeted for customization
 */
@ExperimentalKepkoApi
@Composable
public fun PersistentPreferenceThemeScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    persistence: PersistenceManager = LocalKepkoPersistenceManager.current,
    isSystemInDarkTheme: Boolean = isSystemInDarkTheme(),
    activeProfile: UserVisibleProfile? = null,
    targetProfile: UserVisibleProfile? = null,
    enabled: Boolean = true,
    topContent: @Composable ColumnScope.() -> Unit = {},
    bottomContent: @Composable ColumnScope.() -> Unit = {},
) {
    AnimatedPersistentKepkoTheme(
        persistence = persistence,
        isSystemInDarkTheme = isSystemInDarkTheme,
        profileId = activeProfile?.id,
    ) {
        Scaffold(
            title = Strings.preferenceThemeScreenTitle,
            onBackClick = onBackClick,
            trailingContent = {
                PersistentPreferenceThemeResetButton(persistence, targetProfile, enabled)
            },
            subtitle = targetProfile?.let {
                {
                    Subtitle(
                        text = it.name(),
                        icon = it.icon(),
                        modifier = Modifier.testTag(PersistentPreferenceThemeScreenSemantics.PROFILE_HEADER),
                    )
                }
            },
            modifier = modifier
                .testTag(PersistentPreferenceThemeScreenSemantics.SCREEN)
        ) { contentPadding ->
            PersistentPreferenceThemeContent(
                persistence = persistence,
                isSystemInDarkTheme = isSystemInDarkTheme,
                activeProfile = activeProfile,
                targetProfile = targetProfile,
                enabled = enabled,
                topContent = topContent,
                bottomContent = bottomContent,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .padding(contentPadding),
            )
        }
    }
}

@ExperimentalKepkoApi
@Composable
public fun PersistentPreferenceThemeContent(
    persistence: PersistenceManager = LocalKepkoPersistenceManager.current,
    isSystemInDarkTheme: Boolean,
    activeProfile: UserVisibleProfile? = null,
    targetProfile: UserVisibleProfile? = null,
    enabled: Boolean = true,
    topContent: @Composable ColumnScope.() -> Unit = {},
    bottomContent: @Composable ColumnScope.() -> Unit = {},
    modifier: Modifier = Modifier,
) {
    AnimatedPersistentKepkoTheme(
        persistence = persistence,
        isSystemInDarkTheme = isSystemInDarkTheme,
        profileId = activeProfile?.id,
    ) {
        if (targetProfile != null) {
            PersistentPreferenceThemeProfileContent(
                persistence = persistence,
                profile = targetProfile,
                enabled = enabled,
                topContent = topContent,
                bottomContent = bottomContent,
                modifier = modifier,
            )
        } else {
            PersistentPreferenceThemeGlobalContent(
                persistence = persistence,
                isSystemInDarkTheme = isSystemInDarkTheme,
                activeProfile = activeProfile,
                enabled = enabled,
                topContent = topContent,
                bottomContent = bottomContent,
                modifier = modifier,
            )
        }
    }
}

@Composable
private fun PersistentPreferenceThemeGlobalContent(
    persistence: PersistenceManager,
    isSystemInDarkTheme: Boolean,
    activeProfile: UserVisibleProfile?,
    enabled: Boolean,
    topContent: @Composable ColumnScope.() -> Unit,
    bottomContent: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
) {
    val paletteOverriddenBadge = activeProfile
        ?.takeIf { persistence.profileManager.getProfilePalette(it.id) != null }
        ?.let { Badge.overriddenBy(it.name) }
    val grayscaleOverriddenBadge = activeProfile
        ?.takeIf { persistence.profileManager.getProfileGrayscale(it.id) != null }
        ?.let { Badge.overriddenBy(it.name) }

    val sizeSpring = spring(
        dampingRatio = Spring.DampingRatioLowBouncy,
        stiffness = Spring.StiffnessMediumLow,
        visibilityThreshold = IntSize.VisibilityThreshold,
    )
    val fadeSpring = spring<Float>(stiffness = Spring.StiffnessMedium)
    val enterTransition = expandVertically(animationSpec = sizeSpring) +
        fadeIn(animationSpec = fadeSpring)
    val exitTransition = shrinkVertically(animationSpec = sizeSpring) +
        fadeOut(animationSpec = fadeSpring)

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState()),
    ) {
        topContent()
        PersistentPreferenceThemeMode(persistence, enabled = enabled)
        AnimatedVisibility(
            visible = persistence.getPalettePrimary(profileId = null) == null,
            enter = enterTransition,
            exit = exitTransition,
        ) {
            PersistentPreferenceThemeSystem(
                persistence = persistence,
                isSystemInDarkTheme = isSystemInDarkTheme,
                overriddenBadge = paletteOverriddenBadge,
                enabled = enabled,
            )
        }
        AnimatedVisibility(
            visible = persistence.getPalettePrimary(profileId = null) != null,
            enter = enterTransition,
            exit = exitTransition,
        ) {
            PersistentPreferenceThemeStatic(
                persistence = persistence,
                overriddenBadge = paletteOverriddenBadge,
                enabled = enabled,
            )
        }
        Spacer(Modifier.height(24.dp))
        PersistentPreferenceThemeGrayscale(
            persistence = persistence,
            overriddenBadge = grayscaleOverriddenBadge,
            enabled = enabled,
        )
        Spacer(Modifier.height(8.dp))
        PersistentPreferenceThemeOutline(persistence, enabled = enabled)
        Spacer(Modifier.height(8.dp))
        PersistentPreferenceThemeRoundness(persistence, enabled = enabled)
        bottomContent()
    }
}

@Composable
private fun PersistentPreferenceThemeResetButton(
    persistence: PersistenceManager,
    targetProfile: UserVisibleProfile? = null,
    enabled: Boolean = true,
) {
    val showResetButton = enabled && if (targetProfile != null) {
        persistence.profileManager.getProfilePalette(targetProfile.id) != null ||
            persistence.profileManager.getProfileGrayscale(targetProfile.id) != null
    } else {
        !persistence.isDefault
    }

    Crossfade(targetState = showResetButton) { show ->
        if (show) {
            IconButton(
                painter = Icons.restartAlt,
                contentDescription = Strings.reset,
                onClick = {
                    if (targetProfile != null) {
                        persistence.profileManager.setProfilePalette(targetProfile.id, null)
                        persistence.profileManager.setProfileGrayscale(targetProfile.id, null)
                    } else {
                        persistence.reset()
                    }
                },
            )
        }
    }
}

@Composable
private fun PersistentPreferenceThemeMode(
    persistence: PersistenceManager,
    enabled: Boolean,
) {
    val primaryPalette = persistence.getPalettePrimary(null)
    var lastPrimaryPalette by remember { mutableStateOf(primaryPalette) }
    if (primaryPalette != null) lastPrimaryPalette = primaryPalette
    val fallbackPrimaryPalette = persistence.getActivePalette(profileId = null)

    PreferenceSwitch(
        title = Strings.preferencePaletteModeDynamic,
        description = Strings.preferencePaletteModeDynamicDescription,
        checked = primaryPalette == null,
        enabled = enabled,
        onCheckedChange = { followSystem ->
            persistence.setPalettePrimary(
                null,
                if (followSystem) null else lastPrimaryPalette ?: fallbackPrimaryPalette,
            )
        },
        modifier = Modifier.testTag(PersistentPreferenceThemeScreenSemantics.PALETTE_MODE),
    )
}

@Composable
private fun PersistentPreferenceThemeSystem(
    persistence: PersistenceManager,
    isSystemInDarkTheme: Boolean,
    overriddenBadge: Badge?,
    enabled: Boolean,
) {
    Column {
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            PersistentPreferenceThemeLight(
                persistence = persistence,
                isSystemInDarkTheme = isSystemInDarkTheme,
                overriddenBadge = overriddenBadge,
                enabled = enabled,
                modifier = Modifier.weight(1f),
            )
        }
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            PersistentPreferenceThemeDark(
                persistence = persistence,
                isSystemInDarkTheme = isSystemInDarkTheme,
                overriddenBadge = overriddenBadge,
                enabled = enabled,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PersistentPreferenceThemeStatic(
    persistence: PersistenceManager,
    overriddenBadge: Badge?,
    enabled: Boolean,
) {
    Column {
        Spacer(Modifier.height(8.dp))
        PreferenceRadioGroupPicker(
            title = Strings.preferenceStaticPaletteTitle,
            selectedId = persistence.getPalettePrimary(null)?.id,
            items = ColorPalette.entries.map { palette ->
                palette.asPreferenceRadioGroupItem(segment = palette.category.ordinal)
            },
            onSelectId = { id ->
                ColorPalette.fromIdOrNull(id)?.let { persistence.setPalettePrimary(null, it) }
            },
            badge = overriddenBadge,
            enabled = enabled,
            modifier = Modifier
                .testTag(PersistentPreferenceThemeScreenSemantics.PALETTE_PICKER)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PersistentPreferenceThemeLight(
    persistence: PersistenceManager,
    isSystemInDarkTheme: Boolean,
    overriddenBadge: Badge?,
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {
    PreferenceRadioGroupPicker(
        title = Strings.preferenceLightPaletteTitle,
        selectedId = persistence.paletteLight.id,
        items = ColorPalette.entries.map { palette ->
            palette.asPreferenceRadioGroupItem(
                segment = if (palette.isDark) 1 else 0,
                isDefault = palette == defaultLight,
            )
        },
        onSelectId = { id -> ColorPalette.fromIdOrNull(id)?.let { persistence.paletteLight = it } },
        description = Strings.preferenceLightPaletteDescription,
        badge = overriddenBadge ?: Badge.active.takeIf { !isSystemInDarkTheme },
        enabled = enabled,
        modifier = modifier
            .testTag(PersistentPreferenceThemeScreenSemantics.LIGHT_PICKER)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PersistentPreferenceThemeDark(
    persistence: PersistenceManager,
    isSystemInDarkTheme: Boolean,
    overriddenBadge: Badge?,
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {
    PreferenceRadioGroupPicker(
        title = Strings.preferenceDarkPaletteTitle,
        selectedId = persistence.paletteDark.id,
        items = ColorPalette.entries.map { palette ->
            palette.asPreferenceRadioGroupItem(
                segment = if (palette.isDark) 0 else 1,
                isDefault = palette == defaultDark,
            )
        },
        onSelectId = { id -> ColorPalette.fromIdOrNull(id)?.let { persistence.paletteDark = it } },
        description = Strings.preferenceDarkPaletteDescription,
        badge = overriddenBadge ?: Badge.active.takeIf { isSystemInDarkTheme },
        enabled = enabled,
        modifier = modifier
            .testTag(PersistentPreferenceThemeScreenSemantics.DARK_PICKER)
    )
}

@Composable
private fun PersistentPreferenceThemeGrayscale(
    persistence: PersistenceManager,
    overriddenBadge: Badge?,
    enabled: Boolean,
) {
    PreferenceSwitch(
        title = Strings.preferenceGrayscaleTitle,
        description = Strings.preferenceGrayscaleDescription,
        checked = persistence.isGrayscaleEnabled(null),
        onCheckedChange = { persistence.setGrayscaleEnabled(null, it) },
        enabled = enabled,
        badge = overriddenBadge,
        modifier = Modifier
            .testTag(PersistentPreferenceThemeScreenSemantics.GRAYSCALE)
    )
}

@Composable
private fun PersistentPreferenceThemeOutline(
    persistence: PersistenceManager,
    enabled: Boolean,
) {
    var value by remember(persistence.outline) { mutableStateOf(persistence.outline.value) }
    PreferenceSlider(
        title = Strings.preferenceOutlineTitle,
        value = value,
        onValueChange = { value = it },
        onValueChangeFinished = { persistence.outline = value.dp },
        enabled = enabled,
        valueLabelSuffix = "x",
        valueRange = 1f..5f,
        steps = 3,
        modifier = Modifier
            .testTag(PersistentPreferenceThemeScreenSemantics.OUTLINE)
    )
}

@Composable
private fun PersistentPreferenceThemeRoundness(
    persistence: PersistenceManager,
    enabled: Boolean,
) {
    var value by remember(persistence.roundness) { mutableStateOf(persistence.roundness) }
    PreferenceSlider(
        title = Strings.preferenceRoundnessTitle,
        value = value,
        onValueChange = { value = it },
        onValueChangeFinished = { persistence.roundness = value },
        enabled = enabled,
        valueLabelSuffix = "x",
        valueRange = 0f..1.6f,
        steps = 15,
        modifier = Modifier
            .testTag(PersistentPreferenceThemeScreenSemantics.ROUNDNESS)
    )
}

@ExperimentalKepkoApi
@PreviewWithTest
@Composable
internal fun PersistentPreferenceThemeScreenSystemLightPreview() {
    PreviewPersistentKepkoTheme(configure = {
        paletteLight = defaultLight; paletteDark = defaultLight
    }) {
        PersistentPreferenceThemeScreen(onBackClick = {}, isSystemInDarkTheme = false)
    }
}

@ExperimentalKepkoApi
@PreviewWithTest
@Composable
internal fun PersistentPreferenceThemeScreenSystemDarkPreview() {
    PreviewPersistentKepkoTheme(
        isSystemInDarkTheme = true,
        configure = { paletteLight = defaultDark; paletteDark = defaultDark }
    ) {
        PersistentPreferenceThemeScreen(onBackClick = {}, isSystemInDarkTheme = true)
    }
}

@ExperimentalKepkoApi
@PreviewWithTest
@Composable
internal fun PersistentPreferenceThemeScreenStyleSelectedPreview() {
    PreviewPersistentKepkoTheme(configure = { setPalettePrimary(null, ColorPalette.LIGHT) }) {
        PersistentPreferenceThemeScreen(onBackClick = {})
    }
}

@ExperimentalKepkoApi
@PreviewWithTest
@Composable
internal fun PersistentPreferenceThemeScreenGrayscalePreview() {
    PreviewPersistentKepkoTheme(configure = { setGrayscaleEnabled(null, true) }) {
        PersistentPreferenceThemeScreen(onBackClick = {})
    }
}

@ExperimentalKepkoApi
@PreviewWithTest
@Composable
internal fun PersistentPreferenceThemeScreenGlobalOverriddenSystemPreview() {
    PreviewPersistentKepkoTheme(configure = {
        profileManager.setProfilePalette(previewProfile.id, ColorPalette.SOLARIZED_DARK)
        profileManager.setProfileGrayscale(previewProfile.id, true)
    }) {
        PersistentPreferenceThemeScreen(
            onBackClick = {},
            activeProfile = previewProfile,
        )
    }
}

@ExperimentalKepkoApi
@PreviewWithTest
@Composable
internal fun PersistentPreferenceThemeScreenGlobalOverriddenStaticPreview() {
    PreviewPersistentKepkoTheme(configure = {
        setPalettePrimary(null, ColorPalette.LIGHT)
        profileManager.setProfilePalette(previewProfile.id, ColorPalette.SOLARIZED_DARK)
        profileManager.setProfileGrayscale(previewProfile.id, true)
    }) {
        PersistentPreferenceThemeScreen(
            onBackClick = {},
            activeProfile = previewProfile,
        )
    }
}

@ExperimentalKepkoApi
@PreviewWithTest
@Composable
internal fun PersistentPreferenceThemeScreenDisabledWithTopContentPreview() {
    PreviewPersistentKepkoTheme(configure = { setPalettePrimary(null, ColorPalette.LIGHT) }) {
        PersistentPreferenceThemeScreen(
            onBackClick = {},
            enabled = false,
            topContent = {
                PreferenceSwitch(
                    title = "Host content",
                    checked = true,
                    onCheckedChange = {},
                )
                Spacer(Modifier.height(24.dp))
            },
        )
    }
}

internal val previewProfile = UserVisibleProfile(
    id = "work",
    name = { "Work" },
    icon = { Icons.person },
)

@VisibleForTesting
internal object PersistentPreferenceThemeScreenSemantics {
    const val SCREEN = "theme_screen"
    const val PALETTE_MODE = "theme_palette_mode"
    const val PALETTE_PICKER = "theme_palette_picker"
    const val LIGHT_PICKER = "theme_light_picker"
    const val DARK_PICKER = "theme_dark_picker"
    const val GRAYSCALE = "theme_grayscale"
    const val OUTLINE = "theme_outline"
    const val ROUNDNESS = "theme_roundness"
    const val PROFILE_HEADER = "theme_profile_header"
    const val PROFILE_PALETTE_PICKER = "theme_profile_palette_picker"
    const val PROFILE_GRAYSCALE_PICKER = "theme_profile_grayscale_picker"
}
