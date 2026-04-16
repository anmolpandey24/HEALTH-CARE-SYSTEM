import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Report, ApiResponse } from '../models/user-model';

@Injectable({
  providedIn: 'root',
})
export class ReportService {
  private baseUrl = 'http://localhost:8080/api/reports';

  constructor(private http: HttpClient) {}

  uploadReport(appointmentId: number, patientName: string, file: File): Observable<ApiResponse<Report>> {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('patientName', patientName);
    return this.http.post<ApiResponse<Report>>(`${this.baseUrl}/upload/${appointmentId}`, formData);
  }

  getReports(patientName: string): Observable<ApiResponse<Report[]>> {
    return this.http.get<ApiResponse<Report[]>>(`${this.baseUrl}?patientName=${patientName}`);
  }

  downloadReport(id: number): Observable<Blob> {
    return this.http.get(`${this.baseUrl}/download/${id}`, { responseType: 'blob' });
  }

  downloadReportWithName(id: number): Observable<{ blob: Blob; filename: string }> {
    return this.http.get(`${this.baseUrl}/download/${id}`, {
      responseType: 'blob',
      observe: 'response',
    }).pipe(
      map((response: any) => {
        const contentDisposition = response.headers.get('content-disposition');
        let filename = `report-${id}`;
        if (contentDisposition) {
          const matches = contentDisposition.match(/filename="([^"]+)"/);
          if (matches && matches.length > 1) {
            filename = matches[1];
          }
        }
        return { blob: response.body, filename };
      })
    );
  }
}
