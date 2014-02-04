package de.fhbingen.wbs.translation;

import c10n.annotations.De;
import c10n.annotations.En;

public interface Chart {
    @De("Soll")
    @En("Target")
    String target();
    
    @De("Fortschritt (PV)")
    @En("Progress (PV)")
    String progressPv();

    @De("Diagrammansicht")
    @En("Diagram view")
    String diagramView();
}
