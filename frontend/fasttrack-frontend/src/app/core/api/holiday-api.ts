import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export type HolidayStatus = 'DRAFT' | 'REQUESTED' | 'SCHEDULED' | 'ARCHIVED';

export interface HolidayDto {
  holidayId: string;
  holidayLabel: string;
  employeeId: string;
  startOfHoliday?: string;
  endOfHoliday?: string;
  status: HolidayStatus;
}

export interface CreateHolidayRequest {
  holidayLabel: string;
  employeeId: string;
  startOfHoliday?: string;
  endOfHoliday?: string;
}

@Injectable({
  providedIn: 'root',
})
export class HolidayApi {
  private readonly baseUrl = 'http://localhost:8080/holidays';

  constructor(private http: HttpClient) {}

  list(): Observable<HolidayDto[]> {
    return this.http.get<HolidayDto[]>(this.baseUrl);
  }

  create(req: CreateHolidayRequest): Observable<HolidayDto> {
    return this.http.post<HolidayDto>(this.baseUrl, req);
  }

  delete(holidayId: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${holidayId}`);
  }
}
