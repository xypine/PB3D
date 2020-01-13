

package codenameprojection;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.LinkedList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import JFUtils.Range;
import JFUtils.pathfinding.astarNode;
import JFUtils.point.Point2D;
import java.awt.Polygon;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;

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
    public int frame;
    
    public boolean drawPoints = true;
    public boolean drawLines = false;
    public boolean drawFaces = true;
    public boolean drawErrors = true;
    public int received = 0;
    public int errors = 0;
    
    @Override
    public void paintComponent(Graphics g) {
        Dimension currentSize = getParent().getSize();
        w = currentSize.width;
        h = currentSize.height;
        this.setSize(currentSize);
        super.paintComponent(g);
        repaint();
        
        g.setColor(Color.white);
        g.fillRect(0, 0, w, h);
        
            g.setColor(Color.red);
            HashMap<Integer, Point2D> a = getIDMap();
            drawnFaces = 0;
            drawnLines = 0;
            
            
            
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
                        else{
                            errors++;
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
            if (drawFaces) {
            for (int i : new Range(faces.size())) {
                try {
                    if (!(Objects.isNull(a.get(faces.get(i).points[0]))) && !(Objects.isNull(a.get(faces.get(i).points[1]))) && !(Objects.isNull(a.get(faces.get(i).points[2])))) {
                        double x1 = 0;
                        double x2 = 0;
                        double x3 = 0;
                        double y1 = 0;
                        double y2 = 0;
                        double y3 = 0;
                        boolean draw = true;
                        try {
                            x1 = a.get(faces.get(i).points[0]).x;
                            x2 = a.get(faces.get(i).points[1]).x;
                            x3 = a.get(faces.get(i).points[2]).x;
                            y1 = a.get(faces.get(i).points[0]).y;
                            y2 = a.get(faces.get(i).points[1]).y;
                            y3 = a.get(faces.get(i).points[2]).y;
                        } catch (Exception e) {
                            draw = false;
                        }
                        
                        Color c = Color.blue;
                        try {
                            c = faces_color.get(faces.get(i).originalIndex);
                            if(c.equals(Color.green)){
                                errors++;
                            }
                        } catch (Exception e) {
                            //throw e;
                            if (!drawErrors) {
                                draw = false;
                            }
                        }
                        g.setColor(c);

                        //g.setColor(Color.CYAN);
                        int xpoints[] = {(int)x1,(int) x2,(int) x3};
                        int ypoints[] = {(int)y1,(int) y2,(int) y3};
                        int npoints = 3;
                        if (draw) {
                            g.fillPolygon(new Polygon(xpoints, ypoints, npoints));
                            //g.drawPolygon(xpoints, ypoints, npoints);
                            //g.drawLine(x1, y1, x2, y2);
                            drawnFaces++;
                        }
                        else{
                            errors++;
                        }
                    }
                    else{
                        System.out.println("Points not found for index " + i);
                    }
                } catch (Exception e) {
                    throw e;
                }
            }
        }
        g.setColor(Color.white);
        g.drawString("frame " + frame, w/10, h/7);
        g.drawString("" + errors + " errors while drawing", w/10, h/5);
        g.drawString("" + received + " faces received", w/10, h/6);
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
    LinkedList<face> faces = new LinkedList<>();
    LinkedList<Color> faces_color = new LinkedList<>();
    LinkedList<Float> faces_dist = new LinkedList<>();
    public void updateFaces(LinkedList<Integer[]> newSet, LinkedList<Color> color, LinkedList<Float> dist){
        
        this.faces_color = color;
        this.faces_dist = dist;
        this.received = newSet.size();
        this.faces = constructFaceList(newSet);
        
        Comparator comp = face.DESCENDING_COMPARATOR;
        
        PriorityQueue<face> pr = new PriorityQueue<>();
        
        LinkedList<face> newF = new LinkedList<>();
        
        faces.forEach(l -> pr.add(l));
        
        while(!pr.isEmpty()){
            newF.add(pr.remove());
        }
        faces = newF;
        //Collections.sort(this.faces);
        //faces.sort(null);
    }
    public HashMap getIDMap(){
        HashMap<Integer, Point2D> out = new HashMap<Integer, Point2D>();
        for(Point2D i : points){
            out.put(i.identifier, i);
        }
        return out;
    }
    float last_z = 0;
    LinkedList<face> constructFaceList(LinkedList<Integer[]> origin){
        int index = 0;
        LinkedList<face> out = new LinkedList<>();
        for(Integer[] i : origin){
            float z = Float.MAX_VALUE;
            try {
                z = faces_dist.get(index);
                last_z = z;
            } 
            catch(IndexOutOfBoundsException e){
                //System.out.println(e);
                z = last_z;
                //System.out.println(z);
                throw e;
            }
            catch (Exception e) {
                
                throw e;
            }
            out.add(new face(index, z, i));
            index++;
        }
        return out;
    }
    public float speed = 0;
}
class face implements Comparable<face>{
    public face(int ogIndex, float z, Integer[] points){
        if(Objects.isNull(originalIndex) || Objects.isNull(z) || Objects.isNull(points)){
            System.out.println("a");
            if(points.length > 0){
                if(Objects.isNull(points[0]) || Objects.isNull(points[1]) || Objects.isNull(this.points[2])){
                    System.out.println("NOOOO");
                }
            }
            else{
                System.out.println("\"Verticies must contain 3 points!\"");
                throw new IllegalArgumentException("Verticies must contain 3 points!");
            }
        }
        
        this.originalIndex = ogIndex;
        this.z = z;
        this.points = points;
    }
    
    int originalIndex;
    Integer[] points = new Integer[]{};
    
    public float z = (float) 0;
    public Integer getZ(){
        return (int) z * 100000;
    }
    @Override
    public int compareTo(face o) {
        //return(getZ().compareTo(o.getZ()));
        return ((int)z*10)-((int)o.z*10);
    }
    
    public static final Comparator<face> DESCENDING_COMPARATOR = new Comparator<face>() {
        // Overriding the compare method to sort the age
        public int compare(face d, face d1) {
            return d.getZ() - d1.getZ();
        }
    };
    
}