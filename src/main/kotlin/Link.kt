package com.hoonsalim95.linkextractor

import java.net.URL

data class Link (
    val url: URL,
    val faviconUrl: String? = null,
    val title: String? = null,
)