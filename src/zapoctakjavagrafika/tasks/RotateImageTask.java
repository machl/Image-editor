/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zapoctakjavagrafika.tasks;

import java.awt.image.BufferedImage;

/**
 * Implementace algoritmů pro otočení obrázku o 90° doleva i doprava, otočení o
 * 180° a vertikální i horizontální překlopení.
 * 
 * <p>
 * Přímočaré otočení nebo překlopení spočívá ve vytvoření nového obrázku 
 * procházením původního obrázku v rozdílném pořadí. Každý z typů otočení je 
 * pochopitelně naprogramován zvlášť.
 * </p>
 *
 * @author Lukáš Machalík
 */
public class RotateImageTask extends ImageTask {

    /**
     * Konstanta pro otočení o 180°.
     */
    public static final int ROTATE_180 = 0;

    /**
     * Konstanta pro otočení o 90° doleva.
     */
    public static final int ROTATE_90_LEFT = 1;

    /**
     * Konstanta pro otočení o 90° doprava.
     */
    public static final int ROTATE_90_RIGHT = 2;

    /**
     * Konstanta pro horizontální překlopení.
     */
    public static final int FLIP_HORIZONTALLY = 3;

    /**
     * Konstanta pro vertikální překlopení.
     */
    public static final int FLIP_VERTICALLY = 4;
    
    
    int rotateMode;

    /**
     * Algoritmus pro otočení obrázku o 90° doleva, doprava, nebo otočení
     * o 180°, nebo vertikální nebo horizontální překlopení.
     * Pro určení druhu otočení/překlopení použijte jednu z konstant
     * ROTATE_180, ROTATE_90_LEFT, ROTATE_90_RIGHT, FLIP_HORIZONTALLY,
     * FLIP_VERTICALLY.
     * 
     * @param original vstupní obrázek pro úpravu
     * @param rotateMode druh otočení/překlopení
     */
    public RotateImageTask(BufferedImage original, int rotateMode) {
        super(original);
        this.rotateMode = rotateMode;
    }

    @Override
    protected final BufferedImage doInBackground() {
        switch (rotateMode) {
            case ROTATE_180:
                return result = rotate180();
            case ROTATE_90_LEFT:
                return result = rotate90left();
            case ROTATE_90_RIGHT:
                return result = rotate90right();
            case FLIP_HORIZONTALLY:
                return result = flipHorizontally();
            case FLIP_VERTICALLY:
                return result = flipVertically();
            default:
                return result = original;
        }
    }

    /**
     * Otočí obrázek o 180° stupňů
     *
     * @return otočený obrázek
     */
    private BufferedImage rotate180() {
        BufferedImage afterrotate = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            int oldY = height - y - 1;
            for (int x = 0; x < width; x++) {
                int oldX = width - x - 1;
                afterrotate.setRGB(x, y, getRGB(oldX, oldY));
            }
            setProgress((int) (((double) y / height) * 100));
            if (isCancelled()) {
                return result = original;
            }
        }
        setProgress(100);
        return afterrotate;
    }

    /**
     * Otočí obrázek o 90° doleva (proti směru hodinových ručiček)
     *
     * @return otočený obrázek
     */
    private BufferedImage rotate90left() {
        BufferedImage afterrotate = new BufferedImage(height, width, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < width; y++) {
            int oldX = width - y - 1;
            for (int x = 0; x < height; x++) {
                afterrotate.setRGB(x, y, getRGB(oldX, x));
            }
            setProgress((int) (((double) y / width) * 100));
            if (isCancelled()) {
                return result = original;
            }
        }
        setProgress(100);
        return afterrotate;
    }

    /**
     * Otočí obrázek o 90° doprava (po směru hodinových ručiček)
     *
     * @return otočený obrázek
     */
    private BufferedImage rotate90right() {
        BufferedImage afterrotate = new BufferedImage(height, width, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < width; y++) {
            for (int x = 0; x < height; x++) {
                int oldY = height - x - 1;
                afterrotate.setRGB(x, y, getRGB(y, oldY));
            }
            setProgress((int) (((double) y / width) * 100));
            if (isCancelled()) {
                return result = original;
            }
        }
        setProgress(100);
        return afterrotate;
    }

    /**
     * Překlopí obrázek horizontálně
     *
     * @return překlopený obrázek
     */
    private BufferedImage flipHorizontally() {
        BufferedImage afterflip = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            int oldY = height - y - 1;
            for (int x = 0; x < width; x++) {
                afterflip.setRGB(x, y, getRGB(x, oldY));
            }
            setProgress((int) (((double) y / height) * 100));
            if (isCancelled()) {
                return result = original;
            }
        }
        setProgress(100);
        return afterflip;
    }

    /**
     * Překlopí obrázek vertikálně
     *
     * @return překlopený obrázek
     */
    private BufferedImage flipVertically() {
        BufferedImage afterflip = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int oldX = width - x - 1;
                afterflip.setRGB(x, y, getRGB(oldX, y));
            }
            setProgress((int) (((double) y / height) * 100));
            if (isCancelled()) {
                return result = original;
            }
        }
        setProgress(100);
        return afterflip;
    }
}
