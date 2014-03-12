package de.fhbingen.wbs.translation;

import c10n.annotations.De;
import c10n.annotations.En;

/**
 * Project related translations.
 * <p/>
 * Be sure to encode this file ISO-8859-1, else you will need to use unicode
 * escape characters to prevent encoding issues.
 */
public interface Project {
    @En("Project Manager")
    @De("Projektleiter")
    String projectManager();

    @En("Project name")
    @De("Projektname")
    String projectName();

    @En("Project depth")
    @De("Projektebene")
    String projectTier();

    @En("Project depth")
    @De("Projektebenen")
    String projectLevels();

    @En("Maximum project tiers")
    @De("Maximale Projektebenen")
    String maxProjectTiers();

    @En("Start date")
    @De("Startdatum")
    String startDate();

    @En("End date")
    @De("Enddatum")
    String endDate();

    @De("Übersicht Projekt")
    @En("Project Overview")
    String projectOverviewWindowTitle();
}
