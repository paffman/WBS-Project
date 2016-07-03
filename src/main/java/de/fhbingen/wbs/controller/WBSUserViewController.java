/*
 * The WBS-Tool is a project management tool combining the Work Breakdown
 * Structure and Earned Value Analysis Copyright (C) 2013 FH-Bingen This
 * program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your
 * option) any later version. This program is distributed in the hope that
 * it will be useful, but WITHOUT ANY WARRANTY;; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with this program. If not,
 * see <http://www.gnu.org/licenses/>.
 */

package de.fhbingen.wbs.controller;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.mysql.jdbc.MySQLConnection;
import de.fhbingen.wbs.gui.wpworker.WBSUserView;
import de.fhbingen.wbs.jdbcConnection.MySqlConnect;
import de.fhbingen.wbs.timetracker.TimeTrackerConnector;
import de.fhbingen.wbs.translation.LocalizedStrings;
import de.fhbingen.wbs.translation.Messages;

import de.fhbingen.wbs.wpWorker.Worker;
import javax.swing.JOptionPane;

import de.fhbingen.wbs.dbaccess.DBModelManager;
import de.fhbingen.wbs.dbaccess.data.Employee;
import de.fhbingen.wbs.wpOverview.WPOverview;
import de.fhbingen.wbs.wpOverview.WPOverviewGUI;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * A class to insert the work effort to the work package.
 */
public class WBSUserViewController implements WBSUserView.Delegate {
    /**
     * Translation interface.
     */
    private final Messages messageStrings;

    /** The associated GUI to this class. */
    private WBSUserView gui = new WBSUserView(this);

    /** The employee. */
    private Worker mitarbeiter;

    /** The functionality of the WPOvervieGUI. */
    private WPOverview over;

    /**
     * Constructor.
     * @param mit
     *            The employee from the WPOverview.
     * @param wpOverview
     *            The functionality of the WPOverviewGUI. It initialize the
     *            text fields with the data from the user.
     */
    public WBSUserViewController(final Worker mit, final WPOverview wpOverview) {
        this.messageStrings = LocalizedStrings.getMessages();

        mitarbeiter = mit;
        this.over = wpOverview;
        //getGui().btnhinzufuegen.setVisible(false);
        //getGui().txfLogin.setEnabled(false);
        getGui().setDisplayMode(WBSUserView.EDIT_USER);
        initialize();
        getGui().setTitle(mit.getLogin() + " | " + mit.getName() + ", "
                 + mit.getVorname());
    }

    /**
     * Constructor.
     * @param wpOverview
     *            The functionality of the WPOverviewGUI. It initialize the
     *            text fields with the data from the user.
     */
    public WBSUserViewController(final WPOverview wpOverview) {
        this.messageStrings = LocalizedStrings.getMessages();
        this.over = wpOverview;
        //getGui().btnedit.setVisible(false);
        //getGui().btnPwReset.setVisible(false);
        getGui().setDisplayMode(WBSUserView.NEW_USER);
        getGui().setTitle(LocalizedStrings.getLogin().newUserWindowTitle());
    }

    /**
     * Set the text fields in the GUI with the data from the user.
     */
    public final void initialize() {
        getGui().setLogin(getMitarbeiter().getLogin());
        getGui().setFirstName(getMitarbeiter().getVorname());
        getGui().setName(getMitarbeiter().getName());
        getGui().setPermission((getMitarbeiter().getBerechtigung() == 1));
        getGui().setDailyRate(getMitarbeiter().getTagessatz());
    }

