import { Component, ChangeDetectorRef } from '@angular/core';
import { AuthService } from '../../services/auth-service';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login-component',
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './login-component.html',
  styleUrl: './login-component.css',
})
export class LoginComponent {
  form = {
    username: '',
    password: '',
  };

  loading = false;
  errorMessage = '';
  statusMessage = '';
  messageType: 'error' | 'warning' | 'success' = 'error';
  showPassword = false;
  messageTimeout: any;

  constructor(private auth: AuthService, private router: Router, private cdr: ChangeDetectorRef) {}

  login() {
    if (!this.form.username || !this.form.password) {
      this.showMessage('Please enter both username and password', 'error', 3000);
      return;
    }

    this.loading = true;
    this.statusMessage = '';
    this.errorMessage = '';

    this.auth.login(this.form).subscribe({
      next: (res) => {
        console.log('Login response:', res);
        this.loading = false;
        
        if (res.success && res.data) {
          const { token, role, username } = res.data;
          this.auth.saveToken(token);
          this.auth.saveUser({ username, role });
          this.showMessage('Login successful! Redirecting...', 'success', 1500);
          setTimeout(() => this.router.navigate(['/dashboard']), 1500);
        } else {
          const message = res.message || 'Login failed';
          console.log('Login failed with message:', message);
          
          if (message && message.toLowerCase().includes('blocked')) {
            console.log('User is blocked');
            this.showMessage(message, 'warning', 7000);
          } else {
            this.showMessage(message, 'error', 5000);
          }
        }
      },
      error: (err) => {
        console.log('Login error:', err);
        this.loading = false;
        const errorMsg = err.error?.message || 'Login failed. Please try again.';
        console.log('Error message:', errorMsg);
        if (errorMsg && errorMsg.toLowerCase().includes('blocked')) {
          console.log('User is blocked - error block');
          this.showMessage(errorMsg, 'warning', 7000);
        } else {
          this.showMessage(errorMsg, 'error', 5000);
        }
      },
    });
  }

  private showMessage(message: string, type: 'error' | 'warning' | 'success', duration: number) {
    console.log(`Setting message: "${message}" type: ${type} duration: ${duration}ms`);
    if (this.messageTimeout) {
      clearTimeout(this.messageTimeout);
    }
    this.statusMessage = message;
    this.messageType = type;
    console.log('statusMessage is now:', this.statusMessage);
    console.log('messageType is now:', this.messageType);
    this.cdr.detectChanges();
    this.messageTimeout = setTimeout(() => {
      console.log('Clearing message after timeout');
      this.statusMessage = '';
      this.cdr.detectChanges();
    }, duration);
  }

  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }
}

