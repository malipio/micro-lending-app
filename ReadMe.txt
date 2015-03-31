----------------------------------------------------
This project implements Micro Lending Application with Spring Boot 
(Data JPA, Web MVC, Jackson, Validation) and PojoBuilder.
----------------------------------------------------

Run in Tomcat 8 embedded container:
mvn clean package
java -jar target\micro-lending-app-1.0.0-SNAPSHOT.war


REST Interface
--------------
POST http://localhost:8080/micro-lending-app/v1.0.0/loans/applications
JSON: LoanApplication

PUT http://localhost:8080/micro-lending-app/v1.0.0/loans/{loanId}/extension

GET http://localhost:8080/micro-lending-app/v1.0.0/loans?pesel={pesel}


Note that:
----------
1. Java 8 is required
  