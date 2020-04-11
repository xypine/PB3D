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
import JFUtils.vector.dVector3;
import static codenameprojection.Utils.P3ToP2;
import codenameprojection.drawables.vertexGroup;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jonnelafin
 */
public class modelParser {
    IDManager ids = new IDManager();
                                    //assets/models/Viper8
    public static String filename = "assets/models/misc/gordon";

    public modelParser() {
    }
    
    public modelParser(String filename) {
        this.filename = filename;
    }
    
    public static float size = 500;
    
    public LinkedList<vertexGroup> parseColor(LinkedList<Point3D> points){
        LinkedList<vertexGroup> color = new LinkedList<>();
        String lineC;
        int ind = 0;
        try {
            BufferedReader c = new BufferedReader(new FileReader(filename + "_color.pb3d"));
            lineC = c.readLine();
            while (Objects.nonNull(lineC)) {
                lineC = c.readLine();
                try {
                    String curr = "";
                    //System.out.println(line);
                    int place = 0;
                    Float[] coord = new Float[3];
                    for (char i : lineC.toCharArray()) {
                        if (i == ' ' ) {
                            coord[place] = Float.parseFloat(curr);
                            place++;
                            curr = "";
                        } else {
                            curr = curr + i;
                        }
                    }
                    int id = -1;
                    try {
                        id = points.get(ind).identifier;
                    } catch (Exception e) {
                        //e.printStackTrace();
                    }
                    color.add(new vertexGroup(
                            coord[0], 
                            coord[1], 
                            coord[2],
                            id
                    ));
                    ind++;
                } catch (Exception e) {
                    //e.printStackTrace();
                }
            }
        } catch (Exception e) {
            System.out.println("COULD NOT READ COLOR DATA PROPERLY, ERROR: " + e);
            //e.printStackTrace();
        }
        System.out.println(color.size() + " color points loaded and parsed succesfully!");
        //for(vertexGroup i : color){
        //    System.out.println("    " + i.r + " " + i.g + " " + i.b + " | ID " + i.vertexID);
        //}
        return color;
    }
    
    public LinkedList<LinkedList<Point3D>> parse() throws FileNotFoundException, IOException{
        LinkedList<Point3D> buffer = new LinkedList<>();
        LinkedList<LinkedList<Point3D>> out = new LinkedList<>();
        int frames = 0;
        int points = 0;
        int index = 0;
        String line;
        BufferedReader in;
        
        
        boolean useColor = true;
        in = new BufferedReader(new FileReader(filename + ".pb3d"));
             line = in.readLine();

             while(!Objects.isNull(line))
             {
                    try {
                     String curr = "";
                     //System.out.println(line);
                     line = in.readLine();
                     int place = 0;
                     int[] coord = new int[3];
                     for (char i : line.toCharArray()) {
                         if (i == '#' && frames > 0){
                             out.add(buffer);
                             buffer = new LinkedList<>();
                             frames++;
                             index = 0;
                         }
                         if(i == '#' && !(frames > 0)){
                             frames++;
                         }
                         if (i == ' ') {
                             coord[place] = Integer.parseInt(curr);
                             place++;
                             curr = "";
                         } else {
                             curr = curr + i;
                         }
                     }
                     dVector3 tmp = new dVector3(coord[0]/size, coord[1]/size, coord[2]/size);
                     points++;
                     tmp.identifier = index;
                     index++;
                     buffer.add(tmp);
                 } catch (IOException | NumberFormatException iOException) {
                 }
                    catch(Exception e){
                        System.out.println("Error parsing line: " + e);
                    }
             }

             System.out.println(line);
        out.add(buffer);
        buffer = new LinkedList<>();
        frames++;
        System.out.println(points + " points in " + frames + " frames loaded and parsed succesfully!");
        return out;
    }
    public LinkedList<Integer[]> parseLines(LinkedList<Point3D> points) throws FileNotFoundException, IOException{
        LinkedList<Integer[]> out = new LinkedList<>();
        String line;
        BufferedReader in;
        in = new BufferedReader(new FileReader(filename + "_lines.pb3d"));
             line = in.readLine();

             while(!Objects.isNull(line))
             {
                     String curr = "";
                     //System.out.println(line);
                     int place = 0;
                     int[] coord = new int[2];
                        try {
                            for (char i : line.toCharArray()) {
                                if (i == ' ') {
                                    coord[place] = Integer.parseInt(curr);
                                    place++;
                                    curr = "";
                                } else {
                                    curr = curr + i;
                                }
                            }
                        } catch (NullPointerException nu) {
                            System.out.println("char in the line was null!");
                        }
                        try {
                            out.add(new Integer[]{
                                points.get(coord[0]+1).identifier,
                                points.get(coord[1]+1).identifier
                            });
                        } 
                        catch(Exception e){
                            try {
                                out.add(new Integer[]{
                                    points.get(coord[0]).identifier,
                                    points.get(coord[1]).identifier
                                });
                            } catch (Exception ez) {
                                
                                System.out.println("Error parsing line: " + ez);
                                //ez.printStackTrace();
                            }
                        }
                     line = in.readLine();
                 
             }

             System.out.println(line);
        System.out.println(out.size() + " lines loaded and parsed succesfully!");
        return out;
    }
    public LinkedList<Point2D[]> parseFaces(LinkedList<Point3D> points) throws FileNotFoundException, IOException{
        LinkedList<Point2D[]> out = new LinkedList<>();
        String line;
        BufferedReader in;
        in = new BufferedReader(new FileReader(filename + "_faces.pb3d"));
             line = in.readLine();

             while(!Objects.isNull(line))
             {
                     String curr = "";
                     //System.out.println(line);
                     
                     int place = 0;
                     int[] coord = new int[3];
                        try {
                            for (char i : line.toCharArray()) {
                                if (i == ' ') {
                                    coord[place] = Integer.parseInt(curr);
                                    place++;
                                    curr = "";
                                } else {
                                    curr = curr + i;
                                }
                            }
                        } 
                        catch (NullPointerException nu) {
                            System.out.println("char in the line was null!");
                        }
                     try {
                        out.add(new Point2D[]{
                            P3ToP2(points.get(coord[0]+1)),
                            P3ToP2(points.get(coord[1]+1)),
                            P3ToP2(points.get(coord[2]+1))
                        });
                     }
                     catch(Exception e){
                            try {
                                out.add(new Point2D[]{
                                P3ToP2(points.get(coord[0])),
                                P3ToP2(points.get(coord[1])),
                                P3ToP2(points.get(coord[2]))
                            });
                            } catch (Exception ez) {
                                
                                System.out.println("Error parsing face: " + ez);
                                //ez.printStackTrace();
                            }
                        }
                
                    line = in.readLine();
             }

             System.out.println(line);
        System.out.println(out.size() + " faces loaded and parsed succesfully!");
        return out;
    }
    public static void main(String[] args) {
        try {
            filename = "assets/models/Viper8";
            LinkedList<LinkedList<Point3D>> parse = new modelParser().parse();
            new modelParser().parseLines(parse.getFirst());
            new modelParser().parseFaces(parse.getFirst());
            new modelParser().parseColor(parse.getFirst());
        } catch (IOException ex) {
            Logger.getLogger(modelParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
