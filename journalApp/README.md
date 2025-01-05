# JournalApp

JournalApp is a Spring Boot-based application designed for managing user journals. It integrates modern technologies like MongoDB, Redis, Kafka, and Google OAuth2 for a seamless and secure user experience.

---

## Features

- **User Authentication**:
    - Google OAuth2 for secure login.
    - JWT-based token generation.

- **Database Management**:
    - MongoDB as the primary database for journal storage.

- **Caching**:
    - Redis for fast data retrieval and performance optimization.

- **Event-Driven Architecture**:
    - Kafka for messaging and asynchronous processing.

- **Email Notifications**:
    - SMTP integration using Gmail for user communication.

---

## Tech Stack

- **Backend**: Spring Boot
- **Database**: MongoDB
- **Caching**: Redis
- **Messaging**: Kafka
- **Authentication**: Google OAuth2
- **Build Tool**: Maven
- **Containerization**: Docker

---

## Environment Variables

Below is the list of environment variables required for the application:

### MongoDB
```properties
MONGODB_HOST=mongodb+srv://<username>:<password>@example.mongodb.net
```

### Redis
```properties
REDIS_HOST=example.redis.com
REDIS_PORT=12663
REDIS_PASSWORD=<your_redis_password>
```

### Google OAuth2
```properties
GOOGLE_CLIENT_ID=<your_client_id>
GOOGLE_CLIENT_SECRET=<your_client_secret>
```

### SMTP (Gmail)
```properties
JAVA_EMAIL=<your_email>
JAVA_EMAIL_PASSWORD=<your_email_password>
```

### Kafka
```properties
KAFKA_SERVERS=<your_kafka_broker>
KAFKA_SASL_USERNAME=<your_kafka_username>
KAFKA_SASL_PASSWORD=<your_kafka_password>
```

---

## Running the Application

### Prerequisites
- Docker installed on your machine.
- MongoDB and Redis instances configured.
- Kafka broker setup.

### Steps

1. **Build the Docker Image**:
   ```bash
   docker build -t journal-app:latest .
   ```

2. **Run the Application**:
   ```bash
   docker run -p 8080:8080 --env-file .env journal-app:latest
   ```

3. **Access the Application**:
    - Open your browser and navigate to `http://localhost:8080`.

---

## Endpoints

### Authentication
- **Google OAuth Callback**: `/auth/google/callback`

---

## Dockerfile Overview

- Multi-stage build with Maven for dependency management.
- Lightweight JDK image for runtime.
- Environment variables for dynamic configuration.

---

## Contributing

1. Fork the repository.
2. Create a new branch.
3. Make your changes and commit them.
4. Submit a pull request.

---

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

---

## Contact

For any questions or issues, feel free to reach out to:
- **Email**: arshbhushan@gmail.com
