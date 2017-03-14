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

import KI.Models.KIInput.IKIInput;
import KI.Models.KIOutput.IKIOutput;
import KI.Models.KITemplateConfiguration;

/**
 * The KontentInjector class is the class responsible for initiating the injection process
 * and generating the output files after injection.
 * <p>
 * Created by khaled.hamdy on 2/14/17.
 */
public class KontentInjector {

    private KITemplateConfiguration currentKIConfig;

    public KontentInjector() {
        currentKIConfig = new KITemplateConfiguration();
    }

    public KontentInjector(KITemplateConfiguration injectionConfig) {
        configureInjector(injectionConfig);
    }

    /**
     * Provide the configuration to be used in the injection process
     *
     * @param injectionConfig The template configuration to be used in the injection
     */
    public void configureInjector(KITemplateConfiguration injectionConfig) {
        this.currentKIConfig = injectionConfig;
    }

    /**
     * Start the content injection process
     *
     * @param inputMethod    Used to provide a template as an input to the KI
     * @param outputMethod   Used by the KI to write the generated output after injection
     * @param contentObjects The objects containing the content to be injected into a template
     */
    private void injectValues(IKIInput inputMethod, IKIOutput outputMethod, Object... contentObjects) throws ReflectiveOperationException {
        if (contentObjects.length == 0)
            return;

        String templateLine;
        String loopBlock = "";
        String injectionOutput;
        KIInjectionEngine injectionEngine = new KIInjectionEngine(currentKIConfig, contentObjects);

        while ((templateLine = inputMethod.readTemplateLine()) != null) {
            injectionOutput = "";

            if (!loopBlock.isEmpty())
                loopBlock += "\n" + templateLine;

            if (templateLine.contains(currentKIConfig.getLoopStartWord())) {
                loopBlock = templateLine;
                continue;
            }

            if (templateLine.contains(currentKIConfig.getLoopEndWord())) {
                injectionOutput = injectionEngine.InjectLoop(loopBlock);
                loopBlock = "";
            }

            if (templateLine.contains(currentKIConfig.getInjectionToken())) {
                injectionOutput = injectionEngine.InjectSingleLine(templateLine);
            }

            outputMethod.writeLine(injectionOutput);
        }
        outputMethod.handleOutputEnd();
    }

}
