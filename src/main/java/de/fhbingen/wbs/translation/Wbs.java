package de.fhbingen.wbs.translation;

import c10n.annotations.De;
import c10n.annotations.En;

/**
 * Translation interface for WBS related terms.
 * <p/>
 * Be sure to encode this file ISO-8859-1, else you will need to use unicode
 * escape characters to prevent encoding issues.
 */
public interface Wbs {
    @De("Aufwand für ")
    @En("Work effort on ")
    String addAufwandWindowTitle();

    @De("Arbeitspaket ID")
    @En("Work package ID")
    String workPackageId();

    @De("Arbeitspaketname")
    @En("Work package name")
    String workPackageName();

    @De("Aufwand")
    @En("Work effort")
    String workEffort();

    @De("Verfügbarkeit bearbeiten")
    @En("Edit availability")
    String editAvailability();

    @De("Neue Verfügbarkeit")
    @En("New availability")
    String newAvailability();

    @De("Arbeitspaket")
    @En("Work package")
    String workPackage();

    @De("{0} Arbeitspakete")
    @En("{0} Work packages")
    String workPackages(String adjective);

    @En("EAC")
    @De("EAC")
    String eac();

    @En("BAC")
    @De("BAC")
    String bac();

    @En("AC")
    @De("AC")
    String ac();

    @En("ETC")
    @De("ETC")
    String etc();

    @En("CPI")
    @De("CPI")
    String cpi();

    @En("EV")
    @De("EV")
    String ev();

    @En("PV")
    @De("PV")
    String pv();

    @En("SV")
    @De("SV")
    String sv();

    @En("SPI")
    @De("SPI")
    String spi();

    @De("Tagessatz")
    @En("Daily rate")
    String dailyRate();

    @De("Betroffene Arbeitspakete")
    @En("Affected work packages")
    String affectedWorkpackages();

    @De("Timeline")
    @En("Time line")
    String timeLine();

    @De("Manuelle Verfügbarkeiten")
    @En("Manual availabilities")
    String manualAvailabilities();

    @De("Baseline")
    @En("Baseline")
    String baseline();

    @De("CPI-Diagramm")
    @En("CPI-Graph")
    String cpiGraph();

    @De("{0}-Diagramm")
    @En("{0}-Graph")
    String graph(String s);


    @De("Fertigstellung")
    @En("Completion")
    String completion();

    @De("Baseline-Berechnung")
    @En("Baseline calculation")
    String baselineCalculation();

    @De("Aufwand eintragen")
    @En("Insert work effort")
    String insertWorkEffort();

    @De("Mitarbeiter")
    @En("Staff member")
    String staff();

    @De("CPI-Farben")
    @En("CPI-Colors")
    String cpiColors();

    @De("Vorgänger / Nachfolger")
    @En("Edit dependencies")
    String addDependencyWindowTitle();

    @De("Start (errechnet)")
    @En("Start (calculated)")
    String calculatedStart();

    @De("Release (errechnet)")
    @En("Release (calculated)")
    String calculatedRelease();

    @De("CPI / SPI")
    @En("CPI / SPI")
    String cpiAndSpi();

    @De("in Tagen")
    @En("in days")
    String inDays();

    @De("{0} in Tagen")
    @En("{0} in days")
    String inDays(String s);

    @De("{0} erfassen")
    @En("Enter {0}")
    String enter(String s);

    @De("Neues Arbeitspaket anlegen")
    @En("Add new work package")
    String addNewWorkPackageWindowTitle();

    @De("Geändertes Datum")
    @En("Date Changed")
    String dateChangedWindowTitle();
}
