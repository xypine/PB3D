

package codenameprojection;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.LinkedList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import JFUtils.Range;
import JFUtils.InputActivated;
import JFUtils.dVector;
import java.util.HashMap;

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
    private LinkedList<Integer[]> lines = new LinkedList<>();
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
        
        try {
            g.setColor(Color.red);
            HashMap<Integer, dVector> a = getIDMap();
            for (int i : new Range(lines.size())) {
                int x1 = a.get(lines.get(i)[0]).intX();
                int x2 = a.get(lines.get(i)[1]).intX();;
                int y1 = a.get(lines.get(i)[0]).intY();;
                int y2 = a.get(lines.get(i)[1]).intY();;
                g.drawLine(x1, y1, x2, y2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        g.setColor(Color.green);
        for(int i : new Range(points.size())){
            dVector pos = points.get(i);
            dVector s = points_sizes.get(i);
            int xOff = s.intX() / 2;
            int yOff = s.intY() / 2;
            g.drawRect(pos.intX() - xOff, pos.intY() - yOff, s.intX(), s.intY());
        }
        
    }
    public void updatePoints(LinkedList<dVector> newSet, LinkedList<dVector> newSizes){
        this.points = newSet;
        this.points_sizes = newSizes;
    }
    public void updateLines(LinkedList<Integer[]> newSet){
        this.lines = newSet;
    }
    private HashMap getIDMap(){
        HashMap<Integer, dVector> out = new HashMap<Integer, dVector>();
        for(dVector i : points){
            out.put(i.identifier, i);
        }
        return out;
    }
}