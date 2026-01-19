import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http'
import { Observable } from 'rxjs';

import { ClientResponse } from './models/client-reponse';
import { CreateClientRequest } from './models/create-client-request';
import { UpdateClientRequest } from './models/update-client-request';

import { BookingResponse } from '../bookings/models/booking-response';


@Injectable({
    providedIn: 'root'
})
export class ClientService {
    
    private baseUrl = 'http://localhost:8080/api/v1/clients'

    constructor(private http: HttpClient) {}

    getClients() {
        return this.http.get<ClientResponse[]>(`${this.baseUrl}`);
    }

    getClientById(id: number) {
        return this.http.get<ClientResponse>(`${this.baseUrl}/${id}`);
    }

    getClientBookings(id: number) {
        return this.http.get<BookingResponse[]>(`${this.baseUrl}/${id}/bookings`);
    }

    createClient(request: CreateClientRequest): Observable<Object> {
        return this.http.post(`${this.baseUrl}`, request);
    }

    updateClient(id: number, request: UpdateClientRequest) {
        return this.http.put(`${this.baseUrl}/${id}/name`, request);
    }

    activateClient(id: number) {
        return this.http.put(`${this.baseUrl}/${id}/activate`, {});
    }

    deactivateClient(id: number) {
        return this.http.put(`${this.baseUrl}/${id}/deactivate`, {});
    }
}
