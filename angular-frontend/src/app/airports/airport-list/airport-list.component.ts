import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { AirportService } from '../airport.service'
import { AirportResponse } from '../models/airport-response'


@Component({
  selector: 'app-airport-list',
  templateUrl: './airport-list.component.html',
  styleUrls: ['./airport-list.component.css']
})
export class AirportListComponent implements OnInit {

  airports: AirportResponse[] = [];
  errorMessage: string | null = null;

  constructor(
    private airportService: AirportService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.getAirports();
  }

  private getAirports() {
    this.errorMessage = null;

    this.airportService.getAirports().subscribe({
      next: response => {
        this.airports = response;
        this.sortAirports();
      },
      error: err => {
        this.errorMessage = err.error?.message || err?.message ||'Unexpected error';
      }
    });
  }

  airportDetails(id: number) {
    this.router.navigate(['airport-details', id]);
  }

  updateAirport(id: number) {
    this.router.navigate(['update-airport', id]);
  }

  openAirport(id: number) {
    this.airportService.openAirport(id).subscribe({
      next: () => {
        this.getAirports();
      },
      error: err => {
        this.errorMessage = err.error?.message || err?.message ||'Unexpected error';
      }
    });
  }

  closeAirport(id: number) {
    this.airportService.closeAirport(id).subscribe({
      next: () => {
        this.getAirports();
      },
      error: err => {
        this.errorMessage = err.error?.message || err?.message ||'Unexpected error';
      }
    })
  }

  private sortAirports() {
    this.airports.sort((a, b) => {
      if (a.operational === b.operational) {
        return a.id - b.id;
      }
      return a.operational ? -1 : 1;
    });
  };
}
