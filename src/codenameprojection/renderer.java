/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package codenameprojection;

import JFUtils.Range;
import JFUtils.point.Point2D;
import JFUtils.point.Point2Int;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 *
 * @author Jonnelafin
 */
public class renderer extends JPanel{
    public BufferedImage output;
    public int cx;
    public int cy;
    public int cz;
    
    public renderer(Component parent){
        //output = new BufferedImage(parent.getWidth(), parent.getWidth(), BufferedImage.TYPE_INT_RGB);
        setIgnoreRepaint(true);
    }
    
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
    public boolean shading = false;
    public boolean drawLines = false;
    public boolean drawFaces = true;
    public boolean drawErrors = false;
    public int received = 0;
    public int errors = 0;
    
    private GraphicsConfiguration config =
            GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration();
    
    public boolean usePixelRendering = false;
    
    private void drawPolygon(Polygon p, Color c){
        for(int x : new Range( output.getWidth()) ) {
            for(int y : new Range( output.getHeight() )){
                if(p.contains(  new Point(x, y)  )){
                    output.setRGB(x, y, c.getRGB());
                }
            }
        }
    }
    private void drawLine(Point2Int start, Point2Int end, Color c){
        for(int x : new Range( output.getWidth()) ) {
            for(int y : new Range( output.getHeight() )){
                if (new Polygon(new int[]{start.x, end.x}, new int[]{start.y,end.y}, 2).contains(new Point(x, y))) {
                    output.setRGB(x, y, c.getRGB());
                }
            }
        }
    }
    
    
    
    private void drawPoint(Point2Int point, Color c){
        output.setRGB(point.x, point.y, c.getRGB());
    }
    
    public final BufferedImage create(final int width, final int height,
            final boolean alpha) {
        return config.createCompatibleImage(width, height, alpha
                ? Transparency.TRANSLUCENT : Transparency.OPAQUE);
    }
    
    private void clear(){
    //    for(int x : new Range( output.getWidth()) ) {
    //        for(int y : new Range( output.getHeight() )){
    //            output.setRGB(x, y, 0);
    //        }
    //    }
    }
    
