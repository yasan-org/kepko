package glass.yasan.kepko.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import glass.yasan.kepko.foundation.annotation.ExperimentalKepkoApi

/**
 * Whether the window is at least tablet-sized, as on tablets, Chromebooks and desktops.
 */
@ExperimentalKepkoApi
@Composable
public fun isLargeWindow(): Boolean = isLargeWindow(LocalWindowInfo.current.containerDpSize)

@ExperimentalKepkoApi
public fun isLargeWindow(size: DpSize): Boolean = minOf(size.width, size.height) >= 600.dp

/**
 * Whether the window is wide enough to show a list and its detail side by side: an expanded width and a height
 * that is not compact, as on tablets in landscape, desktops and TVs.
 */
@ExperimentalKepkoApi
@Composable
public fun isTwoPaneWindow(): Boolean = isTwoPaneWindow(LocalWindowInfo.current.containerDpSize)

@ExperimentalKepkoApi
public fun isTwoPaneWindow(size: DpSize): Boolean = size.width >= 840.dp && size.height >= 480.dp
