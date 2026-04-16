import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth-service';
import { Router, ActivatedRoute } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-otp-verification',
  imports: [FormsModule, CommonModule],
  templateUrl: './otp-verification.html',
  styleUrl: './otp-verification.css',
})
export class OtpVerificationComponent implements OnInit {
  email: string = '';
  otp: string = '';
  loading = false;
  errorMessage = '';
  successMessage = '';
  resendLoading = false;

  constructor(
    private auth: AuthService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    this.email = this.route.snapshot.queryParams['email'] || '';
    if (!this.email) {
      this.router.navigate(['/signup-user']);
      return;
    }
    // Send OTP on page load
    this.sendOtp();
  }

  sendOtp() {
    this.errorMessage = '';
    this.resendLoading = true;

    this.auth.sendOtp(this.email).subscribe({
      next: () => {
        this.resendLoading = false;
        this.successMessage = 'OTP sent successfully to your email';
        setTimeout(() => {
          this.successMessage = '';
        }, 3000);
      },
      error: (err) => {
        this.resendLoading = false;
        this.errorMessage = 'Failed to send OTP. Please try again.';
      },
    });
  }

  onOtpInput(event: any) {
    const value = event.target.value.replace(/[^0-9]/g, '');
    this.otp = value;
  }

  verifyOtp() {
    this.errorMessage = '';
    this.successMessage = '';

    if (!this.otp || this.otp.length !== 6) {
      this.errorMessage = 'Please enter a valid 6-digit OTP';
      return;
    }

    this.loading = true;

    this.auth.verifyOtp(this.email, this.otp).subscribe({
      next: (response) => {
        console.log('OTP verification response:', response);
        this.loading = false;
        this.successMessage = 'OTP verified successfully! Redirecting to login...';
        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 2000);
      },
      error: (err) => {
        console.log('OTP verification error:', err);
        this.loading = false;
        this.errorMessage = err.error?.message || 'Invalid or expired OTP';
      },
    });
  }

  resendOtp() {
    this.sendOtp();
  }
}