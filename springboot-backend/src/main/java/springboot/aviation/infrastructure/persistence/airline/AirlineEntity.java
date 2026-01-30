package springboot.aviation.infrastructure.persistence.airline;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import springboot.aviation.domain.airline.AirlineStatus;
import jakarta.persistence.GenerationType;


@Entity
@Table(name = "airlines", uniqueConstraints = @UniqueConstraint(columnNames = "iata_code"))
public class AirlineEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "iata_code", nullable = false, length = 2, unique = true)
    private String iataCode;
    
    @Column(name = "airline_name")
    private String airlineName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AirlineStatus status;

    public AirlineEntity() {}

    public Long getId() { return id; }
    public String getIataCode() { return iataCode; }
    public String getAirlineName() { return airlineName; }
    public AirlineStatus getStatus() { return status; }

    public void setId(Long id) { this.id = id; }
    public void setIataCode(String iataCode) { this.iataCode = iataCode; }
    public void setAirlineName(String airlineName) { this.airlineName = airlineName; }
    public void setStatus(AirlineStatus status) { this.status = status; }
}
