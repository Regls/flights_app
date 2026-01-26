package springboot.aviation.domain.airport;

import springboot.aviation.exception.BusinessException;
import springboot.aviation.messages.AirportMessages;


public class Airport {
    
    private final Long id;
    private final String iataCode;
    private String airportName;
    private String city;
    private AirportStatus status;

    private static final String ONLY_LETTERS = "^[a-zA-Z ]+$";

    private static final String ONLY_LETTERS_AND_SINGLE_SPACES = "^[A-Za-zÀ-ÖØ-öø-ÿ]+( [A-Za-zÀ-ÖØ-öø-ÿ]+)*$";

    public Airport(Long id, String iataCode, String airportName, String city, AirportStatus status) {
        this.id = id;
        this.iataCode = iataCode;
        this.airportName = airportName;
        this.city = city;
        this.status = status;
    }

    public static Airport create(String iataCode, String airportName, String city) {
        validateCreationRules(iataCode, airportName, city);
        return new Airport(null, iataCode.toUpperCase(), airportName, city, AirportStatus.OPEN);
    }

    public static Airport restore(Long id, String iataCode, String airportName, String city, AirportStatus status) {
        return new Airport(id, iataCode.toUpperCase(), airportName, city, status);
    }

    public static void validateCreationRules(String iataCode, String airportName, String city){
        if (iataCode == null || iataCode.isBlank()) {
            throw new BusinessException(AirportMessages.IATA_CODE_REQUIRED);
        }
        if (iataCode.length() != 3) {
            throw new BusinessException(AirportMessages.IATA_CODE_3_DIGITS);
        }
        if (!iataCode.matches(ONLY_LETTERS)) {
            throw new BusinessException(AirportMessages.IATA_CODE_ONLY_LETTERS);
        }
        if (airportName == null || airportName.isBlank()) {
            throw new BusinessException(AirportMessages.NAME_REQUIRED);
        }
        if (!airportName.matches(ONLY_LETTERS_AND_SINGLE_SPACES)) {
            throw new BusinessException(AirportMessages.NAME_ONLY_LETTERS_AND_SPACES);
        }
        if (city == null || city.isBlank()) {
            throw new BusinessException(AirportMessages.CITY_REQUIRED);
        }
        if (!city.matches(ONLY_LETTERS_AND_SINGLE_SPACES)) {
            throw new BusinessException(AirportMessages.CITY_ONLY_LETTERS_AND_SPACES);
        }
    }

    public void changeName(String airportName) {
        if (airportName == null || airportName.isBlank()) {
            throw new BusinessException(AirportMessages.NAME_REQUIRED);
        }
        if (!airportName.matches(ONLY_LETTERS_AND_SINGLE_SPACES)) {
            throw new BusinessException(AirportMessages.NAME_ONLY_LETTERS_AND_SPACES);
        }

        this.airportName = airportName;
    }

    public void open() {
        if (this.status == AirportStatus.OPEN){
            throw new BusinessException(AirportMessages.AIRPORT_ALREADY_OPEN);
        }
        this.status = AirportStatus.OPEN;
    }

    public void close() {
        if (this.status == AirportStatus.CLOSED){
            throw new BusinessException(AirportMessages.AIRPORT_ALREADY_CLOSED);
        }
        this.status = AirportStatus.CLOSED;
    }

    public Long getId() { return id; }
    public String getIataCode() { return iataCode; }
    public String getAirportName() { return airportName; }
    public String getCity() { return city; }
    public AirportStatus getStatus() { return status; }
    public boolean isOpen() {return this.status == AirportStatus.OPEN;}
}
