package glass.yasan.kepko.persistence

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.onRoot
import com.github.takahirom.roborazzi.ExperimentalRoborazziApi
import com.github.takahirom.roborazzi.roborazziSystemPropertyTaskType
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File
import kotlin.math.roundToInt

@Serializable
private data class CrowdinScreenshotContext(
    val keys: List<String>,
    val texts: List<CrowdinScreenshotText>,
)

@Serializable
private data class CrowdinScreenshotText(
    val text: String,
    val x: Int,
    val y: Int,
    val width: Int,
    val height: Int,
)

private val json = Json { prettyPrint = true }

/**
 * Keeps the Crowdin context at [filePath] in sync with the preview: the [CrowdinStrings] [keys] it
 * shows, and the text and content descriptions on screen with their pixel bounds, which the
 * `tools/crowdin-screenshots` uploader uses to highlight each string.
 *
 * Recording writes the file, or deletes it for a preview with no [keys]. Verifying fails when the
 * committed file is stale, like a snapshot that no longer matches, so CI re-records it.
 */
@OptIn(ExperimentalRoborazziApi::class)
internal fun SemanticsNodeInteractionsProvider.captureCrowdinContext(
    filePath: String,
    keys: List<String>?,
) {
    val taskType = roborazziSystemPropertyTaskType()
    if (!taskType.isRecording() && !taskType.isVerifying()) return

    val file = File(filePath)
    val expected = keys?.let { json.encodeToString(CrowdinScreenshotContext(it.sorted(), screenTexts())) + "\n" }
    when {
        taskType.isRecording() -> if (expected == null) file.delete() else file.writeText(expected)
        expected != file.takeIf { it.exists() }?.readText() -> throw AssertionError(
            "$filePath is out of date with the preview's @CrowdinStrings or texts; record snapshots again.",
        )
    }
}

private fun SemanticsNodeInteractionsProvider.screenTexts(): List<CrowdinScreenshotText> {
    val root = onRoot().fetchSemanticsNode().boundsInRoot
    val textMatcher = SemanticsMatcher.keyIsDefined(SemanticsProperties.Text) or
        SemanticsMatcher.keyIsDefined(SemanticsProperties.ContentDescription)

    return onAllNodes(textMatcher, useUnmergedTree = true)
        .fetchSemanticsNodes()
        .flatMap { node ->
            val bounds = node.boundsInRoot.intersect(root)
            if (bounds.isEmpty) return@flatMap emptyList()
            val texts = node.config.getOrNull(SemanticsProperties.Text).orEmpty().map { it.text } +
                node.config.getOrNull(SemanticsProperties.ContentDescription).orEmpty()
            texts.filter { it.isNotBlank() }.map { text ->
                CrowdinScreenshotText(
                    text = text,
                    x = bounds.left.roundToInt(),
                    y = bounds.top.roundToInt(),
                    width = bounds.width.roundToInt(),
                    height = bounds.height.roundToInt(),
                )
            }
        }
        .distinct()
}
