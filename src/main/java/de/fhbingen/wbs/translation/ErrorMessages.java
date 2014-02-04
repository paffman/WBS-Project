package de.fhbingen.wbs.translation;

import c10n.annotations.De;
import c10n.annotations.En;

/**
 * Error Messages.
 */
public interface ErrorMessages {
    @De("Das gew\u00E4hlte Passwort entspricht nicht den Passwortregeln.")
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

    @De("Ein Fehler ist beim \u00e4ndern des Passworts aufgetreten. Bitte "
            + "versuchen Sie es erneut.")
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

    @De("Ein Fehler ist beim Laden des Datenbanktreibers aufgetreten. "
            + "Bitte vergewissern Sie sich, dass ihre Software richtig "
            + "installiert ist.")
    @En("An error did occur while loading the database driver. Please verify "
            + "the setup of this software.")
    String databaseDriverError();

    @De("Die eingegebene Serveradresse, der Benutzer oder das Passwort ist "
            + "falsch"
            + ".\nSollte ihre Datenbankinstallation auf einem anderen Port "
            + "liegen, geben sie die Adresse wie folgt ein:\nbeispiel.de:port")
    @En("The supplied server address, user or password is wrong.\nIf your "
            + "database runs on a different port, please write your address like"
            + " this:\nexample.com:port")
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

    @De("Projekteinrichtungsassistent ist aufgrund eines "
            + "Fehlers fehlgeschlagen. Bitte versuchen sie" + " es erneut.")
    @En("Project setup assistant failed due to an error. Please try "
            + "again.")
    String projectSetupAssistantFailedToCreateProject();

    @De("Weiterf\u00fchrende Information")
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

    @De("\u00DCberpr\u00fcfen sie Benutzernamen und Passwort.")
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
    
    @De("Es konnte kein Index-Eintrag f�r die Datanbank gefunden werden.")
    @En("There was no index-entry found within the database.")
    String loginMissingIndex();
    
    @De("\u00DCberpr\u00fcfen sie das Index-DB Passwort.")
    @En("Check your index-db password.")
    String loginMissingIndexPw();

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
    
    @De("Der gew\u00FCnschte Nachfolger {0} verursacht eine Schleife\nund wird nicht eingef\u00FCgt.")
    @En("The selected successor {0} causes a loop and will not be inserted.")
    String successorLoop(String wp);
    @De("Schleife erkannt")
    @En("Loop detected")
    String successorLoopTitle();
    
    @De("Fehler beim anlegen der Baseline.")
    @En("Error while creating the baseline.")
    String baselineCreatingError();
    
    @De("Fehler beim l\u00F6schen aus der Datenbank.")
    @En("Error while deleting from the database.")
    String deleteFromDbError();
    
    @De("Paket kann nicht aus der Datenbank gel\u00F6scht werden!")
    @En("Pakage cannot be deleted from the database!")
    String deletePackageFromDbError();
    
    @De("Es sind bereits Aufw\u00E4nde eingegeben worden, AP kann nicht gel\u00F6scht werden.")
    @En("There are already efforts for this workpackage, therefore it cannot be deleted.")
    String deletePackageEffortError();
    
    @De("Es sind noch Unterarbeitspakete vorhanden, diese m\u00FCssen zuerst gel\u00F6scht werden")
    @En("There are still subworkpackages existent. These must be deleted first.")
    String deletePackageSubwpError();
    
    @De("Bitte erst alle Vorg\u00E4nger / Nachfolger-Beziehungen l\u00F6schen!")
    @En("There are still predecessors and/or successors. Thes must be deleted first.")
    String deletePackageDependencyError();
            
    @De("Hauptpaket kann nicht gel\u00F6scht werden!")
    @En("Main workpackage cannot be deleted!")
    String deletePackageMainError();
    
    @De("Fehler!")
    @En("Error!")
    String error();
    
    @De("Bitte geben Sie eine korrekte ID an.")
    @En("Plese input a coorect ID.")
    String idIncorrect();
}
