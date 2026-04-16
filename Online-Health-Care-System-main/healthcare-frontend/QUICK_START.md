# Healthcare Frontend - Quick Start Guide

## ⚡ Getting Started in 5 Minutes

### Step 1: Install Dependencies
```bash
cd healthcare-frontend
npm install
```

### Step 2: Ensure Backend is Running
The backend should be running on `http://localhost:8080`
- Database: MySQL at `localhost:3306` (springdb)
- Backend: Spring Boot application

### Step 3: Start Development Server
```bash
npm start
```
The application will open at `http://localhost:4200`

### Step 4: Access the Application
1. **Landing Page**: `http://localhost:4200`
2. **Login**: `http://localhost:4200/login`
3. **Register as Patient**: `http://localhost:4200/signup-user`
4. **Register Center**: `http://localhost:4200/signup-admin`

---

## 🧪 Test the Application

### 1. Create a Patient Account
- Go to `/signup-user`
- Create credentials:
  - Username: testuser123
  - Email: test@domain.com
  - Password: TestPass123
- Click "Create Account"
- You'll be redirected to login

### 2. Login as Patient
- Go to `/login`
- Enter: testuser123 / TestPass123
- Click "Sign In"
- You'll see the Patient Dashboard

### 3. Test Patient Features
**My Appointments Tab**
- View booked appointments
- Cancel appointments

**Book Appointment Tab**
- Select diagnostic center
- Choose test
- Select time slot
- Click "Book Appointment"

**My Reports Tab**
- View downloaded reports
- Click "Download" to get reports

### 4. Create & Test Center Admin Account
- Go to `/signup-admin`
- Create credentials:
  - Username: centeradmin123
  - Email: center@domain.com
  - Password: AdminPass123

### 5. Login as Center Admin
- Login with above credentials
- You'll see the Center Dashboard

**Appointments Tab**
- View all center appointments
- Update status to Confirmed/Completed

**Request Center Tab**
- Fill in center details
- Submit request
- Will show status once reviewed

### 6. Test Super Admin Features
- Login with SUPER_ADMIN role credentials
- You'll see the Admin Dashboard

**All Centers Tab**
- View all registered diagnostic centers

**Pending Approval Tab**
- Approve or reject center requests

**Users Tab**
- View all users
- Block/Unblock users

---

## 🔍 Validation Checklist

### Frontend Components
- [ ] Login component renders with proper styling
- [ ] Signup components have password strength indicator
- [ ] Dashboard loads based on user role
- [ ] Navbar shows user info and logout button
- [ ] Landing page displays all sections

### Authentication
- [ ] JWT token is stored in localStorage after login
- [ ] Token is sent in Authorization header
- [ ] Protected routes redirect to login if not authenticated
- [ ] Logout clears token and user data

### User Dashboard
- [ ] Displays all user appointments
- [ ] Can book new appointments via form
- [ ] Can cancel appointments
- [ ] Can download reports

### Admin Dashboard
- [ ] Shows pending approval centers
- [ ] Can approve/reject centers
- [ ] Shows center appointments
- [ ] Can update appointment status

### Super Admin Dashboard
- [ ] Shows all centers
- [ ] Shows all users
- [ ] Can block/unblock users
- [ ] All status updates reflected

### Styling & Responsiveness
- [ ] All components use Tailwind CSS
- [ ] Mobile responsive (test on 375px width)
- [ ] Tablet responsive (test on 768px width)
- [ ] Desktop responsive (1024px+ width)
- [ ] Gradient backgrounds render correctly
- [ ] Dark mode toggle works (if enabled)

### Error Handling
- [ ] Error messages display properly
- [ ] Success notifications appear
- [ ] Loading spinners show during API calls
- [ ] Form validation messages show

---

## 🐛 Troubleshooting

### Issue: "Port 4200 already in use"
```bash
ng serve --port 4201
```

