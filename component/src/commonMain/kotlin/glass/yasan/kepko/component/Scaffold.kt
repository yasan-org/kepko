package glass.yasan.kepko.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import glass.yasan.kepko.foundation.color.contentColorFor
import glass.yasan.kepko.foundation.theme.KepkoTheme
import glass.yasan.kepko.resource.Icons
import androidx.compose.material3.Scaffold as Material3Scaffold

@Composable
public fun Scaffold(
    title: String,
    modifier: Modifier = Modifier,
    leadingContent: @Composable (RowScope.() -> Unit)? = null,
    trailingContent: @Composable (RowScope.() -> Unit)? = null,
    bottomBar: @Composable RowScope.() -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    containerColor: Color = KepkoTheme.colors.midground,
    contentColor: Color = contentColorFor(containerColor),
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    badge: Badge? = null,
    subtitle: (@Composable () -> Subtitle)? = null,
    textAlign: TextAlign = TextAlign.Unspecified,
    reverse: Boolean = false,
    content: @Composable (contentPadding: PaddingValues) -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            Column {
                TitleBar(
                    title = title,
                    leadingContent = leadingContent,
                    trailingContent = trailingContent,
                    badge = badge,
                    textAlign = textAlign,
                    reverse = reverse,
                )
                subtitle?.let {
                    HorizontalDivider()
                    SubtitleBar(subtitle = it())
                }
            }
        },
        bottomBar = bottomBar,
        snackbarHost = snackbarHost,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        containerColor = containerColor,
        contentColor = contentColor,
        contentWindowInsets = contentWindowInsets,
        content = content,
    )
}

@Composable
public fun Scaffold(
    title: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    backIcon: Painter = Icons.chevronBackward,
    trailingContent: @Composable RowScope.() -> Unit = {},
    bottomBar: @Composable RowScope.() -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    containerColor: Color = KepkoTheme.colors.midground,
    contentColor: Color = contentColorFor(containerColor),
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    badge: Badge? = null,
    subtitle: (@Composable () -> Subtitle)? = null,
    textAlign: TextAlign = TextAlign.Unspecified,
    reverse: Boolean = false,
    content: @Composable (contentPadding: PaddingValues) -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            Column {
                TitleBar(
                    title = title,
                    onBackClick = onBackClick,
                    backIcon = backIcon,
                    trailingContent = trailingContent,
                    badge = badge,
                    textAlign = textAlign,
                    reverse = reverse,
                )
                subtitle?.let {
                    HorizontalDivider()
                    SubtitleBar(subtitle = it())
                }
            }
        },
        bottomBar = bottomBar,
        snackbarHost = snackbarHost,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        containerColor = containerColor,
        contentColor = contentColor,
        contentWindowInsets = contentWindowInsets,
        content = content,
    )
}

@Composable
public fun Scaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit,
    bottomBar: @Composable (RowScope.() -> Unit) = {},
    snackbarHost: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    containerColor: Color = KepkoTheme.colors.midground,
    contentColor: Color = contentColorFor(containerColor),
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    content: @Composable (contentPadding: PaddingValues) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val contentFocusRequester = remember { FocusRequester() }
    val maxWidth = LocalScaffoldContentMaxWidth.current

    Material3Scaffold(
        modifier = modifier,
        topBar = {
            Column(
                modifier = Modifier.downIntoContent(
                    contentFocusRequester = contentFocusRequester,
                    focusManager = focusManager,
                ),
            ) {
                topBar()
                HorizontalDivider()
            }
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .background(KepkoTheme.colors.midground)
                    .windowInsetsPadding(
                        insets = WindowInsets.safeDrawing
                            .only(WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal),
                    )
            ) {
                Row(
                    content = bottomBar,
                    modifier = Modifier
                        .background(KepkoTheme.colors.midground)
                        .fillMaxWidth()
                        .centeredMaxWidth(maxWidth),
                )
            }
        },
        snackbarHost = snackbarHost,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        containerColor = containerColor,
        contentColor = contentColor,
        contentWindowInsets = contentWindowInsets,
    ) { contentPadding ->
        ScaffoldContent(
            contentPadding = contentPadding,
            contentFocusRequester = contentFocusRequester,
            content = content,
        )
    }
}

@PreviewWithTest
@Composable
internal fun ScaffoldBasicPreview() {
    KepkoTheme {
        Scaffold(
            title = "Title",
        ) { paddingValues ->
            PreviewScaffoldContent(paddingValues)
        }
    }
}

@PreviewWithTest
@Composable
internal fun ScaffoldWithBackIconPreview() {
    KepkoTheme {
        Scaffold(
            title = "Title",
            onBackClick = {},
        ) { paddingValues ->
            PreviewScaffoldContent(paddingValues)
        }
    }
}

