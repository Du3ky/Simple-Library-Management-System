# Simple Library Management System

This is a Spring Boot application for managing a simple library system. It allows for adding, retrieving, updating, and deleting books, as well as managing individual book copies.

## Features

- Add a new book
- View all books (with pagination)
- View detailed information about a book and its copies
- Update book details
- Delete books
- Add and update book copies
- Global error handling
- Validation on DTOs
- Unit tests for service and controller layers

## Technologies Used

- Java 17
- Spring Boot 3
- Spring Data JPA
- Hibernate
- H2 in-memory database
- JUnit 5 & Mockito

## How to Run the Application

1. **Clone the repository**

   ```bash
   git clone https://github.com/Du3ky/Simple-Library-Management-System
   
2. **Build and run using Maven**

mvn clean install,
mvn spring-boot:run

    The application will start on http://localhost:8080


3. **API Documentation (Swagger UI)**

    Open your browser at: http://localhost:8080/swagger-ui.html