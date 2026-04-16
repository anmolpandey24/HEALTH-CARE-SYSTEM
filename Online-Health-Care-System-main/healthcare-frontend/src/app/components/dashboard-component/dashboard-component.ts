import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth-service';
import { AppointmentService } from '../../services/appointment-service';
import { DiagnosticTestService } from '../../services/diagnostic-test-service';
import { DiagnosticCenterService } from '../../services/diagnostic-center-service';
import { ReportService } from '../../services/report-service';
import { AdminService } from '../../services/admin-service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { NavbarComponent } from '../navbar/navbar';
import {
  Appointment,
  DiagnosticCenter,
  DiagnosticTest,
  CenterTest,
  Report,
  User,
  CenterRequest,
} from '../../models/user-model';

@Component({
  selector: 'app-dashboard-component',
  imports: [CommonModule, FormsModule, NavbarComponent],
  templateUrl: './dashboard-component.html',
  styleUrl: './dashboard-component.css',
})
export class DashboardComponent implements OnInit {
  // User Info
  currentUser: any;
  userRole: string = '';

  // Data
  myAppointments: Appointment[] = [];
  allCenters: DiagnosticCenter[] = [];
  approvedCenters: DiagnosticCenter[] = [];
  pendingCenters: DiagnosticCenter[] = [];
  myCenter: DiagnosticCenter | null = null;
  allTests: DiagnosticTest[] = [];
  centerTests: CenterTest[] = [];
  myReports: Report[] = [];
  allUsers: User[] = [];
  centerAppointments: Appointment[] = [];
  slotOptions = ['MORNING', 'AFTERNOON', 'EVENING'];

  activeTab: string = 'appointments';
  loading = false;
  error: string = '';
  successMessage: string = '';

  // Booking Form
  bookingForm = {
    centerId: 0,
    testId: 0,
    appointmentDate: '',
    slot: '',
  };

  centerForm = {
    centerName: '',
    address: '',
  };

  testToAdd = {
    testId: 0,
    maxBooking: 1,
  };

  newTest = {
    testName: '',
    description: '',
    testPrice: 0,
  };

  reportFile: File | null = null;
  selectedReportAppointmentId: number = 0;
  reportPatientName: string = '';
  reportFiles: { [appointmentId: number]: File | null } = {};
  showReportUploadForm: { [appointmentId: number]: boolean } = {};

  resetPasswordForm = {
    currentPassword: '',
    newPassword: '',
    confirmPassword: '',
  };

  // Filter
  selectedCenterId: number = 0;
  selectedTestId: number = 0;

  constructor(
    private authService: AuthService,
    private appointmentService: AppointmentService,
    private diagnosticTestService: DiagnosticTestService,
    private diagnosticCenterService: DiagnosticCenterService,
    private reportService: ReportService,
    private adminService: AdminService,
    private router: Router
  ) {
    this.currentUser = this.authService.getCurrentUser();
    this.userRole = this.authService.getRole();
    console.log('Dashboard constructor userRole=', this.userRole, 'token present=', !!this.authService.getToken());
  }

  ngOnInit(): void {
    if (this.userRole === 'USER') {
      this.loadUserDashboard();
    } else if (this.userRole === 'ADMIN') {
      this.loadAdminDashboard();
    } else if (this.userRole === 'SUPER_ADMIN') {
      this.loadSuperAdminDashboard();
    }
  }

  loadUserDashboard(): void {
    this.loadMyAppointments();
    this.loadApprovedCenters();
    this.loadMyReports();
  }

  loadAdminDashboard(): void {
    this.loadCenterAppointments();
    this.loadMyCenter();
    this.loadAllTests();
  }

  loadMyAppointments(): void {
    this.loading = true;
    this.appointmentService.getMyAppointments().subscribe({
      next: (res) => {
        this.myAppointments = res.data || [];
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to load appointments';
        this.loading = false;
      },
    });
  }

  loadAllTests(): void {
    this.diagnosticTestService.getAllTests().subscribe({
      next: (res) => {
        this.allTests = res.data || [];
      },
      error: (err) => {
        // this.error = 'Failed to load tests';
      },
    });
  }

  loadApprovedCenters(): void {
    this.diagnosticCenterService.getApprovedCenters().subscribe({
      next: (res) => {
        this.approvedCenters = res.data || [];
      },
      error: (err) => {
        this.error = 'Failed to load available centers';
      },
    });
  }

  loadMyReports(): void {
    this.reportService.getReports(this.currentUser.username).subscribe({
      next: (res) => {
        this.myReports = res.data || [];
      },
      error: (err) => {
        // this.error = 'Failed to load reports';
      },
    });
  }

