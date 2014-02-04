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

    @De("ArbeitspaketID")
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

    @De("EAC")
    String eac();

    @De("BAC")
    String bac();

    @De("AC")
    String ac();

    @De("ETC")
    String etc();

    @De("CPI")
    String cpi();

    @De("EV")
    String ev();

    @De("PV")
    String pv();

    @De("SV")
    String sv();
    @De("SPI")
    String spi();

    @De("Tagessatz")
    @En("Daily rate")
    String dailyRate();

    @De("Betroffene Arbeitspakete")
    String affectedWorkpackages();
}
