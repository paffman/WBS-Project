package de.fhbingen.wbs.translation;

import c10n.annotations.De;
import c10n.annotations.En;

/**
 * Translation interface for WBS related terms.
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

    @De("Verf\u00fcgbarkeit bearbeiten")
    @En("Edit availability")
    String editAvailability();

    @De("Neue Verf\u00fcgbarkeit")
    @En("New availability")
    String newAvailability();

    @De("Arbeitspaket")
    @En("Work package")
    String workPackage();

    @De("{0} Arbeitspakete")
    @En("{0} Work packages")
    String workPackages(String adjective);

    @En("EAC")
    String eac();

    @En("BAC")
    String bac();

    @En("AC")
    String ac();

    @En("ETC")
    String etc();

    @En("CPI")
    String cpi();

    @En("EV")
    String ev();

    @En("PV")
    String pv();

    @En("SV")
    String sv();

    @En("SPI")
    String spi();

    @De("Tagessatz")
    @En("Daily rate")
    String dailyRate();

    @De("Betroffene Arbeitspakete")
    String affectedWorkpackages();

    @De("Timeline")
    @En("Timeline")
    String timeline();

    @De("Manuelle Verfügbarkeiten")
    String manualAvailabilities();

    @En("Baseline")
    String baseline();

    @De("CPI-Diagramm")
    @En("CPI-Graph")
    String cpiGraph();

    @De("{0}-Diagramm")
    @En("{0}-Graph")
    String graph(String s);



    @De("Fertigstellung")
    String completion();

    @De("Baseline-Berechnung")
    String baselineCalculation();

    @De("Aufwand eintragen")
    String insertWorkEffort();

    @De("Mitarbeiter")
    String staff();

    @De("CPI-Farben")
    String cpiColors();

    @De("Vorgänger / Nachfolger")
    String addDependencyWindowTitle();

    @De("Start (errechnet)")
    String calculatedStart();

    @De("Release (errechnet)")
    String calculatedRelease();

    @De("CPI / SPI")
    String cpiAndSpi();

    @De("in Tagen")
    String inDays();

    @De("{0} in Tagen")
    String inDays(String s);

    @De("{0} erfassen")
    String enter(String s);

    @De("Neues Arbeitspaket anlegen")
    String addNewWorkPackageWindowTitle();

    @De("Geändertes Datum")
    String dateChangedWindowTitle();
}
