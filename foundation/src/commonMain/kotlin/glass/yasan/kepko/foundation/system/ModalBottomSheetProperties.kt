package glass.yasan.kepko.foundation.system

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.runtime.Composable
import glass.yasan.kepko.foundation.annotation.ExperimentalKepkoApi

/**
 * Properties for a modal bottom sheet that leaves the system bar icons as the window behind it has them.
 *
 * On Android a modal bottom sheet opens in its own window, and Material3 picks that window's status and navigation
 * bar icon colors from the sheet's content color. The bars sit over the screen behind the sheet, not over the sheet,
 * so the icons can end up with no contrast against it. These properties copy the icon appearance from the host window
 * instead. On other platforms they are the Material3 defaults.
 */
@ExperimentalKepkoApi
@ExperimentalMaterial3Api
@Composable
public expect fun hostSystemBarsModalBottomSheetProperties(
    shouldDismissOnBackPress: Boolean = true,
    shouldDismissOnClickOutside: Boolean = true,
): ModalBottomSheetProperties
