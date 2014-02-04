package de.fhbingen.wbs.translation;

import c10n.annotations.De;
import c10n.annotations.En;

/**
 * Interface for translations that don't fit any specific category e.g. 
 * dictionary words.
 */
public interface General {
    @De("Datum")
    String date();

    @De("Beschreibung")
    String description();    
}