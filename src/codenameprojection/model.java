/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package codenameprojection;

import java.util.LinkedList;

/**
 *
 * @author Jonnelafin
 */
public class model {
    LinkedList<model_frame> frames;
    boolean single_frame;
    public model(LinkedList<model_frame> frames, boolean singleFrame) {
        this.frames = frames;
        single_frame = singleFrame;
    }
    
    public model_frame getFrame(int index){
        if(!single_frame){
            return frames.get(index);
        }
        return frames.getFirst();
    }
}
