package de.fhbingen.wbs.translation;

import c10n.C10NConfigBase;
import c10n.LocaleProvider;
import c10n.annotations.DefaultC10NAnnotations;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;


/**
 * Uses English as locale if found non translated locale.
 */


public class C10NUseEnglishDefaultConfiguration extends C10NConfigBase {
    private static Set<Locale> translatedLocales = new HashSet<Locale>(Arrays
            .asList(new Locale[]{Locale.GERMAN, Locale.ENGLISH}));

    @Override
    public void configure() {
        install(new DefaultC10NAnnotations());

        setLocaleProvider(new LocaleProvider() {
            @Override
            public Locale getLocale() {

                Locale systemLanguage = Locale.forLanguageTag(Locale
                        .getDefault().getLanguage());
                if (!translatedLocales.contains(systemLanguage)) {
                    return Locale.ENGLISH;
                }
                return systemLanguage;
            }
        });
    }
}
