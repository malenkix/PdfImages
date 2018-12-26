package io.github.malenkix.pdfimages;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * @author Maik
 */
public class PdfModel {

    public static final int PAGE = 0;

    private final int type;

    private PdfModel parent;
    private final ArrayList<PdfModel> models = new ArrayList<>();

    private BufferedImage original;
    private BufferedImage preview;

    private boolean forRemoval;
    private boolean blank;
    private Color color;

    public PdfModel(int type) {
        this.type = type;
    }

    public PdfModel() {
        this(-1);
    }

    public boolean isPage() {
        return type == PAGE;
    }

    public PdfModel getParent() {
        return parent;
    }

    public void setParent(PdfModel parent) {
        this.parent = parent;
    }

    public PdfModel getModel(int index) {
        return models.get(index);
    }

    public void addModel(PdfModel model) {
        models.add(model);
    }

    public BufferedImage getOriginal() {
        return original;
    }

    public void setOriginal(BufferedImage original) {
        this.original = original;
    }

    public BufferedImage getPreview() {
        return preview;
    }

    public boolean hasSamePreviewAs(PdfModel other) {

        if (other == null) {
            return false;
        }
        if (this == other) {
            return true;
        }

        if (type != other.type) {
            return false;
        }
        if (preview.getWidth() != other.preview.getWidth() || preview.getHeight() != other.preview.getHeight()) {
            return false;
        }

        for (int x = 0; x < preview.getWidth(); x++) {
            for (int y = 0; y < preview.getHeight(); y++) {
                if (preview.getRGB(x, y) != other.preview.getRGB(x, y)) {
                    return false;
                }
            }
        }

        return true;
    }

    public void setPreview(BufferedImage preview) {
        this.preview = preview;
    }

    public boolean isForRemoval() {
        return forRemoval;
    }

    public void setForRemoval(boolean forRemoval) {
        this.forRemoval = forRemoval;
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

    public void setColor(Color color) {
        this.color = color;
    }

    public BufferedImage getReplacement() {
        if (blank) {
            final BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
            image.setRGB(0, 0, new Color(255, 255, 255, 0).getRGB());
            return image;
        } else if (color != null) {
            final BufferedImage image = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    image.setRGB(x, y, color.getRGB());
                }
            }
            return image;
        }
        return null;
    }

}
