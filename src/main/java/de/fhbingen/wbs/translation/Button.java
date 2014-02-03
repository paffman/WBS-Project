package de.fhbingen.wbs.translation;

import c10n.annotations.De;
import c10n.annotations.En;

/**
 * Interface with general purpose button translations.
 */
public interface Button {
    @De("Weiter")
    @En("Next")
    String next();

    @De("Zur\u00fcck")
    @En("Back")
    String back();

    @De("OK")
    @En("OK")
    String ok();

    @De("Abbrechen")
    @En("Cancel")
    String cancel();

    @De("Best\u00e4tigen")
    @En("Confirm")
    String confirm();

    @De("Schlie\u00dfen")
    @En("Exit")
    String exit();
}
