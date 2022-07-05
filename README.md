# Link-Extractor

## Features
### 1. extract favicon from url
### 2. extract title from url

---
## Dependencies
1. jsoup
   - version: 1.15.1
   - https://github.com/jhy/jsoup
2. coroutine
   - version: 1.6.3
   - https://github.com/Kotlin/kotlinx.coroutines
---
## Usage
### Add dependencies
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

### Code
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