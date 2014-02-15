/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zapoctakjavagrafika.tasks;

import java.awt.image.BufferedImage;
import javax.swing.SwingWorker;

/**
 * Abstraktní třída pro použití s filtry.
 * Dědí od SwingWorker<BufferedImage, Object>.
 * Navíc skdružuje některé společné metody využívané filtry.
 * Implementuje konstruktor, metodu pro převod a získání barev z obrázku.
 * Ošetřuje také pixely mimo rozměry obrázku (pro potřeby filtrů rozpoznání hran a
 * Gaussova rozostření). Třída je předkem pro všechny třídy filtrů.
 * 
 * @author Lukáš Machalík
 */
public abstract class ImageTask extends SwingWorker<BufferedImage, Object> {

    /**
     * Původní obrázek
     */
    protected BufferedImage original;
    
    /**
     * Výsledný upravený obrázek filtrem.
     */
    protected BufferedImage result;
    
    /**
     * Rozměry původního obrázku
     */
    protected int width, height;

    /**
     * Základní konstruktor pro všechny filtry.
     * Ukládá původní obrázek pro další zpracování.
     * Jako výsledek přiřazuje původní obrázek, pro případ, kdy zpracovávání
     * filtru je zrušeno (nebo selže).
     * @param original původní obrázek
     */
    public ImageTask(BufferedImage original) {
        this.original = original;
        this.result = original;
        this.width = original.getWidth();
        this.height = original.getHeight();
    }
    
    /**
     * Vrací ARGB hodnotu pixelu na pozici x,y.
     * Pokud jsou souřadnice mimo souřadnice obrázku, vrátí ARGB hodnotu
     * existujícího pixelu nejblíže k souřadnicím x,y (některý z krajních
     * pixelů).
     * @param x souřadnice v ose x
     * @param y souřadnice v ose y
     * @return argb hodnota pixelu
     */
    protected int getRGB(int x, int y) {
        if (x < 0) {
            x = 0;
        } else if (x >= width) {
            x = width-1;
        }
        
        if (y < 0) {
            y = 0;
        } else if (y >= height) {
            y = height-1;
        }
        
        return original.getRGB(x, y);
    }
    
    /**
     * Převede jednočíselnou hodnotu ARGB pixelu na jednotlivé složky.
     * Vrací pole ve tvaru {red, green, blue}
     * @param argb ARGB hodnota
     * @return pole velikosti 3 ve tvaru {red, green, blue}
     */
    protected int[] intToRgb(int argb) {
        return new int[]{
                    (argb >> 16) & 0xff, //red
                    (argb >> 8) & 0xff, //green
                    (argb) & 0xff //blue
                };
    }

    /**
     * Převede pole ve tvaru {red, green, blue} na jednočíselnou hodnotu ARGB.
     * @param rgb pole velikosti 3 ve tvaru {red, green, blue}
     * @return ARGB hodnota
     */
    protected int rgbToInt(int[] rgb) {
        return ((rgb[0] & 0x0ff) << 16) | ((rgb[1] & 0x0ff) << 8) | (rgb[2] & 0x0ff);
    }
    
    /**
     * Převede hodnotu "úrovně šedé" na jednočíselnou hodnotu ARGB. Jako R, G a B
     * je použita právě jediná hodnota.
     * @param gray hodnota šedé pro dosazení za všechny RGB hodnoty
     * @return ARGB hodnota
     */
    protected int rgbToInt(int gray) {
        return ((gray & 0x0ff) << 16) | ((gray & 0x0ff) << 8) | (gray & 0x0ff);
    }
}
