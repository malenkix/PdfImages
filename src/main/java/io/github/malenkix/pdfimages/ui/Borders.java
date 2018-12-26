package io.github.malenkix.pdfimages.ui;

import java.awt.Color;
import java.util.Arrays;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 *
 * @author Maik
 */
public final class Borders {

    private Borders() {
        //
    }

    public static EmptyBorder emptyBorder(final int top, final int left, final int bottom, final int right) {
        return new EmptyBorder(top, left, bottom, right);
    }

    public static EmptyBorder emptyBorder(final int topBottom, final int leftRight) {
        return emptyBorder(topBottom, leftRight, topBottom, leftRight);
    }

    public static EmptyBorder emptyBorder(final int inset) {
        return emptyBorder(inset, inset);
    }

    public static LineBorder line(final Color color, final int thickness) {
        return new LineBorder(color, thickness);
    }

    public static LineBorder line(final Color color) {
        return line(color, 1);
    }

    public static Border dashed(final Color color, final int thickness) {
        return BorderFactory.createDashedBorder(color, thickness, 1f, 1f, false);
    }

    public static Border dashed(final Color color) {
        return dashed(color, 1);
    }

    public static CompoundBorder compound(final Border firstBorder, final Border secondBorder, final Border... borders) {
        if (borders == null || borders.length == 0) {
            return new CompoundBorder(firstBorder, secondBorder);
        }
        return new CompoundBorder(firstBorder, compound(secondBorder, borders[0], Arrays.copyOfRange(borders, 1, borders.length)));
    }
}
