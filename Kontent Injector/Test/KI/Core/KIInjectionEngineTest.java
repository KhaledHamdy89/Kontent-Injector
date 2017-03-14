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

import KI.Models.KITemplateConfiguration;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * KI Injection Engine Unit Tests
 * Created by khaled.hamdy on 3/14/17.
 */
public class KIInjectionEngineTest {

    @Test
    public void injectSingleLine_LineWithoutInjection_ReturnSameLine() throws Exception {
        KITemplateConfiguration kiConfig = new KITemplateConfiguration();
        KIInjectionEngine injectionEngine = new KIInjectionEngine(kiConfig, new String[]{"Test"});

        String templateLine = "Test injection";
        String injectedLine = injectionEngine.InjectSingleLine(templateLine);
        assertTrue(templateLine.equals(injectedLine));
    }

    @Test
    public void injectSingleLine_LineWithInjection_NoConfigs_ContentMethodReturnString() throws Exception {
        KIInjectionEngine injectionEngine = new KIInjectionEngine(new KITemplateConfiguration(), new Object[]{new MockContentObject()});

        String templateLine = "This is how $%$MockContentObject.methodReturnsString$%$ is done!";
        String expectedLine = "This is how " + MockContentObject.EXPECTED_STRING_FROM_STRING + " is done!";
        String injectedLine = injectionEngine.InjectSingleLine(templateLine);
        assertTrue(expectedLine.equals(injectedLine));
    }

    @Test
    public void injectSingleLine_LineWithInjection_NoConfigs_ContentMethodReturnObject() throws Exception {
        KIInjectionEngine injectionEngine = new KIInjectionEngine(new KITemplateConfiguration(), new Object[]{new MockContentObject()});

        String templateLine = "This is how $%$MockContentObject.methodReturnsMockContentObject$%$ is done!";
        String expectedLine = "This is how " + MockContentObject.EXPECTED_STRING_FROM_OBJECT + " is done!";
        String injectedLine = injectionEngine.InjectSingleLine(templateLine);
        assertTrue(expectedLine.equals(injectedLine));
    }

    @Test
    public void injectSingleLine_LineWithMultipleInjections_NoConfigs_SameMethodCall() throws Exception {
        KIInjectionEngine injectionEngine = new KIInjectionEngine(new KITemplateConfiguration(), new Object[]{new MockContentObject()});

        String templateLine = "This is how $%$MockContentObject.methodReturnsString$%$ is done! $%$MockContentObject.methodReturnsString$%$ is done like that!";
        String expectedLine = "This is how " + MockContentObject.EXPECTED_STRING_FROM_STRING + " is done! " + MockContentObject.EXPECTED_STRING_FROM_STRING + " is done like that!";
        String injectedLine = injectionEngine.InjectSingleLine(templateLine);
        assertTrue(expectedLine.equals(injectedLine));
    }

    @Test
    public void injectSingleLine_LineWithMultipleInjections_NoConfigs_DifferentMethodCall() throws Exception {
        KIInjectionEngine injectionEngine = new KIInjectionEngine(new KITemplateConfiguration(), new Object[]{new MockContentObject()});

        String templateLine = "This is how $%$MockContentObject.methodReturnsString$%$ is done! $%$MockContentObject.methodReturnsMockContentObject$%$ is done like that!";
        String expectedLine = "This is how " + MockContentObject.EXPECTED_STRING_FROM_STRING + " is done! " + MockContentObject.EXPECTED_STRING_FROM_OBJECT + " is done like that!";
        String injectedLine = injectionEngine.InjectSingleLine(templateLine);
        assertTrue(expectedLine.equals(injectedLine));
    }

    @Test
    public void injectSingleLine_LineWithInjection_CustomTokenConfigs_ContentMethodReturnString() throws Exception {
        KITemplateConfiguration templateConfig = new KITemplateConfiguration();
        templateConfig.setInjectionToken("###");
        KIInjectionEngine injectionEngine = new KIInjectionEngine(templateConfig, new Object[]{new MockContentObject()});

        String templateLine = "This is how ###MockContentObject.methodReturnsString### is done!";
        String expectedLine = "This is how " + MockContentObject.EXPECTED_STRING_FROM_STRING + " is done!";
        String injectedLine = injectionEngine.InjectSingleLine(templateLine);
        assertTrue(expectedLine.equals(injectedLine));
    }

    @Test
    public void injectSingleLine_LineWithInjection_CustomToken_ClassAlias_ContentMethodReturnString() throws Exception {
        KITemplateConfiguration templateConfig = new KITemplateConfiguration();
        templateConfig.setInjectionToken("###");
        templateConfig.addClassAlias(MockContentObject.class, "coolestClass");
        KIInjectionEngine injectionEngine = new KIInjectionEngine(templateConfig, new Object[]{new MockContentObject()});

        String templateLine = "This is how ###coolestClass.methodReturnsString### is done!";
        String expectedLine = "This is how " + MockContentObject.EXPECTED_STRING_FROM_STRING + " is done!";
        String injectedLine = injectionEngine.InjectSingleLine(templateLine);
        assertTrue(expectedLine.equals(injectedLine));
    }

    @Test
    public void injectSingleLine_LineWithInjection_CustomToken_MethodAlias_ContentMethodReturnString() throws Exception {
        KITemplateConfiguration templateConfig = new KITemplateConfiguration();
        templateConfig.setInjectionToken("###");
        templateConfig.addMethodAlias(MockContentObject.class, "methodReturnsString", "coolestMethod");
        KIInjectionEngine injectionEngine = new KIInjectionEngine(templateConfig, new Object[]{new MockContentObject()});

        String templateLine = "This is how ###MockContentObject.coolestMethod### is done!";
        String expectedLine = "This is how " + MockContentObject.EXPECTED_STRING_FROM_STRING + " is done!";
        String injectedLine = injectionEngine.InjectSingleLine(templateLine);
        assertTrue(expectedLine.equals(injectedLine));
    }

    @Test
    public void injectSingleLine_LineWithInjection_CustomToken_ClassAlias_MethodAlias_ContentMethodReturnString() throws Exception {
        KITemplateConfiguration templateConfig = new KITemplateConfiguration();
        templateConfig.setInjectionToken("OOO");
        templateConfig.addClassAlias(MockContentObject.class, "coolClass");
        templateConfig.addMethodAlias(MockContentObject.class, "methodReturnsString", "coolMethod");
        KIInjectionEngine injectionEngine = new KIInjectionEngine(templateConfig, new Object[]{new MockContentObject()});

        String templateLine = "This is how OOOcoolClass.coolMethodOOO is done!";
        String expectedLine = "This is how " + MockContentObject.EXPECTED_STRING_FROM_STRING + " is done!";
        String injectedLine = injectionEngine.InjectSingleLine(templateLine);
        assertTrue(expectedLine.equals(injectedLine));
    }

    private class MockContentObject {

        static final String EXPECTED_STRING_FROM_STRING = "STRING INJECTION";
        static final String EXPECTED_STRING_FROM_OBJECT = "OBJECT INJECTION";


        public Object methodReturnsMockContentObject() {
            return new MockContentObject();
        }

        public String methodReturnsString() {
            return EXPECTED_STRING_FROM_STRING;
        }

        @Override
        public String toString() {
            return EXPECTED_STRING_FROM_OBJECT;
        }
    }

}