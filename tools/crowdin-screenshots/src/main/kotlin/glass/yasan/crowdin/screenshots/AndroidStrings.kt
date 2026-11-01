package glass.yasan.crowdin.screenshots

import org.w3c.dom.Element
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

/** Reads translatable `<string>` resources, for `--dry-run` without Crowdin. */
internal fun readAndroidStrings(files: List<File>): Map<String, SourceString> {
    val builder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
    var id = 0L
    return files.flatMap { file ->
        val elements = builder.parse(file).getElementsByTagName("string")
        (0 until elements.length)
            .map { elements.item(it) as Element }
            .filter { it.getAttribute("translatable") != "false" }
            .map { SourceString(id = ++id, identifier = it.getAttribute("name"), text = it.textContent) }
    }.associateBy { it.identifier }
}
