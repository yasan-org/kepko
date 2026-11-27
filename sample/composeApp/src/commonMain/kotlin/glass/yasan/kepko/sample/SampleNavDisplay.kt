package glass.yasan.kepko.sample

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.scene.Scene
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.defaultPopTransitionSpec
import androidx.navigation3.ui.defaultTransitionSpec
import glass.yasan.kepko.component.ProvideScaffoldContentMaxWidth
import glass.yasan.kepko.foundation.annotation.ExperimentalKepkoApi
import glass.yasan.kepko.navigation.ListDetailSceneStrategy
import glass.yasan.kepko.navigation.isLargeWindow
import glass.yasan.kepko.navigation.isLayoutChange
import glass.yasan.kepko.navigation.isTwoPaneWindow
import glass.yasan.kepko.navigation.layoutChangeTransform
import glass.yasan.kepko.navigation.rememberListDetailSceneStrategy
import glass.yasan.kepko.persistence.PersistentPreferenceThemeScreen
import glass.yasan.kepko.persistence.UserVisibleProfile
import glass.yasan.kepko.sample.home.markdown.MarkdownScreen
import glass.yasan.kepko.sample.home.serialization.SerializationScreen

@OptIn(ExperimentalKepkoApi::class)
@Composable
internal fun SampleNavDisplay(
    backStack: SnapshotStateList<Route>,
    activeProfileId: String,
    onProfileSelect: (String) -> Unit,
) {
    val isLargeWindow = isLargeWindow()
    val isTwoPane = isTwoPaneWindow()
    val listDetailSceneStrategy = rememberListDetailSceneStrategy<Route>(isLargeWindow, isTwoPane)
    val selectedRoute = if (isTwoPane) backStack.getOrNull(1) else null

    OpenFirstScreenEffect(backStack = backStack, isTwoPane = isTwoPane)

    // Always provided: changing it while a resize moves screens between scenes breaks their insets padding.
    ProvideScaffoldContentMaxWidth(maxWidth = SampleNavDisplayDefaults.ContentMaxWidth) {
        NavDisplay(
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            sceneStrategies = listOf(listDetailSceneStrategy),
            entryDecorators = listOf(rememberSaveableStateHolderNavEntryDecorator()),
            transitionSpec = { sampleTransform(isPop = false) },
            popTransitionSpec = { sampleTransform(isPop = true) },
            entryProvider = { route ->
                sampleEntry(
                    route = route,
                    backStack = backStack,
                    selectedRoute = selectedRoute,
                    activeProfileId = activeProfileId,
                    onProfileSelect = onProfileSelect,
                )
            },
        )
    }
}

/**
 * Opens the first screen beside Home, so the second pane is never empty.
 */
@Composable
private fun OpenFirstScreenEffect(
    backStack: SnapshotStateList<Route>,
    isTwoPane: Boolean,
) {
    val isHomeOnTop = backStack.lastOrNull() == Route.Home
    DisposableEffect(isTwoPane, isHomeOnTop) {
        if (isTwoPane && isHomeOnTop) backStack.add(Route.Theme)
        onDispose {}
    }
}

private fun AnimatedContentTransitionScope<Scene<Route>>.sampleTransform(isPop: Boolean): ContentTransform = when {
    isLayoutChange() -> layoutChangeTransform()
    isPop -> defaultPopTransitionSpec<Route>()()
    else -> defaultTransitionSpec<Route>()()
}

private fun sampleEntry(
    route: Route,
    backStack: SnapshotStateList<Route>,
    selectedRoute: Route?,
    activeProfileId: String,
    onProfileSelect: (String) -> Unit,
): NavEntry<Route> {
    val onBackClick: () -> Unit = { backStack.removeLastOrNull() }
    val metadata = if (route == Route.Home) ListDetailSceneStrategy.list() else ListDetailSceneStrategy.detail()

    return NavEntry(key = route, metadata = metadata) {
        when (route) {
            Route.Home -> HomeScreen(
                onNavigate = { destination -> backStack.openFromHome(destination) },
                selectedRoute = selectedRoute,
            )

            Route.Theme -> PersistentPreferenceThemeScreen(
                onBackClick = onBackClick,
                activeProfile = getSampleProfileById(activeProfileId),
            )

            Route.Profiles -> ProfilesScreen(
                activeProfileId = activeProfileId,
                onProfileSelect = onProfileSelect,
                onProfileThemeClick = { profile -> backStack.add(Route.ProfileTheme(profile.id)) },
                onBackClick = onBackClick,
            )

            is Route.ProfileTheme -> PersistentPreferenceThemeScreen(
                onBackClick = onBackClick,
                activeProfile = getSampleProfileById(activeProfileId),
                targetProfile = getSampleProfileById(route.profileId),
            )

            Route.Icons -> IconScreen(onBackClick = onBackClick)

            Route.Markdown -> MarkdownScreen(onBackClick = onBackClick)

            Route.Serialization -> SerializationScreen(onBackClick = onBackClick)

            Route.TitleBar -> TitleBarScreen(onBackClick = onBackClick)
        }
    }
}

/**
 * Opens [route] from Home, replacing whichever screen was open beside it.
 */
private fun SnapshotStateList<Route>.openFromHome(route: Route) {
    while (size > 1) removeAt(lastIndex)
    add(route)
}

private fun getSampleProfileById(id: String?): UserVisibleProfile =
    sampleProfiles.firstOrNull { it.id == id } ?: sampleProfiles.first()
