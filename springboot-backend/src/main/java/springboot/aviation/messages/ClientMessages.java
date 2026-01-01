package springboot.aviation.messages;

import static springboot.aviation.messages.CommonMessages.*;


public class ClientMessages {
    
    public static final String CPF_REQUIRED = "CPF" + REQUIRED;
    public static final String CPF_11_DIGITS = "CPF must have 11 digits";
    public static final String CPF_ONLY_DIGITS = "CPF must contain only digits";
    public static final String FIRST_NAME_REQUIRED = "Client first name" + REQUIRED;
    public static final String LAST_NAME_REQUIRED = "Client last name" + REQUIRED;
    public static final String NAME_ONLY_LETTERS = "Client names" + ONLY_LETTERS;
    public static final String CLIENT_ALREADY_ACTIVE = "Client is alredy active";
    public static final String CLIENT_ALREADY_INACTIVE = "Client is alredy inactive";

    private ClientMessages(){}
}
