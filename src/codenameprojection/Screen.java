

package codenameprojection;

import javax.swing.JFrame;

/**
 *
 * @author Jonnelafin
 */
public class Screen extends JFrame{
    public renderer r;
    
    
    
    public Screen(){
        //Create the renderer
        r = new renderer(this);
        
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
