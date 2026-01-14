import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http'
import { Client } from './client';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class ClientService {
    
    private baseUrl = 'http://localhost:8080/api/v1/clients'

    constructor(private http: HttpClient) {}

    getClients() {
        return this.http.get<Client[]>(`${this.baseUrl}`);
    }

    getClientById(id: number) {
        return this.http.get<Client>(`${this.baseUrl}/${id}`);
    }

    createClient(client: Client): Observable<Object> {
        return this.http.post(`${this.baseUrl}`, client);
    }

    updateClient(id: number, client: Client) {
        return this.http.put(`${this.baseUrl}/${id}`, client);
    }

    deleteClient(id: number) {
        return this.http.delete(`${this.baseUrl}/${id}`);
    }
}
