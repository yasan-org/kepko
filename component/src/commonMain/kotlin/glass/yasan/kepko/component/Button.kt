package glass.yasan.kepko.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import glass.yasan.kepko.foundation.border.borderStrokeFor
import glass.yasan.kepko.foundation.color.contentColorFor
import glass.yasan.kepko.foundation.color.getSemanticColors
import glass.yasan.kepko.foundation.theme.KepkoTheme
import org.jetbrains.compose.resources.painterResource

@Composable
public fun Button(
    text: String?,
    onClick: () -> Unit,
    leadingIcon: Painter?,
    modifier: Modifier = Modifier,
    description: String? = null,
    onClickLabel: String? = null,
    onLongClick: (() -> Unit)? = null,
    onLongClickLabel: String? = null,
    onDoubleClick: (() -> Unit)? = null,
    contentModifier: Modifier = Modifier,
    containerColor: Color = KepkoTheme.colors.foreground,
    contentColor: Color = contentColorFor(containerColor),
    enabled: Boolean = true,
    shape: Shape = ButtonDefaults.shape(),
    textAlign: TextAlign = TextAlign.Start,
    fontSize: TextUnit = 14.sp,
    fontWeight: FontWeight = FontWeight.Bold,
    border: BorderStroke? = borderStrokeFor(containerColor),
    contentPadding: PaddingValues = ButtonDefaults.contentPadding(),
    interactionSource: MutableInteractionSource? = null,
    fillWidth: Boolean = true,
    badge: Badge? = null,
    trailingIcon: Painter? = null,
    hapticFeedbackEnabled: Boolean = true,
) {
    ButtonInternal(
        text = text,
        description = description,
        modifier = modifier,
        onClick = onClick,
        onClickLabel = onClickLabel,
        onLongClick = onLongClick,
        onLongClickLabel = onLongClickLabel,
        onDoubleClick = onDoubleClick,
        contentModifier = contentModifier,
        containerColor = containerColor,
        contentColor = contentColor,
        enabled = enabled,
        shape = shape,
        textAlign = textAlign,
        fontSize = fontSize,
        fontWeight = fontWeight,
        border = border,
        contentPadding = contentPadding,
        interactionSource = interactionSource,
        fillWidth = fillWidth,
        badge = badge,
        hapticFeedbackEnabled = hapticFeedbackEnabled,
        leadingContent = {
            ButtonIcon(
                painter = leadingIcon,
                hasText = text != null,
                paddingEdgeIsStart = false,
            )
        },
        trailingContent = {
            ButtonIcon(
                painter = trailingIcon,
                hasText = text != null,
                paddingEdgeIsStart = true,
            )
        },
    )
}

@Composable
private fun ButtonIcon(
    painter: Painter?,
    hasText: Boolean,
    paddingEdgeIsStart: Boolean,
) {
    if (painter == null) return
    val padding = when {
        !hasText -> Modifier
        paddingEdgeIsStart -> Modifier.padding(start = 12.dp)
        else -> Modifier.padding(end = 12.dp)
    }
    Icon(
        painter = painter,
        contentDescription = null,
        modifier = padding,
    )
}

@Composable
public fun Button(
    text: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    description: String? = null,
    onClickLabel: String? = null,
    onLongClick: (() -> Unit)? = null,
    onLongClickLabel: String? = null,
    onDoubleClick: (() -> Unit)? = null,
    contentModifier: Modifier = Modifier,
    containerColor: Color = KepkoTheme.colors.foreground,
    contentColor: Color = contentColorFor(containerColor),
    enabled: Boolean = true,
    shape: Shape = ButtonDefaults.shape(),
    textAlign: TextAlign = TextAlign.Start,
    fontSize: TextUnit = 14.sp,
    fontWeight: FontWeight = FontWeight.Bold,
    border: BorderStroke? = borderStrokeFor(containerColor),
    contentPadding: PaddingValues = ButtonDefaults.contentPadding(),
    interactionSource: MutableInteractionSource? = null,
    fillWidth: Boolean = true,
    badge: Badge? = null,
    hapticFeedbackEnabled: Boolean = true,
    leadingContent: @Composable RowScope.() -> Unit = {},
    trailingContent: @Composable RowScope.() -> Unit = {},
) {
    ButtonInternal(
        text = text,
        description = description,
        modifier = modifier,
        onClick = onClick,
        onClickLabel = onClickLabel,
        onLongClick = onLongClick,
        onLongClickLabel = onLongClickLabel,
        onDoubleClick = onDoubleClick,
        contentModifier = contentModifier,
        containerColor = containerColor,
        contentColor = contentColor,
        enabled = enabled,
        shape = shape,
        textAlign = textAlign,
        fontSize = fontSize,
        fontWeight = fontWeight,
        border = border,
        contentPadding = contentPadding,
        interactionSource = interactionSource,
        fillWidth = fillWidth,
        badge = badge,
        leadingContent = leadingContent,
        trailingContent = trailingContent,
        hapticFeedbackEnabled = hapticFeedbackEnabled,
    )
}

