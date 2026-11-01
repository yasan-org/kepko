package glass.yasan.crowdin.screenshots

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal class MatchingTest {

    @Test
    fun `matches ignoring case and whitespace`() {
        val matches = textMatcher("Follow  System")!!
        assertTrue(matches(normalize("FOLLOW SYSTEM")))
        assertFalse(matches(normalize("Follow System Theme")))
    }

    @Test
    fun `placeholders match any text`() {
        val matches = textMatcher("Overridden by %1\$s")!!
        assertTrue(matches(normalize("OVERRIDDEN BY WORK")))
        assertFalse(matches(normalize("Overridden by")))
    }

    @Test
    fun `placeholder-only strings match nothing`() {
        assertNull(textMatcher("%1\$s"))
        assertNull(textMatcher("%1\$s %2\$d"))
    }

    @Test
    fun `resolves Android escapes and quotes`() {
        assertEquals("Don't\nstop…", unescape("Don\\'t\\nstop\\u2026"))
        assertEquals("  spaced  ", unescape("\"  spaced  \""))
        assertTrue(textMatcher("Line one\\nLine two")!!(normalize("Line one\nLine two")))
    }

    @Test
    fun `tags only read keys, with boxes or without a position`() {
        val strings = listOf(
            SourceString(1, "outline_title", "Outline"),
            SourceString(2, "color_outline", "Outline"),
            SourceString(3, "go_back", "Go back"),
        ).associateBy { it.identifier }
        val context = ScreenshotContext(
            keys = listOf("outline_title", "go_back", "missing"),
            texts = listOf(ScreenText("OUTLINE", 1, 2, 3, 4)),
        )

        val tags = tagsFor(context, strings) {}

        assertEquals(listOf(Tag(1, Position(1, 2, 3, 4)), Tag(3)), tags)
    }
}
