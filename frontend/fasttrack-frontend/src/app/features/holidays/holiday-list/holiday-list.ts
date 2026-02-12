import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  ReactiveFormsModule,
  Validators,
  NonNullableFormBuilder,
  FormGroup,
} from '@angular/forms';
import { finalize } from 'rxjs/operators';
import {
  HolidayApi,
  HolidayDto,
  CreateHolidayRequest,
} from '../../../core/api/holiday-api';

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

  form: FormGroup;

  constructor(private api: HolidayApi, private fb: NonNullableFormBuilder) {
    // start/end are optional: no Validators.required
    this.form = this.fb.group({
      employeeId: ['', [Validators.required, Validators.pattern(/^klm[0-9]{6}$/)]],
      holidayLabel: ['', Validators.required],
      startOfHoliday: [''],
      endOfHoliday: [''],
    });
  }

  ngOnInit(): void {
    this.loadHolidays();
  }

  private loadHolidays(): void {
    this.setBusy(true);

    this.api
      .list()
      .pipe(finalize(() => this.setBusy(false)))
      .subscribe({
        next: (data) => (this.holidays = data ?? []),
        error: (err) => (this.error = this.extractErrorMessage(err, 'Failed to load holidays.')),
      });
  }

  create(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.setBusy(true);

    const v = this.form.getRawValue();

    const payload: CreateHolidayRequest = {
      employeeId: v.employeeId.trim().toLowerCase(),
      holidayLabel: v.holidayLabel.trim(),
    };

    const startIso = this.toIsoIfProvided(v.startOfHoliday);
    if (startIso) payload.startOfHoliday = startIso;

    const endIso = this.toIsoIfProvided(v.endOfHoliday);
    if (endIso) payload.endOfHoliday = endIso;

    this.api
      .create(payload)
      .pipe(finalize(() => this.setBusy(false)))
      .subscribe({
        next: () => {
          this.form.reset();
          this.loadHolidays();
        },
        error: (err) => (this.error = this.extractErrorMessage(err, 'Failed to create holiday.')),
      });
  }

  remove(holidayId: string): void {
    if (!confirm('Cancel this holiday?')) return;

    this.setBusy(true);

    this.api
      .delete(holidayId)
      .pipe(finalize(() => this.setBusy(false)))
      .subscribe({
        next: () => this.loadHolidays(),
        error: (err) => (this.error = this.extractErrorMessage(err, 'Failed to cancel holiday.')),
      });
  }

  private setBusy(isBusy: boolean): void {
    this.loading = isBusy;
    if (isBusy) this.error = null;
  }

  private toIsoIfProvided(value: unknown): string | null {
    if (typeof value !== 'string') return null;
    const trimmed = value.trim();
    if (!trimmed) return null;

    const date = new Date(trimmed);
    return Number.isNaN(date.getTime()) ? null : date.toISOString();
  }

  private extractErrorMessage(err: unknown, fallback: string): string {
    const e = err as any;
    if (e?.error?.message) return e.error.message;
    if (typeof e?.error === 'string' && e.error.trim()) return e.error;
    if (typeof e?.message === 'string' && e.message.trim()) return e.message;
    return fallback;
  }
}
