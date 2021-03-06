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

package KI.Models.KIOutput;

/**
 * The String builder Output allows the user to pass a string builder
 * object to be used by the KI to write the output to after processing
 * Created by khaled.hamdy on 3/9/17.
 */
public class StringBuilderOutput implements IKIOutput {

    private final StringBuilder outputStringBuilder;

    /**
     * The String builder Output allows the user to pass a string builder
     * object to be used by the KI to write the output to after processing
     *
     * @param outputStringBuilder The string builder object to be used by the KI
     */
    public StringBuilderOutput(StringBuilder outputStringBuilder) {
        this.outputStringBuilder = outputStringBuilder;
    }

    @Override
    public void writeLine(String outputLine) {
        outputStringBuilder.append(outputLine + "\n");
    }

    @Override
    public void handleOutputEnd() {
    }
}
