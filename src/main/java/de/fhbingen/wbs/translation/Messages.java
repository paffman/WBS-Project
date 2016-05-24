package de.fhbingen.wbs.translation;

import c10n.annotations.De;
import c10n.annotations.En;

/**
 * Translation class for user reply messages.
 * <p/>
 * Be sure to encode this file ISO-8859-1, else you will need to use unicode
 * escape characters to prevent encoding issues.
 */
public interface Messages extends ErrorMessages {

    @De("Sind sie sicher, dass sie {0} wollen?")
    @En("Are you sure you want to {0}?")
    String confirmText(String actionName);

    @De("Ein g\u00fcltiges Passwort besteht aus:\n" +
            "- mindestens 6 Zeichen\n" +
            "- mindestens ein Kleinbuchstabe\n" +
            "- mindestens eine Zahl\n" +
            "- mindestens ein Großbuchstabe oder Sonderzeichen\n" +
            "- alle Zeichen müssen aus dem US-ASCII Zeichensatz sein " +
            "(kein ß," +
            " ä, ö, ü, ?, etc.)")
    @En("A valid password must match the following rules:\n" +
            "- at least 6 characters length\n" +
            "- at least one lower case letter\n" +
            "- at least one number\n" +
            "- at least one upper case letter or one special character\n" +
            "- contain only characters of the US-ASCII character set")
    String guidelinesPassword();

    @De("Ein gültiger Benutzername besteht aus:\n" +
            "- maximal 11 Zeichen\n" +
            "- einem Buchstaben am Anfang\n" +
            "- danach nur: Buchstaben, Zahlen, '.' und '-'")
    @En("Your username must match following rules:\n" +
            "- maximum of 11 characters\n" +
            "- first character is a letter\n" +
            "- next characters consist of only: letters, numbers, '.' and '-'")
    String guidelinesUsername();

    @De("Folgende Regeln gelten für Datenbanknamen:\n" +
            "- Länge: 1-64 Zeichen\n" +
            "Gültige Zeichen: Buchstaben, Zahlen, '$' und '_'")
    @En("Valid database names consist of:\n" +
            "- 1 to 64 characters in length\n" +
            "- Only consist of: letters, numbers, '$' and '_'")
    String guidelinesDatabaseName();

    @De("Passwort wurde erfolgreich geändert.")
    @En("Password was changed successfully")
    String passwordChangeConfirm();

    @De("Sind diese Werte richtig?")
    @En("Are these values correct?")
    String valuesCorrect();

    @De("Die Einrichtung Ihres Projekts war erfolgreich.")
    @En("Your project has been setup successfully.")
    String projectSetupSuccess();

    @De("Benutzer geändert.")
    @En("User changed.")
    String userChanged();

    @De("Benutzer hinzugefügt.")
    @En("User added.")
    String userAdded();

    @De("Das Passwort wurde auf \"1234\" zurückgesetzt.")
    @En("The password has been reset to \"1234\".")
    String passwordHasBeenReset();

    @De("Die Ressourcen wurden geändert.")
    @En("Resources have changed.")
    String resourcesChanged();

    @De("Neuberechnung starten.")
    @En("Recalculate.")
    String recalculate();

    @De("Ein Wunschdatum wurde geändert.")
    @En("A target date has been changed.")
    String targetDateChanged();

    @De("Neue APs wurden erstellt.")
    @En("New work packages were created.")
    String newApsWereCreated();

    @De("Abhängigkeiten wurden geändert.")
    @En("Dependencies were changed.")
    String dependenciesHaveChanged();

    @De("Der BAC eines APs wurde geändert.")
    @En("The BAC of a work package was changed.")
    String bacHasChanged();

    @De("Es wurde ein AP gelöscht.")
    @En("A work package was deleted.")
    String apWasDeleted();

    @De("Es wurde ein AP aktiv/inaktiv gesetzt.")
    @En("The active state of a work package was changed.")
    String apActiveStateChanged();

    @De("Timeline wurde unter {0} gespeichert")
    @En("Time line was saved at {0}")
    String timeLineSaved(String outfile);

