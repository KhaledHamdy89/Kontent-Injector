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
import KI.Models.KITemplateConfiguration;

import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;

/**
 * The injection engine is the core that is used to inject content into a template.
 * It does not know anything about templates, reading or writing. Simply getting some
 * string, searching for injection templates, injecting the content and returning back
 * a string
 * Created by khaled.hamdy on 3/9/17.
 */
class KIInjectionEngine {

    private final KITemplateConfiguration templateConfig;
    private final Map<String, InjectionEngineCache> injectionCache;

    /**
     * Construct an object from the KIInjectionEngine to handle core injection processes
     *
     * @param templateConfig The configuration of the template in use
     * @param contentObjects The objects holding the injection content
     */
    KIInjectionEngine(KITemplateConfiguration templateConfig, Object[] contentObjects) {
        this.templateConfig = templateConfig;
        injectionCache = new HashMap<>(contentObjects.length);
        initializeCache(templateConfig.getClassesConfigurations(), contentObjects);
    }

    /**
     * Initialize the engine's cache to enhance performance and ease the access to the content
     *
     * @param classesConfigurations Classes configurations provided by the developer
     * @param contentObjects        The objects holding the injection content
     */
    private void initializeCache(Map<Class<?>, KIClassConfiguration> classesConfigurations, Object[] contentObjects) {
        for (Object contentObject : contentObjects) {
            Class<?> objectClass = contentObject.getClass();
            KIClassConfiguration classConfig = classesConfigurations.getOrDefault(objectClass, new KIClassConfiguration(objectClass));
            InjectionEngineCache injectionCache = new InjectionEngineCache(contentObject, classConfig);

            this.injectionCache.put(classConfig.getTargetClassAlias(), injectionCache);
        }
    }

    /**
     * Handle a single line injection
     *
     * @param templateLine The target line containing the injection templates
     * @return The processed line after injection, or the same line (if no injection templates, or if corrupted injection templates were found)
     * @throws ReflectiveOperationException An exception is thrown if an injection failed due to wrong methods/classes provided
     */
    String InjectSingleLine(String templateLine) throws ReflectiveOperationException {
        String injectedLine = templateLine;

        Set<String> injectionTemplates = fetchInjectionTemplates(templateLine);

        if (injectionTemplates.size() == 0)
            return injectedLine;

        for (String injectionTemplate : injectionTemplates) {

            Object injectionValue = fetchInjectionValue(injectionTemplate);

            if (injectionValue == null)
                continue;

            injectedLine = injectedLine.replaceAll(Matcher.quoteReplacement(injectionTemplate), injectionValue.toString());
        }

        return injectedLine;
    }

    /**
     * Handle loop injections
     *
     * @param loopBlock The block containing the injection loop
     * @return The processed block after injection, or the same block (if no injection templates, or if corrupted injection templates were found)
     * @throws ReflectiveOperationException An exception is thrown if an injection failed due to wrong methods/classes provided
     */
    String InjectLoop(String loopBlock) throws ReflectiveOperationException {

        String loopExtraction = extractLoopTemplate(loopBlock);
        if (loopExtraction == null)
            return loopBlock;

        String injectedBlock = loopExtraction.replace(templateConfig.getLoopStartFullWord(), "");
        injectedBlock = injectedBlock.replace(templateConfig.getLoopEndFullWord(), "");

        injectedBlock = handleLoopInjection(injectedBlock);

        return loopBlock.replace(loopExtraction, injectedBlock);
    }

    /**
     * Start loop injection process
     *
     * @param loopExtraction The exact loop block after removing extra strings that should not be included in the loop, and the start/end loop words
     * @return The loop block after injection
     * @throws ReflectiveOperationException An exception is thrown if an injection failed due to wrong methods/classes provided
     */
    private String handleLoopInjection(String loopExtraction) throws ReflectiveOperationException {
        StringBuilder injectedBlock = new StringBuilder();

        boolean isMultilineLoop = loopExtraction.contains("\n");
        Set<String> injectionTemplates = fetchInjectionTemplates(loopExtraction);
        Map<String, Object> injectionCollections = new HashMap<>();
        int maxCollectionSize = fetchInjectionCollections(injectionTemplates, injectionCollections);


        for (int i = 0; i < maxCollectionSize; i++) {
            injectedBlock.append(handleInjectionIteration(i, loopExtraction, injectionCollections));
            injectedBlock.append(isMultilineLoop ? "\n" : " ");
        }

        return injectedBlock.toString();
    }

    /**
     * Handle a single iteration's injection of a loop
     *
     * @param index                The current iteration index
     * @param loopExtraction       The loop template
     * @param injectionCollections The map holding the injection template as the key, and the object holding the injection content as the value
     * @return The injected string for a single iteration
     */
    private String handleInjectionIteration(int index, String loopExtraction, Map<String, Object> injectionCollections) {
        String injectedIteration = loopExtraction;
        for (Map.Entry<String, Object> injectionEntry : injectionCollections.entrySet()) {
            String injectionTemplate = injectionEntry.getKey();
            Object injectionObject = injectionEntry.getValue();
            if (!(injectionObject instanceof Collection) && !(injectionObject instanceof Object[])) {
                injectedIteration = injectedIteration.replaceAll(Matcher.quoteReplacement(injectionTemplate), injectionObject.toString());
                continue;
            }

            if (injectionObject instanceof Collection)
                injectionCollections.put(injectionTemplate, injectionObject = ((Collection<?>) injectionObject).toArray());

            Object[] injectionObjectsArray = (Object[]) injectionObject;
            String injectionValue = injectionObjectsArray.length < index ? injectionObjectsArray[index].toString() : "";

            injectedIteration = injectedIteration.replaceAll(Matcher.quoteReplacement(injectionTemplate), injectionValue);
        }
        return injectedIteration;
    }

