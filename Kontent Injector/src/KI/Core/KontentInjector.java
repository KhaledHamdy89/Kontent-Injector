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

import KI.Exceptions.InvalidInputException;
import KI.Models.KITemplateConfiguration;

import java.io.*;

/**
 * The KontentInjector class is the class responsible for initiating the injection process
 * and generating the output files after injection.
 * <p>
 * Created by khaled.hamdy on 2/14/17.
 */
public class KontentInjector {

    private KITemplateConfiguration currentKIConfig;

    public KontentInjector() {
        this(new KITemplateConfiguration());
    }

    public KontentInjector(KITemplateConfiguration injectionConfig) {
        configureInjector(injectionConfig);
    }

    public void configureInjector(KITemplateConfiguration injectionConfig) {
        this.currentKIConfig = injectionConfig;
    }

    public void injectValues(String templateString, StringBuilder outputString, Object... contentObjects) throws InvalidInputException {
        InputValidator.validate(templateString);
        injectValues(new StringReader(templateString), outputString, null, contentObjects);
    }

    public void injectValues(String templateString, File outputFile, Object... contentObjects) throws InvalidInputException {
        InputValidator.validate(templateString);
        injectValues(new StringReader(templateString), null, outputFile, contentObjects);
    }

    public void injectValues(File templateFile, StringBuilder outputString, Object... contentObjects) throws FileNotFoundException, InvalidInputException {
        InputValidator.validate(templateFile);
        injectValues(new FileReader(templateFile), outputString, null, contentObjects);
    }

    public void injectValues(File templateFile, File outputFile, Object... contentObjects) throws FileNotFoundException, InvalidInputException {
        InputValidator.validate(templateFile);
        injectValues(new FileReader(templateFile), null, outputFile, contentObjects);

    }

    private void injectValues(Reader reader, StringBuilder outputString, File outputFile, Object[] contentObjects) {
        if (contentObjects.length == 0)
            return;
        BufferedReader bufferReader = new BufferedReader(reader);
    }
}
