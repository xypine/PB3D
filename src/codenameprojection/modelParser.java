/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package codenameprojection;

import JFUtils.dVector3;
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
    public LinkedList<dVector3> parse() throws FileNotFoundException, IOException{
        LinkedList<dVector3> out = new LinkedList<>();
        String line;
        BufferedReader in;
        in = new BufferedReader(new FileReader("model.txt"));
             line = in.readLine();

             while(!Objects.isNull(line))
             {
                    try {
                     String curr = "";
                     System.out.println(line);
                     line = in.readLine();
                     int place = 0;
                     int[] coord = new int[3];
                     for (char i : line.toCharArray()) {
                         if (i == ' ') {
                             coord[place] = Integer.parseInt(curr);
                             place++;
                             curr = "";
                         } else {
                             curr = curr + i;
                         }
                     }
                     out.add(new dVector3(coord[0], coord[1], coord[2]));
                 } catch (IOException | NumberFormatException iOException) {
                 }
                    catch(Exception e){
                        System.out.println("Error parsing line: " + e);
                    }
             }

             System.out.println(line);
        System.out.println(out.size() + " points loaded and parsed succesfully!");
        return out;
    }
    public LinkedList<dVector3> parseLines() throws FileNotFoundException, IOException{
        LinkedList<dVector3> out = new LinkedList<>();
        String line;
        BufferedReader in;
        in = new BufferedReader(new FileReader("model_lines.txt"));
             line = in.readLine();

             while(!Objects.isNull(line))
             {
                    try {
                     String curr = "";
                     System.out.println(line);
                     line = in.readLine();
                     int place = 0;
                     int[] coord = new int[3];
                     for (char i : line.toCharArray()) {
                         if (i == ' ') {
                             coord[place] = Integer.parseInt(curr);
                             place++;
                             curr = "";
                         } else {
                             curr = curr + i;
                         }
                     }
                     out.add(new dVector3(coord[0], coord[1], coord[2]));
                 } catch (IOException | NumberFormatException iOException) {
                 }
                    catch(Exception e){
                        System.out.println("Error parsing line: " + e);
                    }
             }

             System.out.println(line);
        System.out.println(out.size() + " points loaded and parsed succesfully!");
        return out;
    }
    public static void main(String[] args) {
        try {
            new modelParser().parse();
        } catch (IOException ex) {
            Logger.getLogger(modelParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}