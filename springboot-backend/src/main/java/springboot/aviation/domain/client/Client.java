package springboot.aviation.domain.client;

import springboot.aviation.exception.BusinessException;
import springboot.aviation.messages.ClientMessages;


public class Client {
    
    private final Long id;
    private final String cpf;
    private String firstName;
    private String lastName;
    private ClientStatus status;

    private static final String ONLY_LETTERS = "^[A-Za-zÀ-ÖØ-öø-ÿ]+$";

    public Client(Long id, String cpf, String firstName, String lastName, ClientStatus status) {
        this.id = id;
        this.cpf = cpf;
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = status;
    }

    public static Client create(String cpf, String firstName, String lastName) {
        validateCreationRules(cpf, firstName, lastName);
        return new Client(null, cpf, firstName, lastName, ClientStatus.ACTIVE);
    }

    public static Client restore(Long id, String cpf, String firstName, String lastName, ClientStatus status){
        return new Client(id, cpf, firstName, lastName, status);
    }

    private static void validateCreationRules(String cpf, String firstName, String lastName) {
        if (cpf == null || cpf.isBlank()) {
            throw new BusinessException(ClientMessages.CPF_REQUIRED);
        }
        if (cpf.length() != 11) {
            throw new BusinessException(ClientMessages.CPF_11_DIGITS);
        }
        if (!cpf.matches("\\d+")) {
            throw new BusinessException(ClientMessages.CPF_ONLY_DIGITS);
        }
        if (firstName == null || firstName.isBlank()) {
            throw new BusinessException(ClientMessages.FIRST_NAME_REQUIRED);
        }
        if (lastName == null || lastName.isBlank()) {
            throw new BusinessException(ClientMessages.LAST_NAME_REQUIRED);
        }
        if (!firstName.matches(ONLY_LETTERS) || !lastName.matches(ONLY_LETTERS)) {
            throw new BusinessException(ClientMessages.NAME_ONLY_LETTERS);
        }
    }

    public void changeName(String firstName, String lastName) {
        if (firstName == null || firstName.isBlank()) {
            throw new BusinessException(ClientMessages.FIRST_NAME_REQUIRED);
        }
        if (lastName == null || lastName.isBlank()) {
            throw new BusinessException(ClientMessages.LAST_NAME_REQUIRED);
        }
        if (!firstName.matches(ONLY_LETTERS) || !lastName.matches(ONLY_LETTERS)) {
            throw new BusinessException(ClientMessages.NAME_ONLY_LETTERS);
        }

        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void activate() {
        if(this.status == ClientStatus.ACTIVE){
            throw new BusinessException(ClientMessages.CLIENT_ALREADY_ACTIVE);
        }
        this.status = ClientStatus.ACTIVE;
    }

    public void deactivate() {
        if(this.status == ClientStatus.INACTIVE){
            throw new BusinessException(ClientMessages.CLIENT_ALREADY_INACTIVE);
        }
        this.status = ClientStatus.INACTIVE;
    }

    public Long getId() { return id; }
    public String getCpf() { return cpf; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public ClientStatus getStatus() { return status; }
    public boolean isActive() {return this.status == ClientStatus.ACTIVE;}
}
