import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { AirportService } from '../airport.service';
import { AirportResponse } from '../models/airport-response';
import { UpdateAirportRequest } from '../models/update-airport-request';


@Component({
  selector: 'app-update-airport',
  templateUrl: './update-airport.component.html',
  styleUrls: ['./update-airport.component.css']
})
export class UpdateAirportComponent implements OnInit {

  id: number;
  airport: UpdateAirportRequest ={
    airportName: ''
  };

  airportResponse: AirportResponse;

  errorMessage: string | null = null;
  isSubmitting = false;

  constructor(
    private airportService: AirportService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.id = Number(this.route.snapshot.paramMap.get('id'));

    this.airportService.getAirportById(this.id).subscribe({
      next: response => {
          this.airportResponse = response;
          this.airport.airportName = response.airportName;
      },
      error: err => {
        this.errorMessage = err.error?.message || err?.message ||'Unexpected error';
        this.isSubmitting = false;
      }
    });
  }

  saveAirport(): void {
    this.isSubmitting = true;
    this.errorMessage = null;

    
    this.airportService.updateAirport(this.id, this.airport).subscribe({
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
