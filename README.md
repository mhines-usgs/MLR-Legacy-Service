[![Build Status](https://travis-ci.org/USGS-CIDA/MLR-Legacy-Service.svg?branch=master)](https://travis-ci.org/USGS-CIDA/MLR-Legacy-Service) [![Coverage Status](https://coveralls.io/repos/github/USGS-CIDA/MLR-Legacy-Service/badge.svg?branch=master)](https://coveralls.io/github/USGS-CIDA/MLR-Legacy-Service?branch=master)

# MLR-Legacy-Service
Monitoring Location Legacy CRU Service

## Local Configuration
You will need a postgreSQL database to run this application. A Dockerized version is available at https://github.com/USGS-CIDA/MLR_Legacy_DB.
The configuration is located in src/main/results/application.yml. You will need to create an application.yml file in your local project's root directory to provide the variable values. It should contain:

```
mlrLegacyDbHost: localhost
mlrLegacyDbPort: 5435
mlrLegacyDataUsername: mlr_legacy_data
mlrLegacyDataPassword: changeMe

oauthResourceTokenKeyUri: https://your.auth.server.url/oauth/token_key
oauthResourceId: myResourceId

springFrameworkLogLevel: info

maintenanceRoles: ROLE_ONE, ROLE_TWO

keystoreLocation: classpath:yourKeystore.jks
keystorePassword: changeMe
```

## Automated Testing
This application has two flavors of automated tests: unit tests (in the gov.usgs.wma.mlrlegacy package) and integration tests (in the gov.usgs.wma.mlrlegacy.db package) requiring a database. The unit tests can be run in isolation according to your normal practices.
The integration tests can be run in a terminal with the maven command ```mvn verify -P it``` in the project's root directory. Running in this manner will pull the database Docker image from the central repository and run it in a container.
They can also be run in your IDE against a database accessible to you. (Note that you should not use a shared database as the tests will destroy data and may have contention issues with other processes accessing the database.)
In either case, configuration information will be pulled from the maven setting.xml file. It will need to contain the following profile:
```
  <profile>
    <id>it</id>
    <properties>
      <postgresPassword>changeMe</postgresPassword>
      <mlrLegacyPassword>changeMe</mlrLegacyPassword>
      <mlrLegacyDataPassword>changeMe</mlrLegacyDataPassword>
      <mlrLegacyUserPassword>changeMe</mlrLegacyUserPassword>
      <mlrLegacyDataUsername>mlr_legacy_data</mlrLegacyDataUsername>
      <mlrLegacyServicePassword>changeMe</mlrLegacyServicePassword>
    </properties>
  </profile>
```

## Running the Application
Open a terminal window and navigate to the project's root directory.
Use the maven command ```mvn spring-boot:run``` to run the application.
It will be available at http://localhost:8080/monitoringLocations in you browser.

Swagger documentation is available at http://localhost:8080/swagger-ui.html

ctrl-c will stop the application.
