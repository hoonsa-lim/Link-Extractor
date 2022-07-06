package com.hoonsalim95.linkextractor.platform

import com.hoonsalim95.linkextractor.makePerfectUrl
import com.hoonsalim95.linkextractor.model.ArticleItem
import com.hoonsalim95.linkextractor.model.Blog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import org.jsoup.Jsoup
import java.util.concurrent.TimeUnit

class Medium(
    val blog: Blog
): TargetPlatform {

    override val domain: String
        get() = blog.homeUrl.toString()

    override val subPath: String
        get() = "/search?q="

    override val targetHomeUrl: String
        get() = "$domain${blog.blogId}"

    override val targetFullUrl: String
        get() = "$domain$subPath"

    override fun crawling(keyword: String, duration: Int, timeUnit: TimeUnit): Flow<List<ArticleItem>> {
        println("run domain = $domain")
        println("run crawling url == $targetFullUrl")
        try {
            val html = Jsoup.connect(targetFullUrl + keyword)
//                .cookies(mapOf(
//                    "uid" to "lo_f3206d24afea",
//                    "sid" to "1%3A2Vb%2BbLf8ZMZZFxBuJe4zhVrEPT4XHRMwB7fOf1xJuT9BP3TTFxE2YxFJmox1r5KB",
//                    "_ga" to "GA1.2.168405358.1649872307",
//                    "g_state" to "{\"i_p\":1658839735933,\"i_l\":4}",
//                    "lightstep_guid/medium-web" to "4b4ba91ab0f7116c",
//                    "lightstep_session_id" to "8eba8ae9c21ddbf7",
//                    "pr" to "2",
//                    "tz" to "-540",
//                    "_gid" to "GA1.2.741363461.1657015309",
//                    "sz" to "861",
//                    "__cfruid" to "0b02e86963e081edd941fc990055ee65f8a07185-1657028417",
//                    "_dd_s" to "rum=0&expire=1657032333915",
//                ))
//                .userAgent("Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Mobile Safari/537.36")
                .get()
            println("run crawling html == $html")

            return flowOf(html)
                .map {
                    delay(3000)
                    it.getElementsByAttributeValueContaining("href", blog.blogId)
                }
//                .filter {
//                    it.forEachIndexed { index, element ->
//                        println("a tag $index == $element")
//                    }
//
//                    it.hasAttr("href") && it.hasAttr("aria-label")
//                }
                .filter {
                    it.forEachIndexed { index, element -> println("result index == $index == $element") }
                    it.attr("aria-label") == "Post Preview Title"
                }
                .map {
                    println("filter 2 aria == ${it.size}")
                    it.toList()
                }
                .map {
                    println("flow 5 ${it.size}")
                    it.map {
                        ArticleItem("당근", makePerfectUrl(it.attr("href"), blog.homeUrl), "2022-02-02", )
                    }
                }
        }catch (e: Exception){
            println("error == ${e.toString()}")
            e.printStackTrace()
            return emptyFlow()
        }
    }
}