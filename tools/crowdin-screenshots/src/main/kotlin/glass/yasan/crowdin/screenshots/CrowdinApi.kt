package glass.yasan.crowdin.screenshots

internal class CrowdinException(message: String) : Exception(message)

internal interface CrowdinApi {
    fun sourceStrings(): Map<String, SourceString>
    fun screenshots(): List<Screenshot>
    fun createScreenshot(name: String, png: ByteArray): Screenshot
    fun replaceTags(screenshotId: Long, tags: List<Tag>)
    fun deleteScreenshot(screenshotId: Long)
}
