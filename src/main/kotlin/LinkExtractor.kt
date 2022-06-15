package com.hoonsalim95.linkextractor

class LinkExtractor {
    fun extract(url: String?): Link {
        if (url.isNullOrBlank()) throw  java.lang.Exception("null or blank")
        var scheme: String? = null
        var domain: String? = null
        var subUrl: String? = null

        val result = url.split("://")      //size 0인 경우도 있을 수 있음

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
                throw java.lang.Exception("url is only ://, or contains many ://")
            }
        }

        return Link(scheme, domain, subUrl)
    }

    private fun extractDomainSubUrl(url: String): Pair<String?, String?>{
        val result = url.replaceFirst("://", "").split("/")

        return try {
            Pair(result.firstOrNull(),
                "/" + result.filterIndexed { i,_-> i > 0 }.joinToString("/"))
        }catch (e: Exception){
            Pair(null, null)
        }
    }
}