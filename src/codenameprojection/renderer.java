/*
 * The MIT License
 *
 * Copyright 2019 Elias Eskelinen.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package codenameprojection;

import JFUtils.Range;
import JFUtils.dirs;
import JFUtils.point.Point2D;
import JFUtils.point.Point2Int;
import JFUtils.quickTools;
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
import java.awt.TexturePaint;
import java.awt.Transparency;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        try {
            this.base = ImageIO.read(new File(new dirs().textures + "/walls/walls0.png"));
        } catch (MalformedURLException ex) {
            Logger.getLogger(renderer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(renderer.class.getName()).log(Level.SEVERE, null, ex);
        }
        //output = new BufferedImage(parent.getWidth(), parent.getWidth(), BufferedImage.TYPE_INT_RGB);
        setIgnoreRepaint(true);
    }
    
    public float nano = 0;
    private LinkedList<Point2D> points = new LinkedList<>();
    private LinkedList<Integer> pointsToHide = new LinkedList<>();
    private LinkedList<Point2D> points_sizes = new LinkedList<>();
    
    private LinkedList<Integer[]> lines = new LinkedList<>();
    private LinkedList<Color> lines_color = new LinkedList<>();
    private HashMap<Integer, BufferedImage> images = new HashMap<>();
    
    BufferedImage base;
    public int w;
    public int h;
    public int drawnLines;
    public int drawnFaces;
    public int frame;
    
    public boolean drawPoints = true;
    public boolean shading = true;
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
    
    quickTools qT = new quickTools();
    
    @Override
    public void paintComponent(Graphics gd) {
        Graphics2D g = (Graphics2D) gd;
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
                Point2D pos = points.get(i);
                if (!pointsToHide.contains(pos.identifier)) {
                    try {
                        //int value2 = new Random().nextInt(255) / 2;
                        //int value3 = new Random().nextInt(255) / 2;
                        //g.setColor(new Color(value2, value, value3));
                        
                        Point2D s = points_sizes.get(i);
                        
                        int cS = (int) (25 - (s.x * 2));
                        //cS = (int) (cS * 1000);
                        //        if(cS > 255){
                        //            cS = 255;
                        //        }
                        if (cS < 0) {
                            cS = 0;
                        }

                        //g.setColor(new Color(cS, cS, cS));
                        int xOff = cS / 2;
                        int yOff = cS / 2;
                        if (usePixelRendering) {
                            //drawPoint(pos, Color.red);
                            gb.drawRect(pos.intX() - xOff, pos.intY() - yOff, cS, cS);
                        } else {
                            g.drawRect(pos.intX() - xOff, pos.intY() - yOff, cS, cS);
                        }
                        //g.drawRect(pos.intX() - xOff, pos.intY() - yOff, s.intX(), s.intY());
                    } catch (Exception e) {
                        //throw e;
                    }
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
                        Point2D o = new Point2D(0, 0);
                        Point2D t = new Point2D(0, 0);
                        Point2D r = new Point2D(0, 0);
                        int oi = 0;
                        int ti = 0;
                        int ri = 0;
                        boolean draw = true;
                        try {
                            //face name = (face) (getIDMap().get(i));
                            oi = faces.get(i).points[0].identifier;
                            ti = faces.get(i).points[1].identifier;
                            ri = faces.get(i).points[2].identifier;
                            o = a.get(oi);
                            t = a.get(ti);
                            r = a.get(ri);
                            x1 = o.x;
                            x2 = t.x;
                            x3 = r.x;
                            y1 = o.y;
                            y2 = t.y;
                            y3 = r.y;
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
                                //IMAGES???
                                float wb = 0;
                                int wc = 0;
                                for(int x : xpoints){
                                    if(x < w && x > 0){
                                        wb = wb + x;
                                        wc++;
                                    }
                                }
                                wb = wb / wc;
                                float hb = 0;
                                int hc = 0;
                                for(int y : ypoints){
                                    if(y < h && y > 0){
                                        hb = hb + y;
                                        hc++;
                                    }
                                }
                                hb = hb / hc;
                                //TexturePaint tex = new TexturePaint(base, new Rectangle2D.Double(wb, hb, base.getWidth(), base.getHeight()));
                                //gb.setPaint(tex);
                                //gb.fillPolygon(facesToDraw.get(i));
                                //g.setClip(new Polygon(xpoints, ypoints, npoints));
                                //BufferedImage shaded = qT.colorImage(base, c.getRed(), c.getGreen(), c.getBlue(), 1);
                                
                                int multiplier = 4;
                                
                                LinkedList<Integer> dists = new LinkedList<Integer>();
                                try {
                                    int d1 = (int) points_sizes.get(points.indexOf(o)).x;dists.add(d1);
                                    int d2 = (int) points_sizes.get(points.indexOf(t)).x;dists.add(d2);
                                    int d3 = (int) points_sizes.get(points.indexOf(r)).x;dists.add(d3);
                                    
                                } catch (Exception e) {
                                    errors++;
                                }
                                int sum = 0;
                                for(Integer dist : dists){
                                    sum = sum + dist;
                                }
                                try {
                                    sum = sum / dists.size();
                                    multiplier = 4-sum;
                                } catch (Exception e) {
                                }
                                TexturePaint tex = new TexturePaint(base, new Rectangle2D.Double(wb, hb, base.getWidth()*multiplier, base.getHeight()*multiplier));
                                //g.setPaint(tex);
                                //g.fillPolygon(new Polygon(xpoints, ypoints, npoints));
                                //g.drawImage(base, w/2-(int)wb, h/2-(int)hb, w, h, this);
                                //g.setClip(0, 0, w, h);
                                int middle = 255 / 2;
                                Color c2 = c;
                                int res = 7;
                                int rc = (int)(c.getRed() / res) * res;
                                int gc = (int)(c.getGreen() / res) * res;
                                int bc = (int)(c.getBlue() / res) * res;
                                
                                
                                
                                if(rc < 0){
                                    rc = 0;
                                }
                                if(gc < 0){
                                    gc = 0;
                                }
                                if(bc < 0){
                                    bc = 0;
                                }
                                c2 = new Color(rc, gc, bc);
                                g.setColor(c2);
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
            if(!Objects.isNull(gb)){
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
                    float wb = 0;
                    for(int x : facesToDraw.get(i).xpoints){
                        wb = wb + x;
                    }
                    wb = wb / facesToDraw.get(i).xpoints.length;
                    float hb = 0;
                    for(int y : facesToDraw.get(i).ypoints){
                        hb = hb + y;
                    }
                    hb = hb / facesToDraw.get(i).ypoints.length;
                    TexturePaint tex = new TexturePaint(base, new Rectangle2D.Double(wb, hb, base.getWidth(), base.getHeight()));
                    gb.setPaint(tex);
                    gb.fillPolygon(facesToDraw.get(i));
                    Color c = faceColorsToDraw.get(i);
                    c = new Color(c.getRed(), c.getGreen(), c.getBlue(), 133);
                    gb.setColor(c);
                    gb.fillPolygon(facesToDraw.get(i));
                }
                g.drawImage(output, 0, 0, this);
            }
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
    public void updatePoints(LinkedList<Point2D> newSet, LinkedList<Point2D> newSizes, LinkedList<Integer> pointsToHide){
        this.points = newSet;
        this.points_sizes = newSizes;
        this.pointsToHide = pointsToHide;
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