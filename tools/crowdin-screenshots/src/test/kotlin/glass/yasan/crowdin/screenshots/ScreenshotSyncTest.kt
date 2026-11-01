package glass.yasan.crowdin.screenshots

import java.io.File
import kotlin.io.path.createTempDirectory
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class ScreenshotSyncTest {

    private val directory = createTempDirectory().toFile()
    private val strings = mapOf("title" to SourceString(1, "title", "Title"))

    @AfterTest
    fun tearDown() {
        directory.deleteRecursively()
    }

    @Test
    fun `uploads new screenshots and deletes stale ones in its namespace only`() {
        snapshot("Screen", "png-1")
        val api = FakeCrowdinApi(
            Screenshot(900, "ns.Removed@0123456789ab.png", emptyList()),
            Screenshot(901, "manual.png", emptyList()),
            Screenshot(902, "other.Screen@0123456789ab.png", emptyList()),
        )

        assertTrue(sync(api))

        val names = api.screenshots.map { it.name }
        assertEquals(listOf("manual.png", "other.Screen@0123456789ab.png", "ns.Screen@${hashOf("png-1")}.png"), names)
        assertEquals(listOf(Tag(1, Position(0, 0, 10, 10))), api.screenshots.last().tags)
    }

    @Test
    fun `leaves unchanged screenshots alone`() {
        snapshot("Screen", "png-1")
        val api = FakeCrowdinApi()
        sync(api)
        api.calls.clear()

        assertTrue(sync(api))

        assertEquals(emptyList(), api.calls)
    }

    @Test
    fun `changed image replaces the old screenshot`() {
        snapshot("Screen", "png-1")
        val api = FakeCrowdinApi()
        sync(api)
        snapshot("Screen", "png-2")

        assertTrue(sync(api))

        assertEquals(listOf("ns.Screen@${hashOf("png-2")}.png"), api.screenshots.map { it.name })
    }

    @Test
    fun `failure keeps stale screenshots`() {
        snapshot("Screen", "png-1")
        val api = FakeCrowdinApi(Screenshot(900, "ns.Removed@0123456789ab.png", emptyList()), failTags = true)

        assertFalse(sync(api))

        assertTrue(api.screenshots.any { it.id == 900L })
    }

    @Test
    fun `missing directory fails without deleting anything`() {
        val api = FakeCrowdinApi(Screenshot(900, "ns.Removed@0123456789ab.png", emptyList()))

        assertFailsWith<CrowdinException> {
            syncScreenshots(listOf(File(directory, "missing")), strings, api, "ns", strict = false) {}
        }
        assertFailsWith<CrowdinException> { sync(api) }

        assertEquals(listOf(900L), api.screenshots.map { it.id })
    }

    @Test
    fun `strict mode fails on unknown keys`() {
        snapshot("Screen", "png-1", keys = """["title", "typo"]""")

        assertFalse(syncScreenshots(listOf(directory), strings, api = null, namespace = "", strict = true) {})
        assertTrue(syncScreenshots(listOf(directory), strings, api = null, namespace = "", strict = false) {})
    }

    private fun sync(api: CrowdinApi): Boolean =
        syncScreenshots(listOf(directory), strings, api, namespace = "ns", strict = false) {}

    private fun snapshot(name: String, png: String, keys: String = """["title"]""") {
        File(directory, "$name.png").writeText(png)
        File(directory, "$name.json").writeText(
            """{"keys": $keys, "texts": [{"text": "TITLE", "x": 0, "y": 0, "width": 10, "height": 10}]}""",
        )
    }

    private fun hashOf(png: String): String =
        java.security.MessageDigest.getInstance("SHA-256").digest(png.toByteArray())
            .joinToString("") { "%02x".format(it) }.take(12)
}

private class FakeCrowdinApi(vararg existing: Screenshot, private val failTags: Boolean = false) : CrowdinApi {

    val screenshots = existing.toMutableList()
    val calls = mutableListOf<String>()
    private var nextId = 1000L

    override fun sourceStrings(): Map<String, SourceString> = emptyMap()

    override fun screenshots(): List<Screenshot> = screenshots.toList()

    override fun createScreenshot(name: String, png: ByteArray): Screenshot {
        calls += "create $name"
        return Screenshot(nextId++, name, emptyList()).also { screenshots += it }
    }

    override fun replaceTags(screenshotId: Long, tags: List<Tag>) {
        calls += "tags $screenshotId"
        if (failTags) throw CrowdinException("tags failed")
        screenshots.replaceAll { if (it.id == screenshotId) it.copy(tags = tags) else it }
    }

    override fun deleteScreenshot(screenshotId: Long) {
        calls += "delete $screenshotId"
        screenshots.removeAll { it.id == screenshotId }
    }
}
