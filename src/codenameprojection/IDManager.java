/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codenameprojection;

import JFUtils.point.Point2D;
import JFUtils.vector.dVector2;

/**
 *
 * @author Elias Eskelinen (Jonnelafin)
 */
public class IDManager {
   int free = 0;
   public Point2D getNew(){
       Point2D out = new Point2D(0, 0);
       out.identifier = free;
       free++;
       return out;
   }
   public int getFreeID(){
       int next = free;
       free++;
       return next;
   }
}