    @De("Die Dauerberechnung ist eventuell nicht aktuell. Trotzdem Baseline "
            + "berechnen?")
    @En("The calculation of the duration is not up to date. Do you want to " +
            "calculate the baseline anyways?")
    String durationCalculationNotUpToDate();

    @De("Die Importierte DB wurde berechnet.")
    @En("Imported database was recalculated.")
    String importedDbWasCalculated();

    @De("Die Ansicht wurde aktualisiert.")
    @En("The view was refreshed.")
    String viewWasRefreshed();

    @De("Bitte klicken Sie auf das gewünschte Arbeitspaket, um die Details anzuzeigen.")
    @En("Please click on the desired work package to show more information.")
    String clickOnWorkpackageToShowDetails();

    @De("Arbeitspaket {0} wurde als {1} hinzugefügt.")
    @En("Work package {0} was added as {1}")
    String workPackageXwasCreatedAsY(String x, String y);

    @De("Arbeitspaket {0} wurde als {1} gelöscht.")
    @En("Work package {0} was deleted as {1}")
    String workpackageXwasDeletedAsY(String x, String y);

    @De("Wollen Sie das geänderte {0} in die UAP übernehmen?")
    @En("Do you want to apply the changed {0} on all sub work packages?")
    String workPackageApplyDateOnSubWorkPackages(String endOrStartDate);

    @De("Bitte BAC eingeben:")
    @En("Please input BAC:")
    String insertBac();

    @De("Geben sie die Zugangsdaten zur Datenbank und ihren Benutzernamen an" +
            "." + " Mit einem Klick auf 'OK' gelangen sie dann in das " +
            "WBS-Tool.")
    @En("Input the access data to the database and your login. The WBS-Tool "
            + "then starts with a click on 'OK'")
    String loginHelpMsg();

    @De("Es ist bereits ein Projektleiter eingeloggt.\nWollen sie sich " +
            "trotzdem einloggen?\nWarnung: Die Daten können inkonsistent " +
            "werden, wenn mehrere Projektleiter daran arbeiten.")
    @En("There is already a projekt manager logged in.\nDo you want to log in" +
            " regardless?\nWarning: Data may become inconsistent, " +
            "if multiple project managers are working on it.")
    String loginPMSemaphoreOccupied();

    @De("Bitte AP auswählen.")
    @En("Please select a workpackage.")
    String selectWp();

    @De("Benutzer wurde an der Datenbank abgemeldet.")
    @En("User was logged out.")
    String logOut();
    
    @De("Die Planned Values werden neu berechnet. Wollen sie fortfahren?")
    @En("The Planned Values will be recalculated. Do you want to continue?")
    String pvChange();

    @De("Das Arbeitspacket wurde erfolgreich umgehängt.")
    @En("The orkpackage has successfully been moved.")
    String wpMoveWpHasBeenMoved();

    @De("Beim Umhängen des Arbeitspacketes, ist ein Fehler aufgetreten.")
    @En("An Error has occured, while moving the workpackage.")
    String wpMoveError();

    @De("Es wurde kein Zielarbeitspaket ausgewählt. Ziehen Sie das Arbeitspacket auf ein anderes Arbeitspacket.")
    @En("No target workpackage has been selected. Drag the workpackage on to another workpackage.")
    String wpMoveNoneSelected();

    @De("Das Zielarbeitspacket ist gleich dem Ausgangsarbeitspacket.")
    @En("The target workpackage is the same as the source workpackage.")
    String wpMoveWpIsItself();

    @De("Das Arbeitspacket befindet sich bereits an dieser Position.")
    @En("The workpackage already is at this position.")
    String wpMoveParentOfTargetWp();

    @De("Das Arbeitspacket ist bereits ein OAP des Ziel Arbeitspacket.")
    @En("The workpackage is a parent workpackage of the target workpackage.")
    String wpMoveAlreadyIsChild();

    @De("Das Zielarbeitspacket ist kein OAP.")
    @En("The target workpackage is no parent workpackage")
    String wpMoveTargetWpIsNoOAP();

    @De("Das Verschieben ist nicht möglich, da dabei die maximale Tiefe überschritten werden würde.")
    @En("Movin this workpackage is not possible, since this would exceed the maximal depth.")
    String wpMoveMaxDepth();
}
