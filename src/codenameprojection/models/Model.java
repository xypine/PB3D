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

package codenameprojection.models;

import JFUtils.Range;
import JFUtils.point.Point3D;
import codenameprojection.drawables.vertexGroup;
import codenameprojection.driver;
import java.util.LinkedList;

/**
 *
 * @author Jonnelafin
 */
public class Model {
    public LinkedList<ModelFrame> frames;
    public double rotation_X = 0;
    public double rotation_Y = 0;
    public double rotation_Z = 0;
    
    /**
     *  Makes the model ignore the 0th point when returning faces, can reduce face "flickering"
     */
    public boolean ignoreRootNode = false;
    
    /**
     * Points with a lower index will be considered rootnodes
     */
    public int ignoreRootNodeThreshold = 1;
    
    public double scale = 1;
    
    
     double x = 0;
    public synchronized void setX(double x){
        this.x = x;
    }
    public double getX(){
        return this.x;
    }
     double y = 0;
    public synchronized void setY(double y){
        this.y = y;
    }
    public double getY(){
        return this.y;
    }
     double z = 0;
    public synchronized void setZ(double z){
        this.z = z;
    }
    public double getZ(){
        return this.z;
    }
    
    public boolean hidePoints = true;
    public boolean hideLines = false;
    public boolean hideFaces = false;
    
    public int minFrame = 0;
    
    public boolean single_frame;
    public Model(LinkedList<ModelFrame> frames, boolean singleFrame) {
        this.frames = frames;
        single_frame = singleFrame;
    }
    
    public LinkedList<ModelFrame> frames_cache = new LinkedList<>();
    
    public driver parent;
    
    boolean buffering = false;
    
    int latest = 0;
    
    synchronized void updatecache(){
        buffering = true;
        int token = latest + 1;
        LinkedList<ModelFrame> newCache = new LinkedList<>();
        for(int i : new Range(frames.size())){
            newCache.add(getFrame(i, true, true, true, true));
        }
//        if(token > latest){
          frames_cache = newCache;
//        }
        latest = token;
        buffering = false;
    }
    
    synchronized public ModelFrame getFrame(int index){
        return getFrame(index, false, true, true, true);
    }
    
    synchronized public ModelFrame getFrame(int index, boolean rotate, boolean translate, boolean scale){
        return getFrame(index, false, rotate, translate, scale);
    }
    
    public String name = "unnamed_model";
    
    public void executeLogic(driver parent_){
        this.parent = parent_;
        //Please replace in order to execute custom logic
    }
    
    public LinkedList<Integer> getByColor(float r, float g, float b){
        LinkedList<Integer> out = new LinkedList<>();
        for(vertexGroup i : frames.getFirst().color){
            if(i.r == r && i.g == g && i.b == b){
                out.add(i.vertexID);
            }
        }
        return out;
    }
    public double animationSpeed = 1;
    synchronized public ModelFrame getFrame(int index, boolean skipCache, boolean rotate, boolean translate, boolean scale){
        index = (int) (index * animationSpeed);
        try {
            index = index % (frames.size() - 1);
        } catch (Exception e) {
        }
        if(index > frames.size()-1){
            index = frames.size()-1;
        }
        if(index < minFrame){
            index = minFrame;
        }
        if(frames.hashCode() == frames_cache.hashCode() && !skipCache){
        //if(false){
            return frames_cache.get(index);
        }
        else if(!skipCache){
            if(!buffering){
                updatecache();
            }
            return frames_cache.get(index);
        }
        ModelFrame out;
        if(!single_frame){
//            System.out.println(index);
            out = (ModelFrame) frames.get(index).clone();
        }
        else{
            out = (ModelFrame) frames.get(minFrame).clone();
        }
        
        
        LinkedList<Point3D> rootnodes = new LinkedList<>(); //LEFT HERE
        Point3D rootNode = out.points.getFirst();
        int ind = 0;
        for(Point3D i : (LinkedList<Point3D>)(out.points.clone())){
            int oldID = i.identifier;
            Point3D i2 = i.clone();
            //i2 = i;
            if(rotate){
                i2 = driver.matmul(driver.RY((float) rotation_Y), i2.toFVector3()).toDVector3();
                //i = driver.matmul(driver.RZ((float) rotation_Z), i.toFVector3()).toDVector3();
                i2 = driver.matmul(driver.RX((float) rotation_X), i2.toFVector3()).toDVector3();
            }
            if(scale){
                i2 = Point3D.multiply(i2, new Point3D(this.scale,this.scale, this.scale));
            }
            Point3D global = new Point3D(x, y, z);
            if (translate) {
                i2 = Point3D.add(global, i2);
            }
            i2.identifier = oldID;
            out.points.set(ind, i2);
            ind++;
        }
        if(ignoreRootNode){
            out.points.removeFirst();
        }
        LinkedList<Point3D[]> faces_out = new LinkedList<>();
        out.faces.forEach(l -> {
            boolean add = true;
            for(Point3D i : l){
                if(i.identifier == rootNode.identifier){
                    add = false;
                }
            }
            if(add || !ignoreRootNode){
                faces_out.add(l);
            }
        });
        out.faces = faces_out;
        if(hideLines){
            out.lines = new LinkedList<>();
        }
        if(hideFaces){
            out.faces = new LinkedList<>();
        }
        return out;
    }

    @Override
    public Model clone() {
        Model m = new Model(frames, single_frame);
        m.animationSpeed = this.animationSpeed;
        m.x = x;
        m.y = y;
        m.z = z;
        return m;
    }
    
    public Point3D getLoc(){
        return new Point3D(x, y, z);
    }
    
}
