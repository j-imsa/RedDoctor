# RedDoctor


### Challenge

> [CHALLENGE.md](CHALLENGE.md)


### Document:

> http://localhost:8088/api-docs

> http://localhost:8088/docs-ui


### Requests:

1. Add a day

```shell
curl --location 'http://localhost:8088/v0.9/doctors' \
--header 'Content-Type: application/json' \
--data '{  
    "date": "11-12-2024",
    "start": "11:30:10",
    "end": "12:35:00"
}'
```

2. Doctor can see all appointments

```shell
curl --location 'http://localhost:8088/v0.9/doctors/11-12-2024?page=1&size=15&sort_direction=asc&status=all'
```

3. Doctor can remove an appointment

```shell
curl --location --request DELETE 'http://localhost:8088/v0.9/doctors/_xGtBGXubs1yZucqNt8cKb_DJezaZ-xv-u7Ken-8VzhmBiQXqjaqkiy3M4v0jPa_dd'
```

4. Patient can see available appointments

```shell
curl --location 'http://localhost:8088/v0.9/doctors/11-12-2024?status=open&page=1&size=15&sort_direction=asc'
```

5. Patient can take an appointment

```shell
curl --location 'http://localhost:8088/v0.9/patients/i625bsvXoeAwTFiPGd6Huo4gGIXgCqkMtuF1rwPMTN_sUiDq4kuHgJWIljKc715O' \
--header 'Content-Type: application/json' \
--data '{
    "name": "Foo bar",
    "phone_number": "9131231234"
}'
```

6. Patient can see his/her appointments

```shell
curl --location 'http://localhost:8088/v0.9/patients/9131231234'
```



### Postman imports

> Environment: [DoctorApp Environment.postman_environment.json](DoctorApp%20Environment.postman_environment.json)

> Collection: [DoctorApp Collection.postman_collection.json](DoctorApp%20Collection.postman_collection.json)


### To-Do

- [ ] Concurrency check
  - Locking
    - [ ] Optimistic Locking
    - [ ] Pessimistic Locking
  - Transactional Boundaries
    - [ ] @Transactional

- [x] Status
  - [x] OPEN
  - [x] TAKEN
  - [x] DELETED

- [ ] Docker
  - [Google Jib](https://cloud.google.com/java/getting-started/jib)
  - Compose
    - [ ] MySQL

- [ ] Code Coverage
  - [JaCoCo](https://www.jacoco.org/jacoco/trunk/index.html)

- [ ] Testcontainers
  - [Link](https://java.testcontainers.org/)
