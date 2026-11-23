package glass.yasan.kepko.foundation.system

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.Window
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import glass.yasan.kepko.foundation.annotation.ExperimentalKepkoApi

@ExperimentalKepkoApi
@ExperimentalMaterial3Api
@Composable
public actual fun hostSystemBarsModalBottomSheetProperties(
    shouldDismissOnBackPress: Boolean,
    shouldDismissOnClickOutside: Boolean,
): ModalBottomSheetProperties {
    val hostWindow = currentHostWindow()

    // Read once per host window, so the sheet keeps the appearance the host had when the sheet opened.
    return remember(hostWindow, shouldDismissOnBackPress, shouldDismissOnClickOutside) {
        if (hostWindow == null) {
            ModalBottomSheetProperties(
                shouldDismissOnBackPress = shouldDismissOnBackPress,
                shouldDismissOnClickOutside = shouldDismissOnClickOutside,
            )
        } else {
            val insetsController = WindowCompat.getInsetsController(hostWindow, hostWindow.decorView)
            ModalBottomSheetProperties(
                isAppearanceLightStatusBars = insetsController.isAppearanceLightStatusBars,
                isAppearanceLightNavigationBars = insetsController.isAppearanceLightNavigationBars,
                shouldDismissOnBackPress = shouldDismissOnBackPress,
                shouldDismissOnClickOutside = shouldDismissOnClickOutside,
            )
        }
    }
}

/** The window the caller is composed in: the enclosing dialog's window, or else the activity's. */
@Composable
private fun currentHostWindow(): Window? {
    val view = LocalView.current
    if (view.isInEditMode) return null

    return currentDialogWindow() ?: view.context.findActivity()?.window
}

private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
