import com.hoonsalim95.linkextractor.LinkExtractor
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ExtractTest {
    private val linkExtractor = LinkExtractor()

    @Test
    fun test_url에서_스킴과_도메인_및_서브url을_추출한다() {
        //given
        val s = "https"
        val d = "m.android.com"
        val su = "/tools/testing"
        val url = "$s://$d$su"

        //when
        val link = linkExtractor.extract(url)

        //then
        assertEquals(s, link.scheme)
        assertEquals(d, link.domain)
        assertEquals(su, link.subUrl)
    }
}