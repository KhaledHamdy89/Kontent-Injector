package KI.Models;

import KI.Exceptions.InvalidInputException;
import KI.Exceptions.InvalidityType;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * KI Configuration model is the model that holds the data needed to parse a template
 * by defining the injection token, loop start word and loop end word.
 * <p>
 * The Injection token is usually a set of symbols (not necessarily) that is placed
 * before and after injection templates also before and after loop start and end words
 * <p>
 * The loop start and end words are the words that indicate the start and end of a loop.
 * The words are expected to appear in a template enclosed by the injection token
 * (Although the words are saved in the model without the tokens)
 * <p>
 * Defaults:
 * Injection Token: $%$
 * Loop Start Word: LOOP
 * Loop End Word: ENDLOOP
 * <p>
 * Created by Khaled.Hamdy on 2/14/17.
 */
public class KITemplateConfiguration {
    private String injectionToken = "$%$";
    private String loopStartWord = "LOOP";
    private String loopEndWord = "ENDLOOP";
    private Map<Class<?>, KIClassConfiguration> classesAliases = new HashMap<>();

    /**
     * Configuration object holding the Kontent Injector's template configurations.
     * Defaults:
     * Injection Token: $%$
     * Loop Start Word: LOOP
     * Loop End Word: ENDLOOP
     */
    public KITemplateConfiguration() {
    }

    /**
     * Gets the injection token used in a template
     *
     * @return A string holding the injection template (Default: $%$)
     */
    public String getInjectionToken() {
        return injectionToken;
    }

    /**
     * Sets the injection token used in a template
     *
     * @param injectionToken The string to be used as an injection token
     */
    public void setInjectionToken(String injectionToken) {
        this.injectionToken = injectionToken;
    }

    /**
     * Get the loop start word used in a template to indicate the start of a loop
     *
     * @return A string holding the loop start word (Default: LOOP)
     */
    public String getLoopStartWord() {
        return loopStartWord;
    }

    /**
     * Set the loop start word to be used in a template
     *
     * @param loopStartWord The string to be used as a loop start word
     */
    public void setLoopStartWord(String loopStartWord) {
        this.loopStartWord = loopStartWord;
    }

    /**
     * Get the loop end word used in a template to indicate the end of a loop
     *
     * @return A string holding the loop end word (Default: ENDLOOP)
     */
    public String getLoopEndWord() {
        return loopEndWord;
    }

    /**
     * Set the loop end word to be used in a template
     *
     * @param loopEndWord The string to be used as a loop end word
     */
    public void setLoopEndWord(String loopEndWord) {
        this.loopEndWord = loopEndWord;
    }

    /**
     * Add an alias to be used in templates instead of a class' name
     *
     * @param targetClass The target class to set the alias for
     * @param alias       The alias used in templates referring to the class
     * @throws InvalidInputException An invalid input exception is thrown if any of the input parameters is null or empty
     */
    public void addClassAlias(Class<?> targetClass, String alias) throws InvalidInputException {
        validateInput(targetClass);
        validateInput(alias);
        KIClassConfiguration classConfig = classesAliases.getOrDefault(targetClass, new KIClassConfiguration(targetClass));
        classConfig.setClassAlias(alias);
        classesAliases.put(targetClass, classConfig);
    }

    /**
     * Add a method's alias to be used in templates instead of the method's actual name
     *
     * @param targetClass The class containing the method
     * @param methodName  The method's name
     * @param methodAlias The method's alias
     * @throws InvalidInputException An invalid input exception is thrown if any of the input parameters is null or empty,
     *                               or if the class doesn't contain the a method with the name sent in the parameters
     */
    public void addMethodAlias(Class<?> targetClass, String methodName, String methodAlias) throws InvalidInputException {
        validateInput(targetClass);
        validateInput(methodName);
        validateInput(methodAlias);
        validateClassMethod(targetClass, methodName);

        KIClassConfiguration classConfig = classesAliases.getOrDefault(targetClass, new KIClassConfiguration(targetClass));
        classConfig.addMethodAlias(methodName, methodAlias);
        classesAliases.put(targetClass, classConfig);
    }

    /**
     * Remove a class' alias so the template would contain the class' name
     *
     * @param targetClass The target class to remove the alias for
     */
    public void removeClassAlias(Class<?> targetClass) {
        KIClassConfiguration classConfig = classesAliases.get(targetClass);
        if (classConfig == null)
            return;
        classConfig.setClassAlias(null);
    }

    /**
     * Clears all classes aliases
     */
    public void clearClassesAliases() {
        classesAliases.clear();
    }

    void validateConfigurations() throws InvalidInputException {
        for (KIClassConfiguration config : classesAliases.values()) {
            validateAgainstInjectionKeywords(config.getTargetClassAlias());
            validateAgainstInjectionKeywords(config.getTargetClassAlias());
        }
    }

    private void validateAgainstInjectionKeywords(String test) throws InvalidInputException {
        if(test.equals(injectionToken) || test.equals(loopStartWord) || test.equals(loopEndWord))
            throw new InvalidInputException(InvalidityType.CONFLICTING_ALIAS);
    }

    /**
     * Check if a method with the sent name exists in the target class, and if it is parameter-less
     *
     * @param targetClass The target class to check for the method in
     * @param methodName  The method name
     * @throws InvalidInputException An exception containing the invalidity type
     */
    private void validateClassMethod(Class<?> targetClass, String methodName) throws InvalidInputException {
        Method targetMethod = null;
        for (Method method : targetClass.getMethods()) {
            if (method.getName() != methodName)
                continue;
            targetMethod = method;
            break;
        }
        if (targetMethod == null)
            throw new InvalidInputException(InvalidityType.METHOD_DOESNT_EXIST);

        if (targetMethod.getParameters().length > 0)
            throw new InvalidInputException(InvalidityType.METHOD_SHOULD_NOT_HAVE_PARAMETERS);

    }

    /**
     * Validates an object is not null, or if the object is a string, that it's not empty
     *
     * @param inputObject The target object to be validated
     * @throws InvalidInputException An exception containing the invalidity type
     */
    private void validateInput(Object inputObject) throws InvalidInputException {
        if (inputObject == null)
            throw new InvalidInputException(InvalidityType.NULL);
        if (!(inputObject instanceof String))
            return;
        if (((String) inputObject).isEmpty())
            throw new InvalidInputException(InvalidityType.EMPTY_STRING);
    }


}