  loadCenterTests(centerId: number): void {
    if (!centerId) {
      this.centerTests = [];
      this.bookingForm.testId = 0;
      return;
    }

    // Clear existing data to force fresh fetch
    this.centerTests = [];

    this.diagnosticTestService.getTestsByCenter(centerId).subscribe({
      next: (res) => {
        const list = res.data || [];
        this.centerTests = list.map((ct: any) => ({
          id: ct.id,
          centerId: ct.center?.id,
          testId: ct.test?.id,
          testName: ct.test?.testname || ct.test?.testName,
          testPrice: ct.test?.price ?? ct.test?.testPrice,
          maxBooking: ct.maxBooking ?? ct.slotAvailable ?? 0,
        }));
      },
      error: (err) => {
        this.error = 'Failed to load tests for selected center';
      },
    });
  }

  onCenterChange(centerId: number): void {
    this.bookingForm.centerId = centerId;
    this.bookingForm.testId = 0;
    this.bookingForm.slot = '';
    this.centerTests = [];
    if (centerId) {
      this.loadCenterTests(centerId);
    }
  }

  loadMyCenter(): void {
    this.diagnosticCenterService.getMyCenter().subscribe({
      next: (res) => {
        this.myCenter = res.data || null;
        if (this.myCenter?.id && this.myCenter.status === 'APPROVED') {
          this.loadCenterTests(this.myCenter.id);
        }
      },
      error: () => {
        this.myCenter = null;
      },
    });
  }

  addTestToMyCenter(): void {
    if (!this.myCenter?.id) {
      this.error = 'No approved center available to add tests';
      return;
    }

    if (!this.testToAdd.testId || this.testToAdd.maxBooking <= 0) {
      this.error = 'Please select a test and provide a valid max booking';
      return;
    }

    this.diagnosticTestService.addTestToCenter(this.myCenter.id, this.testToAdd).subscribe({
      next: (res) => {
        this.successMessage = res.message || 'Test added to center successfully!';
        this.loadCenterTests(this.myCenter?.id || 0);
        this.testToAdd = { testId: 0, maxBooking: 1 };
        setTimeout(() => (this.successMessage = ''), 5000);
      },
      error: (err) => {
        this.error = err.error?.message || 'Failed to add test to center';
      },
    });
  }

  createNewTest(): void {
    if (!this.newTest.testName.trim() || this.newTest.testPrice <= 0) {
      this.error = 'Please enter a valid test name and price';
      return;
    }

    this.diagnosticTestService.createTest(this.newTest).subscribe({
      next: (res) => {
        this.successMessage = res.message || 'Test created successfully!';
        this.newTest = { testName: '', description: '', testPrice: 0 };
        this.loadAllTests();
        setTimeout(() => (this.successMessage = ''), 5000);
      },
      error: (err) => {
        this.error = err.error?.message || 'Failed to create test';
      },
    });
  }

  onReportAppointmentChange(appointmentId: number): void {
    this.selectedReportAppointmentId = appointmentId;
    const selected = this.centerAppointments.find((apt) => apt.id === appointmentId);
    this.reportPatientName = selected?.userName || '';
  }

  onReportFileChange(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.reportFile = input.files && input.files.length ? input.files[0] : null;
  }

  onReportFileChangeForAppointment(event: Event, appointmentId: number): void {
    const input = event.target as HTMLInputElement;
    this.reportFiles[appointmentId] = input.files && input.files.length ? input.files[0] : null;
  }

  toggleReportUploadForm(appointmentId: number): void {
    this.showReportUploadForm[appointmentId] = !this.showReportUploadForm[appointmentId];
  }

  uploadReportForAppointment(appointment: Appointment): void {
    const appointmentId = appointment.id || 0;
    const file = this.reportFiles[appointmentId];
    if (!appointmentId || !appointment.userName || !file) {
      this.error = 'Please choose a report file for the selected appointment';
      return;
    }

    this.loading = true;
    this.reportService.uploadReport(appointmentId, appointment.userName, file).subscribe({
      next: (res) => {
        this.successMessage = res.message || 'Report uploaded successfully!';
        this.loading = false;
        this.error = '';
        this.reportFiles[appointmentId] = null;
        this.showReportUploadForm[appointmentId] = false;
        setTimeout(() => (this.successMessage = ''), 5000);
      },
      error: (err) => {
        this.loading = false;
        this.error = err.error?.message || 'Failed to upload report';
      },
    });
  }

