import { Component, OnInit } from '@angular/core';
import { Airport } from '../airport';
import { AirportService } from '../airport.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-create-airport',
  templateUrl: './create-airport.component.html',
  styleUrls: ['./create-airport.component.css']
})
export class CreateAirportComponent implements OnInit {

  airport: Airport = new Airport();
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
