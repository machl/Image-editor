/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zapoctakjavagrafika;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;
import javax.swing.JComponent;

/**
 * JPanel upravený pro účely zobrazování obrázku v okně. Obrázek je umístěn na střed.
 * @author Lukáš Machalík
 */
public class ImagePanel extends JPanel {

    /**
     * Obrázek pro zobrazení.
     */
    private Image image;
    
    /**
     * Změní obrázek na pozadí panelu.
     * @param imageToShow nový obrázek
     */
    public void setImage(Image imageToShow) {
        this.image = imageToShow;
        Dimension size = new Dimension(imageToShow.getWidth(null), imageToShow.getHeight(null));
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setSize(size);
        setLayout(null);
        //repaint();
    }

    /**
     * Vykreslí obrázek na střed.
     * 
     * @see javax.swing.JComponent
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent(Graphics g) {
        if (image != null) {
            // umisťuje obrázek na střed
            g.drawImage(image, getWidth()/2 - image.getWidth(null)/2,
                    getHeight()/2 - image.getHeight(null)/2, null);
        }
    }
}