

package codenameprojection;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.LinkedList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import JFUtils.Range;
import JFUtils.point.Point2D;
import java.awt.Polygon;
import java.util.HashMap;
import java.util.Objects;

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
    public float nano = 0;
    private LinkedList<Point2D> points = new LinkedList<>();
    private LinkedList<Point2D> points_sizes = new LinkedList<>();
    private LinkedList<Integer[]> lines = new LinkedList<>();
    private LinkedList<Color> lines_color = new LinkedList<>();
    public int w;
    public int h;
    public int drawnLines;
    public int drawnFaces;
    
    public boolean drawPoints = true;
    public boolean drawLines = true;
    public boolean drawErrors = true;
    
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
        
            g.setColor(Color.red);
            HashMap<Integer, Point2D> a = getIDMap();
            drawnFaces = 0;
            
            for(int i : new Range(faces.size())){
                try {
                    if (!(Objects.isNull(a.get(faces.get(i)[0]))) && !(Objects.isNull(a.get(faces.get(i)[1]))) && !(Objects.isNull(a.get(faces.get(i)[2])))) {
                        int x1 = a.get(faces.get(i)[0]).intX();
                        int x2 = a.get(faces.get(i)[1]).intX();
                        int x3 = a.get(faces.get(i)[2]).intX();
                        int y1 = a.get(faces.get(i)[0]).intY();
                        int y2 = a.get(faces.get(i)[1]).intY();
                        int y3 = a.get(faces.get(i)[2]).intY();
                        
                        
                        boolean draw = true;
                        Color c = Color.blue;
                        try {
                            c = faces_color.get(i);
                        } catch (Exception e) {
                            //throw e;
                            if (!drawErrors) {
                                draw = false;
                            }
                        }
                        g.setColor(c);
                        
                        //g.setColor(Color.CYAN);
                        
                        int xpoints[] = {x1, x2, x3};
                        int ypoints[] = {y1, y2, y3};
                        int npoints = 3;
                        if (draw) {
                            //g.fillPolygon(new Polygon(new int[]{x1, x2, x3}, new int[]{x1, x2, x3}, 3));
                            g.fillPolygon(xpoints, ypoints, npoints);
                            //g.drawLine(x1, y1, x2, y2);
                            drawnFaces++;
                        }
                    }
                } catch (Exception e) {
                    throw e;
                }
            }
            
            if (drawLines) {
            for (int i : new Range(lines.size())) {
                try {
                    if (!(Objects.isNull(a.get(lines.get(i)[0]))) && !(Objects.isNull(a.get(lines.get(i)[1])))) {
                        int x1 = a.get(lines.get(i)[0]).intX();
                        int x2 = a.get(lines.get(i)[1]).intX();
                        int y1 = a.get(lines.get(i)[0]).intY();
                        int y2 = a.get(lines.get(i)[1]).intY();
                        
                        
                        boolean draw = true;
                        Color c = Color.red;
                        try {
                            c = lines_color.get(i);
                        } catch (Exception e) {
                            //throw e;
                            if (!drawErrors) {
                                draw = false;
                            }
                        }
                        g.setColor(c);
                        
                        if (draw) {
                            g.drawLine(x1, y1, x2, y2);
                            drawnLines++;
                        }
                    }
                } catch (Exception e) {
                    throw e;
                }
            }
        }
            g.setColor(Color.green);
            //int value = (int) (new Random().nextInt(255) / 2) * 4;
            
            
            if (drawPoints) {
            for (int i : new Range(points.size())) {
                try {
                    //int value2 = new Random().nextInt(255) / 2;
                    //int value3 = new Random().nextInt(255) / 2;
                    //g.setColor(new Color(value2, value, value3));
                    
                    Point2D pos = points.get(i);
                    Point2D s = points_sizes.get(i);
                    int xOff = s.intX() / 2;
                    int yOff = s.intY() / 2;
                    
                    int cS = (int) (s.x * 1000);
                    if(cS > 255){
                        cS = 255;
                    }
                    if(cS < 0){
                        cS = 0;
                    }
                    
                    //g.setColor(new Color(cS, cS, cS));
                    
                    g.drawRect(pos.intX() - xOff, pos.intY() - yOff, s.intX(), s.intY());
                } catch (Exception e) {
                    //throw e;
                }
            }
        }
        g.setColor(Color.white);
        g.drawString("" + points.size() + " Points, " + drawnLines + " Lines and " + drawnFaces + " faces drawn", w/10, h/10);
        g.drawString("" + nano + " frames per nanosecond", w - w/5, h/10);
        g.drawString("" + (int) (nano * 1000000000) + " FPS", w - w/5, h/7);
        g.drawString("speed: " + speed + "", w - w/5, h/6);
    }
    public void updatePoints(LinkedList<Point2D> newSet, LinkedList<Point2D> newSizes){
        this.points = newSet;
        this.points_sizes = newSizes;
    }
    public void updateLines(LinkedList<Integer[]> newSet, LinkedList<Color> color){
        this.lines = newSet;
        this.lines_color = color;
    }
    LinkedList<Integer[]> faces = new LinkedList<>();
    LinkedList<Color> faces_color = new LinkedList<>();
    LinkedList<Float> faces_dist = new LinkedList<>();
    public void updateFaces(LinkedList<Integer[]> newSet, LinkedList<Color> color, LinkedList<Float> dist){
        this.faces = newSet;
        this.faces_color = color;
        this.faces_dist = dist;
    }
    public HashMap getIDMap(){
        HashMap<Integer, Point2D> out = new HashMap<Integer, Point2D>();
        for(Point2D i : points){
            out.put(i.identifier, i);
        }
        return out;
    }
    public float speed = 0;
}