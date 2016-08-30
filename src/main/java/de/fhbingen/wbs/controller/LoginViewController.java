/*
 * The WBS-Tool is a project management tool combining the Work Breakdown
 * Structure and Earned Value Analysis
 * Copyright (C) 2013 FH-Bingen
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY;; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package de.fhbingen.wbs.controller;

import c10n.C10N;
import com.mashape.unirest.http.exceptions.UnirestException;
import de.fhbingen.wbs.dbaccess.DBModelManager;
import de.fhbingen.wbs.dbaccess.data.Employee;
import de.fhbingen.wbs.functions.WpManager;
import de.fhbingen.wbs.globals.Loader;
import de.fhbingen.wbs.gui.login.LoginView;
import de.fhbingen.wbs.jdbcConnection.MySqlConnect;
import de.fhbingen.wbs.jdbcConnection.SQLExecuter;
import de.fhbingen.wbs.timetracker.TimeTrackerConnector;
import de.fhbingen.wbs.translation.C10NUseEnglishDefaultConfiguration;
import de.fhbingen.wbs.translation.LocalizedStrings;
import de.fhbingen.wbs.wpOverview.WPOverview;
import de.fhbingen.wbs.wpWorker.User;

import javax.swing.*;
import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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
 * Ruft die LoginView auf<br/>
 * setzt nach der Pfadeingabe den Pfad in der MDBConnect Klasse<br/>
 *
 * @author Samson von Graevenitz, Daniel Metzler, Michael Anstatt
 * @version 2.0 - 2012-08-20
 */
public class LoginViewController implements LoginView.ActionsDelegate,
        LoginView.DataSource {

    /**
     * Holds the gui-object.
     */
    private LoginView gui;
    /**
     * last Host the client was connected to.
     */
    public static String lastDbHost = null;
    /**
     * Name of the last database the client was connected to.
     */
    public static String lastDbName = null;
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
     * The token for the login user.
     */
    public static String tokenLoginUser = null;

    /**
     * Last application server address.
     */
    public static String lastApplicationAddress;

    /**
     * Project with application server.
     */
    public static boolean withApplicationServer;

    /**
     * Constructor initializes the LoginView and the Listeners for it.
     */
    public LoginViewController() {
        loadLastDB();
        gui = new LoginView(this, this);
    }

    /**
     * Connetor to the application server.
     */
    private TimeTrackerConnector tracker;

    /**
     * This method is called when the "ok"-Button in the GUI is activated. It
     * controls the database connection and handles the login. If everything is
     * valid the wbs-tool is started.
     */
    public final void next() {

        // get input from gui
        String host = gui.getHost();
        String db = gui.getDbName();
        String user = gui.getUsername();
        char[] indexDbPw = gui.getIndexPassword();
        char[] userPw = gui.getUserPassword();
        Boolean pl = gui.isProjectLeader();
        String application = gui.getApplicationField();

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
                        .getMessages().loginConnectionFailure() + "\n"
                        + LocalizedStrings.getMessages().loginCheckUsername());
            } else {
                JOptionPane.showMessageDialog(gui, LocalizedStrings
                        .getMessages().loginConnectionFailure()
                        + "\nException: " + e.toString());
            }
            return;
        }

        try {
            withApplicationServer = ProjectSetupAssistant.getWithApplicationServer(MySqlConnect.getConnection(), db);
        } catch(SQLException e){
            e.printStackTrace();
        }

        if(withApplicationServer) {
            if (gui.getApplicationField().equals("")) {
                JOptionPane.showMessageDialog(gui, LocalizedStrings.getMessages()
                        .loginMissingApplication());
                return;
            }
            else {

                try {
                    tracker = new TimeTrackerConnector(gui.getApplicationField());
                    if (!tracker.checkConnection()) {
                        JOptionPane.showMessageDialog(gui, LocalizedStrings.getMessages()
                                .connectionApplicationFailure());
                        return;
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(gui, LocalizedStrings.getMessages()
                            .connectionApplicationFailure());
                    return;
                }

                try {
                    Map<String, Object> data = new HashMap<>();
                    data.put("username", getGui().getUsername());
                    data.put("password", new String(getGui().getUserPassword()));
                    tracker.post("login/", data, false);
                    tokenLoginUser = tracker.getToken();
                } catch (UnirestException e) {
                    e.printStackTrace();
                }
            }
        }

        if(!withApplicationServer){
            application = "";
        }

        // save database as last accessed db
        saveLastDB(host, db, user, indexDbPw, application);

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
                        JOptionPane.showConfirmDialog(gui,
                                LocalizedStrings.getMessages().
                                        loginPMSemaphoreOccupied(),
                                LocalizedStrings.getDbChooser()
                                        .projectManagerLogin(),
                                JOptionPane.YES_NO_OPTION);

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
            } else if (rslt.next()) {//maggi
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
            final String user, final char[] indexPw, final String application) {
        LoginViewController.lastApplicationAddress = application;
        LoginViewController.lastDbName = db;

        File dbConfig = new File("DbConfig.txt");
        try {
            PrintWriter out = new PrintWriter(dbConfig);
            out.println(host);
            out.println(db);
            out.println(indexPw);
            out.println(user);
            out.println(AddWorkEffortController.workEffortType);
            out.println(application);
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
                String type = in.readLine();
                if(type != null && type.equals(LocalizedStrings.getGeneralStrings().hours())) {
                    AddWorkEffortController.workEffortType = LocalizedStrings.getGeneralStrings().hours();
                } else {
                    AddWorkEffortController.workEffortType = LocalizedStrings.getGeneralStrings().days();
                }
                this.lastApplicationAddress = in.readLine();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public final String getLastDbHost() {
        return lastDbHost;
    }

    @Override
    public final String getLastDbName() {
        return lastDbName;
    }

    @Override
    public final String getLastDbIndexPw() {
        return lastDbIndexPw;
    }

    @Override
    public final String getLastDbUser() {
        return lastDbUser;
    }

    @Override
    public final String getLastApplicationAddress(){ return lastApplicationAddress; }

    /**
     * @return the gui
     */
    public final LoginView getGui() {
        return gui;
    }

    @Override
    public void cancelPerformed() {
        System.exit(0);
    }

    @Override
    public void confirmPerformed() {
        this.next();
    }

    @Override
    public void helpPerformed() {
        JOptionPane.showMessageDialog(getGui(),
                LocalizedStrings.getMessages().loginHelpMsg());
    }

    @Override
    public void newDbPerformed() {
        ProjectSetupAssistant.newProject(getGui());
    }

    /**
     * erstellt ein Objekt von LoginViewController() und beginnt somit das Programm durch
     * Konstruktoraufruf von LoginViewController()
     *
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(Locale.getDefault().getLanguage().equals(Locale
                .GERMAN));
        C10N.configure(new C10NUseEnglishDefaultConfiguration());
        new LoginViewController();
    }

}
