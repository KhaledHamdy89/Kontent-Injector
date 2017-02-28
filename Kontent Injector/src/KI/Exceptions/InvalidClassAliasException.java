package KI.Exceptions;

import java.text.MessageFormat;

/**
 * Invalid class alias exception is thrown if an invalid alias is sent.
 * Invalid alias such as an empty string, null, injection token, loop start/end word
 * Created by khaled.hamdy on 2/20/17.
 */
public class InvalidClassAliasException extends InvalidInputException {

    public InvalidClassAliasException(String className, String alias, InvalidityType invalidityType) {
        super(MessageFormat.format("Invalid class alias ({0}) for class {1}.{2}.", alias, className, invalidityType.toString()));
    }
}
