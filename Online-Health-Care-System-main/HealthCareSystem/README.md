# HealthCareSystem

## Project Overview

HealthCareSystem is a Spring Boot backend application for managing diagnostic centers, diagnostic tests, appointment booking, and report management. It supports user and admin authentication, center approval flows, test and appointment management, and report upload/download.

## Key Features

- User and Admin registration and login
- JWT-based authentication for protected endpoints
- Diagnostic center creation and approval workflow
- Diagnostic test creation and assignment to centers
- Appointment booking with slot and capacity validation
- Appointment status management
- Report file upload and download

## Technology Stack

- Java 17
- Spring Boot
- Spring Security
- Spring Data JPA
- MySQL
- JWT authentication
- Lombok

## Configuration

The application database connection is configured in `src/main/resources/application.properties`:

```properties
spring.application.name=HealthCareSystem
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

spring.datasource.url=jdbc:mysql://localhost:3306/springdb
spring.datasource.username=root
spring.datasource.password=Cfg@1234
```

## Security

- All endpoints under `/api/auth/**` are public.
- All other endpoints require JWT authentication.
- JWT tokens are validated by `JwtFilter`.
- The filter extracts `username` and `role` from the token and attaches them to request attributes.
- Passwords are stored using BCrypt encryption.

## Authentication Endpoints

### `POST /api/auth/register-user`
Register a regular user.
- Request body: `RegisterRequest`
- Role assigned: `USER`
- Validation:
  - `username` must be 5-20 characters, start with a letter, and may include letters, numbers, dots, or underscores
  - `email` must match `@domain.com|org|net|in`
  - `password` must contain at least one uppercase letter and one number

### `POST /api/auth/register-admin`
Register an admin user.
- Request body: `RegisterRequest`
- Role assigned: `ADMIN`

### `POST /api/auth/login`
Authenticate with username and password.
- Request body: `LoginRequest`
- Response data: JWT token

## API Endpoints

### Admin Routes (`/api/admin`)

#### `GET /api/admin/centers`
Returns all diagnostic centers.

#### `GET /api/admin/centers/pending`
Returns centers in pending state.

#### `PUT /api/admin/centers/{id}/approve`
Approves a diagnostic center.

#### `PUT /api/admin/centers/{id}/reject`
Rejects a diagnostic center.

#### `GET /api/admin/users`
Returns all registered users.

#### `PUT /api/admin/users/{id}/block`
Blocks a user account.

#### `PUT /api/admin/users/{id}/unblock`
Unblocks a user account.

### Appointment Routes (`/api/appointments`)

#### `POST /api/appointments`
Book an appointment.
- Request body: `AppointmentRequest`
- Requires authenticated user
- Validations:
  - Center must exist and be approved
  - Test must exist
  - Appointment date must not be in the past
  - Duplicate booking for same user/center/test/date is prevented
  - Slot capacity is enforced by `CenterTest.maxBooking`

#### `GET /api/appointments/my`
Returns appointments for the authenticated user.

#### `PUT /api/appointments/{id}/status`
Update appointment status.
- Query parameter: `status`
- Allowed values: `PENDING`, `APPROVED`, `REJECTED`

#### `DELETE /api/appointments/{id}`
Cancel a user appointment.
- Only the user who booked the appointment may cancel it.

### Center Routes (`/api/centers`)

#### `POST /api/centers/request`
Request creation of a diagnostic center.
- Request body: `CenterRequest`
- Only `ADMIN` users are allowed to perform this operation.
- New center status is set to `PENDING`.

#### `POST /api/centers/{centerId}/add-test`
Add a test to a diagnostic center.
- Request body: `CenterTestRequest`
- Only the center owner can add tests.

#### `DELETE /api/centers/{centerId}/remove-test/{testId}`
Remove a specific test from a center.
- Only the center owner may remove tests.

#### `GET /api/centers/{centerId}/tests`
Fetch tests assigned to a specific center.

#### `GET /api/centers/appointments`
Fetch all appointments for the center owned by the authenticated user.

#### `PUT /api/centers/appointments/{id}/status`
Update appointment status for a center appointment.
- Query parameter: `status`

### Diagnostic Test Routes (`/api/tests`)

#### `POST /api/tests`
Create a new diagnostic test.
- Request body: `DiagnosticTestRequest`

#### `GET /api/tests`
Get all diagnostic tests.

#### `POST /api/tests/center/{centerId}`
Add an existing test to a center.
- Request body: `CenterTestRequest`
- Requires center owner and approved center

#### `GET /api/tests/center/{centerId}`
Get tests assigned to a specific center.

### Report Routes (`/api/reports`)

#### `POST /api/reports/upload/{appointmentId}`
Upload a report file for an appointment.
- Request params:
  - `patientName`
  - `file` (multipart file)
- Only the center owner can upload reports for appointments belonging to their center.
- Center must be approved.

#### `GET /api/reports?patientName=...`
Get reports filtered by patient name.

#### `GET /api/reports/download/{id}`
Download the report file.
- Response includes content type and attachment headers.

## Data Models

### RegisterRequest
- `username`
- `email`
- `password`

### LoginRequest
- `username`
- `password`

### AppointmentRequest
- `centerId`
- `testId`
- `appointmentDate`
- `slot`

### CenterRequest
- `ownerName`
- `centerName`
- `location`

### CenterTestRequest
- `testId`
- `maxBooking`

### ApiResponse<T>
Response wrapper used across most endpoints:
- `success`
- `message`
- `data`

## Entity Summary

### User
- `id`, `username`, `email`, `password`, `role`, `active`

### DiagnosticCenter
- `id`, `ownerName`, `centerName`, `location`, `status`, `user`

### DiagnosticTest
- `id`, `testname`, `description`, `price`

### CenterTest
- `id`, `center`, `test`, `maxBooking`

### Appointment
- `id`, `appointmentDate`, `slot`, `status`, `center`, `test`, `user`

### Report
- `id`, `patientName`, `fileName`, `fileType`, `data`, `appointment`

## Error Handling

The application uses custom exceptions with a global handler:
- `BadRequestException` → `400 Bad Request`
- `AdminAccessException` → `400 Bad Request`
- `CenterNotFoundException` → `404 Not Found`
- `TestNotAvailableException` → `404 Not Found`
- `SlotFullException` → `404 Not Found`
- `ApointmentNotFoundException` → `404 Not Found`

## Additional Notes

- The JWT secret is hard-coded in `JwtUtil` as `mysecretkeymysecretkeymysecretkey`.
- The filter logic in `JwtFilter` allows requests without authorization headers to continue, but the security config still protects non-auth routes.
- The center owner check is performed by comparing the authenticated username with center ownership.
- `Appointment` slots are limited based on `CenterTest.maxBooking`.

## Running the Project

1. Ensure MySQL is running and a database named `springdb` exists.
2. Update database credentials if needed in `src/main/resources/application.properties`.
3. Run the application with Maven:

```bash
./mvnw spring-boot:run
```

4. Use an API client such as Postman to interact with the endpoints.

---

This README documents all routes, payloads, entities, and behavior in the `HealthCareSystem` project. If you want, I can also generate a shorter API reference or an OpenAPI-style route summary file.