    private BufferedImage createImageFromBytes(byte[] imageData) {
        ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
        try {
            return ImageIO.read(bais);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void paintComponent(Graphics g) {
        Instant beginTime = Instant.now();
        Dimension currentSize = getParent().getSize();
        w = currentSize.width;
        h = currentSize.height;
        this.setSize(currentSize);
        super.paintComponent(g);
        Graphics2D gb = null;
        if (usePixelRendering) {
            if (Objects.isNull(output) || !(output.getWidth() == w && output.getHeight() == h)) {
                output = create(w, h, false);
            } else {
                //clear();
            }
            //gb = (Graphics2D) output.getGraphics();
            //if(Objects.isNull(gb)){
                gb = output.createGraphics();
            //}
            gb.setRenderingHint(
                    RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            //gb.setColor(Color.BLACK);
            //gb.fillRect(0, 0, w, h);
        }
        
        errors = 0;
        repaint();
        
        g.setColor(Color.black);
        g.fillRect(0, 0, w, h);
        
            g.setColor(Color.red);
            HashMap<Integer, Point2D> a = getIDMap();
            drawnFaces = 0;
            drawnLines = 0;
            
            LinkedList<Polygon> facesToDraw = new LinkedList<>();
            LinkedList<Color> faceColorsToDraw = new LinkedList<>();
            
            LinkedList<Integer[][]> linesToDraw = new LinkedList<>();
            LinkedList<Color> lineColorsToDraw = new LinkedList<>();
            
            if (drawLines) {
            for (int i : new Range(lines.size())) {
                try {
                    boolean gotFine = false;
                    try {
                        gotFine = !(Objects.isNull(a.get(lines.get(i)[0]))) && !(Objects.isNull(a.get(lines.get(i)[1])));
                    } catch (Exception e) {
                        errors++;
                    }
                    if (gotFine) {
                        int x1 = 0;
                        int y1 = 0;
                        int x2 = 0;
                        int y2 = 0;
                        try {
                            x1 = a.get(lines.get(i)[0]).intX();
                            x2 = a.get(lines.get(i)[1]).intX();
                            y1 = a.get(lines.get(i)[0]).intY();
                            y2 = a.get(lines.get(i)[1]).intY();
                        } catch (Exception e) {
                            errors++;
                        }
                        
                        
                        boolean draw = true;
                        Color c = Color.red;
                        try {
                            c = lines_color.get(i);
                        } catch (Exception e) {
                            
                            if (!drawErrors) {
                                draw = false;
                            }
                            //throw e;
                        }
                        if (shading) {
                            g.setColor(c);
                        }
                        
                        
                        if (draw) {
                            //
                            if (usePixelRendering) {
                                //drawLine(new Point2Int(x1, y1), new Point2Int(x2, y2), c);
                                //gb.setColor(c);
                                //gb.drawLine(x1, y1, x2, y2);
                                linesToDraw.add(new Integer[][]{
                                    {x1, y1}, {x2, y2} 
                                });
                                lineColorsToDraw.add(c);
                            } else {
                                g.drawLine(x1, y1, x2, y2);
                            }
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
                    if (usePixelRendering) {
                        drawPoint(pos, Color.red);
                    } else {
                        g.drawRect(pos.intX() - xOff, pos.intY() - yOff, s.intX(), s.intY());
                    }
                    //g.drawRect(pos.intX() - xOff, pos.intY() - yOff, s.intX(), s.intY());
                } catch (Exception e) {
                    //throw e;
                }
            }
        }
            if (drawFaces) {
            for (int i : new Range(faces.size())) {
                try {
                    if (!(Objects.isNull(a.get(faces.get(i).points[0].identifier))) && !(Objects.isNull(a.get(faces.get(i).points[1].identifier))) && !(Objects.isNull(a.get(faces.get(i).points[2].identifier)))) {
                        double x1 = 0;
                        double x2 = 0;
                        double x3 = 0;
                        double y1 = 0;
                        double y2 = 0;
                        double y3 = 0;
                        boolean draw = true;
                        try {
                            //face name = (face) (getIDMap().get(i));
                            x1 = a.get(faces.get(i).points[0].identifier).x;
                            x2 = a.get(faces.get(i).points[1].identifier).x;
                            x3 = a.get(faces.get(i).points[2].identifier).x;
                            y1 = a.get(faces.get(i).points[0].identifier).y;
                            y2 = a.get(faces.get(i).points[1].identifier).y;
                            y3 = a.get(faces.get(i).points[2].identifier).y;
                        } catch (Exception e) {
                            draw = false;
                            //throw e;
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
                            throw e;
                        }
                        g.setColor(c);

                        //g.setColor(Color.CYAN);
                        int xpoints[] = {(int)x1,(int) x2,(int) x3};
                        int ypoints[] = {(int)y1,(int) y2,(int) y3};
                        int npoints = 3;
                        if (draw) {
                            //g.fillPolygon(new Polygon(xpoints, ypoints, npoints));
                            //Rectangle r = new Rectangle();
                            if (usePixelRendering) {
                                faceColorsToDraw.add(c);
                                facesToDraw.add(new Polygon(xpoints, ypoints, npoints));
                                //gb.setColor(c);
                                //gb.fillPolygon(new Polygon(xpoints, ypoints, npoints));
                                ////drawPolygon(new Polygon(xpoints, ypoints, npoints), c);
                            } else {
                                g.fillPolygon(new Polygon(xpoints, ypoints, npoints));
                            }
                            //g.drawPolygon(xpoints, ypoints, npoints);
                            //g.drawLine(x1, y1, x2, y2);
                            drawnFaces++;
                        }
                        else{
                            errors++;
                        }
                    }
                    else{
                        //System.out.println("Points not found for index " + i);
                    }
                } catch (Exception e) {
                    throw e;
                }
            }
        }
            
        
        if (usePixelRendering) {
            gb.setColor(Color.BLACK);
            gb.fillRect(0, 0, w, h);
            for(int i : new Range(linesToDraw.size())){
                gb.setColor(lineColorsToDraw.get(i));
                Integer[][] coords = linesToDraw.get(i);
                //int x1 = coords[0][0];
                //int y1 = coords[0][1];
                //int x2 = coords[1][0];
                //int y2 = coords[1][1];
                gb.drawLine(coords[0][0], coords[0][1], coords[1][0], coords[1][1]);
            }
            for(int i : new Range(facesToDraw.size())){
                gb.setColor(faceColorsToDraw.get(i));
                gb.fillPolygon(facesToDraw.get(i));
            }
            g.drawImage(output, 0, 0, this);
        }
            
            
        Duration deltaTime = Duration.between(beginTime, Instant.now());
        g.setColor(Color.white);
        g.drawString("frame " + frame, w/10, h/7);
        g.drawString("" + errors + " errors while drawing", w/10, h/5);
        g.drawString("" + received + " faces received", w/10, h/6);
        g.drawString("" + points.size() + " Points, " + drawnLines + " Lines and " + drawnFaces + " faces drawn", w/10, h/10);
        g.drawString("" + nano + " frames per nanosecond", w - w/5, h/10);
        g.drawString("" + (int) (nano * 1000000000) + " FPS (Calc)", w - w/5, h/3);
        g.drawString("" + (int) (JFUtils.math.Conversions.toCPNS(deltaTime.getNano()) * 1000000000) + " FPS (Draw)", w - w/5, h/2);
        g.drawString("speed: " + speed + "", w - w/5, h/6);
        g.drawString("x, y, z: " + cx + ", " + cy + ", " + cz, w - w/5, h/5);
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
    LinkedList<face> faces_unsorted = new LinkedList<>();
    LinkedList<Color> faces_color = new LinkedList<>();
    LinkedList<Float> faces_dist = new LinkedList<>();
    public void updateFaces(LinkedList<face> newSet, LinkedList<Color> color, LinkedList<Float> dist){
        
        this.faces_color = color;
        this.faces_dist = dist;
        this.received = newSet.size();
        this.faces_unsorted = newSet;
        
        //Comparator comp = face.DESCENDING_COMPARATOR;
        
        //PriorityQueue<face> pr = new PriorityQueue<>();
        
        //LinkedList<face> newF = new LinkedList<>();
        
        //faces_unsorted.forEach(l -> pr.add(l));
        
        //while(!pr.isEmpty()){
        //    newF.add(pr.remove());
        //}
        //faces_unsorted = (LinkedList<face>) faces.clone();
        faces = faces_unsorted;
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
    
    public float speed = 0;
}