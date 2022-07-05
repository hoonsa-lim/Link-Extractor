package com.hoonsalim95.linkextractor

import java.net.URL

/**
 * 크롤링한 데이터 중 도메인이 붙어 있지 않은 url에 scheme 과 도메인을 연결하는 함수
 *
 * @param wholeUrl: /abc/def/hij...
 * @param url: https://www.google.com
 *
 * @return https://www.google.com/abc/def/hij...
 */
fun makePerfectUrl(wholeUrl: String, url: URL): String {
    return if (wholeUrl.contains(LinkExtractor.DOMAIN_START_SIGN))
        wholeUrl
    else
        wholeUrl.replaceFirstChar { url.protocol + LinkExtractor.DOMAIN_START_SIGN + url.authority + it }
}