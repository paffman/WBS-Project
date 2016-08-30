package de.fhbingen.wbs.translation;

import c10n.annotations.De;
import c10n.annotations.En;

/**
 * Be sure to encode this file ISO-8859-1, else you will need to use unicode
 * escape characters to prevent encoding issues.
 */
public interface Status {
    @De("initialisiere...")
    @En("initialize...")
    String initialize();

    @De("Berechne Arbeitspakete ohne Vorgaenger.")
    @En("Calculate sub work packages without predecessors.")
    String calculateSubWpWihoutPredecessors();

    @De("Prüfe Oberarbeitspaket-Ebenen.")
    @En("Check top-level work package levels.")
    String checkTopLevelWorkpackageLevels();

    @De("Prüfe OAP auf Ebene {0}.")
    @En("Check top-level work packages on level {0}.")
    String checkTopLevelWorkpackageOnLevel(int level);

    @De("Speichere errechnete Werte.")
    @En("Save calculated values.")
    String saveValues();

    @De("Oberarbeitspaket {0} wird versucht zu berechnen.")
    @En("Try to calculate top-level work package {0}.")
    String tryCalcualteTopLevelWp(String wp);

    @De("Pruefe Unterarbeitspakte von {0}.")
    @En("Check subworkpackages of {0}")
    String checkSubWps(String wp);

    @De("{0} wird berechnet.")
    @En("Calculate {0}.")
    String calculateWp(String wp);

    @De("{0} wurde berechnet.")
    @En("{0} was calculated.")
    String wpWasCalculated(String wp);

    @De("{0} kann noch nicht berechnet werden, weil noch Unterarbeitspakete "
            + "oder Vorgänger fehlen.")
    @En("{0} could not be calculated yet, because there are missing " +
            "sub work packages or predecessors.")
    String wpCouldNotBeCalculated(String wp);

    @De("Enddatum zum OAP {0} wurde von {1} übernommen ({2}).")
    @En("End date of top-level work package {0} was assumed from {1} ({2}).")
    String endDateAssumed(String topWp, String subWp, String date);

    @De("Enddatum zum Oberarbeitspaket {0} existiert nicht, " +
            "" + "da keine Unterarbeitspakete vorhanden.")
    @En("End date of top-level work packge {0} does not exists, " +
            "" + "because there are no sub work packages.")
    String endDateDoesNotExist(String wp);

    @De("Die Dauer von {0} ist berechnet, es endet am {1}.")
    @En("The duration of {0} is calculated, it ends on {1}.")
    String durationFinallyCalculated(String wp, String date);

    @De("Setzte Enddatum von eventuell vorhandenen Nachfolgern von {0} auf "
            + "{1}.")
    @En("Set end date of potentially existent successors of {0} to {1}")
    String setEndDateOfSuccessors(String wp, String date);

    @De("Es wurde ein Nachfolger von {0} gefunden, " +
            "der jetzt berechnet werden" + " kann.")
    @En("A successor of {0} was found and can now be calculated.")
    String successorFound(String wp);

    @De("Unterarbeitspaket {0} wird berechnet.")
    @En("Calculate sub work package {0}.")
    String calculateSubWp(String wp);

    @De("Aktueller Tag: {0}, heute können zusammen {1} Std. gerabeitet " +
            "werden" + ". BAC: {2}")
    @En("Current day: {0}. Today {1} hours can be worked together. BAC: {2}")
    String worksHoursOnDay(String date, int work, int bac);

    @De("{0} muss heute {1} Stunden arbeiten.")
    @En("{0} has to work {1} hours today.")
    String workerWorkHoursToday(String worker, int hours);

    @De("{0} muss heute zusätzlich {1} Stunden arbeiten (weil ein anderer " +
            "Mitarbeiter weniger Zeit hat).")
    @En("{0} has to work additional {1} hour today (because another employee has less time).")
    String workerAdditionalWork(String worker, int hours);

    @De("Alle Stunden von {0} werden am {1} abgearbeitet, " +
            "es werden {2} von {3} verfügbaren Stunden an diesem Tag " +
            "verbraucht.")
    @En("All hours of {0} will be finished on {1}, " + "{2} of {3} of the " +
            "available hours on this day are used up.")
    String finishedHoursOfWp(String worker, String date, int consumed,
                             int available);

    @De("Aktueller Tag ist letzter Tag mit PV: {0} PV: {1}")
    @En("Current day is last day with pv: {0} pv: {1}")
    String lastDayWithPV(String date, double pv);

    @De("Aktueller Tag: {0}, liegt in der Vergangenheit, " +
            "hier darf nichts mehr geändert werden, es werden {1} Stunden " +
            "gearbeitet. BAC: {2}")
    @En("Current day: {0}, lies in the past. Nothing may be changed anymore. " +
            "" + "{1} are to be worked. BAC: {2}")
    String currentDayInPast(String date, double hours, int bac);

    @De("Das Arbeitspaket {0} wurde fertig berechnet. Enddatum: {1}")
    @En("The calculation of work package {0} is finished. End date: {1}")
    String calculationFinished(String wp, String date);

    @De("Das Startdatum eventuell vorhandener Nachfolger von {0} wird auf " +
            "{1}" + " gesetzt.")
    @En("The start date of potentially existent successors of {0} will be " +
            "set" + " to {1}.")
    String setStartDateOfSuccessors(String wp, String date);

    @De("PV von {0} ist am {1} {2}.")
    @En("PV of {0} is {2} on {1}.")
    String pvValueOnDate(String wp, String date, double pv);

    @De("Berechne {0}. Ebene.")
    @En("Calculate level {0}.")
    String calculateLevel(int lvl);

    @De("Berechne Dauer - einen Moment bitte.")
    @En("Calculate duration - please wait..")
    String calculateDuration(int lvl);

    @De("Lade Arbeitspakete...")
    @En("Load workpackages...")
    String loadWps();

    @De("Lade Beziehungen...")
    @En("Load dependencies...")
    String loadDependencies();

    @De("Arbeitspakete werden neu berechnet...")
    @En("Workpackages are being recalculated...")
    String recalcWps();
}
