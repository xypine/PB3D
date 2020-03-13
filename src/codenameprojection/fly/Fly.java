/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package codenameprojection.fly;

import JFUtils.Range;
import JFUtils.point.Point2D;
import JFUtils.point.Point3D;
import JFUtils.point.Point3F;
import codenameprojection.driver;
import codenameprojection.model;
import codenameprojection.model_frame;
import java.awt.FlowLayout;
import java.util.Arrays;
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
        
        Driver.s.r.usePixelRendering = false;
        Driver.s.r.drawFaces = true;
        Driver.s.r.drawPoints = true;
        Driver.s.r.drawLines = false;
        Driver.s.r.shading = false;
        Driver.rotation_mode = false;
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
        int l_f = 0;
        Driver.inp.verbodose = true;
        Integer[] defaultKeys = new Integer[]{
            //74, 
            //76, 
            //73, 
            //75,
        };
        for(int i : new Range(1000)){
            Driver.ingoredInputs.add(i);
        }
        for(int i : defaultKeys){
            Driver.ingoredInputs.removeAll(Arrays.asList(defaultKeys));
        }
        //Driver.models.clear();
        int ship = (int) Driver.models.keySet().toArray()[0];
        LinkedList<model> points = constructCloud(); //new LinkedList<>(); //
        LinkedList<Integer> handles = new LinkedList<>();
        for(model m : points){
            Integer handle = m.hashCode();
            Driver.models.put(handle, m);
            handles.add(handle);
        }
        Driver.shadingMultiplier = 0.5F;
        LinkedList<model_frame> frames2 = new LinkedList<>();
        LinkedList<Point3D> points2 = new LinkedList<>();
        LinkedList<Integer[]> lines2 = new LinkedList<>();
        LinkedList<Point2D[]> faces2 = new LinkedList<>();
        points2.add(new Point3D(0, 0, 0));
        frames2.add(new model_frame(points2 , lines2, faces2));
        model cursor = new model(frames2, true);
        int cursorHandle = cursor.hashCode();
        Driver.models.put(cursorHandle, cursor);
        float speed = 0.4F;
        Driver.screenPosition_org = new Point3D(0, 0, 0);
        Point3D last_sp = Driver.screenPosition_org.clone();
        while (true) {
            Driver.angleYM = Driver.angleYM + Driver.inp.cX * 0.0002;
            Driver.inp.cX = (int) (Driver.inp.cX * 0.5);
            Driver.angleXM = Driver.angleXM + Driver.inp.cY * 0.0002;
            Driver.inp.cY = (int) (Driver.inp.cY * 0.5);
            //System.out.println(Driver.inp.mouseX());
            //w
            if(Driver.inp.keys[87] && false){
                //JFUtils.quickTools.alert("Space!");
                model m = Driver.models.get(cursorHandle);
                //Driver.screenPosition_org = Point3D.add(Driver.screenPosition_org, Point3D.multiply(Driver.viewAngle, new Point3D(1, 1, 1)));
                Point3D rot = Driver.matmul(Driver.RX((float)Driver.angleX), new Point3F(0, 0, -speed)).toDVector3();
                rot = Driver.matmul(Driver.RY((float)Driver.angleY), rot.toFVector3()).toDVector3();
                Driver.screenPosition_org = Point3D.add(Driver.screenPosition_org, rot);
                //transform.rotation * DirectionVector * 10f;
                //Driver.screenPosition_org = new Point3D(0, 0, 0);
                //m.getFrame(0).points.getFirst().x = 10;
               // m.getFrame(0).points.getFirst().y = 10;
                //m.getFrame(0).points.getFirst().z = 10;
                //Driver.inp.keys[32] = false;
            }
            //s
            if(Driver.inp.keys[83] && false){
                model m = Driver.models.get(cursorHandle);
                Point3D rot = Driver.matmul(Driver.RX((float)Driver.angleX), new Point3F(0, 0, speed)).toDVector3();
                rot = Driver.matmul(Driver.RY((float)Driver.angleY), rot.toFVector3()).toDVector3();
                Driver.screenPosition_org = Point3D.add(Driver.screenPosition_org, rot);
            }
            //a
            if(Driver.inp.keys[65] && false){
                model m = Driver.models.get(cursorHandle);
                Point3D rot = Driver.matmul(Driver.RX((float)Driver.angleX), new Point3F(speed, 0, 0)).toDVector3();
                rot = Driver.matmul(Driver.RY((float)Driver.angleY), rot.toFVector3()).toDVector3();
                Driver.screenPosition_org = Point3D.add(Driver.screenPosition_org, rot);
            }
            //d
            if(Driver.inp.keys[68] && false){
                model m = Driver.models.get(cursorHandle);
                Point3D rot = Driver.matmul(Driver.RX((float)Driver.angleX), new Point3F(-speed, 0, 0)).toDVector3();
                rot = Driver.matmul(Driver.RY((float)Driver.angleY), rot.toFVector3()).toDVector3();
                Driver.screenPosition_org = Point3D.add(Driver.screenPosition_org, rot);
            }
            model shipModel = Driver.models.get(ship);
            for(Point3D i : shipModel.getFrame(0).points){
                Point3D newLoc = Point3D.add(Driver.screenPosition_org, Point3D.subtract(i, last_sp));
                i.x = newLoc.x;
                i.y = newLoc.y;
                i.z = newLoc.z;
            }
            for(Integer handle : handles){
                model m = Driver.models.get(handle);
                if(m.getFrame(0).points.getFirst().z < size){
                    m.getFrame(0).points.getFirst().z = m.getFrame(0).points.getFirst().z + 0.03*50;
                }
                else{
                    m.getFrame(0).points.getFirst().z = -size;
                }
            }
            last_sp = Driver.screenPosition_org.clone();
            //Sleep
            try {
                Thread.sleep((long) 1);
            } catch (InterruptedException ex) {
                Logger.getLogger(Fly.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
    
    int size = 1400;
    
    int rx = 16;
    int ry = 15;
    int rz = 15;
    LinkedList<model> constructCloud(){
        Random rnd = new Random();
        LinkedList<model> out = new LinkedList<model>();
        for(int x : new Range(rx)){
            for (int y : new Range(ry)) {
                for (int z : new Range(rz)){
                    LinkedList<model_frame> frames = new LinkedList<>();
                    LinkedList<Point3D> points = new LinkedList<>();
                    LinkedList<Integer[]> lines = new LinkedList<>();
                    LinkedList<Point2D[]> faces = new LinkedList<>();
                    
                    int rndX = rnd.nextInt(size*2) - size;
                    int rndY = rnd.nextInt(size*2) - size;
                    int rndZ = rnd.nextInt(size*2) - size;
                    
                    boolean hasConnections = rnd.nextBoolean();
                    if(hasConnections){
                        hasConnections = rnd.nextBoolean();
                    }
                    if(hasConnections){
                        hasConnections = rnd.nextBoolean();
                    }
                    if(hasConnections){
                        hasConnections = rnd.nextBoolean();
                    }
                    if(hasConnections){
                        hasConnections = rnd.nextBoolean();
                    }
                    if(hasConnections){
                        hasConnections = rnd.nextBoolean();
                    }
                    
                    
                    points.add(new Point3D(rndX, rndY, rndZ));
                    
                    if(hasConnections && out.size() > 2){
                        //lines.add(new Integer[]{points.getFirst().identifier, out.get(rnd.nextInt(out.size()-1)).getFrame(0).points.getFirst().identifier});
                    }
                    
                    
                    
                    frames.add(new model_frame(points , lines, faces));
                    model m = new model(frames, true);
                    out.add(m);
                }
            }
        }
        
        return out;
    }
    
}
