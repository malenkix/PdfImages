package io.github.malenkix.pdfimages.io;

import io.github.malenkix.pdfimages.viewmodels.PdfPage;

/**
 *
 * @author Maik
 */
public class PdfReaderUpdate extends PdfUpdate {

    private PdfPage page;

    public PdfReaderUpdate(PdfUpdateState state) {
        super(state);
    }

    public PdfPage getPage() {
        return page;
    }

    public void setPage(PdfPage page) {
        this.page = page;
    }

    public static PdfReaderUpdate progress(PdfPage page) {
        final PdfReaderUpdate update = new PdfReaderUpdate(PdfUpdateState.PAGE_PROGRESS);
        update.setPage(page);
        return update;
    }

    public static PdfReaderUpdate error(Throwable error) {
        final PdfReaderUpdate update = new PdfReaderUpdate(PdfUpdateState.ERROR);
        update.setError(error);
        return update;
    }
}
