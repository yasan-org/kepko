package glass.yasan.kepko.component

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import glass.yasan.kepko.foundation.annotation.ExperimentalKepkoApi
import glass.yasan.kepko.foundation.system.hostSystemBarsModalBottomSheetProperties
import glass.yasan.kepko.foundation.theme.KepkoTheme
import androidx.compose.material3.BottomSheetDefaults as Material3BottomSheetDefaults

@OptIn(ExperimentalMaterial3Api::class, ExperimentalKepkoApi::class)
public object ModalBottomSheetDefaults {

    public val sheetMaxWidth: Dp = Material3BottomSheetDefaults.SheetMaxWidth

    public val shape: Shape = RectangleShape

    public val scrimColor: Color
        @Composable get() = KepkoTheme.colors.scrim

    public val contentWindowInsets: WindowInsets
        @Composable get() = Material3BottomSheetDefaults.windowInsets

    @Composable
    public fun colors(
        containerColor: Color = KepkoTheme.colors.midground,
        contentColor: Color = KepkoTheme.colors.content,
    ): ModalBottomSheetColors = ModalBottomSheetColors(
        containerColor = containerColor,
        contentColor = contentColor,
    )

    /**
     * Properties that keep the system bar icons as the screen behind the sheet has them, instead of recoloring them
     * for the sheet's content color.
     */
    @Composable
    public fun properties(
        shouldDismissOnBackPress: Boolean = true,
        shouldDismissOnClickOutside: Boolean = true,
    ): ModalBottomSheetProperties = hostSystemBarsModalBottomSheetProperties(
        shouldDismissOnBackPress = shouldDismissOnBackPress,
        shouldDismissOnClickOutside = shouldDismissOnClickOutside,
    )
}
