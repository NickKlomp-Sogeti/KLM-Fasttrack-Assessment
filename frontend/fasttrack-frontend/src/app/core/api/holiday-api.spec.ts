import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { HolidayApi } from './holiday-api';

describe('HolidayApi', () => {
  let service: HolidayApi;
  let httpMock: HttpTestingController;

  const baseUrl = `http://localhost:8080/holidays`;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
      ],
    });

    service = TestBed.inject(HolidayApi);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should call GET /holidays on list()', () => {
    service.list().subscribe();

    const req = httpMock.expectOne(baseUrl);
    expect(req.request.method).toBe('GET');

    req.flush([]);
  });

  it('should call POST /holidays on create()', () => {
    const payload = {
      employeeId: 'klm012345',
      holidayLabel: 'Test Holiday',
    };

    service.create(payload).subscribe();

    const req = httpMock.expectOne(baseUrl);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(payload);

    req.flush({ ...payload, holidayId: '123', status: 'DRAFT' });
  });

  it('should call DELETE /holidays/:id on remove()', () => {
    const id = '123';

    service.delete(id).subscribe();

    const req = httpMock.expectOne(`${baseUrl}/${id}`);
    expect(req.request.method).toBe('DELETE');

    req.flush(null);
  });
});
