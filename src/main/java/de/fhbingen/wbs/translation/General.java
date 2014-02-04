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
}
