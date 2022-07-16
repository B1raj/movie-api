# movie-api

This app uses gradle for managing the dependency. Hence, we can use below standard commands after checking out the project.

### Running in spring boot Jar
Create Jar
```
./gradlew bootJar
```
This application is packaged as a jar which has Tomcat embedded. No Tomcat or JBoss installation is necessary.

1. Import the project as existing gradle project.
2. Make sure you are using JDK 1.11 and Gradle
2. Run application with the following configuration.
```
    "java -jar movie-api-0.0.1-SNAPSHOT.jar"
```
3. Check the stdout file to make sure no exceptions are thrown. Once the application runs you should see something like this

```
2022-07-16 19:06:31.010  WARN 72300 --- [           main] JpaBaseConfiguration$JpaWebConfiguration : spring.jpa.open-in-view is enabled by default. Therefore, database queries may be performed during view rendering. Explicitly configure spring.jpa.open-in-view to disable this warning
2022-07-16 19:06:32.469  INFO 72300 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
2022-07-16 19:06:32.486  INFO 72300 --- [           main] c.b.b.m.movieapi.MovieApiApplication     : Started MovieApiApplication in 9.734 seconds (JVM running for 10.522)
```
4. Application starts on default 8080 port. Browse the following URL to view the basic swagger documentation.
```
 http://localhost:8080/swagger-ui.html
```

The application uses h2 in-memory database, So once the application is booted up the console can be accessed at
```
http://localhost:8080/h2-console 
un: sa
pwd: password
```
## Refresh dependencies (if needed)
gradle build --refresh-dependencies

## Run application 
./gradlew :bootRun

## Run all test cases
./gradlew :test


[solution.md](./solution.md) <br>
[how_to_run.md](./how_to_run.md ) <br>
[how_to_test.md](./how_to_test.md ) <br>
[to_do.md](./to_do.md )<br>
[assumptions.md](./assumptions.md)<br>
[scale.md](./scale.md)<br>