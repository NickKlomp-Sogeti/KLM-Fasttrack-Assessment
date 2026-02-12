import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  ReactiveFormsModule,
  Validators,
  UntypedFormBuilder,
  UntypedFormGroup,
} from '@angular/forms';
import { HolidayApi, HolidayDto } from '../../../core/api/holiday-api';

@Component({
  selector: 'app-holiday-list',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './holiday-list.html',
  styleUrls: ['./holiday-list.css'],
})
export class HolidayList implements OnInit {
  holidays: HolidayDto[] = [];
  loading = false;
  error: string | null = null;

  form: UntypedFormGroup;

  constructor(private api: HolidayApi, private fb: UntypedFormBuilder) {
    this.form = this.fb.group({
      employeeId: ['', [Validators.required, Validators.pattern(/^klm[0-9]{6}$/)]],
      holidayLabel: ['', [Validators.required]],
      startOfHoliday: ['', [Validators.required]],
      endOfHoliday: ['', [Validators.required]],
    });
  }

  ngOnInit(): void {
    this.refresh();
  }

  refresh(): void {
    this.loading = true;
    this.error = null;

    this.api.list().subscribe({
      next: (data: HolidayDto[]) => {
        this.holidays = data ?? [];
        this.loading = false;
      },
      error: (err: any) => {
        this.error = err?.error?.message ?? 'Failed to load holidays';
        this.loading = false;
      },
    });
  }

  create(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.error = null;

    // datetime-local gives "YYYY-MM-DDTHH:mm" (no timezone)
    // Convert to UTC ISO-8601 so Spring can parse it reliably.
    const v: any = this.form.getRawValue();
    const req = {
      employeeId: v.employeeId,
      holidayLabel: v.holidayLabel,
      startOfHoliday: new Date(v.startOfHoliday).toISOString(),
      endOfHoliday: new Date(v.endOfHoliday).toISOString(),
    };

    this.api.create(req).subscribe({
      next: () => {
        this.form.reset();
        this.refresh();
      },
      error: (err: any) => {
        this.error = err?.error?.message ?? 'Failed to create holiday';
      },
    });
  }

  remove(id: string): void {
    if (!confirm('Cancel this holiday?')) return;

    this.error = null;

    this.api.delete(id).subscribe({
      next: () => this.refresh(),
      error: (err: any) => {
        this.error = err?.error?.message ?? 'Failed to cancel holiday';
      },
    });
  }
}
