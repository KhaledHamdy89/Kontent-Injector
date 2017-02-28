package KI.Exceptions;

/**
 * Indicates an incorrect input
 * <p>
 * Created by khaled.hamdy on 2/28/17.
 * Copyright (c) 2017 Khaled Hamdy
 */
public class InvalidInputException extends Exception {

    public InvalidInputException(InvalidityType invalidityType) {
        this(invalidityType.toString());
    }

    public InvalidInputException(String errorMessage) {
        super(errorMessage);
    }

}
