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

import JFUtils.Range;
import JFUtils.point.Point2D;
import JFUtils.point.Point3D;
import JFUtils.quickTools;
import codenameprojection.cube.Cube;
import codenameprojection.drawables.vertexGroup;
import codenameprojection.driver;
import codenameprojection.modelParser;
import static codenameprojection.modelParser.filename;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jonnelafin
 */
public class ModelUtils {
    
    public static void main(String[] args) {
        modelParser.filename = "assets/models/misc/color";
        try {
            modelParser.size = 10;
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
            Double[][] heightmapd = heightmap(1, models);
            for(Double[] row : heightmapd){
                for(Double i : row){
                    System.out.print(Math.round(i) + " ");
                }
                System.out.println("");
            }
            driver Driver = new driver();
            Thread t = new Thread(){
                @Override
                public void run() {
                    super.run(); //To change body of generated methods, choose Tools | Templates.
                    Driver.run();
                    //Driver = new driver(null);
                }

            };
            Driver.startWithNoModel = false;
        
            
            LinkedList<Model> grid = constructGrid(heightmapd.length, heightmapd[0].length, heightmapd);
            LinkedList<Integer> gridHandles = new LinkedList<>();
            for(Model m2 : grid){
                Integer handle = m2.hashCode();
                Driver.models.put(handle, m2);
                gridHandles.add(handle);
            }
            t.start();
            while(!Driver.running){
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Cube.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
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
        int w = (int) Math.ceil(maxs.x - mins.x);
        int h = (int) Math.ceil(maxs.y - mins.y);
        System.out.println("Width: " + w);
        System.out.println("Height: " + h);
        Double[][] out = new Double[w+2][h+2];
        for(int x : new Range(w+2)){
            for(int y : new Range(h+2)){
                out[x][y] = -99999D;
            }
        }
        for(Point3D i : joined){
            try {
                int x = (int) i.x;
                int y = (int) i.z;
                x = x + w / 2;
                y = y + h / 2;
                double z = i.y;
                double curr = out[x][y];
                if (z > curr) {
                    out[x][y] = z;
                }
            } catch (Exception e) {
                //throw e;
            }
        }
        int x = 0;
        int y = 0;
        Double[][] out2 = new Double[out.length][out[0].length];
        for(Double[] row : out){
            for(Double i : row){
                int done = 0;
                //i != -99999D
                if (true) {
                    double sum = 0;
                    double raw_done = 0;
                    double raw_sum = 0;
                    for(Point2D d : quickTools.vectorDirs4){
                        try {
                            double val = out[(int) (x + d.x)][(int) (y + d.y)];
                            //if (val != -99999D && sum / 10 < val) {
                            //    sum = sum + val;
                            //    done++;
                            //}
                            if(val > sum / done && val > -99999D){
                                sum = val;
                                done++;
                            }
                            raw_sum = raw_sum + val;
                            raw_done++;
                        } catch (Exception e) {
                        }
                    }
                    for(Point2D d : quickTools.dirs){
                        try {
                            double val = out[(int) (x + d.x * 2)][(int) (y + d.y * 2)];
                            //if (val != -99999D && sum / 10 < val) {
                            //    sum = sum + val;
                            //    done++;
                            //}
                            if(val > sum / done && val > -99999D){
                                sum = val;
                                done++;
                            }
                            raw_sum = raw_sum + val;
                            raw_done++;
                        } catch (Exception e) {
                        }
                    }
                    double raw = raw_sum / raw_done;
                    
                    out2[x][y] = i;
                    //raw != -99999D
                    //if (raw > -9999D) {
                    //    out[x][y] = sum / done;
                    //}
                    if (i != -99999D || raw != -99999D) {
                        out2[x][y] = sum;
                    }
                }
                y++;
            }
            x++;
            y = 0;
        }
        return out2;
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
            if (i.z < y) {
                y = i.z;
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
            if (i.z > y) {
                y = i.z;
            }
        }
        Point2D out = new Point2D(x, y);
        return out;
    }
    static LinkedList<Model> constructGrid(int rx2, int ry2, Double[][] h){
        int ind = 0;
        int rz2 = 1;
        Random rnd = new Random();
        LinkedList<Model> out = new LinkedList<>();
        for(int z : new Range(rz2)){
            for (int x : new Range(rx2)) {
                for (int y: new Range(ry2)){
                    LinkedList<ModelFrame> frames = new LinkedList<>();
                    LinkedList<Point3D> points = new LinkedList<>();
                    LinkedList<Integer[]> lines = new LinkedList<>();
                    LinkedList<Point3D[]> faces = new LinkedList<>();
                    LinkedList<vertexGroup> color = new LinkedList<>();

                    double rndX = (x - (rx2 / 2) )*1;
                    double rndY = (y - (ry2 / 2) )*1;
                    double rndZ = h[x][y];



                    points.add(new Point3D(rndX, rndZ, rndY));


                    if(out.size() > 2){
                        try {
                            Point3D last = out.get(ind - 1).getFrame(0).points.getFirst();
                            if (y !=0) {
                                lines.add(new Integer[]{points.getFirst().identifier, last.identifier});
                            }
                        } catch (Exception e) {
                        }
                            
                            
                        try {
                            Point3D pair = out.get(ind - ry2).getFrame(0).points.getFirst();
                            if (x != 0) {
                                lines.add(new Integer[]{points.getFirst().identifier, pair.identifier});
                            }
                        } catch (Exception e) {
                        }
                        
                        try {
                            Point3D pair = out.get(ind - (rx2 * ry2)*rz2).getFrame(0).points.getFirst();
                            if (false) {
                                lines.add(new Integer[]{points.getFirst().identifier, pair.identifier});
                            }
                        } catch (Exception e) {
                            //System.out.println(ind - (rx2 * ry2)*rz2);
                        }
                            //lines.add(new Integer[]{points.getFirst().identifier, out.get(ind - ry2).getFrame(0).points.getFirst().identifier});
                            //lines.add(new Integer[]{points.getFirst().identifier, out.get(ind - rz2).getFrame(0).points.getFirst().identifier});
                            //faces.add(new Point2D[]{

                            //});
                    }
                    //if(y == ry2){
                    //    lines.add(new Integer[]{points.getFirst().identifier, out.get(ind - ry2).getFrame(0).points.getFirst().identifier});
                    //}


                    frames.add(new ModelFrame(points , lines, faces, color));
                    Model m = new Model(frames, true);
                    m.hidePoints = false;
                    m.hideLines = false;
                    out.add(m);
                    ind = ind + 1;
                }
            }
        }
        
        return out;
    }
}
