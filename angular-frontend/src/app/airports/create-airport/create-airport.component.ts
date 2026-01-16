import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { AirportService } from '../airport.service';
import { CreateAirportRequest } from '../models/create-airport-request';


@Component({
  selector: 'app-create-airport',
  templateUrl: './create-airport.component.html',
  styleUrls: ['./create-airport.component.css']
})
export class CreateAirportComponent implements OnInit {

  airport: CreateAirportRequest = {
    iataCode: '',
    airportName: '',
    city: ''
  };

  errorMessage: string | null = null;
  isSubmitting = false;

  constructor(
    private airportService: AirportService,
    private router: Router
  ) {}

  ngOnInit(): void {}

  saveAirport(){
    this.isSubmitting = true;
    this.errorMessage = null;

    this.airportService.createAirport(this.airport).subscribe({
      next: () => {
        this.goToAirportList();
      },
      error: err => {
        this.errorMessage = err.error?.message || err?.message ||'Unexpected error';
        this.isSubmitting = false;
      }
    });
  }

  goToAirportList(){
    this.router.navigate(['/airports']);
  }
  
  onSubmit(){
    this.saveAirport();
  }
}
