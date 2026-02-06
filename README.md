# Flight Management System

A full-stack application for managing clients, airports, airlines, flights, and bookings.

## 🚀 Technologies

| Layer | Technology |
|-------|------------|
| Backend | Spring Boot 3.0.4, Hibernate, JPA |
| Frontend | Angular |
| Database | PostgreSQL (dev) / H2 (test) |
| API | REST |
| Documentation | Swagger/OpenAPI |
| Architecture | Clean Architecture (Hexagonal) |
| Testing | JUnit, Jasmine/Karma |

## 📋 Features

- **Client Management**: Create, update, activate/deactivate clients
- **Airport Management**: Manage airports with open/close status
- **Airline Management**: Handle airlines with activate/suspend functionality
- **Flight Management**: Create flights and manage their status (scheduled, in_flight, arrived, cancelled)
- **Booking System**: Create and manage flight bookings with confirmation/cancellation

## 🏃‍♂️ Quick Start

### Prerequisites
- Java 17+
- Node.js 14+
- PostgreSQL (for development)
- Maven 3.6+

### Backend Setup
```bash
cd springboot-backend

# Set environment variables
export DB_URL=jdbc:postgresql://localhost:5432/flights_db
export DB_USER=your_username
export DB_PASSWORD=your_password

# Run application
mvn clean install
mvn spring-boot:run
```

### Frontend Setup
```bash
cd angular-frontend
npm install
ng serve
```

### Access
- **Frontend**: http://localhost:4200
- **Backend API**: http://localhost:8080/api/v1
- **Swagger UI**: http://localhost:8080/swagger-ui.html

## 📚 API Endpoints

### Airlines (`/api/v1/airlines`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/` | List all airlines |
| GET | `/{id}` | Get airline by ID |
| GET | `/iata/{iataCode}` | Get airline by IATA Code |
| GET | `/{id}/flights` | Get flights by airline |
| POST | `/` | Create new airline |
| PUT | `/{id}/name` | Update airline name |
| PUT | `/{id}/activate` | Activate airline |
| PUT | `/{id}/suspend` | Suspend airline |

### Airports (`/api/v1/airports`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/` | List all airports |
| GET | `/{id}` | Get airport by ID |
| GET | `/iata/{iataCode}` | Get airport by IATA Code |
| GET | `/{id}/flights` | Get flights by airport |
| POST | `/` | Create new airport |
| PUT | `/{id}/name` | Update airport name |
| PUT | `/{id}/open` | Open airport |
| PUT | `/{id}/close` | Close airport |

### Clients (`/api/v1/clients`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/` | List all clients |
| GET | `/{id}` | Get client by ID |
| GET | `/cpf/{cpf}` | Get client by CPF |
| GET | `/{id}/bookings` | Get bookings by client |
| POST | `/` | Create new client |
| PUT | `/{id}/name` | Update client name |
| PUT | `/{id}/activate` | Activate client |
| PUT | `/{id}/deactivate` | Deactivate client |

### Flights (`/api/v1/flights`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/` | List all flights |
| GET | `/{id}` | Get flight by ID |
| GET | `/flight-number/{flightNumber}` | Get flight by flight number |
| GET | `/{id}/bookings` | Get bookings by flight |
| POST | `/` | Create new flight |
| PUT | `/{id}/depart` | Mark flight as in_flight |
| PUT | `/{id}/arrive` | Mark flight as arrived |
| PUT | `/{id}/cancel` | Cancel flight |

### Bookings (`/api/v1/bookings`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/` | List all bookings |
| GET | `/{id}` | Get booking by ID |
| POST | `/` | Create new booking |
| PUT | `/{id}/confirm` | Confirm booking |
| PUT | `/{id}/cancel` | Cancel booking |

## 📝 Request Examples

### Create Airline
```json
POST /api/v1/airlines
{
  "iataCode": "AA",
  "airlineName": "American Airlines"
}
```

