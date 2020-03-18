/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codenameprojection.drawables;

import JFUtils.point.Point2Int;
import java.awt.Color;

/**
 *
 * @author Elias Eskelinen (Jonnelafin)
 */
public class Line {
    public Point2Int start;
    public Point2Int end;
    public Color c;
    public Line(Point2Int start, Point2Int end, Color c){
        this.start = start;
        this.end = end;
        this.c = c;
    }
}
