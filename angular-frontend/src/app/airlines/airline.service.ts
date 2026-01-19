import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http'
import { Observable } from 'rxjs';

import { AirlineResponse } from './models/airline-response';
import { CreateAirlineRequest } from './models/create-airline-request';
import { UpdateAirlineRequest } from './models/update-airline-request';
import { FlightResponse } from '../flights/models/flight-response';


@Injectable({
    providedIn: 'root'
})
export class AirlineService {
    
    private baseUrl = 'http://localhost:8080/api/v1/airlines'

    constructor(private http: HttpClient) {}

    getAirlines() {
        return this.http.get<AirlineResponse[]>(`${this.baseUrl}`);
    }

    getAirlineById(id: number) {
        return this.http.get<AirlineResponse>(`${this.baseUrl}/${id}`);
    }

    getAirlineFlights(id: number) {
        return this.http.get<FlightResponse[]>(`${this.baseUrl}/${id}/flights`);
    }

    createAirline(request: CreateAirlineRequest): Observable<Object> {
        return this.http.post(`${this.baseUrl}`, request);
    }

    updateAirline(id: number, request: UpdateAirlineRequest) {
        return this.http.put(`${this.baseUrl}/${id}/name`, request);
    }

    activateAirline(id: number) {
        return this.http.put(`${this.baseUrl}/${id}/activate`, {});
    }

    suspendAirline(id: number) {
        return this.http.put(`${this.baseUrl}/${id}/suspend`, {});
    }
}