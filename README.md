# records-of-processing-activities
A service that provides functionality to register, modify, list and delete GDPR records of processing activities.

## Requirements
- maven
- java 15
- docker
- docker-compose

### Environment Variables

```
export SSO_HOST
```

## Run tests
```
mvn verify
```

## Run locally
```
docker-compose up --build
```
or:
```
mvn spring-boot:run -Dspring-boot.run.profiles=develop
```

Then in another terminal e.g.
```
% curl -i http://localhost:8080/ping
% curl -i http://localhost:8080/ready
```
