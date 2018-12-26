package io.github.malenkix.pdfimages.viewmodels;

import java.awt.image.BufferedImage;

/**
 *
 * @author Maik
 */
public abstract class PdfObject {

    private BufferedImage original;
    private BufferedImage preview;

    public BufferedImage getOriginal() {
        return original;
    }

    public boolean hasOriginal() {
        return original != null;
    }

    public boolean hasSameOriginalAs(PdfObject other) {

        if (other == null) {
            return false;
        }
        if (this == other) {
            return true;
        }
        if (!getClass().equals(other.getClass())) {
            return false;
        }

        if (original.getWidth() != other.original.getWidth() || original.getHeight() != other.original.getHeight()) {
            return false;
        }

        for (int x = 0; x < original.getWidth(); x++) {
            for (int y = 0; y < original.getHeight(); y++) {
                if (original.getRGB(x, y) != other.original.getRGB(x, y)) {
                    return false;
                }
            }
        }

        return true;
    }

    public void setOriginal(BufferedImage original) {
        this.original = original;
    }

    public BufferedImage getPreview() {
        return preview;
    }

    public boolean hasPreview() {
        return preview != null;
    }

    public void setPreview(BufferedImage preview) {
        this.preview = preview;
    }
    
    public abstract boolean isPage();

    public abstract boolean isForRemoval();
}
