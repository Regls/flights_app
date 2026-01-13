import { Component, OnInit } from '@angular/core';
import { AirportService } from '../airport.service';
import { Airport } from '../airport';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-update-airport',
  templateUrl: './update-airport.component.html',
  styleUrls: ['./update-airport.component.css']
})
export class UpdateAirportComponent implements OnInit {

  id: number;
  airport: Airport = new Airport();
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
      next: data => {
          this.airport = data;
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
