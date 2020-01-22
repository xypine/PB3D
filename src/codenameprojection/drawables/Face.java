/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codenameprojection.drawables;

import JFUtils.point.Point2Int;
import java.awt.Color;
import java.awt.Polygon;

/**
 *
 * @author Elias Eskelinen <elias.eskelinen@protonmail.com>
 */
public class Face {
    public Polygon p;
    public Color c;
    public Face(Polygon p, Color c){
        this.p = p;
        this.c = c;
    }
}
