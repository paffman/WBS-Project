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

    @De("Ein g\u00fcltiges Passwort besteht aus:\n" +
            "- mindestens 6 Zeichen\n" +
            "- mindestens ein Kleinbuchstabe\n" +
            "- mindestens eine Zahl\n" +
            "- mindestens ein Gro\u00dfbuchstabe oder Sonderzeichen\n" +
            "- alle Zeichen m\u00fcssen aus dem US-ASCII Zeichensatz sein " +
            "(kein \u00df," +
            " \u00e4, \u00f6, \u00fc, \u20ac)")
    @En("A valid password must match the following rules:\n" +
            "- at least 6 characters length\n" +
            "- at least one lower case letter\n" +
            "- at least one number\n" +
            "- at least one upper case letter or one special character\n" +
            "- contain only characters of the US-ASCII character set")
    String guidelinesPassword();

    @De("Ein g\u00fcltiger Benutzername besteht aus:\n" +
            "- maximal 10 Zeichen\n" +
            "- einem Buchstaben am Anfang\n" +
            "- danach nur: Buchstaben, Zahlen, '.' und '-'")
    @En("Your username must match following rules:\n" +
            "- maximum of 10 characters\n" +
            "- first character is a letter\n" +
            "- next characters consist of only: letters, numbers, '.' and '-'")
    String guidelinesUsername();

    @De("Folgende Regeln gelten für Datenbanknamen:\n" +
            "- L\u00e4nge: 1-64 Zeichen\n" +
            "G\u00fcltige Zeichen: Buchstaben, Zahlen, '$' und '_'")
    @En("Valid database names consist of:\n" +
            "- 1 to 64 characters in length\n" +
            "- Only consist of: letters, numbers, '$' and '_'")
    String guidelinesDatabaseName();

    @De("Passwort wurde erfolgreich ge\u00e4ndert.")
    @En("Password was changed successfully")
    String passwordChangeConfirm();

    @De("Sind diese Werte richtig?")
    @En("Are these values correct?")
    String valuesCorrect();

    @De("Die Einrichtung Ihres Projekts war erfolgreich.")
    @En("Your project has been setup successfully.")
    String projectSetupSuccess();

    @De("Benutzer ge\u00e4ndert.")
    @En("User changed.")
    String userChanged();

    @De("Benutzer hinzugef\u00fcgt.")
    @En("User added.")
    String userAdded();

    @De("Das Passwort wurde auf \"1234\" zur\u00fcckgesetzt.")
    @En("The password has been reset to \"1234\".")
    String passwordHasBeenReset();

    @De("Die Ressourcen wurden ge\u00e4ndert.")
    String resourcesChanged();

    @De("Neuberechnung starten.")
    String recalculate();

    @De("Ein Wunschdatum wurde ge\u00e4ndert.")
    String wishDateChanged();

    @De("Neue APs wurden erstellt.")
    String newApsWereCreated();

    @De("Abh\u00e4ngigkeiten wurden ge\u00e4ndert.")
    String dependenciesHaveChanged();

    @De("Der BAC eines APs wurde ge\u00e4ndert.")
    String bacHasChanged();

    @De("Es wurde ein AP gel\u00f6scht.")
    String apWasDeleted();

    @De("Es wurde ein AP aktiv/inaktiv gesetzt.")
    String apActiveStateChanged();

    @De("Timeline wurde unter {0} gespeichert")
    String timelineSaved(String outfile);

    @De("Die Dauerberechnung ist eventuell nicht aktuell. Trotzdem Baseline " +
            "berechnen?")
    String durationCalculationNotUpToDate();

    @De("Die Importierte DB wurde berechnet.")
    String importedDbWasCalculated();

    @De("Die Ansicht wurde aktualisiert.")
    String viewWasRefreshed();

    @De("Bitte klicken Sie auf das gewünschte Arbeitspaket, um die Details anzuzeigen.")
    String clickOnWorkpackageToShowDetails();

    @De("Arbeitspaket {0} wurde als {1} angelegt.")
    String workpackageXwasCreatedAsY(String x, String y);

    @De("Arbeitspaket {0} wurde als {1} gelöscht.")
    String workpackageXwasDeletedAsY(String x, String y);

    @De("Wollen Sie das geänderte {0} in die UAP übernehmen?")
    String workPackageApplyDateOnSubWorkPackages(String endOrStartDate);

    @De("Bitte BAC eingeben:")
    String insertBac();

    @De("Geben sie die Zugangsdaten zur Datenbank und ihren Benutzernamen an. Mit einem Klick auf 'OK' gelangen sie dann in das WBS-Tool.")
    @En("Input the access data to the database and your login. The WBS-Tool then starts with a click on 'OK'")
    String loginHelpMsg();

    @De("Es ist bereits ein Projektleiter eingeloggt.\nWollen sie sich trotzdem einloggen?\nWarnung: Die Daten k\u00F6nnen inkonsistent werden, wenn mehrere Projektleiter daran arbeiten.")
    @En("There is already a projekt manager logged in.\nDo you want to log in regardless?\nWarning: Data may become inconsistent, if multiple project managers are working on it.")
    String loginPMSemaphoreOccupied();

    @De("Bitte AP ausw\u00E4hlen.")
    @En("Please select a workpackage.")
    String selectWp();

    @De("Benutzer wurde an der Datenbank abgemeldet.")
    @En("User has logged of.")
    String logOf();
}
