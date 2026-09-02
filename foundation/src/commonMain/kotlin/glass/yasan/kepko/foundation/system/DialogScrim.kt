package glass.yasan.kepko.foundation.system

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.DialogProperties
import glass.yasan.kepko.foundation.annotation.ExperimentalKepkoApi

/**
 * Copies these properties with [color] as the dialog scrim.
 *
 * Android returns the receiver unchanged, since its scrim is set by [DialogScrimEffect].
 */
@ExperimentalKepkoApi
public expect fun DialogProperties.withScrimColor(color: Color): DialogProperties

/**
 * Dims the dialog window this is composed in with the alpha of [color]. Android only, no-op elsewhere.
 */
@ExperimentalKepkoApi
@Composable
public expect fun DialogScrimEffect(color: Color)
