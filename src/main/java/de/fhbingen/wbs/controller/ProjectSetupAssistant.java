package de.fhbingen.wbs.controller;


import de.fhbingen.wbs.gui.projectsetupassistant.DatabaseAdminLogin;
import de.fhbingen.wbs.gui.projectsetupassistant.ProjectProperties;

import javax.swing.JFrame;
import java.awt.Component;

/**
 * Created by Maxi on 02.01.14.
 */
public final class ProjectSetupAssistant implements ProjectProperties.Actions,
        DatabaseAdminLogin.Actions {
    /**
     * Maximum number of project tiers.
     * Tiers are limited because of database limitation of varchar to a
     * length of 255. In big projects you might end up with more than 100
     * workpackages in any tier. Different tier ID's are separated by a dot.
     * This equals: 255/4 = ~63
     */
    private static final int MAX_TIERS = 63;

    /**
     * The {@link de.fhbingen.wbs.gui.projectsetupassistant
     * .DatabaseAdminLogin} instance.
     */
    private final DatabaseAdminLogin databaseAdminLogin;
    /**
     * The {@link de.fhbingen.wbs.gui.projectsetupassistant
     * .ProjectProperties} instance.
     */
    private final ProjectProperties projectProperties;

    /**
     * Private default constructor. Static method must be used to create a
     * new Project.
     */
    private ProjectSetupAssistant(final JFrame parent) {
        databaseAdminLogin = new DatabaseAdminLogin(parent, this);
        projectProperties = new ProjectProperties(parent, this);
        projectProperties.setVisible(true);
    }

    /**
     * Starts new Project routine.
     */
    public static void newProject(final JFrame parent) {
        ProjectSetupAssistant projectSetupAssistant =
                new ProjectSetupAssistant(parent);
    }

    //DatabaseAdminLogin
    @Override
    public void okButtonPressedDatabaseAdminLogin() {
        assert validateProjectPropertiesEntries();
        validateDatabaseAdminLoginEntries();
        validateDatabaseAccess();
    }


    @Override
    public void cancelButtonPressed() {
        databaseAdminLogin.dispose();
        projectProperties.dispose();
    }

    @Override
    public void backButtonPressedDatabaseAdminLogin() {
        databaseAdminLogin.setVisible(false);
        projectProperties.setVisible(true);
    }
    //ProjectProperties
    @Override
    public void nextButtonPressedProjectProperties() {
        if(validateProjectPropertiesEntries()) {
            projectProperties.setVisible(false);
            databaseAdminLogin.setVisible(true);
        }
    }

    @Override
    public void backButtonPressedProjectProperties() {

    }

    private boolean validateProjectPropertiesEntries() {
        return true;
    }

    private boolean validateDatabaseAdminLoginEntries() {
        return true;
    }



    /**
     * Method that validates database access and checks for necessary rights.
     */
    private boolean validateDatabaseAccess() {
        return false;
    }
}
