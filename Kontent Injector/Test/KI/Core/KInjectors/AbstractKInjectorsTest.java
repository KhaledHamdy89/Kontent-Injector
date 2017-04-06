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
import KI.Models.KIClassConfiguration;

import java.util.HashMap;
import java.util.Map;

/**
 * The Abstract KInjector class holding all common methods needed for KInjectors tests
 * Created by khaled.hamdy on 3/22/17.
 */
public abstract class AbstractKInjectorsTest {
    protected Map<String, InjectionEngineCache> injectionCache;

    /**
     * Initialize the injection cache map
     *
     * @param classesConfigurations Classes configuration from the injection template
     * @param contentObjects        Objects holding the content to be injected
     */
    protected void initializeCache(Map<Class<?>, KIClassConfiguration> classesConfigurations, Object[] contentObjects) {
        injectionCache = new HashMap<>();
        for (Object contentObject : contentObjects) {
            Class<?> objectClass = contentObject.getClass();
            KIClassConfiguration classConfig = classesConfigurations.getOrDefault(objectClass, new KIClassConfiguration(objectClass));
            InjectionEngineCache injectionCache = new InjectionEngineCache(contentObject, classConfig);

            this.injectionCache.put(classConfig.getTargetClassAlias(), injectionCache);
        }
    }
}
