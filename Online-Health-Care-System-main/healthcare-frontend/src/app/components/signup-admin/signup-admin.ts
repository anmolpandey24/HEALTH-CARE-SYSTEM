import { Component } from '@angular/core';
import { AuthService } from '../../services/auth-service';
import { Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-signup-admin',
  imports: [FormsModule, RouterModule, CommonModule],
  templateUrl: './signup-admin.html',
  styleUrl: './signup-admin.css',
})
export class SignupAdmin {
  form = {
    username: '',
    email: '',
    password: '',
  };

  loading = false;
  errorMessage = '';
  successMessage = '';
  showPassword = false;
  passwordStrength = 0;

  constructor(private auth: AuthService, private router: Router) {}

  validatePassword(): void {
    const password = this.form.password;
    let strength = 0;

    if (password.length >= 8) strength++;
    if (/[a-z]/.test(password)) strength++;
    if (/[A-Z]/.test(password)) strength++;
    if (/[0-9]/.test(password)) strength++;
    if (/[^a-zA-Z0-9]/.test(password)) strength++;

    this.passwordStrength = strength;
  }

  signup() {
    this.errorMessage = '';
    this.successMessage = '';

    if (!this.form.username || !this.form.email || !this.form.password) {
      this.errorMessage = 'All fields are required';
      return;
    }

    if (this.form.username.length < 5) {
      this.errorMessage = 'Username must be at least 5 characters long';
      return;
    }

    if (!this.form.email.includes('@')) {
      this.errorMessage = 'Please enter a valid email address';
      return;
    }

    if (this.form.password.length < 8) {
      this.errorMessage = 'Password must be at least 8 characters long';
      return;
    }

    this.loading = true;

    this.auth.registerAdmin(this.form).subscribe({
      next: () => {
        this.loading = false;
        this.successMessage = 'Center admin account created successfully! Redirecting to verification...';
        setTimeout(() => {
          this.router.navigate(['/otp-verification'], { queryParams: { email: this.form.email } });
        }, 2000);
      },
      error: (err) => {
        this.loading = false;
        this.errorMessage = err.error?.message || 'Sign up failed. Please try again.';
      },
    });
  }

  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }
}
