package glass.yasan.kepko.component

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
        private val previews by lazy {
            JvmAnnotationScanner("glass.yasan.kepko.component.PreviewWithTest")
                .scanPackageTrees("glass.yasan.kepko.component")
                .getPreviews()
        }
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun screenshotTests() {
        previews.forEach { preview ->
            runDesktopComposeUiTest {
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
