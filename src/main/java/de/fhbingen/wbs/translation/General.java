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

    @De("Übersicht")
    @En("Overview")
    String overview();

    @De("Projektverfügbarkeit")
    @En("Project availability")
    String projectAvailability();

    @De("KW")
    @En("WW")
    String calendarWeekAbbreviation();

    @De("Verfügbarkeit")
    @En("Availability")
    String availability();

    @De("Verfügbarkeiten")
    @En("Availabilities")
    String availabilities();

    @De("Nicht verfügbar")
    @En("Not available")
    String notAvailable();

    @De("Warnung")
    @En("Warning")
    String warning();

    @De("Tag")
    @En("Day")
    String day();

    @De("Tage")
    @En("Days")
    String days();

    @De("Stunden")
    @En("Hours")
    String hours();

    @De("Woche")
    @En("Week")
    String week();

    @De("Monat")
    @En("Month")
    String month();

    @De("Jahr")
    @En("Year")
    String year();

    @De("{0} auswählen")
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

    @De("Abhängigkeit")
    @En("Dependency")
    String dependency();

    @De("Abhängigkeiten")
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

    @De("Vorgänger")
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

    @De("Ordner")
    @En("Folder")
    String folder();

    @De("JPG Bilder (*.jpg, *.jpeg)")
    @En("JPG Images (*.jpg, *.jpeg)")
    String jpgImages();

    @De("und")
    @En("and")
    String and();

    @De("Testfall")
    @En("Testcase")
    String testcase();

    @De("Vorbedingungen")
    @En("Preconditions")
    String precondition();

    @De("Erwartetes Ergebnis")
    @En("Expected result")
    String expectedResult();

    @De("Bemerkung")
    @En("Remark")
    String remark();

    @De("Ergebnis")
    @En("Result")
    String result();

    @De("\"Beschreibung- und Erwatetes Ergebnis\" sind Pflicht!")
    @En("\"Desciption and expected result field\" must be set!")
    String descriptionExpectedResultMessage();

    @De("\"Testnamen Feld\" ist Plficht!")
    @En("\"Testname field\" must be set!")
    String testnameMessage();

    @De("Es existieren keine Testfälle!")
    @En("No Testcases exists")
    String testcaseMessage();

    @De("Wollen Sie dieses Arbeitspaket wirklich verschieben?")
    @En("Do you really want to move this workpackage?")
    String confirmMovingWP();
}

