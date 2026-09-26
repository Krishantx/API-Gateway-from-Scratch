# API Gateway

Spring Boot (Java 21) microservice gateway with config-driven dynamic routing, JWT authentication, and rate limiting delegated to a separate rate-limiter service.

## Pipeline

Request flow through the gateway:

1. **Correlation ID** — assigns a UUID, populates MDC, exposes `X-Corelation-Id` on the response
2. **Validation** — rejects requests missing headers required by the route (`requiredHeaders`)
3. **Authentication** — for `authRequired` routes, verifies the `Authorization: Bearer <JWT>` (HS256, 30-min TTL) → 401 on bad/missing token
4. **Rate limit** — calls `rlaas:8085/check` before forwarding (429 on throttle, 503 if rate limiter unreachable)
5. **Discovery + routing** — resolves the target from Service Discovery (Eureka) and forwards to the registered instance

## Routes (`src/main/resources/routes.yml`)

| Path | Service | Methods | x-api-key | JWT required |
|---|---|---|---|---|
| `/profile` | SAMPLE-MICROSERVICE-1 | GET, POST, DELETE | yes | yes |
| `/product` | SAMPLE-MICROSERVICE-2 | GET | no | no |
| `/order` | SAMPLE-MICROSERVICE-3 | GET | no | no |
| `/authenticate` | SAMPLE-MICROSERVICE-1 | POST | no | no |

## Run

Runs as part of the Docker Compose topology (gateway `:8080`, discovery `:8081`, samples `:8082`-`:8084`, rate limiter `:8085`):

```sh
cd Microservice-Architecture
docker compose up --build -d
```

## Test

Run the suite + coverage in a container (host Maven is broken):

```sh
docker run --rm -v "$PWD":/app -w /app -v ~/.m2:/root/.m2 maven:3.9.9-eclipse-temurin-21 mvn test jacoco:report
```

Status as of 2026-09-26: **69 tests green, JaCoCo 100%** (instruction 915/915, branch 58/58, line 270/270).

## Load test

`stress-script.js` + results: [docs/load-test-results.md](docs/load-test-results.md).

Summary: ~270 authenticated req/s sustained at p95 ≈ 94 ms with 0 errors; 401 rejection p95 ≈ 5 ms.

```sh
k6 run --summary-export=./k6-summary.json stress-script.js
```

## TODOs before public push

- Externalize secrets — JWT signing key (`JwtService`), `rlaas.apikey` (`application.properties`) → environment variables
- CI workflow (GitHub Actions: `mvn package` + coverage gate)
- Decide rate-limit integration mode (`FilterConfig` rate-limit registration is currently commented; filter is auto-registered at runtime)