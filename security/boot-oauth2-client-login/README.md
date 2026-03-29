# Boot OAuth2 Client Login Demo

A simple Spring Boot application demonstrating OAuth2 client login and resource protection using Keycloak as the
authorization server.

## Description

This project is a sandbox for learning Spring Security with OAuth2 integration. It includes basic setup for OAuth2 login
and resource protection. Authorization server will be using Keycloak, and the client application will be a simple Spring
Boot app with a React frontend.

## Prerequisites

- Java 17 or higher
- Gradle 7.0 or higher

## Getting Started

### Clone the Repository

```bash
git clone <repository-url>
cd boot-oauth2
```

### Run the keycloak server

```bash
docker run -p 9000:8080 \
-e KC_BOOTSTRAP_ADMIN_USERNAME=admin \
-e KC_BOOTSTRAP_ADMIN_PASSWORD=admin \
quay.io/keycloak/keycloak:latest start-dev
```

### Create a realm, client, and user in Keycloak for testing.

1. Access Keycloak admin console at `http://localhost:9000/auth/admin/`
2. Create a new realm: myrealm
3. Create a new client: myclient1
    - Set Valid Redirect URIs to `http://localhost:8080/*`
    - Set Web Origins to `http://localhost:8080`
4. Create a new user: testuser
    - Set a password for the user

### Build the React app into static folder

```bash
rm -rf src/main/resources/static/index.html src/main/resources/static/assets
cd frontend && npm install && npm run build
cp -r dist/* ../src/main/resources/static/
```

### Run the Application

```bash
./gradlew bootRun
```

The application will start on `http://localhost:8080`.

## Configuration

OAuth2 configuration can be found in `src/main/resources/application.yaml`.

## Usage

- Access the application at `http://localhost:8080`
- Protected endpoints require authentication via OAuth2
- The type of Authentication in the security context after a request is successfully authorized is OAuth2AuthenticationToken. 
- The OAuth2AuthenticationToken principal is an OidcUser built from the ID token.