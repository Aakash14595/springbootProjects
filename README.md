# User Management API

Spring Boot user management API with Oracle persistence, JWT authentication, and Swagger documentation.

## Requirements

- Java 17
- Oracle XE for local development, or a compatible Oracle database for production
- Maven (or the included Maven Wrapper)

## Spring profiles

The default profile is `dev`, which connects to local Oracle XE at `localhost:1521:XE` using the local development defaults. Override these defaults with environment variables when needed:

| Variable | Development default | Purpose |
| --- | --- | --- |
| `DEV_DB_URL` | `jdbc:oracle:thin:@localhost:1521:XE` | Local Oracle JDBC URL |
| `DEV_DB_USERNAME` | `system` | Local database username |
| `DEV_DB_PASSWORD` | Required | Local database password |
| `JWT_SECRET` | Required | JWT signing secret; use a unique random value |
| `SERVER_PORT` | `9090` | HTTP port |

Run locally:

```powershell
$env:SPRING_PROFILES_ACTIVE = "dev"
$env:DEV_DB_PASSWORD = "<your local Oracle password>"
$env:JWT_SECRET = "<a unique random secret, at least 32 characters>"
.\mvnw.cmd spring-boot:run
```

For production, activate `prod` and provide the database and JWT secrets through the deployment platform or Jenkins credentials. The production profile requires `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, and `JWT_SECRET`; it uses Hibernate `validate` rather than changing the schema and does not log SQL.

```text
SPRING_PROFILES_ACTIVE=prod
DB_URL=jdbc:oracle:thin:@<host>:<port>/<service>
DB_USERNAME=<application database user>
DB_PASSWORD=<secret from Jenkins credentials>
JWT_SECRET=<long, unique secret from Jenkins credentials>
```

Use a dedicated, least-privilege application schema/user in production, not Oracle `SYSTEM`. Create or migrate the schema before deployment to match the application entities.

## Tests

Tests use the isolated `test` profile and an in-memory H2 database in Oracle compatibility mode; they do not require Oracle XE or production secrets.

```powershell
.\mvnw.cmd test
```

## Jenkins

The root `Jenkinsfile` runs the Maven verification/build and archives the executable JAR. It provides an `APP_ENV` parameter (`dev` or `prod`) as `SPRING_PROFILES_ACTIVE` for the job. Configure `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, and `JWT_SECRET` as Jenkins credentials/environment variables for the production runtime; do not put secrets in the repository or the pipeline file. The pipeline expects a Linux Jenkins agent with Java 17.

The pipeline builds and publishes the artifact but does not deploy it to a server: deployment needs the target host/platform, service restart method, and credential IDs to be supplied for your infrastructure. Run the packaged JAR with `SPRING_PROFILES_ACTIVE=dev` or `SPRING_PROFILES_ACTIVE=prod` on the target.

## API

- `POST /api/users/user` — create a user
- `POST /api/users/authenticate` — authenticate and receive a JWT
- `GET /api/users` — list users (JWT required)
- `DELETE /api/users/{id}` — delete a user (JWT required)
- Swagger UI: `/swagger-ui/index.html`
