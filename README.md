# MLR-Legacy-Service
Monitoring Location Legacy CRU Service

## Database Configuration
You will need a postgreSQL database to run this application. A Dockerized version is available at https://github.com/dsteinich/MLR_Legacy_DB.
The connection configuration is located in src/main/results/application.yml. You will need to create an application.yml file in your local project's root directory to provide the variable values. It should contain:

```
mlrLegacyDbHost: localhost
mlrLegacyDbPort: 5435
mlrLegacyUsername: mlr_legacy_data
mlrLegacyPasswd: changeMe
mlrLegacyServicePwd: changeMe
```

## Running the Application
Open a terminal window and navigate to the project's root directory.
Use the maven command ```mvn spring-boot:run``` to run the application.
It will be available at http://localhost:8080/monitoringLocations in you browser.

Swagger documentation is available at http://localhost:8080/swagger-ui.html

ctrl-c will stop the application.
