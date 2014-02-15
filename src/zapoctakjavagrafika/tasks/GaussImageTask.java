/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zapoctakjavagrafika.tasks;

import java.awt.image.BufferedImage;

/**
 * Implementace algoritmů pro Gaussian blur.
 * 
 * <p>
 * Pokročilý algoritmus pro rozmazání (rozostření) obrázku. Základní myšlenkou 
 * je, že hodnota každého pixelu je nahrazena hodnotou váženého průměru 
 * okolních pixelů. Váha každého pixelu je větší, čím blíže je k pixelu právě 
 * zpracovávanému. Váhu získáme Gaussovou funkcí (pro dimenzi 2):
 * <br /><center><img src="doc-files/vzorec2.png"/></center><br />
 * kde σ (malá sigma) je faktor rozmazání (čím je větší, tím je obrázek 
 * rozmazanější) definován jako hodnota směrodatné odchylky, e je Eulerovo 
 * číslo a x, y jsou vzdálenosti od právě zpracovávaného pixelu (středu). 
 * Pomocí Gaussovy funkce spočítáme matici vah (Gaussian kernel), jejichž 
 * prostřední prvek odpovídá váze právě zpracovávaného pixelu. Rozměr takové 
 * matice (kernel size) také určuje, jaké maximální hodnoty může nabývat x a y. 
 * Matici vah dále upravíme tak, aby její suma (součet všech prvků) byla 1 – 
 * vydělení celé matice její sumou. Matici vah poté můžeme aplikovat na všechny 
 * barevné složky každého pixelu obrázku.<br />
 * Jako rohové (a krajní) pixely, u kterých matice vah požaduje hodnoty 
 * pixelů i mimo obrázek, jsou použity pixely nejblíže (okrajové) 
 * k požadovanému pixelu.
 * </p>
 * 
 * @author Lukáš Machalík
 */
public class GaussImageTask extends ImageTask {

    private final int kernelSize;
    
    /**
     * Algoritmus pro rozmazání obrázku pomocí Gaussian bluru.
     * Čím větší rozměr matice (kernel size) je, tím je obrázek rozmazanější.
     * 
     * @param original vstupní obrázek pro úpravu
     * @param kernelSize rozměr matice (kernel size)
     */
    public GaussImageTask(BufferedImage original, int kernelSize) {
        super(original);
        this.kernelSize = kernelSize;
    }
    
    @Override
    protected final BufferedImage doInBackground() {
        // určení hodnoty sigma pro vzorec (odhad hodnoty)
        // počítání přesné směrodatné odchylky je výkonově příliš složité
        final float sigma = ((float)kernelSize)/6;
        
        // výpočet čtvercové matice vah o zadaném rozměru
        float[][] kernel = new float[kernelSize][kernelSize];
        final int center = kernelSize/2;
        final double prvniZlomek = 1.0/(2*Math.PI*(sigma*sigma));
        final double druhyZlomekJmenovatel = -(2*(sigma*sigma));
        float sum = 0;
        for (int i = 0; i < kernelSize; i++) {
            for (int j = 0; j < kernelSize; j++) {
                int x = Math.abs(center-j);
                int y = Math.abs(center-i);
                float v = (float) (prvniZlomek * Math.exp((x*x + y*y)/druhyZlomekJmenovatel)); // vzorec
                kernel[i][j] = v;
                sum += v;
            }
        }
        // úprava matice vah tak, aby součet všech prvků matice byl 1
        float normalizator = 1/sum;
        for (int i = 0; i < kernelSize; i++) {
            for (int j = 0; j < kernelSize; j++) {
                kernel[i][j] = normalizator * kernel[i][j];
            }
        }
        
        // vytvoření nového obrázku
        BufferedImage afterblur = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // aplikace matice vah na každý pixel původního obrázku
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                
                // součet pro jednotlivé složky
                float rSum = 0; 
                float gSum = 0;
                float bSum = 0;
                
                // aplikace matice vah na pixel
                for (int i = 0; i < kernelSize; i++) {
                    int ry = y - center + i;
                    for (int j = 0; j < kernelSize; j++) {
                        int rx = x - center + j;
                        int[] rgb = intToRgb(getRGB(rx, ry));
                        rSum += ((float)rgb[0]) * kernel[i][j];
                        gSum += ((float)rgb[1]) * kernel[i][j];
                        bSum += ((float)rgb[2]) * kernel[i][j];
                        //sum += getPixel(y - center + i, x - center + j) * kernel[i][j];
                        
                    }
                }
                
                int[] argb = {(int)rSum, (int)gSum, (int)bSum};
                
                afterblur.setRGB(x, y, rgbToInt(argb));
            }
            setProgress((int)(((double)y / height) * 100));
            if (isCancelled()) {
                return result = original;
            }
        }
        setProgress(100);
        
        return result = afterblur;
    }
}
