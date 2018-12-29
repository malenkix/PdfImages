package io.github.malenkix.pdfimages.ui;

import io.github.malenkix.pdfimages.i18n.I18n;
import io.github.malenkix.pdfimages.io.PdfReaderWorker;
import io.github.malenkix.pdfimages.io.PdfUpdateState;
import io.github.malenkix.pdfimages.io.PdfWriterWorker;
import io.github.malenkix.pdfimages.viewmodels.PdfObject;
import io.github.malenkix.pdfimages.viewmodels.PdfPage;
import io.github.malenkix.swing.BorderPanel;
import io.github.malenkix.swing.Borders;
import io.github.malenkix.swing.Dialogs;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import javax.swing.JFrame;
import javax.swing.JProgressBar;

/**
 *
 * @author Maik
 */
public class Frame extends JFrame {

    private final MenuBar menuBar = new MenuBar();

    private final BorderPanel framePanel = new BorderPanel(5, 5);

    private final PdfScrollPane pdfScrollPane = new PdfScrollPane();
    private final JProgressBar feedProgressBar = new JProgressBar();
    private final PdfEditorPanel editorPanel = new PdfEditorPanel(pdfScrollPane);

    private final AtomicBoolean hintToStop = new AtomicBoolean();

    public Frame() {
        super("PdfImages");
        initMenuBar();
        initComponents();
        initActions();
        initLayout();
        localize();
    }

    private void initMenuBar() {
        super.setJMenuBar(menuBar);
        menuBar.setFrame(this);
        menuBar.setFileOpenConsumer(this::onOpenFile);
        menuBar.setFileSaveConsumer(this::onSaveFile);
        menuBar.setFileExitRunnable(super::dispose);
        menuBar.setLanguageConsumer(locale -> {
            I18n.setCurrentLocale(locale);
            localize();
        });
    }

    private void initComponents() {
        framePanel.setBorder(Borders.empty(10));
        pdfScrollPane.setEnabled(false);
        feedProgressBar.setStringPainted(true);
        editorPanel.setFrame(this);
        editorPanel.setBorder(Borders.compound(Borders.lineGray(), Borders.empty(5)));
        editorPanel.setPageControlsEnabled(false);
        editorPanel.setImageControlsEnabled(false);
    }

    private void initActions() {
        super.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                hintToStop.set(true);
            }
        });
        pdfScrollPane.addSelectionListener(e -> {
            editorPanel.setObject(pdfScrollPane.getSelectedValue());
            pdfScrollPane.requestListRepaint();
        });
    }

    private void initLayout() {
        super.add(framePanel, BorderPanel.CENTER);
        framePanel.addBottom(feedProgressBar);
        framePanel.addCenter(pdfScrollPane);
        framePanel.addRight(editorPanel);
    }

    private void onOpenFile(File file) {
        if (file != null) {

            final PdfReaderWorker worker = new PdfReaderWorker(file);
            worker.setHintToStop(hintToStop);
            worker.setCallback(update -> {
                if (PdfUpdateState.BEFORE_LOAD == update.getState()) {
                    menuBar.setLocked(true);
                    feedProgressBar.setValue(0);
                    pdfScrollPane.clear();
                    pdfScrollPane.setEnabled(false);
                } else if (PdfUpdateState.PAGE_PROGRESS == update.getState() && update.getPage() != null) {
                    feedProgressBar.setValue(worker.getProgress());
                    pdfScrollPane.addModel(update.getPage());
                    update.getPage().getImages().forEach(pdfScrollPane::addModel);
                } else if (PdfUpdateState.DONE == update.getState()) {
                    menuBar.setLocked(false);
                    feedProgressBar.setValue(100);
                    pdfScrollPane.setEnabled(true);
                } else if (PdfUpdateState.ERROR == update.getState()) {
                    menuBar.setLocked(false, true);
                }
            });
            worker.execute();
        }
    }

    private void onSaveFile(File openFile, File saveFile) {
        if (openFile != null && saveFile != null) {
            final List<PdfPage> pageModels = pdfScrollPane.getModels(PdfObject::isPage).stream().map(o -> (PdfPage) o).collect(Collectors.toList());
            final PdfWriterWorker worker = new PdfWriterWorker(openFile, saveFile, pageModels);
            worker.setHintToStop(hintToStop);
            worker.setCallback(update -> {
                if (PdfUpdateState.BEFORE_LOAD == update.getState()) {
                    menuBar.setLocked(true);
                    feedProgressBar.setValue(0);
                    pdfScrollPane.clearSelection();
                    pdfScrollPane.setEnabled(false);
                } else if (PdfUpdateState.DONE == update.getState()) {
                    menuBar.setLocked(false);
                    feedProgressBar.setValue(100);
                    pdfScrollPane.setEnabled(true);
                } else if (PdfUpdateState.ERROR == update.getState()) {
                    menuBar.setLocked(false, true);
                }
            });
            worker.execute();
        }
    }

    private void localize() {
        Dialogs.setDefaultLocale(I18n.getCurrentLocale());
        menuBar.localize();
        editorPanel.localize();
    }
}
