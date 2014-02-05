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
    @En("The supplied passwords must match.")
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

    @De("Das gew\u00e4hlte Datum ist nicht im korrekten Format dd.mm.yyyy")
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

    @De("Bitte prüfen Sie Ihre Eingaben.")
    @En("Please validate your inputs.")
    String checkInputs();

    @De("Problem beim lesen der Baseline.")
    @En("Error while loading the baseline.")
    String baselineLoadingError();

    @De("Bitte {0}-Feld ausf\u00fcllen.")
    @En("Please fill out {0}-field.")
    String fillFieldError(String s);

    @De("{0} ist keine g\u00fcltige Zahl.")
    @En("{0} is not a valid number")
    String valueInFieldIsNotANumber(String s);

    @De("Das gewünschte Startdatum kann nicht eingehalten werden.")
    String startDateCantBeAchieved();

    @De("Das gewünschte Enddatum kann nicht eingehalten werden.")
    String endDateCantBeAchieved();

    @De("Timeline konnte nicht exportiert werden")
    String timelineExportError();

    @De("Diese Verfügbarkeit kann nicht bearbeitet werden, da automatisch gesetzt. Bitte verwenden Sie manuelle Verfügbarkeiten!")
    String availablilityCanNotBeChanged();

    @De("Bitte markieren Sie ein Oberarbeitspaket!")
    String selectTopLevelWorkpackage();

    @De("Sie können nur Aufwände zu Arbeitspaketen erfassen, nicht zu OAPs!")
    String noWorkEffortsOnTopLevelWorkpackages();

    @De("Bitte markieren Sie erst ein Arbeitspaket, um dann dort ein neues einzufügen!")
    String selectWorkPackageToAddNew();

    @De("Problem mit der Baumstruktur bei {0}.")
    String treeStructureProblemAt(String s);

    @De("Bitte markieren Sie das Arbeitspaket das gelöscht werden soll.")
    String markWorkpackageToDelete();

    @De("Es existiert bereits ein Arbeitspaket mit dieser ID. Bitte geben Sie" +
            " eine neue ID ein.")
    String workPackageIdAlreadyExists();

    @De("Bitte geben Sie die richtige Anzahl an Ebenen ein \n"
            + " Ebenenanzahl ist {0}")
    String workpackageWrongLevel(int levels);

    @De("Es sind keine Oberarbeitspakete zu dieser ID vorhanden.")
    String workPackageNoTopLevelWorkPackagesForThisId();

    @De("Bitte erst das Arbeitspaket speichern.")
    String workPackagePleaseSaveToContinue();

    @De("Das Arbeitspaket muss UAP bleiben, da bereits Aufwände eingetragen " +
            "wurden.")
    String workPackageCanNotBeTopLevelBecauseOfWorkEfforts();

    @De("Das Arbeitspaket muss OAP bleiben, da UAPs existieren")
    String workPackageCanNotBeSubWorkPackageBecauseItHasChildren();

    @De("Falsche ID eingegeben.")
    String workPackageWrongId();

    @De("Name darf nicht leer sein.")
    String workPackageNeedsName();

    @De("Projekt muss OAP sein.")
    String workPackageRootHasToBeTopLevel();

    @De("Projekt muss aktiv sein")
    String workPackageRootHasToBeActive();

    @De("Das Projekt braucht ein Startdatum.")
    String workPackageRootNeedsDate();

    @De("Gewünschtes Enddatum liegt vor dem gewünschten Startdatum.")
    String endDateCanNotBeBeforeStartDate();

    @De("Bitte Leiter auswählen.")
    String workPackageSelectManager();

    @De("Auf unterster Ebene koennen keine OAP angelegt werden.")
    String workPackageOutOfLevels();

    @De("{0} muss eine Zahl > 0 sein")
    String numberMustBePositive(String s);

}
