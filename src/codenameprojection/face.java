/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codenameprojection;

import JFUtils.point.Point2D;
import java.util.Comparator;
import java.util.Objects;

/**
 *
 * @author guest-f1wfbl
 */
public class face implements Comparable<face>{

    public face() {
    }
    
    public face(int ogIndex, float z, Point2D[] points){
        if(Objects.isNull(originalIndex) || Objects.isNull(z) || Objects.isNull(points)){
            System.out.println("a");
            if(points.length > 0){
                if(Objects.isNull(points[0]) || Objects.isNull(points[1]) || Objects.isNull(this.points[2])){
                    System.out.println("NOOOO");
                }
            }
            else{
                System.out.println("\"Verticies must contain 3 points!\"");
                throw new IllegalArgumentException("Verticies must contain 3 points!");
            }
        }
        
        this.originalIndex = ogIndex;
        this.z = z;
        this.points = points;
    }
    
    int originalIndex;
    Point2D[] points = new Point2D[]{};
    
    public float z = (float) 0;
    public Integer getZ(){
        return (int) z * 100000;
    }
    @Override
    public int compareTo(face o) {
        //return(getZ().compareTo(o.getZ()));
        return ((int)z*100)-((int)o.z*100);
    }
    
    public static final Comparator<face> DESCENDING_COMPARATOR = new Comparator<face>() {
        // Overriding the compare method to sort the age
        public int compare(face d, face d1) {
            return d.getZ() - d1.getZ();
        }
    };
    
}
