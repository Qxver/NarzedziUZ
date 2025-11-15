# NarzedziUZ - E-commerce Application

Spring Boot application for an e-commerce store.

## Prerequisites

- Docker
- Docker Compose

## Running the Application

1. Build and start the application with Docker Compose:

```bash
docker compose up --build
```

This will:
- Start a PostgreSQL 16 database container
- Build and start the Spring Boot application container
- The application will be available at http://localhost:8080

2. To stop the application:

```bash
docker compose down
```

3. To stop and remove volumes (clean database):

```bash
docker compose down -v
```

## Database Configuration

The application uses PostgreSQL database with the following default credentials:
- Database: `narzedziuz`
- Username: `narzedziuz_user`
- Password: `secure_password`
- Port: `5432`

## Application Endpoints

### Web Pages
- Home: http://localhost:8080/
- Login: http://localhost:8080/login
- Register: http://localhost:8080/register

### API Endpoints

#### Products
- `GET /api/products` - Get all products
- `GET /api/products/{id}` - Get product by ID
- `GET /api/products/category/{categoryId}` - Get products by category
- `GET /api/products/search?query={name}` - Search products by name
- `POST /api/products` - Create a new product
- `PUT /api/products/{id}` - Update a product
- `DELETE /api/products/{id}` - Delete a product

#### Orders
- `GET /api/orders/user/{userId}` - Get orders for a user
- `POST /api/orders` - Create a new order from cart

## Development

### Local Development (without Docker)

1. Make sure PostgreSQL is running locally
2. Update `src/main/resources/application.yml` with your database credentials
3. Run the application:

```bash
./mvnw spring-boot:run
```

### Building the Application

```bash
./mvnw clean package
```

## Technology Stack

- Spring Boot 3.5.7
- Java 17
- PostgreSQL 16
- Hibernate/JPA
- Thymeleaf
- Lombok
- Maven
- Docker

