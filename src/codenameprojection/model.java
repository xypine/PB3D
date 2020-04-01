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

package codenameprojection;

import JFUtils.Range;
import JFUtils.point.Point3D;
import codenameprojection.drawables.vertexGroup;
import java.util.LinkedList;

/**
 *
 * @author Jonnelafin
 */
public class model {
    public LinkedList<model_frame> frames;
    public double rotation_X = 0;
    public double rotation_Y = 0;
    public double rotation_Z = 0;
    
     double x = 0;
    public void setX(double x){
        this.x = x;
    }
    public double getX(){
        return this.x;
    }
     double y = 0;
    public void setY(double y){
        this.y = y;
    }
    public double getY(){
        return this.y;
    }
     double z = 0;
    public void setZ(double z){
        this.z = z;
    }
    public double getZ(){
        return this.z;
    }
    
    public boolean hidePoints = true;
    public boolean hideLines = false;
    public boolean hideFaces = false;
    
    boolean single_frame;
    public model(LinkedList<model_frame> frames, boolean singleFrame) {
        this.frames = frames;
        single_frame = singleFrame;
    }
    
    public LinkedList<model_frame> frames_cache = new LinkedList<>();
    
    public driver parent;
    
    boolean buffering = false;
    
    int latest = 0;
    
    synchronized void updatecache(){
        buffering = true;
        int token = latest + 1;
        LinkedList<model_frame> newCache = new LinkedList<>();
        for(int i : new Range(frames.size())){
            newCache.add(getFrame(i, true, true, true));
        }
//        if(token > latest){
          frames_cache = newCache;
//        }
        buffering = false;
    }
    
    synchronized public model_frame getFrame(int index){
        return getFrame(index, false, true, true);
    }
    
    synchronized public model_frame getFrame(int index, boolean rotate, boolean translate){
        return getFrame(index, false, rotate, translate);
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
    
    synchronized public model_frame getFrame(int index, boolean skipCache, boolean rotate, boolean translate){
        try {
            index = index % (frames.size() - 1);
        } catch (Exception e) {
        }
        if(index > frames.size()-1){
            index = frames.size()-1;
        }
        if(index < 0){
            index = 0;
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
        model_frame out;
        if(!single_frame){
//            System.out.println(index);
            out = (model_frame) frames.get(index).clone();
        }
        else{
            out = (model_frame) frames.getFirst().clone();
        }
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
            Point3D global = new Point3D(x, y, z);
            if (translate) {
                i2 = Point3D.add(global, i2);
            }
            i2.identifier = oldID;
            out.points.set(ind, i2);
            ind++;
        }
        if(hideLines){
            out.lines = new LinkedList<>();
        }
        if(hideFaces){
            out.faces = new LinkedList<>();
        }
        return out;
    }

    @Override
    protected Object clone() {
        return new model(frames, single_frame);
    }
    
}
