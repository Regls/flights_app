import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { AirportService } from '../airport.service';
import { AirportResponse } from '../models/airport-response';
import { FlightResponse } from '../../flights/models/flight-response';


@Component({
  selector: 'app-airport-details',
  templateUrl: './airport-details.component.html',
  styleUrls: ['./airport-details.component.css']
})
export class AirportDetailsComponent implements OnInit {
  
  airport!: AirportResponse;
  flights: FlightResponse[] = []

  errorMessage: string | null = null
  
  constructor(
    private airportService: AirportService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    this.loadAirport(id);
    this.loadFlights(id);
  }

  private loadAirport(id: number) {
    this.airportService.getAirportById(id).subscribe({
      next: data => this.airport = data,
      error: err => {
        this.errorMessage = err.error?.message || err?.message ||'Unexpected error';
      }
    });
  }

  private loadFlights(id: number) {
    this.airportService.getAirportFlights(id).subscribe({
      next: data => this.flights = data,
      error: err => {
        this.errorMessage = err.error?.message || err?.message ||'Unexpected error';
      }
    });
  }

  goToAirportList() {
    this.router.navigate(['airports']);
  }

}
