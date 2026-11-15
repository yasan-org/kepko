package glass.yasan.kepko.markdown

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.v2.runDesktopComposeUiTest
import io.github.takahirom.roborazzi.captureRoboImage
import sergio.sastre.composable.preview.scanner.jvm.JvmAnnotationScanner
import kotlin.test.Test

/**
 * Automatically discovers and screenshots all [PreviewWithTest] annotated previews.
 */
internal class PreviewScreenshotTests {

    companion object {
        // A group of samples is four columns wide, which is wider than the default window.
        private const val WINDOW_WIDTH = 1400
        private const val WINDOW_HEIGHT = 2000

        private val previews by lazy {
            JvmAnnotationScanner("glass.yasan.kepko.markdown.PreviewWithTest")
                .scanPackageTrees("glass.yasan.kepko.markdown")
                .getPreviews()
        }
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun screenshotTests() {
        previews.forEach { preview ->
            runDesktopComposeUiTest(width = WINDOW_WIDTH, height = WINDOW_HEIGHT) {
                setContent {
                    preview()
                }
                onRoot().captureRoboImage(
                    filePath = "src/jvmTest/snapshots/${preview.methodName}.png",
                )
            }
        }
    }
}
