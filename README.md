# Auth Service

A REST API for authentication built with Java and Spring Boot, featuring user registration, login with JWT token generation, and a complete password recovery flow.

## ✨ Features

- **User registration** with data validation and encrypted passwords (BCrypt)
- **Login** with credential authentication and JWT token generation
- **Password recovery** via temporary token with expiration
- Public and protected routes configured with Spring Security

## 🛠️ Tech Stack

- **Java 21**
- **Spring Boot 4.1.1**
  - Spring Web
  - Spring Security
  - Spring Data JPA
  - Validation
- **PostgreSQL** (running via Docker)
- **JJWT** (io.jsonwebtoken) — JWT token generation and validation
- **BCrypt** — password hashing
- **Gradle**

## 📋 Prerequisites

- JDK 21
- Docker
- An API testing tool (Postman, Insomnia, etc.)

## ⚙️ Setup and Running

### 1. Clone the repository

```bash
git clone https://github.com/gustbaccas/auth-service.git
cd auth-service
```

### 2. Start the PostgreSQL database with Docker

```bash
docker run --name postgres-auth -e POSTGRES_PASSWORD=yourpassword -e POSTGRES_DB=authdb -p 5432:5432 -d postgres
```

### 3. Set the JWT environment variable

Generate a secure secret key (minimum 256 bits in Base64):

```bash
openssl rand -base64 32
```

Set this key as the `JWT_SECRET` environment variable before running the application (e.g., in your IDE's run configuration, or exported in the terminal).

### 4. Adjust `application.properties`

Make sure the database credentials in `src/main/resources/application.properties` match the ones used in step 2:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/authdb
spring.datasource.username=postgres
spring.datasource.password=yourpassword
```

### 5. Run the application

```bash
./gradlew bootRun
```

The API will be available at `http://localhost:8080`.

## 📡 Endpoints

All endpoints are under the `/auth` prefix.

### Register

```
POST /auth/register
```

**Body:**
```json
{
  "email": "user@email.com",
  "password": "password123"
}
```

**Response (201):**
```json
{
  "id": 1,
  "email": "user@email.com"
}
```

### Login

```
POST /auth/login
```

**Body:**
```json
{
  "email": "user@email.com",
  "password": "password123"
}
```

**Response (200):**
```json
{
  "id": 1,
  "email": "user@email.com",
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

### Forgot Password

```
POST /auth/forgot-password
```

**Body:**
```json
{
  "email": "user@email.com"
}
```

**Response (200):**
```
"If this email exists, password reset instructions have been sent."
```

> For security reasons, the response is always the same, regardless of whether the email exists in the database or not.

### Reset Password

```
POST /auth/reset-password
```

**Body:**
```json
{
  "token": "received-token",
  "newPassword": "newPassword123"
}
```

**Response (200):**
```
"Password has been reset successfully."
```

## 🏗️ Project Structure

```
src/main/java/io/github/gustbaccas/auth_service/
├── config/          # Security configuration (SecurityConfig)
├── controller/       # REST endpoints (AuthController)
├── dto/              # Data transfer objects (Request/Response)
├── entity/           # JPA entities (User, PasswordResetToken)
├── enums/            # Enumerations (Role)
├── repository/       # Data access interfaces (JpaRepository)
└── service/          # Business logic (UserService, JwtService)
```

## 🔒 Security

- Passwords are never stored in plain text — always hashed with BCrypt
- JWT tokens are signed with a secret key provided via environment variable (never hardcoded)
- Password reset tokens expire after 30 minutes and are tied to a single user
- Password recovery responses don't reveal whether an email is registered, preventing user enumeration

## 📌 Roadmap

- [ ] Validate JWT tokens on protected routes via a security filter
- [ ] Real email sending for password recovery (Spring Mail)
- [ ] Custom exception handling (clearer error responses)
