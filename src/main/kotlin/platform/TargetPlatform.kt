package com.hoonsalim95.linkextractor.platform

import com.hoonsalim95.linkextractor.model.ArticleItem
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.TimeUnit

interface TargetPlatform {
    val domain: String
    val subPath: String
    val targetHomeUrl: String
    val targetFullUrl: String

    fun crawling(keyword: String, duration: Int, timeUnit: TimeUnit): Flow<List<ArticleItem>>
}