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

package Mocks;

import java.util.ArrayList;
import java.util.List;

/**
 * MockContentObject is meant to act as a mock object providing fixed output
 * to be used for content injection unit tests
 * Created by khaled.hamdy on 3/16/17.
 */

public class MockContentObject {

    public static final String EXPECTED_STRING_FROM_STRING = "STRING INJECTION";
    public static final String EXPECTED_STRING_FROM_OBJECT = "OBJECT INJECTION";

    /**
     * This method should always return the .toString() method of the MockContentObject
     * class
     *
     * @return the value in the constant EXPECTED_STRING_FROM_OBJECT = "OBJECT INJECTION"
     */
    public Object methodReturnsMockContentObject() {
        return new MockContentObject();
    }

    /**
     * This method should always return the value of the constant EXPECTED_STRING_FROM_STRING
     *
     * @return EXPECTED_STRING_FROM_STRING = "STRING INJECTION"
     */
    public String methodReturnsString() {
        return EXPECTED_STRING_FROM_STRING;
    }

    /**
     * This method should always return a list containing strings "1", "2", "3", "4", "5", "Cool"
     *
     * @return A list containing strings "1", "2", "3", "4", "5", "Cool"
     */
    public List<String> methodReturnStringList() {
        List<String> resultList = new ArrayList<>();
        resultList.add("1");
        resultList.add("2");
        resultList.add("3");
        resultList.add("4");
        resultList.add("5");
        resultList.add("Cool");
        return resultList;
    }

    @Override
    public String toString() {
        return EXPECTED_STRING_FROM_OBJECT;
    }
}
