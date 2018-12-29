package io.github.malenkix.pdfimages.ui;

import io.github.malenkix.pdfimages.i18n.Bundle;
import io.github.malenkix.pdfimages.i18n.I18n;
import io.github.malenkix.swing.Dialogs;
import java.io.File;
import java.util.HashSet;
import java.util.Locale;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

/**
 *
 * @author Maik
 */
public class MenuBar extends JMenuBar {

    private final JMenu fileMenu = new JMenu();
    private final JMenuItem fileOpenItem = new JMenuItem();
    private final JMenu fileOpenRecentMenu = new JMenu();
    private final JMenuItem fileSaveItem = new JMenuItem();
    private final JMenuItem fileSaveAsItem = new JMenuItem();
    private final JMenuItem fileExitItem = new JMenuItem();

    private final JMenu languagesMenu = new JMenu();

    private JFrame frame;

    private Consumer<File> fileOpenConsumer;
    private BiConsumer<File, File> fileSaveConsumer;
    private Runnable fileExitRunnable;

    private Consumer<Locale> languageConsumer;

    private final HashSet<File> recentPdfFiles = new HashSet<>();
    private File openPdfFile = null;
    private File savePdfFile = null;

    public MenuBar() {
        initMenuBar();
        initMenuBarActions();
    }

    private void initMenuBar() {

        super.add(fileMenu);
        super.add(Box.createHorizontalGlue());
        super.add(languagesMenu);

        fileMenu.add(fileOpenItem);
        fileMenu.add(fileOpenRecentMenu);
        fileMenu.addSeparator();
        fileMenu.add(fileSaveItem);
        fileMenu.add(fileSaveAsItem);
        fileMenu.addSeparator();
        fileMenu.add(fileExitItem);

        fileSaveItem.setEnabled(false);
        fileSaveAsItem.setEnabled(false);
    }

    private void initMenuBarActions() {

        fileOpenItem.addActionListener(e -> onOpenFileClicked());
        fileSaveItem.addActionListener(e -> onSaveFileClicked(true));
        fileSaveAsItem.addActionListener(e -> onSaveFileClicked(false));
        fileExitItem.addActionListener(e -> {
            if (fileExitRunnable != null) {
                fileExitRunnable.run();
            }
        });

        final ButtonGroup bg = new ButtonGroup();
        I18n.getLocales().forEach(locale -> {
            final String displayLanguage = locale.getDisplayLanguage(locale);
            final JRadioButtonMenuItem languageItem = new JRadioButtonMenuItem(displayLanguage);
            languageItem.setSelected(locale == I18n.getCurrentLocale());
            languageItem.addActionListener(e -> {
                if (languageConsumer != null) {
                    languageConsumer.accept(locale);
                }
            });
            bg.add(languageItem);
            languagesMenu.add(languageItem);
        });
    }

    private void onOpenFileClicked() {
        final File file = Dialogs.choosePdfFile(frame, openPdfFile, Dialogs.OPEN_DIALOG);
        if (file != null) {
            onOpenFile(file);
        }
    }

    private void onOpenFile(File file) {

        openPdfFile = file;
        fileOpenItem.setToolTipText(file.toString());

        if (!recentPdfFiles.contains(file)) {
            recentPdfFiles.add(file);
            final JMenuItem fileOpenRecentItem = new JMenuItem(file.toString());
            fileOpenRecentItem.addActionListener(e -> onOpenFile(file));
            fileOpenRecentMenu.add(fileOpenRecentItem);
        }

        if (fileOpenConsumer != null) {
            fileOpenConsumer.accept(openPdfFile);
        }
    }

    private void onSaveFileClicked(final boolean usePreviousFile) {

        final File file;
        if (usePreviousFile && savePdfFile != null) {
            file = savePdfFile;
        } else {
            file = Dialogs.choosePdfFile(frame, savePdfFile, Dialogs.SAVE_DIALOG);
        }

        if (file != null) {

            savePdfFile = file;
            fileSaveItem.setToolTipText(file.toString());

            if (openPdfFile != null && fileSaveConsumer != null) {
                fileSaveConsumer.accept(openPdfFile, savePdfFile);
            }
        }
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public void setFileOpenConsumer(Consumer<File> fileOpenConsumer) {
        this.fileOpenConsumer = fileOpenConsumer;
    }

    public void setFileSaveConsumer(BiConsumer<File, File> fileSaveConsumer) {
        this.fileSaveConsumer = fileSaveConsumer;
    }

    public void setFileExitRunnable(Runnable fileExitRunnable) {
        this.fileExitRunnable = fileExitRunnable;
    }

    public void setLanguageConsumer(Consumer<Locale> languageConsumer) {
        this.languageConsumer = languageConsumer;
    }

    public void localize() {

        final Bundle bundle = I18n.getCurrentBundle();

        fileMenu.setText(bundle.getFile());
        fileOpenItem.setText(bundle.getFileOpen());
        fileOpenRecentMenu.setText(bundle.getFileOpenRecent());
        fileSaveItem.setText(bundle.getFileSave());
        fileSaveAsItem.setText(bundle.getFileSaveAs());
        fileExitItem.setText(bundle.getFileExit());
        languagesMenu.setText(bundle.getLanguages());
    }

    public void setLocked(boolean locked, boolean hasError) {
        fileOpenItem.setEnabled(!locked);
        fileOpenRecentMenu.setEnabled(!locked);
        fileSaveItem.setEnabled(!locked && !hasError);
        fileSaveAsItem.setEnabled(!locked && !hasError);
        languagesMenu.setEnabled(!locked);
    }

    public void setLocked(boolean locked) {
        setLocked(locked, false);
    }

}
