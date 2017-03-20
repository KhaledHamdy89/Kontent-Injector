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

import KI.Models.KIClassConfiguration;

import java.lang.reflect.Method;

/**
 * The Injection Cache is meant to link a content object with it's corresponding
 * KIClassConfiguration object to ease access to it, and enhance performance
 */
public class InjectionEngineCache {

    private final KIClassConfiguration classConfig;
    private final Object contentObject;

    /**
     * Construct an injection cache object
     *
     * @param contentObject The object holding the injection content
     * @param classConfig   The class configuration corresponding to the contentObject's class
     */
    public InjectionEngineCache(Object contentObject, KIClassConfiguration classConfig) {
        this.classConfig = classConfig;
        this.contentObject = contentObject;
    }

    /**
     * Get the value by invoking the method corresponding to the name passed in the parameters
     * on the specific content object.
     * (A Parameter-less method that is not void is expected)
     *
     * @param methodName The method name to search for in the contentObject's class
     * @return An Object returned from the method
     * @throws ReflectiveOperationException An Exception is thrown if the method's invocation failed
     */
    public Object fetchInjection(String methodName) throws ReflectiveOperationException {
        methodName = classConfig.getMethodName(methodName);
        Method targetMethod = classConfig.getTargetClass().getMethod(methodName);
        return targetMethod.invoke(contentObject);
    }
}
