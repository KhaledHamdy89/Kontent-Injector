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
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;

/**
 * Input Validator unit tests
 * Created by khaled.hamdy on 3/13/17.
 */
public class InputValidatorTest {

    @Test
    public void Validate_ValidNotNullObject_NoException() {
        boolean exceptionCaught = false;
        try {
            InputValidator.validate(new Object());
        } catch (InvalidInputException ex) {
            exceptionCaught = true;
        }
        assertFalse(exceptionCaught);
    }

    @Test
    public void Validate_ValidNotEmptyString_NoException() {
        boolean exceptionCaught = false;
        try {
            InputValidator.validate("Test");
        } catch (InvalidInputException ex) {
            exceptionCaught = true;
        }
        assertFalse(exceptionCaught);
    }

    @Test
    public void Validate_ValidNotEmptyList_NoException() {
        boolean exceptionCaught = false;
        try {
            List<Integer> testList = new ArrayList<>();
            testList.add(1);
            InputValidator.validate(testList);
        } catch (InvalidInputException ex) {
            exceptionCaught = true;
        }
        assertFalse(exceptionCaught);
    }

    @Test(expected = InvalidInputException.class)
    public void Validate_ValidNullObject_InvalidInputException() throws InvalidInputException {
        InputValidator.validate(null);
    }

    @Test(expected = InvalidInputException.class)
    public void Validate_ValidEmptyString_InvalidInputException() throws InvalidInputException {
        InputValidator.validate("");
    }

    @Test(expected = InvalidInputException.class)
    public void Validate_ValidEmptyList_InvalidInputException() throws InvalidInputException {
        InputValidator.validate(new ArrayList<String>());
    }

}