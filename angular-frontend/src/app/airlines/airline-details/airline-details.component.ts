import { Component, OnInit } from '@angular/core';
import { Airline } from '../airline';
import { ActivatedRoute, Router } from '@angular/router';
import { AirlineService } from '../airline.service';

@Component({
  selector: 'app-airline-details',
  templateUrl: './airline-details.component.html',
  styleUrls: ['./airline-details.component.css']
})
export class AirlineDetailsComponent implements OnInit {
  
  airline!: Airline
  errorMessage: string | null = null
  constructor(
    private airlineService: AirlineService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    this.airlineService.getAirlineById(id).subscribe({
      next: data => (this.airline = data),
      error: err => {
        this.errorMessage = err.error?.message || err?.message ||'Unexpected error';
      }
    });
  }

  goToAirlineList() {
    this.router.navigate(['airlines']);
  }

}
