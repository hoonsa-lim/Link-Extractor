package com.hoonsalim95.linkextractor.selenium_crawling

import org.openqa.selenium.WebDriver

interface Browser {
    fun create(): WebDriver
}