### Issue: Tailwind CSS not applying
```bash
npm install --save-dev tailwindcss postcss autoprefixer
npm run build
```

### Issue: Backend connection error
- Check if Spring Boot backend is running on port 8080
- Check CORS configuration in backend
- Update API URL in services if needed

### Issue: Cannot login
- Verify backend has the user registered
- Check browser console for errors
- Verify JWT token is being received

### Issue: Module not found errors
```bash
rm -rf node_modules package-lock.json
npm install
```

---

## 📁 Important File Locations

| File | Purpose |
|------|---------|
| `src/app/app.routes.ts` | Route configuration |
| `src/app/app.config.ts` | App setup with HTTP+Interceptor |
| `src/app/services/auth-service.ts` | Authentication logic |
| `src/app/interceptors/jwt-interceptor.ts` | JWT token injection |
| `src/app/guards/auth-guard.ts` | Route protection |
| `src/styles.css` | Global & Tailwind styles |
| `tailwind.config.js` | Tailwind configuration |
| `src/app/components/dashboard-component/` | Main dashboard |

---

## 🚀 Build for Production

```bash
npm run build
```

Output will be in `dist/healthcare-frontend/`

---

## 📚 API Documentation

### Base URL: `http://localhost:8080/api`

#### Authentication Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/auth/register-user` | Register patient |
| POST | `/auth/register-admin` | Register center |
| POST | `/auth/login` | Login with credentials |

#### Appointment Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/appointments` | Book appointment |
| GET | `/appointments/my` | Get user's appointments |
| PUT | `/appointments/{id}/status` | Update status |
| DELETE | `/appointments/{id}` | Cancel appointment |

#### Center Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/centers/request` | Request center |
| GET | `/centers/{id}/tests` | Get available tests |
| GET | `/centers/appointments` | Get center appointments |
| PUT | `/centers/appointments/{id}/status` | Update appointment |

#### Admin Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/admin/centers` | All centers |
| GET | `/admin/centers/pending` | Pending approvals |
| PUT | `/admin/centers/{id}/approve` | Approve center |
| PUT | `/admin/centers/{id}/reject` | Reject center |
| GET | `/admin/users` | All users |
| PUT | `/admin/users/{id}/block` | Block user |
| PUT | `/admin/users/{id}/unblock` | Unblock user |

---

## 💡 Tips & Best Practices

1. **Always check console** for errors: Open DevTools (F12) → Console tab
2. **Use Network tab** to debug API calls: DevTools → Network tab
3. **Browser storage**: DevTools → Application → Local Storage → token
4. **Clear cache**: Press Ctrl+Shift+Delete or Cmd+Shift+Delete
5. **Use mobile view**: DevTools → Toggle device toolbar (Ctrl+Shift+M)

---

## 🔐 Security Notes

- JWT tokens are stored in localStorage
- Tokens are sent in Authorization header for all API calls
- Passwords are hashed with BCrypt on backend
- Routes are protected with AuthGuard
- Roles are checked for admin and super-admin pages

---

## 📞 Support

For issues or assistance, refer to:
- Browser Console: `F12` or `Cmd+Option+I`
- Network Errors: Check DevTools Network tab
- Backend Issues: Check if Spring Boot is running
- Database Issues: Verify MySQL is running with correct credentials

---

## ✨ Features Overview

| Feature | Patient | Admin | Super Admin |
|---------|---------|-------|------------|
| View Appointments | ✅ | ✅ | ❌ |
| Book Appointments | ✅ | ❌ | ❌ |
| Cancel Appointments | ✅ | ❌ | ❌ |
| View Reports | ✅ | ❌ | ❌ |
| Request Center | ❌ | ✅ | ❌ |
| Manage Appointments | ❌ | ✅ | ❌ |
| Approve Centers | ❌ | ❌ | ✅ |
| Manage Users | ❌ | ❌ | ✅ |

---

**Last Updated**: 2024
**Angular Version**: 21.2.0
**Tailwind CSS**: v4.2.2
