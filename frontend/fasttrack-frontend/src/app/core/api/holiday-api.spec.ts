import { TestBed } from '@angular/core/testing';

import { HolidayApi } from './holiday-api';

describe('HolidayApi', () => {
  let service: HolidayApi;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HolidayApi);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
