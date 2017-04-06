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

import KI.Models.KITemplateConfiguration;
import Mocks.MockContentObject;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * The Single Line KInjector unit tests
 * Created by khaled.hamdy on 3/16/17.
 */
public class SingleLineKInjectorTest extends AbstractKInjectorsTest {

    @Test
    public void injectSingleLine_LineWithoutInjection_NotActiveNotReady() {
        KITemplateConfiguration templateConfig = new KITemplateConfiguration();
        initializeCache(templateConfig.getClassesConfigurations(), new Object[]{new MockContentObject()});
        SingleLineKInjector injectionEngine = new SingleLineKInjector(templateConfig, injectionCache);

        String templateLine = "This is how injection is done!";
        injectionEngine.inspectLine(templateLine);
        assertFalse(injectionEngine.isActive());
        assertFalse(injectionEngine.isReadyForProcessing());
    }

    @Test
    public void injectSingleLine_LineWithLoopWords_NotActiveNotReady() throws Exception {
        KITemplateConfiguration templateConfig = new KITemplateConfiguration();
        templateConfig.setInjectionToken("^^");
        initializeCache(templateConfig.getClassesConfigurations(), new Object[]{new MockContentObject()});
        SingleLineKInjector injectionEngine = new SingleLineKInjector(templateConfig, injectionCache);

        String templateLine = "This is how ^^Loop^^ ^^MockContentObject.methodReturnsString^^ is done! ^^ENDLOOP^^";
        injectionEngine.inspectLine(templateLine);
        assertFalse(injectionEngine.isActive());
        assertFalse(injectionEngine.isReadyForProcessing());
    }

    @Test
    public void injectSingleLine_LineWithInjection_NoConfigs_ContentMethodReturnString() throws Exception {
        KITemplateConfiguration templateConfig = new KITemplateConfiguration();
        initializeCache(templateConfig.getClassesConfigurations(), new Object[]{new MockContentObject()});
        SingleLineKInjector injectionEngine = new SingleLineKInjector(templateConfig, injectionCache);

        String templateLine = "This is how $%$MockContentObject.methodReturnsString$%$ is done!";
        String expectedLine = "This is how " + MockContentObject.EXPECTED_STRING_FROM_STRING + " is done!";
        String injectedLine = startInjectionProcess(injectionEngine, templateLine);
        assertTrue(expectedLine.equals(injectedLine));
    }

    @Test
    public void injectSingleLine_2ConsecutiveLinesWithInjection_NoConfigs_ContentMethodReturnString() throws Exception {
        KITemplateConfiguration templateConfig = new KITemplateConfiguration();
        initializeCache(templateConfig.getClassesConfigurations(), new Object[]{new MockContentObject()});
        SingleLineKInjector injectionEngine = new SingleLineKInjector(templateConfig, injectionCache);

        String templateLine = "This is how $%$MockContentObject.methodReturnsString$%$ is done!";
        String expectedLine = "This is how " + MockContentObject.EXPECTED_STRING_FROM_STRING + " is done!";
        String injectedLine = startInjectionProcess(injectionEngine, templateLine);
        assertTrue(expectedLine.equals(injectedLine));


        String templateLine2 = "This is how $%$MockContentObject.methodReturnsString$%$ is done! AGAIN!";
        String expectedLine2 = "This is how " + MockContentObject.EXPECTED_STRING_FROM_STRING + " is done! AGAIN!";
        String injectedLine2 = startInjectionProcess(injectionEngine, templateLine2);
        assertTrue(expectedLine2.equals(injectedLine2));
    }

    @Test
    public void injectSingleLine_LineWithInjection_NoConfigs_ContentMethodReturnObject() throws Exception {
        KITemplateConfiguration templateConfig = new KITemplateConfiguration();
        initializeCache(templateConfig.getClassesConfigurations(), new Object[]{new MockContentObject()});
        SingleLineKInjector injectionEngine = new SingleLineKInjector(templateConfig, injectionCache);

        String templateLine = "This is how $%$MockContentObject.methodReturnsMockContentObject$%$ is done!";
        String expectedLine = "This is how " + MockContentObject.EXPECTED_STRING_FROM_OBJECT + " is done!";
        String injectedLine = startInjectionProcess(injectionEngine, templateLine);
        assertTrue(expectedLine.equals(injectedLine));
    }

    @Test
    public void injectSingleLine_LineWithMultipleInjections_NoConfigs_SameMethodCall() throws Exception {
        KITemplateConfiguration templateConfig = new KITemplateConfiguration();
        initializeCache(templateConfig.getClassesConfigurations(), new Object[]{new MockContentObject()});
        SingleLineKInjector injectionEngine = new SingleLineKInjector(templateConfig, injectionCache);

        String templateLine = "This is how $%$MockContentObject.methodReturnsString$%$ is done! $%$MockContentObject.methodReturnsString$%$ is done like that!";
        String expectedLine = "This is how " + MockContentObject.EXPECTED_STRING_FROM_STRING + " is done! " + MockContentObject.EXPECTED_STRING_FROM_STRING + " is done like that!";
        String injectedLine = startInjectionProcess(injectionEngine, templateLine);
        assertTrue(expectedLine.equals(injectedLine));
    }

