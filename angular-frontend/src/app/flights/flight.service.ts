import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http'
import { Observable } from 'rxjs';
import { FlightResponse } from './models/flight-reponse';
import { CreateFlightRequest } from './models/create-flight-request';

@Injectable({
    providedIn: 'root'
})
export class FlightService {
    
    private baseUrl = 'http://localhost:8080/api/v1/flights'

    constructor(private http: HttpClient) {}

    getFlights() {
        return this.http.get<FlightResponse[]>(`${this.baseUrl}`);
    }

    getFlightById(id: number) {
        return this.http.get<FlightResponse>(`${this.baseUrl}/${id}`);
    }

    createFlight(request: CreateFlightRequest): Observable<Object> {
        return this.http.post(`${this.baseUrl}`, request);
    }

    departFlight(id: number) {
        return this.http.put(`${this.baseUrl}/${id}/depart`, {});
    }

    arriveFlight(id: number) {
        return this.http.put(`${this.baseUrl}/${id}/arrive`, {});
    }

    cancelFlight(id: number) {
        return this.http.put(`${this.baseUrl}/${id}/cancel`, {});
    }
}
