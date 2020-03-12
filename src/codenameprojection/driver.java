/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package codenameprojection;

import JFUtils.Input;
import JFUtils.InputActivated;
import JFUtils.Range;
import JFUtils.graphing.Graph;
import JFUtils.point.Point2D;
import JFUtils.point.Point3D;
import JFUtils.point.Point3F;
import JFUtils.vector.dVector3;
import PBEngine.Supervisor;
import static codenameprojection.Utils.vToP2;
import java.awt.Color;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jonnelafin
 */
public class driver{
    public Supervisor pbSudo = null;
    
    private boolean usePB = true;
    
    
    
    Duration deltaTime = Duration.ZERO;
    Instant beginTime = Instant.now();
    
    boolean rotation = true;
    
    private int xScreenCenter = 320/2;
    private int yScreenCenter = 240/2;
    public Point3D screenPosition = new dVector3( 0, 0, 7 );
    public Point3D screenPosition_org = screenPosition.clone();
    public dVector3 viewAngle = new dVector3( 0, 90, 90 );
    public Point3D viewAngle_org = viewAngle.clone();
    
    private static final double DEG_TO_RAD = 0.017453292;
    private double modelScale = 10;
    
    double CT = Math.cos( DEG_TO_RAD * viewAngle.x );
    double ST = Math.sin( DEG_TO_RAD * viewAngle.x );
    double CP = Math.cos( DEG_TO_RAD * viewAngle.y );
    double SP = Math.sin( DEG_TO_RAD * viewAngle.y );
    
    //False: "gloabal"
    //True: "local"
    public boolean rotation_mode = true;
    public LinkedList<LinkedList<Point3D>> frames = new LinkedList<>();
    
    public float tickDelta = 0F;
    
    public boolean an_pause = false;
    
    public final Graph grapher = new Graph();
    
    public Point3D camera = new Point3D(0, 0, 0);
    
