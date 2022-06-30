package com.hoonsalim95.linkextractor

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
    fun extractLegacy(url: String?): Link {
        if (url.isNullOrBlank()) throw  Exception(MSG_NULL_OR_BLANK)
        var scheme: String? = null
        var domain: String? = null
        var subUrl: String? = null

        val result = url.split(DOMAIN_START_SIGN)      //size 0인 경우도 있을 수 있음

        when(result.size){
            1 -> {
                val domainSubUrl = extractDomainSubUrl(result[0])
                domain = domainSubUrl.first
                subUrl = domainSubUrl.second
            }
            2 -> {
                val domainSubUrl = extractDomainSubUrl(result[1])
                scheme = result[0]
                domain = domainSubUrl.first
                subUrl = domainSubUrl.second
            }
            else -> {
                throw java.lang.Exception("url is only $DOMAIN_START_SIGN, or contains many $DOMAIN_START_SIGN")
            }
        }

        return Link(scheme, domain, subUrl)
    }

    private fun extractDomainSubUrl(url: String): Pair<String?, String?>{
        val result = url.replaceFirst(DOMAIN_START_SIGN, "").split(PATH_DIVIDER)

        return try {
            Pair(result.firstOrNull(),
                PATH_DIVIDER + result.filterIndexed { i,_-> i > 0 }.joinToString(PATH_DIVIDER))
        }catch (e: Exception){
            Pair(null, null)
        }
    }

    fun extract(url: String?): Link {
        if (url.isNullOrBlank()) throw  IllegalArgumentException(MSG_NULL_OR_BLANK)
        if (!url.contains(DOMAIN_START_SIGN)) throw  IllegalArgumentException("$MSG_REQUIRED_SCHEME\nurl == $url")

        val result = url.split(DOMAIN_START_SIGN)
        if (result.size > 2) throw  IllegalArgumentException("$MSG_NOT_ALLOW_START_SIGN.\nurl == $url")
        if (result.size < 2) throw  IllegalArgumentException("$MSG_NOT_ALLOW_URL\nurl == $url")
        if (result.first() != "http" && result.first() != "https") throw  IllegalArgumentException("$MSG_NOT_ALLOW_SCHEME\nurl == $url")

        val result2 = result[1].split(PATH_DIVIDER, limit = 2)
        if (result2.first().split(".").size < 2) throw  IllegalArgumentException("$MSG_NOT_ALLOW_DOMAIN.\nurl == $url")

        return Link(result.first(), result2.first(), result2.lastOrNull()?.replaceFirstChar { "$PATH_DIVIDER$it" })
    }

    fun extractRegex(url: String?): Link {
        if (url.isNullOrBlank()) throw  IllegalArgumentException(MSG_NULL_OR_BLANK)
        if (WEB_URL_REGEX.matcher(url).matches().not()) throw java.lang.IllegalArgumentException(MSG_NOT_ALLOW_URL)

        val schemeAndOther = url.split(DOMAIN_START_SIGN)
        val domainAndSubUrl = schemeAndOther[1].split(PATH_DIVIDER, limit = 2)
        val subUrl = domainAndSubUrl.lastOrNull()?.replaceFirstChar { "$PATH_DIVIDER$it" }
        return Link(schemeAndOther.first(), domainAndSubUrl.first(), subUrl)
    }
}