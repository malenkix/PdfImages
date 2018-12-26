package io.github.malenkix.pdfimages.viewmodels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Maik
 */
public class PdfPage extends PdfObject {

    private final ArrayList<PdfImage> images = new ArrayList<>();
    private boolean forRemoval;

    public List<PdfImage> getImages() {
        return Collections.unmodifiableList(images);
    }

    public PdfImage getImage(int index) {
        return images.get(index);
    }

    public void addImage(PdfImage image) {
        images.add(image);
    }

    @Override
    public boolean isPage() {
        return true;
    }

    @Override
    public boolean isForRemoval() {
        return forRemoval;
    }

    public void setForRemoval(boolean forRemoval) {
        this.forRemoval = forRemoval;
    }

}
