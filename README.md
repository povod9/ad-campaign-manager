# Adcampaign Service

## Description

A REST API for managing advertising campaigns, products, and sellers.  
Built on Spring Boot 4.0.6, MapStruct, Spring Security (JWT authentication), and H2 for local development.

**Security:**  
All business logic is scoped to the resource owner - no seller can access someone else’s products or campaigns.  
All key endpoints require a valid JWT token.  
Only registration and login are public.


The service will run on http://localhost:8080 by default.

---

## How to Run

### Prerequisites
- Java 21
- Maven

### Steps
1. Clone the repository.
2. Build the project:
   ```bash
   mvn spring-boot:run
   
---

## ENV/Configuration

**JWT secret and expiration must be set either via environment variables or in application.yaml:**
- `JWT_SECRET` — Secret key for JWT signing (a default is set in application.yaml for local).
- `JWT_EXPIRATION` — Token expiration, in milliseconds (`8600000` by default).

---

## Example Postman Requests

### Register a Seller

```http
POST /sellers/registration

{
  "sellerName": "John Doe",
  "email": "johndoe@example.com",
  "password": "Secret123!",
  "emeraldAmountFunds": 1000
}
```

---

### Login

```http
POST /sellers/login

{
  "email": "johndoe@example.com",
  "password": "Secret123!"
}
```
**The response will contain your `accessToken`. Use it for all authorized requests below.**

Every request below (except registration and login) must include:

```
Authorization: Bearer <accessToken>
```

---

### Create Product

```http
POST /products
Your Token

{
  "name": "Electric Toothbrush"
}
```

---

### Get All Your Products

```http
GET /products
Your Token
```

---

### Get Your Product by Id

```http
GET /products/1
Your Token
```

---

### Update Product

```http
PUT /products/1
Your Token

{
  "name": "Ultrasonic Toothbrush"
}
```

---

### Create Campaign

```http
POST /campaigns
Your Token

{
  "name": "Lola",
  "keywords": ["toothbrush", "hygiene"],
  "bidAmount": 250,
  "campaignFund": 1000,
  "status": "ON",
  "town": ["Warsaw", "Lublin"],
  "radius": 15,
  "productId": 1
}
```

---

### Get All Your Campaigns

```http
GET /campaigns
Your Token
```

---

### Get Campaign by Id

```http
GET /campaigns/1
Your Token
```

---

### Update Campaign

```http
PUT /campaigns/1
Your Token

{
  "name": "Lola",
  "keywords": ["new", "campaign"],
  "bidAmount": 300,
  "status": "OFF",
  "town": ["Cracow"],
  "radius": 18,
  "productId": 1
}
```

---

### Delete Campaign

```http
DELETE /campaigns/1
Your Token
```

---

### Update Seller

```http
PUT /sellers
Your Token

{
  "sellerName": "John Freeman",
  "email": "johnfreeman@example.com",
  "password": "NewSecret123!"
}
```

---

## Stack / Dependencies

- Java 21
- Spring Boot 4.0.6
- MapStruct 1.6
- Spring Security (JWT)
- Lombok
- H2 (in-memory, default)
- Validation
---

### Notes

- **Resource ownership is strictly enforced - you're not allowed to access or modify resources that are not owned by you.**
- Does not aggregate global or public resources - all operations are owner scoped and authenticated.
- The server will not start without a valid JWT_SECRET.
- Error responses are always structured with reason and message.

---
