package KI.Exceptions;

/**
 * An enum containing all possible invalidity types with their messages
 * <p>
 * Created by khaled.hamdy on 2/28/17.
 * Copyright (c) 2017 Khaled Hamdy
 */
public enum InvalidityType {
    EMPTY_STRING("The alias cannot be an empty string"),
    NULL("The alias/Class cannot be null"),
    INJECTION_KEYWORDS_CONFLICT("The alias cannot be a configuration [Injection token, Loop start word, Loop end word] used in the KI config object"),
    METHOD_DOES_NOT_EXIST("No method was found with the provided name in the target class"),
    METHOD_SHOULD_NOT_HAVE_PARAMETERS("The method provided is expecting parameters, please use a parameter-less method for injection"),
    DUPLICATE_ALIAS("There are duplicate aliases among classes and methods aliases"),
    DUPLICATE_INJECTION_KEYWORD("Duplicate injection keywords, please use a different string");

    private String errorMessage;

    InvalidityType(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return errorMessage;
    }
}
