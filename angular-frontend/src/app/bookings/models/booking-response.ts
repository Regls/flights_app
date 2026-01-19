import { ClientSummaryResponse } from './client-summary-response';
import { FlightSummaryResponse } from './flight-summary-response'
import { BookingStatus } from './booking-status.enum';

export interface BookingResponse {
    id: number;
    client: ClientSummaryResponse;
    flight: FlightSummaryResponse;
    createdAt: string;
    status: BookingStatus;
}
