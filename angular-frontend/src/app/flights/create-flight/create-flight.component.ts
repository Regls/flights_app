import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { FlightService } from '../flight.service';
import { CreateFlightRequest } from '../models/create-flight-request';
import { FlightResponse } from '../models/flight-reponse';


@Component({
  selector: 'app-create-flight',
  templateUrl: './create-flight.component.html',
  styleUrls: ['./create-flight.component.css']
})
export class CreateFlightComponent implements OnInit {

  flight: CreateFlightRequest = {
    flightNumber: '',
    airlineId: 0,
    departureAirportId: 0,
    arrivalAirportId: 0,
    departureTime: '',
    arrivalTime: '',
  };

  flightResponse: FlightResponse;
  errorMessage: string | null = null;
  isSubmitting = false;

  constructor(
    private flightService: FlightService,
    private router: Router
  ) {}

  ngOnInit(): void {}

  saveFlight(){
    this.isSubmitting = true;
    this.errorMessage = null;

    this.flightService.createFlight(this.flight).subscribe({
      next: () => {
        this.goToFlightList();
      },
      error: err => {
        this.errorMessage = err.error?.message || err?.message ||'Unexpected error';
        this.isSubmitting = false;
      }
    });
  }

  goToFlightList(){
    this.router.navigate(['/flights']);
  }
  
  onSubmit(){
    this.saveFlight();
  }
}
