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

### To get csrf token, send a GET request to `/login` to set the

`XSRF-TOKEN` cookie, then extract the token value from the cookie for use in subsequent requests:

`.csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())` is used to set the XSRF-TOKEN as raw value in the
cookie, so you can directly use the cookie value as the token in the `X-XSRF-TOKEN` header.

```bash
curl --location 'http://localhost:8080/login'
```

### To login using Postman or curl, send a POST request to `/login`:

```bash
curl --location 'http://localhost:8080/login' \
--header 'X-XSRF-TOKEN: 17e123d0-fe58-48ad-a192-1af2327d41ea' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'username=admin' \
--data-urlencode 'password=123123'
```

### To access protected endpoints, include the `X-XSRF-TOKEN` header with the token value obtained from the

```bash
curl --location --request POST 'http://localhost:8080/api/messages' \
--header 'X-XSRF-TOKEN: 17e123d0-fe58-48ad-a192-1af2327d41ea' \
--data ''
```

## Frontend Integration

This project also includes a simple frontend built with React that interacts with the backend API, located in the
`frontend` directory.
This frontend application is designed to run inside Spring Boot using static files. To set up the frontend, navigate to
the `frontend` directory and install the dependencies, build the React application to generate the static files, then
copy the generated static files to the `src/main/resources/static` directory of the Spring Boot application. You can use
the following commands to do this:

```bash
rm -rf src/main/resources/static/index.html src/main/resources/static/assets
cd frontend && npm install && npm run build
cp -r dist/* ../src/main/resources/static/
```