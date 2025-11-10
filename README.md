# OptiRoute 🚚

A sophisticated route optimization system built with Spring Boot that implements advanced algorithms to optimize delivery routes for logistics companies.

## 📋 Table of Contents

- [Overview](#overview)
- [SOLID Principles - Open/Closed Principle (OCP)](#solid-principles---openclosed-principle-ocp)
- [Design Patterns](#design-patterns)
- [Architecture](#architecture)
- [Technologies](#technologies)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Running the Project](#running-the-project)
- [API Documentation](#api-documentation)
- [Testing](#testing)
- [License](#license)

## 🎯 Overview

**OptiRoute** is a delivery route optimization platform that helps logistics companies plan efficient delivery routes. The system considers various constraints such as vehicle capacity (weight and volume), warehouse operating hours, and delivery locations to calculate optimal tours using sophisticated algorithms.

### Key Features

- **Route Optimization**: Multiple algorithms (Nearest Neighbor, Clarke-Wright Savings, AI-Powered)
- **AI-Powered Optimization**: Deep learning integration using DeepSeek AI for intelligent route planning based on historical delivery patterns
- **Multi-Vehicle Support**: Different vehicle types (Van, Truck, Bike) with varying capacities
- **Customer Management**: Customer profiles with preferred delivery time slots
- **Warehouse Management**: Multiple warehouses with operating hours
- **Delivery Tracking**: Real-time delivery status monitoring with historical data
- **Delivery History Analytics**: Tracks delivery performance, delays, and patterns for AI learning
- **RESTful API**: Complete CRUD operations for all entities
- **Database Migrations**: Liquibase for version-controlled schema changes
- **API Documentation**: Interactive Swagger/OpenAPI documentation
- **HashiCorp Vault Integration**: Secure secrets management for API keys and credentials

## 🏗️ SOLID Principles - Open/Closed Principle (OCP)

The **Open/Closed Principle** states that *"Software entities should be open for extension but closed for modification."* OptiRoute demonstrates this principle through several implementations:

### 1. Strategy Pattern for Route Optimization

The system uses the **Strategy Pattern** to implement different route optimization algorithms without modifying existing code:

```
TourOptimizer (Interface)
    ├── NearestNeighborOptimizer
    ├── ClarkeWrightOptimizer
    └── AIOptimizer (DeepSeek-powered)
```

**How it implements OCP:**
- **Closed for modification**: The `TourOptimizer` interface defines a contract that remains stable
- **Open for extension**: New optimization algorithms can be added by simply implementing the `TourOptimizer` interface
- **Benefit**: Adding new algorithms (e.g., Genetic Algorithm, Ant Colony Optimization, or AI-based optimizers like our AIOptimizer using DeepSeek) requires no changes to existing code

**Recent Extension Example:**
The `AIOptimizer` was added to leverage DeepSeek AI for route optimization based on historical delivery data without modifying the existing `NearestNeighborOptimizer` or `ClarkeWrightOptimizer` implementations.

### 2. Service Layer Abstraction

The application separates business logic into service interfaces and implementations:
- Domain services define contracts
- Application services implement business logic
- Controllers depend on abstractions, not concrete implementations

### 3. Entity Inheritance and Polymorphism

Value Objects and Embedded types (like `Coordinates`, `Address`) can be extended with new attributes without modifying the entities that use them.

### 4. Repository Pattern

JPA repositories provide data access abstraction, allowing the underlying persistence mechanism to change without affecting business logic.

## 🎨 Design Patterns

### 1. **Strategy Pattern** ⭐
**Location**: `infrastructure.strategy` package

**Purpose**: Encapsulates different route optimization algorithms and makes them interchangeable.

**Implementation**:
- `TourOptimizer` interface defines the contract
- `NearestNeighborOptimizer`: Greedy algorithm that always picks the nearest unvisited delivery
- `ClarkeWrightOptimizer`: Savings algorithm that merges routes to maximize efficiency
- `AIOptimizer`: AI-powered optimizer using DeepSeek API that learns from historical delivery patterns

**Benefit**: Algorithms can be swapped at runtime based on requirements (speed vs. optimization quality vs. AI-powered intelligence).

### 2. **Repository Pattern**
**Location**: `domain.repository` package

**Purpose**: Abstracts data access logic and provides a collection-like interface for accessing domain objects.

**Benefit**: Decouples business logic from data access, making the code testable and maintainable.

### 3. **DTO Pattern**
**Location**: `presentation.dto` package

**Purpose**: Separates internal domain models from external API representations.

**Benefit**: Prevents exposure of internal domain structure and allows API evolution independently.

### 4. **Service Layer Pattern**
**Location**: `application.service` and `domain.service` packages

**Purpose**: Encapsulates business logic and orchestrates domain operations.

**Benefit**: Keeps controllers thin and business logic reusable across different presentation layers.

### 5. **Value Object Pattern**
**Location**: `domain.vo` package

**Purpose**: Represents domain concepts without identity (e.g., `Coordinates`, `Address`).

**Benefit**: Ensures immutability and encapsulates validation logic for complex attributes.

### 6. **Factory Pattern**
**Location**: Implicitly used through Spring's dependency injection and builder patterns

**Purpose**: Creates complex objects without exposing instantiation logic.

**Benefit**: Simplifies object creation and promotes loose coupling.

## 🏛️ Architecture

OptiRoute follows a **Clean Architecture** / **Hexagonal Architecture** approach with clear separation of concerns:

```
┌─────────────────────────────────────────────────────────────┐
│                     Presentation Layer                       │
│  (Controllers, DTOs, Request/Response Handling)              │
│  └─ presentation/controller                                  │
│  └─ presentation/dto                                         │
│  └─ presentation/advice (Global Exception Handling)          │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│                     Application Layer                        │
│  (Use Cases, Business Logic Orchestration)                   │
│  └─ application/service                                      │
│  └─ application/mapper (DTO ↔ Domain mapping)               │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│                       Domain Layer                           │
│  (Core Business Logic, Entities, Value Objects)              │
│  └─ domain/entity (Delivery, Tour, Vehicule, WareHouse)     │
│  └─ domain/service (Domain business rules)                   │
│  └─ domain/repository (Data access interfaces)               │
│  └─ domain/vo (Value Objects: Coordinates, Address)         │
│  └─ domain/enums (DeliveryStatus, VehicleType)              │
│  └─ domain/exception (Domain exceptions)                     │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│                    Infrastructure Layer                      │
│  (Technical Implementation Details)                          │
│  └─ infrastructure/config (Spring configuration)             │
│  └─ infrastructure/strategy (Route optimization algorithms)  │
│  └─ infrastructure/logging (Logging aspects)                 │
└─────────────────────────────────────────────────────────────┘
```

### Layer Responsibilities

#### 1. **Presentation Layer**
- Handles HTTP requests/responses
- Validates input data
- Transforms DTOs to domain objects
- API documentation (OpenAPI/Swagger)

#### 2. **Application Layer**
- Orchestrates use cases
- Coordinates domain services
- Transaction management
- Maps between DTOs and domain models

#### 3. **Domain Layer** (Core)
- Contains business entities and rules
- Pure business logic (framework-agnostic)
- Defines repository interfaces
- Domain exceptions and validations

#### 4. **Infrastructure Layer**
- Implements technical concerns
- Database access (JPA/Hibernate)
- External service integrations
- Configuration and cross-cutting concerns

### Key Domain Entities

```
WareHouse (1) ──────→ (N) Tour
    ↓ has                    ↓ has
Address                 Vehicule (1) ──→ (N) Tour
                             ↓
                        VehicleType
                        
Tour (1) ──────────→ (N) Delivery ←────── (N) Customer
                         ↓ has                 ↓ has
                     Coordinates          PreferredTimeSlot
                         ↓ has                 ↓ has
                    DeliveryStatus           Address
                         ↓
                         │
                    (1) Delivery ──→ (N) DeliveryHistory
                                            ↓ tracks
                                     Performance Metrics
```

## 🛠️ Technologies

| Category | Technology | Version |
|----------|-----------|---------|
| **Language** | Java | 21 |
| **Framework** | Spring Boot | 3.5.6 |
| **Persistence** | Spring Data JPA | 3.5.6 |
| **Database** | H2 (Development) / PostgreSQL (Production) | 2.2.224 / 42.7.3 |
| **Migration** | Liquibase | 4.31.1 |
| **AI Integration** | Spring AI (DeepSeek) | 1.0.3 |
| **Secrets Management** | HashiCorp Vault | 4.2.1 |
| **Documentation** | SpringDoc OpenAPI | 2.2.0 |
| **Build Tool** | Maven | 3.9.6+ |
| **Container** | Docker | - |
| **Testing** | JUnit, Spring Boot Test | - |
| **Utilities** | Lombok | - |
| **Data Generation** | JavaFaker | 1.0.2 |

## 📦 Prerequisites

Before running the project, ensure you have the following installed:

- **Java 21** or higher ([Download](https://adoptium.net/))
- **Maven 3.6+** (or use the included Maven wrapper)
- **Docker** and **Docker Compose** (optional, for containerized deployment)
- **Git** (for cloning the repository)

## 🚀 Getting Started

### Clone the Repository

```bash
git clone <repository-url>
cd OptiRoute
```

### Configure Application

1. Copy the example properties file:
```bash
cp src/main/resources/application-example.properties src/main/resources/application.properties
```

2. Update database configuration if needed in `application.properties`

### Configure AI Optimization (Optional)

The application uses DeepSeek AI for intelligent route optimization. To enable this feature:

1. **Set up HashiCorp Vault** (recommended for production):
   - The application uses Vault to securely store the DeepSeek API key
   - Initialize Vault using the provided script:
   ```bash
   ./vault-init/init-secrets.sh
   ```
   - Store your DeepSeek API key in Vault under the path configured in `bootstrap.yml`

2. **Or use environment variables** (development):
   ```bash
   export DEEPSEEK_APIKEY=your-api-key-here
   ```

3. **Configure optimizer strategy** in `application.yml`:
   ```yaml
   optimize:
     strategy: ai  # Options: ai, nearest-neighbor, clarke-wright
   ```


### Build the Project

Using Maven wrapper:
```bash
./mvnw clean install
```

Or using system Maven:
```bash
mvn clean install
```

## 🏃 Running the Project

### Option 1: Using Maven (Development)

```bash
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`

### Option 2: Using Docker (Recommended for Production)

#### Build the Docker Image

```bash
docker build -t optiroute:latest .
```

#### Run the Container

```bash
docker run -p 8080:8080 optiroute:latest
```

#### Using Docker Compose (if available)

```bash
docker-compose up -d
```

### Option 3: Run the JAR Directly

```bash
java -jar target/optiroute-0.0.1-SNAPSHOT.jar
```

## 📚 API Documentation

Once the application is running, access the interactive API documentation:

### Swagger UI
```
http://localhost:8080/swagger-ui.html
```

### OpenAPI Specification
```
http://localhost:8080/v3/api-docs
```

### H2 Database Console (Development)
```
http://localhost:8080/h2-console
```
- **JDBC URL**: `jdbc:h2:~/test`
- **Username**: `sa`
- **Password**: (leave empty)

## 🔗 API Endpoints

### Warehouse Management
- `GET /warehouse` - List all warehouses
- `POST /warehouse` - Create a new warehouse
- `GET /warehouse/{id}` - Get warehouse by ID
- `PUT /warehouse/{id}` - Update warehouse
- `DELETE /warehouse/{id}` - Delete warehouse

### Tour Management
- `GET /tours` - List all tours
- `POST /tours` - Create a new tour
- `PUT /tours/{id}` - Update tour
- `DELETE /tours/{id}` - Delete tour
- `POST /optimize` - Run the Optimizer

### Vehicle Management
- `GET /vehicule` - List all vehicles
- `POST /vehicule` - Create a new vehicle
- `GET /vehicule/{id}` - Get vehicle by ID
- `PUT /vehicule/{id}` - Update vehicle
- `DELETE /vehicule/{id}` - Delete vehicle

### Delivery Management
- `GET /deliveries` - List all deliveries
- `POST /deliveries` - Create a new delivery
- `GET /deliveries/{id}` - Get delivery by ID
- `PUT /deliveries/{id}` - Update delivery status
- `DELETE /deliveries/{id}` - Delete delivery

### Customer Management
- `GET /customers` - List all customers
- `POST /customers` - Create a new customer
- `GET /customers/{id}` - Get customer by ID
- `PUT /customers/{id}` - Update customer
- `DELETE /customers/{id}` - Delete customer

### Delivery History
- `GET /delivery-history` - List delivery history records
- `GET /delivery-history/{id}` - Get specific history record
- Analytics endpoints for AI learning

## 🧪 Testing

### Run All Tests

```bash
./mvnw test
```

### Run Specific Test Class

```bash
./mvnw test -Dtest=OptirouteApplicationTests
```

### Generate Test Coverage Report

```bash
./mvnw clean test jacoco:report
```

## 📂 Project Structure

```
optiroute/
├── src/
│   ├── main/
│   │   ├── java/com/optiroute/optiroute/
│   │   │   ├── application/           # Application services & mappers
│   │   │   │   ├── service/           # Use case implementations
│   │   │   │   ├── mapper/            # DTO ↔ Domain mapping
│   │   │   │   └── helper/            # Application helpers
│   │   │   ├── domain/                # Domain entities & business logic
│   │   │   │   ├── entity/            # Core entities (Delivery, Tour, etc.)
│   │   │   │   ├── service/           # Domain services
│   │   │   │   ├── repository/        # Data access interfaces
│   │   │   │   ├── vo/                # Value Objects (Coordinates, Address)
│   │   │   │   ├── enums/             # Domain enumerations
│   │   │   │   ├── event/             # Domain events
│   │   │   │   └── exception/         # Domain exceptions
│   │   │   ├── infrastructure/        # Technical implementations
│   │   │   │   ├── config/            # Spring configuration
│   │   │   │   ├── strategy/          # Route optimization algorithms
│   │   │   │   ├── logging/           # Logging aspects
│   │   │   │   └── seeder/            # Database seeding utilities
│   │   │   ├── presentation/          # Controllers & DTOs
│   │   │   │   ├── controller/        # REST controllers
│   │   │   │   ├── dto/               # Data Transfer Objects
│   │   │   │   └── advice/            # Exception handlers
│   │   │   ├── utility/               # Helper utilities (e.g., Haversine)
│   │   │   └── OptirouteApplication.java
│   │   └── resources/
│   │       ├── application.yml        # Main configuration
│   │       ├── application-dev.yml    # Development profile
│   │       ├── application-qa.yml     # QA profile
│   │       ├── bootstrap.yml          # Bootstrap configuration (Vault)
│   │       ├── applicationContext.xml
│   │       ├── logback-spring.xml     # Logging configuration
│   │       └── db/changelog/          # Liquibase migrations
│   └── test/                          # Test files
├── logs/                              # Application logs
├── vault-init/                        # Vault initialization scripts
├── Dockerfile                         # Docker configuration
├── docker-compose.yml                 # Docker Compose setup
├── pom.xml                           # Maven dependencies
├── openapi.json                      # OpenAPI specification
├── postman_collection.json           # Postman API tests
└── README.md                         # This file
```

## 🎯 Route Optimization Algorithms

### 1. Nearest Neighbor Algorithm
- **Complexity**: O(n²)
- **Strategy**: Greedy approach - always visit the nearest unvisited delivery
- **Pros**: Fast execution, simple implementation
- **Cons**: May not find the optimal solution
- **Best for**: Quick route generation, small delivery sets

### 2. Clarke-Wright Savings Algorithm
- **Complexity**: O(n² log n)
- **Strategy**: Calculates savings from merging individual routes
- **Pros**: Better optimization quality, considers vehicle capacity
- **Cons**: More computational overhead
- **Best for**: Larger delivery sets, when optimization quality matters

### 3. AI-Powered Optimizer (DeepSeek)
- **Complexity**: Variable (depends on AI model)
- **Strategy**: Leverages machine learning from historical delivery patterns
- **Features**:
  - Learns from past delivery performance and delays
  - Considers historical traffic patterns based on day of week
  - Provides intelligent recommendations and justifications
  - Adapts to changing delivery conditions over time
- **Pros**: Continuously improving optimization, contextually aware decisions
- **Cons**: Requires API connectivity, depends on historical data quality
- **Best for**: Long-term optimization, data-rich environments, complex routing scenarios

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 👥 Authors

- Younes Bousfiha - Initial work

## 🙏 Acknowledgments

- Clarke-Wright Savings Algorithm paper
- Spring Boot documentation
- OCP principal

---

**Built with ❤️ using Spring Boot and Clean Architecture principles**

