# records-of-processing-activities
A service that provides functionality to register, modify, list and delete GDPR records of processing activities.

## Requirements
- maven
- java 17
- docker
- docker-compose

### Environment Variables

```
export OIDC_ISSUER
export OIDC_JWKS
export MONGO_HOST
export MONGO_PORT
export MONGO_USERNAME
export MONGO_PASSWORD
```

## Run tests
```
mvn verify
```

## Run locally
```
docker-compose up --build
```

Then in another terminal e.g.
```
% curl -i http://localhost:8081/ping
% curl -i http://localhost:8081/ready
```

or:
```
docker-compose up -d
mvn spring-boot:run -Dspring-boot.run.profiles=develop
```

Then in another terminal e.g.
```
% curl -i http://localhost:8080/ping
% curl -i http://localhost:8080/ready
```