@Suppress("LongParameterList")
@Composable
internal fun ButtonInternal(
    text: String?,
    description: String?,
    modifier: Modifier,
    onClick: () -> Unit,
    onClickLabel: String?,
    onLongClick: (() -> Unit)? = null,
    onLongClickLabel: String? = null,
    onDoubleClick: (() -> Unit)? = null,
    contentModifier: Modifier,
    containerColor: Color,
    contentColor: Color,
    enabled: Boolean,
    shape: Shape,
    textAlign: TextAlign,
    fontSize: TextUnit,
    fontWeight: FontWeight,
    border: BorderStroke?,
    contentPadding: PaddingValues,
    interactionSource: MutableInteractionSource?,
    fillWidth: Boolean,
    badge: Badge?,
    leadingContent: @Composable RowScope.() -> Unit,
    trailingContent: @Composable RowScope.() -> Unit,
    hapticFeedbackEnabled: Boolean,
) {
    val descriptionColor = if (enabled) {
        contentColor.copy(alpha = ButtonDefaults.DescriptionAlpha)
    } else {
        ButtonPrimitiveDefaults.disabledContentColor
    }

    ButtonPrimitive(
        onClick = onClick,
        onClickLabel = onClickLabel,
        onLongClick = onLongClick,
        onDoubleClick = onDoubleClick,
        onLongClickLabel = onLongClickLabel,
        content = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(
                        vertical = 8.dp,
                        horizontal = 4.dp,
                    )
                    .heightIn(min = KepkoTheme.dimensions.iconSize)
                    .then(contentModifier),
            ) {
                leadingContent()
                text?.let {
                    ButtonLabel(
                        text = it.uppercase(),
                        description = description,
                        textAlign = textAlign,
                        fontSize = fontSize,
                        fontWeight = fontWeight,
                        descriptionColor = descriptionColor,
                        modifier = if (fillWidth) Modifier.weight(1f) else Modifier,
                    )
                }
                badge?.let {
                    TextPill(
                        badge = it,
                        modifier = Modifier.padding(start = 12.dp),
                    )
                }
                trailingContent()
            }
        },
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor,
        enabled = enabled,
        shape = shape,
        border = border,
        contentPadding = contentPadding,
        interactionSource = interactionSource,
        hapticFeedbackEnabled = hapticFeedbackEnabled,
    )
}

@Composable
private fun ButtonLabel(
    text: String,
    description: String?,
    textAlign: TextAlign,
    fontSize: TextUnit,
    fontWeight: FontWeight,
    descriptionColor: Color,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = when (textAlign) {
            TextAlign.Center -> Alignment.CenterHorizontally
            TextAlign.End -> Alignment.End
            else -> Alignment.Start
        },
    ) {
        Text(
            text = text,
            textAlign = textAlign,
            fontSize = fontSize,
            fontWeight = fontWeight,
            maxLines = 1,
        )
        description?.let {
            Text(
                text = it,
                textAlign = textAlign,
                color = descriptionColor,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                fontWeight = FontWeight.Normal,
            )
        }
    }
}


@PreviewWithTest
@Composable
internal fun ButtonLightPreview() {
    KepkoTheme(palette = LIGHT) { PreviewContent() }
}

@PreviewWithTest
@Composable
internal fun ButtonDarkPreview() {
    KepkoTheme(palette = DARK) { PreviewContent() }
}

@PreviewWithTest
@Composable
internal fun ButtonBlackPreview() {
    KepkoTheme(palette = BLACK) { PreviewContent() }
}

@PreviewWithTest
@Composable
internal fun ButtonSolarizedLightPreview() {
    KepkoTheme(palette = SOLARIZED_LIGHT) { PreviewContent() }
}

@PreviewWithTest
@Composable
internal fun ButtonSolarizedDarkPreview() {
    KepkoTheme(palette = SOLARIZED_DARK) { PreviewContent() }
}

@PreviewWithTest
@Composable
internal fun ButtonIconOnlyLightPreview() {
    KepkoTheme(palette = LIGHT) { IconOnlyPreviewContent() }
}

@PreviewWithTest
@Composable
internal fun ButtonIconOnlyDarkPreview() {
    KepkoTheme(palette = DARK) { IconOnlyPreviewContent() }
}

@PreviewWithTest
@Composable
internal fun ButtonIconOnlyBlackPreview() {
    KepkoTheme(palette = BLACK) { IconOnlyPreviewContent() }
}

@PreviewWithTest
@Composable
internal fun ButtonIconOnlySolarizedLightPreview() {
    KepkoTheme(palette = SOLARIZED_LIGHT) { IconOnlyPreviewContent() }
}

@PreviewWithTest
@Composable
internal fun ButtonIconOnlySolarizedDarkPreview() {
    KepkoTheme(palette = SOLARIZED_DARK) { IconOnlyPreviewContent() }
}

@Composable
private fun IconOnlyPreviewContent() {
    val containerColors = KepkoTheme.colors.getSemanticColors() +
            KepkoTheme.colors.foreground +
            KepkoTheme.colors.content

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .background(KepkoTheme.colors.foreground)
            .padding(8.dp),
    ) {
        containerColors.forEach { containerColor ->
            Button(
                text = null,
                onClick = {},
                containerColor = containerColor,
                leadingIcon = painterResource(Res.drawable.ic_asterisk),
                trailingIcon = null,
                fillWidth = false,
            )
        }
    }
}

@Composable
private fun PreviewContent() {
    val containerColors = KepkoTheme.colors.getSemanticColors() +
            KepkoTheme.colors.foreground +
            KepkoTheme.colors.content

    Column(
        modifier = Modifier
            .background(KepkoTheme.colors.foreground)
            .padding(vertical = 4.dp),
    ) {
        containerColors.forEach { containerColor ->
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .padding(
                        vertical = 2.dp,
                        horizontal = 8.dp,
                    ),
            ) {
                Button(
                    text = "Button Text",
                    onClick = {},
                    containerColor = containerColor,
                    leadingIcon = painterResource(Res.drawable.ic_asterisk),
                    trailingIcon = painterResource(Res.drawable.ic_asterisk),
                    badge = Badge.preview,
                )
                Button(
                    text = "Button Text",
                    onClick = {},
                    containerColor = containerColor,
                )
                Button(
                    text = "Button Text",
                    onClick = {},
                    description = "Disabled",
                    containerColor = containerColor,
                    enabled = false,
                )
            }
        }
    }
}
