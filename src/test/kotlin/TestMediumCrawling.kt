import com.hoonsalim95.linkextractor.model.Blog
import com.hoonsalim95.linkextractor.model.Favicon
import com.hoonsalim95.linkextractor.platform.Medium
import com.hoonsalim95.linkextractor.selenium_crawling.ChromeBrowser
import com.hoonsalim95.linkextractor.selenium_crawling.SeleniumCrawler
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.distinctUntilChanged
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.openqa.selenium.By
import java.net.URL
import java.util.concurrent.TimeUnit
import kotlin.test.assertTrue

class TestMediumCrawling : TestCrawling{
    override val target: Medium
        get() = Medium(
            Blog(
                "/daangn",
                "당근마켓",
                URL("https://medium.com/daangn"),
                Favicon(URL("https://medium.com/daangn")),
            )
        )

    @Test
    override fun test_입력한_키워드로_글_가져오기() {
        runBlocking {
            val browser = ChromeBrowser("/Users/hslim/Documents/MyData/programming/Util/chromedriver").create()
            val targetUrl = "https://www.google.com/search?q=kotlin"
            val locator = By.xpath("//*[@id=\"rso\"]/div[1]/div/div[1]/div/a")

            SeleniumCrawler(browser, targetUrl, locator, 2000)
                .runCrawling(10)
                .distinctUntilChanged()
                .collect {
                    println("result == ${it.getAttribute("href")}")
                    assertTrue { it.getAttribute("href").contains("/daangn") }
                }
        }
    }

    @Test
    override fun test_입력한_시간동안_동작_후_flow_종료() {

    }
}