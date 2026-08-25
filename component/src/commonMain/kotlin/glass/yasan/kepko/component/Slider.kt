package glass.yasan.kepko.component

import androidx.annotation.IntRange
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import glass.yasan.kepko.foundation.theme.KepkoTheme
import androidx.compose.material3.Slider as Material3Slider
import androidx.compose.material3.SliderDefaults as Material3SliderDefaults

@Composable
public fun Slider(
    value: Float,
    onValueChange: (Float) -> Unit,
    onValueChangeFinished: (() -> Unit)? = null,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    @IntRange(from = 0) steps: Int = 0,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val animatedValue by animateFloatAsState(value)

    Material3Slider(
        value = animatedValue,
        onValueChange = onValueChange,
        onValueChangeFinished = onValueChangeFinished,
        enabled = enabled,
        valueRange = valueRange,
        steps = steps,
        interactionSource = interactionSource,
        colors = Material3SliderDefaults.colors(
            activeTickColor = KepkoTheme.colors.foreground,
            activeTrackColor = KepkoTheme.colors.content,
            disabledActiveTickColor = KepkoTheme.colors.foreground,
            disabledActiveTrackColor = KepkoTheme.colors.contentDisabled,
            disabledInactiveTickColor = KepkoTheme.colors.contentDisabled,
            disabledInactiveTrackColor = KepkoTheme.colors.background,
            disabledThumbColor = KepkoTheme.colors.contentDisabled,
            inactiveTickColor = KepkoTheme.colors.content,
            inactiveTrackColor = KepkoTheme.colors.background,
            thumbColor = KepkoTheme.colors.content,
        ),
        modifier = modifier,
    )
}

@PreviewWithTest
@Composable
internal fun SliderLightPreview() {
    KepkoTheme(palette = LIGHT) { PreviewContent() }
}

@PreviewWithTest
@Composable
internal fun SliderDarkPreview() {
    KepkoTheme(palette = DARK) { PreviewContent() }
}

@PreviewWithTest
@Composable
internal fun SliderBlackPreview() {
    KepkoTheme(palette = BLACK) { PreviewContent() }
}

@PreviewWithTest
@Composable
internal fun SliderSolarizedLightPreview() {
    KepkoTheme(palette = SOLARIZED_LIGHT) { PreviewContent() }
}

@PreviewWithTest
@Composable
internal fun SliderSolarizedDarkPreview() {
    KepkoTheme(palette = SOLARIZED_DARK) { PreviewContent() }
}

@Composable
private fun PreviewContent() {
    val values = arrayOf(0, 25, 50, 75, 100)
    val enabled = arrayOf(true, false)

    Column(
        modifier = Modifier.background(KepkoTheme.colors.foreground)
    ) {
        values.forEach { value ->
            enabled.forEach { isEnabled ->
                Column(
                    modifier = Modifier.padding(16.dp),
                ) {
                    Slider(
                        value = value / 100f,
                        onValueChange = {},
                        steps = 5,
                        enabled = isEnabled,
                    )
                }
                HorizontalDivider()
            }
        }
    }
}
