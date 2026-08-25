package glass.yasan.kepko.resource

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.currentComposer
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.v2.runDesktopComposeUiTest
import androidx.compose.ui.unit.dp
import io.github.takahirom.roborazzi.captureRoboImage
import kotlin.reflect.full.memberProperties
import kotlin.test.Test

internal class IconsSnapshotTests {

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun screenshotTests() {
        Icons::class.memberProperties.sortedBy { it.name }.forEach { property ->
            val getterName = "get${property.name.replaceFirstChar { it.uppercase() }}"
            val method = Icons::class.java.methods.first { it.name == getterName }
            runDesktopComposeUiTest {
                setContent {
                    val painter = method.invoke(Icons, currentComposer, 0) as Painter
                    Image(
                        painter = painter,
                        contentDescription = property.name,
                        modifier = Modifier.size(24.dp),
                    )
                }
                onRoot().captureRoboImage(
                    filePath = "src/jvmTest/snapshots/icon_${property.name}.png",
                )
            }
        }
    }
}
