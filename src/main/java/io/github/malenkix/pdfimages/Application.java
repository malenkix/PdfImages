package io.github.malenkix.pdfimages;

import io.github.malenkix.pdfimages.ui.Frame;
import io.github.malenkix.swing.SwingUtils;

/**
 *
 * @author Maik
 */
public final class Application {

    private Application() {
        //
    }

    public static void main(String[] args) throws Exception {

        System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");

        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
        });

        SwingUtils.boot(Frame.class, 0.85f);
    }
}
