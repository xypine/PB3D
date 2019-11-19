/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package codenameprojection;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.LinkedList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import jfUtils.Range;
import jfUtils.dVector;

/**
 *
 * @author Jonnelafin
 */
public class Screen extends JFrame{
    renderer r;
    public Screen(){
        //Create the renderer
        r = new renderer();
        
        //init this
        this.setTitle("Projection renderer");
        this.setSize(400, 550);
        this.setLocationRelativeTo(null);
        this.setLocation(0, 0);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        //Init components
        this.add(r);
        
        //Set visible
        this.setVisible(true);
    }
}
class renderer extends JPanel{
    private LinkedList<dVector> points = new LinkedList<>();
    private LinkedList<dVector> points_sizes = new LinkedList<>();
    private LinkedList<dVector[]> lines = new LinkedList<>();
    private LinkedList<dVector> lines_sizes = new LinkedList<>();
    public int w;
    public int h;
    @Override
    public void paintComponent(Graphics g) {
        Dimension currentSize = getParent().getSize();
        w = currentSize.width;
        h = currentSize.height;
        this.setSize(currentSize);
        super.paintComponent(g);
        repaint();
        
        g.setColor(Color.black);
        g.fillRect(0, 0, w, h);
        
        g.setColor(Color.green);
        for(int i : new Range(points.size())){
            dVector pos = points.get(i);
            dVector s = points_sizes.get(i);
            int xOff = s.intX() / 2;
            int yOff = s.intY() / 2;
            g.drawRect(pos.intX() - xOff, pos.intY() - yOff, s.intX(), s.intY());
        }
        for(int i : new Range(lines.size())){
            dVector pos1 = lines.get(i)[0];
            dVector pos2 = lines.get(i)[1];
            dVector s = lines_sizes.get(i);
            int xOff = s.intX() / 2;
            int yOff = s.intY() / 2;
            g.drawLine(pos1.intX() - xOff, pos2.intX() - xOff, pos1.intY() - yOff, pos2.intY() - yOff);
        }
    }
    public void updatePoints(LinkedList<dVector> newSet, LinkedList<dVector> newSizes){
        this.points = newSet;
        this.points_sizes = newSizes;
    }
    public void updateLines(LinkedList<dVector[]> newSet, LinkedList<dVector> newSizes){
        this.lines = newSet;
        this.lines_sizes = newSizes;
    }
}