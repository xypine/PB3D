/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package codenameprojection.drawables;

import JFUtils.point.Point3D;
import java.awt.Color;

/**
 *
 * @author Jonnelafin
 */
public class Vertex extends Point3D{
    public Color c;
    public Vertex(double nx, double ny, double nz, Color c) {
        super(nx, ny, nz);
        this.c = c;
    }

    public Vertex(double nx, double ny, double nz) {
        super(nx, ny, nz);
        this.c = Color.WHITE;
    }
    
    @Override
    public Vertex clone() {
        Vertex out = fromP3D(super.clone());
        out.identifier = identifier;
        out.c = c;
        return out;
    }
    
    public static Vertex fromP3D(Point3D i){
        Vertex out = new Vertex(i.x, i.y, i.z, Color.WHITE);
        return out;
    }
    public Vertex fromP3DandThis(Point3D i){
        Vertex out = new Vertex(i.x, i.y, i.z, c);
        out.identifier = identifier;
        return out;
    }
}
