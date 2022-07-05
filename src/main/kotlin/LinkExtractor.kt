package com.hoonsalim95.linkextractor

import com.hoonsalim95.linkextractor.model.Favicon
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.net.URL
import java.util.regex.Pattern

class LinkExtractor {
    companion object{
        /**
         * message
         */
        const val MSG_NULL_OR_BLANK = "null or blank"

        //scheme
        const val MSG_REQUIRED_SCHEME = "please attach scheme and '://' like 'https://'"
        const val MSG_NOT_ALLOW_START_SIGN = "not allow. more than two '://' like 'https://'"
        const val MSG_NOT_ALLOW_SCHEME = "allow only 'http' or 'https'"

        const val MSG_NOT_ALLOW_URL = "please check url"
        const val MSG_NOT_ALLOW_DOMAIN = "not valid, please check domain"

        /**
         * regex
         */
        val WEB_URL_REGEX: Pattern = Pattern.compile("^(https?):\\/\\/([^:\\/\\s]+)(:([^\\/]*))?((\\/[^\\s/\\/]+)*)?\\/([^#\\s\\?]*)(\\?([^#\\s]*))?(#(\\w*))?$")

        /**
         * const text
         */
        const val DOMAIN_START_SIGN = "://"
        const val PATH_DIVIDER = "/"
    }

    suspend fun extractFavicon(urlText: String?): Favicon {
        validationUrl(urlText)
        val url = URL(urlText)

        return Favicon(url,
            faviconUrl = extractFaviconUrl(url),
            targetUrlTitle = extractUrlTitle(url).first()
        )
    }

    suspend fun extractFaviconUrl(url: URL) : String? {
        val head = Jsoup.connect(url.toString()).get().head()

        return CoroutineScope(Dispatchers.IO).async {
            val deferredLink = async { findFaviconFromTag("link", head, url) }
            val deferredMeta = async { findFaviconFromTag("meta", head, url) }
            return@async Pair(deferredLink.await(), deferredMeta.await())
        }.await().let {
            it.first ?: it.second   //link 데이터를 우선으로 하고, null 일 경우 meta 데이터 활용
        }
    }

    private fun findFaviconFromTag(tagName: String, head: Element, url: URL): String? {
        val targetTags = head.getElementsByTag(tagName).toList()
        when(tagName){
            "link" -> {
                return targetTags
                    .filter { it.hasAttr("rel") && it.hasAttr("href") }
                    .firstOrNull { it.attr("rel") == "shortcut icon" || it.attr("rel") == "icon" }
                    ?.attr("href")
                    ?.let { wholeFavicon -> makePerfectUrl(wholeFavicon, url) }
            }
            "meta" -> {
                return targetTags
                    .filter { it.hasAttr("content") && it.hasAttr("itemprop") }
                    .firstOrNull { it.attr("itemprop") == "image" && it.attr("content").contains(".png") }
                    ?.attr("content")
                    ?.let { wholeFavicon -> makePerfectUrl(wholeFavicon, url) }
            }
            else -> return null
        }
    }

    fun extractUrlTitle(url: URL) : Flow<String> {
        return flowOf(Jsoup.connect(url.toString()).get())
            .mapNotNull { it.head() }
            .map { head -> head.getElementsByTag("title")
                .firstOrNull()
                ?.text() ?: url.authority
            }
    }

    private fun divideUrl(url: String): Triple<String, String, String?>{
        val schemeAndOther = url.split(DOMAIN_START_SIGN)
        val domainAndSubUrl = schemeAndOther[1].split(PATH_DIVIDER, limit = 2)
        val subUrl = domainAndSubUrl.lastOrNull()?.replaceFirstChar { "$PATH_DIVIDER$it" }
        return Triple(schemeAndOther.first(), domainAndSubUrl.first(), subUrl)
    }

    private fun validationUrl(url: String?){
        if (url.isNullOrBlank()) throw IllegalArgumentException(MSG_NULL_OR_BLANK)
        if (WEB_URL_REGEX.matcher(url).matches().not()) throw IllegalArgumentException(MSG_NOT_ALLOW_URL)
    }
}