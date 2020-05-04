/*
 * The MIT License
 *
 * Copyright 2019 Elias Eskelinen.
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

package fps;

import JFUtils.Range;
import JFUtils.point.Point2D;
import JFUtils.point.Point3D;
import JFUtils.point.Point3F;
import JFUtils.vector.dVector3;
import codenameprojection.Flags;
import codenameprojection.Utils;
import codenameprojection.drawables.vertexGroup;
import codenameprojection.driver;
import codenameprojection.models.Model;
import codenameprojection.modelParser;
import codenameprojection.models.ModelFrame;
import java.awt.FlowLayout;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JSlider;
import kuusisto.tinysound.Sound;
import kuusisto.tinysound.TinySound;

/**
 *
 * @author Jonnelafin
 */
public class FPSTest {
    public static void main(String[] args) {
        new FPSTest();
    }

    codenameprojection.driver Driver;
    JFrame frame;
    private Instant beginTime;
    private Duration deltaTime;
    int boltCooldown = 0;
    float jetleft = 70;
    boolean menu = false;
    boolean pause = false;
    public FPSTest() {
        Driver = new driver();
        Thread t = new Thread(){
            @Override
            public void run() {
                super.run(); //To change body of generated methods, choose Tools | Templates.
                Driver.run();
                //Driver = new driver(null);
            }
            
        };
        modelParser.filename = "assets/models/hl/gman_talk";
        modelParser.size = 100;
        Driver.startWithNoModel = false;
        
        
        t.start();
        while(!Driver.running){
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(FPSTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        int cubeHandle = 0;
        
        LinkedList<Model> points = constructCloud(); // //new LinkedList<>();
        LinkedList<Integer> handles = new LinkedList<>();
        for(Model m : points){
            Integer handle = m.hashCode();
            Driver.models.put(handle, m);
            handles.add(handle);
        }
        LinkedList<Model> grid = constructGrid();
        LinkedList<Integer> gridHandles = new LinkedList<>();
        for(Model m : grid){
            Integer handle = m.hashCode();
            Driver.models.put(handle, m);
            gridHandles.add(handle);
        }
        
        Driver.s.r.debug = false;
        
        Driver.an_pause = false;
        //Driver.zero();
        
        double scale = 1;
        Driver.s.r.scale = 1 / scale;
        Driver.s.r.scale_restore = 1 * scale;
        
        Driver.s.r.usePixelRendering = true;
        Driver.s.r.drawFaces = true;
        Driver.s.r.drawPoints = true;
        Driver.s.r.drawLines = true;
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
            //87,
            //a
            //65,
            //83,
            //d
            //68,
    //        81, //q
    //        69  //e
        };
        for(int i : new Range(1000)){
            Driver.ingoredInputs.add(i);
        }
        for(int i : defaultKeys){
            Driver.ingoredInputs.removeAll(Arrays.asList(defaultKeys));
        }
        
        LinkedList<Integer> boltHandles = new LinkedList<>();
        float speed = 0.4F;
//        Driver.screenPosition_org = new Point3D(0, 0, 0);
//        Point3D last_sp = Driver.screenPosition_org.clone();
        float side = 1.3F;
        float up = -2F;
        float front = -8;
        float front2 = 0;
        float shipRotY = 0;
        float shipRotZ = 0;
        
        //model singlePoint = loadPoint();
        //int singlePointHandle = singlePoint.hashCode();
        //Driver.models.put(singlePointHandle, singlePoint);
        
        
        float thrust = 0F;
        float thrust2 = 0F;
        float v = 0F;
        Point3D vel = new Point3D(0, 0, 0);
        //float shipRotX = 0;
        
        double jump = 0;
        double jump_vel = 0;
        
        double shift_m = 1;
        Model first_object = (Model) Driver.models.get(Driver.defaultModelKey);
        first_object.hideLines = true;
        double speed2 = 0.2;
        first_object.animationSpeed = speed2;
        first_object.setY(-.5);
        System.out.println("Default model name: " + first_object.name);
        Sound pew = null;
        if (Flags.soundEnabled) {
            pew = TinySound.loadSound(new File(new JFUtils.dirs().music + "pew.wav"));
        }
        
        Point3D volDir = new Point3D(0, 0, 0);
        
        double volRight = 0;
        double volLeft = 0;
        double pan = 0.5;
        Driver.shadingAdd = 220;
        Driver.shadingMin = 5;
        Driver.shadingMultiplier = 3F;
        Driver.screenPosition_org_next = new Point3D(0, -2.75, 1.4);
        Driver.screenPosition_org_next.identifier = -2;
        first_object.hideLines = false;
        first_object.hideFaces = true;
        first_object.hidePoints = true;
        while (Driver.s.r.frame < 170 / speed2){
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(FPSTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        first_object.hidePoints = true;
        first_object.hideFaces = false;
        first_object.hideLines = true;
        first_object.scale = 0.75;
        first_object.single_frame = true;
        first_object.minFrame = 59;
        Driver.shadingAdd = 0;
        Driver.shadingMin = 20;
        Driver.shadingMultiplier = 1.4F;
        FPSMap map = new FPSMap(this);
        Driver.s.r.extraDrawables.add(map);
        Driver.screenPosition_org_next = new Point3D( 0, 0, 7 );
        Driver.screenPosition_org_next.identifier = -2;
        while (true) {
            Model gordon = (Model) Driver.models.get(Driver.defaultModelKey);
            //model single = (Model) Driver.models.get(singlePointHandle);
            beginTime = Instant.now();
            /*single.rotation_Y = single.rotation_Y + 0.0001;
            single.scale = 40;
            single.hidePoints = false;
            single.hideFaces = true;
            single.hideLines = true;*/
            //jump = jump * .99999;
                //!Driver.inp.parentInFocus || 
            if((Driver.inp.isEscDown) && !menu){
                if(Driver.inp.isEscDown){
                    Driver.inp.isEscDown = false;
                }
                map.showMenu();
            }
            
            /*if(Driver.inp.mouseWheel == 1){
            int last = Driver.inp.mouseWheel;
            if(last == 2){
            Driver.s.r.scale = Driver.s.r.scale * 2;
            Driver.s.r.scale_restore = Driver.s.r.scale_restore / 2;
            }
            if(last == 0){
            Driver.s.r.scale = Driver.s.r.scale / 2;
            Driver.s.r.scale_restore = Driver.s.r.scale_restore * 2;
            }
            System.out.println(last);
            Driver.inp.mouseWheel = 1;
            }*/
            
            if (!pause) {
                //Point3D singleLoc = single.getFrame(0).points.get(1);
                //gordon.setX(pan);
                volDir = Point3D.subtract(gordon.getLoc(), Driver.screenPosition);
                volRight =(100 - volDir.x);
                volLeft  =(100 - volDir.z);
                pan =  1 - (volLeft - volRight)/5F;
                //pan = pan + .33333333F;
                if(pan < -.8){
                    pan = -.8;
                }
                if(pan > .8){
                    pan = .8;
                }
                //System.out.println(pan);
                //System.out.println(volDir);
                gordon.rotation_Y = gordon.rotation_Y + 0.00001;
                if (jump > 0) {
                    jump = jump - 0.00024F;
                } else if(Driver.getScreenPosition_org().x < 30 && Driver.getScreenPosition_org().z < 30 && Driver.getScreenPosition_org().x > -30 && Driver.getScreenPosition_org().z > -30){
                    jump = jump *0.9999;
                }
                else{
                    jump = jump - 0.00024F*2*deltaTime.getNano()*0.0001;
                }
                jump = jump + jump_vel;
                jump_vel = jump_vel * 0;
                vel = new Point3D(0, 0, 0);
                Point3D rotVec = Driver.matmul(Driver.RX((float) Driver.angleX), new Point3F(thrust2, 0, -thrust)).toDVector3();
                rotVec = Driver.matmul(Driver.RY((float) Driver.angleY), rotVec.toFVector3()).toDVector3();
                rotVec.y = 0;
                Point3D screenPos = Driver.getScreenPosition_org().clone();
                screenPos.y = 0;
                vel = Point3D.add(screenPos, rotVec);
                double sin = Math.sin(Math.abs(vel.x/2) + Math.abs(vel.z/2));
                if (jump > 0) {
                    sin = 0;
                }
                vel.y = -1.8F - jump + sin * 0.12F;
                Driver.screenPosition_org_next = vel.clone();
                Driver.screenPosition_org_next.identifier = -2;
                Model cubeM = (Model) Driver.models.values().toArray()[cubeHandle];
                cubeM.hideLines = true;
                cubeM.hidePoints = true;
                thrust = thrust * 0.99F;
                thrust2 = thrust2 * 0.99F;
            }
            
            if (!Driver.inp.isControlDown && !pause) {
                //System.setProperty("apple.awt.fullscreenhidecursor","true");
                try {                                               //(deltaTime.getNano() * .0000000003)
                    Driver.angleYM = Driver.angleYM + Driver.inp.cX * 0.0005;
                    //System.out.println(Driver.angleYM);
                    Driver.inp.cX = (int) (Driver.inp.cX * 0.1);    //0.0002
                    Driver.angleXM = Driver.angleXM + Driver.inp.cY * 0.0005;
                    Driver.inp.cY = (int) (Driver.inp.cY * 0.1);
                } catch (Exception e) {
                }
            }
            else{
                System.setProperty("apple.awt.fullscreenhidecursor","false");
            }
            //System.out.println(Driver.inp.mouseX());
            LinkedList<Integer> torem = new LinkedList<>();
            Driver.an_pause = pause;
            if (!Driver.an_pause) {
                for (Integer bolt : boltHandles) {
                    try {
                        Model cursor = Driver.models.get(bolt);
                        //Point3D.multiply(Driver.viewAngle, new Point3D(1, 1, 1)
                        double speeds = 0.5;
                        Point3D one = cursor.frames.get(0).points.get(0);
                        Point3D two = cursor.frames.get(0).points.get(1);
                        //cursor.getFrame(0).points.get(0).z = cursor.getFrame(0).points.get(0).z - 0.5;
                        //cursor.getFrame(0).points.get(1).z = cursor.getFrame(0).points.get(1).z - 0.5;
                        Point3D newPo = Point3D.add(one, Point3D.subtract(one, two));
                        Point3D newPt = Point3D.add(two, Point3D.subtract(one, two));
                        Point3D velBolt = Point3D.subtract(one, two);
                        velBolt = Point3D.multiply(velBolt, new Point3D(.007, .007, .007));
                        cursor.setX(cursor.getX() + velBolt.x);
                        cursor.setY(cursor.getY() + velBolt.y);
                        cursor.setZ(cursor.getZ() + velBolt.z);
                        if (Math.abs(Utils.getDistance(new Point3D(cursor.getX(), cursor.getY(), cursor.getZ()), new Point3D(0, 0, 0))) > 2000) {
                            //Driver.models.remove(bolt);
                            torem.add(bolt);
                        } else {
                            //System.out.println(Math.abs(Utils.getDistance(new Point3D(cursor.x, cursor.y, cursor.z), new Point3D(0, 0, 0))));
                        }
                        //cursor.getFrame(0).points.get(0).x = newPo.x;
                        //cursor.getFrame(0).points.get(0).y = newPo.y;
                        //cursor.getFrame(0).points.get(0).z = newPo.z;

                        //cursor.getFrame(0).points.get(1).x = newPt.x;
                        //cursor.getFrame(0).points.get(1).y = newPt.y;
                        // cursor.getFrame(0).points.get(1).z = newPt.z;
                    } catch (Exception e) {
                    }
                }
            }
            if (!menu) {
                for (Integer bolt : torem) {
                    Driver.models.remove(bolt);
                    boltHandles.remove(bolt);
                }

                //meta
                if (Driver.inp.isShiftDown) {
                    shift_m = 2;
                } else {
                    shift_m = 1;
                }

                //space
                if (Driver.inp.keys[32] && jetleft > 0) {
                    jump_vel = jump_vel + 0.0007F * 1.5;
                    jetleft = jetleft - .5F;
                } else if (Driver.inp.keys[32]) {
                    jetleft = jetleft - .1F;
                }
                if (jetleft < 8000) {
                    jetleft = jetleft + 0.1F;
                }
                ////System.out.println(jetleft);
                //f //70
                //System.out.println(boltCooldown);
                if (Driver.inp.mouseDown && boltCooldown < -400) {
                    LinkedList<ModelFrame> frames2 = new LinkedList<>();
                    LinkedList<Point3D> points2 = new LinkedList<>();
                    LinkedList<Integer[]> lines2 = new LinkedList<>();
                    LinkedList<Point3D[]> faces2 = new LinkedList<>();
                    LinkedList<vertexGroup> color2 = new LinkedList<>();
                    Point3D from = Driver.getScreenPosition_org().clone();
                    from.x = -from.x;
                    from.y = -from.y - 1;
                    from.z = from.z - .1;
                    Point3D from2 = Driver.matmul(Driver.RX((float) -Driver.angleX), new Point3F(0, 0, 6)).toDVector3();
                    from2 = Driver.matmul(Driver.RY((float) -Driver.angleY), from2.toFVector3()).toDVector3();
                    from2 = Point3D.add(from2, from);
                    
                    points2.add(from);
                    points2.add(from2);
                    lines2.add(new Integer[]{from.identifier, from2.identifier});
                    frames2.add(new ModelFrame(points2, lines2, faces2, color2));
                    Model cursor = new Model(frames2, true);
                    int cursorHandle = cursor.hashCode();
                    Driver.models.put(cursorHandle, cursor);
                    //emit sound
                    if(Flags.soundEnabled){
                        //System.out.println(new JFUtils.dirs().music + "pew.mp3");
                        pew.play(.2, 0);
                    }
                    
                    boltHandles.add(cursorHandle);
                    boltCooldown = boltCooldown + 4500;
                } else if (boltCooldown > -500) {
                    boltCooldown = boltCooldown - 1;
                }
                //w
                if (Driver.inp.keys[87]) {
                    //    cubeM.setX(cubeM.getX() + 100);
                    //cubeM.rotation_X++;
                    //System.out.println("W!");
                    thrust = (float) (thrust + 0.0025F * shift_m);
                }
                //s
                if (Driver.inp.keys[83]) {
                    thrust = (float) (thrust - 0.0025F * shift_m);
                }
                //a
                if (Driver.inp.keys[65]) {
                    thrust2 = (float) (thrust2 + 0.0025F * shift_m);
                    
                }
                //d
                if (Driver.inp.keys[68]) {
                    thrust2 = (float) (thrust2 - 0.0025F * shift_m);
                }
            }
            //q
            if(Driver.inp.keys[81]){
            }
            //e
            if(Driver.inp.keys[69]){
            }
            //b
            if(Driver.inp.keys[66]){
                Driver.s.r.debug = !Driver.s.r.debug;
                Driver.inp.keys[66] = false;
            }
            //p
            if(Driver.inp.keys[80]){
                Driver.an_pause = !Driver.an_pause;
                Driver.inp.keys[80] = false;
            }
            //o
            if(Driver.inp.keys[79]){
                //Driver.s.r.
            }
            //r
            if(Driver.inp.keys[82]){
                Driver.s.r.scale = Driver.s.r.scale / 1.25;
                Driver.s.r.scale_restore = Driver.s.r.scale_restore * 1.25;
                Driver.inp.keys[82] = false;
            }
            //t
            if(Driver.inp.keys[84]){
                Driver.s.r.scale = Driver.s.r.scale * 1.25;
                Driver.s.r.scale_restore = Driver.s.r.scale_restore / 1.25;
                Driver.inp.keys[84] = false;
            }
//            last_sp = Driver.screenPosition_org.clone();
            
            
            //Sleep
            try {
                Thread.sleep((long) 0.00001);
            } catch (InterruptedException ex) {
                Logger.getLogger(FPSTest.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            deltaTime = Duration.between(beginTime, Instant.now());
//            System.out.println("Fly.java excecution time: " + deltaTime.getNano());
            Driver.s.r.customStrings_next.put("lol", "FPSDelta: " + deltaTime.getNano());
            Driver.s.r.customStrings_next.put("lol2", "Bolts: " + boltHandles.size());
        }
    }
    
    int size = 1400*10;
    
    int rx = 5;
    int ry = 5;
    int rz = 5;
    LinkedList<Model> constructCloud(){
        Random rnd = new Random();
        LinkedList<Model> out = new LinkedList<Model>();
        for(int x : new Range(rx)){
            for (int y : new Range(ry)) {
                for (int z : new Range(rz)){
                    LinkedList<ModelFrame> frames = new LinkedList<>();
                    LinkedList<Point3D> points = new LinkedList<>();
                    LinkedList<Integer[]> lines = new LinkedList<>();
                    LinkedList<Point3D[]> faces = new LinkedList<>();
                    LinkedList<vertexGroup> color = new LinkedList<>();
                    
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
                    
                    
                    
                    frames.add(new ModelFrame(points , lines, faces, color));
                    Model m = new Model(frames, true);
                    m.hidePoints = false;
                    out.add(m);
                }
            }
        }
        
        return out;
    }
    
    int rx2 = 15*2;
    int ry2 = rx2;
    int rz2 = 1;
    
    Model loadPoint(){
        Model out = null;
        try {
            LinkedList<ModelFrame> frames = new LinkedList<>();
            LinkedList<Point3D> points = new modelParser("assets/models/single").parse().getFirst();
            LinkedList<Integer[]> parseLines = new LinkedList<>();//new modelParser("assets/models/single").parseLines(points);
            LinkedList<Point3D[]> parseFaces = new LinkedList<>();//new modelParser("assets/models/single").parseFaces(points);
            LinkedList<vertexGroup> color = new LinkedList<>();
            frames.add(new ModelFrame(points , parseLines, parseFaces, color));
            out = new Model(frames, true);
            out.hidePoints = false;
        } catch (IOException ex) {
            Logger.getLogger(FPSTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return out;
    }
    
    LinkedList<Model> constructGrid(){
        int ind = 0;
        Random rnd = new Random();
        LinkedList<Model> out = new LinkedList<>();
        boolean facesU = true;
        Model m2 = null;
        try {
            LinkedList<Point3D> points3 = new modelParser("assets/models/misc/plane").parse().getFirst();
            LinkedList<Integer[]> parseLines3 = new modelParser("assets/models/misc/plane").parseLines(points3);
            LinkedList<Point3D[]> parseFaces3 = new modelParser("assets/models/misc/plane").parseFaces(points3);
            LinkedList<ModelFrame> frames2 = new LinkedList<>();
            frames2.add(new ModelFrame(points3, parseLines3, parseFaces3, new LinkedList<>()));
            m2 = new Model(frames2, true);
        } catch (IOException iOException) {
            facesU = false;
        }
        for(int z : new Range(rz2)){
            for (int x : new Range(rx2)) {
                for (int y: new Range(ry2)){
                    LinkedList<ModelFrame> frames = new LinkedList<>();
                    LinkedList<Point3D> points = new LinkedList<>();
                    LinkedList<Integer[]> lines = new LinkedList<>();
                    LinkedList<Point3D[]> faces = new LinkedList<>();
                    LinkedList<vertexGroup> color = new LinkedList<>();

                    double rndX = (x +0.5 - (rx2 / 2) )*2;
                    double rndY = (y +.5- (ry2 / 2) )*2;
                    double rndZ = (z +.5- (rz2 / 2) )*2;



                    points.add(new Point3D(-rndX, -rndZ, -rndY));


                    if(out.size() > 2){
                        try {
                            Point3D last = out.get(ind - 1).getFrame(0).points.getFirst();
                            if (y !=0) {
                                lines.add(new Integer[]{points.getFirst().identifier, last.identifier});
                            }
                        } catch (Exception e) {
                        }
                            
                            
                        try {
                            Point3D pair = out.get(ind - ry2).getFrame(0).points.getFirst();
                            if (x != 0) {
                                lines.add(new Integer[]{points.getFirst().identifier, pair.identifier});
                            }
                        } catch (Exception e) {
                        }
                        
                        try {
                            Point3D pair = out.get(ind - (rx2 * ry2)*rz2).getFrame(0).points.getFirst();
                            if (false) {
                                lines.add(new Integer[]{points.getFirst().identifier, pair.identifier});
                            }
                        } catch (Exception e) {
                            System.out.println(ind - (rx2 * ry2)*rz2);
                        }
                            //lines.add(new Integer[]{points.getFirst().identifier, out.get(ind - ry2).getFrame(0).points.getFirst().identifier});
                            //lines.add(new Integer[]{points.getFirst().identifier, out.get(ind - rz2).getFrame(0).points.getFirst().identifier});
                            //faces.add(new Point2D[]{

                            //});
                    }
                    //if(y == ry2){
                    //    lines.add(new Integer[]{points.getFirst().identifier, out.get(ind - ry2).getFrame(0).points.getFirst().identifier});
                    //}
                    /*
                    try {
                        LinkedList<Point3D> points3 = new modelParser("assets/models/misc/Plane").parse().getFirst();
                        LinkedList<Integer[]> parseLines3 = new modelParser("assets/models/misc/Plane").parseLines(points);
                        LinkedList<Point3D[]> parseFaces3 = new modelParser("assets/models/misc/Plane").parseFaces(points);
                        for(Point3D i : points3){
                            i.x = i.x + rndX;
                            i.y = i.y + rndX;
                            //i.z = i.z + rndX;
                        }
                        points.addAll(points3);
                        lines.addAll(parseLines3);
                        faces.addAll(parseFaces3);
                    } catch (IOException iOException) {
                    }*/
                    frames.add(new ModelFrame(points , lines, faces, color));
                    Model m = new Model(frames, true);
                    m.hidePoints = false;
                    m.hideLines = true;
                    Model m3 = m2.clone();
                    m3.setX(rndX);
                    m3.setY(rndY);
                    out.add(m);
                    //out.add(m3);
                    ind = ind + 1;
                }
            }
        }
        
        return out;
    }
    
}
