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
    CONFLICTING_ALIAS("The alias cannot be a configuration [Injection token, Loop start word, Loop end word] used in the KI config object"),
    METHOD_DOESNT_EXIST("No method was found with the provided name in the target class"),
    METHOD_SHOULD_NOT_HAVE_PARAMETERS("The method provided is expecting parameters, please use a parameter-less method for injection");

    private String errorMessage;

    InvalidityType(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return errorMessage;
    }
}
