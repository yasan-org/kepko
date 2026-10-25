package glass.yasan.kepko.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import glass.yasan.kepko.foundation.theme.KepkoTheme
import glass.yasan.kepko.resource.Icons
import glass.yasan.kepko.resource.Strings

@Composable
public fun TitleBar(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    TitleBarContent(
        modifier = modifier,
        windowInsets = WindowInsets.safeDrawing
            .only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal),
        containerColor = KepkoTheme.colors.foreground,
        content = content,
    )
}

@Composable
internal fun TitleBarContent(
    modifier: Modifier = Modifier,
    windowInsets: WindowInsets?,
    containerColor: Color,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        content = content,
        // The background goes first so it also fills the system insets, such as a TV's overscan margins.
        modifier = modifier
            .fillMaxWidth()
            .background(containerColor)
            .let {
                if (windowInsets == null) {
                    it
                } else {
                    it.windowInsetsPadding(windowInsets)
                }
            },
    )
}

@Composable
public fun TitleBar(
    title: String,
    modifier: Modifier = Modifier,
    leadingContent: @Composable (RowScope.() -> Unit)? = null,
    trailingContent: @Composable (RowScope.() -> Unit)? = null,
    badge: Badge? = null,
    textAlign: TextAlign = TextAlign.Unspecified,
    reverse: Boolean = false,
) {
    val effectiveLeading = if (reverse) trailingContent else leadingContent
    val effectiveTrailing = if (reverse) leadingContent else trailingContent

    TitleBar(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            effectiveLeading?.let { it() }
            if (reverse) {
                badge?.let {
                    TextPill(
                        badge = it,
                        modifier = Modifier.padding(horizontal = 12.dp),
                    )
                }
            }
            Text(
                text = title.uppercase(),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                maxLines = 1,
                textAlign = when {
                    textAlign == TextAlign.Center -> TextAlign.Center
                    reverse -> TextAlign.End
                    else -> TextAlign.Start
                },
                modifier = Modifier
                    .let { if (effectiveLeading == null) it.padding(start = 16.dp) else it }
                    .padding(vertical = 16.dp)
                    .let { if (effectiveTrailing == null) it.padding(end = 16.dp) else it }
                    .weight(1f)
            )
            if (!reverse) {
                badge?.let {
                    TextPill(
                        badge = it,
                        modifier = Modifier.padding(horizontal = 12.dp),
                    )
                }
            }
            effectiveTrailing?.let { it() }
        }
    }
}

@Composable
public fun TitleBar(
    title: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    backIcon: Painter = Icons.chevronBackward,
    trailingContent: @Composable RowScope.() -> Unit = {},
    badge: Badge? = null,
    textAlign: TextAlign = TextAlign.Unspecified,
    reverse: Boolean = false,
) {
    val backButton: @Composable RowScope.() -> Unit = {
        IconButton(
            painter = backIcon,
            contentDescription = Strings.back,
            onClick = onBackClick,
            onClickLabel = Strings.goBack,
            modifier = Modifier
                .testTag(TitleBarSemantics.BACK_BUTTON)
                .let { if (reverse) it.graphicsLayer(scaleX = -1f) else it },
        )
    }

    TitleBar(
        title = title,
        modifier = modifier,
        leadingContent = if (LocalTitleBarBackButtonVisible.current) backButton else null,
        trailingContent = trailingContent,
        badge = badge,
        textAlign = textAlign,
        reverse = reverse,
    )
}

@PreviewWithTest
@Composable
internal fun TitleBarBasicPreview() {
    KepkoTheme {
        TitleBar(title = "Title")
    }
}

@PreviewWithTest
@Composable
internal fun TitleBarWithBackIconPreview() {
    KepkoTheme {
        TitleBar(
            title = "Title",
            onBackClick = {},
        )
    }
}

@PreviewWithTest
@Composable
internal fun TitleBarCenteredPreview() {
    KepkoTheme {
        TitleBar(
            title = "Title",
            textAlign = TextAlign.Center,
        )
    }
}

@PreviewWithTest
@Composable
internal fun TitleBarReversedPreview() {
    KepkoTheme {
        TitleBar(
            title = "Title",
            reverse = true,
        )
    }
}

@PreviewWithTest
@Composable
internal fun TitleBarWithBackIconReversedPreview() {
    KepkoTheme {
        TitleBar(
            title = "Title",
            onBackClick = {},
            reverse = true,
        )
    }
}

@PreviewWithTest
@Composable
internal fun TitleBarWithBadgePreview() {
    KepkoTheme {
        TitleBar(
            title = "Title",
            badge = Badge.new,
        )
    }
}

@PreviewWithTest
@Composable
internal fun TitleBarWithTrailingContentPreview() {
    KepkoTheme {
        TitleBar(
            title = "Title",
            trailingContent = {
                Icon(
                    painter = Icons.chevronForward,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 16.dp),
                )
            },
        )
    }
}

@PreviewWithTest
@Composable
internal fun TitleBarWithTrailingContentAndBadgePreview() {
    KepkoTheme {
        TitleBar(
            title = "Title",
            badge = Badge.experimental,
            trailingContent = {
                Icon(
                    painter = Icons.chevronForward,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 16.dp),
                )
            },
        )
    }
}

@PreviewWithTest
@Composable
internal fun TitleBarSlotPreview() {
    KepkoTheme {
        TitleBar {
            Text(
                text = "Title Bar",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}

/**
 * Test tags for [TitleBar].
 */
public object TitleBarSemantics {
    public const val BACK_BUTTON: String = "title_bar_back_button"
}
