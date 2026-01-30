package springboot.aviation.domain.airline;

import springboot.aviation.exception.BusinessException;
import springboot.aviation.messages.AirlineMessages;


public class Airline {
    
    private final Long id;
    private final String iataCode;
    private String airlineName;
    private AirlineStatus status;

    private static final String ONLY_LETTERS_AND_SINGLE_SPACES = "^[A-Za-zÀ-ÖØ-öø-ÿ]+( [A-Za-zÀ-ÖØ-öø-ÿ]+)*$";

    private static final String IATA_CODE_PATTERN = "^[A-Z0-9]{2}$";

    public Airline(Long id, String iataCode, String airlineName, AirlineStatus status) {
        this.id = id;
        this.iataCode = iataCode;
        this.airlineName = airlineName;
        this.status = status;
    }

    public static Airline create(String iataCode, String airlineName) {
        validateCreationRules(iataCode, airlineName);
        return new Airline(null, iataCode.toUpperCase(), airlineName, AirlineStatus.ACTIVE);
    }

    public static Airline restore(Long id, String iataCode, String airlineName, AirlineStatus status) {
        return new Airline(id, iataCode.toUpperCase(), airlineName, status);
    }

    public static void validateCreationRules(String iataCode, String airlineName) {
        if (iataCode == null || iataCode.isBlank()) {
            throw new BusinessException(AirlineMessages.IATA_CODE_REQUIRED);
        }
        if (!iataCode.matches(IATA_CODE_PATTERN)){
            throw new BusinessException(AirlineMessages.IATA_CODE_INVALID);
        }
        if (airlineName == null || airlineName.isBlank()) {
            throw new BusinessException(AirlineMessages.NAME_REQUIRED);
        }
        if (!airlineName.matches(ONLY_LETTERS_AND_SINGLE_SPACES)) {
            throw new BusinessException(AirlineMessages.NAME_ONLY_LETTERS_AND_SINGLE_SPACES);
        }
    }

    public void changeName(String airlineName) {
        if (airlineName == null || airlineName.isBlank()) {
            throw new BusinessException(AirlineMessages.NAME_REQUIRED);
        }
        if (!airlineName.matches(ONLY_LETTERS_AND_SINGLE_SPACES)) {
            throw new BusinessException(AirlineMessages.NAME_ONLY_LETTERS_AND_SINGLE_SPACES);
        }

        this.airlineName = airlineName;
    }

    public void activate() {
        if (this.status == AirlineStatus.ACTIVE) {
            throw new BusinessException(AirlineMessages.AIRLINE_ALREADY_ACTIVE);
        }
        this.status = AirlineStatus.ACTIVE;
    }

    public void suspend() {
        if (this.status == AirlineStatus.SUSPENDED) {
            throw new BusinessException(AirlineMessages.AIRLINE_ALREADY_SUSPENDED);
        }
        this.status = AirlineStatus.SUSPENDED;
    }

    public Long getId() { return id; }
    public String getIataCode() { return iataCode; }
    public String getAirlineName() { return airlineName; }
    public AirlineStatus getStatus() { return status; }
    public boolean isActive() {return this.status == AirlineStatus.ACTIVE;}

    public boolean matchesFlightNumber(String flightNumber) {
        if (flightNumber == null || flightNumber.length() < 2) {
            return false;
        }
        return flightNumber.startsWith(this.iataCode);
    }
}