    @Test
    public void injectSingleLine_LineWithMultipleInjections_NoConfigs_DifferentMethodCall() throws Exception {
        KITemplateConfiguration templateConfig = new KITemplateConfiguration();
        initializeCache(templateConfig.getClassesConfigurations(), new Object[]{new MockContentObject()});
        SingleLineKInjector injectionEngine = new SingleLineKInjector(templateConfig, injectionCache);

        String templateLine = "This is how $%$MockContentObject.methodReturnsString$%$ is done! $%$MockContentObject.methodReturnsMockContentObject$%$ is done like that!";
        String expectedLine = "This is how " + MockContentObject.EXPECTED_STRING_FROM_STRING + " is done! " + MockContentObject.EXPECTED_STRING_FROM_OBJECT + " is done like that!";
        String injectedLine = startInjectionProcess(injectionEngine, templateLine);
        assertTrue(expectedLine.equals(injectedLine));
    }

    @Test
    public void injectSingleLine_LineWithInjection_CustomTokenConfigs_ContentMethodReturnString() throws Exception {
        KITemplateConfiguration templateConfig = new KITemplateConfiguration();
        templateConfig.setInjectionToken("###");
        initializeCache(templateConfig.getClassesConfigurations(), new Object[]{new MockContentObject()});
        SingleLineKInjector injectionEngine = new SingleLineKInjector(templateConfig, injectionCache);

        String templateLine = "This is how ###MockContentObject.methodReturnsString### is done!";
        String expectedLine = "This is how " + MockContentObject.EXPECTED_STRING_FROM_STRING + " is done!";
        String injectedLine = startInjectionProcess(injectionEngine, templateLine);
        assertTrue(expectedLine.equals(injectedLine));
    }

    @Test
    public void injectSingleLine_LineWithInjection_CustomToken_ClassAlias_ContentMethodReturnString() throws Exception {
        KITemplateConfiguration templateConfig = new KITemplateConfiguration();
        templateConfig.setInjectionToken("###");
        templateConfig.addClassAlias(MockContentObject.class, "coolestClass");
        initializeCache(templateConfig.getClassesConfigurations(), new Object[]{new MockContentObject()});
        SingleLineKInjector injectionEngine = new SingleLineKInjector(templateConfig, injectionCache);

        String templateLine = "This is how ###coolestClass.methodReturnsString### is done!";
        String expectedLine = "This is how " + MockContentObject.EXPECTED_STRING_FROM_STRING + " is done!";
        String injectedLine = startInjectionProcess(injectionEngine, templateLine);
        assertTrue(expectedLine.equals(injectedLine));
    }

    @Test
    public void injectSingleLine_LineWithInjection_CustomToken_MethodAlias_ContentMethodReturnString() throws Exception {
        KITemplateConfiguration templateConfig = new KITemplateConfiguration();
        templateConfig.setInjectionToken("###");
        templateConfig.addMethodAlias(MockContentObject.class, "methodReturnsString", "coolestMethod");
        initializeCache(templateConfig.getClassesConfigurations(), new Object[]{new MockContentObject()});
        SingleLineKInjector injectionEngine = new SingleLineKInjector(templateConfig, injectionCache);

        String templateLine = "This is how ###MockContentObject.coolestMethod### is done!";
        String expectedLine = "This is how " + MockContentObject.EXPECTED_STRING_FROM_STRING + " is done!";
        String injectedLine = startInjectionProcess(injectionEngine, templateLine);
        assertTrue(expectedLine.equals(injectedLine));
    }

    @Test
    public void injectSingleLine_LineWithInjection_CustomToken_ClassAlias_MethodAlias_ContentMethodReturnString() throws Exception {
        KITemplateConfiguration templateConfig = new KITemplateConfiguration();
        templateConfig.setInjectionToken(";;");
        templateConfig.addClassAlias(MockContentObject.class, "coolClass");
        templateConfig.addMethodAlias(MockContentObject.class, "methodReturnsString", "coolMethod");
        initializeCache(templateConfig.getClassesConfigurations(), new Object[]{new MockContentObject()});
        SingleLineKInjector injectionEngine = new SingleLineKInjector(templateConfig, injectionCache);

        String templateLine = "This is how ;;coolClass.coolMethod;; is done!";
        String expectedLine = "This is how " + MockContentObject.EXPECTED_STRING_FROM_STRING + " is done!";
        String injectedLine = startInjectionProcess(injectionEngine, templateLine);
        assertTrue(expectedLine.equals(injectedLine));
    }

    private String startInjectionProcess(SingleLineKInjector injectionEngine, String templateLine) throws ReflectiveOperationException {
        injectionEngine.inspectLine(templateLine);
        assertTrue(injectionEngine.isActive());
        assertTrue(injectionEngine.isReadyForProcessing());
        return injectionEngine.processInjection();
    }

}