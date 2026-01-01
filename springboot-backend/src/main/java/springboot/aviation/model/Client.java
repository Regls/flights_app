package springboot.aviation.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import springboot.aviation.exception.BusinessException;


@Entity
@Table(name = "clients", uniqueConstraints = @UniqueConstraint(columnNames = "cpf"))
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "cpf", unique = true, length = 11)
    private String cpf;

    @Column(name = "client_first_name")
    private String clientFirstName;

    @Column(name = "client_last_name")
    private String clientLastName;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    private static final String ONLY_LETTERS = "^[A-Za-zÀ-ÖØ-öø-ÿ]+$";

    protected Client() {
    }

    private Client(String cpf, String clientFirstName, String clientLastName) {

        validateCreationRules(cpf, clientFirstName, clientLastName);

        this.cpf = cpf;
        this.clientFirstName = clientFirstName;
        this.clientLastName = clientLastName;
        this.active = true;
    }

    //domain
    public static Client createClient(String cpf, String clientFirstName, String clientLastName) {
        return new Client(cpf, clientFirstName, clientLastName);
    }

    public void validateCreationRules(String cpf, String clientFirstName, String clientLastName) {
        if (cpf == null || cpf.isBlank()) {
            throw new BusinessException("CPF is required");
        }
        if (cpf.length() != 11) {
            throw new BusinessException("CPF must have 11 digits");
        }
        if (!cpf.matches("\\d+")) {
            throw new BusinessException("CPF must contain only digits");
        }
        if (clientFirstName == null || clientFirstName.isBlank()) {
            throw new BusinessException("Client first name is required");
        }
        if (clientLastName == null || clientLastName.isBlank()) {
            throw new BusinessException("Client last name is required");
        }
        if (!clientFirstName.matches(ONLY_LETTERS) || !clientLastName.matches(ONLY_LETTERS)) {
            throw new BusinessException("Client names must contain only letters");
        }
    }

    public void changeName(String clientFirstName, String clientLastName) {
        if (clientFirstName == null || clientFirstName.isBlank()) {
            throw new BusinessException("Client first name is required");
        }
        if (clientLastName == null || clientLastName.isBlank()) {
            throw new BusinessException("Client last name is required");
        }
        if (!clientFirstName.matches(ONLY_LETTERS) || !clientLastName.matches(ONLY_LETTERS)) {
            throw new BusinessException("Client names must contain only letters");
        }

        this.clientFirstName = clientFirstName;
        this.clientLastName = clientLastName;
    }

    public boolean hasCpf(String cpf){
        return this.cpf.equals(cpf);
    }

    public boolean hasFirstName(String clientFirstName){
        return this.clientFirstName.equals(clientFirstName);
    }
    
    public boolean hasLastName(String clientLastName){
        return this.clientLastName.equals(clientLastName);
    }

    public void activate() {
        if (this.active) {
            throw new BusinessException("Client is already active");
        }
        this.active = true;
    }

    public void deactivate() {
        if (!this.active) {
            throw new BusinessException("Client is already inactive");
        }
        this.active = false;
    }

    public boolean isActive() {
        return active;
    }
}
