/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package codenameprojection.fly;

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
public class Fly {
    public static void main(String[] args) {
        new Fly();
    }

    codenameprojection.driver Driver;
    JFrame frame;
    public Fly() {
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
                Logger.getLogger(Fly.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Driver.an_pause = true;
        //Driver.zero();
        
        Driver.s.r.usePixelRendering = true;
        Driver.s.r.drawFaces = true;
        Driver.s.r.drawPoints = true;
        Driver.s.r.drawLines = false;
        System.out.println("Init complete!");
        
        frame = new JFrame("\"PB3D\" Fly simulator :)");
        
        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());
        
        JSlider step = new JSlider(0, 100, 0);
        
        frame.add(step);
        //frame.setVisible(true);
        
        float p = -1;
        
        int a = 2;
        Random r = new Random();
        float x = 0;
        float y = 0;
        
    }
    
}
