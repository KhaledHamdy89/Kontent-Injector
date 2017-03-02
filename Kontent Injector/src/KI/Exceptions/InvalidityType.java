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

package KI.Exceptions;

/**
 * An enum containing all possible invalidity types with their messages
 * <p>
 * Created by khaled.hamdy on 2/28/17.
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
