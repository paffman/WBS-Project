package de.fhbingen.wbs.translation;

import c10n.annotations.De;
import c10n.annotations.En;

/**
 * Error Messages.
 * <p/>
 * Be sure to encode this file ISO-8859-1, else you will need to use unicode
 * escape characters to prevent encoding issues.
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

    @De("Die beiden Passwörter stimmen nicht überein.")
    @En("The supplied passwords must match.")
    String passwordsNotMatchingError();

    @De("Ein Fehler ist beim ändern des Passworts aufgetreten. Bitte " +
            "versuchen Sie es erneut.")
    @En("An error occured while changing your password. Please try again.")
    String passwordChangeError();

    @De("Das alte Passwort ist falsch.")
    @En("The old password is wrong.")
    String passwordOldWrong();

    @De("Bitte alle Felder ausfüllen.")
    @En("Please complete all fields.")
    String fillAllFieldsError();

    @De("Der eingegebene Benutzername ist ungültig.")
    @En("The supplied username is invalid.")
    String userNameInvalid();

    @De("Fehler: Ungültige Eingabe")
    @En("Error: Invalid Input Data")
    String inputErrorWindowTitle();

    @De("Look and Feel konnte nicht geladen werden.")
    @En("Couldn't load look and feel.")
    String couldNotLoadLookAndFeel();

    @De("Ein Fehler ist beim Laden des Datenbanktreibers aufgetreten. " +
            "Bitte vergewissern Sie sich, dass ihre Software richtig " +
            "installiert ist.")
    @En("An error did occur while loading the database driver. Please verify " +
            "" + "the setup of this software.")
    String databaseDriverError();

    @De("Die eingegebene Serveradresse, der Benutzer oder das Passwort ist "
            + "falsch" + ".\nSollte ihre Datenbankinstallation auf einem " +
            "anderen Port " + "liegen, geben sie die Adresse wie folgt " +
            "ein:\nbeispiel.de:port")
    @En("The supplied server address, user or password is wrong.\nIf your " +
            "database runs on a different port, please write your address " +
            "like" + " this:\nexample.com:port")
    String databaseLoginError();

    @De("Der Wert im Projektebenenfeld ist keine gültige Zahl.")
    @En("The Value in project level field is not a number.")
    String projectPropertiesLevelsNotANumber();

    @De("Der Wert im {0}-Feld ist zu hoch.")
    @En("The value in {0}-field too high.")
    String valueTooHigh(String s);

    @De("Der Wert im {0}-Feld ist zu niedrig.")
    @En("The value in {0}-field too low.")
    String valueTooLow(String s);

    @De("Das gewählte Datum ist nicht im korrekten Format dd.mm.yyyy")
    @En("The supplied date is invalid.")
    String dateInvalid();

    @De("Der gewählte Datenbankname ist bereits belegt.")
    @En("The supplied database name is already in use.")
    String databaseNameAlreadyExists();

    @De("Der gewählte Datenbankname ist ungültig.")
    @En("The supplied database name is invalid.")
    String databaseNameInvalid();

    @De("Projekteinrichtungsassistent ist aufgrund eines " + "Fehlers " +
            "fehlgeschlagen. Bitte versuchen sie" + " es erneut.")
    @En("Project setup assistant failed due to an error. Please try " +
            "again.")
    String projectSetupAssistantFailedToCreateProject();

    @De("Weiterführende Information")
    @En("More information")
    String moreInformation();

    @De("Bitte eine Serveradresse eintragen.")
    @En("Please input the server address.")
    String loginMissingHost();

    @De("Bitte einen Datenbanknamen eintragen.")
    @En("Please input the database name.")
    String loginMissingDbName();

    @De("Tragen sie bitte ihren Benutzernamen ein.")
    @En("Please input your username.")
    String loginMissingUser();

    @De("Verbindung konnte nicht aufgebaut werden!")
    @En("Connection could not be established!")
    String loginConnectionFailure();

    @De("Überprüfen sie Benutzernamen und Passwort.")
    @En("Check your login and password.")
    String loginCheckUsername();

    @De("Der Benutzer konnte nicht in der Datenbank gefunden werden.")
    @En("User couldn't be found in the database.")
    String loginUserNotFound();

    @De("Der Benutzer ist kein Projektleiter.")
    @En("The user is not a project manager.")
    String loginMissingPMAtuhority();

    @De("Das Einloggen als Projektleiter ist fehlgeschlagen!")
    @En("The login as project manager has failed!")
    String loginPMLoginFailed();

    @De("Es konnte kein Index-Eintrag für die Datanbank gefunden werden.")
    @En("There was no index-entry found within the database.")
    String loginMissingIndex();

    @De("Überprüfen sie das Index-DB Passwort.")
    @En("Check your index-db password.")
    String loginMissingIndexPw();

    @De("Bitte prüfen Sie Ihre Eingaben.")
    @En("Please validate your inputs.")
    String checkInputs();

    @De("Problem beim lesen der Baseline.")
    @En("Error while loading the baseline.")
    String baselineLoadingError();

    @De("Bitte {0}-Feld ausfüllen.")
    @En("Please fill out {0}-field.")
    String fillFieldError(String s);

    @De("{0} ist keine gültige Zahl.")
    @En("{0} is not a valid number")
    String valueInFieldIsNotANumber(String s);

    @De("Das gewünschte Startdatum kann nicht eingehalten werden.")
    @En("The start date you entered can not be achieved.")
    String startDateCanNotBeAchieved();

    @De("Das gewünschte Enddatum kann nicht eingehalten werden.")
    @En("The end date you entered can not be achieved.")
    String endDateCanNotBeAchieved();

    @De("Timeline konnte nicht exportiert werden")
    @En("Time line could not be exported.")
    String timeLineExportError();

    @De("Diese Verfügbarkeit kann nicht bearbeitet werden, " +
            "" + "da automatisch gesetzt. Bitte verwenden Sie manuelle " +
            "Verfügbarkeiten.")
    @En("Automatic Availability can not be edited. Please use manual " +
            "Availabilities")
    String availabilityCanNotBeChanged();

    @De("Bitte markieren Sie ein Oberarbeitspaket.")
    @En("Please select a top level work package.")
    String selectTopLevelWorkPackage();

    @De("Sie können nur Aufwände zu Arbeitspaketen erfassen, nicht zu OAPs.")
    String noWorkEffortsOnTopLevelWorkPackages();

    @De("Bitte markieren Sie erst ein Arbeitspaket, um dann dort ein neues "
            + "einzufügen.")
    @En("To create a new work package, please select another work package " +
            "first.")
    String selectWorkPackageToAddNew();

    @De("Problem mit der Baumstruktur bei {0}.")
    @En("Tree structure is faulty at {0}")
    String treeStructureProblemAt(String s);

    @De("Bitte markieren Sie das Arbeitspaket das gelöscht werden soll.")
    @En("Please select the work package you want to delete.")
    String markWorkPackageToDelete();

    @De("Es existiert bereits ein Arbeitspaket mit dieser ID. Bitte geben " +
            "Sie" + " eine neue ID ein.")
    @En("Work package ID is already existing. Please choose a different ID.")
    String workPackageIdAlreadyExists();

    @De("Bitte geben Sie die richtige Anzahl an Ebenen ein \n" + " " +
            "Ebenenanzahl ist {0}")
    @En("Level count is wrong. Please insert exactly {0} levels.")
    String workPackageWrongLevel(int levels);

    @De("Es sind keine Oberarbeitspakete zu dieser ID vorhanden.")
    @En("There are no top level work packages with this ID.")
    String workPackageNoTopLevelWorkPackagesForThisId();

    @De("Bitte erst das Arbeitspaket speichern.")
    @En("Please save the work package to continue.")
    String workPackagePleaseSaveToContinue();

    @De("Das Arbeitspaket muss UAP bleiben, da bereits Aufwände eingetragen "
            + "wurden.")
    @En("Work package can not be a top level work package, " +
            "" + "since work efforts have been entered.")
    String workPackageCanNotBeTopLevelBecauseOfWorkEfforts();

    @De("Das Arbeitspaket muss OAP bleiben, da UAPs existieren.")
    @En("The work package needs to be a top level work package, " +
            "" + "since it has sub work packages.")
    String workPackageCanNotBeSubWorkPackageBecauseItHasChildren();

    @De("Falsche ID eingegeben.")
    @En("Wrong ID entered.")
    String workPackageWrongId();

    @De("Name darf nicht leer sein.")
    @En("Name can not be empty.")
    String workPackageNeedsName();

    @De("Projekt muss OAP sein.")
    @En("Project has to be a top level work package.")
    String workPackageRootHasToBeTopLevel();

    @De("Projekt muss aktiv sein")
    @En("Project work package has to be active.")
    String workPackageRootHasToBeActive();

    @De("Das Projekt braucht ein Startdatum.")
    @En("The project needs a start date.")
    String workPackageRootNeedsDate();

    @De("Gewünschtes Enddatum liegt vor dem gewünschten Startdatum.")
    @En("Entered end date can not be previous to the start date.")
    String endDateCanNotBeBeforeStartDate();

    @De("Bitte Leiter auswählen.")
    @En("Please select a manager")
    String workPackageSelectManager();

    @De("Auf unterster Ebene können keine OAP angelegt werden.")
    @En("You are out of levels.")
    String workPackageOutOfLevels();

    @De("{0} muss eine Zahl > 0 sein")
    @En("{0} must be a positive number.")
    String numberMustBePositive(String s);

    @De("Der gewünschte Nachfolger {0} verursacht eine Schleife\n" + "und " +
            "wird nicht eingefügt.")
    @En("The selected successor {0} causes a loop and will not be inserted.")
    String successorLoop(String wp);

    @De("Schleife erkannt")
    @En("Loop detected")
    String successorLoopTitle();

    @De("Fehler beim anlegen der Baseline.")
    @En("Error while creating the baseline.")
    String baselineCreatingError();

    @De("Fehler beim löschen aus der Datenbank.")
    @En("Error while deleting from the database.")
    String deleteFromDbError();

    @De("Paket kann nicht aus der Datenbank gelöscht werden!")
    @En("Package cannot be deleted from the database!")
    String deletePackageFromDbError();

    @De("Es sind bereits Aufwände eingegeben worden, AP kann nicht gelöscht "
            + "werden.")
    @En("There are already efforts for this work package, " +
            "" + "therefore it cannot be deleted.")
    String deletePackageEffortError();
    
    @De("Das Paket kann nicht gelöscht werden, da noch Konflikte dazu existieren.")
    @En("The package cannot be deleted, because there are existing conflicts.")
    String deleteConflictsError();
    
    @De("Das Paket konnte nicht gelöscht werden, da die PV-Einträge nicht gelöscht werden konnten.")
    @En("The package could not be deleted, because there was a failure while deleting the pv-entries.")
    String deletePVError();

    @De("Es sind noch Unterarbeitspakete vorhanden, " + "diese müssen zuerst " +
            "gelöscht werden")
    @En("There are still sub work packages existent. These must be deleted "
            + "first.")
    String deletePackageSubwpError();

    @De("Bitte erst alle Vorgänger / Nachfolger-Beziehungen löschen!")
    @En("There are still predecessors and/or successors. These must be " +
            "deleted first.")
    String deletePackageDependencyError();

    @De("Hauptpaket kann nicht gelöscht werden!")
    @En("Main work package cannot be deleted!")
    String deletePackageMainError();

    @De("Fehler!")
    @En("Error!")
    String error();

    @De("Bitte geben Sie eine korrekte ID an.")
    @En("Please input a correct ID.")
    String idIncorrect();
}
