/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package codenameprojection.fly;

import codenameprojection.model;
import codenameprojection.model_frame;
import java.util.LinkedList;

/**
 *
 * @author Jonnelafin
 */
public class cyclone extends model{

    public cyclone(LinkedList<model_frame> frames, boolean singleFrame) {
        super(frames, singleFrame);
    }

    @Override
    public void executeLogic() {
        super.executeLogic(); //To change body of generated methods, choose Tools | Templates.
        x--;
    }

    
}
