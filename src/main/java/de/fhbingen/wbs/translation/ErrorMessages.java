package de.fhbingen.wbs.translation;

import c10n.annotations.De;
import c10n.annotations.En;

/**
 * Error Messages.
 */
public interface ErrorMessages {
    @De("Wert im {0}-Feld ist zu lang.")
    @En("Value in {0}-field too long.")
    String stringTooLong(String item);

    @De("Passwort entspricht nicht den Passwortregeln.")
    String passwordInvalidError();

    @De("Das eingegebene Passwort ist ung\u00fcltig.")
    String passwordWrongError();

    @De("Die beiden Passw\u00f6rter stimmen nicht \u00fcberein.")
    String passwordsNotMatchingError();

    @De("Fehler beim \u00e4ndern des Passworts.")
    String passwordChangeError();

    @De("Das alte Passwort ist ung\u00fcltig.")
    String passwordOldWrong();

    @De("Bitte alle Felder ausf\u00fcllen.")
    String fillAllFieldsError();

    @De("Nutzername ung\u00fcltig.")
    String userNameInvalid();

    @De("Fehler: Ung\u00fcltige Eingabe")
    @En("Error: Invalid Input Data")
    String inputErrorWindowTitle();

    @De("Look and Feel konnte nicht geladen werden.")
    @En("Couldn't load look and feel.")
    String couldNotLoadLookAndFeel();
}
