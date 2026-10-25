package glass.yasan.kepko.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavMetadataKey
import androidx.navigation3.runtime.get
import androidx.navigation3.runtime.metadata
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope
import glass.yasan.kepko.foundation.annotation.ExperimentalKepkoApi

/**
 * On large or two-pane windows, shows the [list] entry beside the [detail] entry above it, sliding the panes
 * as room changes.
 */
@ExperimentalKepkoApi
public class ListDetailSceneStrategy<T : Any> internal constructor(
    private val isLargeWindow: Boolean,
    private val isTwoPane: Boolean,
    private val paneState: ListDetailPaneState,
) : SceneStrategy<T> {
    public companion object {
        // Navigation 3 stores metadata under toString(), which is "[object Object]" for any plain object on JS.
        private object ListKey : NavMetadataKey<Boolean> {
            override fun toString(): String = "glass.yasan.kepko.navigation.ListDetailSceneStrategy.List"
        }

        private object DetailKey : NavMetadataKey<Boolean> {
            override fun toString(): String = "glass.yasan.kepko.navigation.ListDetailSceneStrategy.Detail"
        }

        public fun list(): Map<String, Any> = metadata { put(ListKey, true) }

        public fun detail(): Map<String, Any> = metadata { put(DetailKey, true) }
    }

    override fun SceneStrategyScope<T>.calculateScene(entries: List<NavEntry<T>>): Scene<T>? {
        if (!isLargeWindow && !isTwoPane) return null
        val listIndex = entries.indexOfLast { entry -> entry.metadata[ListKey] == true }
        if (listIndex == -1) return null
        val details = entries.drop(listIndex + 1)
        if (details.any { entry -> entry.metadata[DetailKey] != true }) return null
        val isDetailBesideList = isTwoPane && details.size == 1

        return ListDetailScene(
            listEntry = entries[listIndex],
            detailEntry = details.lastOrNull(),
            isTwoPane = isTwoPane,
            showsDetailBackButton = !isDetailBesideList,
            paneState = paneState,
            previousEntries = if (isDetailBesideList) entries.take(listIndex) else entries.dropLast(1),
        )
    }
}

@ExperimentalKepkoApi
@Composable
public fun <T : Any> rememberListDetailSceneStrategy(
    isLargeWindow: Boolean,
    isTwoPane: Boolean,
): ListDetailSceneStrategy<T> {
    val paneState = rememberListDetailPaneState()
    return remember(isLargeWindow, isTwoPane, paneState) {
        ListDetailSceneStrategy(isLargeWindow = isLargeWindow, isTwoPane = isTwoPane, paneState = paneState)
    }
}
