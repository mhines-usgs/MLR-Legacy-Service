# MLR-Legacy-Service
Monitoring Location Legacy CRU Service

To run this locally, you will need a postgres database. Database configuration 
is located in src/main/results/application.yml. The password of the database 
needs to be set by exporting it in the MAVEN_OPTS and then using the 
```mvn spring-boot:run``` command.
For example:
```
export MAVEN_OPTS=-DmlrLegacyPasswd=yourpassword
mvn spring-boot:run
```
