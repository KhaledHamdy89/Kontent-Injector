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

package KI.Models;

import java.util.HashMap;
import java.util.Map;

/**
 * KIClassConfiguration is the model that holds a class' alias along with any methods aliases
 * <p>
 * Created by khaled.hamdy on 2/22/17.
 */
public class KIClassConfiguration {
    private Class<?> targetClass;
    private String classAlias;
    private Map<String, String> methodsAliases = new HashMap<>();

    /**
     * Initialize the KI class config object that hold's a class' alias and method aliases (if any)
     *
     * @param targetClass The target class
     */
    public KIClassConfiguration(Class<?> targetClass) {
        this.targetClass = targetClass;
    }

    /**
     * Get the target class
     *
     * @return The target class
     */
    public Class<?> getTargetClass() {
        return this.targetClass;
    }

    /**
     * Get the class' alias
     *
     * @return The target class' alias or the class' name if no alias is set
     */
    public String getTargetClassAlias() {
        return classAlias == null ? targetClass.getName() : classAlias;
    }

    /**
     * Set an alias for the config's class
     *
     * @param alias A string defining the target class' alias that will be used in templates
     */
    void setClassAlias(String alias) {
        this.classAlias = alias;
    }

    /**
     * Remove class alias
     */
    void removeClassAlias() {
        classAlias = null;
    }

    /**
     * Add an alias to a method contained inside the target class
     *
     * @param methodName  The name of the method
     * @param methodAlias The method's alias that will be used in templates
     */
    void addMethodAlias(String methodName, String methodAlias) {
        methodsAliases.put(methodAlias, methodName);
    }

    /**
     * Gets a method's actual name from its alias
     *
     * @param methodAlias A method's alias that is used in templates
     * @return The method's real name, or the string sent as the "methodAlias" if nothing is found
     */
    public String getMethodName(String methodAlias) {
        return methodsAliases.getOrDefault(methodAlias, methodAlias);
    }

    /**
     * Remove method Alias
     *
     * @param methodName method's name
     */
    void removeMethodAlias(String methodName) {
        String targetMethodAlias = "";
        for (Map.Entry<String, String> methodMapping : methodsAliases.entrySet()) {
            if(!methodMapping.getValue().equals(methodName))
                continue;
            methodsAliases.get(methodMapping.getKey());
            return;
        }
    }

    /**
     * Get all methods aliases
     *
     * @return A map holding the methods aliases (keys) and the actual method names (values)
     */
    Map<String, String> getMethodsAliases() {
        return methodsAliases;
    }

}
