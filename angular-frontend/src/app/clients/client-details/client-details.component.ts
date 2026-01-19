import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { ClientService } from '../client.service';
import { ClientResponse } from '../models/client-reponse';
import { BookingResponse } from 'src/app/bookings/models/booking-response';


@Component({
  selector: 'app-client-details',
  templateUrl: './client-details.component.html',
  styleUrls: ['./client-details.component.css']
})
export class ClientDetailsComponent implements OnInit {
  
  client!: ClientResponse;
  bookings: BookingResponse[] = []

  errorMessage: string | null = null;
  
  constructor(
    private clientService: ClientService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    this.loadClient(id);
    this.loadBookings(id);
  }

  private loadClient(id: number) {
    this.clientService.getClientById(id).subscribe({
      next: data => this.client = data,
      error: err => {
        this.errorMessage = err.error?.message || err?.message ||'Unexpected error';
      }
    });
  }

  private loadBookings(id: number) {
    this.clientService.getClientBookings(id).subscribe({
      next: data => this.bookings = data,
      error: err => {
        this.errorMessage = err.error?.message || err?.message ||'Unexpected error';
      }
    });
  }

  goToClientList() {
    this.router.navigate(['clients']);
  }

}
