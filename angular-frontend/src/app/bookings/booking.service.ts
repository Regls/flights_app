import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http'
import { Observable } from 'rxjs';

import { BookingResponse } from './models/booking-response';
import { CreateBookingRequest } from './models/create-booking-request';


@Injectable({
    providedIn: 'root'
})
export class BookingService {
    
    private baseUrl = 'http://localhost:8080/api/v1/bookings'

    constructor(private http: HttpClient) {}

    getBookings() {
        return this.http.get<BookingResponse[]>(`${this.baseUrl}`);
    }

    getBookingById(id: number) {
        return this.http.get<BookingResponse>(`${this.baseUrl}/${id}`);
    }

    createBooking(request: CreateBookingRequest): Observable<Object> {
        return this.http.post(`${this.baseUrl}`, request);
    }

    confirmBooking(id: number) {
        return this.http.put(`${this.baseUrl}/${id}/confirm`, {});
    }

    cancelBooking(id: number) {
        return this.http.put(`${this.baseUrl}/${id}/cancel`, {});
    }
}
