package de.fhbingen.wbs.chooseDB;

import c10n.C10N;
import de.fhbingen.wbs.dbaccess.DBModelManager;
import de.fhbingen.wbs.dbaccess.data.Employee;
import de.fhbingen.wbs.translation.C10NUseEnglishDefaultConfiguration;
import de.fhbingen.wbs.translation.LocalizedStrings;
import de.fhbingen.wbs.functions.WpManager;
import de.fhbingen.wbs.globals.Loader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import javax.swing.JOptionPane;
import de.fhbingen.wbs.jdbcConnection.MySqlConnect;
import de.fhbingen.wbs.jdbcConnection.SQLExecuter;
import de.fhbingen.wbs.wpOverview.WPOverview;
import de.fhbingen.wbs.wpWorker.User;

/**
 * Studienprojekt: WBS Kunde: Pentasys AG, Jens von Gersdorff Projektmitglieder:
 * Andre Paffenholz, Peter Lange, Daniel Metzler, Samson von Graevenitz
 * Studienprojekt: PSYS WBS 2.0<br/>
 * Kunde: Pentasys AG, Jens von Gersdorff<br/>
 * Projektmitglieder: <br/>
 * Michael Anstatt,<br/>
 * Marc-Eric Baumgärtner,<br/>
 * Jens Eckes,<br/>
 * Sven Seckler,<br/>
 * Lin Yang<br/>
 * Ruft die DBChooserGUI auf<br/>
 * setzt nach der Pfadeingabe den Pfad in der MDBConnect Klasse<br/>
 *
 * @author Samson von Graevenitz, Daniel Metzler, Michael Anstatt
 * @version 2.0 - 2012-08-20
 */
public class DBChooser {

    /**
     * Holds the gui-object.
     */
    private DBChooserGUI gui;
    /**
     * last Host the client was connected to.
     */
    private String lastDbHost = null;
    /**
     * Name of the last database the client was connected to.
     */
    private String lastDbName = null;
    /**
     * Password for the id_wbs database of the last host the client was
     * connected to.
     */
    private String lastDbIndexPw = null;
    /**
     * Last username used to login into a database.
     */
    private String lastDbUser = null;

    /**
     * Constructor initializes the DBChooserGUI and the Listeners for it.
     */
    public DBChooser() {
        loadLastDB();
        gui = new DBChooserGUI(this);
        new DBChooserButtonAction(this);
    }

