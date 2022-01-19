This is the continuation of my previous User Leave Management System Spring MVC project. 

I have updated this project with Spring Boot(2.6.2) which configures Hibernate as the default JPA provider, so it's no longer necessary to define the entityManagerFactory bean unless we want to customize it. Moreover, Spring Boot provides an embedded Apache Tomcat-Server build to deploy the application. So, no need to run gradle commands(gradle build, gradle tomcatrun) to deploy.

I used MySql database and Spring Data JPA for the implementation of the data access layers.

Required:

mysql-8.0.27 or any version spring boot provides - to manage DB. ddl.sql & dml.sql file is in DDL-scripts & DML-scripts folder respectively. Other db informations are in application.properties file.

