package io.github.malenkix.pdfimages;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Maik
 */
public final class I18n {

    private static final Logger LOG = LoggerFactory.getLogger(I18n.class);

    public static interface Bundle {

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

    private static final LinkedHashMap<Locale, Bundle> BUNDLES = new LinkedHashMap<>();
    private static Locale currentLocale = Locale.ENGLISH;

    static {
        BUNDLES.put(Locale.ENGLISH, new Bundle() {
        });
    }

    public static Set<Locale> getLocales() {
        return Collections.unmodifiableSet(BUNDLES.keySet());
    }

    public static Bundle getCurrentBundle() {
        return BUNDLES.get(currentLocale);
    }

    public static Locale getCurrentLocale() {
        return currentLocale;
    }

    public static void setCurrentLocale(final Locale locale) {
        if (locale == null) {
            throw new NullPointerException("'locale' may not be null");
        }
        if (BUNDLES.get(locale) == null) {
            throw new IllegalArgumentException(String.format("'locale' '%s' is not valid", locale));
        }
        LOG.debug("Change locale from '{}' to '{}'.", currentLocale, locale);
        I18n.currentLocale = locale;
    }

}
