import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

export interface Application {
  id?: number;
  source?: string;
  url: string;
  company?: string;
  title?: string;
  location?: string;
  description?: string;
  salaryMin?: number;
  salaryMax?: number;
  currency?: string;
  status?: string;
  appliedAt?: string;
}

@Injectable({ providedIn: 'root' })
export class ApiService {
  private applicationsBaseUrl = 'http://localhost:8081';
  private importBaseUrl = 'http://localhost:8082';

  constructor(private http: HttpClient) {}

  listApplications() {
    return this.http.get<Application[]>(`${this.applicationsBaseUrl}/applications`);
  }

  importByUrl(url: string) {
    return this.http.post<Application>(`${this.importBaseUrl}/import`, { url });
  }
}
