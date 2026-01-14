import { Component, OnInit } from '@angular/core';
import { AirlineService } from '../airline.service';
import { Airline } from '../airline';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-update-airline',
  templateUrl: './update-airline.component.html',
  styleUrls: ['./update-airline.component.css']
})
export class UpdateAirlineComponent implements OnInit {

  id: number;
  airline: Airline = new Airline();
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
      next: data => {
          this.airline = data;
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
