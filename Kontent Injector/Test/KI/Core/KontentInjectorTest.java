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
import KI.Models.KIInput.StringInput;
import KI.Models.KIOutput.StringBuilderOutput;
import KI.Models.KITemplateConfiguration;
import Mocks.MockContentObject;
import Mocks.MockContentObject2;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * This is a test intended to test the integration of all the components of the Kontent-Injector
 * Created by khaled.hamdy on 4/6/17.
 */
public class KontentInjectorTest {

    private String getTestTemplate() {
        String template;
        template = "Let's test some injection!\n\n";
        template += "We first start off by testing a single line injection\n";
        template += "Awesome KInjector says: @@MockContentObject.methodReturnsString@@\n";
        template += "now we test using a class alias and a method alias! @@Mock2.The_S_Method@@\n";
        template += "now! A class Alias and no method alias @@Mock2.methodReturnsMockContentObject@@\n";
        template += "For our big finale! LOOOOOOOOOPS\n\n";
        template += "Single Line: @@LOOP@@element: @@MockContentObject.methodReturnStringList@@ @@ENDLOOP@@\n";
        template += "AND MULTILINE!! @@LOOP@@here you go... @@Mock2.methodReturnStringList@@ ...\n";
        template += "repeat!! @@ENDLOOP@@\n";
        template += "Report...\n";
        template += "Everything is AWESOME!";
        return template;
    }

    private String getExpectedInjection() {
        String expectedInjection;
        expectedInjection = "Let's test some injection!\n\n";
        expectedInjection += "We first start off by testing a single line injection\n";
        expectedInjection += "Awesome KInjector says: STRING INJECTION\n";
        expectedInjection += "now we test using a class alias and a method alias! STRING INJECTION V2\n";
        expectedInjection += "now! A class Alias and no method alias OBJECT INJECTION V2\n";
        expectedInjection += "For our big finale! LOOOOOOOOOPS\n\n";
        expectedInjection += "Single Line: element: 1 element: 2 element: 3 element: Cool \n";
        expectedInjection += "AND MULTILINE!! here you go... 1 V2 ...\n";
        expectedInjection += "repeat!! here you go... 2 V2 ...\n";
        expectedInjection += "repeat!! here you go... 3 V2 ...\n";
        expectedInjection += "repeat!! here you go... Cool V2 ...\n";
        expectedInjection += "repeat!! \n";
        expectedInjection += "Report...\n";
        expectedInjection += "Everything is AWESOME!\n";
        return expectedInjection;
    }

    private KITemplateConfiguration getTestTemplateConfig() throws InvalidInputException {
        KITemplateConfiguration config = new KITemplateConfiguration();
        config.setInjectionToken("@@");
        config.addClassAlias(MockContentObject2.class, "Mock2");
        config.addMethodAlias(MockContentObject2.class, "methodReturnsString", "The_S_Method");
        return config;
    }

    @Test
    public void injectValues() throws Exception {
        KontentInjector injector = new KontentInjector();
        injector.configureInjector(getTestTemplateConfig());
        StringBuilder injectionHolder = new StringBuilder();
        StringInput input = new StringInput(getTestTemplate());
        StringBuilderOutput output = new StringBuilderOutput(injectionHolder);
        injector.injectValues(input, output, new MockContentObject(), new MockContentObject2());
        String injectionOutput = injectionHolder.toString();
        assertTrue(injectionOutput.equals(getExpectedInjection()));
    }

}