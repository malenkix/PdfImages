package io.github.malenkix.pdfimages.xui;

import io.github.malenkix.pdfimages.ui.ListScrollPane;
import io.github.malenkix.pdfimages.viewmodels.PdfObject;

/**
 *
 * @author Maik
 */
public class PdfScrollPane extends ListScrollPane<PdfObject> {

    public PdfScrollPane() {
        super.setCellRenderer(new PdfListCellRenderer());
    }
}
