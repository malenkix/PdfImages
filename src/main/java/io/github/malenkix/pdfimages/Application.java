package io.github.malenkix.pdfimages;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Maik
 */
public final class Application {

    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    private Application() {
        //
    }

    public static void main(String[] args) throws Exception {

        System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");

        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        final Properties properties = new Properties();
        properties.putAll(System.getenv());
        properties.putAll(System.getProperties());

        final String keysValues = properties.entrySet().stream().map(Map.Entry::toString).collect(Collectors.joining("\n"));

        LOG.debug("System Properties:\n{}", keysValues);

        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            LOG.error("@{}: '{}'", thread, throwable.getMessage(), throwable);
        });

        SwingUtilities.invokeLater(() -> {

            final Frame frame = new Frame();
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            final Toolkit toolkit = frame.getToolkit();
            final Dimension screen = toolkit.getScreenSize();

            frame.setSize((int) (screen.width * 0.85f), (int) (screen.height * 0.85f));
            frame.setLocationRelativeTo(null);

            LOG.info("Frame: {}", frame.getBounds());

            frame.setVisible(true);
            frame.localize();
        });
    }
}
