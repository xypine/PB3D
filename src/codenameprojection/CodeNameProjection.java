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
import JFUtils.fVector3;

/**
 *
 * @author Elias Eskelinen
 */
public class CodeNameProjection {
    public static double minUtilsVer = 2.21;

    /**
     * @param args the command line arguments
     */
    
    
    public static void main(String[] args) {
        if(JFUtils.versionCheck.version != minUtilsVer){
            throw new UnsupportedClassVersionError("cnprojection needs jfutils " + minUtilsVer);
        }

    
        new driver();
    }
    
}
class driver{
    private int xScreenCenter = 320/2;
    private int yScreenCenter = 240/2;
    private dVector3 screenPosition = new dVector3( 0, 0, 7 );
    private dVector3 screenPosition_org = screenPosition.clone();
    private dVector3 viewAngle = new dVector3( 0, 90, 90 );
    private dVector3 viewAngle_org = viewAngle.clone();
    
    private static final double DEG_TO_RAD = 0.017453292;
    private double modelScale = 10;
    
    double CT = Math.cos( DEG_TO_RAD * viewAngle.x );
    double ST = Math.sin( DEG_TO_RAD * viewAngle.x );
    double CP = Math.cos( DEG_TO_RAD * viewAngle.y );
    double SP = Math.sin( DEG_TO_RAD * viewAngle.y );
    
    
    
