/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package treeGenerator;

import JFUtils.Range;
import JFUtils.vector.dVector3;
import codenameprojection.CodeNameProjection;
import codenameprojection.driver;
import java.awt.FlowLayout;
import java.util.LinkedList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JSlider;

/**
 *
 * @author Jonnelafin
 */
public class runz {
    public static void main(String[] args) {
        new runz();
    }

    codenameprojection.driver Driver;
    JFrame frame;
    public runz() {
        Driver = new driver(null);
        Thread t = new Thread(){
            @Override
            public void run() {
                super.run(); //To change body of generated methods, choose Tools | Templates.
                Driver.run();
                //Driver = new driver(null);
            }
            
        };
        t.start();
        while(!Driver.running){
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(runz.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Driver.an_pause = true;
        Driver.zero();
        
        Driver.s.r.usePixelRendering = false;
        Driver.s.r.drawFaces = false;
        Driver.s.r.drawPoints = true;
        Driver.s.r.drawLines = true;
        System.out.println("Init complete!");
        
        frame = new JFrame("\"PB3D\" Tree generator");
        
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());
        
        JSlider step = new JSlider(0, 100, 0);
        
        frame.add(step);
        frame.setVisible(true);
        
        float p = -1;
        
        int a = 2;
        Random r = new Random();
        float x = 0;
        float y = 0;
        while (true) {            
            if(step.getValue() /100F != p){
                p = step.getValue() / 100F;
                Driver.zero();
                float z = 0;
                x = x / 2;
                y = y / 2;
                
                Driver.points.add(new dVector3(0, 0, 0));
                for(int i : new Range(a)){
                    z = z + p;
                    if(r.nextInt(5) == 1){
                        if(r.nextBoolean()){
                            x = x + 0.1F;
                        }
                        else{
                            x = x - 0.1F;
                        }
                    }
                    Driver.points.add(new dVector3(x, z, y));
                    Driver.lines.add(new Integer[]{Driver.points.getLast().identifier, Driver.points.getFirst().identifier});
                }
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(runz.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
