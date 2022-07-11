# metrics_library

## Description
This project is a Java library builded with Gradle that encapsulate logic and dependencies for getting metrics from a Spring Application.

## Dependencies
* org.springframework.boot:spring-boot-starter
* org.springframework.boot:spring-boot-starter-web
* org.springframework.boot:spring-boot-starter-test
* org.springframework.boot:spring-boot-starter-actuator
* io.micrometer:micrometer-registry-prometheus

## To use Prometheus with this library

Initiate a docker container with this command:
```shell
sudo docker run -d --name prometheus_metrics_library -p 9090:9090 -v /your_path/prometheus.yml:/etc/prometheus/prometheus.yml prom/prometheus --config.file=/etc/prometheus/prometheus.yml
```

The `prometheus.yml` file:
```yaml
global:
  scrape_interval:     15s # By default, scrape targets every 15 seconds.
  evaluation_interval: 15s # Evaluate rules every 15 seconds. The default is every 1 minute.

# A scrape configuration containing exactly one endpoint to scrape:
# Here it's Prometheus itself.
scrape_configs:
  # The job name is added as a label `job=` to any time series scraped from this config.
  - job_name: 'prometheus'

    # Override the global default and scrape targets from this job every 5 seconds.
    scrape_interval: 15s

    static_configs:
      - targets: ['localhost:9090']

  # Details to connect Prometheus with Spring Boot actuator end point to scrap the data
  # The job name is added as a label `job=` to any time series scraped from this config.
  - job_name: 'spring-actuator'
    # Actuator end point to collect the data. 
    metrics_path: '/actuator/prometheus'

    #How frequently to scape the data from the end point
    scrape_interval: 15s

    #target end point. We are using the Docker, so local host will not work. You can change it with
    #localhost if not using the Docker.
    static_configs:
    - targets: ['10.98.218.243:8080'] # Put your local host in this target
```
Running this container with this configuration, you are able to scrape the spring actuator path and the prometheus itself.

---

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