    /**
     * Map the injection templates in a loop to the corresponding injection content
     *
     * @param injectionTemplates   A set holding the injection templates in a loop
     * @param injectionCollections The map to be updated with the mapping between injection templates and their content
     * @return The maximum collection size between injection contents that are of type collection
     * @throws ReflectiveOperationException An exception is thrown if an injection failed due to wrong methods/classes provided
     */
    private int fetchInjectionCollections(Set<String> injectionTemplates, Map<String, Object> injectionCollections) throws ReflectiveOperationException {
        int maxCollectionSize = 0;

        for (String injectionTemplate : injectionTemplates) {
            Object injectionObject = fetchInjectionValue(injectionTemplate);
            injectionCollections.put(injectionTemplate, injectionObject);
            if (!(injectionObject instanceof Collection<?>))
                continue;
            int collectionSize = ((Collection<?>) injectionObject).size();
            maxCollectionSize = maxCollectionSize < collectionSize ? collectionSize : maxCollectionSize;
        }
        return maxCollectionSize;
    }

    /**
     * Extract the loop part from the loop block excluding any string outside the block enclosed by the start/end loop words
     *
     * @param loopBlock The block where a loop is detected
     * @return The exact block enclosed by the start/end loop words (including the start/end loop words)
     */
    private String extractLoopTemplate(String loopBlock) {
        int loopStartIndex = loopBlock.indexOf(templateConfig.getLoopStartFullWord());

        if (loopStartIndex == -1)
            return null;

        int loopEndIndex = loopBlock.indexOf(templateConfig.getLoopEndFullWord(), loopStartIndex);

        if (loopEndIndex <= loopStartIndex)
            return null;

        return loopBlock.substring(loopStartIndex, loopEndIndex + templateConfig.getLoopEndFullWord().length());
    }

    /**
     * Resolving the injection template and getting the appropriate content
     *
     * @param injectionTemplate The string holding the injection template
     * @return The corresponding content after invoking the method in the injection template or null if the injection template is malformed
     * @throws ReflectiveOperationException An exception is thrown if an injection failed due to wrong methods/classes provided
     */
    private Object fetchInjectionValue(String injectionTemplate) throws ReflectiveOperationException {

        injectionTemplate = injectionTemplate.replaceAll(Matcher.quoteReplacement(templateConfig.getInjectionToken()), "");

        String[] injectionTemplateParts = injectionTemplate.split("\\.");

        if (injectionTemplateParts.length != 2)
            return null;

        String injectionClass = injectionTemplateParts[0];
        String injectionMethod = injectionTemplateParts[1];

        InjectionEngineCache targetClassInjection = injectionCache.get(injectionClass);

        return targetClassInjection.fetchInjection(injectionMethod);
    }

    /**
     * Fetch distinct injection templates from the template string
     *
     * @param templateString The raw string fetched from the template being parsed
     * @return A set containing all the distinct injection templates
     */
    private Set<String> fetchInjectionTemplates(String templateString) {
        Set<String> injectionTemplates = new HashSet<>();
        String token = templateConfig.getInjectionToken();

        if (!templateString.contains(token))
            return injectionTemplates;

        int indexOfFirstToken;
        int indexOfSecondToken = 0;
        int tokenSize = templateConfig.getInjectionToken().length();

        while ((indexOfFirstToken = templateString.indexOf(token, indexOfSecondToken)) != -1) {
            indexOfSecondToken = templateString.indexOf(token, indexOfFirstToken + tokenSize);

            if (indexOfSecondToken <= indexOfFirstToken)
                break;

            String injectionTemplate = templateString.substring(indexOfFirstToken, indexOfSecondToken + tokenSize);
            if (!injectionTemplate.contains("."))
                continue;
            injectionTemplates.add(injectionTemplate);

        }

        return injectionTemplates;
    }

    /**
     * The Injection Cache is meant to link a content object with it's corresponding
     * KIClassConfiguration object to ease access to it, and enhance performance
     */
    class InjectionEngineCache {

        private final KIClassConfiguration classConfig;
        private final Object contentObject;

        /**
         * Construct an injection cache object
         *
         * @param contentObject The object holding the injection content
         * @param classConfig   The class configuration corresponding to the contentObject's class
         */
        InjectionEngineCache(Object contentObject, KIClassConfiguration classConfig) {
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
        Object fetchInjection(String methodName) throws ReflectiveOperationException {
            methodName = classConfig.getMethodName(methodName);
            Method targetMethod = classConfig.getTargetClass().getMethod(methodName);
            return targetMethod.invoke(contentObject);
        }
    }
}
