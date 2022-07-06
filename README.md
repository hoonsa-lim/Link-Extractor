# Link-Extractor

## Features
### 1. crawling. use selenium on chrome
### 2. extract favicon from url
### 3. extract title from url

---
## Dependencies
1. jsoup
   - version: 1.15.1
   - https://github.com/jhy/jsoup
2. selenium (java, support)
   - version: 4.3.0
   - https://github.com/SeleniumHQ/selenium
3. coroutine
   - version: 1.6.3
   - https://github.com/Kotlin/kotlinx.coroutines
---
## Add dependencies
#### 1. gradle
```groovy
//build.gradle(project)
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

//build.gradle(module)
dependencies {
    ...
    implementation 'com.github.hoonsa-lim:Link-Extractor:0.1.3'
}
```
#### 2. or maven
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```
```xml
<dependency>
    <groupId>com.github.hoonsa-lim</groupId>
    <artifactId>Link-Extractor</artifactId>
    <version>0.1.3</version>
</dependency>
```


### Option android - add permission (AndroidManifest.xml)
```xml
<uses-permission android:name="android.permission.INTERNET"/>
```

### Option Crawling - download chrome driver
1. download chrome browser
2. check chrome version -> chrome://settings/help
3. download chrome driver -> https://chromedriver.chromium.org/downloads

---
## Code
### 1. extract favicon and title
```kotlin
val linkExtractor = LinkExtractor()

runBlocking {
    val result = linkExtractor.extractFavicon("https://m.naver.com/")
    println("result == $result")
    
    // result == Favicon(
    // targetUrl = https://m.naver.com/, 
    // faviconUrl = https://m.naver.com/favicon.ico, 
    // targetUrlTitle = NAVER
    // )
}
```

### 2. crawling
```kotlin
runBlocking {
   val browser = ChromeBrowser("/userChromeDriverPath/chromedriver").create()
   val targetUrl = "https://www.google.com/search?q=kotlin"
   val locator = By.xpath("//*[@id=\"rso\"]/div[1]/div/div[1]/div/a")
   val delaySeconds = 2000      //for dynamic web page
   
   SeleniumCrawler(browser, targetUrl, locator, delaySeconds)       
      .runCrawling(10)      //maximum seconds
      .distinctUntilChanged()
      .collect {
         println("extracted url == ${it.getAttribute("href")}")
      }
}
```
---
## Reference
1. xpath
   - https://en.wikipedia.org/wiki/XPath
   - https://stackoverflow.com/questions/22571267/how-to-verify-an-xpath-expression-in-chrome-developers-tool-or-firefoxs-firebug