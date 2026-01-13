import { Component, OnInit } from '@angular/core';
import { Airport } from '../airport';
import { ActivatedRoute, Router } from '@angular/router';
import { AirportService } from '../airport.service';

@Component({
  selector: 'app-airport-details',
  templateUrl: './airport-details.component.html',
  styleUrls: ['./airport-details.component.css']
})
export class AirportDetailsComponent implements OnInit {
  
  airport!: Airport
  errorMessage: string | null = null
  constructor(
    private airportService: AirportService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    this.airportService.getAirportById(id).subscribe({
      next: data => (this.airport = data),
      error: err => {
        this.errorMessage = err.error?.message || err?.message ||'Unexpected error';
      }
    });
  }

  goToAirportList() {
    this.router.navigate(['airports']);
  }

}
