export interface CreateFlightRequest {
    flightNumber: string;
    airlineId: number;
    departureAirportId: number;
    arrivalAirportId: number;
    departureTime: string;
    arrivalTime: string;
}