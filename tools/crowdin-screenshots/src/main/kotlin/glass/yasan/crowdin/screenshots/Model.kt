package glass.yasan.crowdin.screenshots

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

internal val json = Json {
    ignoreUnknownKeys = true
    explicitNulls = false
}

/** What a snapshot test wrote next to a PNG: the string keys it read and the texts on screen. */
@Serializable
internal data class ScreenshotContext(
    val keys: List<String>,
    val texts: List<ScreenText>,
)

@Serializable
internal data class ScreenText(
    val text: String,
    val x: Int,
    val y: Int,
    val width: Int,
    val height: Int,
) {
    val position: Position get() = Position(x = x, y = y, width = width, height = height)
}

@Serializable
internal data class Position(
    val x: Int,
    val y: Int,
    val width: Int,
    val height: Int,
)

/** A Crowdin screenshot tag; without a [position] the string is linked but not highlighted. */
@Serializable
internal data class Tag(
    val stringId: Long,
    val position: Position? = null,
)

@Serializable
internal data class Screenshot(
    val id: Long,
    val name: String,
    val tags: List<Tag>? = null,
)

internal data class SourceString(
    val id: Long,
    val identifier: String,
    val text: String,
)
