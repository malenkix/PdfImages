package io.github.malenkix.pdfimages;

import io.github.malenkix.pdfimages.i18n.Bundle;
import io.github.malenkix.pdfimages.i18n.I18n;
import io.github.malenkix.pdfimages.io.PdfReaderWorker;
import io.github.malenkix.pdfimages.io.PdfUpdateState;
import io.github.malenkix.pdfimages.io.PdfWriterWorker;
import io.github.malenkix.pdfimages.ui.Dialogs;
import io.github.malenkix.pdfimages.viewmodels.PdfImage;
import io.github.malenkix.pdfimages.viewmodels.PdfObject;
import io.github.malenkix.pdfimages.viewmodels.PdfPage;
import io.github.malenkix.pdfimages.xui.PdfScrollPane;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Maik
 */
public class Frame extends JFrame {

    private static final Logger LOG = LoggerFactory.getLogger(Frame.class);

    private final JMenuBar menuBar = new JMenuBar();

    private final JMenu fileMenu = new JMenu();
    private final JMenuItem fileOpenItem = new JMenuItem();
    private final JMenuItem fileSaveItem = new JMenuItem();
    private final JMenuItem fileSaveAsItem = new JMenuItem();
    private final JMenuItem fileExitItem = new JMenuItem();

    private final JMenu languagesMenu = new JMenu();

    private final JPanel framePanel = new JPanel(new BorderLayout(5, 5), true);

    private final PdfScrollPane pdfScrollPane = new PdfScrollPane();

    private final JProgressBar feedProgressBar = new JProgressBar();

    private final JPanel detailsPanel = new JPanel(new BorderLayout(5, 5), true);
    private final JCheckBox detailRemovePageCheckBox = new JCheckBox();
    private final JPanel optionsHelperPanel = new JPanel(new BorderLayout(5, 5), true);
    private final JPanel optionsPanel = new JPanel(new GridLayout(0, 2, 5, 5), true);
    private final ButtonGroup optionButtonGroup = new ButtonGroup();
    private final JRadioButton optionBlankButton = new JRadioButton();
    private final JRadioButton optionWhiteButton = new JRadioButton();
    private final JRadioButton optionColorButton = new JRadioButton();
    private final JPanel optionColorPreviewPanel = new JPanel();
    private final JButton optionApplyButton = new JButton();

    private final AtomicBoolean hintToStop = new AtomicBoolean();

    private File openPdfFile = null;
    private File savePdfFile = null;

    public Frame() {
        super("PdfImages");
        initMenuBar();
        initMenuBarActions();
        initComponents();
        initActions();
        initLayout();
    }

    private void initMenuBar() {

        super.setJMenuBar(menuBar);

        menuBar.add(fileMenu);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(languagesMenu);

        fileMenu.add(fileOpenItem);
        fileMenu.addSeparator();
        fileMenu.add(fileSaveItem);
        fileMenu.add(fileSaveAsItem);
        fileMenu.addSeparator();
        fileMenu.add(fileExitItem);
    }

    private void initMenuBarActions() {

        fileOpenItem.addActionListener(e -> onOpenFileClicked());
        fileSaveItem.addActionListener(e -> onSaveFileClicked(true));
        fileSaveAsItem.addActionListener(e -> onSaveFileClicked(false));
        fileExitItem.addActionListener(e -> super.dispose());

        final ButtonGroup bg = new ButtonGroup();
        I18n.getLocales().forEach(locale -> {
            final String displayLanguage = locale.getDisplayLanguage(locale);
            final JRadioButtonMenuItem languageItem = new JRadioButtonMenuItem(displayLanguage);
            languageItem.setSelected(locale == I18n.getCurrentLocale());
            languageItem.addActionListener(e -> {
                I18n.setCurrentLocale(locale);
                localize();
            });
            bg.add(languageItem);
            languagesMenu.add(languageItem);
        });
    }

    private void initComponents() {
        fileSaveItem.setEnabled(false);
        fileSaveAsItem.setEnabled(false);
        framePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        pdfScrollPane.setEnabled(false);
        feedProgressBar.setStringPainted(true);
        detailsPanel.setBorder(new CompoundBorder(new LineBorder(Color.GRAY), new EmptyBorder(5, 5, 5, 5)));
        detailRemovePageCheckBox.setEnabled(false);
        optionsPanel.setBorder(new CompoundBorder(new MatteBorder(1, 0, 0, 0, Color.GRAY), new EmptyBorder(5, 0, 0, 0)));
        optionButtonGroup.add(optionBlankButton);
        optionButtonGroup.add(optionWhiteButton);
        optionButtonGroup.add(optionColorButton);
        optionBlankButton.setEnabled(false);
        optionWhiteButton.setEnabled(false);
        optionColorButton.setEnabled(false);
        optionColorPreviewPanel.setBackground(null);
        optionColorPreviewPanel.setBorder(new LineBorder(Color.GRAY));
        optionApplyButton.setEnabled(false);
    }

