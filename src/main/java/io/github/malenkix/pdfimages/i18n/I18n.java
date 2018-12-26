package io.github.malenkix.pdfimages.i18n;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Set;

/**
 *
 * @author Maik
 */
public final class I18n {

    private static final LinkedHashMap<Locale, Bundle> BUNDLES = new LinkedHashMap<>();
    private static Locale currentLocale = Locale.ENGLISH;

    static {
        BUNDLES.put(Locale.ENGLISH, new Bundle() {
            // see defaults
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
        I18n.currentLocale = locale;
    }

}
