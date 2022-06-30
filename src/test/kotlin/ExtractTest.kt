import com.hoonsalim95.linkextractor.LinkExtractor
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
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
                linkExtractor.extractRegex(url)
            }

            //then
//            assertTrue {
//                exception.message?.contains(LinkExtractor.MSG_REQUIRED_SCHEME) ?: false ||
//                exception.message?.contains(LinkExtractor.MSG_NOT_ALLOW_START_SIGN) ?: false ||
//                exception.message?.contains(LinkExtractor.MSG_NOT_ALLOW_SCHEME) ?: false
//            }
            assertTrue {
                exception.message?.contains(LinkExtractor.MSG_NOT_ALLOW_URL) ?: false
            }
        }
    }

    @Test
    fun test_도메인이_올바르지_않을_경우_예외_발생(){
        //given
        val notAllowUrls = listOf(
            "http://com",   //x
            "http://com..",   //x
            "http://.com.",   //x
            "http://asd.com.",   //x
            "http://.com.aasd.asd.",   //x
            "http://com,aasd,asd,",   //x
        )

        notAllowUrls.forEachIndexed { index, url ->
            println("${index + 1}. $url")

            //when
            val exception = assertThrows<java.lang.IllegalArgumentException> {
                linkExtractor.extractRegex(url)
            }

            //then
            assertTrue {
                exception.message?.contains(LinkExtractor.MSG_NOT_ALLOW_URL) ?: false
            }
        }
    }

    @Test
    fun test_정상URL_추출_시_예외_미발생(){
        //given
        val allowUrl = listOf(
            "https://mungi.kr/375",
            "https://yangbox.tistory.com/117",
            "https://www.baeldung.com/kotlin/throws-annotation",
            "http://www.ktword.co.kr/test/view/view.php?m_temp1=2340",
            "https://www.google.com/search?q=url+%ED%85%8C%EC%8A%BD&rlz=1C5CHFA_enKR984KR984&oq=url+%ED%85%8C%EC%8A%BD&aqs=chrome..69i57j35i39j0i131i433i512j0i20i263i512j0i67i131i433j69i60j69i61j69i60.1462j0j7&sourceid=chrome&ie=UTF-8",
            "https://hoy.kr/_hoy/"
        )

        allowUrl.forEachIndexed { index, url ->
            println("${index + 1}. $url")

            //when
            val link = assertDoesNotThrow {
                linkExtractor.extractRegex(url)
            }

            //then
            assertTrue { (link.scheme ?: "") == "http" || (link.scheme ?: "") == "https" }
            assertNotNull(link.domain)
            assertNotNull(link.subUrl)
        }
    }
}