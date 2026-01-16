import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { ClientService } from '../client.service'
import { ClientResponse } from '../models/client-reponse'


@Component({
  selector: 'app-client-list',
  templateUrl: './client-list.component.html',
  styleUrls: ['./client-list.component.css']
})
export class ClientListComponent implements OnInit {

  clients: ClientResponse[] = [];
  errorMessage: string | null = null;

  constructor(
    private clientService: ClientService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.getClients();
  }

  private getClients() {
    this.errorMessage = null;

    this.clientService.getClients().subscribe({
      next: response => {
        this.clients = response;
        this.sortClients();
      },
      error: err => {
        this.errorMessage = err.error?.message || err?.message ||'Unexpected error';
      }
    });
  }

  clientDetails(id: number) {
    this.router.navigate(['client-details', id]);
  }

  updateClient(id: number) {
    this.router.navigate(['update-client', id]);
  }

  activateClient(id: number) {
    this.clientService.activateClient(id).subscribe({
      next: () => {
        this.getClients();
      },
      error: err => {
        this.errorMessage = err.error?.message || err?.message ||'Unexpected error';
      }
    });
  }

  deactivateClient(id: number) {
    this.clientService.deactivateClient(id).subscribe({
      next: () => {
        this.getClients();
      },
      error: err => {
        this.errorMessage = err.error?.message || err?.message ||'Unexpected error';
      }
    });
  }

  private sortClients() {
    this.clients.sort((a, b) => {
      if (a.status === b.status) {
        return a.id - b.id;
      }
      return a.status ? -1 : 1;
    });
  };
}

