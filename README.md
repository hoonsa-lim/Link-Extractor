# Link-Extractor

## Feature
1. extract favicon from url
2. extract title from url

## Dependencies
1. jsoup
2. coroutine

## Usage
### add dependencies (build.gradle) 
```groovy

dependencies {

    //html parser
    implementation 'org.jsoup:jsoup:1.15.1'

    //coroutine
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.3'
}
```

### Code
```kotlin
val linkExtractor = LinkExtractor()

runBlocking {
    val link = linkExtractor.extract("https://m.naver.com/")
    println("link == $link")
    
    //link == Link(url=https://m.naver.com/, faviconUrl=https://m.naver.com/favicon.ico, title=NAVER)
}
```
