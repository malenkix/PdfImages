package io.github.malenkix.pdfimages.io;

/**
 *
 * @author Maik
 */
public class PdfUpdate {

    private final PdfUpdateState state;
    private Throwable error;

    public PdfUpdate(PdfUpdateState state) {
        this.state = state;
    }

    public PdfUpdateState getState() {
        return state;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }

    public static PdfUpdate error(Throwable error) {
        final PdfUpdate update = new PdfUpdate(PdfUpdateState.ERROR);
        update.setError(error);
        return update;
    }
}
