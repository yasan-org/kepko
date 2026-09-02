package glass.yasan.kepko.foundation.color

import androidx.compose.ui.graphics.Color

public enum class NamedColor(
    public val id: String,
    private val resolve: Colors.() -> Color,
) {
    SUCCESS(
        id = "success",
        resolve = { success }
    ),
    ON_SUCCESS(
        id = "on_success",
        resolve = { onSuccess }
    ),
    INFORMATION(
        id = "information",
        resolve = { information }
    ),
    ON_INFORMATION(
        id = "on_information",
        resolve = { onInformation }
    ),
    CAUTION(
        id = "caution",
        resolve = { caution }
    ),
    ON_CAUTION(
        id = "on_caution",
        resolve = { onCaution }
    ),
    DANGER(
        id = "danger",
        resolve = { danger }
    ),
    ON_DANGER(
        id = "on_danger",
        resolve = { onDanger }
    ),
    CONTENT(
        id = "content",
        resolve = { content }
    ),
    CONTENT_SUBTLE(
        id = "content_subtle",
        resolve = { contentSubtle }
    ),
    CONTENT_DISABLED(
        id = "content_disabled",
        resolve = { contentDisabled }
    ),
    INVERSE_CONTENT(
        id = "inverse_content",
        resolve = { inverseContent }
    ),
    INVERSE_CONTENT_SUBTLE(
        id = "inverse_content_subtle",
        resolve = { inverseContentSubtle }
    ),
    INVERSE_CONTENT_DISABLED(
        id = "inverse_content_disabled",
        resolve = { inverseContentDisabled }
    ),
    FOREGROUND(
        id = "foreground",
        resolve = { foreground }
    ),
    MIDGROUND(
        id = "midground",
        resolve = { midground }
    ),
    BACKGROUND(
        id = "background",
        resolve = { background }
    ),
    OUTLINE(
        id = "outline",
        resolve = { outline }
    ),
    SCRIM(
        id = "scrim",
        resolve = { scrim }
    ),
    INVERSE_FOREGROUND(
        id = "inverse_foreground",
        resolve = { inverseForeground }
    ),
    INVERSE_MIDGROUND(
        id = "inverse_midground",
        resolve = { inverseMidground }
    ),
    INVERSE_BACKGROUND(
        id = "inverse_background",
        resolve = { inverseBackground }
    ),
    ;

    public fun resolve(colors: Colors): Color = colors.resolve()

    public companion object {
        public fun fromIdOrNull(id: String): NamedColor? = entries.firstOrNull { it.id == id }
    }
}
