## Spring Boot Actuator

**Step 1:**  
Add the following dependency in your pom file. Don't add version number. It is managed by Spring Boot Starter Parent module (BOM).  
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```
**Step 2:** Start your Spring Boot application on the given port (refer application.properties | application.yml).  

**Note:** Spring Boot Actuator expose its endpoints based on Spring HATEOAS. It's managed by Spring Boot Starter Parent.  

**Step 3:** By Default Spring Boot Actuator module exposes the following endpoints.  
==> All Spring Boot Actuator endpoints are grouped by this path `/actuator`.
* `/actuator/health`  
* `/actuator/health/{*path}`  
* `/actuator/info`
  
==> If you hit this endpoint in your browser `http://localhost:<port>/actuator` this would give the following JSON response.  
```json
{
   "_links":{
      "self":{
         "href":"http://localhost:8085/actuator",
         "templated":false
      },
      "health":{
         "href":"http://localhost:8085/actuator/health",
         "templated":false
      },
      "health-path":{
         "href":"http://localhost:8085/actuator/health/{*path}",
         "templated":true
      },
      "info":{
         "href":"http://localhost:8085/actuator/info",
         "templated":false
      }
   }
}
```

==> For `/actuator/health` the following response will be sent by Actuator.  
```json
{
   "status":"UP"
}
```

==> For `/actuator/info` the following response will be sent by Actuator.  
```json
{}
```

==> There are other `/actuator` endpoints which is not exposed by default. In order to expose those endpoints you have to configure the following property in your `application.properties` file. In this way you can expose all `*` or specific end points using comma separated endpoint names.
```properties
management.endpoints.web.exposure.include=*
management.endpoints.web.exposure.include=beans,caches,conditions,env
```

==> If you want to specify your custom path to access existing Actuator endpoints configure below property to override defaults. After this step, you can access `/endpoints/health`
```properties
management.endpoints.web.base-path=/endpoints
```

==> If you want to use different path navigation for existing Actuator endpoints configure below property to override defaults. After this step, you can access `/endpoints/system/info`
```properties
management.endpoints.web.path-mapping.env=/system/env
```

==> If you want to expose all your actuator endpoints in a different server port, use this property.
```properties
management.server.port=8082
```

## Actuator Endpoints
| Actuator Endpoint | Descriotion                                                                       |
|-------------------|-----------------------------------------------------------------------------------|
| /actuator         | Discovery page for endpooints                                                     |
| /autoconfig       | Auto-Config Report                                                                |
| /beans            | List of Spring Beans                                                              |
| /configprops      | List properties from @ConfigurationProperties                                     |
| /dump             | Display thread dump                                                               |
| /env              | Environment Properties                                                            |
| /flyway           | List applied flyway DB migrations                                                 |
| /health           | Display application health info                                                   |
| /info             | Application info                                                                  |
| /loggers          | Show and edit loggers                                                             |
| /liquibase        | Show applied Liquibase DB migrations. Need neccessary dependency in the classpath |
| /metrics          | Show metrics information                                                          |
| /mappings         | Show request mappings (REST controller request/response information)              |
| /shutdown         | Gracefully shutdown - not enabled by default                                      |
| /trace            | Show last 100 request                                                             |
| /docs             | Spring MVC: Shows Actuator documentation                                          |
| /heapdump         | Provides hprof head dump file                                                     |
| /jolokia          | Expose JMX beans via http with Jolokia                                            |
| /logfile          | Returns log file                                                                  |

## Custom Endpoints
Spring Boot does offers to expose your custom service endpoint as Spring Boot Actuator specific to your use case.  
In order to expose your custom endpoint, 
1. Create your class annotated with `@Endpoint` and `@Component`.
2. Expose your actuator endpoint name as `@Endpoint id` attribute.
3. Based on your usecase, you can mark this `@Endpoint enableByDefault true or false`
4. Any methods annotated with `@ReadOperation`, `@WriteOperation`, or `@DeleteOperation` are automatically exposed over JMX and, in a web application, over HTTP as well.  
5. Run your application and hit `/actuator/mycustom-endpoint`

