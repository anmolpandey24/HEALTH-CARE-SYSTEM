import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { DiagnosticTest, DiagnosticTestRequest, CenterTest, CenterTestRequest, ApiResponse } from '../models/user-model';

@Injectable({
  providedIn: 'root',
})
export class DiagnosticTestService {
  private baseUrl = 'http://localhost:8080/api/tests';

  constructor(private http: HttpClient) {}

  createTest(request: DiagnosticTestRequest): Observable<ApiResponse<DiagnosticTest>> {
    return this.http.post<ApiResponse<DiagnosticTest>>(`${this.baseUrl}`, request);
  }

  getAllTests(): Observable<ApiResponse<DiagnosticTest[]>> {
    return this.http.get<ApiResponse<DiagnosticTest[]>>(`${this.baseUrl}`);
  }

  addTestToCenter(centerId: number, request: CenterTestRequest): Observable<ApiResponse<CenterTest>> {
    return this.http.post<ApiResponse<CenterTest>>(`${this.baseUrl}/center/${centerId}`, request);
  }

  getTestsByCenter(centerId: number): Observable<ApiResponse<CenterTest[]>> {
    const timestamp = new Date().getTime();
    return this.http.get<ApiResponse<CenterTest[]>>(`${this.baseUrl}/center/${centerId}?t=${timestamp}`);
  }
}