  uploadReport(): void {
    if (!this.selectedReportAppointmentId || !this.reportPatientName || !this.reportFile) {
      this.error = 'Please select an appointment and choose a report file';
      return;
    }

    this.loading = true;
    this.reportService.uploadReport(this.selectedReportAppointmentId, this.reportPatientName, this.reportFile).subscribe({
      next: (res) => {
        this.successMessage = res.message || 'Report uploaded successfully!';
        this.loading = false;
        this.error = '';
        this.reportFile = null;
        this.selectedReportAppointmentId = 0;
        this.reportPatientName = '';
        const input = document.getElementById('reportFile') as HTMLInputElement | null;
        if (input) {
          input.value = '';
        }
        setTimeout(() => (this.successMessage = ''), 5000);
      },
      error: (err) => {
        this.loading = false;
        this.error = err.error?.message || 'Failed to upload report';
      },
    });
  }

  boardAppointment(): void {
    if (
      !this.bookingForm.centerId ||
      !this.bookingForm.testId ||
      !this.bookingForm.appointmentDate ||
      !this.bookingForm.slot
    ) {
      this.error = 'Please select center, test, date, and slot';
      return;
    }

    const selectedTest = this.centerTests.find((test) => test.testId === this.bookingForm.testId);
    if (!selectedTest) {
      this.error = 'Selected test is not available';
      return;
    }

    if ((selectedTest.maxBooking ?? 0) <= 0) {
      this.error = 'This test is fully booked and cannot be selected';
      return;
    }

    const request = {
      centerId: Number(this.bookingForm.centerId),
      testId: Number(this.bookingForm.testId),
      appointmentDate: this.bookingForm.appointmentDate,
      slot: this.bookingForm.slot?.toString().trim().toUpperCase(),
    };

    this.loading = true;
    this.appointmentService.bookAppointment(request).subscribe({
      next: (res) => {
        this.successMessage = 'Appointment booked successfully!';
        this.loading = false;
        this.loadMyAppointments();
        this.loadCenterTests(this.bookingForm.centerId);
        setTimeout(() => {
          this.successMessage = '';
          this.activeTab = 'appointments';
        }, 2000);
      },
      error: (err) => {
        this.error = err.error?.message || 'Failed to book appointment';
        this.loading = false;
      },
    });
  }

  cancelAppointment(id: number): void {
    if (
      confirm('Are you sure you want to cancel this appointment?')
    ) {
      this.appointmentService.cancelAppointment(id).subscribe({
        next: (res) => {
          this.successMessage = 'Appointment cancelled successfully';
          this.loadMyAppointments();
          setTimeout(() => (this.successMessage = ''), 2000);
        },
        error: (err) => {
          this.error = 'Failed to cancel appointment';
        },
      });
    }
  }

