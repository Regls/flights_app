import { AirlineSummaryResponse } from './airline-summary-response';
import { AirportSummaryResponse } from './airport-summary-response'
import { FlightStatus } from './flight-status.enum';

export interface FlightResponse {
    id: number;
    flightNumber: string;
    airline: AirlineSummaryResponse;
    departureAirport: AirportSummaryResponse;
    arrivalAirport: AirportSummaryResponse;
    departureTime: string;
    arrivalTime: string;
    status: FlightStatus;
}
