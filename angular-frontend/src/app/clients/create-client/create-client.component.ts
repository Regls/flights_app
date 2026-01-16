import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { ClientService } from '../client.service';
import { CreateClientRequest } from '../models/create-client-request';


@Component({
  selector: 'app-create-client',
  templateUrl: './create-client.component.html',
  styleUrls: ['./create-client.component.css']
})
export class CreateClientComponent implements OnInit {

  client: CreateClientRequest = {
    cpf: '',
    clientFirstName: '',
    clientLastName: ''
  };

  errorMessage: string | null = null;
  isSubmitting = false;

  constructor(
    private clientService: ClientService,
    private router: Router
  ) {}

  ngOnInit(): void {}

  saveClient(){
    this.isSubmitting = true;
    this.errorMessage = null;

    this.clientService.createClient(this.client).subscribe({
      next: () => {
        this.goToClientList();
      },
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