    /**
     * This method is called when the "ok"-Button in the GUI is activated. It
     * controls the database connection and handles the login. If everything is
     * valid the wbs-tool is started.
     */
    public final void next() {

        // get input from gui
        String host = gui.getHostField().getText();
        String db = gui.getDbNameField().getText();
        String user = gui.getUserField().getText();
        char[] indexDbPw = gui.getDbPwPasswordField().getPassword();
        char[] userPw = gui.getPwPasswordField().getPassword();
        Boolean pl = gui.getPlCheckBox().isSelected();

        // check input
        if (host.equals("")) {
            JOptionPane.showMessageDialog(gui, LocalizedStrings.getMessages()
                    .loginMissingHost());
            return;
        }
        if (db.equals("")) {
            JOptionPane.showMessageDialog(gui, LocalizedStrings.getMessages()
                    .loginMissingDbName());
            return;
        }
        if (user.equals("")) {
            JOptionPane.showMessageDialog(gui, LocalizedStrings.getMessages()
                    .loginMissingUser());
            return;
        }

        // get index of database
        String dbIndex = getDatabaseIndex(host, db, indexDbPw);
        if (dbIndex == null) {
            return;
        }

        // test connection
        MySqlConnect.setDbConncetion(host, db, dbIndex, dbIndex + "_" + user,
                userPw);
        try {
            if (!tryConnection()) {
                JOptionPane.showMessageDialog(gui, LocalizedStrings
                        .getMessages().loginConnectionFailure());
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (e.getMessage().contains("Access denied for user")) {
                JOptionPane.showMessageDialog(gui, LocalizedStrings
                        .getMessages().loginConnectionFailure() + "\n" +
                        LocalizedStrings.getMessages().loginCheckUsername());
            } else {
                JOptionPane.showMessageDialog(gui, LocalizedStrings
                        .getMessages().loginConnectionFailure()
                        + "\nException: " + e.toString());
            }
            return;
        }

        // save database as last accessed db
        saveLastDB(host, db, user, indexDbPw);

        // get employee data
        Employee employee =
                DBModelManager.getEmployeesModel().getEmployee(user);
        if (employee == null) {
            JOptionPane.showMessageDialog(gui, LocalizedStrings.getMessages()
                    .loginUserNotFound());
            return;
        }

        // check Project Leader authority and semaphore
        if (pl) {
            if (!employee.isProject_leader()) {
                JOptionPane.showMessageDialog(gui, LocalizedStrings
                        .getMessages().loginMissingPMAtuhority());
                return;
            }
            if (!DBModelManager.getSemaphoreModel().enterSemaphore("pl",
                    employee.getId())) {
                int answer =
                        JOptionPane.showConfirmDialog(gui, LocalizedStrings.getMessages().loginPMSemaphoreOccupied(), LocalizedStrings.getDbChooser().projectManagerLogin(), JOptionPane.YES_NO_OPTION);
                if (answer == JOptionPane.YES_OPTION) {
                    if (!DBModelManager.getSemaphoreModel().enterSemaphore(
                            "pl", employee.getId(), true)) {
                        JOptionPane.showMessageDialog(gui, LocalizedStrings
                                .getMessages().loginPMLoginFailed());
                        return;
                    }
                } else {
                    return;
                }
            }
        }

        // create user data
        User userData =
                new User(employee.getLogin(), employee.getId(),
                        employee.getLast_name(), employee.getFirst_name(), pl);

        // start WBS-Tool
        final User threadUser = userData;
        Thread loader = new Thread() {
            public void run() {
                Loader splashScreen = new Loader(gui);
                WpManager.loadDB();
                new WPOverview(threadUser, gui);
                splashScreen.dispose();
            }
        };
        loader.start();
        gui.dispose();
    }

    /**
     * This method queries the unique id in the id_wbs db for a given dbName
     *
     * @param host
     *            Host where db is located.
     * @param db
     *            Database for which the id is required.
     * @param indexDbPw
     *            Password for the user of the id_wbs db.
     * @return Unique id of the given database.
     */
    private String getDatabaseIndex(final String host, final String db,
            final char[] indexDbPw) {
        MySqlConnect.setDbConncetion(host, "id_wbs", "", "idxUser", indexDbPw);
        String ret = null;
        try {
            ResultSet rslt =
                    SQLExecuter.executeQuery("call "
                            + "db_identifier_select_by_dbname('" + db + "');");
            if (rslt == null) {
                JOptionPane.showMessageDialog(gui, LocalizedStrings
                        .getMessages().loginConnectionFailure()
                        + "\n"
                        + LocalizedStrings.getMessages().loginMissingIndexPw());
            } else if (rslt.next()) {
                ret = rslt.getString("id");
            } else {
                JOptionPane.showMessageDialog(gui, LocalizedStrings
                        .getMessages().loginConnectionFailure()
                        + "\n"
                        + LocalizedStrings.getMessages().loginMissingIndex());
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(gui, LocalizedStrings.getMessages()
                    .loginConnectionFailure()
                    + "\n"
                    + LocalizedStrings.getMessages().loginMissingIndex());
        } finally {
            try {
                MySqlConnect.getConnection().close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            MySqlConnect.setDbConncetion(null, null, null, null, null);
        }
        return ret;

    }

    /**
     * Tries out the currently used database connection.
     *
     * @return Returns true if the connection works. False if otherwise.
     * @throws Exception
     *             throws any occurring exception
     */
    private boolean tryConnection() throws Exception {
        try {
            // direct use and not use through SQLExecuter to circumvent
            // Exception Handling
            Connection c = MySqlConnect.getConnection();
            Statement stmt =
                    c.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_READ_ONLY);
            stmt.executeQuery("call project_select()");
            return true;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * saveLastDB: writes the login data, except the user password, into a file,
     * which is loaded on the next startup.
     *
     * @param host
     *            host of the database.
     * @param db
     *            name of the database.
     * @param user
     *            user of the database, without database index pr�fix.
     * @param indexPw
     *            password for the index database.
     */
    private void saveLastDB(final String host, final String db,
            final String user, final char[] indexPw) {
        File dbConfig = new File("DbConfig.txt");
        try {
            PrintWriter out = new PrintWriter(dbConfig);
            out.println(host);
            out.println(db);
            out.println(indexPw);
            out.println(user);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * loadLastDB(): loads the data of the last used db into the data elements
     * of this class.
     */
    private void loadLastDB() {
        File dbConfig = new File("DbConfig.txt");
        if (dbConfig.canRead()) {
            try {
                BufferedReader in =
                        new BufferedReader(new FileReader(dbConfig));
                this.lastDbHost = in.readLine();
                this.lastDbName = in.readLine();
                this.lastDbIndexPw = in.readLine();
                this.lastDbUser = in.readLine();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @return the lastDbHost
     */
    public final String getLastDbHost() {
        return lastDbHost;
    }

    /**
     * @return the lastDbName
     */
    public final String getLastDbName() {
        return lastDbName;
    }

    /**
     * @return the lastDbIndexPw
     */
    public final String getLastDbIndexPw() {
        return lastDbIndexPw;
    }

    /**
     * @return the lastDbUser
     */
    public final String getLastDbUser() {
        return lastDbUser;
    }

    /**
     * @return the gui
     */
    public final DBChooserGUI getGui() {
        return gui;
    }

    /**
     * erstellt ein Objekt von DBChooser() und beginnt somit das Programm durch
     * Konstruktoraufruf von DBChooser()
     *
     * @param args
     */
    public static void main(String[] args) {
        //Locale.setDefault(Locale.GERMAN);
        System.out.println(Locale.getDefault().getLanguage().equals(Locale
                .GERMAN));
        C10N.configure(new C10NUseEnglishDefaultConfiguration());
        new DBChooser();
    }
}