    public ConcurrentHashMap<Integer, model> models = new ConcurrentHashMap<>();
    public int addCube(dVector3 center, double size, boolean Addlines, boolean addFaces) throws IOException{
        LinkedList<LinkedList<Point3D>> frames2 = new modelParser("Cube").parse();
        LinkedList<LinkedList<Point3D>> ref = (LinkedList<LinkedList<Point3D>>) frames.clone();
        int iz = 0;
        for(LinkedList<Point3D> i: frames){
            LinkedList<Point3D> i2 = new LinkedList<>();
            for(Point3D d : i){
                int id = d.identifier;
                d =  dVector3.multiply(d, new dVector3(size, size, size));
                d =  dVector3.add(d, center);
                d.identifier = id;
                i2.add(d);
            }
            ref.add(i2);
            iz++;
        }
        LinkedList<Point3D> points2 = frames2.getFirst();
        LinkedList<Integer[]> lines2 = new modelParser("Cube").parseLines(points2);
        LinkedList<Point2D[]> faces2 = new modelParser("Cube").parseFaces(points2);
        
        model m = new model(new LinkedList<model_frame>(), true);
        points2.forEach(l -> m.frames.getFirst().points.add(l));
        int id = points2.hashCode();
        models.put(id, m);
        
        //this.points.addAll(points2);
        //this.lines.addAll(lines2);
        //this.faces.addAll(faces2);
        
        //points = points2;
        //lines = lines2;
        //faces = faces2;
        
        //frames = frames2;
        //this.frames = ref;
        return id;
    }
    /**
     *
     * @param center
     * @param size
     * @param Addlines
     * @param addFaces
     * @return The handle of the added object
     */
    public int addCubeOLD(dVector3 center, double size, boolean Addlines, boolean addFaces){
        double s = size;
        //zxy
        dVector3 dlu = new dVector3(center.x +s, center.y +s, center.z -s);
        dVector3 dld = new dVector3(center.x +s, center.y -s, center.z -s);
        dVector3 dru = new dVector3(center.x -s, center.y +s, center.z -s);
        dVector3 drd = new dVector3(center.x -s, center.y -s, center.z -s);
        dVector3 ulu = new dVector3(center.x +s, center.y +s, center.z +s);
        dVector3 uld = new dVector3(center.x +s, center.y -s, center.z +s);
        dVector3 uru = new dVector3(center.x -s, center.y +s, center.z +s);
        dVector3 urd = new dVector3(center.x -s, center.y -s, center.z +s);
        points.add(dlu);
        points.add(dld);
        points.add(dru);
        points.add(drd);
        points.add(ulu);
        points.add(uld);
        points.add(uru);
        points.add(urd);
        if (Addlines) {
            lines.add(new Integer[]{dlu.identifier, dld.identifier});
            lines.add(new Integer[]{dlu.identifier, dru.identifier});
            lines.add(new Integer[]{dld.identifier, drd.identifier});
            lines.add(new Integer[]{dru.identifier, drd.identifier});
            
            lines.add(new Integer[]{ulu.identifier, uld.identifier});
            lines.add(new Integer[]{ulu.identifier, uru.identifier});
            lines.add(new Integer[]{uld.identifier, urd.identifier});
            lines.add(new Integer[]{uru.identifier, urd.identifier});
            
            lines.add(new Integer[]{dlu.identifier, ulu.identifier});
            lines.add(new Integer[]{dld.identifier, uld.identifier});
            lines.add(new Integer[]{dru.identifier, uru.identifier});
            lines.add(new Integer[]{drd.identifier, urd.identifier});
        }
        if(addFaces){
            faces.add(new Point2D[]{vToP2(dlu), vToP2(dld), vToP2(dru)});
            faces.add(new Point2D[]{vToP2(dld), vToP2(drd), vToP2(dru)});
            faces.add(new Point2D[]{vToP2(ulu), vToP2(uld), vToP2(uru)});
            faces.add(new Point2D[]{vToP2(uld), vToP2(urd), vToP2(uru)});
            
            faces.add(new Point2D[]{vToP2(dlu), vToP2(dld), vToP2(ulu)});
            faces.add(new Point2D[]{vToP2(ulu), vToP2(uld), vToP2(dld)});
            faces.add(new Point2D[]{vToP2(dru), vToP2(drd), vToP2(uru)});
            faces.add(new Point2D[]{vToP2(uru), vToP2(urd), vToP2(drd)});
            
            faces.add(new Point2D[]{vToP2(dlu), vToP2(dru), vToP2(dru)});
            
            //faces.add(new Integer[]{urd), drd), dru)});
            
            //faces.add(new Integer[]{ulu), uld), uru)});
            //faces.add(new Integer[]{uld), urd), uru)});
            //faces.add(new Integer[]{ulu), dld), dru)});
            //faces.add(new Integer[]{uld), drd), dru)});
            
            //faces.add(new Integer[]{dld), drd), drd)});
        }
        //CONTINUE FROM HERE, ADD THE CUBE TO THE MODELS MAP
        return 0;
    }
    public void addCube(dVector3 center, double size) throws IOException{
        addCube(center, size, true, true);
    }
    public LinkedList<Point3D> points;
    public LinkedList<Integer[]> lines;
    public LinkedList<Point2D[]> faces = new LinkedList<>();
    
    IDManager ids = new IDManager();
    
    LinkedList<Float> face_dists = new LinkedList<>();
    LinkedList<Color> faces_color = new LinkedList<>();
    public Screen s;
    public Input inp;
    public driver(Supervisor sudo){
        pbSudo = sudo;
        if(Objects.isNull(pbSudo)){
            usePB = false;
        }
        usePB = false;
        //dVector3 point = new dVector3(0, 0, 0);
        InputActivated refI = new InputActivated();
        s = new Screen();
        inp = new Input(refI);
        inp.verbodose = false;
        s.addKeyListener(inp);
        s.addMouseListener(inp);
        
        
    }
    
    public boolean running = false;
    public boolean init = false;
    
    public LinkedList<Integer> ingoredInputs = new LinkedList<>();
    
