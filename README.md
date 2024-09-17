# RedDoctor

### Microservices and Cloud-Native Applications with Java

Welcome to the RedDoctor, a **microservice and cloud-native application** repository!
This is a non-commercial, personal repository provided by [Iman Salehi](https://github.com/j-imsa) to help junior
developers learn how to build microservices and cloud-native applications using Java and related technologies.

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Setup](#setup)
- [Usage](#usage)
- [Project Structure](#project-structure)
- [Contributing](#contributing)
- [License](#license)

## Overview

In this repository, you'll learn the essential concepts of microservices architecture and cloud-native development in
action.
We'll explore topics like service communication, resilience, scalability, containerization, and deployment on cloud
platforms according to
the [12 Factors](https://12factor.net/) ([Link2](https://developer.ibm.com/articles/creating-a-12-factor-application-with-open-liberty/))
and [15 Factors](https://developer.ibm.com/articles/15-factor-applications/) methodologies.
By following along, you'll get hands-on experience with real-world scenarios and best practices based on a simple
challenge, which you can find [here](CHALLENGE.md) and read more about its logic and business.

## Features

- Challenge 1: Creating a basic microservice
    - Spring Boot
    - Restful best practices
    - DB connection
        - Local - in-memory like H2
        - Local
            - SQL-based: MySQL, PostgreSQL, ...
            - NoSQL-based: Redis, MongoDB, ...
        - Dockerized
    - Repository
        - Spring Data JPA
        - Spring Data Rest
    - Service
        - Mapper
            - Manual
            - [ModelMapper](https://modelmapper.org/)
            - [MapStruct](https://mapstruct.org/)
    - DTO
        - [Data Transfer Object](https://martinfowler.com/eaaCatalog/dataTransferObject.html)
    - Exceptions
    - Validation
    - Audit
    - Document
    - Logger
        - Aspect
            - @annotation
            - execution
    - Annotation
        - [Lombok](https://projectlombok.org/)
        - [Vavr](https://vavr.io/)
    - Test
        - Unit Test
        - Integration Test
        - Testcontainers
        - End-To-End Testing


- Challenge 2: The microservice sizing

- Challenge 3: Docker comes!

- Challenge 4: Cloud Native Apps

- Challenge 5: Configurations

- Challenge 6: Databases

- Challenge 7: Discovery service

- Challenge 8: Gateway

- Challenge 9: Resilient

- Challenge 10: Observability

- Challenge 11: Security

- Challenge 12: Event-Driven
    - Challenge 12.1: RabbitMQ
    - Challenge 12.2: Kafka

- Challenge 13: Kubernetes(K8s)
    - Challenge 13.1: Helm
    - Challenge 13.2: Service discovery
    - Challenge 13.3: Load balancing
    - Challenge 13.4: Deploying
    - Challenge 13.5: Ingress

## Technologies Used

- **Java 17+**: The main programming language.
- **Spring Boot**: For building microservices.
- **Spring Cloud**: For service discovery, configuration management, and load balancing.
- **Docker**: For containerization of applications.
- **Kubernetes**: To orchestrate and manage containerized applications.
- **Kafka/RabbitMQ**: For event-driven communication between services.
- **MySQL/PostgreSQL**: For data persistence.
- **Redis**: For caching.
- **Prometheus & Grafana**: For monitoring and visualization.
- **Jenkins**: For continuous integration and deployment.

## Setup

1. **Clone the repository:**

   ```bash
   git clone https://github.com/j-imsa/RedDoctor.git
   cd RedDoctor
   ```

2. **Prerequisites:**
    - Java 17+
    - Docker and Docker Compose
    - Maven
    - Access to a cloud platform (AWS, GCP, or Azure)

3. **Build the project:**

   ```bash
   mvn clean install
   ```

4. **Run the application locally using Docker Compose:**

   ```bash
   docker-compose up
   ```

5. **Access the services:**
    - API Gateway: `http://localhost:8080`
    - Monitoring (Prometheus): `http://localhost:9090`
    - Monitoring (Grafana): `http://localhost:3000`
    - Actuator endpoints: `http://localhost:8088/api-docs`
    - Swagger UI: `http://localhost:8088/docs-ui`
    - Requests:
      - Environment: [DoctorApp Environment.postman_environment.json](DoctorApp%20Environment.postman_environment.json)
      - Collection: [DoctorApp Collection.postman_collection.json](DoctorApp%20Collection.postman_collection.json)

## Usage

Each microservice in this project is designed to perform a specific function. You'll find services such as:

- **Doctor Service:** Manages doctor-related operations.
- **User Service:** Manages user-related operations.
- **Appointment Service:** Handles orders and their processing.
- **Inventory Service:** Tracks inventory levels and availability.

You can use the APIs provided by each service to simulate interactions and learn how microservices communicate in a
distributed system.

## Project Structure

```
├── doctor-service
├── appointment-service
│   └── patient-service
├── user-service  
├── inventory-service
├── api-gateway
├── config-service
├── discovery-service
├── docker-compose.yml
└── README.md
```

- **doctor-service**: Manages doctors.
- **user-service**: Manages users.
- **inventory-service**: Tracks inventory.
- **api-gateway**: Routes requests to appropriate services.
- **config-service**: Centralized configuration for all services.
- **discovery-service**: Service discovery using Eureka.

## Contributing

Contributions are welcome! If you'd like to contribute, please follow these steps:

1. Fork the repository.
2. Create a new feature branch.
3. Make your changes.
4. Submit a pull request.

Please ensure your code follows best practices and is well-documented.

## License

This project is licensed under the Apache License, Version 2.0 - see the [LICENSE](https://www.apache.org/licenses/LICENSE-2.0) file for details.


