package de.fhbingen.wbs.translation;

import c10n.annotations.De;
import c10n.annotations.En;

/**
 * Translation class for user reply messages.
 */
public interface Messages extends ErrorMessages{

    @De("Sind sie sicher, dass sie {0} wollen?")
    @En("Are you sure you want to {0}?")
    String confirmText(String actionName);

    String passwordGuidelines();

    @De("Passwort wurde erfolgreich ge\u00e4ndert.")
    String passwordChangeConfirm();

    @De("Sind diese Werte richtig?")
    String valuesCorrect();
}
