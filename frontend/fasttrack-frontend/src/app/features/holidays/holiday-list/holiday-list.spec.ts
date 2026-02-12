import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HolidayList } from './holiday-list';

describe('HolidayList', () => {
  let component: HolidayList;
  let fixture: ComponentFixture<HolidayList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HolidayList]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HolidayList);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
