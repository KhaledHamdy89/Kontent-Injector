package KI.Exceptions;

/**
 * Created by khaled.hamdy on 2/28/17.
 */
public class InvalidInputException extends Exception {

    public InvalidInputException(InvalidityType invalidityType){
        this(invalidityType.toString());
    }

    public InvalidInputException(String errorMessage){
        super(errorMessage);
    }

}
