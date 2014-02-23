package de.fhbingen.wbs.translation;

import c10n.annotations.De;
import c10n.annotations.En;

/**
 * Interface for project setup assistant specific translations.
 * <p/>
 * Be sure to encode this file ISO-8859-1, else you will need to use unicode
 * escape characters to prevent encoding issues.
 */
public interface ProjectSetup extends Button, Database, Project, Login, Wbs {
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

    @De("Zusammenfassung der Eingaben")
    @En("Summary of your inputs")
    String summary();

    @De("Projekteinrichtung erfolgreich.")
    @En("Project Setup Successful")
    String projectSetupSuccessTitle();
}
