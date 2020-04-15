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

import JFUtils.point.Point3D;
import JFUtils.vector.dVector3;
import JFUtils.point.Point2D;
import codenameprojection.drawables.Vertex;
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
    public static Point2D VeToP2(Vertex source){
        Point2D o = new Point2D(source.x, source.y);
        o.identifier = source.identifier;
        return o;
    }
    public static Point2D P3ToP2(Point3D source){
        Point2D o = new Point2D(source.x, source.y);
        o.identifier = source.identifier;
        return o;
    }
    float last_z = 0;
    public LinkedList<face> constructFaceList(LinkedList<Point2D[]> origin, LinkedList<Float> faces_dist){
        int index = 0;
        LinkedList<face> out = new LinkedList<>();
        for(Point2D[] i : origin){
            float z = 0;
            try {
                z = faces_dist.get(index);
                last_z = z;
            } 
            catch(IndexOutOfBoundsException e){
                //System.out.println(e);
                z = last_z;
                //System.out.println(z);
                //throw e;
            }
            catch (Exception e) {
                
                //throw e;
            }
            out.add(new face(index, z, i));
            index++;
        }
        return out;
    }
    public static Point3D average(Point3D one, Point3D two, Point3D three){
        double x = (one.x + two.x + three.x) / 3D;
        double y = (one.y + two.y + three.y) / 3D;
        double z = (one.z + two.z + three.z) / 3D;
        return new Point3D(x, y, z);
    }
    public static Point3D average(LinkedList<Point3D> set){
        double x = 0;
        double y = 0;
        double z = 0;
        for(Point3D i : set){
            x = x + i.x;
            y = y + i.y;
            z = z + i.z;
        }
        x = x / set.size();
        y = y / set.size();
        z = z / set.size();
        return new Point3D(x, y, z);
    }
    public static Vertex averageVer(LinkedList<Vertex> set){
        double x = 0;
        double y = 0;
        double z = 0;
        for(Point3D i : set){
            x = x + i.x;
            y = y + i.y;
            z = z + i.z;
        }
        x = x / set.size();
        y = y / set.size();
        z = z / set.size();
        return new Vertex(x, y, z);
    }
}
