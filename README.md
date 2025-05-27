# Music-service

This project is a REST API built on Java, Spring, PostgreSQL. Spring Security and JWT were used for authentication control.

🎯 Objective: Study microservices, spring boot, JWT, data model and services integration.

## Technologies

- Java 17
- Spring Boot 3.0
- Spring Security
- Spring Data JPA
- Spring Validator
- JWT (JSON Web Token)
- PostgreSQL
- Docker (containerization)

## Features

- Submit a song
- List all songs
- Infinite scroll with pageable song loading
- Like song
- Comment on a song
- Annotate a excerpt of a song
- Logged-in users can view songs they previously liked.

## How to run

### Prerequisites

- [JDK 17](https://www.oracle.com/java/technologies/javase-downloads.html)
- [PostgreSQL](https://www.postgresql.org/download/)
- [Maven](https://maven.apache.org/download.cgi)
- [Docker](https://www.docker.com/) (optional)

### Database configuration

Create a PostgreSQL database and update the `application.properties` file with your credentials:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/your_db
spring.datasource.username=your_user
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
jwt.security.token=your_secret
```

### Running

Clone this repository:

```sh
git clone https://github.com/login_authAPI.git
cd
mvn clean install
mvn spring-boot:run
```

The API will be accessible at `http://localhost:8080`

# Endpoints
### Public Access
`GET` /music/{musicId} - GET a song by ID.

Returns a full song object

Response (200 OK):
```json
{
    "id": "Long",
    "title": "String",
    "artist": "String",
    "album": "String",
    "genre": "String",
    "lyrics": "String",
    "liked": "boolean",
    "likeCount": "int",
    "commentCount": "int"
}
```
If a valid Authorization token is provided, previously liked songs will be marked accordingly.

###
`GET` /music/search?page={}&size={} - Search songs with pagination. 

Returns a paginated list of simplified song data. 

Response (200 OK):
```json
{
    "id": "Long",
    "title": "String",
    "artist": "String",
    "liked": "boolean",
    "likeCount": "int",
    "commentCount": "int"
} 
```
As the previus, a valid token will indicate previously liked songs.

### Authorized access
Requirements:
- Valid registered user;
- ADMIN role;
- `Authorization: Bearer <token>` in request headers;
###
- Errors: 
    - 403 FORBIDDEN (Insufficient permissions);
    - 401 UNAUTHORIZED (expired, missing or invalid token);
###
`GET` /music/list - List all registered songs. 

Response (200 OK):
```json
[
    {
    "id": "Long",
    "title": "String",
    "artist": "String",
    "album": "String",
    "genre": "String",
    "lyrics": "String",
    "liked": "boolean",
    "likeCount": "int",
    "commentCount": "int"
    },
    {...}
]
```
###
`POST` music/publish - Publish a song.

Request Body:

```json
{
    "title": "String",
    "artist": "String",
    "album": "String",
    "genre": "String",
    "lyrics": "String"
}
```