@PreviewWithTest
@Composable
internal fun ScaffoldCenteredPreview() {
    KepkoTheme {
        Scaffold(
            title = "Title",
            textAlign = TextAlign.Center,
        ) { paddingValues ->
            PreviewScaffoldContent(paddingValues)
        }
    }
}

@PreviewWithTest
@Composable
internal fun ScaffoldReversedPreview() {
    KepkoTheme {
        Scaffold(
            title = "Title",
            reverse = true,
        ) { paddingValues ->
            PreviewScaffoldContent(paddingValues)
        }
    }
}

@PreviewWithTest
@Composable
internal fun ScaffoldWithBackIconReversedPreview() {
    KepkoTheme {
        Scaffold(
            title = "Title",
            onBackClick = {},
            reverse = true,
        ) { paddingValues ->
            PreviewScaffoldContent(paddingValues)
        }
    }
}

@PreviewWithTest
@Composable
internal fun ScaffoldWithAnnotationPreview() {
    KepkoTheme {
        Scaffold(
            title = "Title",
            badge = Badge.new,
        ) { paddingValues ->
            PreviewScaffoldContent(paddingValues)
        }
    }
}

@PreviewWithTest
@Composable
internal fun ScaffoldWithTrailingContentPreview() {
    KepkoTheme {
        Scaffold(
            title = "Title",
            trailingContent = {
                Icon(
                    painter = Icons.chevronForward,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 16.dp),
                )
            },
        ) { paddingValues ->
            PreviewScaffoldContent(paddingValues)
        }
    }
}

@PreviewWithTest
@Composable
internal fun ScaffoldWithTrailingContentAndAnnotationPreview() {
    KepkoTheme {
        Scaffold(
            title = "Title",
            badge = Badge.experimental,
            trailingContent = {
                Icon(
                    painter = Icons.chevronForward,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 16.dp),
                )
            },
        ) { paddingValues ->
            PreviewScaffoldContent(paddingValues)
        }
    }
}

@PreviewWithTest
@Composable
internal fun ScaffoldWithBottomBarPreview() {
    KepkoTheme {
        Scaffold(
            title = "Title",
            bottomBar = {
                Text(
                    text = "Bottom Bar",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp),
                )
            },
        ) { paddingValues ->
            PreviewScaffoldContent(paddingValues)
        }
    }
}

@PreviewWithTest
@Composable
internal fun ScaffoldWithBackIconAndBottomBarPreview() {
    KepkoTheme {
        Scaffold(
            title = "Title",
            onBackClick = {},
            bottomBar = {
                Text(
                    text = "Bottom Bar",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp),
                )
            },
        ) { paddingValues ->
            PreviewScaffoldContent(paddingValues)
        }
    }
}

@PreviewWithTest
@Composable
internal fun ScaffoldWithCustomTopBarPreview() {
    KepkoTheme {
        Scaffold(
            topBar = {
                TitleBar {
                    Text(
                        text = "Top Bar",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(16.dp),
                    )
                }
            },
        ) { paddingValues ->
            PreviewScaffoldContent(paddingValues)
        }
    }
}

@PreviewWithTest
@Composable
internal fun ScaffoldWithCustomTopBarAndBottomBarPreview() {
    KepkoTheme {
        Scaffold(
            topBar = {
                TitleBar {
                    Text(
                        text = "Top Bar",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(16.dp),
                    )
                }
            },
            bottomBar = {
                Text(
                    text = "Bottom Bar",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp),
                )
            },
        ) { paddingValues ->
            PreviewScaffoldContent(paddingValues)
        }
    }
}

@PreviewWithTest
@Composable
internal fun ScaffoldWithSubtitlePreview() {
    KepkoTheme {
        Scaffold(
            title = "Title",
            subtitle = { Subtitle(text = "Work", icon = Icons.person) },
        ) { paddingValues ->
            PreviewScaffoldContent(paddingValues)
        }
    }
}

@PreviewWithTest
@Composable
internal fun ScaffoldWithBackIconAndSubtitlePreview() {
    KepkoTheme {
        Scaffold(
            title = "Title",
            onBackClick = {},
            subtitle = { Subtitle(text = "Work", icon = Icons.person) },
        ) { paddingValues ->
            PreviewScaffoldContent(paddingValues)
        }
    }
}

@Composable
private fun PreviewScaffoldContent(paddingValues: PaddingValues) {
    Text(
        text = "Content",
        modifier = Modifier
            .padding(paddingValues)
            .padding(16.dp),
    )
}
