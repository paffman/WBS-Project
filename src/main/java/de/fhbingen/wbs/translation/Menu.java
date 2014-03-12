package de.fhbingen.wbs.translation;

import c10n.annotations.De;
import c10n.annotations.En;

public interface Menu {
    @De("Hilfe")
    @En("Help")
    String help();

    @De("Info")
    @En("Info")
    String info();

    @De("Datei")
    @En("File")
    String file();    
}
