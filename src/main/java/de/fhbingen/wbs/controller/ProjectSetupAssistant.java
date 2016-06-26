package de.fhbingen.wbs.controller;


import c10n.C10N;
import de.fhbingen.wbs.dbaccess.ScriptRunner;
import de.fhbingen.wbs.gui.projectsetupassistant.DatabaseAdminLogin;
import de.fhbingen.wbs.gui.projectsetupassistant.ProjectProperties;
import de.fhbingen.wbs.timetracker.TimeTrackerConnector;
import de.fhbingen.wbs.translation.Messages;
import de.fhbingen.wbs.translation.ProjectSetup;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
     * MySQL maximum for database names.
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
     * Regular expression validating the user name.
     */
    private static final Pattern REGEX_USER_NAME =
            Pattern.compile("[A-Za-z][.\\-A-Za-z0-9]{0,10}");
    /**
     * Regular expression validating the database name.
     */
    private static final Pattern REGEX_DATABASE_NAME = Pattern
            .compile("[0-9a-zA-Z$_]+");

    /**
     * Name of the wbs database used for project ids.
     */
    private static final String SQL_WBS_DB_NAME = "id_wbs"; //NON-NLS

    /**
     * Name of the script creating database structures.
     */
    private static final String SCRIPT_CREATE_WBS_DB =
            "create-wbs-db.sql"; //NON-NLS
    /**
     * Name of the script creating {@value #SQL_WBS_DB_NAME} database
     * structures.
     */
    private static final String SCRIPT_CREATE_ID_WBS =
            "create-id-wbs.sql"; //NON-NLS
    /**
     * Name of the script creating views on the database structures.
     */
    private static final String SCRIPT_CREATE_VIEWS =
            "create-views.sql"; //NON-NLS

    /**
     * Name of the script creating stored procedures on the database.
     */
    private static final String SCRIPT_CREATE_STORED_PROCEDURES =
            "create-stored-procedures.sql"; //NON-NLS
    /**
     * SQL statement for calling the stored procedure that creates a new id
     * in {@value #SQL_WBS_DB_NAME} database.
     */
    private static final String SQL_CALL_DB_IDENTIFIER_NEW
            = "CALL db_identifier_new (?)"; //NON-NLS

    private static final String SQL_CALL_DB_USERID_SELECT_BY_USERNAME
            = "CALL db_userid_select_by_username (?)";

    /**
     * SQL statement for creating the project database. Used in {@link
     * #createProjectDatabase(java.sql.Connection, String)}.
     */
    private static final String SQL_CREATE_PROJECT_DATABASE
            = "CREATE DATABASE ?WBS? DEFAULT CHARACTER SET utf8" //NON-NLS
            + " COLLATE utf8_general_ci"; //NON-NLS
    /**
     * Placeholder used by {@link #SQL_CREATE_PROJECT_DATABASE}.
     */
    private static final String SQL_PLACEHOLDER = "?WBS?"; //NON-NLS
    /**
     * Stored procedure call for getting the identifier by database name.
     */
    private static final String SQL_CALL_DB_IDENTIFIER_SELECT_BY_DBNAME =
            "CALL db_identifier_select_by_dbname (?)"; //NON-NLS
    /**
     * Stored procedure call for getting the id for an employee by his login
     * name.
     */
    private static final String SQL_CALL_EMPLOYEES_SELECT_BY_LOGIN =
            "CALL employees_select_by_login (?)"; //NON-NLS
    /**
     * Stored procedure call for creating a new entry in the project db.
     */
    private static final String SQL_CALL_PROJECT_NEW =
            "CALL project_new (?,?,?)"; //NON-NLS
    /**
     * Value of error code that describes rejected login data.
     */
    private static final int SQL_LOGIN_ERROR_CODE = 1045;

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
     * Login window.
     */
    private final JFrame loginWindow;

    /**
     * Current active dialog. Used for putting error messages.
     */
    private JDialog activeDialog;

    /**
     * DB connection.
     */
    private Connection connection;

    /**
     * Connetor to the application server.
     */
    private TimeTrackerConnector tracker;

    private int dbID;

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
        loginWindow = parent;
    }

    /**
     * Starts new Project routine.
     * @param parent the parent for dialogs. Dialogs will block all input to
     *               parent frame.
     */
    public static void newProject(final JFrame parent) {
        new ProjectSetupAssistant(parent);
    }

    //DatabaseAdminLogin
    @Override
    public void nextButtonPressedDatabaseAdminLogin() {
        assert validateProjectPropertiesEntries();
        if (validateDatabaseAdminLoginEntries()
                && validateAndEstablishDatabaseAccess()
                && validateDatabaseRights(connection)
                && validateConnectionApplication()
                && validateApplicationUser()) {
            databaseAdminLogin.setVisible(false);
            switch (showSummary()) {
                case JOptionPane.OK_OPTION:
                    setupProjectOnDatabase();
                    setupProjectOnApplication();
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

    /**
     * Shows summary of both dialogs.
     * @return true if summary was accepted.
     */
    private int showSummary() {
        String summary = "<html><b><i>" + labels.projectProperties() //NON-NLS
                        + "</i></b><br /><table>" //NON-NLS
                        + newSummaryLine(labels.projectName(),
                        projectProperties.getProjectName())
                        + newSummaryLine(labels.projectLevels(),
                        projectProperties.getProjectLevels())
                        + newSummaryLine(labels.startDate(),
                        projectProperties.getStartDate())
                        + newSummaryLine(labels.databaseNameWillBeCreated(),
                        projectProperties.getDatabaseName())
                        + newSummaryLine(labels.firstName(),
                        projectProperties.getFirstName())
                        + newSummaryLine(labels.surname(),
                        projectProperties.getSurname())
                        + newSummaryLine(labels.dailyRate(),
                        projectProperties.getDailyRate())
                        + newSummaryLine(labels.loginLong(),
                        projectProperties.getUserName())
                        + "</table><br /><b><i>"//NON-NLS
                        + labels.databaseApplication()
                        + "</i></b><br /><table>" //NON-NLS
                        + newSummaryLine(labels.serverAddress(),
                        databaseAdminLogin.getServerAddress())
                        + newSummaryLine(labels.rootLoginName(),
                        databaseAdminLogin.getUserName())
                        + newSummaryLine(labels.applicationServer(),
                        databaseAdminLogin.getApplicationAddress())
                        + "</table><br /><br />" //NON-NLS
                        + messages.valuesCorrect() + "</html>"; //NON-NLS
        JLabel label = new JLabel(summary);
        return JOptionPane.showConfirmDialog(databaseAdminLogin,
                label, labels.summary(), JOptionPane.YES_NO_CANCEL_OPTION);
    }

    /**
     * Generates html code (table) for a new summary line.
     * @param label label name.
     * @param value the value of the text field associated to the label
     * @return generated html table code.
     */
    private String newSummaryLine(final String label,
                                        final String value) {
        return "<tr><td>" + label + ": </td>" //NON-NLS
                + "<td>" + value + "</td></tr>"; //NON-NLS
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
                || databaseAdminLogin.getServerAddress().isEmpty()
                || databaseAdminLogin.getApplicationAddress().isEmpty()) {
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
        if (returnValue && !isStringValid(projectProperties.getProjectName())) {
            returnValue = false;
            showErrorMessage(messages.stringTooLong(labels.projectName()));
        }
        if (returnValue && !isProjectLevelsValid(projectProperties.
                getProjectLevels())) {
            returnValue = false;
        }
        if (returnValue && !isDateValid(projectProperties.getStartDate())) {
            returnValue = false;
            showErrorMessage(messages.dateInvalid());
        }
        if (returnValue && !isDatabaseNameValid(projectProperties
                .getDatabaseName())) {
            returnValue = false;
            showErrorMessage(messages.databaseNameInvalid() + "\n\n"
                    + messages.guidelinesDatabaseName());
        }
        if (returnValue && !isStringValid(projectProperties.getFirstName())) {
            returnValue = false;
            showErrorMessage(messages.stringTooLong(labels.firstName()));
        }
        if (returnValue && !isStringValid(projectProperties.getSurname())) {
            returnValue = false;
            showErrorMessage(messages.stringTooLong(labels.surname()));
        }
        if (returnValue && !isDailyRateValid(projectProperties.getDailyRate()
        )) {
            returnValue = false;
            showErrorMessage(
                    messages.valueInFieldIsNotANumber(labels.dailyRate()));
        }
        if (returnValue && !isUsernameValid(projectProperties.getUserName())) {
            returnValue = false;
            showErrorMessage(messages.userNameInvalid() + "\n\n"
                    + messages.guidelinesUsername());
        }
        if (returnValue && !isPasswordValid(projectProperties.getPassword())) {
            returnValue = false;
            showErrorMessage(messages.passwordInvalidError() + "\n\n"
                    + messages.guidelinesPassword());
        } else {
            if (!arePasswordsEqual(projectProperties.getPassword(),
                    projectProperties.getPassword2())) {
                returnValue = false;
                showErrorMessage(messages.passwordsNotMatchingError());
            }
        }
        return returnValue;
    }

    /**
     * Checks if daily rate is valid.
     * @param dailyRate to check.
     * @return true if daily rate is valid.
     */
    private boolean isDailyRateValid(final String dailyRate) {
        try {
            double rate = Double.parseDouble(dailyRate);
            return rate != Double.NaN && rate > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks database name input for validity using the regular expression.
     * {@link #REGEX_DATABASE_NAME}.
     * @param databaseName database name to check against the regex.
     * @return true if databse name is valid.
     */
    private static boolean isDatabaseNameValid(final String databaseName) {
        return (databaseName.length() <= MAX_DATABASE_NAME_LENGTH)
                && REGEX_DATABASE_NAME.matcher(databaseName).matches();
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
                showErrorMessage(messages.valueTooHigh(labels.projectLevels()));
                return false;
            }
            if (projectLevelsInt < 2) {
                showErrorMessage(messages.valueTooLow(labels.projectLevels()));
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            showErrorMessage(messages.projectPropertiesLevelsNotANumber());
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
              || projectProperties.getUserName().isEmpty()
              || projectProperties.getDatabaseName().isEmpty()
        );
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
    private boolean isDateValid(final String startDate) { //TODO improve
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
    public static boolean arePasswordsEqual(final char[] password,
                                      final char[] password2) {
        boolean returnValue = Arrays.equals(password, password2);
        Arrays.fill(password, '0');
        Arrays.fill(password2, '0');
        return returnValue;
    }

    /**
     * Checks if password is valid against the security guidelines.
     * @param password password to check. Gets filled with 0 after usage.
     *                 DON'T REUSE
     * @return true if valid.
     */
    public static boolean isPasswordValid(final char[] password) {
        if (password.length < MIN_PW_LENGTH) {
            return false;
        }
        CharsetEncoder asciiEncoder = Charset.forName("US-ASCII")
                .newEncoder();
        boolean onlyAscii = true;
        boolean hasLowercaseCharacter = false;
        boolean hasSpecialCharacterOrUppercaseCharacter = false;
        boolean hasNumber = false;
        for (char c : password) {
            onlyAscii &= asciiEncoder.canEncode(c);
            hasLowercaseCharacter |= Character.isLowerCase(c);
            hasSpecialCharacterOrUppercaseCharacter |=
                    Character.isUpperCase(c) || !Character.isLetterOrDigit(c);
            hasNumber |= Character.isDigit(c);
        }
        Arrays.fill(password, '0');
        return onlyAscii && hasLowercaseCharacter
                && hasSpecialCharacterOrUppercaseCharacter && hasNumber;
    }

    /**
     * Checks username for conformity.
     * @param userName user name to check.
     * @return true if valid.
     */
    private static boolean isUsernameValid(final String userName) {
        return REGEX_USER_NAME.matcher(userName).matches();
    }

    /**
     * Checks if string is too long for database.
     * @param s String to check.
     * @return true if valid.
     */
    private boolean isStringValid(final String s) {
        return (s.length() > 0 && s.length() <= MAX_VARCHAR_LENGTH);
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
    private boolean validateAndEstablishDatabaseAccess() {
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
                                "jdbc:mysql://" //NON-NLS
                                        + databaseAdminLogin.getServerAddress(),
                                databaseAdminLogin.getUserName(),
                                new String(password));
            } catch (SQLException e) {
                if (e.getErrorCode() == SQL_LOGIN_ERROR_CODE) {
                    showErrorMessage(messages.loginWrongError());
                } else {
                    showErrorMessage(messages.databaseLoginError());
                }
                e.printStackTrace();
                return false;
            }

            Arrays.fill(password, '0');
        return true;
    }
    /**
     * Method that checks database for necessary rights to setup a project.
     * TODO: read several pages of documentation and code to figure out how
     * to read permissions on a mysql database. Also figure out which
     * permissions we need exactly. Alternatively just remove this code
     * since error message will be forwarded to the user at runtime.
     *
     * @param connection the connection to use.
     * @return true if login has necessary rights to create a project.
     */
    private static boolean validateDatabaseRights(final Connection connection) {
        return true;
    }

    /**
     * Loads database driver.
     * @throws InstantiationException if anything goes wrong while loading
     * Driver, e.g. it does not exist.
     * @throws IllegalAccessException if anything goes wrong while loading
     * Driver, e.g. it does not exist.
     * @throws ClassNotFoundException if anything goes wrong while loading
     * Driver, e.g. it does not exist.
     */
    private void loadDatabaseDriver() throws InstantiationException,
            IllegalAccessException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
    }

    /**
     * Checks if project name is free.
     * @param connection the connection to use.
     * @param databaseName database name to check.
     * @return true if database name is free.
     * @throws java.sql.SQLException
     * {@link java.sql.DatabaseMetaData#getCatalogs()} and
     * {@link java.sql.ResultSet}.
     */
    private static boolean checkIfDatabaseNameIsFree(final Connection
                                                            connection,
                                              final String
                                              databaseName)
            throws SQLException {
        ResultSet databaseNames = connection.getMetaData().getCatalogs();
        //iterate each catalog in the ResultSet
        while (databaseNames.next()) {
            // Get the database name, which is at position 1
            String currentDatabaseName = databaseNames.getString(1);
            if (currentDatabaseName.equals(databaseName)) {
                databaseNames.close();
                return false;
            }
        }
        databaseNames.close();
        return true;
    }

    /**
     * Calls static function to create database and handles errors.
     */
    private void setupProjectOnDatabase() {
        try {
            String dbName = projectProperties.getDatabaseName();
            if (connection.isValid(0) && checkIfDatabaseNameIsFree(connection,
                    dbName)) {
                connection.setAutoCommit(false);
                setupNewProjectDataStructureOnDatabase(connection, dbName);
                createProjectData();
                JOptionPane.showConfirmDialog(loginWindow,
                        messages.projectSetupSuccess(),
                        labels.projectSetupSuccessTitle(),
                        JOptionPane.DEFAULT_OPTION);
                //connection.close();
            } else {
                showErrorMessage(messages.databaseNameAlreadyExists());
                projectProperties.setVisible(true);
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
            String errorMessage = messages
                    .projectSetupAssistantFailedToCreateProject();
            if (e.getMessage().contains("privilege")) { //NON-NLS
                errorMessage = errorMessage + "\n\n" + messages
                        .moreInformation() + ": " + e.getLocalizedMessage();
            }
            showErrorMessage(errorMessage);
        }
    }

    /**
     * Sets parameters of transaction and calls all necessary subroutines to
     * create a project database.
     * @param connection the connection to use.
     * @param dbName name of database to create.
     * @throws SQLException see other used methods.
     * @throws IOException see other used methods.
     */
    private static void setupNewProjectDataStructureOnDatabase(
            final Connection connection, final String dbName)
            throws SQLException, IOException {
        assert connection != null;
        int oldTransactionIsolation = connection.getTransactionIsolation();
        boolean oldAutoCommit = connection.getAutoCommit();
        try {
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.
                    TRANSACTION_SERIALIZABLE);
            createIdWbsIfNotPresent(connection);
            createProjectDatabase(connection, dbName);
            useDatabase(connection, dbName);
            createTables(connection, dbName);
            createViews(connection, dbName);
            createStoredProcedures(connection, dbName);
            createDbIdentifier(connection, dbName);
            connection.commit();
        } catch (SQLException | IOException e) {
            connection.rollback();
            System.err.println("Transaction is being rolled back!"); //NON-NLS
            throw e;
        } finally {
            connection.setAutoCommit(oldAutoCommit);
            connection.setTransactionIsolation(oldTransactionIsolation);
        }
    }

    /**
     * Gets the id from employee table.
     * @param connection the connection to use.
     * @param userName the database name to get id for.
     * @return id of database name.
     * @throws SQLException if something goes wrong.
     */
    private static int getIdByUserName(final Connection connection,
                                           final String databaseName,
                                           final String userName)
            throws SQLException {
        String oldDatabaseName = connection.getCatalog();
        useDatabase(connection, databaseName);
        try {
            PreparedStatement statement = connection.prepareStatement(
                    SQL_CALL_EMPLOYEES_SELECT_BY_LOGIN);
            statement.setString(1, userName);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1);
        } finally {
            connection.setCatalog(oldDatabaseName);
        }
    }

    /**
     * Gets the id from {@value #SQL_WBS_DB_NAME}.
     * @param connection the connection to use.
     * @param databaseName the database name to get id for.
     * @return id of database name.
     * @throws SQLException if something goes wrong.
     */
    private static int getIdByDatabaseName(final Connection connection,
                                           final String databaseName)
            throws SQLException {
        String oldDatabaseName = connection.getCatalog();
        useDatabase(connection, SQL_WBS_DB_NAME);
        try {
            PreparedStatement statement = connection.prepareStatement(
                    SQL_CALL_DB_IDENTIFIER_SELECT_BY_DBNAME);
            statement.setString(1, databaseName);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt(2);
        } finally {
            connection.setCatalog(oldDatabaseName);
        }
    }

    private int getUserID(final Connection connection,
                                 final String username) throws SQLException{
        String oldDatabaseName = connection.getCatalog();
        useDatabase(connection, SQL_WBS_DB_NAME);
        try{
            PreparedStatement statement = connection.prepareStatement(
                    SQL_CALL_DB_USERID_SELECT_BY_USERNAME);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1);
        } finally {
            connection.setCatalog(oldDatabaseName);
        }


    }
    /**
     * Runs a script from the resource bundle on the connection.
     * @param connection connection to run the script on.
     * @param scriptName script name in resource bundle relative to {@link
     * de.fhbingen.wbs.controller.ProjectSetupAssistant}
     *                   package.
     * @throws IOException {@link java.io.FileReader#FileReader(String)} and
     * {@link de.fhbingen.wbs.dbaccess.ScriptRunner#runScript(java.io.Reader)}
     * @throws SQLException
     * {@link de.fhbingen.wbs.dbaccess.ScriptRunner#runScript(java.io.Reader)}
     */
    private static void runScript(final Connection connection,
                                  final String scriptName) throws IOException,
            SQLException {
        InputStream resource = ProjectSetupAssistant.class.getClassLoader()
                .getResourceAsStream("scripts/" + scriptName);
        if (resource == null) {
            throw new FileNotFoundException(scriptName);
        } else {
            Reader inputFile = new InputStreamReader(resource);
            ScriptRunner runner = new ScriptRunner(connection, false, false,
                    false);
            runner.setLogWriter(null);
            runner.runScript(inputFile);
        }
    }

    /**
     * Checks if catalog to be used is already in use and switches if
     * necessary.
     * @param connection connection to the database.
     * @param databaseName database name to change to.
     * @throws SQLException {@link java.sql.Connection#setCatalog(String)}
     */

    private static void useDatabase(final Connection connection,
                                    final String databaseName) throws
            SQLException {
        if (!connection.getCatalog().equals(databaseName)) {
            connection.setCatalog(databaseName);
        }
    }
    /**
     * Creates id_wbs database if not present yet.
     * @param connection connection to the database.
     * @throws IOException {@link #runScript(java.sql.Connection, String)}
     * @throws SQLException {@link #runScript(java.sql.Connection, String)}
     */
    private static void createIdWbsIfNotPresent(final Connection connection)
            throws IOException, SQLException {
        if (checkIfDatabaseNameIsFree(connection, SQL_WBS_DB_NAME)) {
            runScript(connection, SCRIPT_CREATE_ID_WBS);
        }
    }

    /**
     * Creates the project database.
     * @param connection the connection to use.
     * @param dbName name of database to use.
     * @throws IOException {@link #runScript(java.sql.Connection, String)}
     * @throws SQLException {@link #runScript(java.sql.Connection, String)}
     */
    private static void createProjectDatabase(final Connection connection,
                                              final String
                                                      dbName)
            throws SQLException, IOException {
        assert connection != null;
        assert connection.isValid(0);
        //Prepared statements don't support table and database creation.
        String createDatabaseQuery = SQL_CREATE_PROJECT_DATABASE.replace(
                SQL_PLACEHOLDER, dbName);
        Statement createDatabaseStatement = connection.createStatement();
        createDatabaseStatement.execute(createDatabaseQuery);
    }

    /**
     * Creates tables on the database.
     * @param connection the connection to use.
     * @param dbName name of database to use.
     *
     * @throws IOException {@link #runScript(java.sql.Connection, String)}
     * @throws SQLException {@link #runScript(java.sql.Connection, String)}
     */
    private static void createTables(final Connection connection,
                                     final String dbName)
            throws SQLException, IOException {
        assert connection != null;
        assert connection.isValid(0);

        useDatabase(connection, dbName);
        runScript(connection, SCRIPT_CREATE_WBS_DB);

    }

    /**
     * Creates views on the database.
     * @param connection the connection to use.
     * @param dbName name of database to use.
     *
     * @throws IOException {@link #runScript(java.sql.Connection, String)}
     * @throws SQLException {@link #runScript(java.sql.Connection, String)}
     */
    private static void createViews(final Connection connection,
                                    final String dbName) throws SQLException,
            IOException {
        assert connection != null;
        assert connection.isValid(0);

        useDatabase(connection, dbName);
        runScript(connection, SCRIPT_CREATE_VIEWS);
    }

    /**
     * Creates stored procedures on the database.
     * @param connection the connection to use.
     * @param dbName name of database to use.
     *
     * @throws IOException {@link #runScript(java.sql.Connection, String)}
     * @throws SQLException {@link #runScript(java.sql.Connection, String)}
     */
    private static  void createStoredProcedures(final Connection connection,
                                                final String dbName)
            throws SQLException, IOException {
        assert connection != null;
        assert connection.isValid(0);

        useDatabase(connection, dbName);
        runScript(connection, SCRIPT_CREATE_STORED_PROCEDURES);
    }

    /**
     * Creates the entry in id_wbs databse.
     * @param connection the connection to use.
     * @param dbName name of database to place id entry of.
     *
     * @throws SQLException {@link java.sql.PreparedStatement#execute()} and
     * {@link #useDatabase(java.sql.Connection, String)}
     */
    private static void createDbIdentifier(final Connection connection,
                                           final String dbName) throws
            SQLException {
        assert connection != null;
        assert connection.isValid(0);

        useDatabase(connection, SQL_WBS_DB_NAME);
        String storedProcedure = SQL_CALL_DB_IDENTIFIER_NEW;
        PreparedStatement statement = connection.prepareStatement(
                storedProcedure);
        statement.setString(1, dbName);
        statement.execute();
    }

    /**
     * Function that creates neccessary data on the database.
     * This includes:
     * <ul>
     * <li>Employee entry and user creation on database</li>
     * <li>Project entry</li>
     * <li>Work package entry</li>
     * </ul>
     * @throws java.sql.SQLException if something goes wrong.
     */
    private void createProjectData() throws SQLException {
        createProjectManagerUser();
        createProjectEntryOnDatabase();
        connection.commit();
    }

    /**
     * Created project entry in project table and work package in work
     * package table.
     * @throws SQLException if something goes wrong.
     */
    private void createProjectEntryOnDatabase() throws SQLException {

        final String databaseName = projectProperties.getDatabaseName();
        useDatabase(connection, databaseName);

        //create values
        int projectLevels = Integer.parseInt(projectProperties
                .getProjectLevels());
        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        java.sql.Date startDate = null;
        try {
            startDate = new java.sql.Date(dateFormat.parse(
                    projectProperties.getStartDate()).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //create project in project table.
        PreparedStatement projectNewStatement = connection.prepareStatement(SQL_CALL_PROJECT_NEW);
        projectNewStatement.setInt(1, 1); //safe to assume this will be 1
        projectNewStatement.setString(2, projectProperties.getProjectName());
        projectNewStatement.setInt(3, projectLevels);
        projectNewStatement.execute();

        //create work package in work package table.
        StringBuilder workpackageStringId = new StringBuilder("0");

        for (int i = 1; i < projectLevels; i++) {
            workpackageStringId.append(".0");
        }

        StringBuilder callWorkPackageNew = new StringBuilder(
                "CALL workpackage_new ("); //NON-NLS
        final int parameterCount = 22;
        for (int i = 1; i < parameterCount; i++) {
            callWorkPackageNew.append("?,");
        }
        callWorkPackageNew.append("?)");
        PreparedStatement projectWorkPackageStatement = connection
                .prepareStatement(callWorkPackageNew.toString());
        projectWorkPackageStatement.setString(1, workpackageStringId.toString());
        projectWorkPackageStatement.setInt(2, 1);
        projectWorkPackageStatement.setInt(3, 1);
        projectWorkPackageStatement.setNull(4, Types.INTEGER); //parent
        projectWorkPackageStatement.setString(5,
                projectProperties.getProjectName());
        projectWorkPackageStatement.setNull(6, Types.VARCHAR);
        projectWorkPackageStatement.setDouble(7, 0);
        projectWorkPackageStatement.setDouble(8, 0);
        projectWorkPackageStatement.setDouble(9, 0);
        projectWorkPackageStatement.setDouble(10, 0);
        projectWorkPackageStatement.setDouble(11, 0);
        projectWorkPackageStatement.setDouble(12, 1.0);
        projectWorkPackageStatement.setDouble(13, 0);
        projectWorkPackageStatement.setDouble(14, 0);
        projectWorkPackageStatement.setDouble(15, 0);
        projectWorkPackageStatement.setDouble(16,
                Double.parseDouble(projectProperties.getDailyRate()));
        projectWorkPackageStatement.setNull(17, Types.DATE);
        projectWorkPackageStatement.setBoolean(18, true); //isOAP
        projectWorkPackageStatement.setBoolean(19, false); //isInactive
        projectWorkPackageStatement.setNull(20, Types.DATE);
        projectWorkPackageStatement.setDate(21, startDate);
        projectWorkPackageStatement.setNull(22, Types.DATE);

        projectWorkPackageStatement.execute();
    }

    /**
     * Creates the project manager user on the project.
     * @throws SQLException if something goes wrong.
     */
    private void createProjectManagerUser() throws SQLException {
        String databaseName = projectProperties.getDatabaseName();
        dbID = getIdByDatabaseName(connection, databaseName);
        useDatabase(connection, databaseName);
        String formattedId = String.format("%04d", dbID); //NON-NLS

        final int paramCount = 9;
        String storedProcedure = "CALL employees_new("; //NON-NLS
        for (int i = 1; i < paramCount; i++) {
            storedProcedure += "?,";
        }
        storedProcedure += "?)";

        PreparedStatement statement = connection.prepareStatement(storedProcedure);
        statement.setString(1, projectProperties.getUserName());
        statement.setString(2, projectProperties.getSurname());
        statement.setString(3, projectProperties.getFirstName());
        statement.setBoolean(4, true);
        statement.setDouble(5, Double.parseDouble(projectProperties
                .getDailyRate()));
        statement.setInt(6, 0); //TODO not yet specified in db-interface
        char[] password = projectProperties.getPassword();
        statement.setString(7, new String(password));
        Arrays.fill(password, '0');
        statement.setString(8, databaseName);
        statement.setString(9, formattedId);

        statement.execute();
        statement.close();

    }

    private void setupProjectOnApplication(){
        tracker.createProject();
        try {
            tracker.addUserToProject(dbID, getUserID(connection, projectProperties.getUserName()));
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    private boolean validateConnectionApplication(){
        tracker = new TimeTrackerConnector(databaseAdminLogin.getApplication());
        if(!tracker.checkConnection()){
            showErrorMessage(messages.connectionApplicationFailure());
        }
        return tracker.checkConnection();
    }

    private boolean validateApplicationUser(){
        try {
            int response = tracker.createUser(projectProperties.getUserName(), new String(projectProperties.getPassword()));
            if (response == HttpURLConnection.HTTP_FORBIDDEN) {
                response = tracker.loginUser(projectProperties.getUserName(), new String(projectProperties.getPassword()));
                if (response == HttpURLConnection.HTTP_BAD_REQUEST) {
                    showErrorMessage(messages.wrongUserPassword());
                    return false;
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
