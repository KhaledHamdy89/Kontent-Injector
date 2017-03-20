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

import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;

/**
 * The KInjector responsible for handling the single line injections
 * Created by khaled.hamdy on 3/16/17.
 */
public class SingleLineKInjector extends AbstractKInjector {

    private String lineToProcess = "";
    private boolean isActive = false;

    public SingleLineKInjector(KITemplateConfiguration templateConfig, Map<String, InjectionEngineCache> injectionCache) {
        super(templateConfig, injectionCache);
    }

    @Override
    public void inspectLine(String templateLine) {
        if (!isLineProcessable(templateLine))
            return;

        lineToProcess = templateLine;
        isActive = true;
    }

    private boolean isLineProcessable(String templateLine) {
        boolean doesContainInjectionToken = templateLine.contains(templateConfig.getInjectionToken());
        boolean doesContainLoopStartWord = templateLine.contains(templateConfig.getLoopStartWord());
        boolean doesContainLoopEndWord = templateLine.contains(templateConfig.getLoopEndWord());

        return doesContainInjectionToken && !doesContainLoopStartWord && !doesContainLoopEndWord;
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

    @Override
    public boolean isReadyForProcessing() {
        return !lineToProcess.isEmpty();
    }

    /**
     * Handle a single line injection
     *
     * @return The processed line after injection, or the same line (if no injection templates, or if corrupted injection templates were found)
     * @throws ReflectiveOperationException An exception is thrown if an injection failed due to wrong methods/classes provided
     */
    @Override
    public String processInjection() throws ReflectiveOperationException {
        if (!isActive)
            return null;

        String injectedLine = lineToProcess;

        Set<String> injectionTemplates = fetchInjectionTemplates(lineToProcess);

        if (injectionTemplates.size() == 0)
            return injectedLine;

        for (String injectionTemplate : injectionTemplates) {

            Object injectionValue = fetchInjectionValue(injectionTemplate);

            if (injectionValue == null)
                continue;

            injectedLine = injectedLine.replaceAll(Matcher.quoteReplacement(injectionTemplate), injectionValue.toString());
        }

        isActive = false;
        lineToProcess = "";

        return injectedLine;
    }

}
