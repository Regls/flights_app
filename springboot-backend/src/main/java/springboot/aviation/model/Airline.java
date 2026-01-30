package springboot.aviation.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import springboot.aviation.exception.BusinessException;
import springboot.aviation.messages.AirlineMessages;


@Entity
@Table(name = "airlines", uniqueConstraints = @UniqueConstraint(columnNames = "iata_code"))
public class Airline {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "iata_code", unique = true, length = 2)
    private String iataCode;

    @NotBlank
    @Column(name = "airline_name")
    private String airlineName;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AirlineStatus status = AirlineStatus.ACTIVE;

    private static final String ONLY_LETTERS_AND_SINGLE_SPACES = "^[A-Za-zÀ-ÖØ-öø-ÿ]+( [A-Za-zÀ-ÖØ-öø-ÿ]+)*$";

    private static final String IATA_CODE_PATTERN = "^[A-Z0-9]{2}$";

    protected Airline() {
    }

    private Airline(String iataCode, String airlineName) {

        validateCreationRules(iataCode, airlineName);

        this.iataCode = iataCode.trim().toUpperCase();
        this.airlineName = airlineName;
        this.status = AirlineStatus.ACTIVE;
    }

    //domain
    public static Airline createAirline( String iataCode, String airlineName) {
        return new Airline(iataCode, airlineName);
    }

    public void validateCreationRules(String iataCode, String airlineName) {
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

    public Long hasId() {
        return this.id;
    }

    public String hasIataCode() {
        return this.iataCode;
    }

    public String hasName() {
        return this.airlineName;
    }

    public boolean hasIataCode(String iataCode) {
        return this.iataCode.equals(iataCode);
    }

    public boolean hasName(String airlineName) {
        return this.airlineName.equals(airlineName);
    }

    public boolean matchesFlightNumber(String flightNumber) {
        if (flightNumber == null || flightNumber.length() < 2) {
            return false;
        }
        return flightNumber.startsWith(this.iataCode);
    }

    public void activate() {
        if (isActive()) {
            throw new BusinessException(AirlineMessages.AIRLINE_ALREADY_ACTIVE);
        }
        this.status = AirlineStatus.ACTIVE;
    }

    public void suspend() {
        if (!isActive()) {
            throw new BusinessException(AirlineMessages.AIRLINE_ALREADY_SUSPENDED);
        }
        this.status = AirlineStatus.SUSPENDED;
    }

    public boolean isActive() {
        return this.status == AirlineStatus.ACTIVE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Airline airline = (Airline) o;

        return iataCode.equals(airline.iataCode);
    }

    @Override
    public int hashCode() {
        return iataCode.hashCode();
    }

}
