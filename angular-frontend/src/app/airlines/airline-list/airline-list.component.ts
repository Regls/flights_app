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

  deleteAirline(id: number) {
    this.airlineService.deleteAirline(id).subscribe({
      next: () => this.getAirlines(),
      error: err => {
        this.errorMessage = err.error?.message || err?.message ||'Unexpected error';
      }
    });
  }
}
