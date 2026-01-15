import { Component, OnInit } from '@angular/core';
import { Client } from '../client'
import { ClientService } from '../client.service'
import { Router } from '@angular/router';
@Component({
  selector: 'app-client-list',
  templateUrl: './client-list.component.html',
  styleUrls: ['./client-list.component.css']
})
export class ClientListComponent implements OnInit {

  clients: Client[] = [];
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
      next: data => {
        this.clients = data;
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

  deleteClient(id: number) {
    this.clientService.deleteClient(id).subscribe({
      next: () => this.getClients(),
      error: err => {
        this.errorMessage = err.error?.message || err?.message ||'Unexpected error';
      }
    });
  }

  private sortClients() {
    this.clients.sort((a, b) => {
      if (a.active === b.active) {
        return a.id - b.id;
      }
      return a.active ? -1 : 1;
    });
  };
}

