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

    @De("Schließen")
    @En("Close")
    String close();

    @De("L\u00f6schen")
    @En("Delete")
    String delete();

    @De("{0} löschen")
    String delete(String s);

    @De("Passwort zur\u00fccksetzen")
    @En("Reset password")
    String passwordReset();

    @De("{0} speichern")
    String save(String s);

    @De("{0} anzeigen")
    String show(String s);

    @De("Neues {0} anlegen")
    String addNewNeutral(String s);


    @De("Neuen {0} anlegen")
    String addNewMale(String s);


    @De("Neue {0} anlegen")
    String addNewFemale(String s);



    @De("{0} anlegen")
    String add(String s);

    @De("Hinzufügen")
    String add();

    @De("{0} eintragen")
    String register(String s);

    @De("Ausklappen")
    String expand();

    @De("Einklappen")
    String collapse();

    @De("{0} aktualisieren")
    String refresh(String s);

    @De("{0} berechnen")
    String calculate(String s);

    @De("Datei")
    String file();

    @De("Hilfe")
    String help();

    @De("Aktualisieren")
    String refresh();

    @De("{0} ändern")
    String change(String password);

    @De("Abmelden")
    String logout();

    @De("Info")
    String info();

    @De("Verwalten")
    String manage();

    @De("Übernehmen")
    String apply();
}
