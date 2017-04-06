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

public class MockContentObject2 {

    public static final String EXPECTED_STRING_FROM_STRING = "STRING INJECTION V2";
    public static final String EXPECTED_STRING_FROM_OBJECT = "OBJECT INJECTION V2";

    /**
     * This method should always return the .toString() method of the MockContentObject
     * class
     *
     * @return the value in the constant EXPECTED_STRING_FROM_OBJECT = "OBJECT INJECTION V2"
     */
    public Object methodReturnsMockContentObject() {
        return new MockContentObject2();
    }

    /**
     * This method should always return the value of the constant EXPECTED_STRING_FROM_STRING
     *
     * @return EXPECTED_STRING_FROM_STRING = "STRING INJECTION V2"
     */
    public String methodReturnsString() {
        return EXPECTED_STRING_FROM_STRING;
    }

    /**
     * This method should always return a list containing strings "1 V2", "2 V2", "3 V2", "Cool V2"
     *
     * @return A list containing strings "1 V2", "2 V2", "3 V2", "Cool V2"
     */
    public List<String> methodReturnStringList() {
        List<String> resultList = new ArrayList<>();
        resultList.add("1 V2");
        resultList.add("2 V2");
        resultList.add("3 V2");
        resultList.add("Cool V2");
        return resultList;
    }

    @Override
    public String toString() {
        return EXPECTED_STRING_FROM_OBJECT;
    }
}
