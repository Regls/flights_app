import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { AirlineService } from '../airline.service';
import { CreateAirlineRequest } from '../models/create-airline-request';


@Component({
  selector: 'app-create-airline',
  templateUrl: './create-airline.component.html',
  styleUrls: ['./create-airline.component.css']
})
export class CreateAirlineComponent implements OnInit {

  airline: CreateAirlineRequest = {
    iataCode: '',
    airlineName: ''
  };

  errorMessage: string | null = null;
  isSubmitting = false;

  constructor(
    private airlineService: AirlineService,
    private router: Router
  ) {}

  ngOnInit(): void {}

  saveAirline(){
    this.isSubmitting = true;
    this.errorMessage = null;

    this.airlineService.createAirline(this.airline).subscribe({
      next: () => {
        this.goToAirlineList();
      },
      error: err => {
        this.errorMessage = err.error?.message || err?.message ||'Unexpected error';
        this.isSubmitting = false;
      }
    });
  }

  goToAirlineList(){
    this.router.navigate(['/airlines']);
  }
  
  onSubmit(){
    this.saveAirline();
  }
}
