package springboot.aviation.messages;

import static springboot.aviation.messages.common.CommonMessages.*;


public final class AirlineMessages {
    
    public static final String IATA_CODE_REQUIRED = "Airline IATA code" + REQUIRED;
    public static final String IATA_CODE_INVALID = "Airline IATA code must have 2 alphanumeric characters";
    public static final String NAME_REQUIRED = "Airline name" + REQUIRED;
    public static final String NAME_ONLY_LETTERS_AND_SINGLE_SPACES = "Airline name" + ONLY_LETTERS_AND_SINGLE_SPACES;
    public static final String AIRLINE_ALREADY_ACTIVE = "Airline is already active";
    public static final String AIRLINE_ALREADY_SUSPENDED = "Airline is already suspended";

    private AirlineMessages(){}
}
