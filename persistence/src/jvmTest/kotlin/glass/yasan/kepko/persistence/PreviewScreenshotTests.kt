package glass.yasan.kepko.persistence

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.v2.runDesktopComposeUiTest
import io.github.takahirom.roborazzi.captureRoboImage
import sergio.sastre.composable.preview.scanner.core.preview.ComposablePreview
import sergio.sastre.composable.preview.scanner.jvm.JvmAnnotationScanner
import kotlin.test.Test

internal class PreviewScreenshotTests {

    companion object {
        private val previews by lazy {
            JvmAnnotationScanner("glass.yasan.kepko.persistence.PreviewWithTest")
                .scanPackageTrees("glass.yasan.kepko.persistence")
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
                captureCrowdinContext(
                    filePath = "src/jvmTest/snapshots/${preview.methodName}.json",
                    keys = preview.crowdinStrings(),
                )
            }
        }
    }
}

/** The keys of the preview's [CrowdinStrings] annotation, or `null` if it has none. */
private fun ComposablePreview<*>.crowdinStrings(): List<String>? =
    Class.forName(declaringClass).declaredMethods
        .first { it.name == methodName }
        .getAnnotation(CrowdinStrings::class.java)
        ?.keys
        ?.toList()
