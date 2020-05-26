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

import codenameprojection.config.Flags;
import codenameprojection.demos.core.Launcher;
import codenameprojection.renderer.renderer;
import compressor.IO;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Elias Eskelinen
 */
public class CodeNameProjection {
    public static double minUtilsVer = 3.42;

    /**
     * @param args the command line arguments
     */
    
    private static boolean secure = false;
    public static boolean isSecure(){
        return !(!secure);
    }
    
    
    public static void main(String[] args) {
        
        /*Supervisor supervisor = new PBEngine.Supervisor(0, true, new Point2D(0, 0), param);
        Thread a = new Thread(supervisor);
        //a.start();
        /*while (!supervisor.ready) {
        try {
        Thread.sleep(10);
        } catch (InterruptedException ex) {
        Logger.getLogger(CodeNameProjection.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        //driver driver = new driver();
        //driver.run();
        new Launcher();
    }
        //supervisor.objectManager.addObject(new gameObject(new Point2D(10, 0), 1, 1, renderType.box, supervisor, supervisor.objectManager.getUsableID()));
        //supervisor.engine_gravity = new Point2D(0, 0.1);*/
        //new driver(null);
        
        
    public static void validate(){
        if(JFUtils.versionCheck.version != minUtilsVer){
            //throw new UnsupportedClassVersionError("pb3d needs jfutils " + minUtilsVer + ", current version is " + JFUtils.versionCheck.version);
            Logger.getGlobal().log(Level.WARNING, "pb3d needs jfutils {0}, current version is {1}", new Object[]{minUtilsVer, JFUtils.versionCheck.version});
        }
        String h = getHash();
        try {
            IO.writeString(h, "hash.txt");
        } catch (FileNotFoundException ex) {
        }
        System.out.println("Hashcode: " + h);
        codenameprojection.config.Flags.loadAndSet();
        
        if (Flags.secure) {
            try {
                System.out.println("Trying to download hashcode...");
                String h2 = JFUtils.web.WebUtils.readStringFromURL("https://raw.githubusercontent.com/jonnelafin/PB3D/master/hash.txt").replaceAll("\n", "");
                System.out.println("Downloaded hash: " + h2);
                if (h.equals(h2)) {
                    secure = true;
                    System.out.println("Hash validated succesfully");
                } else {
                    throw new SecurityException("HASHES DO NOT MATCH: " + h + " != " + h2);
                }
            } catch (Exception ex) {
                //System.out.println("HASH INVALID, INSECURE MODE ENABLED. ERROR: " + ex);
                Logger.getGlobal().log(Level.WARNING, "HASH INVALID, INSECURE MODE ENABLED. ERROR: {0}", ex);
            }
        } else {
            try {
                throw new SecurityException("INSECURE MODE ENABLED. Securemode disabled from config");
            } catch (SecurityException securityException) {
                //Logger.getGlobal().log(Level.WARNING, "HASH INVALID, INSECURE MODE ENABLED. ERROR: {0}", securityException);
                System.out.println(securityException);
            }
        }
    }
    public static String getHash(){
        LinkedList<Class> classes = new LinkedList<>();
        classes.add(driver.class);
        classes.add(modelParser.class);
        classes.add(renderer.class);
        classes.add(CodeNameProjection.class);
        String h = "";
        int hc = 0;
        for(Class c : classes){
            hc = hc + c.hashCode();
        }
        return hc + "";
    }

}