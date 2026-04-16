import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LoginRequest, RegisterRequest, ApiResponse, User, LoginResponse, ChangePasswordRequest } from '../models/user-model';
import { Observable, BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private baseUrl = 'http://localhost:8080/api/auth';
  private currentUserSubject: BehaviorSubject<any>;
  public currentUser$: Observable<any>;

  constructor(private http: HttpClient) {
    this.currentUserSubject = new BehaviorSubject<any>(this.getUserFromStorage());
    this.currentUser$ = this.currentUserSubject.asObservable();
  }

  registerUser(data: RegisterRequest): Observable<ApiResponse<any>> {
    return this.http.post<ApiResponse<any>>(`${this.baseUrl}/register-user`, data);
  }

  registerAdmin(data: RegisterRequest): Observable<ApiResponse<any>> {
    return this.http.post<ApiResponse<any>>(`${this.baseUrl}/register-admin`, data);
  }

  login(data: LoginRequest): Observable<ApiResponse<LoginResponse>> {
    return this.http.post<ApiResponse<LoginResponse>>(`${this.baseUrl}/login`, data);
  }

  changePassword(data: ChangePasswordRequest): Observable<ApiResponse<any>> {
    return this.http.post<ApiResponse<any>>(`${this.baseUrl}/change-password`, data);
  }

  sendOtp(email: string): Observable<any> {
    return this.http.post(`http://localhost:8080/api/otp/send?email=${encodeURIComponent(email)}`, {}, { responseType: 'text' });
  }

  verifyOtp(email: string, otp: string): Observable<any> {
    return this.http.post(`http://localhost:8080/api/otp/verify?email=${encodeURIComponent(email)}&otp=${encodeURIComponent(otp)}`, {}, { responseType: 'text' });
  }

  saveToken(token: string) {
    localStorage.setItem('token', token);
  }

  getToken() {
    return localStorage.getItem('token');
  }

  saveUser(user: any) {
    localStorage.setItem('user', JSON.stringify(user));
    this.currentUserSubject.next(user);
  }

  getUserFromStorage() {
    const userStr = localStorage.getItem('user');
    return userStr ? JSON.parse(userStr) : null;
  }

  getCurrentUser() {
    return this.currentUserSubject.value;
  }

  getRole(): string {
    const user = this.getCurrentUser();
    return user?.role || null;
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }

  getUsername(): string {
    const user = this.getCurrentUser();
    return user?.username || '';
  }

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    this.currentUserSubject.next(null);
  }
}
