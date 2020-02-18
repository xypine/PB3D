/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codenameprojection;

import JFUtils.point.Point2D;
import JFUtils.point.Point3D;
import java.util.LinkedList;

/**
 *
 * @author guest-pecpvn
 */
public class model {
    public LinkedList<LinkedList<Point3D>> points;
    public LinkedList<Integer[]> lines;
    public LinkedList<Point2D[]> faces = new LinkedList<>();

    public model(LinkedList<LinkedList<Point3D>> p, LinkedList<Integer[]> l, LinkedList<Point2D[]> f) {
        this.points = p;
        this.lines = l;
        this.faces = f;
    }
    
}
