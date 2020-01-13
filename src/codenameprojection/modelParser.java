/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package codenameprojection;

import JFUtils.vector.dVector3;
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
    
    public String filename = "model";
    
    final float size = 50;
    public LinkedList<LinkedList<dVector3>> parse() throws FileNotFoundException, IOException{
        LinkedList<dVector3> buffer = new LinkedList<>();
        LinkedList<LinkedList<dVector3>> out = new LinkedList<>();
        int frames = 0;
        int points = 0;
        int index = 0;
        String line;
        BufferedReader in;
        in = new BufferedReader(new FileReader(filename + ".txt"));
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
    public LinkedList<Integer[]> parseLines(LinkedList<dVector3> points) throws FileNotFoundException, IOException{
        LinkedList<Integer[]> out = new LinkedList<>();
        String line;
        BufferedReader in;
        in = new BufferedReader(new FileReader(filename + "_lines.txt"));
             line = in.readLine();

             while(!Objects.isNull(line))
             {
                    try {
                     String curr = "";
                     //System.out.println(line);
                     line = in.readLine();
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
                 } catch (IOException | NumberFormatException iOException) {
                 }
                 
             }

             System.out.println(line);
        System.out.println(out.size() + " lines loaded and parsed succesfully!");
        return out;
    }
    public LinkedList<Integer[]> parseFaces(LinkedList<dVector3> points) throws FileNotFoundException, IOException{
        LinkedList<Integer[]> out = new LinkedList<>();
        String line;
        BufferedReader in;
        in = new BufferedReader(new FileReader(filename + "_faces.txt"));
             line = in.readLine();

             while(!Objects.isNull(line))
             {
                    try {
                     String curr = "";
                     //System.out.println(line);
                     line = in.readLine();
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
                        out.add(new Integer[]{
                            points.get(coord[0]+1).identifier,
                            points.get(coord[1]+1).identifier,
                            points.get(coord[2]+1).identifier
                        });
                     }
                     catch(Exception e){
                            try {
                                out.add(new Integer[]{
                                points.get(coord[0]).identifier,
                                points.get(coord[1]).identifier,
                                points.get(coord[2]).identifier
                            });
                            } catch (Exception ez) {
                                
                                System.out.println("Error parsing face: " + ez);
                                //ez.printStackTrace();
                            }
                        }
                 } catch (IOException | NumberFormatException iOException) {
                 }
             }

             System.out.println(line);
        System.out.println(out.size() + " faces loaded and parsed succesfully!");
        return out;
    }
    public static void main(String[] args) {
        try {
            LinkedList<LinkedList<dVector3>> parse = new modelParser().parse();
            new modelParser().parseLines(parse.getFirst());
        } catch (IOException ex) {
            Logger.getLogger(modelParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