  downloadReport(reportId: number): void {
    this.reportService.downloadReportWithName(reportId).subscribe({
      next: (response: any) => {
        const url = window.URL.createObjectURL(response.blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = response.filename || `report-${reportId}`;
        link.click();
        window.URL.revokeObjectURL(url);
        this.successMessage = 'Report downloaded successfully!';
        setTimeout(() => (this.successMessage = ''), 3000);
      },
      error: (err) => {
        this.error = 'Failed to download report';
      },
    });
  }


  loadCenterAppointments(): void {
    this.loading = true;
    this.diagnosticCenterService.getCenterAppointments().subscribe({
      next: (res) => {
        this.centerAppointments = res.data || [];
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to load center appointments';
        this.loading = false;
      },
    });
  }

  requestCenter(): void {
    if (!this.centerForm.centerName || !this.centerForm.address) {
      this.error = 'Please fill all fields';
      return;
    }

    console.log('Submitting center request', this.centerForm, 'token present=', !!this.authService.getToken());
    this.loading = true;
    this.diagnosticCenterService
      .requestCenter(this.centerForm)
      .subscribe({
        next: (res) => {
          this.successMessage = 'Center request submitted successfully!';
          this.centerForm = { centerName: '', address: '' };
          this.loading = false;
          setTimeout(() => (this.successMessage = ''), 2000);
        },
        error: (err) => {
          this.error = err.error?.message || 'Failed to request center';
          this.loading = false;
        },
      });
  }

  updateAppointmentStatus(id: number, status: string): void {
    this.diagnosticCenterService
      .updateAppointmentStatus(id, status)
      .subscribe({
        next: (res) => {
          this.successMessage = `Appointment status updated to ${status}`;
          this.loadCenterAppointments();
          if (this.myCenter?.id) {
            this.loadCenterTests(this.myCenter.id);
          }
          setTimeout(() => (this.successMessage = ''), 2000);
        },
        error: (err) => {
          this.error = 'Failed to update appointment status';
        },
      });
  }

  // ============ SUPER ADMIN DASHBOARD ============
  loadSuperAdminDashboard(): void {
    this.loadAllCenters();
    this.loadPendingCenters();
    this.loadAllUsers();
  }

  loadAllCenters(): void {
    this.loading = true;
    this.adminService.getAllCenters().subscribe({
      next: (res) => {
        this.allCenters = res.data || [];
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to load centers';
        this.loading = false;
      },
    });
  }

  loadPendingCenters(): void {
    this.adminService.getPendingCenters().subscribe({
      next: (res) => {
        this.pendingCenters = res.data || [];
      },
      error: (err) => {
        // this.error = 'Failed to load pending centers';
      },
    });
  }

  loadAllUsers(): void {
    this.adminService.getAllUsers().subscribe({
      next: (res) => {
        this.allUsers = res.data || [];
      },
      error: (err) => {
        // this.error = 'Failed to load users';
      },
    });
  }

  approveCenter(id: number): void {
    if (confirm('Approve this diagnostic center?')) {
      this.adminService.approveCenter(id).subscribe({
        next: (res) => {
          this.successMessage = 'Center approved successfully';
          this.loadAllCenters();
          this.loadPendingCenters();
          setTimeout(() => (this.successMessage = ''), 2000);
        },
        error: (err) => {
          this.error = 'Failed to approve center';
        },
      });
    }
  }

  rejectCenter(id: number): void {
    if (confirm('Reject this diagnostic center?')) {
      this.adminService.rejectCenter(id).subscribe({
        next: (res) => {
          this.successMessage = 'Center rejected';
          this.loadAllCenters();
          this.loadPendingCenters();
          setTimeout(() => (this.successMessage = ''), 2000);
        },
        error: (err) => {
          this.error = 'Failed to reject center';
        },
      });
    }
  }

  blockUser(id: number): void {
    if (confirm('Block this user?')) {
      this.adminService.blockUser(id).subscribe({
        next: (res) => {
          this.successMessage = 'User blocked successfully';
          this.loadAllUsers();
          setTimeout(() => (this.successMessage = ''), 2000);
        },
        error: (err) => {
          this.error = 'Failed to block user';
        },
      });
    }
  }

  unblockUser(id: number): void {
    if (confirm('Unblock this user?')) {
      this.adminService.unblockUser(id).subscribe({
        next: (res) => {
          this.successMessage = 'User unblocked successfully';
          this.loadAllUsers();
          setTimeout(() => (this.successMessage = ''), 2000);
        },
        error: (err) => {
          this.error = 'Failed to unblock user';
        },
      });
    }
  }

  resetPassword(): void {
    this.error = '';
    this.successMessage = '';

    if (!this.resetPasswordForm.currentPassword || !this.resetPasswordForm.newPassword || !this.resetPasswordForm.confirmPassword) {
      this.error = 'All password fields are required';
      return;
    }

    if (this.resetPasswordForm.newPassword !== this.resetPasswordForm.confirmPassword) {
      this.error = 'New password and confirm password must match';
      return;
    }

    if (this.resetPasswordForm.newPassword.length < 8) {
      this.error = 'New password must be at least 8 characters long';
      return;
    }

    const username = this.currentUser?.username;
    if (!username) {
      this.error = 'Unable to determine current user';
      return;
    }

    this.loading = true;
    this.authService
      .changePassword({
        username,
        currentPassword: this.resetPasswordForm.currentPassword,
        newPassword: this.resetPasswordForm.newPassword,
        confirmPassword: this.resetPasswordForm.confirmPassword,
      })
      .subscribe({
        next: (res) => {
          this.loading = false;
          if (res.success) {
            this.successMessage = res.message || 'Password updated successfully. Redirecting to login...';
            this.authService.logout();
            setTimeout(() => this.router.navigate(['/login']), 1800);
          } else {
            this.error = res.message || 'Failed to update password';
          }
        },
        error: (err) => {
          this.loading = false;
          this.error = err.error?.message || 'Failed to update password. Please check your current password.';
        },
      });
  }

  getStatusColor(status: string | undefined): string {
    switch (status?.toUpperCase()) {
      case 'CONFIRMED':
        return 'bg-green-100 text-green-800';
      case 'COMPLETED':
        return 'bg-blue-100 text-blue-800';
      case 'CANCELLED':
        return 'bg-red-100 text-red-800';
      case 'PENDING':
        return 'bg-yellow-100 text-yellow-800';
      case 'APPROVED':
        return 'bg-green-100 text-green-800';
      case 'REJECTED':
        return 'bg-red-100 text-red-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  }

  setActiveTab(tab: string): void {
    this.activeTab = tab;
    this.error = '';
    this.successMessage = '';
  }
}
