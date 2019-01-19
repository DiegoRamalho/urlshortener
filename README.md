# URL Shortener API

This is a Java 8 project for short URL creation and save it into mongodb database. It uses:

* [Spring Boot](https://spring.io/projects/spring-boot) and [Spring Data](https://spring.io/projects/spring-data) 

* [Springfox](http://springfox.github.io/springfox/)

* [MapStruct](http://mapstruct.org/)

* [Lombok](https://projectlombok.org)

## Running with Maven

To run using maven just run:


    mvn clean spring-boot:run

The default configuration to the mongoDB is localhost:27017, if it are running at other host or port you can run the application using:
 

    mvn clean spring-boot:run -Dspring-boot.run.arguments=--spring.data.mongodb.host=<MONGODB_HOST>,--spring.data.mongodb.port=<MONGODB_PORT>

Or change the properties at application.yml file.

## Running with Docker

To generate the image from docker via maven just run:
 

    mvn clean package docker:removeImage docker:build 
	
To execute the application:
 

    docker-compose [-f src/main/docker/docker-compose.yml] down && docker-compose [-f src/main/docker/docker-compose.yml] up -d
	
Viewing log:
 

    docker logs -f docker_urlshortener-app_1

    
## See API methods

To see the API methods, run the application and go to:
 

    http://localhost:8080/swagger-ui.html
	
## Structure
### main/java
#### config
Project Spring context settings.

#### domain
Entities of the project. 

#### repository
Spring Data JPA repositories.

* By default, they should extend MongoRepository<Entity, IDClass>.

* It should end with the expression Repository.

#### rest
REST Services Interfaces

* Only DTOs should be exported.

* It should end with the expression RestService.

#### rest.dto
Data Transfer Objects using to moving the database data to the client.

#### rest.impl
REST Services Implementation

* It should be annotated with @ RestController and @ RequestMapping ("/ api").

* It should end with the expression RestServiceImpl.

#### rest.util
Utility classes for generating REST services.

#### service
Interface of the services present in the project.

* It should end with the expression Service. 

#### service.impl
Concrete implementation of the services present in the project.

* Manipulates DTOs and transforms them into the corresponding entity.

* It should be annotated with @ Service.

* It should end with the expression ServiceImpl.

#### service.mapper
DTO converter interface for Entities.

* It should extends EntityMapper<DTO, Entity>.

* It should end with the expression Mapper.

### main/resources
Project Resources.

#### config
Spring configuration files.

* application.yml: Project Settings.

### test/java
#### rest e service
Test for the REST webservice and Services project.

### test/resources
Configuration to execute the tests in this project.
