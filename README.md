# CS4337BigDataProject
a production-ready web application using Springboot, MySQL,  with microservice architecture

## Project Description

This project includes 5 microservices. 
1. User Service
2. Authentication Service
3. Booking Service
4. Tour Management Service
5. Eureka Server

Eureka server is yet to be implemented. This will allow the services to register themselves with the Eureka server and discover other services.
Then we will use the RestTemplate to make calls to other services. This handles HTTP requests with @GetMapping, @PostMapping, @PutMapping, @DeleteMapping. 
The rest controller will be used to do CRUD operations in our project.

## User Service

This service is responsible for managing users. It will have the following endpoints:
1. POST /users - to create a new user - Passed a CreateUserRequest object in the request body
2. POST /{email}/update-name - to update the name of a user - Passed a UpdateNameRequest object in the request body
3. GET /users - to get all users
4. DELETE /delete - to delete a user by email - Passed a String email in the request body
5. PUT /users/{id} - to update a user by id
