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
package codenameprojection.demos;

import codenameprojection.models.ModelUtils;
import compressor.GUI;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author Elias Eskelinen (elias.eskelinen@protonmail.com)
 */
public class Launcher extends JFrame{
    
    static Class[] vanilla = {ModelUtils.class, GUI.class};
    
    public static void main(String[] args) {
        for(Class i : vanilla){
            try {
                Record.announce(i);
            } catch (InstantiationException | IllegalAccessException ex) {
                Logger.getLogger(Launcher.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Launcher launcher = new Launcher();
    }
    public Launcher() throws HeadlessException {
        setSize(500, 500);
        setTitle("demo launcher");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new FlowLayout());
        LinkedList<demoInterface> recorded = Record.getRecorded();
        for(demoInterface i : recorded){
            add(new JLabel(i.NAME));
        }
        setVisible(true);
    }
    
}
//class act