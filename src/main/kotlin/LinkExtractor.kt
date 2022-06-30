package com.hoonsalim95.linkextractor

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
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

    suspend fun extract(urlText: String?): Link {
        validationUrl(urlText)
        val url = URL(urlText)
        extractFavicon(url).collect {
            println("extract favicon extract result == $it")
        }
        return Link(url)
    }

    fun extractFavicon(url: URL) : Flow<String> {
        return flowOf(Jsoup.connect(url.toString()).get())
            .mapNotNull { it.head() }
            .map { head -> head.getElementsByTag("link").toList() }
            .map { links -> links
                .filter { it.hasAttr("rel") && it.hasAttr("href") }
                .last { it.attr("rel") == "shortcut icon" || it.attr("rel") == "icon" }
                .attr("href")           //shortcut icon 으로 찾음
            }
            .map { favicon ->
                if (favicon.contains(DOMAIN_START_SIGN)) favicon
                else favicon.replaceFirstChar { "${url.protocol + DOMAIN_START_SIGN + url.authority + it}" }
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