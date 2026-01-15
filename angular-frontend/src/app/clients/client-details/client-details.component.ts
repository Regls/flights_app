import { Component, OnInit } from '@angular/core';
import { ClientResponse } from '../models/client-reponse';
import { ActivatedRoute, Router } from '@angular/router';
import { ClientService } from '../client.service';

@Component({
  selector: 'app-client-details',
  templateUrl: './client-details.component.html',
  styleUrls: ['./client-details.component.css']
})
export class ClientDetailsComponent implements OnInit {
  
  client!: ClientResponse
  errorMessage: string | null = null
  constructor(
    private clientService: ClientService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    this.clientService.getClientById(id).subscribe({
      next: data => (this.client = data),
      error: err => {
        this.errorMessage = err.error?.message || err?.message ||'Unexpected error';
      }
    });
  }

  goToClientList() {
    this.router.navigate(['clients']);
  }

}
