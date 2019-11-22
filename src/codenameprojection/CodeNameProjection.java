/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codenameprojection;

import JFUtils.Input;
import JFUtils.InputActivated;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import JFUtils.dVector;
import JFUtils.dVector3;

/**
 *
 * @author Elias Eskelinen
 */
public class CodeNameProjection {
    public static double minUtilsVer = 2.022;

    /**
     * @param args the command line arguments
     */
    
    
    public static void main(String[] args) {
        if(JFUtils.versionCheck.version < minUtilsVer){
            throw new UnsupportedClassVersionError("cnprojection needs a jfutils version greater than " + minUtilsVer);
        }

    
        new driver();
    }
    
}
class driver{
    private int xScreenCenter = 320/2;
    private int yScreenCenter = 240/2;
    private dVector3 screenPosition = new dVector3( 0, 0, 20 );
    private dVector3 viewAngle = new dVector3( 0, 90, 180 );
    
    private static final double DEG_TO_RAD = 0.017453292;
    private double modelScale = 10;
    
    double CT = Math.cos( DEG_TO_RAD * viewAngle.x );
    double ST = Math.sin( DEG_TO_RAD * viewAngle.x );
    double CP = Math.cos( DEG_TO_RAD * viewAngle.y );
    double SP = Math.sin( DEG_TO_RAD * viewAngle.y );

    public driver(){
        //dVector3 point = new dVector3(0, 0, 0);
        InputActivated refI = new InputActivated();
        Screen s = new Screen();
        Input inp = new Input(refI);
        inp.verbodose = true;
        s.addKeyListener(inp);
        s.addMouseListener(inp);
        LinkedList<dVector3> points = new LinkedList<>();
        LinkedList<dVector3[]> lines = new LinkedList<>();
        final dVector3 point1 = new dVector3(0, 0, 0);
        final dVector3 point22 = new dVector3(0, 1, -10);
        final dVector3 point23 = new dVector3(0, -1, -10);
        final dVector3 point33 = new dVector3(1, 0, 5);
        final dVector3 point34 = new dVector3(-1, 0, 5);
        final dVector3 point44 = new dVector3(1, 0, -10);
        final dVector3 point45 = new dVector3(-1, 0, -10);
        final dVector3[] line1 = new dVector3[]{point33, point23};
        points.add(point1);
        points.add(point22);
        points.add(point23);
        points.add(point33);
        points.add(point34);
        points.add(point44);
        points.add(point45);
        lines.add(line1);
        
        int sleep = 200;
        while(true){
            //Init
            LinkedList<dVector> set = new LinkedList<>();
            LinkedList<dVector> sizes = new LinkedList<>();
            LinkedList<dVector[]> lines_set = new LinkedList<>();
            LinkedList<dVector[]> lines_sizes = new LinkedList<>();
            
            xScreenCenter = s.r.w / 2;
            yScreenCenter = s.r.h / 2;
            
            //Check input
            double factor = -0.5D;
            if(inp.keys[68] == true){
                screenPosition.x += factor;
            }
            if(inp.keys[65] == true){
                screenPosition.x -= factor;
            }
            if(inp.keys[87] == true){
                screenPosition.y += factor;
            }
            if(inp.keys[83] == true){
                screenPosition.y -= factor;
            }
            else{
            }
            
            //Calc
            for(dVector3 i : points){
                //System.out.println("Original[" +i.hashCode() + "] :" + i);
                dVector3 point2 = new dVector3(0, 0, 0);
                projectPoint(i, point2);
                dVector point2D = new dVector(point2.x, point2.y);
                int size = (int) (1 + i.z);
                if(size < 0){
                    size = 0;
                }
                //System.out.println("Projected: " + point2);
                sizes.add(new dVector(size, size));
                set.add(point2D);
            }
            for(dVector3[] l : lines){
                //Every point must exist in both lists
                try {
                    dVector new_start = set.get(points.indexOf(l[0]));
                    dVector new_end = set.get(points.indexOf(l[1]));
                    lines_set.add( new dVector[]{new_start, new_end} );
                    lines_sizes.add(new dVector[]{ sizes.get(points.indexOf(l[0])) , sizes.get(points.indexOf(l[1]))} );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //Rendering
            s.r.updatePoints(set, sizes);
            //s.r.updateLines(lines_set, lines_sizes);
            //System.out.println("orighinal: ");
            //System.out.println("projected: " + point2);
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException ex) {
                Logger.getLogger(CodeNameProjection.class.getName()).log(Level.SEVERE, null, ex);
            }
            //point.x = point.x + 0.1;
            //point.z--;
            //points.get(0).x = points.get(0).x + 0.1;
            //points.get(0).z--;
        }
    }
    
    public void projectPoint( dVector3 input, dVector3 output )
    {
     double x = screenPosition.x + input.x * CT - input.y * ST;
     double y = screenPosition.y + input.x * ST * SP + input.y * CT * SP
       + input.z * CP;
     double temp = viewAngle.z / (screenPosition.z + input.x * ST * CP
       + input.y * CT * CP - input.z * SP );

     output.x = xScreenCenter + modelScale * temp * x;
     output.y = yScreenCenter - modelScale * temp * y;
     output.z = 0;
    }
}
/*class driver{
public double x = 20;
public double y = 20;
public double z = 0;
public driver(){
// TODO code application logic here
// convert to screen space
Screen screen = new Screen();
dVector P_screen = new dVector(0, 0);
dVector3 P_camera = new dVector3(0, 0, 20);
double near = -2;
double r = 100; //Right side of the screen
double l = 0; //Left side of the screen
double t = 0; //Top of the screen
double b = 100; //Bottom of the screen







//Lets do this!!!

int sleep = 200;

while(true){
//Init
LinkedList<dVector> set = new LinkedList<>();
dVector3 newVertex = new dVector3(x, y, z);
System.out.println("Original: "+newVertex);

//Translate
newVertex.x /= (newVertex.z + 100D) * 0.01D;
newVertex.y /= (newVertex.z + 100D) * 0.01D;
System.out.println("Translated: "+newVertex);
//Convert to pixels
newVertex.x *= (screen.r.w/80D);
newVertex.y *= (screen.r.h/80D);
System.out.println("Pixelated: "+newVertex);
//Zero-ify
newVertex.x += screen.r.w/2D;
newVertex.y += screen.r.h/2D;
System.out.println("Zero'd: "+newVertex);
//test?
System.out.println("Final: "+newVertex);

//Render
set.add(new dVector(x, y));
screen.r.updatePoints(set);
try {
Thread.sleep(sleep);
} catch (InterruptedException ex) {
Logger.getLogger(CodeNameProjection.class.getName()).log(Level.SEVERE, null, ex);
}
x = x + 1;
}


}
}*/