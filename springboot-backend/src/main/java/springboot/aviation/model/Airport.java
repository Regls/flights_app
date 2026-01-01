package springboot.aviation.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import springboot.aviation.exception.BusinessException;


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
            throw new BusinessException("IATA code is required");
        }
        if (iataCode.length() != 3) {
            throw new BusinessException("IATA code must have 3 digits");
        }
        if (!iataCode.matches(ONLY_LETTERS)) {
            throw new BusinessException("IATA code must contain only letters");
        }
        if (airportName == null || airportName.isBlank()) {
            throw new BusinessException("Airport name is required");
        }
        if (!airportName.matches(ONLY_LETTERS_AND_SINGLE_SPACES)) {
            throw new BusinessException("Airport name must contain only letters an spaces");
        }
        if (city == null || city.isBlank()) {
            throw new BusinessException("City is required");
        }
        if (!city.matches(ONLY_LETTERS_AND_SINGLE_SPACES)) {
            throw new BusinessException("City must contain only letters and spaces");
        }
    }

    public void changeName(String airportName) {
        if (airportName == null || airportName.isBlank()) {
            throw new BusinessException("Airport name is required");
        }
        if (!airportName.matches(ONLY_LETTERS_AND_SINGLE_SPACES)) {
            throw new BusinessException("Airport name must contain only letters");
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

    public void openAirport() {
        if (this.operational){
            throw new BusinessException("Airport is already open");
        }
        this.operational = true;
    }

    public void closeAirport() {
        if (!this.operational){
            throw new BusinessException("Airport is already closed");
        }
        this.operational = false;
    }

    public boolean isOperational() {
        return operational;
    }
}
