package glass.yasan.kepko.resource

import androidx.compose.ui.graphics.painter.Painter
import kotlin.reflect.full.memberProperties
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class NamedIconTest {

    @Test
    fun givenDeclaredEntryId_whenFromIdOrNull_thenReturnsMatchingEntry() {
        NamedIcon.entries.forEach { icon ->
            assertEquals(icon, NamedIcon.fromIdOrNull(icon.id))
        }
    }

    @Test
    fun givenUnknownId_whenFromIdOrNull_thenReturnsNull() {
        assertNull(NamedIcon.fromIdOrNull("unknown_icon_id"))
    }

    @Test
    fun givenIconsObject_whenComparedToNamedIconEntries_thenEveryPainterIsCovered() {
        val expectedProperties = Icons::class.memberProperties
            .filter { it.returnType.classifier == Painter::class }
            .map { it.name }
            .sorted()
        val actualProperties = NamedIcon.entries.map { it.id.toCamelCase() }.sorted()

        assertEquals(expectedProperties, actualProperties)
    }

    @Test
    fun givenNamedIconEntries_whenIdsCollected_thenAllAreUnique() {
        val ids = NamedIcon.entries.map { it.id }
        assertEquals(ids.size, ids.toSet().size)
    }

    // Same mapping the icon generator uses; snake_case to camelCase is lossless, unlike the reverse.
    private fun String.toCamelCase(): String =
        replace(Regex("_([a-z0-9])")) { it.groupValues[1].uppercase() }
}
