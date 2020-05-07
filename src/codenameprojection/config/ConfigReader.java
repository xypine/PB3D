/*
 * The MIT License
 *
 * Copyright 2020 Elias Eskelinen <elias.eskelinen@protonmail.com>.
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
package codenameprojection.config;

import JFUtils.Range;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Elias Eskelinen <elias.eskelinen@protonmail.com>
 */
public class ConfigReader {
    static String configPath = new JFUtils.dirs().config + "features.txt";
    
    public static void main(String[] args) {
        System.out.println("Testing config loading...");
        HashMap red = load();
        System.out.println("Keys, values:");
        for(int i : new Range(red.size())){
            System.out.println(red.keySet().toArray()[i] + ": " + red.values().toArray()[i]);
        }
    }
    public static void 
    static String override = 
        "#Enables the hashcheck\n" +
        "secure true\n" +
        "#Nearest release-version\n" +
        "version 3\n";
    public static HashMap<String, Object> load(){
        HashMap<String, Object> out = new HashMap<>();
        try {
            String source = compressor.IO.readAsString(configPath);
            //source = override;
            //System.out.println(source);
            String buffer = "";
            String name = "";
            boolean comment = false;
            for(char i : source.toCharArray()){
                //System.out.println(i);
                switch (i) {
                    case '#':
                        if(buffer.equals("")){
                            comment = true;
                        }
                        break;
                    case ' ':
                        name = buffer;
                        buffer = "";
                        break;
                    case '\n':
                        if (!comment) {
                            Object value = null;
                            if ("true".equals(buffer) || "false".equals(buffer)) {
                                value = "true".equals(buffer);
                            } else {
                                try {
                                    value = Double.parseDouble(buffer);
                                } catch (NumberFormatException numberFormatException) {
                                    value = buffer;
                                }
                            }                            
                            out.put(name, value);
                            buffer = "";
                            name = "";
                        } else {
                            comment = false;
                        }
                        break;
                    default:
                        if(!comment){
                            buffer = buffer + i;
                        }
                        break;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ConfigReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return out;
    }
}
