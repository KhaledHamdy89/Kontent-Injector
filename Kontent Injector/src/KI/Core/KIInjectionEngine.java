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

import KI.Core.KInjectors.AbstractKInjector;
import KI.Core.KInjectors.LoopKInjector;
import KI.Core.KInjectors.SingleLineKInjector;
import KI.Models.KIClassConfiguration;
import KI.Models.KITemplateConfiguration;

import java.util.*;

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
    private List<AbstractKInjector> availableKInjectors;

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
        initializeKInjectors();
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
     * Initialize the KInjector list with all the available KInjectors
     */
    private void initializeKInjectors() {
        availableKInjectors = new ArrayList<>();
        availableKInjectors.add(new SingleLineKInjector(templateConfig, injectionCache));
        availableKInjectors.add(new LoopKInjector(templateConfig, injectionCache));
    }

    /**
     * Process template line using the available KInjectors
     *
     * @param templateLine Template line to process
     * @return The injected line, or null if processing is not currently possible (If more lines are required for processing)
     * @throws ReflectiveOperationException An exception is thrown if an injection failed due to wrong methods/classes provided
     */
    public String processLine(String templateLine) throws ReflectiveOperationException {
        Collections.sort(availableKInjectors);
        boolean hasActive = false;

        for (AbstractKInjector kinjector : availableKInjectors) {
            if (hasActive && !kinjector.isActive())
                return null;

            kinjector.inspectLine(templateLine);

            hasActive = kinjector.isActive();

            if (kinjector.isReadyForProcessing()) {
                return kinjector.processInjection();
            }
        }

        return hasActive ? null : templateLine;
    }
}
