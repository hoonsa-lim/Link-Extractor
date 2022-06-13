package com.hoonsalim95.linkextractor

data class Link (
    val scheme: String? = null,
    val domain: String? = null,
    val subUrl: String? = null,
    val faviconUrl: String? = null,
    val title: String? = null,
    val content: String? = null,
)