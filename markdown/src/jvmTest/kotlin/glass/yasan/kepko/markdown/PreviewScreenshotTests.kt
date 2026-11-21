package glass.yasan.kepko.markdown

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.v2.runDesktopComposeUiTest
import androidx.compose.ui.unit.Density
import coil3.ColorImage
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.annotation.DelicateCoilApi
import coil3.annotation.ExperimentalCoilApi
import coil3.test.FakeImageLoaderEngine
import io.github.takahirom.roborazzi.captureRoboImage
import sergio.sastre.composable.preview.scanner.jvm.JvmAnnotationScanner
import kotlin.test.Test

/**
 * Automatically discovers and screenshots all [PreviewWithTest] annotated previews.
 */
internal class PreviewScreenshotTests {

    companion object {
        // A group of samples is one column, which can be taller than the default window. It is
        // drawn at twice the size, so that the text is large enough to review.
        private const val WINDOW_WIDTH = 800
        private const val WINDOW_HEIGHT = 2400
        private val snapshotDensity = Density(density = 2f)

        // No image is fetched in a snapshot: a grey rectangle stands in for every image loaded.
        private const val IMAGE_COLOR = 0xFF9E9E9E.toInt()
        private const val IMAGE_WIDTH = 240
        private const val IMAGE_HEIGHT = 120

        private val previews by lazy {
            JvmAnnotationScanner("glass.yasan.kepko.markdown.PreviewWithTest")
                .scanPackageTrees("glass.yasan.kepko.markdown")
                .getPreviews()
        }
    }

    @OptIn(ExperimentalTestApi::class, ExperimentalCoilApi::class, DelicateCoilApi::class)
    @Test
    fun screenshotTests() {
        val engine = FakeImageLoaderEngine.Builder()
            .default(ColorImage(color = IMAGE_COLOR, width = IMAGE_WIDTH, height = IMAGE_HEIGHT))
            .build()
        SingletonImageLoader.setUnsafe(
            ImageLoader.Builder(PlatformContext.INSTANCE)
                .components { add(engine) }
                .build(),
        )

        previews.forEach { preview ->
            runDesktopComposeUiTest(width = WINDOW_WIDTH, height = WINDOW_HEIGHT) {
                setContent {
                    CompositionLocalProvider(LocalDensity provides snapshotDensity) {
                        preview()
                    }
                }
                waitForIdle()
                onRoot().captureRoboImage(
                    filePath = "src/jvmTest/snapshots/${preview.methodName}.png",
                )
            }
        }
    }
}
