import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http'
import { Airline } from './airline';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class AirlineService {
    
    private baseUrl = 'http://localhost:8080/airlines'

    constructor(private http: HttpClient) {}

    getAirlines() {
        return this.http.get<Airline[]>(`${this.baseUrl}`);
    }

    getAirlineById(id: number) {
        return this.http.get<Airline>(`${this.baseUrl}/${id}`);
    }

    createAirline(airline: Airline): Observable<Object> {
        return this.http.post(`${this.baseUrl}`, airline);
    }

    updateAirline(id: number, airline: Airline) {
        return this.http.put(`${this.baseUrl}/${id}`, airline);
    }

    deleteAirline(id: number) {
        return this.http.delete(`${this.baseUrl}/${id}`);
    }
}