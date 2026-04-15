# Boot Auth0

A Spring Boot 4.0.3 application demonstrating OAuth2 resource server authentication and authorization using Auth0 as the
identity provider.

## Authority Mapping

- Maps Auth0 `permissions` claim to Spring Security authorities
- Automatically prefixes authorities with `SCOPE_`
- Enables method-level security annotations

## Building the Project

```bash
# Clean build
./gradlew clean build
```

## Running the Application

```bash
# Run using Gradle
./gradlew bootRun
```

The application starts on the default port `8080`.

## Authentication Flow

1. **Obtain JWT Token from Auth0**
    - Use your Auth0 credentials to get a JWT token
    - Ensure the token includes the `boot-auth0-api` audience
    - Token should include any required `permissions` claims

2. **Include Token in Request**
    - Add token to Authorization header: `Authorization: Bearer <token>`
    - Spring Security validates the token using the configured JwtDecoder

3. **Token Validation Process**
    - Signature verification (using OIDC public keys)
    - Issuer validation
    - Audience validation
    - Standard claims validation (exp, iat, etc.)

4. **Authority Resolution**
    - Auth0 `permissions` claim is extracted
    - Converted to Spring Security authorities with `SCOPE_` prefix
    - Used for method-level authorization checks

## Example Usage

### 1. Test Public Endpoint

```bash
curl http://localhost:8080/public
# Response: pong
```

### 2. Access Secured Endpoint (Requires Token)

```bash
# Set your Auth0 JWT token
TOKEN="your_auth0_jwt_token_here"

# Access secured endpoint
curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/messages
# Response: Hello from the secured API!
```

### 3. Access Scoped Endpoint (Requires Specific Permission)

```bash
# Token must have 'read:messages' or 'write:messages' permission
curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/messages/scoped
# Response: Hello from the secured API! This is a scoped message.
```
