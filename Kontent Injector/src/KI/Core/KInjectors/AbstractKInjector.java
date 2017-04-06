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

package KI.Core.KInjectors;

import KI.Core.InjectionEngineCache;
import KI.Models.KITemplateConfiguration;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * The abstract KInjector is the abstract class defining the common methods expected from KInjectors to be used for injection
 * Created by khaled.hamdy on 3/16/17.
 */
public abstract class AbstractKInjector implements Comparable<AbstractKInjector> {

    protected final KITemplateConfiguration templateConfig;
    private final Map<String, InjectionEngineCache> injectionCache;


    AbstractKInjector(KITemplateConfiguration templateConfig, Map<String, InjectionEngineCache> injectionCache) {
        this.templateConfig = templateConfig;
        this.injectionCache = injectionCache;
    }

    /**
     * Every KInjector should be able to examine a single template line to determine if it
     * can process it or if it will be active but still need more lines to process.
     *
     * @param templateLine Single template line
     */
    public abstract void inspectLine(String templateLine);

    /**
     * Indicates if the KInjector is active or not
     *
     * @return A boolean indicating if the KInjector is active or not
     */
    public abstract boolean isActive();

    /**
     * Indicates if the KInjector is ready for processing or not
     *
     * @return A boolean indicating if the KInjector is ready for processing or not
     */
    public abstract boolean isReadyForProcessing();

    /**
     * Process the collected input after examination and return the injected string
     *
     * @return A string holding the injected template part that was collected through examination
     * @throws ReflectiveOperationException An exception is thrown if an injection failed due to wrong methods/classes provided
     */
    public abstract String processInjection() throws ReflectiveOperationException;

    /**
     * Fetch distinct injection templates from the template string
     *
     * @param templateString The raw string fetched from the template being parsed
     * @return A set containing all the distinct injection templates
     */
    protected Set<String> fetchInjectionTemplates(String templateString) {
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
     * Resolving the injection template and getting the appropriate content
     *
     * @param injectionTemplate The string holding the injection template
     * @return The corresponding content after invoking the method in the injection template or null if the injection template is malformed
     * @throws ReflectiveOperationException An exception is thrown if an injection failed due to wrong methods/classes provided
     */
    protected Object fetchInjectionValue(String injectionTemplate) throws ReflectiveOperationException {

        injectionTemplate = injectionTemplate.replaceAll(Pattern.quote(templateConfig.getInjectionToken()), "");

        String[] injectionTemplateParts = injectionTemplate.split("\\.");

        if (injectionTemplateParts.length != 2)
            return null;

        String injectionClass = injectionTemplateParts[0];
        String injectionMethod = injectionTemplateParts[1];

        InjectionEngineCache targetClassInjection = injectionCache.get(injectionClass);

        return targetClassInjection.fetchInjection(injectionMethod);
    }

    @Override
    public int compareTo(AbstractKInjector kinjector) {
        return isActive() && !kinjector.isActive() ? 1 : -1;
    }
}
