import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { DiagnosticCenter, CenterRequest, CenterTest, CenterTestRequest, Appointment, ApiResponse } from '../models/user-model';

@Injectable({
  providedIn: 'root',
})
export class DiagnosticCenterService {
  private baseUrl = 'http://localhost:8080/api/centers';

  constructor(private http: HttpClient) {}

  requestCenter(request: CenterRequest): Observable<ApiResponse<DiagnosticCenter>> {
    return this.http.post<ApiResponse<DiagnosticCenter>>(`${this.baseUrl}/request`, request);
  }

  getApprovedCenters(): Observable<ApiResponse<DiagnosticCenter[]>> {
    return this.http.get<ApiResponse<DiagnosticCenter[]>>(`${this.baseUrl}/approved`);
  }

  getMyCenter(): Observable<ApiResponse<DiagnosticCenter>> {
    return this.http.get<ApiResponse<DiagnosticCenter>>(`${this.baseUrl}/me`);
  }

  addTestToCenter(centerId: number, request: CenterTestRequest): Observable<ApiResponse<CenterTest>> {
    return this.http.post<ApiResponse<CenterTest>>(`${this.baseUrl}/${centerId}/add-test`, request);
  }

  removeTestFromCenter(centerId: number, testId: number): Observable<ApiResponse<any>> {
    return this.http.delete<ApiResponse<any>>(`${this.baseUrl}/${centerId}/remove-test/${testId}`);
  }

  getCenterTests(centerId: number): Observable<ApiResponse<CenterTest[]>> {
    return this.http.get<ApiResponse<CenterTest[]>>(`${this.baseUrl}/${centerId}/tests`);
  }

  getCenterAppointments(): Observable<ApiResponse<Appointment[]>> {
    return this.http.get<ApiResponse<Appointment[]>>(`${this.baseUrl}/appointments`);
  }

  updateAppointmentStatus(id: number, status: string): Observable<ApiResponse<any>> {
    return this.http.put<ApiResponse<any>>(`${this.baseUrl}/appointments/${id}/status?status=${encodeURIComponent(status)}`, {});
  }
}
