package com.hoonsalim95.linkextractor.selenium_crawling

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.flow.*
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement

class SeleniumCrawler(
    val browser: WebDriver,
    val targetUrl: String,
    val locator: By,
    val delayTime: Long = 2000
) {
    private var endFindElement = false

    fun runCrawling(maximumSeconds: Int) = channelFlow {
        try {
            runBlocking {
                this.launch { runTimer(maximumSeconds) }
                this.launch {
                    //show browser
                    println("start browser")
                    browser.get(targetUrl)

                    //find elements
                    delay(delayTime)
                    println("start find elements")
                    val docs = browser.findElements(locator)
                    docs.forEach { send(it) }
                    endFindElement = true
                }
            }
        } catch (e: Exception) {
            println("exception == $e")
            e.printStackTrace()
        } finally {
            browser.close()
        }
    }

    private suspend fun findElement(scope: ProducerScope<WebElement>) {
        //show browser
        println("start browser")
        browser.get(targetUrl)

        //find elements
        delay(delayTime)
        println("start find elements")
        val docs = browser.findElements(locator)
        docs.forEach { scope.send(it) }
        endFindElement = true
    }

    private suspend fun runTimer(maximumSeconds: Int) {
        println("start timer")  //timer
        (0..maximumSeconds)
            .asSequence()
            .asFlow()
            .onEach {
                println("on each == $it")
                delay(1000)
            }
            .map { println("seconds == $it"); it }
            .collect {
                if (it == maximumSeconds) throw Exception("time over")
                if (endFindElement) throw Exception("end. transaction")
            }
    }
}