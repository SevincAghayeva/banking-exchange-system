# banking-exchange-system
A high-performance hybrid architecture implementing a REST-to-gRPC API Gateway (BFF) using Spring Boot 3, HTTP/2, Server Streaming, custom Interceptors, and centralized error mapping.


# Banking Exchange System (REST-to-gRPC BFF Gateway)

A lightweight Spring Boot service showcasing a hybrid architecture: exposes REST endpoints to external clients (BFF pattern) and forwards requests internally to a core gRPC server over HTTP/2.

## Features
* **Unary RPC (`getRate`):** Low-latency request-response currency lookup with input validation.
* **Server Streaming RPC (`getLiveRates`):** Asynchronous thread-managed pipeline pushing dynamic rate updates to the client every 1 second.
* **Global Error Mapping:** Intercepts `StatusRuntimeException` from the gRPC layer and converts them into standard HTTP JSON responses (`404 Not Found`, `400 Bad Request`).
* **Server Interceptor:** Custom middleware tracking request metadata, gRPC method names, and execution latency in milliseconds.

## Tech Stack
* Java 17
* Spring Boot 3.2.5
* Gradle (gRPC BOM managed)
* gRPC (Protobuf v3, Blocking Stub)

---

## Setup & Running

1. Clone the repo:
   ```bash
   git clone [https://github.com/YOUR_USERNAME/banking-exchange-system.git](https://github.com/YOUR_USERNAME/banking-exchange-system.git)
   cd banking-exchange-system


Generate gRPC Java sources via Protobuf compiler:

Bash
./gradlew build
Start the application:

Bash
./gradlew bootRun
The application opens two ports:

8080 - HTTP/REST Gateway

9090 - Internal gRPC Server

Local Configuration (application.properties)
Properties
spring.application.name=banking-exchange-system
server.port=8080
grpc.server.port=9090

grpc.client.exchangeClient.address=static://127.0.0.1:9090
grpc.client.exchangeClient.negotiationType=plaintext
Testing the Endpoints
1. REST Rate Lookup (Unary RPC)
   Request: GET http://localhost:8080/api/rate?from=USD&to=AZN

Response:

JSON
{
"from": "USD",
"to": "AZN",
"rate": 1.70,
"message": "gRPC Serverindən uğurla gəldi!"
}
2. Live Stream (Server Streaming)
   Using Postman gRPC client, invoke getLiveRates on localhost:9090. 
The server streams 10 dynamic updates over 10 seconds before triggering onCompleted().

3. Validation / Error Handling
   Request: GET http://localhost:8080/api/rate?from=BTC&to=AZN

Response:

JSON
{
"error": "NOT_FOUND",
"message": "Kriptovalyuta məzənnələri (BTC) müvəqqəti olaraq dəstəklənmir.",
"status": 404,
"timestamp": 1716666825000
}