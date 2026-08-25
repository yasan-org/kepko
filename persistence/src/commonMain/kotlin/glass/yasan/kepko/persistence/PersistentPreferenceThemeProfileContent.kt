package glass.yasan.kepko.persistence

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import glass.yasan.kepko.component.Badge
import glass.yasan.kepko.component.PreferenceRadioGroupItem
import glass.yasan.kepko.component.PreferenceRadioGroupPicker
import glass.yasan.kepko.foundation.annotation.ExperimentalKepkoApi
import glass.yasan.kepko.foundation.theme.ColorPalette
import glass.yasan.kepko.persistence.internal.asPreferenceRadioGroupItem
import glass.yasan.kepko.resource.Strings

@Composable
internal fun PersistentPreferenceThemeProfileContent(
    persistence: PersistenceManager,
    profile: UserVisibleProfile,
    enabled: Boolean,
    topContent: @Composable ColumnScope.() -> Unit,
    bottomContent: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState()),
    ) {
        topContent()
        PersistentPreferenceThemeProfilePalette(
            persistence = persistence,
            profileId = profile.id,
            enabled = enabled,
        )
        Spacer(Modifier.height(8.dp))
        PersistentPreferenceThemeProfileGrayscale(
            persistence = persistence,
            profileId = profile.id,
            enabled = enabled,
        )
        bottomContent()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PersistentPreferenceThemeProfilePalette(
    persistence: PersistenceManager,
    profileId: String,
    enabled: Boolean,
) {
    val followGlobalItem = PreferenceRadioGroupItem(
        id = PreferenceValueIds.FOLLOW_GLOBAL_ID,
        badge = Badge.default,
        description = persistence.getPalettePrimary(null)?.title?.invoke()
            ?: "${persistence.paletteLight.title()} / ${persistence.paletteDark.title()}",
    ) {
        Strings.preferenceFollowGlobalTitle
    }

    PreferenceRadioGroupPicker(
        title = Strings.preferenceStaticPaletteTitle,
        selectedId = persistence.profileManager.getProfilePalette(profileId)?.id
            ?: PreferenceValueIds.FOLLOW_GLOBAL_ID,
        items = listOf(followGlobalItem) + ColorPalette.entries.map { palette ->
            palette.asPreferenceRadioGroupItem(segment = palette.category.ordinal + 1)
        },
        onSelectId = { id ->
            if (id == PreferenceValueIds.FOLLOW_GLOBAL_ID) {
                persistence.setPalettePrimary(profileId, null)
            } else {
                ColorPalette.fromIdOrNull(id)?.let { persistence.setPalettePrimary(profileId, it) }
            }
        },
        enabled = enabled,
        modifier = Modifier
            .testTag(PersistentPreferenceThemeScreenSemantics.PROFILE_PALETTE_PICKER)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PersistentPreferenceThemeProfileGrayscale(
    persistence: PersistenceManager,
    profileId: String,
    enabled: Boolean,
) {
    val globalValue = if (persistence.isGrayscaleEnabled(null)) Strings.enabled else Strings.disabled

    PreferenceRadioGroupPicker(
        title = Strings.preferenceGrayscaleTitle,
        description = Strings.preferenceGrayscaleDescription,
        selectedId = when (persistence.profileManager.getProfileGrayscale(profileId)) {
            null -> PreferenceValueIds.FOLLOW_GLOBAL_ID
            true -> PreferenceValueIds.GRAYSCALE_ENABLED_ID
            false -> PreferenceValueIds.GRAYSCALE_DISABLED_ID
        },
        items = listOf(
            PreferenceRadioGroupItem(
                id = PreferenceValueIds.FOLLOW_GLOBAL_ID,
                badge = Badge.default,
                description = globalValue,
            ) {
                Strings.preferenceFollowGlobalTitle
            },
            PreferenceRadioGroupItem(id = PreferenceValueIds.GRAYSCALE_ENABLED_ID, segment = 1) { Strings.enabled },
            PreferenceRadioGroupItem(id = PreferenceValueIds.GRAYSCALE_DISABLED_ID, segment = 1) { Strings.disabled },
        ),
        onSelectId = { id ->
            persistence.profileManager.setProfileGrayscale(
                profileId,
                when (id) {
                    PreferenceValueIds.GRAYSCALE_ENABLED_ID -> true
                    PreferenceValueIds.GRAYSCALE_DISABLED_ID -> false
                    else -> null
                },
            )
        },
        enabled = enabled,
        modifier = Modifier
            .testTag(PersistentPreferenceThemeScreenSemantics.PROFILE_GRAYSCALE_PICKER)
    )
}

@ExperimentalKepkoApi
@PreviewWithTest
@Composable
internal fun PersistentPreferenceThemeScreenProfileFollowGlobalPreview() {
    PreviewPersistentKepkoTheme {
        PersistentPreferenceThemeScreen(
            onBackClick = {},
            targetProfile = previewProfile,
        )
    }
}

@ExperimentalKepkoApi
@PreviewWithTest
@Composable
internal fun PersistentPreferenceThemeScreenProfileOverriddenPreview() {
    PreviewPersistentKepkoTheme(configure = {
        profileManager.setProfilePalette(previewProfile.id, ColorPalette.SOLARIZED_DARK)
        profileManager.setProfileGrayscale(previewProfile.id, true)
    }) {
        PersistentPreferenceThemeScreen(
            onBackClick = {},
            activeProfile = previewProfile,
            targetProfile = previewProfile,
        )
    }
}

private object PreferenceValueIds {
    const val FOLLOW_GLOBAL_ID = "follow-global"
    const val GRAYSCALE_ENABLED_ID = "enabled"
    const val GRAYSCALE_DISABLED_ID = "disabled"
}
