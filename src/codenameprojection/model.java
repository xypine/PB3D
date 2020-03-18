/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package codenameprojection;

import JFUtils.Range;
import JFUtils.point.Point3D;
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
    
    public double x = 0;
    public double y = 0;
    public double z = 0;
    
    boolean single_frame;
    public model(LinkedList<model_frame> frames, boolean singleFrame) {
        this.frames = frames;
        single_frame = singleFrame;
    }
    
    public LinkedList<model_frame> frames_cache = new LinkedList<>();
    
    void updatecache(){
        LinkedList<model_frame> newCache = new LinkedList<>();
        for(int i : new Range(frames.size())){
            newCache.add(getFrame(i, true));
        }
        frames_cache = newCache;
    }
    
    public model_frame getFrame(int index){
        return getFrame(index, false);
    }
    
    public String name = "unnamed_model";
    
    public void executeLogic(){
        //Please replace in order to execute custom logic
    }
    
    public model_frame getFrame(int index, boolean skipCache){
        if(frames.hashCode() == frames_cache.hashCode() && !skipCache){
        //if(false){
            return frames_cache.get(index);
        }
        else if(!skipCache){
            updatecache();
            return frames_cache.get(index);
        }
        model_frame out;
        if(!single_frame){
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
            i2 = driver.matmul(driver.RY((float) rotation_Y), i2.toFVector3()).toDVector3();
            //i = driver.matmul(driver.RZ((float) rotation_Z), i.toFVector3()).toDVector3();
            i2 = driver.matmul(driver.RX((float) rotation_X), i2.toFVector3()).toDVector3();
            
            Point3D global = new Point3D(x, y, z);
            i2 = Point3D.add(global, i2);
            i2.identifier = oldID;
            out.points.set(ind, i2);
            ind++;
        }
        return out;
    }

    @Override
    protected Object clone() {
        return new model(frames, single_frame);
    }
    
}
