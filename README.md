# video-hosting-demo
Video hosting demo using Spring Boot and WebFlux

## Installation
1. Clone repository
2. Create folder where application will store video files and make it accessible
2. Create property file from sample:
```
cp ./src/main/resources/application.properties.sample ./src/main/resources/application.properties
```
3. Edit created property file: change video folder path to the one you created before
```
spring.servlet.multipart.enabled=true
spring.servlet.multipart.file-size-threshold=2KB
spring.servlet.multipart.max-file-size=2000MB
spring.servlet.multipart.max-request-size=2150MB
video.location=/home/admin/videos
```
4. Run project:
```
mvn spring-boot:run
```
5. Go to **localhost:8080** in browser to see home page of application
