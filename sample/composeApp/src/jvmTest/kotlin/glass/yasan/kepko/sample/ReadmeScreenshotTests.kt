package glass.yasan.kepko.sample

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.v2.runSkikoComposeUiTest
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.takahirom.roborazzi.captureRoboImage
import sergio.sastre.composable.preview.scanner.jvm.JvmAnnotationScanner
import kotlin.test.Test

internal class ReadmeScreenshotTests {

    companion object {
        val previewWidth: Dp = 400.dp
        val previewHeight: Dp = 800.dp
        private const val DENSITY = 1.5f
        const val LOREM_IPSUM = "Lorem ipsum dolor sit amet, consectetur adipiscing elit."

        private val previews by lazy {
            JvmAnnotationScanner("glass.yasan.kepko.sample.readme.ReadmePreview")
                .scanPackageTrees("glass.yasan.kepko.sample.readme")
                .getPreviews()
        }
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun readmeScreenshots() {
        previews.forEach { preview ->
            runSkikoComposeUiTest(
                size = Size(
                    width = previewWidth.value * DENSITY,
                    height = previewHeight.value * DENSITY,
                ),
                density = Density(DENSITY),
            ) {
                setContent {
                    preview()
                }
                onRoot().captureRoboImage(
                    filePath = "assets/readme/${preview.methodName}.png",
                )
            }
        }
    }

}
