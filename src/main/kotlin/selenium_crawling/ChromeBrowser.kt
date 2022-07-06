package com.hoonsalim95.linkextractor.selenium_crawling

import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions

class ChromeBrowser(
    private val driverPath: String,
    private val driverName: String = "webdriver.chrome.driver",
    private val chromeOption: ChromeOptions? = null
): Browser {

    override fun create(): WebDriver {
        System.setProperty(driverName, driverPath)

        val option = chromeOption ?: ChromeOptions().apply {
            setBinary("")
            addArguments("--start-maximized")
            addArguments("--disable-popup-blocking")
            addArguments("--disable-default-apps")
        }
        return ChromeDriver(option,)
    }
}