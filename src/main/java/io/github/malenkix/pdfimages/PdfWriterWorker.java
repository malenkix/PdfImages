package io.github.malenkix.pdfimages;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import javax.swing.SwingWorker;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

/**
 *
 * @author Maik
 */
public class PdfWriterWorker extends SwingWorker<Void, PdfWriterWorker.PdfReaderUpdate> {

    public static final class PdfReaderUpdate {

        public final boolean start;
        public final PdfModel preview;
        public final boolean done;
        public final boolean error;

        public PdfReaderUpdate(boolean start, PdfModel preview, boolean done, boolean error) {
            this.start = start;
            this.preview = preview;
            this.done = done;
            this.error = error;
        }

    }

    private final File openFile;
    private final File saveFile;
    private final PdfModel[] pages;

    private AtomicBoolean hintToStop = null;
    private Consumer<PdfReaderUpdate> callback = null;

    public PdfWriterWorker(File openFile, File saveFile, PdfModel[] pages) {
        this.openFile = openFile;
        this.saveFile = saveFile;
        this.pages = pages;
    }

    @Override
    protected Void doInBackground() {

        publish(new PdfReaderUpdate(true, null, false, false));

        try (final PDDocument document = PDDocument.load(openFile)) {
            document.setAllSecurityToBeRemoved(true);
            final int numberOfPages = document.getNumberOfPages();
            for (int pageIndex = 0; pageIndex < numberOfPages && !isCancelled() && (hintToStop == null || !hintToStop.get()); pageIndex++) {
                final PdfModel page = pages[pageIndex];
                if (!page.isForRemoval()) {
                    updateResources(document, page, new AtomicInteger(), document.getPage(pageIndex).getResources());
                }
            }
            for (int pageIndex = numberOfPages - 1; pageIndex >= 0 && !isCancelled() && (hintToStop == null || !hintToStop.get()); pageIndex--) {
                if (pages[pageIndex].isForRemoval()) {
                    document.removePage(pageIndex);
                }
            }
            document.save(saveFile);
            publish(new PdfReaderUpdate(false, null, true, false));
        } catch (IOException ex) {
            publish(new PdfReaderUpdate(false, null, false, true));
        }
        return null;
    }

    private void updateResources(final PDDocument document, final PdfModel page, final AtomicInteger index, final PDResources resources) throws IOException {
        if (resources == null) {
            return;
        }
        for (final COSName cosName : resources.getXObjectNames()) {
            final PDXObject xObject = resources.getXObject(cosName);
            if (xObject instanceof PDFormXObject) {
                updateResources(document, page, index, ((PDFormXObject) xObject).getResources());
            } else if (xObject instanceof PDImageXObject) {
                final PdfModel image = page.getModel(index.getAndIncrement());
                final BufferedImage replacement = image.getReplacement();
                if (replacement != null) {
                    resources.put(cosName, LosslessFactory.createFromImage(document, replacement));
                }
            }
        }
    }

    @Override
    protected void process(List<PdfReaderUpdate> chunks) {
        if (callback != null && chunks != null) {
            chunks.forEach(callback::accept);
        }
    }

    public void setHintToStop(AtomicBoolean hintToStop) {
        this.hintToStop = hintToStop;
    }

    public void setCallback(Consumer<PdfReaderUpdate> callback) {
        this.callback = callback;
    }
}
