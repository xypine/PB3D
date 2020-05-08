/*
 * The MIT License
 *
 * Copyright 2020 Elias Eskelinen (elias.eskelinen@protonmail.com).
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package compressor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.LinkedList;
import net.jpountz.lz4.LZ4FrameInputStream;

/**
 *
 * @author Elias Eskelinen (elias.eskelinen@protonmail.com)
 */
public class IO {
    static int filesDone = 0;
    
    static LinkedList<String> dependencyRecord = new LinkedList<>();
    
    public static String readAsString(String path) throws IOException{
        return readAsString(path, true, true);
    }
    public static String readAsString(String path, boolean uncompress, boolean ignoreCompressError) throws IOException{
        if(!dependencyRecord.contains(path)){
            dependencyRecord.add(path);
        }
        filesDone++;
        if(uncompress){
            try {
                LZ4FrameInputStream inStream = new LZ4FrameInputStream(new FileInputStream(new File(path)));
                //inStream.read(restored);
                int read = inStream.read();
                LinkedList<Byte> red = new LinkedList();
                while (read != -1) {
                    red.add((byte) read);
                    read = inStream.read();
                }
                inStream.close();
                Byte[] uncompressed = red.toArray(new Byte[red.size()]);
                byte[] clean = new byte[uncompressed.length];
                int ind = 0;
                for (Byte i : red) {
                    clean[ind] = i;
                    ind++;
                }
                return new String(clean);
            } catch (Exception e) {
                if(ignoreCompressError){
                    return readAsString(path, false, false);
                }
                else{
                    throw e;
                }
            }
        }
        else{
            return readFile(path, Charset.defaultCharset());
        }
    }
    private static String readFile(String path, Charset encoding) throws IOException {
      byte[] encoded = Files.readAllBytes(Paths.get(path));
      return new String(encoded, encoding);
    }
}
