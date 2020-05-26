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

import JFUtils.point.Point3D;
import codenameprojection.drawables.vertexGroup;
import javax.swing.JFrame;
import codenameprojection.driver;
import codenameprojection.modelParser;
import codenameprojection.models.Model;
import codenameprojection.models.ModelFrame;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Elias Eskelinen <elias.eskelinen@protonmail.com>
 */
public class modelViewer {
    driver Driver;
    JTextField mName;
    JTextField mScale;
    JSlider an_slide;
    JLabel an_label;
    public static void main(String[] args) {
        new modelViewer();
    }
    public modelViewer() {
        driver d = new driver();
        this.Driver = d;
        
        JFrame control = new JFrame();
        Thread t = new Thread(){
            @Override
            public void run() {
                super.run(); //To change body of generated methods, choose Tools | Templates.
                d.run();
            }
            
        };
        d.an_pause = true;
        t.start();
        //Wait until the driver has finished initialaizing
        while(!d.running){
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
            }
        }
        //Customize render settings
        d.s.r.drawPoints = false;
        //init control window
        control.setSize(600, 300);
        control.setLayout(new BorderLayout());
        control.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        //Init center panel
        JPanel c = new JPanel(new GridLayout(4, 4));
        control.add(c, BorderLayout.CENTER);
        
        //Add other components
        mName = new JTextField(modelParser.filename);
        c.add(new JLabel("Model filename:"));
        c.add(mName);
        /////////////
        mScale = new JTextField(modelParser.size + "");
        c.add(new JLabel("\nModel Scale:"));
        c.add(mScale);
        //////////////
        an_slide = new JSlider(0, 100, 0);
        an_slide.addChangeListener(new aListener(2, this));
        an_label = new JLabel("Animation Frame:");
        c.add(an_label);
        c.add(an_slide);
        
        JButton reload = new JButton("Apply");
        aListener list = new aListener(1, this);
        list.update();
        reload.addActionListener(list);
        control.add(reload, BorderLayout.PAGE_END);
        
        //Show the frame
        control.setVisible(true);
    }
    
}
class aListener implements ActionListener, ChangeListener{
    int mode = 0;
    int maxF = 0;
    modelViewer parent;
    public aListener(int mode, modelViewer parent) {
        this.mode = mode;
        this.parent = parent;
    }
    
    @Override
    public void actionPerformed(ActionEvent arg0) {
        if(mode == 1){
            update();
        }
    }
    
    
    public void update(){
        modelParser.filename = this.parent.mName.getText();
        try {
            modelParser.size = Float.parseFloat(this.parent.mScale.getText());
        } catch (NumberFormatException numberFormatException) {
            JFUtils.quickTools.alert("Invalid scale");
        }
        loadModel();
        setAnText();
    }
    public void setAnText(){
        parent.an_slide.setMaximum(maxF);
        parent.an_label.setText("Animation Frame (" + parent.Driver.frame + "/" + maxF + ")");
    }
    
    void loadModel(){
        this.parent.Driver.models.clear();
            try {
                LinkedList<LinkedList<Point3D>> p = new modelParser().parse();
                LinkedList<Integer[]> l = new modelParser().parseLines(p.getFirst());
                LinkedList<Point3D[]> f = new modelParser().parseFaces(p.getFirst());
                LinkedList<vertexGroup> c = new modelParser().parseColor(p.getFirst());
                LinkedList<ModelFrame> frames = new LinkedList<>();
                for(LinkedList<Point3D> i : p){
                    frames.add(new ModelFrame(i, l, f, c));
                }
                maxF = frames.size();
                Model m = new Model(frames, frames.size() < 3);
                m.hidePoints = false;
                int hash = m.hashCode();
                this.parent.Driver.models.put(hash, m);
                this.parent.Driver.defaultModelKey = hash;
            } catch (Exception ex) {
                JFUtils.quickTools.alert("Failed to load model", ex+"");
            }
    }

    @Override
    public void stateChanged(ChangeEvent arg0) {
        if(mode == 2){
            //System.out.println(parent.an_slide.getValue());
            parent.Driver.frame = parent.an_slide.getValue();
            setAnText();
        }
    }
    
}
