package springboot.aviation.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import springboot.aviation.exception.BusinessException;
import springboot.aviation.messages.ClientMessages;


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
            throw new BusinessException(ClientMessages.CPF_REQUIRED);
        }
        if (cpf.length() != 11) {
            throw new BusinessException(ClientMessages.CPF_11_DIGITS);
        }
        if (!cpf.matches("\\d+")) {
            throw new BusinessException(ClientMessages.CPF_ONLY_DIGITS);
        }
        if (clientFirstName == null || clientFirstName.isBlank()) {
            throw new BusinessException(ClientMessages.FIRST_NAME_REQUIRED);
        }
        if (clientLastName == null || clientLastName.isBlank()) {
            throw new BusinessException(ClientMessages.LAST_NAME_REQUIRED);
        }
        if (!clientFirstName.matches(ONLY_LETTERS) || !clientLastName.matches(ONLY_LETTERS)) {
            throw new BusinessException(ClientMessages.NAME_ONLY_LETTERS);
        }
    }

    public void changeName(String clientFirstName, String clientLastName) {
        if (clientFirstName == null || clientFirstName.isBlank()) {
            throw new BusinessException(ClientMessages.FIRST_NAME_REQUIRED);
        }
        if (clientLastName == null || clientLastName.isBlank()) {
            throw new BusinessException(ClientMessages.LAST_NAME_REQUIRED);
        }
        if (!clientFirstName.matches(ONLY_LETTERS) || !clientLastName.matches(ONLY_LETTERS)) {
            throw new BusinessException(ClientMessages.NAME_ONLY_LETTERS);
        }

        this.clientFirstName = clientFirstName;
        this.clientLastName = clientLastName;
    }

    public Long hasId(){
        return this.id;
    }

    public String hasCpf(){
        return this.cpf;
    }

    public String hasFirstName(){
        return this.clientFirstName;
    }

    public String hasLastName(){
        return this.clientLastName;
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
            throw new BusinessException(ClientMessages.CLIENT_ALREADY_ACTIVE);
        }
        this.active = true;
    }

    public void deactivate() {
        if (!this.active) {
            throw new BusinessException(ClientMessages.CLIENT_ALREADY_INACTIVE);
        }
        this.active = false;
    }

    public boolean isActive() {
        return active;
    }
}