    /**
     * Check the plausibility from the single text fields in the GUI.
     * @return True: If all fields are correct. False: Else.
     */
    public final boolean check() {
        // Login isn't filled.
        if (getGui().getLogin().equals("")) {
            JOptionPane.showMessageDialog(getGui(),
                    messageStrings.fillFieldError(LocalizedStrings.getLogin()
                            .login()));
            return false;
        }
        // Name isn't filled.
        if (getGui().getName().equals("")
            || getGui().getFirstName().equals("")) {
            JOptionPane.showMessageDialog(getGui(),
                    messageStrings.fillFieldError(LocalizedStrings.getLogin().
                            firstName()));
            return false;
        }

        try {
            if (getGui().getDailyRate() == Double.MIN_NORMAL) {
                JOptionPane.showMessageDialog(getGui(), messageStrings
                        .fillFieldError(LocalizedStrings.getWbs().dailyRate()));
                return false;
            }
            if (getGui().getDailyRate() <= 0) {
                JOptionPane.showMessageDialog(getGui(), messageStrings
                        .valueInFieldIsNotANumber(LocalizedStrings.getWbs()
                                .dailyRate()));
                return false;
            }
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;

    }

    /**
     * Changes the employee. This method is called by a click on the button
     * "btnedit".
     * @param worker
     *            The employee which is changed.
     * @return True: If the changes are successful. False: Else.
     */
    public final boolean changeMitarbeiter(final Worker worker) {
        // Get employee from database.
        Employee emp = DBModelManager.getEmployeesModel().getEmployee(
            worker.getLogin());
        if (emp != null) {
            emp.setFirst_name(worker.getVorname());
            emp.setLast_name(worker.getName());
            emp.setProject_leader(worker.getBerechtigung() == 1);
            emp.setDaily_rate(worker.getTagessatz());
            boolean success = DBModelManager.getEmployeesModel()
                .updateEmployee(emp);
            if (success) {
                getOver().reload();
                WPOverviewGUI.setStatusText(messageStrings.userChanged());
            }
            return success;
        } else {
            return false;
        }
    }

    /**
     * To add a employee to the database.
     * @param worker
     *            The worker which has to be added.
     * @return True: The employee is added successful. False: Else.
     */
    public final boolean addMitarbeiter(final Worker worker) {
        // Get all employees and check that the new employee doesn't
        // exists.
        Employee emp = DBModelManager.getEmployeesModel().getEmployee(
            worker.getLogin());

        if (emp == null) {
            emp = new Employee();
            emp.setLogin(worker.getLogin());
            emp.setFirst_name(worker.getVorname());
            emp.setLast_name(worker.getName());
            emp.setProject_leader(worker.getProjLeiter());
            emp.setPassword("1234".toCharArray());
            emp.setDaily_rate(worker.getTagessatz());
            DBModelManager.getEmployeesModel().addNewEmployee(emp);
            getOver().reload();
            WPOverviewGUI.setStatusText(messageStrings.userAdded());
            return true;
        }
        return false;
    }

    /**
     * Get the actual employee.
     * @return The actual employee.
     */
    public final Worker getActualWorker() {
        return getMitarbeiter();
    }

    /** Reset the password. */
    public final void passwordReset() {
        Employee employee = DBModelManager.getEmployeesModel().getEmployee(
            getMitarbeiter().getId());
        if (employee != null) {
            employee.setPassword("1234".toCharArray());
            if (DBModelManager.getEmployeesModel().updateEmployee(employee)) {
                JOptionPane.showMessageDialog(null,
                    messageStrings.passwordHasBeenReset());
            } else {
                JOptionPane.showMessageDialog(null,
                    messageStrings.passwordChangeError());
            }
        }
    }

    /**
     *  The associated GUI to this class.
     *  @return Gui instance.
     */
    public final WBSUserView getGui() {
        return gui;
    }

    /**
     * The employee.
     * @return Employee instance.
     */
    public final Worker getMitarbeiter() {
        return mitarbeiter;
    }

    /**
     * The functionality of the WPOvervieGUI.
     * @return WPOverviewGui instance.
     */
    public final WPOverview getOver() {
        return over;
    }

    @Override
    public void changeEmployeePerformed() {
        if (this.check()) {
            int rights;
            if (this.getGui().getPermission()) {
                rights = 1;
            } else {
                rights = 0;
            }
            Worker actualWorker = this.getActualWorker();
            actualWorker.setName(this.getGui().getName());
            actualWorker.setVorname(this.getGui().getFirstName());
            actualWorker.setBerechtigung(rights);
            actualWorker.setTagessatz(this.getGui().getDailyRate());
            this.changeMitarbeiter(actualWorker);
            this.getGui().dispose();
        }
    }

    @Override
    public void addEmployeePerformed() {
        if (this.check()) {
            int rights;
            if (this.getGui().getPermission()) {
                rights = 1;
            } else {
                rights = 0;
            }
            this.addMitarbeiter(new Worker(this.getGui().getLogin(),
                    this.getGui().getFirstName(), this.getGui().getName(),
                    rights, this.getGui().getDailyRate()));
            this.getGui().dispose();
            //For the application server update the database.
            try {
                TimeTrackerConnector tracker = new TimeTrackerConnector(LoginViewController.lastApplicationAddress);
                Map<String, Object> data = new HashMap<>();
                data.put("username", getGui().getLogin());
                data.put("password", "1234");

                //create the user
                tracker.post("users/", data, false);
                //login the user
                tracker.post("login/", data, false);

                //add the user to an already existing project
                data.clear();
                data.put("project", "/api/projects/" + ProjectSetupAssistant.getIdByDatabaseName(MySqlConnect.getConnection(),
                        LoginViewController.lastDbName) + "/");
                tracker.post("users/" + ProjectSetupAssistant.getUserID(MySqlConnect.getConnection(), getGui().getLogin()) + "/projects/", data, true);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void cancelPerformed() {
        getGui().dispose();
    }

    @Override
    public void resetPasswordPerformed() {
        this.passwordReset();
        this.getGui().dispose();
    }


}
