/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package codenameprojection;

import JFUtils.point.Point3D;
import JFUtils.vector.dVector3;
import JFUtils.point.Point2D;
import java.util.LinkedList;

/**
 *
 * @author Jonnelafin
 */
public class Utils {
    public static double getDistance(Point3D p1, Point3D p2) {
        return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2) + Math.pow(p1.z - p2.z, 2));
    }
    public static Point2D vToP2(dVector3 source){
        Point2D o = new Point2D(source.x, source.y);
        o.identifier = source.identifier;
        return o;
    }
    float last_z = 0;
    public LinkedList<face> constructFaceList(LinkedList<Point2D[]> origin, LinkedList<Float> faces_dist){
        int index = 0;
        LinkedList<face> out = new LinkedList<>();
        for(Point2D[] i : origin){
            float z = Float.MAX_VALUE;
            try {
                z = faces_dist.get(index);
                last_z = z;
            } 
            catch(IndexOutOfBoundsException e){
                //System.out.println(e);
                z = last_z;
                //System.out.println(z);
                throw e;
            }
            catch (Exception e) {
                
                throw e;
            }
            out.add(new face(index, z, i));
            index++;
        }
        return out;
    }
}
