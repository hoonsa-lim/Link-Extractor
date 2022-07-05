import com.hoonsalim95.linkextractor.model.Blog
import com.hoonsalim95.linkextractor.model.Favicon
import com.hoonsalim95.linkextractor.platform.Medium
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
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
            val d = assertDoesNotThrow {
                val result = this.async {
                    target
                        .crawling("android", 10, TimeUnit.SECONDS)
                        .first()
                }

                this.launch {
                    val r = result.await()
                    println("test result == $r")
                    assertTrue { r.first().title.contains("당근") }

                }
            }
        }
    }

    @Test
    override fun test_입력한_시간동안_동작_후_flow_종료() {

    }
}