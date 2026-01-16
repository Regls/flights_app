import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http'
import { Airport } from './airport';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class AirportService {
    
    private baseUrl = 'http://localhost:8080/api/v1/airports'

    constructor(private http: HttpClient) {}

    getAirports() {
        return this.http.get<Airport[]>(`${this.baseUrl}`);
    }

    getAirportById(id: number) {
        return this.http.get<Airport>(`${this.baseUrl}/${id}`);
    }

    createAirport(airport: Airport): Observable<Object> {
        return this.http.post(`${this.baseUrl}`, airport);
    }

    updateAirport(id: number, airport: Airport) {
        return this.http.put(`${this.baseUrl}/${id}/name`, airport);
    }

    openAirport(id:number) {
        return this.http.put(`${this.baseUrl}/${id}/open`, {});
    }

    closeAirport(id:number) {
        return this.http.put(`${this.baseUrl}/${id}/close`, {});
    }
}