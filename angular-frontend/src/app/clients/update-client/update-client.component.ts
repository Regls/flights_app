import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { ClientService } from '../client.service';
import { ClientResponse } from '../models/client-reponse';
import { UpdateClientRequest } from '../models/update-client-request';


@Component({
  selector: 'app-update-client',
  templateUrl: './update-client.component.html',
  styleUrls: ['./update-client.component.css']
})
export class UpdateClientComponent implements OnInit {

  id: number;
  client: UpdateClientRequest = {
    clientFirstName: '',
    clientLastName: ''
  };

  clientResponse: ClientResponse;

  errorMessage: string | null = null;
  isSubmitting = false;

  constructor(
    private clientService: ClientService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
  this.id = Number(this.route.snapshot.paramMap.get('id'));

  this.clientService.getClientById(this.id).subscribe({
    next: response => {
      this.clientResponse = response;
      this.client.clientFirstName = response.firstName,
      this.client.clientLastName = response.lastName
    },
    error: err => {
      this.errorMessage = err.error?.message || 'Unexpected error';
      this.isSubmitting = false;
    }
  });
}

  saveClient(): void {
    this.isSubmitting = true;
    this.errorMessage = null;

    
    this.clientService.updateClient(this.id, this.client).subscribe({
      next: () => this.goToClientList(),
      error: err => {
        this.errorMessage = err.error?.message || err?.message ||'Unexpected error';
        this.isSubmitting = false;
      }
    });
  }

  goToClientList(){
    this.router.navigate(['/clients']);
  }

  onSubmit(){
    this.saveClient();
  }
}
