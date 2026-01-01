package springboot.aviation.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import springboot.aviation.exception.BusinessException;


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

    protected Airline() {
    }

    private Airline(String iataCode, String airlineName) {

        validateCreationRules(iataCode, airlineName);

        this.iataCode = iataCode;
        this.airlineName = airlineName;
        this.status = AirlineStatus.ACTIVE;
    }

    //domain
    public static Airline createAirline( String iataCode, String airlineName) {
        return new Airline(iataCode, airlineName);
    }

    public void validateCreationRules(String iataCode, String airlineName) {
        if (iataCode == null || iataCode.isBlank()) {
            throw new BusinessException("IATA code is required");
        }
        if (iataCode.length() != 2){
            throw new BusinessException("IATA code must have two digits");
        }
        if (airlineName == null || airlineName.isBlank()) {
            throw new BusinessException("Airline name is required");
        }
        if (!airlineName.matches(ONLY_LETTERS_AND_SINGLE_SPACES)) {
            throw new BusinessException("Airline name must contain only letters and spaces");
        }
    }

    public void changeName(String airlineName) {
        if (airlineName == null || airlineName.isBlank()) {
            throw new BusinessException("Airline name is required");
        }
        if (!airlineName.matches(ONLY_LETTERS_AND_SINGLE_SPACES)) {
            throw new BusinessException("Airline name must contain only letters and spaces");
        }

        this.airlineName = airlineName;
    }

    public boolean hasIataCode(String iataCode){
        return this.iataCode.equals(iataCode);
    }

    public boolean hasName(String airlineName){
        return this.airlineName.equals(airlineName);
    }

    public void activate() {
        if (isActive()) {
            throw new BusinessException("Airline is already active");
        }
        this.status = AirlineStatus.ACTIVE;
    }

    public void suspend() {
        if (!isActive()) {
            throw new BusinessException("Airline is already suspended");
        }
        this.status = AirlineStatus.SUSPENDED;
    }

    public boolean isActive() {
        return this.status == AirlineStatus.ACTIVE;
    }
}
