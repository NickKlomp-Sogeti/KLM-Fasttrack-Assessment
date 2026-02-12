import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of, throwError } from 'rxjs';
import { HolidayList } from './holiday-list';
import { HolidayApi } from '../../../core/api/holiday-api';
import { vi } from 'vitest';

describe('HolidayList', () => {
  let component: HolidayList;
  let fixture: ComponentFixture<HolidayList>;
  let apiMock: any;

  beforeEach(async () => {
    apiMock = {
      list: vi.fn(),
      create: vi.fn(),
      remove: vi.fn(),
    };

    await TestBed.configureTestingModule({
      imports: [HolidayList],
      providers: [
        { provide: HolidayApi, useValue: apiMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(HolidayList);
    component = fixture.componentInstance;
  });

  it('should create component', () => {
    expect(component).toBeTruthy();
  });

  it('should load holidays on init', () => {
    apiMock.list.mockReturnValue(of([]));

    component.ngOnInit();

    expect(apiMock.list).toHaveBeenCalled();
  });

  it('should call create when form is valid', () => {
    apiMock.create.mockReturnValue(of({}));
    apiMock.list.mockReturnValue(of([]));

    component.form.setValue({
      employeeId: 'klm012345',
      holidayLabel: 'Test',
      startOfHoliday: '',
      endOfHoliday: '',
    });

    component.create();

    expect(apiMock.create).toHaveBeenCalled();
  });

  it('should set error when create fails', () => {
    apiMock.create.mockReturnValue(
      throwError(() => ({ error: { message: 'Backend error' } }))
    );

    component.form.setValue({
      employeeId: 'klm012345',
      holidayLabel: 'Test',
      startOfHoliday: '',
      endOfHoliday: '',
    });

    component.create();

    expect(component.error).toBe('Backend error');
  });
});
