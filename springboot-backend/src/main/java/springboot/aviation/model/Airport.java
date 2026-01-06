package springboot.aviation.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import springboot.aviation.exception.BusinessException;
import springboot.aviation.messages.AirportMessages;


@Entity
@Table(name = "airports", uniqueConstraints = @UniqueConstraint(columnNames = "iata_code"))
public class Airport {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "iata_code", unique = true, length = 3)
    private String iataCode;

    @NotBlank
    @Column(name = "airport_name")
    private String airportName;

    @NotBlank
    @Column(name = "city")
    private String city;

    @Column(name = "operational", nullable = false)
    private boolean operational = true;

    private final static String ONLY_LETTERS = "^[a-zA-Z]+$";
    
    private static final String ONLY_LETTERS_AND_SINGLE_SPACES = "^[A-Za-zÀ-ÖØ-öø-ÿ]+( [A-Za-zÀ-ÖØ-öø-ÿ]+)*$";

    protected Airport() {
    }

    private Airport(String iataCode, String airportName, String city) {

        validateCreationRules(iataCode, airportName, city);

        this.iataCode = iataCode;
        this.airportName = airportName;
        this.city = city;
        this.operational = true;
    }

    //domain
    public static Airport createAirport(String iataCode, String airportName, String city) {
        return new Airport(iataCode, airportName, city);
    }

    public void validateCreationRules(String iataCode, String airportName, String city){
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

    public boolean hasIataCode(String iataCode){
        return this.iataCode.equals(iataCode);
    }

    public boolean hasName(String airportName){
        return this.airportName.equals(airportName);
    }

    public boolean isIsCity(String city){
        return this.city.equals(city);
    }

    public void open() {
        if (this.operational){
            throw new BusinessException(AirportMessages.AIRPORT_ALREADY_OPEN);
        }
        this.operational = true;
    }

    public void close() {
        if (!this.operational){
            throw new BusinessException(AirportMessages.AIRPORT_ALREADY_CLOSED);
        }
        this.operational = false;
    }

    public boolean isOperational() {
        return operational;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Airport airport = (Airport) o;

        return iataCode.equals(airport.iataCode);
    }

    @Override
    public int hashCode() {
        return iataCode.hashCode();
    }

}
