import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { AirlineService } from '../airline.service';
import { AirlineResponse } from '../models/airline-response';
import { FlightResponse } from 'src/app/flights/models/flight-response';

@Component({
  selector: 'app-airline-details',
  templateUrl: './airline-details.component.html',
  styleUrls: ['./airline-details.component.css']
})
export class AirlineDetailsComponent implements OnInit {
  
  airline!: AirlineResponse;
  flights: FlightResponse[] = []

  errorMessage: string | null = null

  constructor(
    private airlineService: AirlineService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    this.loadAirline(id);
    this.loadFlights(id);
  }

  private loadAirline(id: number) {
    this.airlineService.getAirlineById(id).subscribe({
      next: data => this.airline = data,
      error: err => {
        this.errorMessage = err.error?.message || err?.message ||'Unexpected error';
      }
    });
  }

  private loadFlights(id: number) {
    this.airlineService.getAirlineFlights(id).subscribe({
      next: data => this.flights = data,
      error: err => {
        this.errorMessage = err.error?.message || err?.message ||'Unexpected error';
      }
    });
  }

  goToAirlineList() {
    this.router.navigate(['airlines']);
  }

}
