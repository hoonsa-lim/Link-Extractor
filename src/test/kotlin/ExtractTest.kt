import com.hoonsalim95.linkextractor.LinkExtractor
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ExtractTest {
    private val linkExtractor = LinkExtractor()

    @Test
    fun extract_scheme() {
        val url = "http://d.android.com/tools/testing"

        assertEquals("http", linkExtractor.extract(url).scheme)
    }

    @Test
    fun extract_domain() {
        val url = "http://d.android.com/tools/testing"

        assertEquals("d.android.com", linkExtractor.extract(url).domain)
    }

    @Test
    fun extract_subUrl() {
        val url = "http://d.android.com/tools/testing"

        assertEquals("/tools/testing", linkExtractor.extract(url).subUrl)
    }
}