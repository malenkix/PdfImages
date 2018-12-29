package io.github.malenkix.pdfimages.viewmodels;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author Maik
 */
public class PdfImage extends PdfObject {

    private final PdfPage page;

    private boolean blank;
    private Color color;
    private boolean border;
    private boolean ellipsis;

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

    public boolean hasBorder() {
        return border;
    }

    public void setBorder(boolean border) {
        this.border = border;
    }

    public boolean hasEllipsis() {
        return ellipsis;
    }

    public void setEllipsis(boolean ellipsis) {
        this.ellipsis = ellipsis;
    }

    public boolean hasUpdate() {
        return isBlank() || hasColor() || hasBorder() || hasEllipsis();
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
        } else if (border) {
            final int width = getOriginal().getWidth();
            final int height = getOriginal().getHeight();
            final int rgb = new Color(255, 255, 255, 0).getRGB();
            updated = new BufferedImage(width, height, getOriginal().getType());
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    updated.setRGB(x, y, rgb);
                }
            }
            final Graphics g = updated.getGraphics();
            g.setColor(Color.BLACK);
            g.drawRect(0, 0, width - 1, height - 1);
            g.drawRect(1, 1, width - 2, height - 2);
        } else if (ellipsis) {
            final int width = getOriginal().getWidth();
            final int height = getOriginal().getHeight();
            final int rgb = new Color(255, 255, 255, 0).getRGB();
            updated = new BufferedImage(width, height, getOriginal().getType());
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    updated.setRGB(x, y, rgb);
                }
            }
            final Graphics g = updated.getGraphics();
            g.setColor(Color.BLACK);
            g.fillOval(0, 0, width, height);
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
