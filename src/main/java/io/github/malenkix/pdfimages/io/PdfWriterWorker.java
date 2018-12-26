package io.github.malenkix.pdfimages.io;

import io.github.malenkix.pdfimages.viewmodels.PdfImage;
import io.github.malenkix.pdfimages.viewmodels.PdfPage;
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
public class PdfWriterWorker extends SwingWorker<Void, PdfUpdate> {

    private final File openFile;
    private final File saveFile;
    private final List<PdfPage> pages;

    private AtomicBoolean hintToStop = null;
    private Consumer<PdfUpdate> callback = null;

    public PdfWriterWorker(File openFile, File saveFile, List<PdfPage> pages) {
        this.openFile = openFile;
        this.saveFile = saveFile;
        this.pages = pages;
    }

    @Override
    protected Void doInBackground() {

        publish(new PdfUpdate(PdfUpdateState.BEFORE_LOAD));

        try (final PDDocument document = PDDocument.load(openFile)) {
            document.setAllSecurityToBeRemoved(true);
            final int numberOfPages = document.getNumberOfPages();
            publish(new PdfUpdate(PdfUpdateState.BEFORE_PAGES));
            for (int pageIndex = 0; pageIndex < numberOfPages && !isCancelled() && (hintToStop == null || !hintToStop.get()); pageIndex++) {
                final PdfPage page = pages.get(pageIndex);
                if (!page.isForRemoval()) {
                    updateResources(document, page, new AtomicInteger(), document.getPage(pageIndex).getResources());
                }
            }
            for (int pageIndex = numberOfPages - 1; pageIndex >= 0 && !isCancelled() && (hintToStop == null || !hintToStop.get()); pageIndex--) {
                if (pages.get(pageIndex).isForRemoval()) {
                    document.removePage(pageIndex);
                }
            }
            document.save(saveFile);
            publish(new PdfUpdate(PdfUpdateState.AFTER_PAGES));
            publish(new PdfUpdate(PdfUpdateState.DONE));
        } catch (IOException ex) {
            publish(PdfUpdate.error(ex));
        }
        return null;
    }

    private void updateResources(final PDDocument document, final PdfPage page, final AtomicInteger index, final PDResources resources) throws IOException {
        if (resources == null) {
            return;
        }
        for (final COSName cosName : resources.getXObjectNames()) {
            final PDXObject xObject = resources.getXObject(cosName);
            if (xObject instanceof PDFormXObject) {
                updateResources(document, page, index, ((PDFormXObject) xObject).getResources());
            } else if (xObject instanceof PDImageXObject) {
                final PdfImage imageModel = page.getImage(index.getAndIncrement());
                final BufferedImage updated = imageModel.getUpdated();
                if (updated != null) {
                    resources.put(cosName, LosslessFactory.createFromImage(document, updated));
                }
            }
        }
    }

    @Override
    protected void process(List<PdfUpdate> chunks) {
        if (callback != null && chunks != null) {
            chunks.forEach(callback::accept);
        }
    }

    public void setHintToStop(AtomicBoolean hintToStop) {
        this.hintToStop = hintToStop;
    }

    public void setCallback(Consumer<PdfUpdate> callback) {
        this.callback = callback;
    }
}
