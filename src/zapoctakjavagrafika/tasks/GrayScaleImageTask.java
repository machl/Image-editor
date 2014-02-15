/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zapoctakjavagrafika.tasks;

import java.awt.image.BufferedImage;

/**
 * Implementace algoritmu na převod obrázku do černobílé (do stupňů šedi).
 * 
 * <p>
 * Přímočarý algoritmus na převod obrázku do černobílé, na rozdíl od sečtení 
 * všech složek RGB a vydělení 3 beru v potaz jas jednotlivých složek a sečtu 
 * je pomocí standartních vah. Zůstane tak zachován jas původního obrázku.<br />
 * Algoritmus tedy projde všechny pixely obrázku a pro každý spočítá úroveň 
 * šedé pomocí vzorce:<br />
 * <center>G = 0.299 * R + 0.587 * G + 0.114 * B</center><br />
 * kde R, G a B jsou jednotlivé hodnoty (0-255) složek barvy obrázku a G je 
 * výsledná úroveň šedé (0-255). Výsledky ukládá do nové instance BufferedImage.
 * </p>
 *
 * @author Lukáš Machalík
 */
public class GrayScaleImageTask extends ImageTask {

    /**
     * Algoritmus na převod obrázku do černobílé (do stupňů šedi).
     * 
     * @param original vstupní obrázek pro úpravu
     */
    public GrayScaleImageTask(BufferedImage original) {
        super(original);
    }

    @Override
    protected final BufferedImage doInBackground() {
        BufferedImage afterConvert = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int[] rgb = intToRgb(getRGB(x, y));
                int r = rgb[0];
                int g = rgb[1];
                int b = rgb[2];

                // Vypočítání úrovně stupňů šedi s použitím poměrů jednotlivých
                // složek barvy pro zachování jasu původního obrázku
                int gray = (int) (.299 * r + .587 * g + .114 * b);

                afterConvert.setRGB(x, y, rgbToInt(gray));
            }
            setProgress((int) (((double) x / width) * 100));
            if (isCancelled()) {
                return result = original;
            }
        }
        setProgress(100);

        return result = afterConvert;
    }
}
