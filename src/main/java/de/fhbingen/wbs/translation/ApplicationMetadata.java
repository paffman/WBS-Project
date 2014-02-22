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

    String shortDescription();

    String author();

    String copyrightHolder();
}
