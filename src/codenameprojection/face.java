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

import JFUtils.point.Point2D;
import JFUtils.point.Point3D;
import java.util.Comparator;
import java.util.Objects;

/**
 *
 * @author guest-f1wfbl
 */
public class face implements Comparable<face>{

    public face() {
    }
    
    public face(int ogIndex, float z, Point3D[] points){
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
    Point3D[] points = new Point3D[]{};
    
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
