package de.fhbingen.wbs.translation;

import c10n.annotations.De;
import c10n.annotations.En;

/**
 * Interface for translations that don't fit any specific category e.g.
 * dictionary words.
 */
public interface General {
    @De("Datum")
    @En("Date")
    String date();

    @De("Beschreibung")
    @En("Description")
    String description();

    @De("Start")
    @En("Start")
    String start();

    @De("optional")
    @En("optional")
    String optional();

    @De("Ende")
    @En("End")
    String end();

    @De("Ganztägig")
    @En("Full-time")
    String fullTime();

    @De("Verfügbar")
    @En("Available")
    String available();

    @De("Kosten")
    @En("Costs")
    String costs();

    @De("{0} Kosten")
    @En("{0} costs")
    String costs(String s);

    @De("Trend")
    @En("Trend")
    String trend();

    @De("Status")
    @En("Status")
    String status();

    @De("Konflikt")
    String conflict();

    @De("Zeitpunkt")
    String pointOfTime();

    @De("Grund")
    String reason();

    @De("Verursacher")
    String causer();

    @De("Übersicht")
    String overview();

    @De("Projektverfügbarkeit")
    String projectAvailability();

    @De("KW")
    String calendarWeekAbbreviation();

    @De("Verfügbarkeit")
    String availablility();

    @De("Nicht verfügbar")
    String notAvailable();

    @De("Warnung")
    String warning();

    @De("Tag")
    String day();

    @De("Woche")
    String week();

    @De("Monat")
    String month();

    @De("Jahr")
    String year();

    @De("{0} auswählen")
    String choose(String s);

    @De("Berechtigung")
    String permission();

    @De("Ansichten")
    String views();

    @De("Ansicht")
    String view();

    @De("Dauer")
    String duration();

    @De("Abhängigkeiten")
    String dependencies();

    @De("Konflikte")
    String conflicts();

    @De("Verfügbarkeiten")
    String availabilities();

    @De("Alle")
    String all();

    @De("Offene")
    String open();

    @De("Fertige")
    String finished();

    @De("Vorgänger")
    String predecessor();

    @De("Nachfolger")
    String successor();

    @De("Abhängigkeit")
    String dependency();

    @De("\u20ac")
    String currencySymbol();

    @De("Inaktiv")
    String inactive();

    @De("Verantwortlicher")
    String responsiblePerson();

    @De("Verlauf")
    String history();

    @De("verwalten")
    String toManage();
}
