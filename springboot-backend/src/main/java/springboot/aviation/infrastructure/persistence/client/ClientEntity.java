package springboot.aviation.infrastructure.persistence.client;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import springboot.aviation.domain.client.ClientStatus;
import jakarta.persistence.GenerationType;


@Entity
@Table(name = "clients", uniqueConstraints = @UniqueConstraint(columnNames = "cpf"))
public class ClientEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 11, unique = true)
    private String cpf;

    @Column(name = "client_first_name")
    private String firstName;

    @Column(name = "client_last_name")
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClientStatus status;

    public ClientEntity() {}

    public Long getId() { return id; }
    public String getCpf() { return cpf; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public ClientStatus getStatus() { return status; }

    public void setId(Long id) { this.id = id; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setStatus(ClientStatus status) { this.status = status; }
}
