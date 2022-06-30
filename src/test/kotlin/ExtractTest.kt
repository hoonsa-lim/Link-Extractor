import com.hoonsalim95.linkextractor.LinkExtractor
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.net.MalformedURLException
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
        runBlocking {
            val link = linkExtractor.extract(url)

            //then
            assertEquals(s, link.url.protocol)
            assertEquals(d, link.url.authority)
            assertEquals(su, link.url.file)
        }
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
                runBlocking {
                    linkExtractor.extract(url)
                }
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
                runBlocking {
                    linkExtractor.extract(url)
                }
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
            "http://www.ktword.co.kr/test/view/view.php?m_temp1=2340",
            "https://www.google.com/search?q=url+%ED%85%8C%EC%8A%BD&rlz=1C5CHFA_enKR984KR984&oq=url+%ED%85%8C%EC%8A%BD&aqs=chrome..69i57j35i39j0i131i433i512j0i20i263i512j0i67i131i433j69i60j69i61j69i60.1462j0j7&sourceid=chrome&ie=UTF-8",
            "https://hoy.kr/_hoy/"
        )

        allowUrl.forEachIndexed { index, url ->
            println("${index + 1}. $url")

            //when
            val link = assertDoesNotThrow {
                runBlocking {
                    linkExtractor.extract(url)
                }
            }

            //then
            assertTrue { (link.url.protocol ?: "") == "http" || (link.url.protocol ?: "") == "https" }
            assertNotNull(link.url.authority)
            assertNotNull(link.url.file)
        }
    }

    @Test
    fun test자바의_URL_객체를_활용한_url_구조분해_및_예외처리(){
        //given
        val urls = listOf(
            "com1231sdf.....",   //x
            "com..",   //x
            "com.",   //x
            "asd.com.",   //x
            "com.aasd.asd.",   //x
            "com,aasd,asd,",   //x
        )

        urls.forEachIndexed { index, url ->
            //when
//            val link = linkExtractor.extractURL(url)
//            println("$index. ${link.toString()}, ${link.scheme}, ${link.domain}, ${link.subUrl}")

            //then
//            assertTrue { exception is MalformedURLException }
        }
    }

    @Test
    fun mainTest(){
        runBlocking {
//            val url = "https://yanolja.github.io/"
//            val url = "https://tech.socarcorp.kr/"
//            val url = "https://engineering.linecorp.com/ko/blog/"
//            val url = "https://techblog.yogiyo.co.kr/"
//            val url = "https://blog.banksalad.com/tech/"
//            val url = "https://developers-kr.googleblog.com/"
            val url = "https://techblog.woowahan.com/"
            val linkExtractor = LinkExtractor()
            val link = linkExtractor.extract("https://m.naver.com/")
            println("link == $link")
        }
    }
}