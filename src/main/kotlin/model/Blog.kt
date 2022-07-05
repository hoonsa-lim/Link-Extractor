package com.hoonsalim95.linkextractor.model

import java.net.URL

data class Blog(
    val blogId: String,
    val blogName: String,
    val homeUrl: URL,
    val favicon: Favicon,
)
