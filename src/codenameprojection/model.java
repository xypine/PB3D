/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package codenameprojection;

import JFUtils.point.Point3D;
import java.util.LinkedList;

/**
 *
 * @author Jonnelafin
 */
public class model {
    LinkedList<model_frame> frames;
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
    
    public model_frame getFrame(int index){
        model_frame out;
        if(!single_frame){
            out = (model_frame) frames.get(index);
        }
        else{
            out = (model_frame) frames.getFirst();
        }
        int ind = 0;
        /*for(Point3D i : out.points){
            int oldID = i.identifier;
            Point3D i2 = i.clone();
            i2 = driver.matmul(driver.RY((float) rotation_Y), i2.toFVector3()).toDVector3();
            //i = driver.matmul(driver.RZ((float) rotation_Z), i.toFVector3()).toDVector3();
            i2 = driver.matmul(driver.RX((float) rotation_X), i2.toFVector3()).toDVector3();
            
            Point3D global = new Point3D(x, y, z);
            i2 = Point3D.add(global, i2);
            i2.identifier = oldID;
            out.points.set(ind, i2);
            ind++;
        }*/
        return out;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        super.clone();
        return new model(frames, single_frame);
    }
    
}
