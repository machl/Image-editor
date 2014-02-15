/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zapoctakjavagrafika.tasks;

import java.awt.image.BufferedImage;

/**
 * Implementace algoritmu Ditheringu pro převod do N stupňů šedi (2-256).
 * 
 * <p>
 * Pokročilý algoritmus pro převod do zvoleného počtu N stupňů šedi (2-256). 
 * Dithering používá optickou iluzi, která způsobuje, že obrázek má více barev 
 * než skutečně má. Algoritmus, který jsem použil, používá mechanismus chyby 
 * rozptylu (error-diffusion mechanism) v jednodimenzionální rozptylu, který 
 * uplatňuje chyby převodu zleva doprava.<br>
 * Například při N=4 budou ve výsledku využity pouze pixely barvy černá, světle 
 * šedá, tmavě šedá a černá. Z dálky pak bude vypadat kombinace těchto barev 
 * mnohem ostřeji, než kdybychom použili přímočarý algoritmus bez Ditheringu 
 * pro převedení do 4 stupňů šedi.
 * </p>
 * 
 * @author Lukáš Machalík
 */
public class DitherImageTask extends ImageTask {

    private final int numOfShades;

    /**
     * Algoritmus pro převod do zvoleného počtu N stupňů šedi (2-256).
     * @param original vstupní obrázek pro úpravu
     * @param numOfShades pošet stupňů šedi (2-256)
     */
    public DitherImageTask(BufferedImage original, int numOfShades) {
        super(original);
        this.numOfShades = numOfShades;
    }

    @Override
    protected final BufferedImage doInBackground() {
        // nový obrázek pro úkládání výsledných dat
        BufferedImage afterDither = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // hodnota pro převedení stupňů šedi v intervalu (0-255) do určité podmnožiny hodnot
        float conversionFactor = ((float) 255 / (numOfShades - 1));

        // tato hodnota uchovává pohybující se odchylku převodu, což umožňuje dělat Dithering
        int errorValue = 0;

        for (int y = 0; y < height; y++) {    // po řádcích
            for (int x = 0; x < width; x++) { // zleva doprava
                // získání RGB hodnot původního pixelu
                int[] rgb = intToRgb(getRGB(x, y));
                int r = rgb[0];
                int g = rgb[1];
                int b = rgb[2];

                // výpočet standartní úrovně šedé
                int gray = (r + g + b) / 3;
                int grayTempCalculation = gray;

                // přičtení hodnoty odchylky (pohybující se rozdíl aktuální
                // hodnoty šedé a hodnot již zpracovaných) k současné hodnotě šedé
                grayTempCalculation = grayTempCalculation + errorValue;

                // upravení dočasné šedé pomocí "shade reduction" vzorce
                grayTempCalculation = (int) (((int) (((float) grayTempCalculation) / conversionFactor + 0.5)) * conversionFactor);
                grayTempCalculation = (int) (Math.round(((float) grayTempCalculation) / conversionFactor) * conversionFactor);

                // změna odchylky zahrnutím posledního výpočtu
                errorValue = (int) (gray + errorValue - grayTempCalculation);

                // převod do intervalu (0-255)
                if (grayTempCalculation < 0) {
                    grayTempCalculation = 0;
                }
                if (grayTempCalculation > 255) {
                    grayTempCalculation = 255;
                }

                afterDither.setRGB(x, y, rgbToInt(grayTempCalculation));
            }
            // pro každý řádek nastavíme počáteční odhylku na 0
            errorValue = 0;

            setProgress((int) (((double) y / height) * 100));
            if (isCancelled()) {
                return result = original;
            }
        }
        setProgress(100);

        return result = afterDither;
    }
}
