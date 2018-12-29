package io.github.malenkix.pdfimages.ui;

import io.github.malenkix.pdfimages.viewmodels.PdfObject;
import io.github.malenkix.swing.ListScrollPane;

/**
 *
 * @author Maik
 */
public class PdfScrollPane extends ListScrollPane<PdfObject> {

    public PdfScrollPane() {
        super(ListScrollPane.HORIZONTAL_WRAP);
        super.setCellRenderer(new PdfListCellRenderer());
    }
}
