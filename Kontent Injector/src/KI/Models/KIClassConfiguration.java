package KI.Models;

import java.util.HashMap;
import java.util.Map;

/**
 * KIClassConfiguration is the model that holds a class' alias along with any methods aliases
 * <p>
 * Created by khaled.hamdy on 2/22/17.
 * Copyright (c) 2017 Khaled Hamdy
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
     * Set an alias for the config's class
     *
     * @param alias A string defining the target class' alias that will be used in templates
     */
    public void setClassAlias(String alias) {
        this.classAlias = alias;
    }

    /**
     * Add an alias to a method contained inside the target class
     *
     * @param methodName  The name of the method
     * @param methodAlias The method's alias that will be used in templates
     */
    public void addMethodAlias(String methodName, String methodAlias) {
        methodsAliases.put(methodAlias, methodName);
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
     * Gets a method's actual name from its alias
     *
     * @param methodAlias A method's alias that is used in templates
     * @return The method's real name, or the string sent as the "methodAlias" if nothing is found
     */
    public String getMethodName(String methodAlias) {
        return methodsAliases.getOrDefault(methodAlias, methodAlias);
    }
}
