package glass.yasan.kepko.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Indication
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import glass.yasan.kepko.foundation.color.ProvideLocalContentColor
import glass.yasan.kepko.foundation.color.getSemanticColors
import glass.yasan.kepko.foundation.theme.KepkoTheme
import androidx.compose.material3.ButtonDefaults as Material3ButtonDefaults

@Composable
public fun ButtonPrimitive(
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    onLongClick: (() -> Unit)? = null,
    onClickLabel: String? = null,
    onLongClickLabel: String? = null,
    onDoubleClick: (() -> Unit)? = null,
    hapticFeedbackEnabled: Boolean = true,
    containerColor: Color = ButtonPrimitiveDefaults.containerColor,
    contentColor: Color = ButtonPrimitiveDefaults.contentColor(containerColor),
    enabled: Boolean = true,
    shape: Shape = ButtonPrimitiveDefaults.shape,
    border: BorderStroke? = ButtonPrimitiveDefaults.border(containerColor),
    contentPadding: PaddingValues = ButtonPrimitiveDefaults.contentPadding(),
    indication: Indication? = ButtonPrimitiveDefaults.indication,
    interactionSource: MutableInteractionSource? = null,
) {
    val resolvedInteractionSource = interactionSource ?: remember { MutableInteractionSource() }
    val isHovered by resolvedInteractionSource.collectIsHoveredAsState()
    val disabledContainerColor = ButtonPrimitiveDefaults.disabledContainerColor(containerColor)
    val resolvedBorder = resolveBorder(
        border = border,
        containerColor = containerColor,
        disabledContainerColor = disabledContainerColor,
        enabled = enabled,
        isHovered = isHovered,
    )
    val resolvedContainerColor = if (enabled) containerColor else disabledContainerColor
    val resolvedContentColor = if (enabled) {
        contentColor
    } else {
        ButtonPrimitiveDefaults.disabledContentColor
    }

    ProvideLocalContentColor(
        color = resolvedContentColor,
    ) {
        Surface(
            modifier = modifier
                .minimumInteractiveComponentSize()
                .clip(shape)
                .combinedClickable(
                    interactionSource = resolvedInteractionSource,
                    indication = indication,
                    enabled = enabled,
                    onClickLabel = onClickLabel,
                    role = Role.Button,
                    onLongClickLabel = onLongClickLabel,
                    onLongClick = onLongClick,
                    onDoubleClick = onDoubleClick,
                    hapticFeedbackEnabled = hapticFeedbackEnabled,
                    onClick = onClick,
                ),
            shape = shape,
            color = resolvedContainerColor,
            contentColor = resolvedContentColor,
            border = resolvedBorder,
        ) {
            CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.labelLarge) {
                Row(
                    modifier = Modifier
                        .defaultMinSize(
                            minWidth = Material3ButtonDefaults.MinWidth,
                            minHeight = Material3ButtonDefaults.MinHeight,
                        )
                        .padding(contentPadding),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    content = content,
                )
            }
        }
    }
}

@Composable
private fun resolveBorder(
    border: BorderStroke?,
    containerColor: Color,
    disabledContainerColor: Color,
    enabled: Boolean,
    isHovered: Boolean,
): BorderStroke? {
    // A container that falls back to a layer while disabled also takes that layer's border, so the
    // button keeps an outline instead of dissolving into the surface behind it.
    val disabledBorder = if (disabledContainerColor == containerColor) {
        border
    } else {
        border ?: ButtonPrimitiveDefaults.border(disabledContainerColor)
    }
    val sourceBorder = if (enabled) border else disabledBorder
    val hoverIncrease = sourceBorder?.width?.times(0.5f)?.coerceAtMost(1.dp) ?: 0.dp
    val animatedWidth by animateDpAsState(
        targetValue = if (enabled && isHovered && sourceBorder != null) {
            sourceBorder.width + hoverIncrease
        } else {
            sourceBorder?.width ?: 0.dp
        },
        label = "ButtonPrimitiveBorderWidth",
    )

    return sourceBorder?.let {
        BorderStroke(width = animatedWidth, brush = it.brush)
    }
}

@PreviewWithTest
@Composable
internal fun ButtonPrimitiveLightPreview() {
    KepkoTheme(palette = LIGHT) { PreviewContent() }
}

@PreviewWithTest
@Composable
internal fun ButtonPrimitiveDarkPreview() {
    KepkoTheme(palette = DARK) { PreviewContent() }
}

@PreviewWithTest
@Composable
internal fun ButtonPrimitiveBlackPreview() {
    KepkoTheme(palette = BLACK) { PreviewContent() }
}

@PreviewWithTest
@Composable
internal fun ButtonPrimitiveSolarizedLightPreview() {
    KepkoTheme(palette = SOLARIZED_LIGHT) { PreviewContent() }
}

@PreviewWithTest
@Composable
internal fun ButtonPrimitiveSolarizedDarkPreview() {
    KepkoTheme(palette = SOLARIZED_DARK) { PreviewContent() }
}

@Composable
private fun PreviewContent() {
    val containerColors = KepkoTheme.colors.getSemanticColors() +
            KepkoTheme.colors.foreground +
            KepkoTheme.colors.content

    Column(
        modifier = Modifier.background(KepkoTheme.colors.foreground)
    ) {
        containerColors.forEach { containerColor ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.padding(
                    vertical = 2.dp,
                    horizontal = 4.dp,
                ),
            ) {
                ButtonPrimitive(
                    onClick = {},
                    containerColor = containerColor,
                    content = {
                        Text(
                            text = "Button",
                            maxLines = 1,
                        )
                    },
                    modifier = Modifier.weight(1f),
                )
                ButtonPrimitive(
                    onClick = {},
                    enabled = false,
                    containerColor = containerColor,
                    content = {
                        Text(
                            text = "Button",
                            maxLines = 1,
                        )
                    },
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}
