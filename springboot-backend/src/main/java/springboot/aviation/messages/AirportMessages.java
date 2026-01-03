package springboot.aviation.messages;

import static springboot.aviation.messages.CommonMessages.*;


public final class AirportMessages {
    
    public static final String IATA_CODE_REQUIRED = "Airport IATA code" + REQUIRED;
    public static final String IATA_CODE_3_DIGITS = "Airport IATA code must have 3 digits";
    public static final String IATA_CODE_ONLY_LETTERS = "Airport IATA code" + ONLY_LETTERS;
    public static final String NAME_REQUIRED = "Airport name" + REQUIRED;
    public static final String NAME_ONLY_LETTERS_AND_SPACES = "Airport name" + ONLY_LETTERS_AND_SINGLE_SPACES;
    public static final String CITY_REQUIRED = "City" + REQUIRED;
    public static final String CITY_ONLY_LETTERS_AND_SPACES = "City" + ONLY_LETTERS_AND_SINGLE_SPACES;
    public static final String AIRPORT_ALREADY_OPEN = "Airport is already open";
    public static final String AIRPORT_ALREADY_CLOSED = "Airport is already closed";



    private AirportMessages(){}
}
