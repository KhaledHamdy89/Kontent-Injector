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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * The KInjector responsible for handling loop injections
 * Created by khaled.hamdy on 3/16/17.
 */
public class LoopKInjector extends AbstractKInjector {

    private String loopBlock = "";
    private boolean isActive = false;

    public LoopKInjector(KITemplateConfiguration templateConfig, Map<String, InjectionEngineCache> injectionCache) {
        super(templateConfig, injectionCache);
    }

    @Override
    public void inspectLine(String templateLine) {
        if (isActive()) {
            loopBlock += "\n" + templateLine;
            return;
        }

        if (!templateLine.contains(templateConfig.getLoopStartFullWord()))
            return;

        loopBlock = templateLine;
        isActive = true;
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

    @Override
    public boolean isReadyForProcessing() {
        return loopBlock.contains(templateConfig.getLoopEndFullWord());
    }

    /**
     * Handle loop injections
     *
     * @return The processed block after injection, or the same block (if no injection templates, or if corrupted injection templates were found)
     * @throws ReflectiveOperationException An exception is thrown if an injection failed due to wrong methods/classes provided
     */
    @Override
    public String processInjection() throws ReflectiveOperationException {
        String loopExtraction = extractLoopTemplate(loopBlock);
        if (loopExtraction == null)
            return loopBlock;

        String injectedBlock = loopExtraction.replace(templateConfig.getLoopStartFullWord(), "");
        injectedBlock = injectedBlock.replace(templateConfig.getLoopEndFullWord(), "");

        injectedBlock = handleLoopInjection(injectedBlock);
        injectedBlock = loopBlock.replace(loopExtraction, injectedBlock);
        loopBlock = "";
        isActive = false;
        return injectedBlock;
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
            //injectedBlock.append(isMultilineLoop ? "\n" : "");
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
                injectedIteration = injectedIteration.replaceAll(Pattern.quote(injectionTemplate), injectionObject.toString());
                continue;
            }

            if (injectionObject instanceof Collection)
                injectionCollections.put(injectionTemplate, injectionObject = ((Collection<?>) injectionObject).toArray());

            Object[] injectionObjectsArray = (Object[]) injectionObject;
            String injectionValue = index < injectionObjectsArray.length ? injectionObjectsArray[index].toString() : "";

            injectedIteration = injectedIteration.replaceAll(Pattern.quote(injectionTemplate), injectionValue);
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

}
