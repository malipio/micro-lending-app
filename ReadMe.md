## General Info

**This project implements Micro Lending Application with Spring Boot
(Data JPA, Web MVC, Jackson, Validation) and PojoBuilder.**

[![Build Status](https://travis-ci.org/malipio/micro-lending-app.svg?branch=master)](https://travis-ci.org/malipio/micro-lending-app)

## Running
Run in Tomcat 8 embedded container:

    mvn clean package
    java -jar target\micro-lending-app-1.0.0-SNAPSHOT.war

To run using maven:

    mvn spring-boot:run


## REST Interface

POST http://localhost:8080/micro-lending-app/v1.0.0/loans/applications
JSON: see /src/test/resources/loan.json:

    {
      "loan": {
        "from": "2015-11-06T15:20:08",
        "to": "2015-12-06T15:20:08",
        "amount": 100.0
      },
      "client": {
        "pesel": "12345678901",
        "firstName": "John",
        "lastName": "Doe"
      }
    }

PUT http://localhost:8080/micro-lending-app/v1.0.0/loans/{loanId}/extension

GET http://localhost:8080/micro-lending-app/v1.0.0/loans?pesel={pesel}


## Notes

1. Java 8 is required
