package glass.yasan.kepko.markdown

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import com.mikepenz.markdown.coil3.Coil3ImageTransformerImpl
import com.mikepenz.markdown.model.ImageData
import com.mikepenz.markdown.model.ImageTransformer
import com.mikepenz.markdown.utils.resolveImageAlt
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.ast.ASTNode

/**
 * The domains images may be loaded from. An image is loaded when its address is `https` and its
 * host is one of the domains or a subdomain of one.
 */
internal class ImageDomains(domains: List<String>) {

    private val domains: Set<String> = domains
        .map { it.trim().trim('.').lowercase() }
        .filter { it.isNotEmpty() }
        .toSet()

    fun allows(link: String): Boolean {
        if (domains.isEmpty() || !link.startsWith(prefix = HTTPS, ignoreCase = true)) return false
        val host = link
            .substring(HTTPS.length)
            .takeWhile { it !in AUTHORITY_END }
            // Anything before an `@` is a user name, which can be made to look like a host.
            .substringAfterLast('@')
            .substringBefore(':')
            .lowercase()
        return domains.any { domain -> host == domain || host.endsWith(".$domain") }
    }

    override fun equals(other: Any?): Boolean = other is ImageDomains && other.domains == domains

    override fun hashCode(): Int = domains.hashCode()

    private companion object {
        const val HTTPS = "https://"
        const val AUTHORITY_END = "/?#\\"
    }
}

/**
 * Loads the images [domains] allows, with Coil. Any other image is not loaded. Everything else,
 * such as an image's size, is Coil's.
 */
internal class DomainImageTransformer(
    private val domains: ImageDomains,
) : ImageTransformer by Coil3ImageTransformerImpl {

    @Composable
    override fun transform(link: String): ImageData? =
        if (domains.allows(link)) Coil3ImageTransformerImpl.transform(link) else null
}

/**
 * An image that is not loaded would leave its space empty: its description stands in for it.
 */
internal fun AnnotatedString.Builder.annotateImageAsDescription(
    content: String,
    child: ASTNode,
    domains: ImageDomains,
): Boolean {
    if (child.type != MarkdownElementTypes.IMAGE) return false
    val link = child.findLinkDestination()?.let { content.substring(it.startOffset, it.endOffset) }
    if (link != null && domains.allows(link)) return false
    append(child.resolveImageAlt(content).orEmpty())
    return true
}

private fun ASTNode.findLinkDestination(): ASTNode? =
    if (type == MarkdownElementTypes.LINK_DESTINATION) {
        this
    } else {
        children.firstNotNullOfOrNull { it.findLinkDestination() }
    }
