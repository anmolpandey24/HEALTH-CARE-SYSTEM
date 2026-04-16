import { Routes } from '@angular/router';
import { LoginComponent } from './components/login-component/login-component';
import { SignupUser } from './components/signup-user/signup-user';
import { SignupAdmin } from './components/signup-admin/signup-admin';
import { DashboardComponent } from './components/dashboard-component/dashboard-component';
import { HomeComponent } from './components/home/home';
import { AuthGuard } from './guards/auth-guard';
import { OtpVerificationComponent } from './components/otp-verification/otp-verification';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'home', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'signup-user', component: SignupUser },
  { path: 'signup-admin', component: SignupAdmin },
  { path: 'otp-verification', component: OtpVerificationComponent },
  {
    path: 'dashboard',
    component: DashboardComponent,
    canActivate: [AuthGuard],
  },
  { path: '**', redirectTo: '/' },
];
