package de.fhbingen.wbs.translation;

import c10n.annotations.De;
import c10n.annotations.En;

/**
 * Interface for application metadata.
 */
public interface ApplicationMetadata {
    @En("WBS-Advanced")
    @De("WBS-Advanced")
    String applicationName();

    @De("Ein Tool zur Projektverwaltung mithilfe einer Work Breakdown<br>"
            + "Structure und zum Controlling mit Earned Value Analyse.")
    @En("A project management tool implementing a work breakdown "
            + "structure<br>and earned value analysis.")
    String shortDescriptionHTML();

    @De("Projektseite: https://github.com/paffman/WBS-Project/")
    @En("Project repository: https://github.com/paffman/WBS-Project/")
    String copyrightHolder();

    @De("Undefinierte Version")
    @En("Undefined version")
    String undefinedVersion();
}
