package io.github.malenkix.pdfimages.viewmodels;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 *
 * @author Maik
 */
public class PdfImage extends PdfObject {

    private final PdfPage page;

    private boolean blank;
    private Color color;

    public PdfImage(PdfPage page) {
        this.page = page;
    }

    public PdfPage getPage() {
        return page;
    }

    public boolean isBlank() {
        return blank;
    }

    public void setBlank(boolean blank) {
        this.blank = blank;
    }

    public Color getColor() {
        return color;
    }

    public boolean hasColor() {
        return color != null;
    }

    public boolean isWhite() {
        return Color.WHITE.equals(color);
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean hasUpdate() {
        return isBlank() || hasColor();
    }

    public BufferedImage getUpdated() {

        final BufferedImage updated;

        if (!hasOriginal()) {
            updated = null;
        } else if (blank) {
            updated = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
            updated.setRGB(0, 0, new Color(255, 255, 255, 0).getRGB());
        } else if (color != null) {
            final int width = getOriginal().getWidth();
            final int height = getOriginal().getHeight();
            final int rgb = color.getRGB();
            updated = new BufferedImage(width, height, getOriginal().getType());
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    updated.setRGB(x, y, rgb);
                }
            }
        } else {
            updated = null;
        }

        return updated;
    }

    @Override
    public boolean isPage() {
        return false;
    }

    @Override
    public boolean isForRemoval() {
        return page.isForRemoval();
    }

}
