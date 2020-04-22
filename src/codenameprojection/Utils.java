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
    public LinkedList<face> constructFaceList(LinkedList<Point3D[]> origin, LinkedList<Float> faces_dist){
        int index = 0;
        LinkedList<face> out = new LinkedList<>();
        for(Point3D[] i : origin){
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
    public static Point3D abs(Point3D from){
        Point3D to = new Point3D(Math.abs(from.x), Math.abs(from.y), Math.abs(from.z));
        to.identifier = from.identifier;
        return to;
    }
    public static double[] map(Point3D one, Point3D two, Point3D three){
        Point3D average = Point3D.divide(Point3D.add(one, two), new Point3D(2, 2, 2));
        Point3D diff = Point3D.subtract(average, three);
        Point3D four = Point3D.subtract(average, diff);
        four = diff;
        return map(one, two, three, four);
    }
    public static double[] map(Point3D one, Point3D two, Point3D three, Point3D four){
        return map(one.x + 0.5, one.y + 0.5, one.x, one.y, two.x, two.y, three.x, three.y, four.x, four.y);
    }
    public static double[] map(double x, double y, double x0, double y0, double x1, double y1, double x2, double y2, double x3, double y3) {
        double A = (x0 - x) * (y0 - y2) - (y0 - y) * (x0 - x2);
        double B = ((x0 - x) * (y1 - y3) - (y0 - y) * (x1 - x3) + (x1 - x) * (y0 - y2) - (y1 - y) * (x0 - x2)) / 2.0;
        double C = (x1 - x) * (y1 - y3) - (y1 - y) * (x1 - x3);

        double det = A - 2.0 * B + C;

        double u;
        if (det == 0.0) {
            u = A / (A - C);
            if (Double.isNaN(u) || u < 0.0 || u > 1.0)
                return null;
        } else {
            double u1 = ((A - B) + Math.sqrt(B * B - A * C)) / det;
            boolean u1valid = !Double.isNaN(u1) && u1 >= 0.0 && 1.0 >= u1;

            double u2 = ((A - B) - Math.sqrt(B * B - A * C)) / det;
            boolean u2valid = !Double.isNaN(u2) && u2 >= 0.0 && 1.0 >= u2;

            if (u1valid && u2valid)
                u = u1 < u2 ? u2 : u1;
            else if (u1valid)
                u = u1;
            else if (u2valid)
                u = u2;
            else
                return null;
        }

        double v1 = ((1.0 - u) * (x0 - x) + u * (x1 - x)) / ((1.0 - u) * (x0 - x2) + u * (x1 - x3));
        boolean v1valid = !Double.isNaN(v1) && v1 >= 0.0 && 1.0 >= v1;

        double v2 = ((1.0 - u) * (y0 - y) + u * (y1 - y)) / ((1.0 - u) * (y0 - y2) + u * (y1 - y3));
        boolean v2valid = !Double.isNaN(v2) && v2 >= 0.0 && 1.0 >= v2;

        double v = v1;
        if (v1valid && v2valid)
            v = v1 < v2 ? v2 : v1;
        else if (v1valid)
            v = v1;
        else if (v2valid)
            v = v2;
        //else
        //    return null;
        return new double[] { u, v };
    }
}
