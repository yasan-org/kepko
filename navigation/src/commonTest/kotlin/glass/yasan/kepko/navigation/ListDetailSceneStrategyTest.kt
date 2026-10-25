package glass.yasan.kepko.navigation

import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategyScope
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

internal class ListDetailSceneStrategyTest {
    @Test
    fun givenTwoPanes_whenOnlyTheListIsOpen_thenTheListIsShownAlone() {
        // given
        val entries = entriesOf(Route.List)

        // when
        val scene = sceneFor(isLargeWindow = true, isTwoPane = true, entries = entries)

        // then
        assertEquals(listOf(entries[0]), scene?.entries)
    }

    @Test
    fun givenTwoPanes_whenADetailIsOpen_thenItGoesBackTogetherWithTheList() {
        // given
        val entries = entriesOf(Route.List, Route.Detail)

        // when
        val scene = sceneFor(isLargeWindow = true, isTwoPane = true, entries = entries)

        // then
        assertEquals(listOf(entries[0], entries[1]), scene?.entries)
        assertEquals(emptyList<NavEntry<Route>>(), scene?.previousEntries)
    }

    @Test
    fun givenTwoPanes_whenADetailOpensAnotherScreen_thenBackReturnsToTheDetail() {
        // given
        val entries = entriesOf(Route.List, Route.Detail, Route.NestedDetail)

        // when
        val scene = sceneFor(isLargeWindow = true, isTwoPane = true, entries = entries)

        // then
        assertEquals(listOf(entries[0], entries[2]), scene?.entries)
        assertEquals(listOf(entries[0], entries[1]), scene?.previousEntries)
    }

    @Test
    fun givenALargeWindowWithOnePane_whenADetailIsOpen_thenBackReturnsToTheList() {
        // given
        val entries = entriesOf(Route.List, Route.Detail)

        // when
        val scene = sceneFor(isLargeWindow = true, isTwoPane = false, entries = entries)

        // then
        assertEquals(listOf(entries[0]), scene?.previousEntries)
    }

    @Test
    fun givenASmallWindow_whenADetailIsOpen_thenNoListDetailSceneIsUsed() {
        // given
        val entries = entriesOf(Route.List, Route.Detail)

        // when
        val scene = sceneFor(isLargeWindow = false, isTwoPane = false, entries = entries)

        // then
        assertNull(scene)
    }

    @Test
    fun givenATwoPaneWindowThatIsNotLarge_whenADetailIsOpen_thenTheListDetailSceneIsUsed() {
        // given
        val entries = entriesOf(Route.List, Route.Detail)

        // when
        val scene = sceneFor(isLargeWindow = false, isTwoPane = true, entries = entries)

        // then
        assertNotNull(scene)
    }

    @Test
    fun givenTwoPanes_whenNoListIsBelow_thenNoListDetailSceneIsUsed() {
        // given
        val entries = entriesOf(Route.Detail)

        // when
        val scene = sceneFor(isLargeWindow = true, isTwoPane = true, entries = entries)

        // then
        assertNull(scene)
    }

    @Test
    fun givenTwoPanes_whenAnUnmarkedEntryIsAboveTheList_thenNoListDetailSceneIsUsed() {
        // given
        val entries = entriesOf(Route.List, Route.Unmarked)

        // when
        val scene = sceneFor(isLargeWindow = true, isTwoPane = true, entries = entries)

        // then
        assertNull(scene)
    }

    private fun sceneFor(
        isLargeWindow: Boolean,
        isTwoPane: Boolean,
        entries: List<NavEntry<Route>>,
    ): Scene<Route>? {
        val strategy = ListDetailSceneStrategy<Route>(
            isLargeWindow = isLargeWindow,
            isTwoPane = isTwoPane,
            paneState = ListDetailPaneState(listWidth = 360.dp),
        )
        return with(strategy) { SceneStrategyScope<Route>().calculateScene(entries) }
    }

    private fun entriesOf(vararg routes: Route): List<NavEntry<Route>> = routes.map { route ->
        val metadata = when (route) {
            Route.List -> ListDetailSceneStrategy.list()
            Route.Unmarked -> emptyMap()
            Route.Detail, Route.NestedDetail -> ListDetailSceneStrategy.detail()
        }
        NavEntry(route, metadata = metadata) {}
    }

    private enum class Route { List, Detail, NestedDetail, Unmarked }
}
