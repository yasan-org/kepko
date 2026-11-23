package glass.yasan.kepko.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import glass.yasan.kepko.foundation.annotation.ExperimentalKepkoApi
import glass.yasan.kepko.foundation.system.DialogNavigationBarContrastEffect
import glass.yasan.kepko.foundation.theme.KepkoTheme
import androidx.compose.material3.ModalBottomSheet as Material3ModalBottomSheet

@OptIn(ExperimentalMaterial3Api::class, ExperimentalKepkoApi::class)
@Composable
public fun ModalBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(),
    sheetMaxWidth: Dp = ModalBottomSheetDefaults.sheetMaxWidth,
    sheetGesturesEnabled: Boolean = true,
    colors: ModalBottomSheetColors = ModalBottomSheetDefaults.colors(),
    shape: Shape = ModalBottomSheetDefaults.shape,
    tonalElevation: Dp = 0.dp,
    scrimColor: Color = ModalBottomSheetDefaults.scrimColor,
    dragHandle: @Composable (() -> Unit)? = { DragHandle() },
    contentWindowInsets: @Composable () -> WindowInsets = { ModalBottomSheetDefaults.contentWindowInsets },
    properties: ModalBottomSheetProperties = ModalBottomSheetDefaults.properties(),
    header: @Composable ColumnScope.() -> Unit = { HorizontalDivider(Modifier.fillMaxWidth()) },
    title: ModalBottomSheetTitle? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    Material3ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        sheetMaxWidth = sheetMaxWidth,
        sheetGesturesEnabled = sheetGesturesEnabled,
        containerColor = colors.containerColor,
        contentColor = colors.contentColor,
        shape = shape,
        tonalElevation = tonalElevation,
        scrimColor = scrimColor,
        dragHandle = dragHandle,
        contentWindowInsets = contentWindowInsets,
        properties = properties,
        modifier = modifier,
    ) {
        DialogNavigationBarContrastEffect()
        header()
        if (title != null) {
            ModalBottomSheetTitleContent(
                title = title,
            )
        }
        content()
    }
}

@Composable
internal fun ModalBottomSheetTitleContent(
    title: ModalBottomSheetTitle,
    modifier: Modifier = Modifier,
    leadingContent: @Composable (() -> Unit)? = null,
) {
    TitleBarContent(
        modifier = modifier,
        windowInsets = null,
        containerColor = KepkoTheme.colors.midground,
    ) {
        Row(
            verticalAlignment = if (title.description == null) Alignment.Top else Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 16.dp, end = 16.dp),
        ) {
            if (leadingContent != null) {
                leadingContent()
            } else if (title.icon != null) {
                Icon(
                    painter = title.icon,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 12.dp),
                )
            }
            Column {
                Text(
                    text = title.text.uppercase(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = KepkoTheme.colors.content,
                )
                if (title.description != null) {
                    Text(
                        text = title.description,
                        color = KepkoTheme.colors.contentSubtle,
                    )
                }
            }
        }
    }
    Spacer(Modifier.height(16.dp))
}
