package de.fhbingen.wbs.controller;


import c10n.C10N;
import de.fhbingen.wbs.gui.projectsetupassistant.DatabaseAdminLogin;
import de.fhbingen.wbs.gui.projectsetupassistant.ProjectProperties;
import de.fhbingen.wbs.translation.Messages;
import de.fhbingen.wbs.translation.ProjectSetup;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Arrays;

/**
 * Project setup assistant controller. This class is handling actions for
 * {@link de.fhbingen.wbs.gui.projectsetupassistant.ProjectProperties} and
 * {@link de.fhbingen.wbs.gui.projectsetupassistant.DatabaseAdminLogin}.
 */
public final class ProjectSetupAssistant implements ProjectProperties.Actions,
        DatabaseAdminLogin.Actions {
    /**
     * Maximum number of project tiers.
     * Tiers are limited because of database limitation of varchar to a
     * length of 255. In big projects you might end up with more than 100
     * work packages in any tier. Different tier ID's are separated by a dot.
     * This equals: 255/4 = ~63
     */
    private static final int MAX_TIERS = 63;

    /**
     * Maximum varchar length on mysql. This should be in a db interface
     * instead of here.
     */
    private static final int MAX_VARCHAR_LENGTH = 255;

    /**
     * Minimum password length defined by the security policy.
     */
    private static final int MIN_PW_LENGTH = 6;

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
     * Translation interface that contains project setup relevant values.
     */
    private final ProjectSetup labels;
    /**
     * Translation interface for general ui messages.
     */
    private final Messages messages;
    /**
     * Current active dialog. Used for putting error messages.
     */
    private JDialog activeDialog;
    /**
     * Private default constructor. Static method must be used to create a
     * new Project.
     * @param parent the parent for dialogs.
     */
    private ProjectSetupAssistant(final JFrame parent) {
        labels = C10N.get(ProjectSetup.class);
        messages = C10N.get(Messages.class);
        databaseAdminLogin = new DatabaseAdminLogin(parent, this);
        projectProperties = new ProjectProperties(parent, this);
        activeDialog = projectProperties;
        projectProperties.setVisible(true);
    }

    /**
     * Starts new Project routine.
     * @param parent the parent for dialogs. Dialogs will block all input to
     *               parent frame.
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
        clearPasswordFields();
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
        if (validateProjectPropertiesEntries()) {
            projectProperties.setVisible(false);
            databaseAdminLogin.setVisible(true);
        }
    }

    /**
     * Checks all text fields in database admin login for validity.
     * @return true if all database admin login text field values are valid.
     */
    private boolean validateDatabaseAdminLoginEntries() {
       //TODO
        return true;
    }

    /**
     * Checks all text fields in project properties for validity.
     * @return true if all text field values are valid.
     */
    private boolean validateProjectPropertiesEntries() {
        boolean returnValue = true;
        if (!isStringValid(projectProperties.getFirstName())) {
            returnValue = false;
            showErrorMessage(messages.stringTooLong(labels.firstName()));
        }
        if (!isStringValid(projectProperties.getSurname())) {
            returnValue &= false;
            showErrorMessage(messages.stringTooLong(labels.surname()));
        }
        if (!isUsernameValid(projectProperties.getUserName())) {
            returnValue &= false;
            showErrorMessage(messages.userNameInvalid());
        }
        if (!isPasswordValid(projectProperties.getPassword())) {
            returnValue &= false;
            showErrorMessage(messages.passwordInvalidError());
        } else {
            if (!arePasswordsEqual(projectProperties.getPassword(),
                    projectProperties.getPassword2())) {
                returnValue &= false;
                showErrorMessage(messages.passwordsNotMatchingError());
            }
        }
        if (!isStringValid(projectProperties.getProjectName())) {
            returnValue &= false;
            showErrorMessage(messages.stringTooLong(labels.projectName()));
        }
        if (!isStringValid(projectProperties.getProjectLevels())) {
            returnValue &= false;
            showErrorMessage(messages.stringTooLong(labels.projectTiers()));
        }
        if (!isDateValid(projectProperties.getStartDate())) {
            returnValue &= false;
        }
        if (!areFieldsFilledProjectProperties()) {
            returnValue &= false;
            showErrorMessage(messages.fillAllFieldsError());
        }
        return returnValue;
    }

    /**
     * Checks if all fields are filled.
     * @return true if all fields are filled.
     */
    private boolean areFieldsFilledProjectProperties() {
        return !(projectProperties.getSurname().isEmpty()
              || projectProperties.getFirstName().isEmpty()
              || projectProperties.getProjectName().isEmpty()
              || projectProperties.getProjectLevels().isEmpty()
              || projectProperties.getStartDate().isEmpty()
              || projectProperties.getUserName().isEmpty());
    }

    /**
     * Shows an error message.
     * @param errorMessage the localized error message.
     */

    private void showErrorMessage(final String errorMessage) {
        System.err.println(errorMessage);
        if (activeDialog != null) {
            JOptionPane.showMessageDialog(activeDialog, errorMessage,
                   messages.inputErrorWindowTitle(), JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Validates date field value.
     * @param startDate date string to validate.
     * @return true if date is valid.
     */
    private boolean isDateValid(final String startDate) {
        //TODO
        return true;
    }

    /**
     * Checks if both passwords are equal and overwrites them afterwards.
     * @param password Gets filled with 0 after usage.
     *                 DON'T REUSE
     * @param password2 Gets filled with 0 after usage.
     *                 DON'T REUSE
     * @return true if passwords match.
     */
    private boolean arePasswordsEqual(final char[] password,
                                      final char[] password2) {
        boolean returnValue = Arrays.equals(password, password2);
        Arrays.fill(password, '0');
        Arrays.fill(password2, '0');
        return returnValue;
    }

    /**
     * Checks if password is valid against mysql guidelines.
     * @param password password to check. Gets filled with 0 after usage.
     *                 DON'T REUSE
     * @return true if valid.
     */
    private boolean isPasswordValid(final char[] password) {
        if (password.length < MIN_PW_LENGTH) {
            return false;
        }
        CharsetEncoder asciiEncoder = Charset.forName("US-ASCII")
                .newEncoder();
        boolean onlyAscii = true;
        for (char c : password) {
            onlyAscii &= asciiEncoder.canEncode(c);
        }
        Arrays.fill(password, '0');
        return onlyAscii;
    }

    /**
     * Checks username for conformity.
     * @param userName user name to check.
     * @return true if valid.
     */
    private boolean isUsernameValid(final String userName) {
        //TODO
        return true;
    }

    /**
     * Checks if string is too long for database.
     * @param s String to check.
     * @return true if valid.
     */
    private boolean isStringValid(final String s) {
        return (s.length() <= MAX_VARCHAR_LENGTH);
    }

    /**
     * Method that validates database access and checks for necessary rights.
     * @return true if login is valid.
     */
    private boolean validateDatabaseAccess() {
        //TODO
        return false;
    }
    /**
     * Method that checks database for necessary rights to setup a project.
     * @return true if login has necessary rights to create a project.
     */
    private boolean validateDatabaseRights() {
        //TODO
        return true;
    }

    /**
     * Clears all Password fields to prevent possible password theft.
     */
    private void clearPasswordFields() {
        projectProperties.clearPasswordFields();
        databaseAdminLogin.clearPasswordField();
    }
}
