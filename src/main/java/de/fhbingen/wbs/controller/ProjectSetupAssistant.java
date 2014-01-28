package de.fhbingen.wbs.controller;


import c10n.C10N;
import de.fhbingen.wbs.gui.projectsetupassistant.DatabaseAdminLogin;
import de.fhbingen.wbs.gui.projectsetupassistant.ProjectProperties;
import de.fhbingen.wbs.translation.Messages;
import de.fhbingen.wbs.translation.ProjectSetup;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringTokenizer;
import java.util.regex.Pattern;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
    private static final int MAX_LEVELS = 63;

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
     * MySQL maximum for database names
     */
    private static final int MAX_DATABASE_NAME_LENGTH = 64;

    /**
     * Maximum day of month.
     */
    private static final int MAX_DAY_OF_MONTH = 31;

    /**
     * Maximum month of year.
     */
    private static final int MAX_MONTH_OF_YEAR = 12;

    /**
     * Regular expressions for the user name.
     */
    private static final String USER_NAME_REGEX =
            "[A-Za-z]{1}[.\\-A-Za-z0-9]{0,10}";

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

    private Connection connection;
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
    public void nextButtonPressedDatabaseAdminLogin() {
        assert validateProjectPropertiesEntries();
        if (validateDatabaseAdminLoginEntries()
                && validateDatabaseAccess()
                && validateDatabaseRights()) {
            databaseAdminLogin.setVisible(false);
            switch (showSummary()) {
                case JOptionPane.OK_OPTION:
                    setupDatabase();
                    break;
                case JOptionPane.NO_OPTION:
                    databaseAdminLogin.setVisible(true);
                    break;
                case JOptionPane.CANCEL_OPTION:
                    cancelButtonPressed();
                    break;
                default:
                    break;
            }
        }

    }

    private void setupDatabase() {
        try {
            if (checkIfDatabaseNameIsFree(projectProperties.getDatabaseName()
            )) {
                createProjectDatabase();
            } else {
                showErrorMessage(messages.databaseNameAlreadyExists());
                projectProperties.setVisible(true);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Shows summary of both dialogs.
     * @return true if summary was accepted.
     */
    private int showSummary() {
        String summary = "<html><b><i>" + labels.projectProperties() //NON-NLS
                        + "</i></b><br />" //NON-NLS
                        + newSummaryLine(labels.projectName(),
                        projectProperties.getProjectName())
                        + newSummaryLine(labels.projectTiers(),
                        projectProperties.getProjectLevels())
                        + newSummaryLine(labels.startDate(),
                        projectProperties.getStartDate())
                        + newSummaryLine(labels.databaseName(),
                        projectProperties.getDatabaseName())
                        + newSummaryLine(labels.firstName(),
                        projectProperties.getFirstName())
                        + newSummaryLine(labels.surname(),
                        projectProperties.getSurname())
                        + newSummaryLine(labels.login(),
                        projectProperties.getUserName())
                        + "<br /><b><i>"//NON-NLS
                        + labels.databaseAdminLogin()
                        + "</i></b><br />" //NON-NLS
                        + newSummaryLine(labels.serverAddress(),
                        databaseAdminLogin.getServerAddress())
                        + newSummaryLine(labels.rootLoginName(),
                        databaseAdminLogin.getUserName())
                        + "<br /><br />" //NON-NLS
                        + messages.valuesCorrect() + "</html>"; //NON-NLS
        JLabel label = new JLabel(summary);
        return JOptionPane.showConfirmDialog(databaseAdminLogin,
                label, labels.summary(), JOptionPane.YES_NO_CANCEL_OPTION);
    }

    private final String newSummaryLine(final String label,
                                        final String value) {
        return label + ": " + value + "<br />"; //NON-NLS
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
        if (databaseAdminLogin.getUserName().isEmpty()
                || databaseAdminLogin.getServerAddress().isEmpty()) {
            showErrorMessage(messages.fillAllFieldsError());
            return false;
        }
        return true;
    }

    /**
     * Checks all text fields in project properties for validity.
     * @return true if all text field values are valid.
     */
    private boolean validateProjectPropertiesEntries() {
        boolean returnValue = true;
        if (!areFieldsFilledProjectProperties()) {
            returnValue = false;
            showErrorMessage(messages.fillAllFieldsError());
        }
        if (returnValue && !isStringValid(projectProperties.getFirstName())) {
            returnValue = false;
            showErrorMessage(messages.stringTooLong(labels.firstName()));
        }
        if (returnValue && !isStringValid(projectProperties.getSurname())) {
            returnValue = false;
            showErrorMessage(messages.stringTooLong(labels.surname()));
        }
        if (returnValue && !isUsernameValid(projectProperties.getUserName())) {
            returnValue = false;
            showErrorMessage(messages.userNameInvalid());
        }
        if (returnValue && !isPasswordValid(projectProperties.getPassword())) {
            returnValue = false;
            showErrorMessage(messages.passwordInvalidError());
        } else {
            if (!arePasswordsEqual(projectProperties.getPassword(),
                    projectProperties.getPassword2())) {
                returnValue &= false;
                showErrorMessage(messages.passwordsNotMatchingError());
            }
        }
        if (returnValue && !isStringValid(projectProperties.getProjectName())) {
            returnValue = false;
            showErrorMessage(messages.stringTooLong(labels.projectName()));
        }
        if (returnValue && !isProjectLevelsValid(projectProperties.getProjectLevels())) {
            returnValue = false;
        }
        if (returnValue && !isDateValid(projectProperties.getStartDate())) {
            returnValue = false;
            showErrorMessage(messages.dateInvalid());
        }
        return returnValue;
    }

    /**
     * Checks if project levels are in bounds.
     * {@see ProjectSetupAssistant#MAX_LEVELS}
     * @param projectLevels the project level string.
     * @return true if project tier value is valid.
     */
    private boolean isProjectLevelsValid(final String projectLevels) {
        try {
            int projectLevelsInt = Integer.parseInt(projectLevels);

            if (projectLevelsInt > MAX_LEVELS) {
                showErrorMessage(messages.valueTooHigh(labels.projectTiers()));
                return false;
            }
            if (projectLevelsInt < 1) {
                showErrorMessage(messages.valueTooLow(labels.projectTiers()));
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            showErrorMessage(messages.notANumber());
            return false;
        }
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
        StringTokenizer tokenizer = new StringTokenizer(startDate, ".");
        if (tokenizer.countTokens() != 3) {
            return false;
        }
        int day = Integer.parseInt(tokenizer.nextToken());
        int month = Integer.parseInt(tokenizer.nextToken());

        return (day <= MAX_DAY_OF_MONTH && month <= MAX_MONTH_OF_YEAR
                && month > 0 && day > 0);
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
        return Pattern.matches(USER_NAME_REGEX, userName);
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
     * Clears all Password fields to prevent possible password theft.
     */
    private void clearPasswordFields() {
        projectProperties.clearPasswordFields();
        databaseAdminLogin.clearPasswordField();
    }
    /**
     * Method that validates and establishes database access.
     * @return true if login is valid.
     */
    private boolean validateDatabaseAccess() {
        try {
            loadDatabaseDriver();
        } catch (InstantiationException | IllegalAccessException
                | ClassNotFoundException e) {
            showErrorMessage(messages.databaseDriverError());
            return false;
        }
        char[] password = databaseAdminLogin.getPassword();
            try {
                connection = DriverManager.getConnection(
                                databaseAdminLogin.getServerAddress(),
                                databaseAdminLogin.getUserName(),
                                new String(password));
            } catch (SQLException e) {
                showErrorMessage(messages.databaseLoginError());
                e.printStackTrace();
                return false;
            }

            Arrays.fill(password, '0');
        return true;
    }

    /**
     * Method that checks database for necessary rights to setup a project.
     * @return true if login has necessary rights to create a project.
     */
    private boolean validateDatabaseRights() {
        //TODO show error message if insufficient rights
        return true;
    }

    /**
     * Creates stored procedures on the database.
     */
    private void createStoredProcedures() {
        //TODO
    }

    /**
     * Loads database driver.
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     */
    private void loadDatabaseDriver() throws InstantiationException,
            IllegalAccessException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
    }

    /**
     * Checks if project name is free.
     * @param databaseName database name to check.
     * @return true if database name is free.
     * @throws java.sql.SQLException
     */
    private boolean checkIfDatabaseNameIsFree(final String databaseName)
            throws SQLException {
        ResultSet databaseNames = connection.getMetaData().getCatalogs();
        //iterate each catalog in the ResultSet
        while (databaseNames.next()) {
            // Get the database name, which is at position 1
            String currentDatabaseName = databaseNames.getString(1);
            if (currentDatabaseName.equals(databaseName)) {
                return false;
            }
        }
        databaseNames.close();
        return true;
    }

    /**
     * Creates the project database including tables.
     * @throws java.sql.SQLException
     */
    private void createProjectDatabase() throws SQLException {
        assert connection != null;
        assert connection.isValid(0);
    }
}
