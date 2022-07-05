package com.hoonsalim95.linkextractor.model

import java.net.URL

data class Favicon (
    val targetUrl: URL,
    val faviconUrl: String? = null,
    val faviconTitle: String? = null,
)