package com.hoonsalim95.linkextractor.model

import java.net.URL

data class Favicon (
    val targetUrl: URL,
    val targetUrlTitle: String? = null,
    val faviconUrl: String? = null,
)