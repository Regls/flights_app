package springboot.aviation.infrastructure.persistence.airport;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import springboot.aviation.domain.airport.AirportStatus;
import jakarta.persistence.GenerationType;


@Entity
@Table(name = "airports", uniqueConstraints = @UniqueConstraint(columnNames = "iata_code"))
public class AirportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "iata_code", nullable = false, length = 3, unique = true)
    private String iataCode;
    
    @Column(name = "airport_name")
    private String airportName;

    @Column(name = "city")
    private String city;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AirportStatus status;

    public AirportEntity() {}

    public Long getId() { return id; }
    public String getIataCode() { return iataCode; }
    public String getAirportName() { return airportName; }
    public String getCity() { return city; }
    public AirportStatus getStatus() { return status; }

    public void setId(Long id) { this.id = id; }
    public void setIataCode(String iataCode) { this.iataCode = iataCode; }
    public void setAirportName(String airportName) { this.airportName = airportName; }
    public void setCity(String city) { this.city = city; }
    public void setStatus(AirportStatus status) { this.status = status; }
}
