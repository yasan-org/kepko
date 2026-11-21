package glass.yasan.kepko.buildlogic.icons

import com.android.ide.common.vectordrawable.Svg2Vector
import java.io.ByteArrayOutputStream
import java.io.File

/** Converts SVGs to Android vector drawables with Android Studio's own converter. */
internal object VectorDrawableConverter {

    fun convert(svg: String, workDirectory: File, name: String): String {
        workDirectory.mkdirs()
        val input = workDirectory.resolve("$name.svg").apply { writeText(svg) }
        val output = ByteArrayOutputStream()
        val error = Svg2Vector.parseSvgToXml(input.toPath(), output)
        check(error.isEmpty()) { "SVG conversion failed: $error" }
        check(output.size() > 0) { "SVG conversion produced no output" }
        return output.toString(Charsets.UTF_8).normalizeLineEndings()
    }
}

internal fun String.normalizeLineEndings(): String = replace("\r\n", "\n")
