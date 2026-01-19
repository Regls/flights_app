import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { FlightService } from '../flight.service';
import { CreateFlightRequest } from '../models/create-flight-request';

import { AirlineService } from '../../airlines/airline.service';
import { AirportService } from '../../airports/airport.service';
import { AirlineSummaryResponse } from '../models/airline-summary-response';
import { AirportSummaryResponse } from '../models/airport-summary-response';


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

  activeAirlines: AirlineSummaryResponse[] = [];
  operationalAirports: AirportSummaryResponse[] = [];

  errorMessage: string | null = null;
  isSubmitting = false;

  constructor(
    private flightService: FlightService,
    private airlineService: AirlineService,
    private airportService: AirportService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadActiveAirlines();
    this.loadOperationalAirports();
  }

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

  private loadActiveAirlines(): void {
    this.airlineService.getAirlines().subscribe({
      next: data => {
        this.activeAirlines = data.filter(airline => airline.status);
      }
    });
  }

  private loadOperationalAirports(): void {
    this.airportService.getAirports().subscribe({
      next: data => {
        this.operationalAirports = data.filter(airport => airport.operational);
      }
    });
  }

  getDepartureAirports() {
    return this.operationalAirports.filter(
      airport => airport.id !== this.flight.arrivalAirportId
    );
  }

  getArrivalAirports() {
    return this.operationalAirports.filter(
      airport => airport.id !== this.flight.departureAirportId
    );
  }
}