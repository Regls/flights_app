import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { AirlineService } from '../airline.service'
import { AirlineResponse } from '../models/airline-response'


@Component({
  selector: 'app-airline-list',
  templateUrl: './airline-list.component.html',
  styleUrls: ['./airline-list.component.css']
})
export class AirlineListComponent implements OnInit {

  airlines: AirlineResponse[] = [];
  errorMessage: string | null = null;

  constructor(
    private airlineService: AirlineService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.getAirlines();
  }

  private getAirlines() {
    this.errorMessage = null;

    this.airlineService.getAirlines().subscribe({
      next: response => {
        this.airlines = response;
        this.sortAirlines();
      },
      error: err => {
        this.errorMessage = err.error?.message || err?.message ||'Unexpected error';
      }
    });
  }

  airlineDetails(id: number) {
    this.router.navigate(['airline-details', id]);
  }

  updateAirline(id: number) {
    this.router.navigate(['update-airline', id]);
  }

  activateAirline(id: number) {
    this.airlineService.activateAirline(id).subscribe({
      next: () => {
        this.getAirlines();
      },
      error: err => {
        this.errorMessage = err.error?.message || err?.message ||'Unexpected error';
      }
    });
  }

  suspendAirline(id: number) {
    this.airlineService.suspendAirline(id).subscribe({
      next: () => {
        this.getAirlines();
      },
      error: err => {
        this.errorMessage = err.error?.message || err?.message ||'Unexpected error';
      }
    });
  }

  private sortAirlines() {
    this.airlines.sort((a, b) => {
      if (a.status === b.status) {
        return a.id - b.id;
      }
      return a.status ? -1 : 1;
    });
  };
}
