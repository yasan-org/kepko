package glass.yasan.kepko.navigation

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class WindowSizeTest {
    @Test
    fun givenAPortraitPhone_whenChecked_thenItIsNotLarge() {
        // given
        val size = DpSize(width = 360.dp, height = 800.dp)

        // when
        val isLarge = isLargeWindow(size)

        // then
        assertFalse(isLarge)
    }

    @Test
    fun givenALandscapePhone_whenChecked_thenItIsNotLarge() {
        // given
        val size = DpSize(width = 800.dp, height = 400.dp)

        // when
        val isLarge = isLargeWindow(size)

        // then
        assertFalse(isLarge)
    }

    @Test
    fun givenAPortraitTablet_whenChecked_thenItIsLarge() {
        // given
        val size = DpSize(width = 600.dp, height = 960.dp)

        // when
        val isLarge = isLargeWindow(size)

        // then
        assertTrue(isLarge)
    }

    @Test
    fun givenAPortraitTablet_whenCheckedForTwoPanes_thenItIsNotWideEnough() {
        // given
        val size = DpSize(width = 800.dp, height = 1280.dp)

        // when
        val isTwoPane = isTwoPaneWindow(size)

        // then
        assertFalse(isTwoPane)
    }

    @Test
    fun givenALandscapeTablet_whenCheckedForTwoPanes_thenItFitsTwoPanes() {
        // given
        val size = DpSize(width = 1280.dp, height = 800.dp)

        // when
        val isTwoPane = isTwoPaneWindow(size)

        // then
        assertTrue(isTwoPane)
    }

    @Test
    fun givenAWideButShortWindow_whenCheckedForTwoPanes_thenItIsNotTallEnough() {
        // given
        val size = DpSize(width = 1000.dp, height = 400.dp)

        // when
        val isTwoPane = isTwoPaneWindow(size)

        // then
        assertFalse(isTwoPane)
    }

    @Test
    fun givenATelevision_whenCheckedForTwoPanes_thenItFitsTwoPanes() {
        // given
        val size = DpSize(width = 960.dp, height = 540.dp)

        // when
        val isTwoPane = isTwoPaneWindow(size)

        // then
        assertTrue(isTwoPane)
    }

    @Test
    fun givenATelevision_whenChecked_thenItIsNotLarge() {
        // given
        val size = DpSize(width = 960.dp, height = 540.dp)

        // when
        val isLarge = isLargeWindow(size)

        // then
        assertFalse(isLarge)
    }
}
