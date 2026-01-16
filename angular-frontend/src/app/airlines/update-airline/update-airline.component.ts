import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { AirlineService } from '../airline.service';
import { AirlineResponse } from '../models/airline-response';
import { UpdateAirlineRequest } from '../models/update-airline-request';


@Component({
  selector: 'app-update-airline',
  templateUrl: './update-airline.component.html',
  styleUrls: ['./update-airline.component.css']
})
export class UpdateAirlineComponent implements OnInit {

  id: number;
  airline: UpdateAirlineRequest = {
    airlineName: ''
  };

  airlineResponse: AirlineResponse;

  errorMessage: string | null = null;
  isSubmitting = false;

  constructor(
    private airlineService: AirlineService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.id = Number(this.route.snapshot.paramMap.get('id'));

    this.airlineService.getAirlineById(this.id).subscribe({
      next: response => {
          this.airline = response;
          this.airline.airlineName = response.airlineName
      },
      error: err => {
        this.errorMessage = err.error?.message || err?.message ||'Unexpected error';
        this.isSubmitting = false;
      }
    });
  }

  saveAirline(): void {
    this.isSubmitting = true;
    this.errorMessage = null;

    
    this.airlineService.updateAirline(this.id, this.airline).subscribe({
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
