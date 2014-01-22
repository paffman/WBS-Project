package de.fhbingen.wbs.translation;

import c10n.annotations.De;
import c10n.annotations.En;

/**
 * Created by Maxi on 20.01.14.
 */
public interface Project {
    @En("Project Manager")
    @De("Projektleiter")
    String projectManager();

    @En("Project name")
    @De("Projektname")
    String projectName();

    @En("Project tier")
    @De("Projektebene")
    String projectTier();

    @En("Project tiers")
    @De("Projektebenen")
    String projectTiers();

    @En("Maximum project tiers")
    @De("Maximale Projektebenen")
    String maxProjectTiers();

    @En("Start date")
    @De("Startdatum")
    String startDate();
}
