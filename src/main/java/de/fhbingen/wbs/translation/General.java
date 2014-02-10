package de.fhbingen.wbs.translation;

import c10n.annotations.De;
import c10n.annotations.En;

/**
 * Interface for translations that don't fit any specific category e.g.
 * dictionary words.
 * <p/>
 * Be sure to encode this file ISO-8859-1, else you will need to use unicode
 * escape characters to prevent encoding issues.
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

    @De("Ganzt�gig")
    @En("Full-time")
    String fullTime();

    @De("Verf�gbar")
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
    @En("Conflict")
    String conflict();

    @De("Konflikte")
    @En("Conflicts")
    String conflicts();

    @De("Zeitpunkt")
    @En("Point of time")
    String pointOfTime();

    @De("Grund")
    @En("Reason")
    String reason();

    @De("Verursacher")
    @En("Causer")
    String causer();

    @De("�bersicht")
    @En("Overview")
    String overview();

    @De("Projektverf�gbarkeit")
    @En("Project availability")
    String projectAvailability();

    @De("KW")
    @En("WW")
    String calendarWeekAbbreviation();

    @De("Verf�gbarkeit")
    @En("Availability")
    String availability();

    @De("Verf�gbarkeiten")
    @En("Availabilities")
    String availabilities();

    @De("Nicht verf�gbar")
    @En("Not available")
    String notAvailable();

    @De("Warnung")
    @En("Warning")
    String warning();

    @De("Tag")
    @En("Day")
    String day();

    @De("Woche")
    @En("Week")
    String week();

    @De("Monat")
    @En("Month")
    String month();

    @De("Jahr")
    @En("Year")
    String year();

    @De("{0} ausw�hlen")
    @En("Choose {0}")
    String choose(String s);

    @De("Berechtigung")
    @En("Permission")
    String permission();

    @De("Ansicht")
    @En("View")
    String view();

    @De("Ansichten")
    @En("Views")
    String views();

    @De("Dauer")
    @En("Duration")
    String duration();

    @De("Abh�ngigkeit")
    @En("Dependency")
    String dependency();

    @De("Abh�ngigkeiten")
    @En("Dependencies")
    String dependencies();

    @De("Alle")
    @En("All")
    String all();

    @De("Offene")
    @En("Open")
    String open();

    @De("Fertige")
    @En("Finished")
    String finished();

    @De("Vorg�nger")
    @En("Predecessor")
    String predecessor();

    @De("Nachfolger")
    @En("Successor")
    String successor();

    @De("\u20ac")
    @En("\u20ac")
    String currencySymbol();

    @De("Inaktiv")
    @En("Inactive")
    String inactive();

    @De("Verantwortlicher")
    @En("Responsible User")
    String responsiblePerson();

    @De("Verlauf")
    @En("History")
    String history();

    @De("verwalten")
    @En("manage")
    String toManage();

    @De("Initialisierung einer importierten DB")
    @En("Initialization of an imported database")
    String initImportDb();

    @De("VG")
    @En("PRE")
    String predecessoreShort();

    @De("NF")
    @En("SUC")
    String successorShort();

    @De("MA")
    @En("EMP")
    String employeeShort();

}

