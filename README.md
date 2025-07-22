# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.5.3/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.5.3/maven-plugin/build-image.html)

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.

# HEY! LISTEN! 
## It's dangerous to go alone! Take this!

## About Project
This is a core project that can be used with Ruge Solutions. 
It´s already with a JWT authentication builded, that allow create a Auth Method with you FrontEnd Application with controlled session or Middleware that need to rotate Token frequently. 
Token it´s encrypted with privateKey on Hash512. 

## Runing Environment
The expostition of endpoints must be allowed on : com.thukera.security.WebSecurity
Application use Log4J and rotate logs while running on Java, or WebServices. 
For WebService running, dependencies as Tomcat or Jboss must be configured.
For build on containners it must be configured on DockerFiles.

### You can access exposed endpoints at:
http://{aplication-url}/swagger-ui/index.html

## Configuration 
src/main/resources/application.properties
src/main/resources/config/log4j2.yml
dockerfile

