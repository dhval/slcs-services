
## Time Sheet App - Open source workflow for managing time sheets.

<img src="https://travis-ci.org/la-team/light-admin.png?branch=master"/>

[Time App](https://slcs.herokuapp.com/login) makes it possible to <b>customize time sheet workflows</b> for small companies without spending a lot of money on few paid options available.

### Features

* <b>Extensible Configuration</b>: Allows developers to easily configure their user interface using [Angular JS](https://angularjs.org/) and [Spring](https://spring.io/).
* <b>Cloud and Local Deployment</b>: Examples demonstrating deployment on Heroku or Ubuntu using [Spring Boot](https://projects.spring.io/spring-boot/).
* <b>CRUD operations</b>: Complete entities manipulation support using Spring repositories.
* <b>Custom Validation</b>: JSR-303 annotation-based validation rules support
* <b>Task Scheduling</b>: Send email/alerts to users using [Spring Scheduler](http://docs.spring.io/spring/docs/current/spring-framework-reference/html/scheduling.html).
* <b>Pluggable Security</b>: Authentication based on [Spring Security](http://www.springsource.org/spring-security).
* <b>REST API</b>: Enriching your application with REST API based on [Spring Data REST](http://www.springsource.org/spring-data/rest).
* <b>Easy integration</b>: Servlet 2.5/3.0 web applications supported


### Deployment

#### IntelliJ IDEA IDE

<b>Environment variables</b>
```
AWS_ACCESS_KEY_ID=YOUR_KEY
AWS_SECRET_ACCESS_KEY=YOUR_SECRET
```

<b>VM arguments</b>
```
-Dserver.port=8080
-Dspring.datasource.url=jdbc:postgresql://localhost:5432/slcsdb
-Dspring.datasource.username=postgres
-Dspring.datasource.password=postgres
```

#### Heroku

##### Run Heroku locally

<b>Create .env file</b>

```
JDBC_DATABASE_URL='jdbc:postgresql://localhost/slcsdb'
JDBC_USER=postgres
JDBC_PASS=postgres
```

<b>Create Procfile.local</b>

```
web: java $JAVA_OPTS -Dserver.port=$PORT -Dspring.datasource.url=$JDBC_DATABASE_URL  \
-Dspring.datasource.username=postgres -Dspring.datasource.password=postgres \
-jar target/services.jar
```

<b>Build and Run </b>
```
$ git add . && git commit -m "wip"
$ mvn clean install
$ heroku local -f Procfile.local  
```

#### Linux CLI

<b>As a spring boot fat executable jar</b>

```
java -jar target/services.jar -Dserver.port=$PORT -Dspring.datasource.url=$JDBC_DATABASE_URL  \
-Dspring.datasource.username=postgres -Dspring.datasource.password=postgres \

```

Or simply using maven
```mvn spring-boot:run```

### About this Application

<b>A Simple time sheet submission workflow</b>

This application allows users to submit their timesheet similar to depositing checks online.

- Step 1: User upload an image/pdf (optional) signed and enter hours for the week.
- Step 2: Supervisor approves the timesheet after verifying uploaded artifact.

<b>Security</b>

This app is secured using spring-security api's. Some of the features are;

- csrf or cross site resource forgery protection, this is more secure than simple cors headers.
 All requests either web or rest must carry csrf token or will be denied.
 This token is generated once your logs in and keeps on changing.

- Bcrypt for encrypting passwords. It is stronger than MD5 and generates different hash for same password based on salt.

- All resources (web and rest) are protected using 'WebSecurityConfigurer'. This allows locking users based on their roles.

<b>Web - Front-End development</b>

This app uses popular front-end libraries like bootstrap, font-awesome, angularjs, angular material design etc.
All of this can be easily upgraded or changed using bower, which is used for managing web resources.
However authentication and templating for user provisioning is rendered server side using spring-thymeleaf library.

To install web dependencies locally.
```
npm install
npm install -g grunt-cli
grunt install
```

<b>Rest API</b>

The front-end uses rest api for querying.

<b> Data Persistence </b>

Uses Spring JPA repository and any database vendor can potentially be plugged in.
User uploaded content is saved in AWS S3 buckets.


### References

Cors  config

http://stackoverflow.com/questions/31724994/spring-data-rest-and-cors
https://dzone.com/articles/cors-support-spring-framework
http://www.javacodegeeks.com/2014/07/spring-rest-ajax-and-cors.html