    private void initActions() {
        super.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                hintToStop.set(true);
            }
        });
        pdfScrollPane.addSelectionListener(e -> {
            detailRemovePageCheckBox.setEnabled(false);
            detailRemovePageCheckBox.setSelected(false);
            optionButtonGroup.clearSelection();
            optionBlankButton.setEnabled(false);
            optionWhiteButton.setEnabled(false);
            optionColorButton.setEnabled(false);
            optionColorPreviewPanel.setBackground(null);
            optionApplyButton.setEnabled(false);
            final PdfObject model = pdfScrollPane.getSelectedValue();
            if (model != null) {
                detailRemovePageCheckBox.setEnabled(model.isPage());
                detailRemovePageCheckBox.setSelected(model.isForRemoval());
                if (model instanceof PdfImage && !model.isForRemoval()) {
                    final PdfImage imageModel = (PdfImage) model;
                    optionBlankButton.setEnabled(true);
                    optionBlankButton.setSelected(imageModel.isBlank());
                    optionWhiteButton.setEnabled(true);
                    optionWhiteButton.setSelected(imageModel.isWhite());
                    optionColorButton.setEnabled(true);
                    optionColorButton.setSelected(imageModel.hasColor() && !imageModel.isWhite());
                    optionColorPreviewPanel.setBackground(imageModel.hasColor() && !imageModel.isWhite() ? imageModel.getColor() : null);
                    optionApplyButton.setEnabled(true);
                }
                pdfScrollPane.requestListRepaint();
            }
        });
        detailRemovePageCheckBox.addActionListener(e -> {
            final PdfObject model = pdfScrollPane.getSelectedValue();
            if (model != null && model.isPage()) {
                ((PdfPage) model).setForRemoval(detailRemovePageCheckBox.isSelected());
                pdfScrollPane.requestListFocus();
                pdfScrollPane.requestListRepaint();
            }
        });
        optionBlankButton.addActionListener(e -> onOptionSelected());
        optionWhiteButton.addActionListener(e -> onOptionSelected());
        optionColorButton.addActionListener(e -> onOptionSelected());
        optionApplyButton.addActionListener(e -> {
            final PdfObject model = pdfScrollPane.getSelectedValue();
            if (model != null && !model.isPage()) {
                final PdfImage imageModel = (PdfImage) model;
                pdfScrollPane.getModels(model::hasSameOriginalAs).forEach(other -> {
                    if (other instanceof PdfImage) {
                        final PdfImage otherImageModel = (PdfImage) other;
                        otherImageModel.setBlank(imageModel.isBlank());
                        otherImageModel.setColor(imageModel.getColor());
                    }
                });
                pdfScrollPane.requestListFocus();
                pdfScrollPane.requestListRepaint();
            }
        });
    }

    private void initLayout() {
        super.add(framePanel, BorderLayout.CENTER);
        framePanel.add(feedProgressBar, BorderLayout.SOUTH);
        framePanel.add(pdfScrollPane, BorderLayout.CENTER);
        framePanel.add(detailsPanel, BorderLayout.EAST);
        detailsPanel.add(detailRemovePageCheckBox, BorderLayout.NORTH);
        detailsPanel.add(optionsHelperPanel, BorderLayout.CENTER);
        optionsHelperPanel.add(optionsPanel, BorderLayout.NORTH);
        optionsPanel.add(optionBlankButton);
        optionsPanel.add(Box.createHorizontalGlue());
        optionsPanel.add(optionWhiteButton);
        optionsPanel.add(Box.createHorizontalGlue());
        optionsPanel.add(optionColorButton);
        optionsPanel.add(optionColorPreviewPanel);
        optionsPanel.add(Box.createHorizontalGlue());
        optionsPanel.add(optionApplyButton);
    }

    private void onOpenFileClicked() {
        final File file = Dialogs.choosePdfFile(this, openPdfFile, JFileChooser.OPEN_DIALOG);
        if (file != null) {

            openPdfFile = file;
            fileOpenItem.setToolTipText(file.toString());

            final PdfReaderWorker worker = new PdfReaderWorker(file);
            worker.setHintToStop(hintToStop);
            worker.setCallback(update -> {
                if (PdfUpdateState.BEFORE_LOAD == update.getState()) {
                    fileSaveItem.setEnabled(false);
                    fileSaveAsItem.setEnabled(false);
                    feedProgressBar.setValue(0);
                    pdfScrollPane.clear();
                    pdfScrollPane.setEnabled(false);
                } else if (PdfUpdateState.PAGE_PROGRESS == update.getState() && update.getPage() != null) {
                    feedProgressBar.setValue(worker.getProgress());
                    pdfScrollPane.addModel(update.getPage());
                    update.getPage().getImages().forEach(pdfScrollPane::addModel);
                } else if (PdfUpdateState.DONE == update.getState()) {
                    fileSaveItem.setEnabled(true);
                    fileSaveAsItem.setEnabled(true);
                    feedProgressBar.setValue(100);
                    pdfScrollPane.setEnabled(true);
                }
            });
            worker.execute();
        }
    }

    private void onSaveFileClicked(final boolean usePreviousFile) {
        final File file;
        if (usePreviousFile && savePdfFile != null) {
            file = savePdfFile;
        } else {
            file = Dialogs.choosePdfFile(this, savePdfFile, JFileChooser.SAVE_DIALOG);
            if (file != null) {
                savePdfFile = file;
                fileSaveItem.setToolTipText(file.toString());
            }
        }
        if (file != null) {
            final List<PdfPage> pageModels = pdfScrollPane.getModels(PdfObject::isPage).stream().map(o -> (PdfPage) o).collect(Collectors.toList());
            final PdfWriterWorker worker = new PdfWriterWorker(openPdfFile, savePdfFile, pageModels);
            worker.setHintToStop(hintToStop);
            worker.setCallback(update -> {
                if (PdfUpdateState.BEFORE_LOAD == update.getState()) {
                    fileOpenItem.setEnabled(false);
                    fileSaveItem.setEnabled(false);
                    fileSaveAsItem.setEnabled(false);
                    feedProgressBar.setValue(0);
                    pdfScrollPane.clearSelection();
                    pdfScrollPane.setEnabled(false);
                } else if (PdfUpdateState.DONE == update.getState()) {
                    fileOpenItem.setEnabled(true);
                    fileSaveItem.setEnabled(true);
                    fileSaveAsItem.setEnabled(true);
                    feedProgressBar.setValue(100);
                    pdfScrollPane.setEnabled(true);
                }
            });
            worker.execute();
        }
    }

    private void onOptionSelected() {
        final PdfObject model = pdfScrollPane.getSelectedValue();
        if (model != null && !model.isPage()) {
            final PdfImage imageModel = (PdfImage) model;
            if (optionBlankButton.isSelected()) {
                imageModel.setBlank(true);
                imageModel.setColor(null);
                optionColorPreviewPanel.setBackground(null);
            } else if (optionWhiteButton.isSelected()) {
                imageModel.setBlank(false);
                imageModel.setColor(Color.WHITE);
                optionColorPreviewPanel.setBackground(null);
            } else if (optionColorButton.isSelected()) {
                final Color color = Dialogs.chooseColor(this, Color.WHITE);
                imageModel.setBlank(false);
                imageModel.setColor(color);
                if (color == null) {
                    optionButtonGroup.clearSelection();
                }
                optionColorPreviewPanel.setBackground(color);
            }
            pdfScrollPane.requestListRepaint();
        }
    }

    public void localize() {

        JFileChooser.setDefaultLocale(I18n.getCurrentLocale());
        JOptionPane.setDefaultLocale(I18n.getCurrentLocale());
        JColorChooser.setDefaultLocale(I18n.getCurrentLocale());

        final Bundle bundle = I18n.getCurrentBundle();

        fileMenu.setText(bundle.getFile());
        fileOpenItem.setText(bundle.getFileOpen());
        fileSaveItem.setText(bundle.getFileSave());
        fileSaveAsItem.setText(bundle.getFileSaveAs());
        fileExitItem.setText(bundle.getFileExit());
        languagesMenu.setText(bundle.getLanguages());
        detailRemovePageCheckBox.setText(bundle.getRemovePage());
        optionBlankButton.setText(bundle.getOptionBlank());
        optionWhiteButton.setText(bundle.getOptionWhite());
        optionColorButton.setText(bundle.getOptionColor());
        optionApplyButton.setText(bundle.getOptionApply());
    }
}
