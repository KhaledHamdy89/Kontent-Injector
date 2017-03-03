/*
 * MIT License
 *
 * Copyright (c) 2017. Khaled Hamdy
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package KI.Core;

import KI.Exceptions.InvalidInputException;
import KI.Exceptions.InvalidityType;

import java.util.Collection;

/**
 * The input validator is responsible for validating input parameters sent by the KI user
 * Created by khaled.hamdy on 3/2/17.
 */
public class InputValidator {

    /**
     * Validates an object is not null, or if the object is a string, that it's not empty
     *
     * @param inputObject The target object to be validated
     * @throws InvalidInputException An exception containing the invalidity type
     */
    public static void validate(Object inputObject) throws InvalidInputException {
        if (inputObject == null)
            throw new InvalidInputException(InvalidityType.NULL);

        if ((inputObject instanceof String)) {
            validateString((String) inputObject);
            return;
        }

        if ((inputObject instanceof Collection<?>)) {
            validateCollection((Collection<?>) inputObject);
            return;
        }
    }

    /**
     * Validate collection input
     * @param inputObject Input collection
     * @throws InvalidInputException An exception is thrown if the collection is empty
     */
    private static void validateCollection(Collection<?> inputObject) throws InvalidInputException {
        if(inputObject.size() == 0)
            throw new InvalidInputException(InvalidityType.EMPTY_COLLECTION);
    }

    /**
     * Validate string input
     * @param inputObject Input string
     * @throws InvalidInputException An exception is thrown if the string is empty
     */
    private static void validateString(String inputObject) throws InvalidInputException {
        if (inputObject.isEmpty())
            throw new InvalidInputException(InvalidityType.EMPTY_STRING);
    }
}
