package de.fhbingen.wbs.translation;

import c10n.annotations.De;
import c10n.annotations.En;

/**
 * Interface for project setup assistant specific translations.
 */
public interface ProjectSetup extends Button, Database, Project, Login {
    @De("Projekteinrichtungsassistent")
    @En("Project setup assistant")
    String projectSetupAssistant();

    @De("Datenbank Admin Login")
    @En("Database Admin Login")
    String databaseAdminLogin();

    @De("Projekteinrichtung")
    @En("Project Setup")
    String projectSetup();

    @De("Projekteigenschaften")
    @En("Project Properties")
    String projectProperties();

    @De("Projektleiteraccount erstellen")
    @En("Create Project Manager Login")
    String projectManagerAccount();
}
