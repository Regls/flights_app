import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { FlightService } from '../flight.service'
import { FlightResponse } from '../models/flight-reponse'
import { FlightStatus } from '../models/flight-status.enum';


@Component({
  selector: 'app-flight-list',
  templateUrl: './flight-list.component.html',
  styleUrls: ['./flight-list.component.css']
})
export class FlightListComponent implements OnInit {
  FlightStatus = FlightStatus;

  flights: FlightResponse[] = [];
  errorMessage: string | null = null;

  constructor(
    private flightService: FlightService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.getFlights();
  }

  private getFlights() {
    this.errorMessage = null;

    this.flightService.getFlights().subscribe({
      next: response => {
        this.flights = response;
        this.sortFlights();
      },
      error: err => {
        this.errorMessage = err.error?.message || err?.message ||'Unexpected error';
      }
    });
  }

  flightDetails(id: number) {
    this.router.navigate(['flight-details', id]);
  }

  departFlight(id: number) {
    this.flightService.departFlight(id).subscribe({
      next: () => {
        this.getFlights();
      },
      error: err => {
        this.errorMessage = err.error?.message || err?.message ||'Unexpected error';
      }
    });
  }

  arriveFlight(id: number) {
    this.flightService.arriveFlight(id).subscribe({
      next: () => {
        this.getFlights();
      },
      error: err => {
        this.errorMessage = err.error?.message || err?.message ||'Unexpected error';
      }
    });
  }

  cancelFlight(id: number) {
    this.flightService.cancelFlight(id).subscribe({
      next: () => {
        this.getFlights();
      },
      error: err => {
        this.errorMessage = err.error?.message || err?.message ||'Unexpected error';
      }
    });
  }

  getStatusClass(status: FlightStatus): string {
    switch (status) {
      case FlightStatus.SCHEDULED:
        return 'text-primary';
      case FlightStatus.IN_FLIGHT:
        return 'text-warning';
      case FlightStatus.ARRIVED:
        return 'text-success';
      case FlightStatus.CANCELLED:
        return 'text-danger';
      default:
        return '';
    }
  }

  private sortFlights() {
    const order: Record<FlightStatus, number> = {
      [FlightStatus.SCHEDULED]: 1,
      [FlightStatus.IN_FLIGHT]: 2,
      [FlightStatus.ARRIVED]: 3,
      [FlightStatus.CANCELLED]: 4
    };

    this.flights.sort((a, b) => order[a.status] - order[b.status]);
  }
}
