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

package wpWorker;

import de.fhbingen.wbs.translation.LocalizedStrings;
import de.fhbingen.wbs.translation.Messages;

import javax.swing.JOptionPane;

import dbaccess.DBModelManager;
import dbaccess.data.Employee;
import wpOverview.WPOverview;
import wpOverview.WPOverviewGUI;

/**
 * A class to insert the work effort to the work package.
 */
public class WBSUser {
    /**
     * Translation interface.
     */
    private final Messages messageStrings;

    /** The associated GUI to this class. */
    private WBSUserGUI gui = new WBSUserGUI();

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
    public WBSUser(final Worker mit, final WPOverview wpOverview) {
        this.messageStrings = LocalizedStrings.getMessages();

        mitarbeiter = mit;
        this.over = wpOverview;
        getGui().btnhinzufuegen.setVisible(false);
        getGui().txfLogin.setEnabled(false);
        new WBSUserButtonAction(this);
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
    public WBSUser(final WPOverview wpOverview) {
        this.messageStrings = LocalizedStrings.getMessages();
        this.over = wpOverview;
        getGui().btnedit.setVisible(false);
        getGui().btnPwReset.setVisible(false);
        new WBSUserButtonAction(this);
        getGui().setTitle(LocalizedStrings.getLogin().newUserWindowTitle());
    }

    /**
     * Set the text fields in the GUI with the data from the user.
     */
    public final void initialize() {
        getGui().txfLogin.setText(getMitarbeiter().getLogin());
        getGui().txfVorname.setText(getMitarbeiter().getVorname());
        getGui().txfName.setText(getMitarbeiter().getName());
        getGui().cbBerechtigung
            .setSelected((getMitarbeiter().getBerechtigung() == 1));

        getGui().txfTagessatz
            .setText(Double.toString(getMitarbeiter().getTagessatz()));
    }

    /**
     * Check the plausibility from the single text fields in the GUI.
     * @return True: If all fields are correct. False: Else.
     */
    public final boolean check() {
        // Login isn't filled.
        if (getGui().txfLogin.getText().equals("")) {
            JOptionPane.showMessageDialog(getGui(),
                    messageStrings.fillFieldError(LocalizedStrings.getLogin()
                            .login()));
            return false;
        }
        // Name isn't filled.
        if (getGui().txfName.getText().equals("")
            || getGui().txfVorname.getText().equals("")) {
            JOptionPane.showMessageDialog(getGui(),
                    messageStrings.fillFieldError(LocalizedStrings.getLogin().
                            firstName()));
            return false;
        }

        try {
            if (getGui().txfTagessatz.getText().equals("")
                || Double.parseDouble(getGui().txfTagessatz.getText()) <= 0) {
                JOptionPane.showMessageDialog(getGui(), messageStrings
                    .fillFieldError(LocalizedStrings.getWbs().dailyRate()));
                return false;
            }
            JOptionPane.showMessageDialog(getGui(), messageStrings
                .valueInFieldIsNotANumber(LocalizedStrings.getWbs()
                    .dailyRate()));
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
    public final WBSUserGUI getGui() {
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
}
