package glass.yasan.kepko.markdown

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class ImageDomainsTest {

    private val domains = ImageDomains(listOf("example.com", " Images.Example.org. "))

    @Test
    fun givenAllowedDomain_whenAllows_thenTrue() {
        assertTrue(domains.allows("https://example.com/picture.png"))
        assertTrue(domains.allows("HTTPS://EXAMPLE.COM/picture.png"))
        assertTrue(domains.allows("https://example.com:8443/picture.png"))
        assertTrue(domains.allows("https://example.com?size=2"))
        assertTrue(domains.allows("https://images.example.org/picture.png"))
    }

    @Test
    fun givenSubdomainOfAllowedDomain_whenAllows_thenTrue() {
        assertTrue(domains.allows("https://cdn.example.com/picture.png"))
        assertTrue(domains.allows("https://a.b.example.com/picture.png"))
    }

    @Test
    fun givenOtherDomain_whenAllows_thenFalse() {
        assertFalse(domains.allows("https://example.org/picture.png"))
        assertFalse(domains.allows("https://notexample.com/picture.png"))
        assertFalse(domains.allows("https://example.com.evil.test/picture.png"))
    }

    @Test
    fun givenAllowedDomainAsUserName_whenAllows_thenFalse() {
        assertFalse(domains.allows("https://example.com@evil.test/picture.png"))
        assertFalse(domains.allows("https://example.com:pass@evil.test/picture.png"))
        assertFalse(domains.allows("https://evil.test\\@example.com/picture.png"))
    }

    @Test
    fun givenOtherScheme_whenAllows_thenFalse() {
        assertFalse(domains.allows("http://example.com/picture.png"))
        assertFalse(domains.allows("//example.com/picture.png"))
        assertFalse(domains.allows("picture.png"))
        assertFalse(domains.allows("data:image/png;base64,AAAA"))
        assertFalse(domains.allows("file:///example.com/picture.png"))
    }

    @Test
    fun givenNoDomains_whenAllows_thenFalse() {
        assertFalse(ImageDomains(emptyList()).allows("https://example.com/picture.png"))
        assertFalse(ImageDomains(listOf(" ", ".")).allows("https://example.com/picture.png"))
    }
}