```java
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import java.util.List;

@Endpoint(id = "mycustom-endpoint", enableByDefault = true)
@Component
public class MyCustomEndpoint {

    @ReadOperation
    public List<String> getNames() {
        return List.of("Karthikeyan", "Pascal", "Thomas");
    }

}
```
Json Response  
```json
[
   "Karthikeyan",
   "Pascal",
   "Thomas"
]
```

## Auto Configured Health Indicators
* Cassandra Health Indicator: Verifies Cassandra Database is up
* Disk Space Health Indicator: Checks for low disk space
* DataSource Health Indicator: Checks the connection to the Database
* Elastic Search Health Indicator: Checks the connection to Elastic Search cluster
* JMS Health Indicator: Checks the connection to the JMS Broker
* Mail Health Check Indicator: Checks the connection to the Mail server
* Mongo Health Indicator: Checks the MongoDB database connection
* RabbitMQ Health Indicator: Checks the RabbitMQ is working properly.
* Redis Health Indicator: Check the database connection to the Redis is working properly.
* Solr Health Indicator: Checks the Solr server is up. 

## Custom Health Indicator
In order to develop your custom HealthIndicator refer below code.
```java
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class MyCustomHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        Random random = new Random();

        if(random.nextBoolean()) {
            return Health.down().withDetail("MSG-001", "Random Failure").build();
        }
        return Health.up().build();
    }
}
```

By default, the health endpoint shows cumulative health information from many components in your application. However, you can enable a detailed view by setting a property
```properties
management.endpoint.health.show-details=always
```

To test your custom Health Indicator endpoint hit the following endpoint.
```http request
http://localhost:8081/actuator/health
http://localhost:8081/actuator/health/myCustom
```

## Spring Framework Application Information
Spring Boot Actuator: Auto-Configured Information Contributors
* Environment Information Contributor - Display All Environment Properties starting with `info`
* Git Information Contributor - Displays Git Information from `git.properties`
* Build Information Contributor - Display Build Information from `META-INF/build-info.properties`
* Also possible to build your custom contributor based on your use case.

In order to get application information via Actuator endpoint you can use `/info`. By default it doesn't return any response. Inorder to get response from `/info` you can configure below properties in your `applicatin.properties`
```properties
info.app.encoding=UTF-8
info.app.java.source=1.8
info.app.java.target=1.8
```

Hit the `/info` path and you will get below response.
```json
{
   "app":{
      "encoding":"UTF-8",
      "java":{
         "source":"1.8",
         "target":"1.8"
      }
   }
}
```

### Adding Git Commit Information
Add the following maven build plugin.
```xml
<plugin>
    <groupId>pl.project13.maven</groupId>
    <artifactId>git-commit-id-plugin</artifactId>
</plugin>
```

Execute `mvn clean package`. During this execution the above plugin will add `git.properties` in your `target/classes` folder.
```properties
git.commit.id=a2e593aac5a00459dc31f812ae20f68616d074b6
git.commit.id.abbrev=a2e593a
git.commit.id.describe=a2e593a-dirty
git.commit.id.describe-short=a2e593a-dirty
```

After applied above steps, when you hit the `/info` it will display Git information.
```json
{
   "git":{
      "branch":"master",
      "commit":{
         "id":"a2e593a",
         "time":"2020-04-11T13:43:27Z"
      }
   }
}
```

