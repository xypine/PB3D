/*
 * The MIT License
 *
 * Copyright 2020 Elias.
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

import JFUtils.point.Point2D;
import JFUtils.point.Point3D;
import codenameprojection.drawables.vertexGroup;
import codenameprojection.modelParser;
import static codenameprojection.modelParser.filename;
import java.io.IOException;
import java.util.LinkedList;

/**
 *
 * @author Jonnelafin
 */
public class ModelUtils {
    
    public static void main(String[] args) {
        filename = "assets/models/Viper8";
        try {
            LinkedList<LinkedList<Point3D>> parse = new modelParser().parse();
            
            //new modelParser().parseLines(parse.getFirst());
            //new modelParser().parseFaces(parse.getFirst());
            //new modelParser().parseColor(parse.getFirst());
            ModelFrame first = new ModelFrame(parse.getFirst(), new LinkedList<Integer[]>(), new LinkedList<Point3D[]>(), new LinkedList<vertexGroup>());
            LinkedList<ModelFrame> frames = new LinkedList<>();
            frames.add(first);
            Model m =  new Model(frames, true);
            LinkedList<Model> models = new LinkedList<>();
            models.add(m);
            heightmap(1, models);
        } catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }
    
    public static Double[][] heightmap(double step, LinkedList<Model> models){
        LinkedList<Point3D> joined = join(models);
        Point2D mins = min(joined);
        Point2D maxs = max(joined);
        System.out.println("Min: " + mins.represent());
        System.out.println("Max: " + maxs.represent());
        return null;
    }
    public static LinkedList<Point3D> join(LinkedList<Model> models){
        LinkedList<Point3D> out = new LinkedList<>();
        for (Model model : models) {
            out.addAll(model.getFrame(0).points);
        }
        return out;
    }
    public static Point2D min(LinkedList<Point3D> org){
        double x = 0;
        double y = 0;
        for (Point3D i : org) {
            if (i.x < x) {
                x = i.x;
            }
            if (i.y < y) {
                y = i.y;
            }
        }
        Point2D out = new Point2D(x, y);
        return out;
    }
    public static Point2D max(LinkedList<Point3D> org){
        double x = 0;
        double y = 0;
        for (Point3D i : org) {
            if (i.x > x) {
                x = i.x;
            }
            if (i.y > y) {
                y = i.y;
            }
        }
        Point2D out = new Point2D(x, y);
        return out;
    }
}
