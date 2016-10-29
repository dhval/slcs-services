
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


3rd Party Time Sheet Manager

This application allows users to submit their timesheet similar to depositing checks online.

Step 1: User upload an image/pdf signed and enter hours for the week.
Step 2: Supervisor approves the timesheet after verifying uploaded artifact.

Security

This app is secured using latest available spring-framework security api's. Some of the features are;

- csrf or cross site resource forgery protection, this is more secure than simple cors headers.
 All requests either web or rest must carry csrf token or will be denied.
 This token is generated once your logs in and keeps on changing.

- Bcrypt for encrypting passwords. It is stronger than MD5 and generates different hash for same password based on salt.

- All resources (web and rest) are protected using 'WebSecurityConfigurer'. This allows locking users based on their roles.

Web UI

This app uses popular front-end libraries like bootstrap, font-awesome, angularjs etc.
The skin is borrowed from [SB Admin 2](http://startbootstrap.com/template-overviews/sb-admin-2/).
All of this can be easily upgraded or changed using bower, which is used for managing web resources.
However authentication and templating for user provioning is done using spring-thymeleaf libraries.

Rest API

The front-end uses rest api for querying.

Persistence

Currenty this app is using MySQL but can be easily migrated to another database.
This is database neutral as all queries are auto-generated using Spring Data Repositories


Tooling

It uses maven to build java artifacts. Front-end static resources are build using bower and grunt.


Deployment

This is deployed using two 'linked' docker images, one for running tomcat and other for running MySQL. For web application servlet 3.0 container is needed.

To have install web dependencies

npm install
npm install -g grunt-cli
grunt install

To run

mvn spring-boot:run


Draft

--- Similar

https://github.com/la-team/light-admin

Cors  config

http://stackoverflow.com/questions/31724994/spring-data-rest-and-cors
https://dzone.com/articles/cors-support-spring-framework
http://www.javacodegeeks.com/2014/07/spring-rest-ajax-and-cors.html


--- Made with love in USA - http://freehtml5.co/demos/voltage/

--- Samples

http://startbootstrap.com/template-categories/popular/
http://getbootstrap.com/examples/sticky-footer-navbar/
https://getbootstrap.com/examples/navbar/
