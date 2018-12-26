package io.github.malenkix.pdfimages.io;

import io.github.malenkix.pdfimages.viewmodels.PdfImage;
import io.github.malenkix.pdfimages.viewmodels.PdfPage;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import javax.swing.SwingWorker;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

/**
 *
 * @author Maik
 */
public class PdfReaderWorker extends SwingWorker<Void, PdfReaderUpdate> {

    private final File openFile;

    private AtomicBoolean hintToStop = null;
    private Consumer<PdfReaderUpdate> callback = null;

    private float dpi = 36f;
    private boolean colored = true;
    private int pageWidth = 150;
    private int pageHeight = 225;
    private int imageWidth = 50;
    private int imageHeight = 80;

    public PdfReaderWorker(File file) {
        this.openFile = file;
    }

    @Override
    protected Void doInBackground() {

        publish(new PdfReaderUpdate(PdfUpdateState.BEFORE_LOAD));

        try (final PDDocument document = PDDocument.load(openFile)) {
            document.setAllSecurityToBeRemoved(true);
            final PDFRenderer renderer = new PDFRenderer(document);
            final int numberOfPages = document.getNumberOfPages();
            publish(new PdfReaderUpdate(PdfUpdateState.BEFORE_PAGES));
            for (int pageIndex = 0; pageIndex < numberOfPages && !isCancelled() && (hintToStop == null || !hintToStop.get()); pageIndex++) {

                final BufferedImage pageImage = renderer.renderImageWithDPI(pageIndex, dpi, colored ? ImageType.RGB : ImageType.GRAY);
                final BufferedImage pagePreview = Thumbnails.of(pageImage).size(pageWidth, pageHeight).asBufferedImage();

                final PdfPage page = new PdfPage();
                page.setOriginal(pageImage);
                page.setPreview(pagePreview);

                collectResources(page, document.getPage(pageIndex).getResources());
                setProgress((int) (((pageIndex + 1) / (float) numberOfPages) * 100f));
                publish(PdfReaderUpdate.progress(page));
            }

            publish(new PdfReaderUpdate(PdfUpdateState.AFTER_PAGES));
            publish(new PdfReaderUpdate(PdfUpdateState.DONE));
        } catch (IOException ex) {
            publish(PdfReaderUpdate.error(ex));
        }
        return null;
    }

    private void collectResources(final PdfPage page, final PDResources resources) throws IOException {
        if (resources == null) {
            return;
        }
        for (final COSName cosName : resources.getXObjectNames()) {
            final PDXObject xObject = resources.getXObject(cosName);
            if (xObject instanceof PDFormXObject) {
                collectResources(page, ((PDFormXObject) xObject).getResources());
            } else if (xObject instanceof PDImageXObject) {

                final PDImageXObject xImage = (PDImageXObject) xObject;
                final BufferedImage image = xImage.getImage();
                final BufferedImage preview = Thumbnails.of(image).size(imageWidth, imageHeight).asBufferedImage();

                final PdfImage imageModel = new PdfImage(page);
                imageModel.setOriginal(image);
                imageModel.setPreview(preview);

                page.addImage(imageModel);
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

    public void setDpi(float dpi) {
        this.dpi = dpi;
    }

    public void setColored(boolean colored) {
        this.colored = colored;
    }

    public void setPageSize(int width, int height) {
        this.pageWidth = width;
        this.pageHeight = height;
    }

    public void setImageSize(int width, int height) {
        this.imageWidth = width;
        this.imageHeight = height;
    }

}
