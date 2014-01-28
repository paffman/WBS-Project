package chooseDB;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

import dbaccess.DBModelManager;
import dbaccess.data.Employee;
import wpOverview.WPOverview;
import wpWorker.User;
import functions.WpManager;
import globals.Loader;
import jdbcConnection.MySqlConnect;
import jdbcConnection.SQLExecuter;

/**
 * Studienprojekt: WBS Kunde: Pentasys AG, Jens von Gersdorff Projektmitglieder:
 * Andre Paffenholz, Peter Lange, Daniel Metzler, Samson von Graevenitz
 * Studienprojekt: PSYS WBS 2.0<br/>
 * Kunde: Pentasys AG, Jens von Gersdorff<br/>
 * Projektmitglieder: <br/>
 * Michael Anstatt,<br/>
 * Marc-Eric BaumgÃ¤rtner,<br/>
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
        String indexDbPw = new String(gui.getDbPwPasswordField().getPassword());
        String userPw = new String(gui.getPwPasswordField().getPassword());
        Boolean pl = gui.getPlCheckBox().isSelected();

        // check input
        if (host.equals("")) {
            JOptionPane.showMessageDialog(gui, "Bitte einen Host eintragen!");
            return;
        }
        if (db.equals("")) {
            JOptionPane.showMessageDialog(gui,
                    "Bitte einen Datenbanknamen eintragen!");
            return;
        }
        if (user.equals("")) {
            JOptionPane.showMessageDialog(gui,
                    "Bitte einen Benutzer eintragen!");
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
                JOptionPane.showMessageDialog(gui,
                        "Verbindung konnte nicht aufgebaut werden!");
                return;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    gui,
                    "Verbindung konnte nicht aufgebaut werden! Exception: "
                            + e.toString());
            return;
        }

        // save database as last accessed db
        saveLastDB(host, db, user, indexDbPw);

        // get employee data
        Employee employee =
                DBModelManager.getEmployeesModel().getEmployee(user);
        if (employee == null) {
            JOptionPane.showMessageDialog(gui, "Der Benutzer konnte nicht "
                    + "in der Datenbank gefunden werden!");
            return;
        }

        // check Project Leader authority and semaphore
        if (pl) {
            if (!employee.isProject_leader()) {
                JOptionPane.showMessageDialog(gui,
                        "Der Benutzer hat keine Projekleiter Berechtigungen!");
                return;
            }
            if (!DBModelManager.getSemaphoreModel().enterSemaphore("pl",
                    employee.getId())) {
                int answer =
                        JOptionPane
                                .showConfirmDialog(
                                        gui,
                                        "Es ist bereits ein Projektleiter "
                                                + "eingeloggt. Wollen sie sich "
                                                + "trotzdem einloggen? "
                                                + "Warnung: "
                                                + "Die Daten können "
                                                + "inkonsistent werden, "
                                                + "wenn mehrere "
                                                + "Projektleiter daran "
                                                + "arbeiten.",
                                        "Projektleiterlogin",
                                        JOptionPane.YES_NO_OPTION);
                if (answer == JOptionPane.YES_OPTION) {
                    if (!DBModelManager.getSemaphoreModel().enterSemaphore(
                            "pl", employee.getId(), true)) {
                        JOptionPane.showMessageDialog(gui,
                                "Der Projektleiterlogin ist fehlgeschlagen!");
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
            final String indexDbPw) {
        MySqlConnect.setDbConncetion(host, "id_wbs", "", "idxUser", indexDbPw);
        try {
            ResultSet rslt =
                    SQLExecuter.executeQuery("call "
                            + "db_identifier_select_by_dbname('" + db + "');");
            rslt.next();
            return rslt.getString("id");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(gui,
                    "Verbindung konnte nicht aufgebaut werden! "
                            + "Es wurde kein Index-Eintrag für "
                            + "die Datenbank gefunden.");
            return null;
        }
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
     *            user of the database, without database index präfix.
     * @param indexPw
     *            password for the index database.
     */
    private void saveLastDB(final String host, final String db,
            final String user, final String indexPw) {
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
     * Start of the application. Creates a DBChooser objects and therefore
     * starts the login gui.
     */
    public static void main(String[] args) {
        new DBChooser();
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
}
