# Spring Boot Form Login Security Demo

This is a Spring Boot application that demonstrates form-based authentication using Spring Security. It includes CSRF
protection, role-based access control.

## Prerequisites

- Java 25
- Gradle (or use the included Gradle wrapper)

## Running the Application

1. Clone or navigate to the project directory.
2. Run the application using Gradle:

   ```bash
   ./gradlew bootRun
   ```

3. The application will start on `http://localhost:8080`.

## Endpoints

### Public Endpoints

- `GET /public/ping` - Health check endpoint
- `GET /csrf` - Set CSRF-TOKEN and Retrieve CSRF token

### Authentication Endpoints

- `GET /login` - Login form (default Spring Security login page)
- `POST /login` - Process login (handled by Spring Security)
- `POST /logout` - Logout (handled by Spring Security)

### Protected Endpoints

- `GET /api/messages` - Requires authentication (USER or ADMIN role)
- `POST /api/messages` - Requires ADMIN role

## Users

The application uses in-memory user details with the following accounts:

- **Username:** `user`, **Password:** `123123`, **Role:** USER
- **Username:** `admin`, **Password:** `123123`, **Role:** ADMIN

## Security Configuration

- Form-based login with custom success and logout handlers
- CSRF protection enabled with cookie-based token repository
- Method-level security enabled using `@PreAuthorize` annotations
- For every request, client must include the CSRF token in the `X-XSRF-TOKEN` header, which can be obtained from the
  `XSRF-TOKEN` cookie set by the application.

## Technologies Used

- Spring Boot 4.0.3
- Spring Security

## Testing

You can test the application using tools like Postman or curl. Make sure to include the CSRF token in your requests to
idempotently access protected endpoints such as 'POST, PUT, DELETE' etc. JSESSIONID cookie and XSRF-TOKEN cookie will be
set in requests automatically by Postman. If you are using curl, you can manually include the cookies in the header.

### To get csrf token, send a GET request to `/csrf` and get the `token` from response body.

```bash
curl --location 'http://localhost:8080/csrf'
```

### To login using Postman or curl, send a POST request to `/login`:

```bash
curl --location 'http://localhost:8080/login' \
--header 'X-XSRF-TOKEN: VFnkXPY2HSlDqrfhlrNRCKgAqOtAudidtoJo1o7eoh5VLGhKMmGCOpUAexFuz4XZoZ5lPM4xhYl5juCwg7YK7ri8lCs3T1Bz' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'username=admin' \
--data-urlencode 'password=123123'
```

### To access protected endpoints, include the `X-XSRF-TOKEN` header with the token value obtained from the

`/csrf` endpoint.

```bash
curl --location --request POST 'http://localhost:8080/api/messages' \
--header 'X-XSRF-TOKEN: CEXoQFCTF0PCW51pnlZmH1SbUW7b-yawyDpYDD4QLzXVzNJibnbdIWCndnDvOvheqXtSfWGufFfqnxCd8Qg-OAwgF1bjqrQE' \
--data ''
```