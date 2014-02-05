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

    @De("Zurück")
    @En("Back")
    String back();

    @De("OK")
    @En("OK")
    String ok();

    @De("Abbrechen")
    @En("Cancel")
    String cancel();

    @De("Bestätigen")
    @En("Confirm")
    String confirm();

    @De("Beenden")
    @En("Exit")
    String exit();

    @De("Schließen")
    @En("Close")
    String close();

    @De("Löschen")
    @En("Delete")
    String delete();

    @De("{0} löschen")
    @En("Delete {0}")
    String delete(String s);

    @De("Passwort zurücksetzen")
    @En("Reset password")
    String passwordReset();

    @De("{0} speichern")
    @En("Save {0}")
    String save(String s);

    @De("{0} anzeigen")
    @En("Show {0}")
    String show(String s);

    @De("Neues {0} anlegen")
    @En("Add new {0}")
    String addNewNeutral(String s);


    @De("Neuen {0} anlegen")
    @En("Add new {0}")
    String addNewMale(String s);


    @De("Neue {0} anlegen")
    @En("Add new {0}")
    String addNewFemale(String s);


    @De("{0} anlegen")
    @En("Add {0}")
    String add(String s);

    @De("Hinzufügen")
    @En("Add")
    String add();

    @De("{0} eintragen")
    @En("Enter {0}")
    String enter(String s);

    @De("Ausklappen")
    @En("Expand")
    String expand();

    @De("Einklappen")
    @En("Collapse")
    String collapse();

    @De("Aktualisieren")
    @En("Refresh")
    String refresh();

    @De("{0} aktualisieren")
    @En("Refresh {0}")
    String refresh(String s);

    @De("{0} berechnen")
    @En("Calculate {0}")
    String calculate(String s);

    @De("Datei")
    @En("File")
    String file();

    @De("Hilfe")
    @En("Help")
    String help();

    @De("Info")
    @En("Info")
    String info();

    @De("{0} ändern")
    @En("Change {0}")
    String change(String password);

    @De("Abmelden")
    @En("Logout")
    String logout();

    @De("Verwalten")
    @En("Manage")
    String manage();

    @De("Übernehmen")
    @En("Apply")
    String apply();
}
