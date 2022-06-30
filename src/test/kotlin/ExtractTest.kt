import com.hoonsalim95.linkextractor.LinkExtractor
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertTrue

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

    @Test
    fun test_스킴이_올바르지_않을_경우_예외_발생(){
        //given
        val notAllowUrls = listOf(
            "m.android.com/tools/testing",
            "httpm.android.com/tools/testing",
            "httpsm.android.com/tools/testing",
            "http:m.android.com/tools/testing",
            "http:/m.android.com/tools/testing",
            "http:\\m.android.com/tools/testing",
            "://m.android.com/tools/testing",
            "m.://android.com/tools/testing",
        )

        notAllowUrls.forEachIndexed { index, url ->
            println("${index + 1}. $url")

            //when
            val exception = assertThrows<java.lang.IllegalArgumentException> {
                linkExtractor.extract(url)
            }

            //then
            assertTrue {
                exception.message?.contains(LinkExtractor.MSG_REQUIRED_SCHEME) ?: false ||
                exception.message?.contains(LinkExtractor.MSG_NOT_ALLOW_START_SIGN) ?: false ||
                exception.message?.contains(LinkExtractor.MSG_NOT_ALLOW_SCHEME) ?: false
            }
        }
    }
}