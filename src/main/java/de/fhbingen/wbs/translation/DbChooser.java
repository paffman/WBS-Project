package de.fhbingen.wbs.translation;

import c10n.annotations.De;
import c10n.annotations.En;

public interface DbChooser extends Button, Login, ProjectSetup, Menu, Database {
    @De("Proejktleiterlogin")
    @En("Project manager login")
    String projectManagerLogin();
}
