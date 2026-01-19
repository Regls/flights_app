import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { BookingService } from '../booking.service'
import { BookingResponse } from '../models/booking-response'
import { BookingStatus } from '../models/booking-status.enum';


@Component({
  selector: 'app-booking-list',
  templateUrl: './booking-list.component.html',
  styleUrls: ['./booking-list.component.css']
})
export class BookingListComponent implements OnInit {
  BookingStatus = BookingStatus;

  bookings: BookingResponse[] = [];
  errorMessage: string | null = null;

  constructor(
    private bookingService: BookingService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.getBookings();
  }

  private getBookings() {
    this.errorMessage = null;

    this.bookingService.getBookings().subscribe({
      next: response => {
        this.bookings = response;
        this.sortBookings();
      },
      error: err => {
        this.errorMessage = err.error?.message || err?.message ||'Unexpected error';
      }
    });
  }

  bookingDetails(id: number) {
    this.router.navigate(['booking-details', id]);
  }

  confirmBooking(id: number) {
    this.bookingService.confirmBooking(id).subscribe({
      next: () => {
        this.getBookings();
      },
      error: err => {
        this.errorMessage = err.error?.message || err?.message ||'Unexpected error';
      }
    });
  }

  cancelBooking(id: number) {
    this.bookingService.cancelBooking(id).subscribe({
      next: () => {
        this.getBookings();
      },
      error: err => {
        this.errorMessage = err.error?.message || err?.message ||'Unexpected error';
      }
    });
  }

  getStatusClass(status: BookingStatus): string {
    switch (status) {
      case BookingStatus.CREATED:
        return 'text-primary';
      case BookingStatus.CONFIRMED:
        return 'text-warning';
      case BookingStatus.CANCELLED:
        return 'text-danger';
      default:
        return '';
    }
  }

  private sortBookings() {
    const order: Record<BookingStatus, number> = {
      [BookingStatus.CREATED]: 1,
      [BookingStatus.CONFIRMED]: 2,
      [BookingStatus.CANCELLED]: 3
    };

    this.bookings.sort((a, b) => order[a.status] - order[b.status]);
  }
}
