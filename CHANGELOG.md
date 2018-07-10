# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html). (Patch version X.Y.0 is implied if not specified.)

## [Unreleased]
### Added
- Dockerfile Healthcheck

### Removed
- Dockerfile docker-entrypoint.sh
- keystore location and password from application.yml

### Updated
- application.yml to conform to other services' oauth config naming conventions

## [0.15.0] - 2017-11-20
### Added
- Global exception handler for Http requests
- Security - User must be authenticated to access any service. User must also be authorized to PATCH, POST, or PUT.
- Validation - Implements additional validations on allowable field lengths, data type and duplicate keys.
- API - GET /monitoringLocations endpoint using Agency Code and Site Number now returns a single monitoring location object instead of an array of one monitoring location object.
- add travis and coveralls
- HTTPS Support

## [0.14.0] - 2017-11-02
### Changed
- Agency Code and Site Number are now required for the GET /monitoringLocations endpoint.

## [0.13] - 2017-10-23
### Added
- No Changes - Testing Release process.

## [0.12] - 2017-10-23
### Added
- No Changes - Testing Release process.

## [0.11] - 2017-10-18
### Added
- PATCH (update) a partial Monitoring Location. Also referred to as a "merge".

## [0.10] - 2017-10-05
### Changed
- No Changes - Testing Release process.

## [0.9] - 2017-10-05
### Changed
- No Changes - Testing Release process.

## [0.8] - 2017-10-02
### Changed
- Dockerfile changed to reflect new way of interacting with Maven Repository.
- Related README updates.

## [0.7] - 2017-09-29
### Changed
- Separate Release and Snapshot Maven Repositories

## [0.6] - 2017-09-28
### Changed
- No Changes - Testing Release process.

## [0.5] - 2017-09-28
### Changed
- No Changes - Testing Release process.

## [0.4] - 2017-09-28
### Changed
- No Changes - Testing Release process.

## [0.3] - 2017-09-28
### Changed
- No Changes - Testing Release process.

## [0.2] - 2017-09-28
### Changed
- No Changes - Testing Release process.

## 0.1 - 2017-09-28
### Added
- GET a list of all Monitoring Locations, optionally filtered by Agency Code and/or Site Number.
- GET a Monitoring Location by it's URI.
- POST (create) a new Monitoring Location.
- PUT (update) an existing Monitoring Location.
- Swagger Documentation.
- Spring Boot Actuator Monitoring.

[Unreleased]: https://github.com/USGS-CIDA/MLR-Legacy-Service/compare/mlrLegacy-0.15.0...master
[0.15.0]: https://github.com/USGS-CIDA/MLR-Legacy-Service/compare/mlrLegacy-0.14...mlrLegacy-0.15.0
[0.14.0]: https://github.com/USGS-CIDA/MLR-Legacy-Service/compare/mlrLegacy-0.13...mlrLegacy-0.14.0
[0.13]: https://github.com/USGS-CIDA/MLR-Legacy-Service/compare/mlrLegacy-0.12...mlrLegacy-0.13
[0.12]: https://github.com/USGS-CIDA/MLR-Legacy-Service/compare/mlrLegacy-0.11...mlrLegacy-0.12
[0.11]: https://github.com/USGS-CIDA/MLR-Legacy-Service/compare/mlrLegacy-0.10...mlrLegacy-0.11
[0.10]: https://github.com/USGS-CIDA/MLR-Legacy-Service/compare/mlrLegacy-0.9...mlrLegacy-0.10
[0.9]: https://github.com/USGS-CIDA/MLR-Legacy-Service/compare/mlrLegacy-0.8...mlrLegacy-0.9
[0.8]: https://github.com/USGS-CIDA/MLR-Legacy-Service/compare/mlrLegacy-0.7...mlrLegacy-0.8
[0.7]: https://github.com/USGS-CIDA/MLR-Legacy-Service/compare/mlrLegacy-0.6...mlrLegacy-0.7
[0.6]: https://github.com/USGS-CIDA/MLR-Legacy-Service/compare/mlrLegacy-0.5...mlrLegacy-0.6
[0.5]: https://github.com/USGS-CIDA/MLR-Legacy-Service/compare/mlrLegacy-0.4...mlrLegacy-0.5
[0.4]: https://github.com/USGS-CIDA/MLR-Legacy-Service/compare/mlrLegacy-0.3...mlrLegacy-0.4
[0.3]: https://github.com/USGS-CIDA/MLR-Legacy-Service/compare/mlrLegacy-0.2...mlrLegacy-0.3
[0.2]: https://github.com/USGS-CIDA/MLR-Legacy-Service/compare/mlrLegacy-0.1...mlrLegacy-0.2
