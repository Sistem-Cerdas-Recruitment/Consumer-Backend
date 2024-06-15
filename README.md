# Consumer Backend

REST API to support Sistem Cerdas untuk _Candidate Matching_ dan Asesmen Talenta IT

## Features

- [x] JWT Authentication and Authorization
- [x] Bcrypt password hashing
- [x] Bearer token authentication
- [x] Job Posting
- [x] Job Application
- [x] Automated Interview
- [x] Interview Evaluation
- [x] Anti Cheat Detection

## Setup

### Prerequisites

- [Java 21](https://www.oracle.com/id/java/technologies/downloads/#jdk21-linux) - Java Development Kit 21
- [PostgreSQL](https://www.postgresql.org/) - Open source object-relational database system

### Installation

1. Clone the repository

```bash
git clone https://github.com/Sistem-Cerdas-Recruitment/Consumer-Backend.git
```

2. Build using Maven

```bash
mvn clean install
```

3. Copy the `application.properties.example` file to `application.properties`

```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
```

4. Start the server

```bash
java -jar target/BE-v1.jar
```

### Docker

1. Build the Docker image

```bash
mvn spring-boot:build-image -Dspring-boot.build-image.imageName=<image-name>
```

2. Run the Docker container

```bash
docker run -p 8080:8080 <image-name>
```

### Environment Variables

- `spring.datasource.url` - Database URL
- `spring.datasource.schema` - Database schema
- `spring.datasource.username` - Database username
- `spring.datasource.password` - Database password
- `jwt.secret` - JWT secret key
- `aws.access.key` - AWS access key
- `aws.secret.key` - AWS secret key
- `service.x-api-key` - API key for external services

### Documentation

- [Postman Collection](https://www.postman.com/warped-comet-178893/workspace/ta-2-capstone-sistem-cerdas/collection/20655682-3730c561-f9aa-4a39-ba97-257a9b05d8e7?action=share&creator=20655682&active-environment=20655682-89fb83a9-f639-48b0-b6a7-15371a5a743f) - Postman Collection for API testing

### Deployment

- [Cloud Run](https://ta-2-sistem-cerdas-be-vi2jkj4riq-et.a.run.app)