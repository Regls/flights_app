import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { BookingService } from '../booking.service';
import { CreateBookingRequest } from '../models/create-booking-request';

import { ClientService } from '../../clients/client.service';
import { FlightService } from '../../flights/flight.service';
import { ClientSummaryResponse } from '../models/client-summary-response';
import { FlightSummaryResponse } from '../models/flight-summary-response';
import { FlightStatus } from '../../flights/models/flight-status.enum';


@Component({
  selector: 'app-create-booking',
  templateUrl: './create-booking.component.html',
  styleUrls: ['./create-booking.component.css']
})
export class CreateBookingComponent implements OnInit {

  booking: CreateBookingRequest = {
    clientId: 0,
    flightId: 0,
    createdAt: ''
  };

  activeClients: ClientSummaryResponse[] = [];
  scheduledFlights: FlightSummaryResponse[] = [];

  errorMessage: string | null = null;
  isSubmitting = false;

  constructor(
    private bookingService: BookingService,
    private clientService: ClientService,
    private flightService: FlightService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadActiveClients();
    this.loadScheduledFlights();
  }

  saveBooking(){
    this.isSubmitting = true;
    this.errorMessage = null;

    this.bookingService.createBooking(this.booking).subscribe({
      next: () => {
        this.goToBookingList();
      },
      error: err => {
        this.errorMessage = err.error?.message || err?.message ||'Unexpected error';
        this.isSubmitting = false;
      }
    });
  }

  goToBookingList(){
    this.router.navigate(['/bookings']);
  }
  
  onSubmit(){
    this.saveBooking();
  }

  private loadActiveClients(): void {
    this.clientService.getClients().subscribe({
      next: data => {
        this.activeClients = data.filter(client => client.status);
      }
    });
  }

  private loadScheduledFlights(): void {
    this.flightService.getFlights().subscribe({
      next: data => {
        this.scheduledFlights = data.filter(flight => flight.status === FlightStatus.SCHEDULED);
      }
    });
  }
}