    public void loadFrame(int f){
        //LinkedList<LinkedList<Point3D>> frames2 = new LinkedList<>();
        LinkedList<Point3D> points2 = new LinkedList<>();
        LinkedList<Integer[]> lines2 = new LinkedList<>();
        LinkedList<Point2D[]> faces2 = new LinkedList<>();
        for(model m : models.values()){
            points2.addAll(m.getFrame(f).points);
            lines2.addAll(m.getFrame(f).lines);
            faces2.addAll(m.getFrame(f).faces);
        }
        this.points = points2;
        this.lines = lines2;
        this.faces = faces2;
    }
    
    public void run(){
        points = new LinkedList<>();
        lines = new LinkedList<>();
        try {
            //addCube(new dVector3(0, 0, 0), 0.5);
            //File err = new File("err.txt");
            //throw new Exception();
            LinkedList<LinkedList<Point3D>> frames2 = new modelParser().parse();
            LinkedList<Point3D> points2 = frames2.getFirst();
            LinkedList<Integer[]> lines2 = new modelParser().parseLines(points2);
            LinkedList<Point2D[]> faces2 = new modelParser().parseFaces(points2);
            model m = new model(new LinkedList<model_frame>(), false);
            for(LinkedList<Point3D> list : frames2){
                m.frames.add(new model_frame(list, lines2, faces2));
            }
            models.put(m.hashCode(), m);
            
        } catch (Exception ex) {
            ex.printStackTrace();
            
            int r = 4;
            int r2 = 4;
            int r3 = 4;
            for (int i : new Range(r)) {
                for (int j : new Range(r2)) {
                    for (int z : new Range(r3)) {
                        try {
                            addCube(new dVector3(i, j, z), 0.5, true, true);
                            //addCube(new dVector3(i, j, z), 0.5, true, true);
                            //addCube(new dVector3(i, j, z), 0.5, true, true);
                        } catch (IOException ex1) {
                            Logger.getLogger(driver.class.getName()).log(Level.SEVERE, null, ex1);
                        }
                    }
                }
            }
            frames.add(points);
        }
        
        
        
        double angleY = 0;
        double angleYM = 0;
        double angleX = 0;
        double angleXM = 0;
        int sleep = 0;
        
        //Graph grapher = new Graph();
        int tickC = 0;
        
        int frame = 0;
        running = true;
        while(running){
            //frame = 10;
            beginTime = Instant.now();
            //Init
            
            s.r.cx = screenPosition_org.intX();
            s.r.cy = screenPosition_org.intY();
            s.r.cz = screenPosition_org.intZ();
            
            if(usePB){
                points = new LinkedList<>();
                lines = new LinkedList<>();
                faces = new LinkedList<>();
                
                //pbSudo.objectManager.getObjects().forEach(l -> addCube(new dVector3(l.x, l.y, 1), 0.5, true, false));
            }
            
            loadFrame(frame);
            int step = 1;
            
            int zep = 1;
            
            try {
               //zep = (int) (24F * (deltaTime.getNano() *0.000000001F + 1F ));
            } catch (Exception e) {
            }
            
            if(zep == 0){
                zep = 1;
            }
            if (!usePB) {
//System.out.println(zep);
                if (tickC % 15 == 0 && !an_pause) {
                    if (frame < frames.size() - 1) {
                        frame++;
                    } else {
                        //System.out.println("frame was " + frame + " before reset");
                        frame = 0;
                    }
                    
                    //points = frames.get(frame);
                    
                }
                s.r.frame = frame;
                try {
                    //points = frames.get(frame);
                } catch (Exception e) {
                }
            }
            
            LinkedList<Point2D> set = new LinkedList<>();
            LinkedList<Point2D> sizes = new LinkedList<>();
            LinkedList<Point2D[]> lines_set = new LinkedList<>();
            LinkedList<Point2D[]> lines_sizes = new LinkedList<>();
            LinkedList<Color> lines_color = new LinkedList<>();
            HashMap<Integer, Float> dist = new HashMap<>();
            lines_color = new LinkedList<>();
            //faces_color = new LinkedList<>();
            
            HashMap<Integer, Point2D> idVSserial = s.r.getIDMap();
            
            
            CT = Math.cos( DEG_TO_RAD * viewAngle.x );//CT=0;
            ST = Math.sin( DEG_TO_RAD * viewAngle.x );//ST=0;
            CP = Math.cos( DEG_TO_RAD * viewAngle.y );//CP=0;
            SP = Math.sin( DEG_TO_RAD * viewAngle.y );//SP=0;
            
            xScreenCenter = s.r.w / 2;
            yScreenCenter = s.r.h / 2;
            
            screenPosition = screenPosition_org.clone();
            
            if(!rotation_mode){
                screenPosition = matmul(RX((float) -angleY), screenPosition.toFVector3()).toDVector3();
                screenPosition = matmul(RY((float) -angleX), screenPosition.clone().toFVector3()).toDVector3();
                //screenPosition = JFUtils.point.Point3F.multiply(screenPosition.toFVector3(), matmul(RY((float) -angleX), screenPosition_org.clone().toFVector3())).toDVector3();
                //screenPosition = JFUtils.math.General.average(screenPosition, matmul(RY((float) -angleX), screenPosition_org.toFVector3()).toDVector3(), screenPosition.identifier);
            }
            
            //Check input   -0.025D*0.05
            double factor_rotation = -0.025D*0.05 * deltaTime.getNano();
            double factor = -0.025D*0.05*4 * deltaTime.getNano() * 0.000001;
            double boost = 1 * deltaTime.getNano() * 0.000002;
            //space
            if(inp.keys[32] == true){
                //viewAngle.y += factor*15;
                factor = factor * 7;
                factor_rotation = factor_rotation * 15;
                boost = boost * 5;
                
            }
            
            //t
            if(inp.keys[84] == true && !ingoredInputs.contains(84)){
                s.r.drawLines = true;
                s.r.drawFaces = false;
            }
            //g
            if(inp.keys[71] == true && !ingoredInputs.contains(71)){
                s.r.drawLines = false;
                s.r.drawFaces = true;
            }
            //b
            if(inp.keys[66] == true && !ingoredInputs.contains(66)){
                s.r.drawLines = false;
                s.r.drawFaces = false;
            }
            
            //d
            if(inp.keys[68] == true && !ingoredInputs.contains(68)){
                screenPosition_org.x = screenPosition_org.x + factor;
            }
            //a
            if(inp.keys[65] == true && !ingoredInputs.contains(65)){
                screenPosition_org.x = screenPosition_org.x - factor;
            }
            //' tai *
            if(inp.keys[222] == true){
                //w
                if(inp.keys[87] == true && !ingoredInputs.contains(87)){
                    //screenPosition_org.y = screenPosition_org.y + factor;
                    screenPosition_org = Point3D.add(Point3D.multiply(viewAngle, new Point3D(0.0001D, 0.0001D, 0.0001D)), screenPosition_org);
                    //screenPosition_org = JFUtils.vector.dVector3.add(screenPosition_org, screenPosition_org);
                }
            }
            else{
                //w
                if(inp.keys[87] == true  && !ingoredInputs.contains(87)){
                    screenPosition_org.y = screenPosition_org.y + factor;
                }
            }
            //s
            if(inp.keys[83] == true && !ingoredInputs.contains(83)){
                screenPosition_org.y = screenPosition_org.y - factor;
            }
            //q
            if(inp.keys[81] == true && !ingoredInputs.contains(81)){
                screenPosition_org.z = screenPosition_org.z - factor*5;
            }
            //e
            if(inp.keys[69] == true && !ingoredInputs.contains(69)){
                screenPosition_org.z = screenPosition_org.z + factor*5;
            }
            //c
            if(inp.keys[67] == true && !ingoredInputs.contains(67)){
                viewAngle.z += factor_rotation*15;
            }
            //z
            if(inp.keys[90] == true && !ingoredInputs.contains(90)){
                viewAngle.z -= factor_rotation*15;
            }
            
            //z
            if(inp.keys[88] == true && !ingoredInputs.contains(88)){
                //viewAngle.y -= factor*15;
            }
            
            //n
            if(inp.keys[78] == true && !ingoredInputs.contains(78)){
                System.out.print("Saving face lists to file...");
                listToFile(s.r.faces);
                listToFile(s.r.faces_unsorted);
                System.out.println("Done!");
            }
            
            //j
            if(inp.keys[74] == true && !ingoredInputs.contains(74)){
                angleXM = angleXM - 0.0004D * 0.3 * boost;
            }
            //l
            if(inp.keys[76] == true && !ingoredInputs.contains(76)){
                angleXM = angleXM + 0.0004D * 0.3 * boost;
            }
            //i
            if(inp.keys[73] == true && !ingoredInputs.contains(73)){
                angleYM = angleYM + 0.0004D * 0.3 * boost;
            }
            //k
            if(inp.keys[75] == true && !ingoredInputs.contains(75)){
                angleYM = angleYM - 0.0004D * 0.3 * boost;
            }
            //p
            if(inp.keys[80] == true && !ingoredInputs.contains(80)){
                an_pause = !an_pause;
            }
            if(inp.keys[86] == true && !ingoredInputs.contains(86)){
                inp.verbodose = !inp.verbodose;
            }
            //R
            if(inp.keys[82] == true && !ingoredInputs.contains(82)){
                rotation = !rotation;
            }
            //1
            if(inp.keys[49] == true && !ingoredInputs.contains(49)){
                rotation_mode = false;
            }
            //2
            if(inp.keys[50] == true && !ingoredInputs.contains(50)){
                rotation_mode = true;
            }
            else{
            }
            s.r.speed = (float) factor;
            
            //screenPosition.z = screenPosition_org.z;
            //Calc
            LinkedList<Point3D> points_bak = new LinkedList<>();
            try {
                points_bak.addAll(points);
            } catch (Exception e) {
                try {
                    points.forEach(l -> {
                        dVector3 d = new dVector3(l.x, l.y, l.z);
                        d.identifier = l.identifier;
                        points_bak.add(d);
                    });
                } catch (Exception ex) {
                    //points_bak = points;
                }
            }
            for(Point3D i : points_bak){
                if(i.z > screenPosition.z){
                    //continue;
                }
                //System.out.println("Original[" +i.hashCode() + "] :" + i);
                Point3F rotated = i.toFVector3();
                
                if(rotation){
                    
                }
                
                if(rotation){
                    Point3F rotated_org = rotated.clone();
                    rotated = matmul(RX((float) angleY ), rotated);
                    //rotated.z = rotated.z - rotated.x;
                    rotated = matmul(RY((float) angleX ), rotated);
                    //rotated = JFUtils.point.Point3F.multiply(rotated, matmul(RY((float) angleX ), rotated_org));
                    //rotated = JFUtils.math.General.average(rotated.toDVector3(), matmul(RY((float) angleX ), rotated_org).toDVector3(), rotated.identifier).toFVector3();
                }
                /*rotated = matmul(RX((float) 0 ), rotated);
                rotated = matmul(RX((float) 0 ), rotated);
                rotated = matmul(RX((float) 0 ), rotated);*/
                //rotated.z = (float) i.z;
                //fVector3 rotated = i.toFVector3();
                //rotated = matmul(RZ(0), rotated);
                //rotated = matmul(RY(-0.06F), rotated);
                
                /*float dist = 0.005F;
                float z = 1;
                if(dist - rotated.z != 0){
                z = 1 / (dist - rotated.z);
                }
                float[][] projection = {
                {z, 0, 0},
                {0, z, 0}
                };
                
                fVector3 projected = null;
                try {
                projected = matmul(projection, rotated);
                } catch (Exception e) {
                }*/
                //projected = fVector3.multiply(projected, new fVector3(20, 20, 20));
                //projected = fVector3.add(projected, new fVector3(s.r.w/2, s.r.h/2, 0));
                Point3F projected = new Point3F(0, 0, 0);
                /*float[][] projection = {
                {z, 0, 0},
                {0, z, 0}
                };
                dVector3 projected = new dVector3(0, 0, 0);*/
                projectPoint(rotated, projected);
                /*projected = matmul(projection, rotated).toDVector3();
                projected = dVector3.multiply(projected, new dVector3(200, 200, 200));
                projected = dVector3.add(projected, new dVector3(200, 200, 20));*/
                Point2D point2D = new Point2D(projected.x, projected.y);
                point2D.identifier = i.identifier;
                int size = (int) (25 - (screenPosition.z - rotated.z) * 2);
                float distP;
                //distP = (float) (screenPosition.z - rotated.z);
                distP = (float) Utils.getDistance(rotated.toDVector3(), screenPosition);
                dist.put(i.identifier, distP);
                if(size < 0){
                    size = 0;
                }
                //System.out.println("Projected: " + point2);
                if(rotated.z < screenPosition.z){
                    sizes.add(new Point2D(size, size));
                    set.add(point2D);
                }
            }
            /*for(Integer[] l : lines){
            //Every point must exist in both lists
            try {
            dVector new_start = set.get(points.indexOf(l[0]));
            dVector new_end = set.get(points.indexOf(l[1]));
            lines_set.add( new dVector[]{new_start, new_end} );
            lines_sizes.add(new dVector[]{ sizes.get(points.indexOf(l[0])) , sizes.get(points.indexOf(l[1]))} );
            } catch (Exception e) {
            e.printStackTrace();
            }
            }*/
            LinkedList<Integer[]> lines2 = new LinkedList<>();
            try {
                lines2.addAll(lines);
            } catch (Exception e) {
                lines.forEach(l -> {lines2.add(l);});
            }
            for(Integer[] line : lines2){
                Point2D point = null;
                try {
                    point = idVSserial.get(line[0]);
                } catch (Exception e) {
                    try {
                        point = idVSserial.get(line[1]);
                    } catch (Exception ez) {
                        throw ez;
                        //lines_color.add(Color.pink);
                    }
                }
                
                if(!Objects.isNull(point)){
                    int distP = (int) (float)(255 - dist.get(point.identifier) * 7);
                    
                    //System.out.println(distP);
                    if(distP > 255){
                        distP = 255;
                    }
                    if(distP < 0){
                        distP = 0;
                    }
                    lines_color.add(new Color(distP, distP, distP));
                }
            }
            
            
            //face_dists = new LinkedList<>();
            //faces_color = new LinkedList<>();
            float lastZ = 0;
            int index = 0;
            final Color def = new Color(0, 0, 0);
            if(face_dists.isEmpty() && !faces.isEmpty()){
                System.out.println("FACES_DIST SIZE: " + face_dists.size());
                System.out.println("List too small, inflating...");
                faces.forEach(l -> face_dists.add(0F));
            }
            if(faces_color.isEmpty() && !faces.isEmpty()){
                System.out.println("FACES_COLOR SIZE: " + faces_color.size());
                System.out.println("List too small, inflating...");
                for(Point2D[] face : faces){
                    faces_color.add(new Color(0, 0, 0));
                }
            }
            for(Point2D[] face : faces){
                boolean cont = false;
                if(face.length == 3){
                    cont = true;
                }
                else{
                    //faces.remove(face);
                }
                
                if(cont){
                    //continue;
                }
                
                Point2D point = null;
                try {
                    point = face[0];
                } catch (Exception e) {
                    try {
                        point = face[1];
                    } catch (Exception ez) {
                        //throw ez;
                        try {
                            point = face[2];
                        } catch (Exception ezz) {
                            //throw ez;
                            
                        }
                    }
                }
                
                
                //System.out.println(face_dists.size());
                if(!Objects.isNull(point)){
                    boolean change = true;
                    float distP = (255 - dist.get(point.identifier) * 7);
                    if(distP == 0.0F){
                        //System.out.println("F2");
                        change = false;
                    }
                    else{
                        lastZ = distP;
                        //System.out.println("F3");
                    }
                    //face_dists.add((float)distP);
                    
                    if(Objects.isNull(face_dists.get(index))){
                        change = false;
                        //face_dists.add(index, lastZ);
                        //System.out.println("List too small, inflating...");
                    }
                    
                    //System.out.println(distP);
                    if(distP > 255){
                        distP = 255;
                    }
                    if(distP < 0){
                        distP = 0;
                    }
                    if(change){
                        face_dists.set(index, lastZ);
                        faces_color.set(index, new Color((int)distP, (int)distP,(int) distP));
                    }
                    //faces_color.add(Color.pink);
                }
                else{
                    //System.out.println("F:");
                    //face_dists.add(lastZ);
                    //face_dists.add(Float.MAX_VALUE);
                    //System.out.println(lastZ);
                    //faces_color.add(Color.green);
                }
                index++;
            }
            
            //Rendering
            s.r.updatePoints(set, sizes);
            s.r.updateLines(lines, lines_color);
            
            
            //Sort faces
            LinkedList<face> sorted = new Utils().constructFaceList(faces, face_dists);
            Collections.sort(sorted);
            
            s.r.updateFaces(sorted, faces_color, face_dists);
            //System.out.println("orighinal: ");
            //System.out.println("projected: " + point2);
            
            angleYM = angleYM * 0.95D;
            angleY = (float) (angleY + angleYM);
            angleXM = angleXM * 0.95D;
            angleX = (float) (angleX + angleXM);
            deltaTime = Duration.between(beginTime, Instant.now());
            s.r.nano = JFUtils.math.Conversions.toCPNS(deltaTime.getNano());
            final int d = (int) JFUtils.math.Conversions.toFPS(deltaTime.getNano());
            final int tc = tickC;
            /*new Thread(){
                @Override
                public void run() {
                    super.run(); //To change body of generated methods, choose Tools | Templates.
                    grapher.update(d, tc);
                }
                
            }.start();*/
            int value = (int) (JFUtils.math.Conversions.toFPS(deltaTime.getNano()));
            if(value < 0) {
                value = 0;
            }
            //System.out.println(value);
            try {
                //grapher.update(value, tickC);
            } catch (Exception e) {
                System.out.println(e);
            }
            tickC++;
            tickDelta = tickDelta + (1 * deltaTime.getNano());
            //System.out.println(deltaTime.getNano() + " nano passed");
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException ex) {
                Logger.getLogger(CodeNameProjection.class.getName()).log(Level.SEVERE, null, ex);
            }
            //point.x = point.x + 0.1;
            //point.z--;
            //points.get(0).x = points.get(0).x + 0.1;
            //points.get(0).z--;
        }
    }
    
    public synchronized void zero(){
        this.frames = new LinkedList<>();
        //frames.add(points);
        this.points = new LinkedList<>();
        this.lines = new LinkedList<>();
        this.faces = new LinkedList<>();
    }
    
    public void projectPoint( Point3F input, Point3F output )
    {
        float sx = (float) screenPosition.x;
        float sy = (float) screenPosition.y;
     float x = (float) (sx + input.x * CT - input.y * ST);
     float y = (float) (sy + input.x * ST * SP + input.y * CT * SP
             + input.z * CP);
     float temp = (float) (viewAngle.z / (screenPosition.z + input.x * ST * CP
             + input.y * CT * CP - input.z * SP ));
    
     //x= ((input.x - sx) * (100/input.z)) + sx;
     //y = ((input.y - sy) * (100/input.z)) + sy;
     //temp = 10;
     
     float F = (float) (input.z-screenPosition.z * 0.2);
     
     //x = (input.x - sx) * (F/input.z) + sx;
     //y = (input.y - sy) * (F/input.z) + sy;
     
     output.x = (float) (xScreenCenter + modelScale * temp * x);
     output.y = (float) (yScreenCenter - modelScale * temp * y);
     
     //output.x = ((input.x - sx) * (100/input.z)) + sx;
     //output.y = ((input.y - sy) * (100/input.z)) + sy;
     output.z = 0;
    }
    public static float[][] RX (float o){
        float d = (float) o;
        float c = (float) Math.cos(d);
        float s = (float) Math.sin(d);
        float ns = (float) -Math.sin(d);
        return new float[][]{
            new float[]{1, 0, 0},
            new float[]{0, c, ns},
            new float[]{0, s, c}
        };
    }
    public static float[][] RY (float o){
        float d = (float) o;
        float c = (float) Math.cos(d);
        float s = (float) Math.sin(d);
        float ns = (float) -Math.sin(d);
        return new float[][]{
            new float[]{c, 0, s},
            new float[]{0, 1, 0},
            new float[]{ns, 0, c}
        };
    }
    public static float[][] RZ (float o){
        float d = (float) o;
        float c = (float) Math.cos(d);
        float s = (float) Math.sin(d);
        float ns = (float) -Math.sin(d);
        return new float[][]{
            new float[]{c, ns, 0},
            new float[]{s, c, 0},
            new float[]{0, 0, 1}
        };
    }
    
    //The following is copied (edited to suit JFTools) from Daniel Shiffmans code, at: https://github.com/CodingTrain/website/blob/master/CodingChallenges/CC_112_3D_Rendering/Processing/CC_112_3D_Rendering/matrix.pde#L50
    //Why? becouse i do not know how multiplication matricies work! :P
    float[][] vecToMatrix(Point3F v) {
        float[][] m = new float[3][1];
        m[0][0] = (float) v.x;
        m[1][0] = (float) v.y;
        m[2][0] = (float) v.z;
        return m;
      }

    Point3F matrixToVec(float[][] m) {
        Point3F v = new Point3F(0,0,0);
        v.x = m[0][0];
        v.y = m[1][0];
        if (m.length > 2) {
          v.z = m[2][0];
        }
        return v;
      }
    Point3F matmul(float[][] a, Point3F b) {
        float[][] m = vecToMatrix(b);
        return matrixToVec(matmul(a,m));
    }
    float[][] matmul(float[][] a, float[][] b) {
        int colsA = a[0].length;
        int rowsA = a.length;
        int colsB = b[0].length;
        int rowsB = b.length;

        if (colsA != rowsB) {
          throw new Error("Columns of A must match rows of B");
        }

        float result[][] = new float[rowsA][colsB];

        for (int i = 0; i < rowsA; i++) {
          for (int j = 0; j < colsB; j++) {
            float sum = 0;
            for (int k = 0; k < colsA; k++) {
              sum += a[i][k] * b[k][j];
            }
            result[i][j] = sum;
          }
        }
        return result;
    }
    void listToFile(List l){
        String filename = l.toString().hashCode() + "";
        String tmp = "";
        for(int i : new Range(l.size())){
            tmp = tmp + l.get(i) + "\n";
        }
        
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "utf-8"))) {
            writer.write(tmp);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(driver.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(driver.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(driver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
/*class driver{
public double x = 20;
public double y = 20;
public double z = 0;
public driver(){
// TODO code application logic here
// convert to screen space
Screen screen = new Screen();
dVector P_screen = new dVector(0, 0);
dVector3 P_camera = new dVector3(0, 0, 20);
double near = -2;
double r = 100; //Right side of the screen
double l = 0; //Left side of the screen
double t = 0; //Top of the screen
double b = 100; //Bottom of the screen







//Lets do this!!!

int sleep = 200;

while(true){
//Init
LinkedList<dVector> set = new LinkedList<>();
dVector3 newVertex = new dVector3(x, y, z);
System.out.println("Original: "+newVertex);

//Translate
newVertex.x /= (newVertex.z + 100D) * 0.01D;
newVertex.y /= (newVertex.z + 100D) * 0.01D;
System.out.println("Translated: "+newVertex);
//Convert to pixels
newVertex.x *= (screen.r.w/80D);
newVertex.y *= (screen.r.h/80D);
System.out.println("Pixelated: "+newVertex);
//Zero-ify
newVertex.x += screen.r.w/2D;
newVertex.y += screen.r.h/2D;
System.out.println("Zero'd: "+newVertex);
//test?
System.out.println("Final: "+newVertex);

//Render
set.add(new dVector(x, y));
screen.r.updatePoints(set);
try {
Thread.sleep(sleep);
} catch (InterruptedException ex) {
Logger.getLogger(CodeNameProjection.class.getName()).log(Level.SEVERE, null, ex);
}
x = x + 1;
}


}
}*/

