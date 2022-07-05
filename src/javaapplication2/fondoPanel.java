
package javaapplication2;


import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author Usuario
 */
public class fondoPanel extends JPanel{
    private Image imagen;
    String dir;
    public fondoPanel(String dir) {
        this.dir=dir;
    }
    
    @Override
    public void paint (Graphics g){
    imagen=new ImageIcon(getClass().getResource(dir)).getImage();
    g.drawImage(imagen,0,0,getWidth(),getHeight(),this);
        setOpaque(false);
        super.paint(g);
    }
    
}
