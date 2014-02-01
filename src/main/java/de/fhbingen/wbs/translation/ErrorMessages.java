package de.fhbingen.wbs.translation;

import c10n.annotations.De;
import c10n.annotations.En;

/**
 * Error Messages.
 */
public interface ErrorMessages {
    @De("Das gewählte Passwort entspricht nicht den Passwortregeln.")
    @En("The supplied password does not match the rules.")
    String passwordInvalidError();

    @De("Wert im {0}-Feld ist zu lang.")
    @En("Value in {0}-field too long.")
    String stringTooLong(String item);

    @De("Der eingegebene Benutzername oder das Passwort ist falsch.")
    @En("The supplied password or username is wrong.")
    String loginWrongError();

    @De("Die beiden Passw\u00f6rter stimmen nicht \u00fcberein.")
    @En("The supplies passwords must match.")
    String passwordsNotMatchingError();

    @De("Ein Fehler ist beim \u00e4ndern des Passworts aufgetreten. Bitte " +
            "versuchen Sie es erneut.")
    @En("An error occured while changing your password. Please try again.")
    String passwordChangeError();

    @De("Das alte Passwort ist falsch.")
    @En("The old password is wrong.")
    String passwordOldWrong();

    @De("Bitte alle Felder ausf\u00fcllen.")
    @En("Please complete all fields.")
    String fillAllFieldsError();

    @De("Der eingegebene Benutzername ist ung\u00fcltig.")
    @En("The supplied username is invalid.")
    String userNameInvalid();

    @De("Fehler: Ung\u00fcltige Eingabe")
    @En("Error: Invalid Input Data")
    String inputErrorWindowTitle();

    @De("Look and Feel konnte nicht geladen werden.")
    @En("Couldn't load look and feel.")
    String couldNotLoadLookAndFeel();

    @De("Ein Fehler ist beim Laden des Datenbanktreibers aufgetreten. " +
            "Bitte vergewissern Sie sich, dass ihre Software richtig " +
            "installiert ist.")
    @En("An error did occur while loading the database driver. Please verify " +
            "the setup of this software.")
    String databaseDriverError();

    @De("Die eingegebene Serveradresse, der Benutzer oder das Passwort ist " +
            "falsch" +
            ".\nSollte ihre Datenbankinstallation auf einem anderen Port " +
            "liegen, geben sie die Adresse wie folgt ein:\nbeispiel.de:port")
    @En("The supplied server address, user or password is wrong.\nIf your " +
            "database runs on a different port, please write your address like" +
            " this:\nexample.com:port")
    String databaseLoginError();

    @De("Der Wert im Projektebenenfeld ist keine g\u00fcltige Zahl.")
    @En("The Value in project level field is not a number.")
    String projectPropertiesLevelsNotANumber();

    @De("Der Wert im {0}-Feld ist zu hoch.")
    @En("The value in {0}-field too high.")
    String valueTooHigh(String s);

    @De("Der Wert im {0}-Feld ist zu niedrig.")
    @En("The value in {0}-field too low.")
    String valueTooLow(String s);

    @De("Das gew\u00e4hlte Datum ist ung\u00fcltig.")
    @En("The supplied date is invalid.")
    String dateInvalid();

    @De("Der gew\u00e4hlte Datenbankname ist bereits belegt.")
    @En("The supplied database name is already in use.")
    String databaseNameAlreadyExists();

    @De("Der gew\u00e4hlte Datenbankname ist ung\u00fcltig.")
    @En("The supplied database name is invalid.")
    String databaseNameInvalid();

    @De("Projekteinrichtungsassistent ist aufgrund eines " +
            "Fehlers fehlgeschlagen. Bitte versuchen sie" +
            " es erneut.")
    @En("Project setup assistant failed due to an error. Please try " +
            "again.")
    String projectSetupAssistantFailedToCreateProject();

    @De("Weiterf\u00fchrende Information")
    @En("More information")
    String moreInformation();
}