### Create Airport
```json
POST /api/v1/airports
{
  "iataCode": "JFK",
  "airportName": "John F. Kennedy International Airport",
  "city": "New York"
}
```

### Create Client
```json
POST /api/v1/clients
{
  "cpf": "12345678900",
  "clientFirstName": "John",
  "clientLastName": "Doe"
}
```

### Create Flight
```json
POST /api/v1/flights
{
  "flightNumber": "AA123",
  "airlineId": 1,
  "departureAirportId": 1,
  "arrivalAirportId": 2,
  "departureTime": "2024-12-25T10:00:00",
  "arrivalTime": "2024-12-25T14:00:00"
}
```

### Create Booking
```json
POST /api/v1/bookings
{
  "clientId": 1,
  "flightId": 1,
  "createdAt": "2024-12-20T09:00:00"
}
```

## 🗄️ Database Schema

The application uses the following main entities:
- **Client**: Customer information and status
- **Airport**: Airport details with operational status
- **Airline**: Airline information and status
- **Flight**: Flight details with status tracking
- **Booking**: Reservation linking clients to flights

## 🧪 Testing

### Backend Tests
```bash
cd springboot-backend
mvn test
```

### Frontend Tests
```bash
cd angular-frontend
npm test
```

## 🔧 Development

### Project Structure
```
flights_app/
├── springboot-backend/
│   └── src/main/java/springboot/aviation/
│       ├── domain/              # Business entities & rules
│       │   ├── airline/         # Airline aggregate
│       │   ├── airport/         # Airport aggregate
│       │   ├── booking/         # Booking aggregate
│       │   ├── client/          # Client aggregate
│       │   └── flight/          # Flight aggregate
│       ├── application/         # Use cases & services
│       │   ├── airline/
│       │   │   ├── usecase/     # Business operations
│       │   │   └── service/     # Query services
│       │   ├── airport/
│       │   ├── booking/
│       │   │   └── port/        # Interfaces for external dependencies
│       │   ├── client/
│       │   └── flight/
│       ├── infrastructure/       # External implementations
│       │   ├── persistence/     # Database adapters
│       │   │   ├── airline/
│       │   │   ├── airport/
│       │   │   ├── booking/
│       │   │   ├── client/
│       │   │   └── flight/
│       │   └── mapper/          # Entity ↔ Domain mappers
│       ├── interfaces/          # API adapters
│       │   ├── controller/      # REST controllers
│       │   └── dto/             # Request/Response DTOs
│       ├── exception/           # Exception handling
│       └── messages/            # Internationalization
└── angular-frontend/
    ├── src/app/
    │   ├── airlines/
    │   ├── airports/
    │   ├── bookings/
    │   ├── clients/
    │   ├── flights/
    │   └── shared/
    └── src/environments/
```

### Clean Architecture Layers

#### 1. Domain Layer (Core)
- **Entities**: Business objects (Airline, Airport, Client, Flight, Booking)
- **Repositories**: Interfaces for data access
- **Value Objects**: Status enums and business rules
- **No dependencies**: Pure business logic

#### 2. Application Layer
- **Use Cases**: Business operations (Create, Activate, Suspend, etc.)
- **Query Services**: Read operations
- **Ports**: Interfaces for external services
- **Depends on**: Domain layer only

#### 3. Infrastructure Layer
- **Persistence**: JPA entities and repository implementations
- **Mappers**: Convert between domain and persistence models
- **External Services**: Third-party integrations
- **Depends on**: Domain and Application layers

#### 4. Interfaces Layer
- **Controllers**: REST API endpoints
- **DTOs**: Request/Response objects
- **Exception Handlers**: Global error handling
- **Depends on**: Application layer

### Status Enums
- **Flight Status**: SCHEDULED, IN_FLIGHT, ARRIVED, CANCELLED
- **Booking Status**: CREATED, CONFIRMED, CANCELLED
- **Airline Status**: ACTIVE, SUSPENDED
- **Airport Status**: OPEN, CLOSED
- **Client Status**: ACTIVE, INACTIVE