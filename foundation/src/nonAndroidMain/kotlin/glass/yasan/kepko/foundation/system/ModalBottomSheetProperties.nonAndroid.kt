package glass.yasan.kepko.foundation.system

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.runtime.Composable
import glass.yasan.kepko.foundation.annotation.ExperimentalKepkoApi

@ExperimentalKepkoApi
@ExperimentalMaterial3Api
@Composable
public actual fun hostSystemBarsModalBottomSheetProperties(
    shouldDismissOnBackPress: Boolean,
    shouldDismissOnClickOutside: Boolean,
): ModalBottomSheetProperties = ModalBottomSheetProperties(
    shouldDismissOnBackPress = shouldDismissOnBackPress,
    shouldDismissOnClickOutside = shouldDismissOnClickOutside,
)
