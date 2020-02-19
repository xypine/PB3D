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

import java.util.HashMap;

/**
 *
 * @author Elias Eskelinen
 */
public class CodeNameProjection {
    public static double minUtilsVer = 2.58;

    /**
     * @param args the command line arguments
     */
    
    
    public static void main(String[] args) {
        if(JFUtils.versionCheck.version != minUtilsVer){
            throw new UnsupportedClassVersionError("cnprojection needs jfutils " + minUtilsVer + ", current version is " + JFUtils.versionCheck.version);
        }
        
        HashMap<String, String> param = new HashMap<>();
        param.put("nowindows", "");
        /*Supervisor supervisor = new PBEngine.Supervisor(0, true, new Point2D(0, 0), param);
        Thread a = new Thread(supervisor);
        //a.start();
        /*while (!supervisor.ready) {
        try {
        Thread.sleep(10);
        } catch (InterruptedException ex) {
        Logger.getLogger(CodeNameProjection.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        driver driver = new driver(null);
        driver.run();
        }
        //supervisor.objectManager.addObject(new gameObject(new Point2D(10, 0), 1, 1, renderType.box, supervisor, supervisor.objectManager.getUsableID()));
        //supervisor.engine_gravity = new Point2D(0, 0.1);*/
        //new driver(null);
        
        
    }
    

