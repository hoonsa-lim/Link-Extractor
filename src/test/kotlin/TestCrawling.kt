import com.hoonsalim95.linkextractor.platform.TargetPlatform
import org.junit.jupiter.api.Test

interface TestCrawling {
    val target: TargetPlatform

    @Test
    fun test_입력한_키워드로_글_가져오기()

    @Test
    fun test_입력한_시간동안_동작_후_flow_종료()
}