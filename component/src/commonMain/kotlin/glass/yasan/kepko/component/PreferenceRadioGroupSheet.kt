package glass.yasan.kepko.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import glass.yasan.kepko.component.extensions.isFullWidth
import glass.yasan.kepko.foundation.annotation.ExperimentalKepkoApi
import glass.yasan.kepko.foundation.theme.KepkoTheme
import glass.yasan.kepko.resource.Icons
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@ExperimentalKepkoApi
@Composable
public fun PreferenceRadioGroupSheet(
    title: String,
    selectedId: String?,
    items: List<PreferenceRadioGroupItem>,
    onSelectItem: (PreferenceRadioGroupItem) -> Unit,
    modifier: Modifier = Modifier,
    description: String? = null,
    leadingContent: @Composable () -> Unit = {},
) {
    PreferenceRadioGroupSheetContent(
        title = title,
        description = description,
        selectedId = selectedId,
        items = items,
        onSelectItem = onSelectItem,
        modifier = modifier,
        leadingContent = leadingContent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalKepkoApi
@Composable
public fun PreferenceRadioGroupSheet(
    title: String,
    description: String?,
    selectedId: String?,
    items: List<PreferenceRadioGroupItem>,
    onSelectId: (String) -> Unit,
    visible: Boolean,
    onDismiss: () -> Unit,
    sheetState: SheetState,
    closeOnSelection: Boolean,
    icon: Painter? = null,
) {
    if (visible) {
        val scope = rememberCoroutineScope()
        val closeJob = remember { arrayOfNulls<Job>(1) }
        val close = {
            // One close at a time, and dismiss only once the sheet has actually hidden.
            if (closeJob[0]?.isActive != true) {
                closeJob[0] = scope.launch {
                    sheetState.hide()
                    if (!sheetState.isVisible) onDismiss()
                }
            }
        }

        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            modifier = Modifier.onKeyEvent { event ->
                val sideArrow = event.key == Key.DirectionLeft || event.key == Key.DirectionRight
                if (sideArrow && event.type == KeyEventType.KeyDown) close()
                sideArrow
            },
            title = ModalBottomSheetTitle(
                text = title,
                icon = icon,
                description = description,
            ),
            content = {
                PreferenceRadioGroupSheetContent(
                    selectedId = selectedId,
                    items = items,
                    onSelectItem = { item ->
                        onSelectId(item.id)
                        if (closeOnSelection) close()
                    },
                )
            },
        )
    }
}

@Composable
private fun PreferenceRadioGroupSheetContent(
    selectedId: String?,
    items: List<PreferenceRadioGroupItem>,
    onSelectItem: (PreferenceRadioGroupItem) -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    description: String? = null,
    leadingContent: @Composable () -> Unit = {},
) {
    BoxWithConstraints(
        modifier = modifier,
    ) {
        val contentPadding by animateDpAsState(
            targetValue = if (isFullWidth()) 0.dp else 16.dp,
            label = "contentPadding",
        )

        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
        ) {
            if (title != null) {
                PreferenceRadioGroupSheetTitle(
                    title = ModalBottomSheetTitle(
                        text = title,
                        description = description,
                    ),
                    leadingContent = leadingContent,
                )
            }
            SegmentedColumn(items = items) { segmentItems ->
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(horizontal = contentPadding),
                ) {
                    segmentItems.forEach { item ->
                        PreferenceRadioButton(
                            title = item.title(),
                            description = item.description,
                            selected = item.id == selectedId,
                            onClick = { onSelectItem(item) },
                            enabled = item.enabled,
                            badge = item.badge,
                            leadingContent = {
                                item.icon?.let { painter ->
                                    Icon(
                                        painter = painter,
                                        contentDescription = null,
                                    )
                                }
                            },
                            modifier = Modifier.testTag(PreferenceRadioGroupSemantics.item(item.id)),
                        )
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun PreferenceRadioGroupSheetTitle(
    title: ModalBottomSheetTitle,
    leadingContent: @Composable () -> Unit,
) {
    ModalBottomSheetTitleContent(
        title = title,
        leadingContent = leadingContent,
    )
}

@PreviewWithTest
@Composable
internal fun PreferenceRadioGroupSheetContentLightPreview() {
    KepkoTheme(palette = LIGHT) { SheetContentPreviewContent() }
}

@PreviewWithTest
@Composable
internal fun PreferenceRadioGroupSheetContentDarkPreview() {
    KepkoTheme(palette = DARK) { SheetContentPreviewContent() }
}

@PreviewWithTest
@Composable
internal fun PreferenceRadioGroupSheetContentBlackPreview() {
    KepkoTheme(palette = BLACK) { SheetContentPreviewContent() }
}

@PreviewWithTest
@Composable
internal fun PreferenceRadioGroupSheetContentSolarizedLightPreview() {
    KepkoTheme(palette = SOLARIZED_LIGHT) { SheetContentPreviewContent() }
}

@PreviewWithTest
@Composable
internal fun PreferenceRadioGroupSheetContentSolarizedDarkPreview() {
    KepkoTheme(palette = SOLARIZED_DARK) { SheetContentPreviewContent() }
}

@Composable
private fun SheetContentPreviewContent() {
    val items = listOf(
        PreferenceRadioGroupItem(
            "item1",
            icon = Icons.check,
            description = "A short description for Item 1.",
        ) { "Item 1" },
        PreferenceRadioGroupItem("item2", Badge.experimental) { "Item 2" },
    )

    PreferenceRadioGroupSheet(
        title = "Preference",
        description = "Lorem ipsum dolor sit amet.",
        selectedId = "item1",
        items = items,
        onSelectItem = {},
        leadingContent = {
            Icon(
                painter = Icons.settings,
                contentDescription = null,
                modifier = Modifier.padding(end = 12.dp),
            )
        },
    )
}
