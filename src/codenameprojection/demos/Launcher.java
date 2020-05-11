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
import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.security.Permission;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 *
 * @author Elias Eskelinen (elias.eskelinen@protonmail.com)
 */
public class Launcher extends JFrame implements ActionListener{
    
    static Class[] vanilla = {null, ModelUtils.class, GUI.class};
    static String[] titles = {"Choose a demo from here","Collision-mesh generator", "Compressor"};
    static String[] comments = {"Choose a demo from the dropdown", "A tool for generating collision-meshes", "lz4 compression tool and demo"};
    
    JComboBox List;
    JTextArea comment = new JTextArea();
    JButton button = new JButton("Launch demo!");
    
    public static void main(String[] args) {
        Launcher launcher = new Launcher();
    }
    public Launcher() throws HeadlessException{
        setSize(500, 500);
        setTitle("demo launcher");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        LinkedList<demoInterface> recorded = Record.getRecorded();
        List = new JComboBox(titles);
        List.addActionListener(this);
        JPanel buttonFrame = new JPanel();
        buttonFrame.add(button);
        button.addActionListener(this);
        add(List, BorderLayout.PAGE_START);
        add(comment, BorderLayout.CENTER);
        add(buttonFrame, BorderLayout.LINE_END);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        int i = List.getSelectedIndex();
        comment.setText(comments[i]);
        if(arg0.getActionCommand().equals(button.getText())){
                Thread t = new Thread(){
                    @Override
                    public void run() {
                        SecurityManager original = System.getSecurityManager();
                        try {
                            MySecurityManager secManager = new MySecurityManager();
                            System.setSecurityManager(secManager);
                            Method methodToFind = null;
                            super.run(); //To change body of generated methods, choose Tools | Templates.
                            methodToFind = vanilla[i].getMethod("main", String[].class);
                            String[] params = null; // init params accordingly
                            methodToFind.invoke(null, (Object) params);
                            System.setSecurityManager(original);
                        } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | NullPointerException e) {
                            JFUtils.quickTools.alert("Invalid demo, as it has no main method!");
                            e.printStackTrace();
                            System.setSecurityManager(original);
                            //e.printStackTrace();
                        }
                        catch (SecurityException e){
                            System.setSecurityManager(original);
                        } catch (InvocationTargetException ex) {
                            ex.getCause().printStackTrace();
                            System.setSecurityManager(original);
                        }
                        System.setSecurityManager(original);
                    }
                };
                setVisible(false);
                t.run();
                setVisible(true);
        }
    }
    
}
//class act
class MySecurityManager extends SecurityManager {
    @Override
    public void checkPermission(Permission perm) {
    }

    @Override
    public void checkPermission(Permission perm, Object context) {
    }

    @Override
    public void checkCreateClassLoader() {
    }

    @Override
    public void checkAccess(Thread t) {
    }

    @Override
    public void checkAccess(ThreadGroup g) {
    }

    @Override
    public void checkExit(int status) {
        System.out.println("Exit code " + status);
        if(status != 0){
            throw new SecurityException("not allow to call System.exit");
        }
    }

    @Override
    public void checkExec(String cmd) {
    }

    @Override
    public void checkLink(String lib) {
    }

    @Override
    public void checkRead(FileDescriptor fd) {
    }

    @Override
    public void checkRead(String file) {
    }

    @Override
    public void checkRead(String file, Object context) {
    }

    @Override
    public void checkWrite(FileDescriptor fd) {
    }

    @Override
    public void checkWrite(String file) {
    }

    @Override
    public void checkDelete(String file) {
    }

    @Override
    public void checkConnect(String host, int port) {
    }

    @Override
    public void checkConnect(String host, int port, Object context) {
    }

    @Override
    public void checkListen(int port) {
    }

    @Override
    public void checkAccept(String host, int port) {
    }

    @Override
    public void checkMulticast(InetAddress maddr) {
    }

    @Override
    public void checkPropertiesAccess() {
    }

    @Override
    public void checkPropertyAccess(String key) {
    }

    @Override
    public void checkPrintJobAccess() {
    }

    @Override
    public void checkPackageAccess(String pkg) {
    }

    @Override
    public void checkPackageDefinition(String pkg) {
    }

    @Override
    public void checkSetFactory() {
    }

    @Override
    public void checkSecurityAccess(String target) {
    }
}