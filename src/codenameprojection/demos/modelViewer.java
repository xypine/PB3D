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
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.LinkedList;
import java.util.Objects;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Elias Eskelinen (elias.eskelinen@protonmail.com)
 */
public class modelViewer {
    driver Driver;
    JTextField mName;
    JTextField mScale;
    JSlider an_slide;
    JSlider sel_slide;
    JLabel sel_label;
    JLabel an_label;
    JCheckBox ignoreOrigin;
    JSlider ignoreOriginR;
    JLabel ignoreOriginRLabel;
    public static void main(String[] args) {
        new modelViewer();
    }
    public modelViewer() {
        driver d = new driver();
        this.Driver = d;
        modelParser.filename = "assets/models/misc/deform";
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
        JPanel c = new JPanel(new GridLayout(6, 2));
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
        //////////////
        ignoreOrigin = new JCheckBox("");
        ignoreOrigin.addItemListener(new aListener(3, this));
        c.add(new JLabel("Ignore model origin point when drawing faces: "));
        c.add(ignoreOrigin);
        //////////////
        ignoreOriginR = new JSlider(1, 100, 1);
        ignoreOriginR.addChangeListener(new aListener(5, this));
        ignoreOriginRLabel = new JLabel("Ignore Origin Threshold: ");
        c.add(ignoreOriginRLabel);
        c.add(ignoreOriginR);
        //////////////
        sel_slide = new JSlider(0, 100, 0);
        sel_slide.addChangeListener(new aListener(4, this));
        sel_label = new JLabel("Selected point:");
        c.add(sel_label);
        c.add(sel_slide);
        
        JButton reload = new JButton("Apply");
        aListener list = new aListener(1, this);
        list.update();
        reload.addActionListener(list);
        control.add(reload, BorderLayout.PAGE_END);
        
        //Show the frame
        control.setVisible(true);
    }
    
}
class aListener implements ActionListener, ChangeListener, ItemListener{
    static int ignoreRootR = 1;
    
    static Color backup;
    static float br;
    static float bg;
    static float bb;
    
    int mode = 0;
    static int maxF = 0;
    static int maxP = 0;
    static int selPoint = 0;
    static boolean ignoreOriginOnFaces = false;
    static modelViewer parent;
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
        modelParser.filename = this.parent.mName.getText().replace('\\', '/');
        try {
            modelParser.size = Float.parseFloat(this.parent.mScale.getText());
        } catch (NumberFormatException numberFormatException) {
            JFUtils.quickTools.alert("Invalid scale");
        }
        loadModel();
        setAnText();
    }
    public void setAnText(){
        parent.Driver.frame = parent.an_slide.getValue();
        Model m = parent.Driver.models.get(parent.Driver.defaultModelKey);
        
        //Restore last color
        vertexGroup colGroup = m.getFrame(0).color.get(selPoint);
        if (Objects.nonNull(br)) {
            colGroup.r = br;
            colGroup.g = bg;
            colGroup.b = bb;
        }
        
        //Set the new color
        selPoint = parent.sel_slide.getValue();
        vertexGroup colGroup2 = m.getFrame(0).color.get(selPoint);
        backup = new Color(colGroup2.r, colGroup2.g, colGroup2.b);
        br = colGroup2.r;
        bg = colGroup2.g;
        bb = colGroup2.b;
        
        colGroup2.r = 1;
        colGroup2.g = 0;
        colGroup2.b = 0;
        
        
        parent.an_slide.setMaximum(maxF);
        parent.sel_slide.setMaximum(maxP-1);
        System.out.println("Frame " + parent.an_slide.getValue() + " / " + maxF);
        parent.an_label.setText("Animation Frame (" + parent.Driver.frame + "/" + maxF + ")");
        System.out.println("Point " + selPoint + " / " + maxP);
        parent.sel_label.setText("Selected point (" + selPoint + "/" + maxP + "): ");
        System.out.println("Ignore Origin Threshold " + ignoreRootR + " / " + maxP);
        parent.ignoreOriginRLabel.setText("Ignore Origin Threshold (" + ignoreRootR + "/" + maxP + "): ");
        
        parent.ignoreOriginR.setMaximum(maxP);
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
                maxP = p.getFirst().size();
                System.out.println("Model has " + maxP + " points in " + maxF + " frames");
                Model m = new Model(frames, frames.size() < 3);
                m.hidePoints = false;
                m.ignoreRootNode = ignoreOriginOnFaces;
                m.ignoreRootNodeThreshold = ignoreRootR;
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
            setAnText();
        }
        if(mode == 4){
            //System.out.println(parent.an_slide.getValue());
            setAnText();
        }
        if(mode == 5){
            ignoreRootR = parent.ignoreOriginR.getValue();
            try {
                parent.Driver.models.get(parent.Driver.defaultModelKey).ignoreRootNodeThreshold = parent.ignoreOriginR.getValue();
            } catch (Exception e) {
                JFUtils.quickTools.alert("Modelviewer error", "Couldn't change default model: " + e);
            }
            setAnText();
        }
    }

    
    void radioUpdate(ItemEvent e){
        ignoreOriginOnFaces = e.getStateChange()==1;
        try {
            parent.Driver.models.get(parent.Driver.defaultModelKey).ignoreRootNode = ignoreOriginOnFaces;
        } catch (Exception e2) {
        }
        System.out.println(ignoreOriginOnFaces?"Checked":"Unchecked");
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if(mode == 3){
            radioUpdate(e);
        }
    }
    
}
