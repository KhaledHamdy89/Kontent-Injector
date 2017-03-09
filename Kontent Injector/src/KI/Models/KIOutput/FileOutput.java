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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * The File Output class allows the user to send a File for the KI
 * to write the output to after processing
 * <p>
 * Created by khaled.hamdy on 3/9/17.
 */
public class FileOutput implements IKIOutput {

    private final PrintWriter fileWriter;

    /**
     * The File Output class allows the user to send a File for the KI
     * to write the output to after processing
     *
     * @param filePath Output file path
     * @throws IOException An IOException is thrown if an exception occurred
     *                     while creating the file's print writer
     */
    public FileOutput(String filePath) throws IOException {
        this(new File(filePath));
    }

    /**
     * The File Output class allows the user to send a File for the KI
     * to write the output to after processing
     *
     * @param outputFile Output File
     * @throws IOException An IOException is thrown if an exception occurred
     *                     while creating the file's print writer
     */
    public FileOutput(File outputFile) throws IOException {
        if (outputFile.exists())
            outputFile.delete();
        outputFile.createNewFile();
        fileWriter = new PrintWriter(new FileWriter(outputFile));
    }

    @Override
    public void writeLine(String outputLine) {
        fileWriter.println(outputLine);
        fileWriter.flush();
    }

    @Override
    public void outputEnded() {
        fileWriter.close();
    }
}