    public void addCube(dVector3 center, double size){
        double s = size;
        dVector3 dlu = new dVector3(center.x +s, center.y +s, center.z -s);
        dVector3 dld = new dVector3(center.x +s, center.y -s, center.z -s);
        dVector3 dru = new dVector3(center.x -s, center.y +s, center.z -s);
        dVector3 drd = new dVector3(center.x -s, center.y -s, center.z -s);
        dVector3 ulu = new dVector3(center.x +s, center.y +s, center.z +s);
        dVector3 uld = new dVector3(center.x +s, center.y -s, center.z +s);
        dVector3 uru = new dVector3(center.x -s, center.y +s, center.z +s);
        dVector3 urd = new dVector3(center.x -s, center.y -s, center.z +s);
        points.add(dlu);
        points.add(dld);
        points.add(dru);
        points.add(drd);
        points.add(ulu);
        points.add(uld);
        points.add(uru);
        points.add(urd);
        lines.add(new Integer[]{dlu.identifier, dld.identifier});
        lines.add(new Integer[]{dlu.identifier, dru.identifier});
        lines.add(new Integer[]{dld.identifier, drd.identifier});
        lines.add(new Integer[]{dru.identifier, drd.identifier});
        
        lines.add(new Integer[]{ulu.identifier, uld.identifier});
        lines.add(new Integer[]{ulu.identifier, uru.identifier});
        lines.add(new Integer[]{uld.identifier, urd.identifier});
        lines.add(new Integer[]{uru.identifier, urd.identifier});
        
        lines.add(new Integer[]{dlu.identifier, ulu.identifier});
        lines.add(new Integer[]{dld.identifier, uld.identifier});
        lines.add(new Integer[]{dru.identifier, uru.identifier});
        lines.add(new Integer[]{drd.identifier, urd.identifier});
    }
    LinkedList<dVector3> points;
    LinkedList<Integer[]> lines;
    public driver(){
        //dVector3 point = new dVector3(0, 0, 0);
        InputActivated refI = new InputActivated();
        Screen s = new Screen();
        Input inp = new Input(refI);
        inp.verbodose = true;
        s.addKeyListener(inp);
        s.addMouseListener(inp);
        points = new LinkedList<>();
        lines = new LinkedList<>();
        addCube(new dVector3(0, 0, 0), 1);
        
        double angleK = 0;
        double angleM = 0;
        double angleX = 0;
        double angleXM = 0;
        int sleep = 1;
        while(true){
            //Init
            LinkedList<dVector> set = new LinkedList<>();
            LinkedList<dVector> sizes = new LinkedList<>();
            LinkedList<dVector[]> lines_set = new LinkedList<>();
            LinkedList<dVector[]> lines_sizes = new LinkedList<>();
            
            xScreenCenter = s.r.w / 2;
            yScreenCenter = s.r.h / 2;
            
            //Check input   -0.025D*0.05
            double factor = -0.025D*0.05;
            if(inp.keys[68] == true){
                screenPosition_org.x += factor;
            }
            if(inp.keys[65] == true){
                screenPosition_org.x -= factor;
            }
            if(inp.keys[87] == true){
                screenPosition_org.y += factor;
            }
            if(inp.keys[83] == true){
                screenPosition_org.y -= factor;
            }
            if(inp.keys[81] == true){
                screenPosition_org.z -= factor*5;
            }
            if(inp.keys[69] == true){
                screenPosition_org.z += factor*5;
            }
            //c
            if(inp.keys[67] == true){
                viewAngle.z += factor*15;
            }
            //z
            if(inp.keys[90] == true){
                viewAngle.z -= factor*15;
            }
            //j
            if(inp.keys[74] == true){
                angleXM = angleXM + 0.0001D;
            }
            //l
            if(inp.keys[76] == true){
                angleXM = angleXM - 0.0001D;
            }
            //i
            if(inp.keys[73] == true){
                angleM = angleM + 0.0001D;
            }
            //k
            if(inp.keys[75] == true){
                angleM = angleM - 0.0001D;
            }
            if(inp.keys[86] == true){
                inp.verbodose = !inp.verbodose;
            }
            else{
            }
            
            screenPosition = screenPosition_org;
            screenPosition = matmul(RX((float) angleM), screenPosition_org.toFVector3()).toDVector3();
            //screenPosition.z = screenPosition_org.z;
            //Calc
            for(dVector3 i : points){
                //System.out.println("Original[" +i.hashCode() + "] :" + i);
                
                fVector3 rotated = matmul(RX((float) angleK ), i.toFVector3());
                rotated = matmul(RY((float) angleX ), rotated);
                /*rotated = matmul(RX((float) 0 ), rotated);
                rotated = matmul(RX((float) 0 ), rotated);
                rotated = matmul(RX((float) 0 ), rotated);*/
                //rotated.z = (float) i.z;
                //fVector3 rotated = i.toFVector3();
                //rotated = matmul(RZ(0), rotated);
                //rotated = matmul(RY(-0.06F), rotated);
                
                float dist = 5F;
                float z = 1 / (dist - rotated.z);
                
                dVector3 projected = new dVector3(0, 0, 0);
                /*float[][] projection = {
                {z, 0, 0},
                {0, z, 0}
                };
                dVector3 projected = new dVector3(0, 0, 0);*/
                projectPoint(rotated.toDVector3(), projected);
                /*projected = matmul(projection, rotated).toDVector3();
                projected = dVector3.multiply(projected, new dVector3(200, 200, 200));
                projected = dVector3.add(projected, new dVector3(200, 200, 20));*/
                dVector point2D = new dVector(projected.x, projected.y);
                point2D.identifier = i.identifier;
                int size = (int) (25 - (screenPosition.z - rotated.z) * 2);
                if(size < 0){
                    size = 0;
                }
                //System.out.println("Projected: " + point2);
                if(rotated.z < screenPosition.z){
                    sizes.add(new dVector(size, size));
                    set.add(point2D);
                }
            }
            /*for(Integer[] l : lines){
            //Every point must exist in both lists
            try {
            dVector new_start = set.get(points.indexOf(l[0]));
            dVector new_end = set.get(points.indexOf(l[1]));
            lines_set.add( new dVector[]{new_start, new_end} );
            lines_sizes.add(new dVector[]{ sizes.get(points.indexOf(l[0])) , sizes.get(points.indexOf(l[1]))} );
            } catch (Exception e) {
            e.printStackTrace();
            }
            }*/
            //Rendering
            s.r.updatePoints(set, sizes);
            s.r.updateLines(lines);
            //System.out.println("orighinal: ");
            //System.out.println("projected: " + point2);
            
            angleM = angleM * 0.95D;
            angleK = (float) (angleK + angleM);
            angleXM = angleXM * 0.95D;
            angleX = (float) (angleX + angleXM);
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
    public static float[][] RX (float o){
        float d = (float) o;
        float c = (float) Math.cos(d);
        float s = (float) Math.sin(d);
        float ns = -(float) Math.sin(d);
        return new float[][]{
            new float[]{1, 0, 0},
            new float[]{0, c, ns},
            new float[]{1, s, c}
        };
    }
    public static float[][] RY (float o){
        float d = (float) o;
        float c = (float) Math.cos(d);
        float s = (float) Math.sin(d);
        float ns = -(float) Math.sin(d);
        return new float[][]{
            new float[]{c, 0, ns},
            new float[]{0, 1, 0},
            new float[]{s, 0, c}
        };
    }
    public static float[][] RZ (float o){
        float d = (float) o;
        float c = (float) Math.cos(d);
        float s = (float) Math.sin(d);
        float ns = -(float) Math.sin(d);
        return new float[][]{
            new float[]{c, ns, 0},
            new float[]{s, c, 0},
            new float[]{0, 0, 1}
        };
    }
    
    //The following is copied (edited to suit JFTools) from Daniel Shiffmans code, at: https://github.com/CodingTrain/website/blob/master/CodingChallenges/CC_112_3D_Rendering/Processing/CC_112_3D_Rendering/matrix.pde#L50
    //Why? becouse i do not know how multiplication matricies work! :P
    float[][] vecToMatrix(fVector3 v) {
        float[][] m = new float[3][1];
        m[0][0] = (float) v.x;
        m[1][0] = (float) v.y;
        m[2][0] = (float) v.z;
        return m;
      }

    fVector3 matrixToVec(float[][] m) {
        fVector3 v = new fVector3(0,0,0);
        v.x = m[0][0];
        v.y = m[1][0];
        if (m.length > 2) {
          v.z = m[2][0];
        }
        return v;
      }
    fVector3 matmul(float[][] a, fVector3 b) {
        float[][] m = vecToMatrix(b);
        return matrixToVec(matmul(a,m));
    }
    float[][] matmul(float[][] a, float[][] b) {
        int colsA = a[0].length;
        int rowsA = a.length;
        int colsB = b[0].length;
        int rowsB = b.length;

        if (colsA != rowsB) {
          throw new Error("Columns of A must match rows of B");
        }

        float result[][] = new float[rowsA][colsB];

        for (int i = 0; i < rowsA; i++) {
          for (int j = 0; j < colsB; j++) {
            float sum = 0;
            for (int k = 0; k < colsA; k++) {
              sum += a[i][k] * b[k][j];
            }
            result[i][j] = sum;
          }
        }
        return result;
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