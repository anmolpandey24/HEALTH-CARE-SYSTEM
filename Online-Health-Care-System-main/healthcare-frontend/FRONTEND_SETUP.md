# HealthCare System - Angular Frontend

A modern, responsive Angular frontend application for the HealthCare System backend. Built with Angular 21+, Tailwind CSS v3, and TypeScript.

## Features

### For Patients
- User registration and login
- Browse and search diagnostic centers
- Book diagnostic test appointments
- View appointment history
- Download test reports
- Responsive mobile-friendly interface

### For Diagnostic Centers
- Register your diagnostic center
- View and manage appointments
- Update appointment status (Confirmed, Completed)
- Request center registration with admin approval

### For Administrators
- View all diagnostic centers
- Approve/reject center registration requests
- Manage user accounts (block/unblock)
- Monitor all appointments across centers

## Tech Stack

- **Framework**: Angular 21.2.0
- **Language**: TypeScript 5.9.2
- **Styling**: Tailwind CSS v4.2.2
- **HTTP Client**: Built-in Angular HttpClient
- **State Management**: RxJS Observables
- **Authentication**: JWT with Interceptor
- **Build Tool**: Angular CLI 21.2.0

## Prerequisites

- Node.js (v18 or higher)
- npm (v11 or higher)
- Angular CLI (`npm install -g @angular/cli@21`)

## Installation

1. **Clone the repository**
   ```bash
   cd healthcare-frontend
   ```

2. **Install dependencies**
   ```bash
   npm install
   ```

3. **Configure API URL**
   - Update `src/app/config/environment.ts` if your backend runs on a different port/domain

## Running the Application

### Development Server

```bash
npm start
```

Navigate to `http://localhost:4200/`. The application will automatically reload if you change any of the source files.

### Production Build

```bash
npm run build
```

The build artifacts will be stored in the `dist/` directory.

## Project Structure

```
src/
├── app/
│   ├── components/
│   │   ├── dashboard-component/    # Main dashboard
│   │   ├── login-component/        # Login page
│   │   ├── signup-user/            # User registration
│   │   ├── signup-admin/           # Center registration
│   │   ├── home/                   # Landing page
│   │   └── navbar/                 # Navigation bar
│   ├── services/
│   │   ├── auth-service.ts         # Authentication
│   │   ├── appointment-service.ts  # Appointments
│   │   ├── diagnostic-center-service.ts
│   │   ├── diagnostic-test-service.ts
│   │   ├── admin-service.ts        # Admin functions
│   │   ├── report-service.ts       # Reports
│   │   └── theme-service.ts        # Theme management
│   ├── models/
│   │   └── user-model.ts           # Data interfaces
│   ├── guards/
│   │   └── auth-guard.ts           # Route protection
│   ├── interceptors/
│   │   └── jwt-interceptor.ts      # JWT token injection
│   ├── config/
│   │   └── environment.ts          # API configuration
│   ├── app.routes.ts               # Routing
│   ├── app.config.ts               # App configuration
│   └── app.ts                      # Root component
├── styles.css                       # Global styles
└── main.ts                         # Entry point
```

## API Endpoints

The application connects to the backend at `http://localhost:8080/api`

### Authentication
- `POST /auth/register-user` - Register patient
- `POST /auth/register-admin` - Register center
- `POST /auth/login` - Login

### Appointments
- `POST /appointments` - Book appointment
- `GET /appointments/my` - Get user's appointments
- `PUT /appointments/{id}/status` - Update status
- `DELETE /appointments/{id}` - Cancel appointment

### Diagnostic Centers
- `POST /centers/request` - Request center
- `GET /centers/{id}/tests` - Get center tests
- `GET /centers/appointments` - Get center appointments

### Admin
- `GET /admin/centers` - All centers
- `GET /admin/centers/pending` - Pending approvals
- `PUT /admin/centers/{id}/approve` - Approve center
- `PUT /admin/centers/{id}/reject` - Reject center
- `GET /admin/users` - All users
- `PUT /admin/users/{id}/block` - Block user
- `PUT /admin/users/{id}/unblock` - Unblock user

## Authentication

The application uses JWT (JSON Web Tokens) for authentication:

1. User logs in with credentials
2. Backend returns JWT token
3. Token is stored in localStorage
4. JwtInterceptor automatically adds token to all API requests
5. Protected routes require authentication via AuthGuard

## Styling

All components are styled using **Tailwind CSS v3** with:
- Responsive design (mobile-first approach)
- Gradient backgrounds
- Smooth animations and transitions
- Dark mode support
- Professional color scheme (Blue & Purple)

## Key Features

### Responsive Design
- Mobile (320px and up)
- Tablet (768px and up)
- Desktop (1024px and up)

### User Experience
- Loading states with spinners
- Error messages with alerts
- Success notifications
- Form validation
- Password strength indicator
- Smooth transitions and animations

### Security
- JWT token authentication
- XSS protection with Angular sanitization
- CORS configured on backend
- Secure password storage (bcrypt hashed)

## Development

### Generate a New Component
```bash
ng generate component components/component-name
```

### Generate a New Service
```bash
ng generate service services/service-name
```

### Run Tests
```bash
npm test
```

## Environment Variables

Create a `.env` file in the root directory (if needed):
```
ANGULAR_APP_API_URL=http://localhost:8080/api
```

## Troubleshooting

### Port already in use
```bash
ng serve --port 4201
```

### Clear node_modules and reinstall
```bash
rm -rf node_modules package-lock.json
npm install
```

### Tailwind CSS not working
```bash
npm run build -- --configuration development
```

## Browser Support

- Chrome (latest)
- Firefox (latest)
- Safari (latest)
- Edge (latest)

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License.

## Support

For issues or questions, please contact the development team or create an issue in the repository.

## Future Enhancements

- [ ] Video consultation feature
- [ ] Payment integration
- [ ] SMS/Email notifications
- [ ] Advanced analytics
- [ ] Multi-language support
- [ ] Progressive Web App (PWA)
- [ ] Push notifications