## Spring Boot Actuator Metrics
Metrics endpoint is available in `/actuator/metrics`
#### System Metrics:
* Total System Memory: In KB - `mem`
* Free Memory: In KB -  `mem.free`
* Number of Processors as - `processors`
* System Uptime In Milliseconds - `uptime`
* Application Context Uptime as - `instance.uptime`
* Average System Load as - `systemload.avearage`
* Heap Information as - `heap`, `heap.committed`, `heap.init`, `heap.used`
* Thread Information as - `threads`, `thread.peak`, `thread.daemon`
* Class Load Information as - `classes`, `classes.loaded`, `classes.unloaded`
* Garbage Collection Information as - `gc.xxx.count`, `gc.xxx.time`
* Datasource Information - `datasource.xxx.active`, `datasource.xxx.usage`
* Cache Metrics - `cache.xxx.size`, `cache.xxx.hit.ratio`, `cache.xxx.miss.ratio`
    * Default Cache Statics for: EhCache, Hazelcast, Infinispan, jCache, Caffine
    * For other Cache Provider implement: `CacheStatisicsProvider`
* Tomcat Sessions: `httpsessions.active`, `httpsessions.max`
 
### How to write your custom `Counter` metrics?
*Suppose, you have a business service which you want to calculate the it hit count? How do you implement this scenario 
in Spring Boot 2.0 using Actuator?*  
Yes, it is possible using `micrometer` library. This dependency is already included 
in Spring Boot Actuator. For more information please refer 
[Production Ready Metrics](https://docs.spring.io/spring-boot/docs/2.1.9.RELEASE/reference/html/production-ready-metrics.html) 
and [MicroMeter - Application Monitoring](https://micrometer.io/)  
 
For the following business service `/product/{id}`, you would want to calculate the hit count then. Refer below code. 
Using `MeterRegistry` you have to create a your new counter endpoint and inside your `getProduct` 
method call `Counter.count` for auto increment.
```java
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

@Service
public class ProductServiceImpl implements ProductService {
    private Counter productCounter;
    
    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, JmsTextMessageService jmsTextMessageService,
                              MeterRegistry registry) {
        this.productCounter = registry.counter("com.techstack.learn.actuator.services.getproduct");
    }
    
    @Override
    public Product getProduct(Integer id) {
        productCounter.increment();
        return productRepository.findById(id).get();
    }

}
```

How to test the above logic using `/actuator/metrics`?  
Along with other `metrics` endpoints, it would display your custom one as shown below.
```json
{
   "names":[
      "com.techstack.learn.actuator.services.getproduct"
   ]
}
``` 
If you hit this `/actuator/metrics/com.techstack.learn.actuator.services.getproduct` it would display
the response.
```json
{
  "name": "com.techstack.learn.actuator.services.getproduct",
  "description": null,
  "baseUnit": null,
  "measurements": [
    {
      "statistic": "COUNT",
      "value": 2.0
    }
  ],
  "availableTags": []
}
```

## Spring Boot Actuator `/httptrace`
Spring Boot Actuator `/actuator/httptrace` endpoint. It will store last 100 request and response of your HTTP request.  
If you are using Spring Boot 2.2.x Actuator, by default it will not expose `/trace` endpoint.  
You have to configure as shown below.
```java
@Configuration
public class ActuatorConfiguration {

    @Bean
    public HttpTraceRepository httpTraceRepository() {
        return new InMemoryHttpTraceRepository();
    }
}
```

Refer: [Actuator Migration Tips: For /trace](https://juplo.de/actuator-httptrace-does-not-work-with-spring-boot-2-2/)

## Runtime Interaction with Running Spring Boot Applications using Actuators
#### JMX / Jolokia
In the Java world JMX is a very popular method used to connect to running JVMs. Via JMX you are able to connect to JVM 
to view properties. But also you are able to go and set properties too.  
The common use case is to go and change your `log levels` for different logger for debugging something.  
For using something like ActiveMQ JMX also be used to see different queses to interact with the actual Message Queues as well.  

* Drawbacks of JMX:
    * Security is cumbersome on a very large enterprise

**Jolokia:** is a library which supports to access your JMX beans. You can access your JMX beans via HTML page from 
Spring Boot Admin UI. For more information refer below links.  
* [Spring Boot Admin - JMX Bean Management](https://codecentric.github.io/spring-boot-admin/1.4.6/#jmx-bean-management)
* [Jolokia - Reference](https://jolokia.org/)

## Setting up Logging
By default Spring Boot application is setting up with `INFO` level logging. Let's look at below service class.  
Inorder to execute your `@Scheduled` service, you have to `@EnableScheduling` in your Spring Boot application.
```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class LogOutputGeneratorService {

    private static Logger log = LoggerFactory.getLogger(LogOutputGeneratorService.class);

    @Scheduled(fixedDelay = 5000)
    public void createLogEntries(){
        log.trace("This is a TRACE message");
        log.debug("This is a DEBUG message");
        log.info("This is a INFO message");
        log.warn("This is a WARN message");
        log.error("This is an ERROR message");
    }
}
```
The above method would print the following `log` messages in your console.
```textmate
2020-04-13 15:54:31.036  INFO 4252 --- [   scheduling-1] c.t.l.a.s.LogOutputGeneratorService      : This is a INFO message
2020-04-13 15:54:31.036  WARN 4252 --- [   scheduling-1] c.t.l.a.s.LogOutputGeneratorService      : This is a WARN message
2020-04-13 15:54:31.036 ERROR 4252 --- [   scheduling-1] c.t.l.a.s.LogOutputGeneratorService      : This is an ERROR message
```

#### How to change log level?
In order to change your application logging level
* You can setup log level in `application.properties`
* You can put it in system properties
* You can use JVM properties

```properties
logging.level.com.techstack.learn.actuator.services.LogOutputGeneratorService=trace
```

Note: Application restart is required.

```textmate
2020-04-13 16:00:28.635 TRACE 4273 --- [   scheduling-1] c.t.l.a.s.LogOutputGeneratorService      : This is a TRACE message
2020-04-13 16:00:28.635 DEBUG 4273 --- [   scheduling-1] c.t.l.a.s.LogOutputGeneratorService      : This is a DEBUG message
2020-04-13 16:00:28.635  INFO 4273 --- [   scheduling-1] c.t.l.a.s.LogOutputGeneratorService      : This is a INFO message
2020-04-13 16:00:28.635  WARN 4273 --- [   scheduling-1] c.t.l.a.s.LogOutputGeneratorService      : This is a WARN message
2020-04-13 16:00:28.635 ERROR 4273 --- [   scheduling-1] c.t.l.a.s.LogOutputGeneratorService      : This is an ERROR message
```

#### How to Change Logging Properties during Runtime?
Let see the current logging level for your configured `log`
```thymeleafurlexpressions
http://localhost:8081/actuator
http://localhost:8081/actuator/loggers/com.techstack.learn.actuator.services.LogOutputGeneratorService
```

```json
{
    "configuredLevel": "TRACE",
    "effectiveLevel": "TRACE"
}
```

In order to change your application `log` level for your application, you can hit `POST` method

```http request
curl -i -X POST -H 'Content-Type: application/json' -d '{"configuredLevel": "INFO"}' http://localhost:8081/actuator/loggers/com.techstack.learn.actuator.services.LogOutputGeneratorService
```

#### Kafka Health Checks

```Install & Start Kafka
1. brew install kafka
2. Create zookeeper.properties at /Users/2604309/kafka
3. zookeeper-server-start /Users/2604309/kafka/zookeeper.properties
3. Create server.properties at /Users/2604309/kafka
4. kafka-server-start /Users/2604309/kafka/server.properties
5. kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic test
6. kafka-console-producer --broker-list localhost:9092 --topic test
7. kafka-topics --delete --topic test --bootstrap-server localhost:9092
```
8. kafka-console-consumer --topic kafka-health-indicator --from-beginning --bootstrap-server localhost:9092 
9. kafka-run-class kafka.tools.GetOffsetShell --broker-list localhost:9092 --topic kafka-health-indicator
10. kafka-run-class kafka.tools.GetOffsetShell --broker-list localhost:9092 --topic test
```
