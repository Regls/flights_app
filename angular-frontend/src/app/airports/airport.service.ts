import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http'
import { Observable } from 'rxjs';

import { AirportResponse } from './models/airport-response';
import { CreateAirportRequest } from './models/create-airport-request';
import { UpdateAirportRequest } from './models/update-airport-request';


@Injectable({
    providedIn: 'root'
})
export class AirportService {
    
    private baseUrl = 'http://localhost:8080/api/v1/airports'

    constructor(private http: HttpClient) {}

    getAirports() {
        return this.http.get<AirportResponse[]>(`${this.baseUrl}`);
    }

    getAirportById(id: number) {
        return this.http.get<AirportResponse>(`${this.baseUrl}/${id}`);
    }

    createAirport(request: CreateAirportRequest): Observable<Object> {
        return this.http.post(`${this.baseUrl}`, request);
    }

    updateAirport(id: number, request: UpdateAirportRequest) {
        return this.http.put(`${this.baseUrl}/${id}/name`, request);
    }

    openAirport(id:number) {
        return this.http.put(`${this.baseUrl}/${id}/open`, {});
    }

    closeAirport(id:number) {
        return this.http.put(`${this.baseUrl}/${id}/close`, {});
    }
}