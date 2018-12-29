package io.github.malenkix.pdfimages.ui;

import io.github.malenkix.pdfimages.viewmodels.PdfImage;
import io.github.malenkix.pdfimages.viewmodels.PdfObject;
import io.github.malenkix.pdfimages.viewmodels.PdfPage;
import io.github.malenkix.swing.Borders;
import java.awt.Color;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 *
 * @author Maik
 */
public class PdfListCellRenderer implements ListCellRenderer<PdfObject> {

    private final DefaultListCellRenderer renderer = new DefaultListCellRenderer();

    @Override
    public Component getListCellRendererComponent(JList<? extends PdfObject> list, PdfObject value, int index, boolean selected, boolean hasFocus) {
        final JLabel label = getDefaultListCellRendererReset(list, index, selected, hasFocus);
        if (value != null) {

            if (value.hasPreview()) {
                label.setIcon(new ImageIcon(value.getPreview()));
            }

            final boolean currentPage = belongsToPage(list, value);
            final boolean forRemoval = value instanceof PdfPage ? ((PdfPage) value).isForRemoval() : ((PdfImage) value).getPage().isForRemoval();
            final boolean edited = value instanceof PdfImage ? ((PdfImage) value).hasUpdate() : false;

            if (currentPage) {
                label.setBackground(Color.GRAY.brighter());
            }

            if (forRemoval && selected) {
                label.setBorder(Borders.compound(Borders.line(Color.RED, 2), Borders.empty(3)));
            } else if (forRemoval) {
                label.setBorder(Borders.compound(Borders.dashed(Color.RED, 2), Borders.empty(3)));
            } else if (selected && edited) {
                label.setBorder(Borders.compound(Borders.line(Color.ORANGE, 2), Borders.empty(3)));
            } else if (edited) {
                label.setBorder(Borders.compound(Borders.dashed(Color.ORANGE, 2), Borders.empty(3)));
            } else if (selected) {
                label.setBorder(Borders.compound(Borders.line(Color.BLUE, 2), Borders.empty(3)));
            } else if (hasFocus) {
                label.setBorder(Borders.compound(Borders.line(Color.BLUE, 1), Borders.empty(4)));
            } else {
                label.setBorder(Borders.empty(5));
            }
        }
        return label;
    }

    private JLabel getDefaultListCellRendererReset(JList<? extends PdfObject> list, int index, boolean selected, boolean hasFocus) {
        final JLabel label = (JLabel) renderer.getListCellRendererComponent(list, null, index, selected, hasFocus);
        label.setText(null);
        label.setBackground(null);
        label.setForeground(Color.BLACK);
        label.setBorder(null);
        return label;
    }

    private boolean belongsToPage(JList<? extends PdfObject> list, PdfObject value) {
        final PdfObject selection = list.getSelectedValue();
        if (selection == null) {
            return false;
        } else if (selection instanceof PdfPage && value instanceof PdfPage) {
            return selection == value;
        } else if (selection instanceof PdfPage && value instanceof PdfImage) {
            return selection == ((PdfImage) value).getPage();
        } else if (selection instanceof PdfImage && value instanceof PdfPage) {
            return ((PdfImage) selection).getPage() == value;
        } else if (selection instanceof PdfImage && value instanceof PdfImage) {
            return ((PdfImage) selection).getPage() == ((PdfImage) value).getPage();
        } else {
            return false;
        }
    }
}
