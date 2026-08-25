package glass.yasan.kepko.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import glass.yasan.kepko.foundation.theme.KepkoTheme
import glass.yasan.kepko.resource.Icons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun PreferenceRadioGroupPicker(
    title: String,
    selectedId: String?,
    items: List<PreferenceRadioGroupItem>,
    onSelectId: (String) -> Unit,
    leadingIcon: Painter,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(),
    description: String? = null,
    badge: Badge? = null,
    enabled: Boolean = true,
    closeOnSelection: Boolean = true,
) {
    PreferenceRadioGroupPickerContent(
        title = title,
        selectedId = selectedId,
        items = items,
        onSelectId = onSelectId,
        modifier = modifier,
        sheetState = sheetState,
        description = description,
        badge = badge,
        enabled = enabled,
        closeOnSelection = closeOnSelection,
        titleIcon = leadingIcon,
        leadingContent = {
            Icon(
                painter = leadingIcon,
                contentDescription = null,
                modifier = Modifier.padding(end = 12.dp),
            )
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun PreferenceRadioGroupPicker(
    title: String,
    selectedId: String?,
    items: List<PreferenceRadioGroupItem>,
    onSelectId: (String) -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(),
    description: String? = null,
    badge: Badge? = null,
    enabled: Boolean = true,
    closeOnSelection: Boolean = true,
    leadingContent: @Composable () -> Unit = {},
) {
    PreferenceRadioGroupPickerContent(
        title = title,
        selectedId = selectedId,
        items = items,
        onSelectId = onSelectId,
        modifier = modifier,
        sheetState = sheetState,
        description = description,
        badge = badge,
        enabled = enabled,
        closeOnSelection = closeOnSelection,
        leadingContent = leadingContent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PreferenceRadioGroupPickerContent(
    title: String,
    selectedId: String?,
    items: List<PreferenceRadioGroupItem>,
    onSelectId: (String) -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(),
    description: String? = null,
    badge: Badge? = null,
    enabled: Boolean = true,
    closeOnSelection: Boolean = true,
    titleIcon: Painter? = null,
    leadingContent: @Composable () -> Unit = {},
) {
    var showSheet by remember { mutableStateOf(false) }

    val selectedTitle = items.firstOrNull { it.id == selectedId }?.title()

    Button(
        text = title,
        onClick = { showSheet = true },
        enabled = enabled,
        badge = badge,
        description = selectedTitle,
        modifier = modifier,
        leadingContent = { leadingContent() },
        trailingContent = {
            Icon(
                painter = Icons.chevronForward,
                contentDescription = null,
                modifier = Modifier.padding(start = 12.dp),
            )
        },
    )

    PreferenceRadioGroupSheet(
        title = title,
        description = description,
        selectedId = selectedId,
        items = items,
        onSelectId = onSelectId,
        visible = showSheet,
        onDismiss = { showSheet = false },
        sheetState = sheetState,
        closeOnSelection = closeOnSelection,
        icon = titleIcon,
    )
}

@PreviewWithTest
@Composable
internal fun PreferenceRadioGroupPickerLightPreview() {
    KepkoTheme(palette = LIGHT) { PreviewContent() }
}

@PreviewWithTest
@Composable
internal fun PreferenceRadioGroupPickerDarkPreview() {
    KepkoTheme(palette = DARK) { PreviewContent() }
}

@PreviewWithTest
@Composable
internal fun PreferenceRadioGroupPickerBlackPreview() {
    KepkoTheme(palette = BLACK) { PreviewContent() }
}

@PreviewWithTest
@Composable
internal fun PreferenceRadioGroupPickerSolarizedLightPreview() {
    KepkoTheme(palette = SOLARIZED_LIGHT) { PreviewContent() }
}

@PreviewWithTest
@Composable
internal fun PreferenceRadioGroupPickerSolarizedDarkPreview() {
    KepkoTheme(palette = SOLARIZED_DARK) { PreviewContent() }
}

@PreviewWithTest
@Composable
internal fun PreferenceRadioGroupPickerDisabledParityLightPreview() {
    KepkoTheme(palette = LIGHT) { DisabledParityPreviewContent() }
}

@PreviewWithTest
@Composable
internal fun PreferenceRadioGroupPickerDisabledParityDarkPreview() {
    KepkoTheme(palette = DARK) { DisabledParityPreviewContent() }
}

@PreviewWithTest
@Composable
internal fun PreferenceRadioGroupPickerDisabledParityBlackPreview() {
    KepkoTheme(palette = BLACK) { DisabledParityPreviewContent() }
}

@PreviewWithTest
@Composable
internal fun PreferenceRadioGroupPickerDisabledParitySolarizedLightPreview() {
    KepkoTheme(palette = SOLARIZED_LIGHT) { DisabledParityPreviewContent() }
}

@PreviewWithTest
@Composable
internal fun PreferenceRadioGroupPickerDisabledParitySolarizedDarkPreview() {
    KepkoTheme(palette = SOLARIZED_DARK) { DisabledParityPreviewContent() }
}

/**
 * A disabled [PreferenceRadioGroupPicker] next to a disabled [PreferenceContainer], so the two
 * disabled treatments can be compared side by side.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DisabledParityPreviewContent() {
    val items = listOf(
        PreferenceRadioGroupItem("item1") { "Item 1" },
        PreferenceRadioGroupItem("item2") { "Item 2" },
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .background(KepkoTheme.colors.midground)
            .padding(vertical = 16.dp),
    ) {
        PreferenceRadioGroupPicker(
            title = "Picker",
            selectedId = "item1",
            items = items,
            onSelectId = {},
            enabled = false,
            leadingIcon = Icons.settings,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        PreferenceContainer(
            title = "Container",
            description = "Item 1",
            enabled = false,
            modifier = Modifier.padding(horizontal = 16.dp),
            leadingContent = {
                Icon(
                    painter = Icons.settings,
                    contentDescription = null,
                )
            },
        )
        PreferenceRadioGroupPicker(
            title = "Picker",
            selectedId = "item1",
            items = items,
            onSelectId = {},
            leadingIcon = Icons.settings,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        PreferenceContainer(
            title = "Container",
            description = "Item 1",
            modifier = Modifier.padding(horizontal = 16.dp),
            leadingContent = {
                Icon(
                    painter = Icons.settings,
                    contentDescription = null,
                )
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PreviewContent() {
    val items = listOf(
        PreferenceRadioGroupItem(
            "item1",
            description = "A short description for Item 1.",
        ) { "Item 1" },
        PreferenceRadioGroupItem("item2", Badge.experimental) { "Item 2" },
        PreferenceRadioGroupItem("item3", segment = 1) { "Item 3" },
        PreferenceRadioGroupItem("item4", segment = 1, enabled = false) { "Item 4" },
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .background(KepkoTheme.colors.midground)
            .padding(vertical = 16.dp),
    ) {
        PreferenceRadioGroupPicker(
            title = "Preference",
            selectedId = "item1",
            items = items,
            onSelectId = {},
            description = "Lorem ipsum dolor sit amet.",
            badge = Badge.beta,
            leadingIcon = Icons.settings,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        PreferenceRadioGroupPicker(
            title = "Preference",
            selectedId = "item2",
            items = items,
            onSelectId = {},
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        PreferenceRadioGroupPicker(
            title = "Preference",
            selectedId = "item1",
            items = items,
            onSelectId = {},
            enabled = false,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
    }
}
