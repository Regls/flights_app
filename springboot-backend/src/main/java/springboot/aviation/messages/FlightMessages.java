package springboot.aviation.messages;

import static springboot.aviation.messages.CommonMessages.*;


public final class FlightMessages {
    
    public static final String FLIGHT_NUMBER_REQUIRED = "Flight number" + REQUIRED;
    public static final String AIRLINE_REQUIRED = "Airline" + REQUIRED;
    public static final String DEPARTURE_AIRPORT_REQUIRED = "Departure airport" + REQUIRED;
    public static final String ARRIVAL_AIRPORT_REQUIRED = "Arrival airport" + REQUIRED;
    public static final String FLIGHT_NUMBER_INVALID_FORMAT = "Flight number must follow airline IATA code + 1 to 4 digits";
    public static final String FLIGHT_NUMBER_MUST_MATCH_AIRLINE = "Flight number must start with airline IATA code";
    public static final String AIRLINE_ACTIVE = "Cannot schedule a flight for an suspended airline";
    public static final String TIME_DEPARTURE_BEFORE = "Departure time must be before arrival time";
    public static final String TIME_NOT_EQUALS = "Departure and arrival times cannot be the same";
    public static final String AIRPORT_NOT_EQUALS = "Departure and arrival airports cannot be the same";
    public static final String AIRPORT_ACTIVE = "Cannot use closed airport";
    public static final String DEPART_ONLY_SCHEDULED = "Only scheduled flights can depart";
    public static final String ARRIVE_ONLY_IN_FLIGHT = "Only in-flight flights can arrive";
    public static final String CANCEL_ONLY_SCHEDULED = "Only scheduled flights can be cancelled";

    private FlightMessages(){}
}
