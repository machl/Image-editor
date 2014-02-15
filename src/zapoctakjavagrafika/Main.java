/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zapoctakjavagrafika;

/**
 * Main třída aplikace.
 * 
 * @author Lukáš Machalík
 */
public class Main {

    /**
     * Main metoda aplikace.
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AppWindow();
            }
        });
    }
}
