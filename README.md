# Cash Register

A full-stack cash register application built with Kotlin/Ktor (backend), React Native/Expo (frontend), and PostgreSQL (database).

## Architecture

- **Backend**: Kotlin + Ktor 2.3.7 + Exposed 0.47.0 + PostgreSQL
- **Frontend**: React Native (Expo) with TypeScript
- **Database**: PostgreSQL 16
- **Infrastructure**: Docker Compose

## Features

- Add items to the register (name + price)
- Remove items from the register
- View all items with running total
- Rate limiting (100 req/60s per client)
- HSTS and security headers
- Structured logging with Logback
- Unit and integration tests

## API

| Method | Path           | Description         |
|--------|----------------|---------------------|
| GET    | `/items`       | List all items      |
| POST   | `/items`       | Add a new item      |
| DELETE | `/items/{id}`  | Remove an item      |

### POST /items request body
```json
{ "name": "Apple", "price": "1.99" }
```

## Running with Docker Compose

```bash
docker compose up --build
```

The backend API will be available at `http://localhost:8080`.

## Backend Development

```bash
cd backend
./gradlew test          # Run unit + integration tests
./gradlew shadowJar     # Build fat JAR
```

## Frontend Development

```bash
cd frontend
npm install
npm start               # Start Expo dev server
npm test                # Run Jest tests
```

## Environment Variables

| Variable            | Default                                      | Description           |
|---------------------|----------------------------------------------|-----------------------|
| `PORT`              | `8080`                                       | Backend port          |
| `DATABASE_URL`      | `jdbc:postgresql://localhost:5432/cashregister` | PostgreSQL JDBC URL |
| `DATABASE_USER`     | `postgres`                                   | DB username           |
| `DATABASE_PASSWORD` | `postgres`                                   | DB password           |
| `EXPO_PUBLIC_API_URL` | `http://localhost:8080`                    | Frontend API base URL |