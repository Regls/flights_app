import { Component, OnInit } from '@angular/core';
import { Airline } from '../airline'
import { AirlineService } from '../airline.service'
import { Router } from '@angular/router';
@Component({
  selector: 'app-airline-list',
  templateUrl: './airline-list.component.html',
  styleUrls: ['./airline-list.component.css']
})
export class AirlineListComponent implements OnInit {

  airlines: Airline[] = [];
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
      next: data => {
        this.airlines = data;
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

  deleteAirline(id: number) {
    this.airlineService.deleteAirline(id).subscribe({
      next: () => this.getAirlines(),
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
