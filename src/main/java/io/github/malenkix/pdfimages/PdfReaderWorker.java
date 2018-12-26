package io.github.malenkix.pdfimages;

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
public class PdfReaderWorker extends SwingWorker<Void, PdfReaderWorker.PdfReaderUpdate> {

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

    private final File file;
    private AtomicBoolean hintToStop = null;
    private Consumer<PdfReaderUpdate> callback = null;

    public PdfReaderWorker(File file) {
        this.file = file;
    }

    @Override
    protected Void doInBackground() {

        publish(new PdfReaderUpdate(true, null, false, false));

        try (final PDDocument document = PDDocument.load(file)) {
            document.setAllSecurityToBeRemoved(true);
            final PDFRenderer renderer = new PDFRenderer(document);
            final int numberOfPages = document.getNumberOfPages();
            for (int pageIndex = 0; pageIndex < numberOfPages && !isCancelled() && (hintToStop == null || !hintToStop.get()); pageIndex++) {

                final BufferedImage pageImage = renderer.renderImageWithDPI(pageIndex, 72f, ImageType.RGB);
                final BufferedImage pagePreview = Thumbnails.of(pageImage).size(200, 300).asBufferedImage();

                final PdfModel pageModel = new PdfModel(PdfModel.PAGE);
                pageModel.setOriginal(pageImage);
                pageModel.setPreview(pagePreview);

                setProgress((int) (((pageIndex + 1) / (float) numberOfPages) * 100f));
                publish(new PdfReaderUpdate(false, pageModel, false, false));

                collectResources(pageModel, document.getPage(pageIndex).getResources());
            }

            publish(new PdfReaderUpdate(false, null, true, false));
        } catch (IOException ex) {
            publish(new PdfReaderUpdate(false, null, false, true));
        }
        return null;
    }

    private void collectResources(final PdfModel parent, final PDResources resources) throws IOException {
        if (resources == null) {
            return;
        }
        for (final COSName cosName : resources.getXObjectNames()) {
            final PDXObject xObject = resources.getXObject(cosName);
            if (xObject instanceof PDFormXObject) {
                collectResources(parent, ((PDFormXObject) xObject).getResources());
            } else if (xObject instanceof PDImageXObject) {

                final PDImageXObject xImage = (PDImageXObject) xObject;
                final BufferedImage image = xImage.getImage();
                final BufferedImage preview = Thumbnails.of(image).size(100, 160).asBufferedImage();

                final PdfModel imageModel = new PdfModel();
                imageModel.setParent(parent);
                imageModel.setOriginal(image);
                imageModel.setPreview(preview);

                parent.addModel(imageModel);

                publish(new PdfReaderUpdate(false, imageModel, false, false));
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
