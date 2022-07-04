# metrics_library

## Description
This project is a Java library builded with Gradle that encapsulate logic and dependencies for getting metrics from a Spring Application.

## Dependencies
* org.springframework.boot:spring-boot-starter
* org.springframework.boot:spring-boot-starter-web
* org.springframework.boot:spring-boot-starter-test
* org.springframework.boot:spring-boot-starter-actuator
* io.micrometer:micrometer-registry-prometheus

## Usage

### Usage with JitPack
Publicated repository in JitPack:

https://jitpack.io/#api-generator-playground/metrics_library

Add this line in your repositories in `build.gradle`
```groovy
repositories {
	//...
	maven { url 'https://jitpack.io' }
}
``` 
And add this implementation in your dependencies
```groovy
dependencies {
    implementation 'com.github.api-generator-playground:metrics_library:1.0'
}
```

---

### Local Usage

For easy sharing in your apps you can publish this project in your local Maven repositories through this command:
```bash
gradle publishToMavenLocal
```

### In your `build.gradle`

After that your application should add the Maven local repositories:
```groovy
repositories {
	mavenCentral()
	mavenLocal() //add this line
}
```

Add this line into your dependencies in build.gradle
```groovy
dependencies {
	//...
	implementation 'org.apigenerator.playground:metrics_library:1.0'
}
```

---

### In your `pom.xml`
?

[![](https://jitpack.io/v/api-generator-playground/metrics_library.svg)](https://jitpack.io/#api-generator-playground/metrics_library)

