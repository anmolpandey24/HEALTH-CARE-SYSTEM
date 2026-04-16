import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { DiagnosticCenter, User, ApiResponse } from '../models/user-model';

@Injectable({
  providedIn: 'root',
})
export class AdminService {
  private baseUrl = 'http://localhost:8080/api/admin';

  constructor(private http: HttpClient) {}

  getAllCenters(): Observable<ApiResponse<DiagnosticCenter[]>> {
    return this.http.get<ApiResponse<DiagnosticCenter[]>>(`${this.baseUrl}/centers`);
  }

  getPendingCenters(): Observable<ApiResponse<DiagnosticCenter[]>> {
    return this.http.get<ApiResponse<DiagnosticCenter[]>>(`${this.baseUrl}/centers/pending`);
  }

  approveCenter(id: number): Observable<ApiResponse<any>> {
    return this.http.put<ApiResponse<any>>(`${this.baseUrl}/centers/${id}/approve`, {});
  }

  rejectCenter(id: number): Observable<ApiResponse<any>> {
    return this.http.put<ApiResponse<any>>(`${this.baseUrl}/centers/${id}/reject`, {});
  }

  getAllUsers(): Observable<ApiResponse<User[]>> {
    return this.http.get<ApiResponse<User[]>>(`${this.baseUrl}/users`);
  }

  blockUser(id: number): Observable<ApiResponse<any>> {
    return this.http.put<ApiResponse<any>>(`${this.baseUrl}/users/${id}/block`, {});
  }

  unblockUser(id: number): Observable<ApiResponse<any>> {
    return this.http.put<ApiResponse<any>>(`${this.baseUrl}/users/${id}/unblock`, {});
  }
}
