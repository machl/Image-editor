/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zapoctakjavagrafika.tasks;

import java.awt.image.BufferedImage;

/**
 * Implementace algoritmu rozpoznání kombinace vertikálních i horizontálních
 * hran.
 * 
 * <p>
 * Základní algoritmus pro rozpoznání hran v obraze. Na jednotlivé pixely 
 * obrázku je použita matice (1, -1) pro rozpoznání vertikálních hran a 
 * (1, -1)T pro rozpoznání horizontálních hran. Hrany se rozpoznávají z obrázku 
 * převedeného do stupňů šedi. Vezme se černobílá hodnota aktuálního pixelu a 
 * od ní se odečte černobílá hodnota pixelu vpravo (resp. pod aktuálním pro 
 * horizontální hrany). Absolutní hodnota tohoto výrazu vyjadřuje míru 
 * pravděpodobnosti hrany (v jednom směru). Pro kombinaci rozpoznaných 
 * horizontálních hran a vertikálních hran do jednoho obrázku je využito vzorce:
 * <br /><center><img src="doc-files/vzorec1.png"/></center><br />
 * kde V je hodnota po aplikaci matice (1, -1) na obrázek, 
 * H je hodnota po aplikaci matice (1, -1)T na obrázek a 
 * C je výsledkem – odpovídá pravděpodobnosti horizontální nebo vertikální 
 * hrany v původním obrázku.<br />
 * Aby měl výsledný obrázek stejný rozměr, bere poslední sloupec a poslední 
 * řádek jako své sousední pixely (které zasahují mimo rozměr obrázku) 
 * aktuální pixel.
 * </p>
 *
 * @author Lukáš Machalík
 */
public class EdgeDetectImageTask extends ImageTask {

    /**
     * Algoritmu rozpoznání kombinace vertikálních i horizontálních hran.
     * 
     * @param original vstupní obrázek pro úpravu
     */
    public EdgeDetectImageTask(BufferedImage original) {
        super(original);
    }

    @Override
    protected final BufferedImage doInBackground() {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // hodnota aktuálně zpracovávaného pixelu
                int[] rgb = intToRgb(getRGB(x, y));
                int r = rgb[0];
                int g = rgb[1];
                int b = rgb[2];
                int gray = (int) (r + g + b) / 3;

                // hodnota pixelu vpravo od aktuálního
                rgb = intToRgb(getRGB(x + 1, y));
                r = rgb[0];
                g = rgb[1];
                b = rgb[2];
                int grayRight = (int) (r + g + b) / 3;

                // hodnota pixelu pod aktuálním
                rgb = intToRgb(getRGB(x, y + 1));
                r = rgb[0];
                g = rgb[1];
                b = rgb[2];
                int grayDown = (int) (r + g + b) / 3;

                // uplatnění matice (1, -1)
                int vertEdge = Math.abs(gray - grayRight);

                // uplatnění matice (1, -1)^T
                int horzEdge = Math.abs(gray - grayDown);

                // kombinace hodnot obou hran do jedné podle vzorce
                int combined = (int) Math.sqrt(vertEdge * vertEdge + horzEdge * horzEdge);

                image.setRGB(x, y, rgbToInt(combined));
            }
            setProgress((int) (((double) x / width) * 100));
            if (isCancelled()) {
                return result = original;
            }
        }
        setProgress(100);

        return result = image;
    }
}
