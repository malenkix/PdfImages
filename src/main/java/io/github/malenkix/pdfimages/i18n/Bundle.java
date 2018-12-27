package io.github.malenkix.pdfimages.i18n;

/**
 *
 * @author Maik
 */
public interface Bundle {

    default String getFile() {
        return "File";
    }

    default String getFileOpen() {
        return "Open File...";
    }

    default String getFileSave() {
        return "Save File";
    }

    default String getFileSaveAs() {
        return "Save File As...";
    }

    default String getFileExit() {
        return "Exit";
    }

    default String getLanguages() {
        return "Languages";
    }

    default String getRemovePage() {
        return "Remove Page";
    }

    default String getOptionNone() {
        return "None";
    }

    default String getOptionBlank() {
        return "Set Blank";
    }

    default String getOptionWhite() {
        return "Set White";
    }

    default String getOptionColor() {
        return "Set Color...";
    }

    default String getOptionApply() {
        return "Apply On Same Images";
    }
}
