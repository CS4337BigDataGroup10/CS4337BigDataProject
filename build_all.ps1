# PowerShell script to build all Docker images in parallel
Start-Job { docker build -t eureka-server ./Microservices/EurekaServer }
Start-Job { docker build -t auth-service ./Microservices/AuthenticationService }
Start-Job { docker build -t booking-service ./Microservices/BookingMakingService }
Start-Job { docker build -t tour-management-service ./Microservices/TourManagementService }
Start-Job { docker build -t user-management-service ./Microservices/UserManagementService }

# Wait for all jobs to complete
Get-Job | Wait-Job
