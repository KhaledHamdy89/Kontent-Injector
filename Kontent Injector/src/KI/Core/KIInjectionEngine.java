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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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

    KIInjectionEngine(KITemplateConfiguration templateConfig, Object[] contentObjects) {
        this.templateConfig = templateConfig;
        injectionCache = new HashMap<>(contentObjects.length);
        initializeCache(templateConfig.getClassesConfigurations(), contentObjects);
    }

    private void initializeCache(Map<Class<?>, KIClassConfiguration> classesConfigurations, Object[] contentObjects) {
        for (Object contentObject : contentObjects) {
            Class<?> objectClass = contentObject.getClass();
            KIClassConfiguration classConfig = classesConfigurations.getOrDefault(objectClass, new KIClassConfiguration(objectClass));
            InjectionEngineCache injectionCache = new InjectionEngineCache(classConfig, contentObject);

            this.injectionCache.put(classConfig.getTargetClassAlias(), injectionCache);
        }
    }

    String InjectLoop(String loopBlock) {
        // TODO: Implement Loop injection
        return null;
    }

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

    private Set<String> fetchInjectionTemplates(String line) {
        Set<String> injectionTemplates = new HashSet<>();
        String token = templateConfig.getInjectionToken();

        if (!line.contains(token))
            return injectionTemplates;

        int indexOfFirstToken;
        int indexOfSecondToken = 0;
        int tokenSize = templateConfig.getInjectionToken().length();

        while ((indexOfFirstToken = line.indexOf(token, indexOfSecondToken)) != -1) {
            indexOfSecondToken = line.indexOf(token, indexOfFirstToken + tokenSize);

            if (indexOfSecondToken <= indexOfFirstToken)
                break;

            String injectionTemplate = line.substring(indexOfFirstToken, indexOfSecondToken + tokenSize);
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

        InjectionEngineCache(KIClassConfiguration classConfig, Object contentObject) {
            this.classConfig = classConfig;
            this.contentObject = contentObject;
        }

        Object fetchInjection(String methodName) throws ReflectiveOperationException {
            methodName = classConfig.getMethodName(methodName);
            Method targetMethod = classConfig.getTargetClass().getMethod(methodName);
            return targetMethod.invoke(contentObject);
        }
    }
}
