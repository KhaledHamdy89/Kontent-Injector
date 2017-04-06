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
 * The Loop KInjector unit tests
 * Created by khaled.hamdy on 3/22/17.
 */
public class LoopKInjectorTest extends AbstractKInjectorsTest {
    @Test
    public void injectLoop_NonLoopWithoutInjection_NotActiveNotReady() {
        KITemplateConfiguration templateConfig = new KITemplateConfiguration();
        initializeCache(templateConfig.getClassesConfigurations(), new Object[]{new MockContentObject()});
        LoopKInjector injectionEngine = new LoopKInjector(templateConfig, injectionCache);

        String templateLine = "This is how loop injection is done!";
        injectionEngine.inspectLine(templateLine);
        assertFalse(injectionEngine.isActive());
        assertFalse(injectionEngine.isReadyForProcessing());
    }

    @Test
    public void injectLoop_NotFullLoopInjection_ActiveNotReady() {
        KITemplateConfiguration templateConfig = new KITemplateConfiguration();
        initializeCache(templateConfig.getClassesConfigurations(), new Object[]{new MockContentObject()});
        LoopKInjector injectionEngine = new LoopKInjector(templateConfig, injectionCache);

        String templateLine = "This is how $%$LOOP$%$ loop injection is done!";
        injectionEngine.inspectLine(templateLine);
        assertTrue(injectionEngine.isActive());
        assertFalse(injectionEngine.isReadyForProcessing());
    }

    @Test
    public void injectLoop_SingleLineLoopInjection_ContentMethodReturningString() throws Exception {
        KITemplateConfiguration templateConfig = new KITemplateConfiguration();
        templateConfig.setInjectionToken("*");
        initializeCache(templateConfig.getClassesConfigurations(), new Object[]{new MockContentObject()});
        LoopKInjector injectionEngine = new LoopKInjector(templateConfig, injectionCache);

        String templateLine = "This is how *LOOP*loop #*MockContentObject.methodReturnStringList* *ENDLOOP*injection is done!";
        String expectedLine = "This is how loop #1 loop #2 loop #3 loop #Cool injection is done!";
        injectionEngine.inspectLine(templateLine);
        String injectedLine = startInjectionProcess(injectionEngine);
        assertTrue(expectedLine.equals(injectedLine));
    }

    @Test
    public void injectLoop_MultiLineLoopInjection_ContentMethodReturningString() throws Exception {
        KITemplateConfiguration templateConfig = new KITemplateConfiguration();
        templateConfig.setInjectionToken("*");
        initializeCache(templateConfig.getClassesConfigurations(), new Object[]{new MockContentObject()});
        LoopKInjector injectionEngine = new LoopKInjector(templateConfig, injectionCache);

        String templateLine = "This is how *LOOP*loop #*MockContentObject.methodReturnStringList*";
        String templateLine2 = "*ENDLOOP*injection is done!";
        String expectedLine = "This is how loop #1\nloop #2\nloop #3\nloop #Cool\ninjection is done!";
        injectionEngine.inspectLine(templateLine);
        assertTrue(injectionEngine.isActive());
        assertFalse(injectionEngine.isReadyForProcessing());
        injectionEngine.inspectLine(templateLine2);
        String injectedLine = startInjectionProcess(injectionEngine);
        assertTrue(expectedLine.equals(injectedLine));
    }

    private String startInjectionProcess(LoopKInjector injectionEngine) throws ReflectiveOperationException {
        assertTrue(injectionEngine.isActive());
        assertTrue(injectionEngine.isReadyForProcessing());
        return injectionEngine.processInjection();
    }
}