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

    private final Messages messageStrings;

    /** The associated GUI to this class. */
    protected WBSUserGUI gui = new WBSUserGUI();

    /** The employee. */
    protected Worker mitarbeiter;

    /** The functionality of the WPOvervieGUI. */
    protected WPOverview over;

    /** A instance of it self. */
    private WBSUser dies;

    /**
     * Constructor.
     * @param mit
     *            The employee from the WPOverview.
     * @param over
     *            The functionality of the WPOverviewGUI. It initialize the
     *            text fields with the data from the user.
     */
    public WBSUser(final Worker mit, final WPOverview over) {
        this.messageStrings = LocalizedStrings.getMessages();
        dies = this;
        mitarbeiter = mit;
        this.over = over;
        gui.btnhinzufuegen.setVisible(false);
        gui.txfLogin.setEnabled(false);
        new WBSUserButtonAction(dies);
        initialize();
        gui.setTitle(mit.getLogin() + " | " + mit.getName() + ", "
            + mit.getVorname());
    }

    /**
     * Constructor.
     * @param over
     *            The functionality of the WPOverviewGUI. It initialize the
     *            text fields with the data from the user.
     */
    public WBSUser(final WPOverview over) {
        this.messageStrings = LocalizedStrings.getMessages();
        this.over = over;
        gui.btnedit.setVisible(false);
        gui.btnPwReset.setVisible(false);
        new WBSUserButtonAction(this);
        gui.setTitle(LocalizedStrings.getLogin().newUserWindowTitle());
    }

    /**
     * Set the text fields in the GUI with the data from the user.
     */
    public final void initialize() {
        gui.txfLogin.setText(mitarbeiter.getLogin());
        gui.txfVorname.setText(mitarbeiter.getVorname());
        gui.txfName.setText(mitarbeiter.getName());
        gui.cbBerechtigung
            .setSelected((mitarbeiter.getBerechtigung() == 1 ? true : false));
        gui.txfTagessatz
            .setText(Double.toString(mitarbeiter.getTagessatz()));
    }

    /**
     * Check the plausibility from the single text fields in the GUI.
     * @return True: If all fields are correct. False: Else.
     */
    public final boolean check() {
        // Login isn't filled.
        if (gui.txfLogin.getText().equals("")) {
            JOptionPane.showMessageDialog(gui, messageStrings.fillFieldError
                    (LocalizedStrings.getLogin().login()));
            return false;
        }
        // Name isn't filled.
        if (gui.txfName.getText().equals("")
            || gui.txfVorname.getText().equals("")) {
            JOptionPane.showMessageDialog(gui, messageStrings.fillFieldError
                    (LocalizedStrings.getLogin().firstName()));
            return false;
        }

        try {
            if (gui.txfTagessatz.getText().equals("")
                || Double.parseDouble(gui.txfTagessatz.getText()) <= 0) {
                JOptionPane.showMessageDialog(gui, messageStrings
                    .fillFieldError(LocalizedStrings.getWbs().dailyRate()));
                return false;
            }
            JOptionPane.showMessageDialog(gui, messageStrings
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
                over.reload();
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
        boolean vorhanden = false;
        // Get all employees and check that the new employee doesn't
        // exists.
        Employee emp = DBModelManager.getEmployeesModel().getEmployee(
            worker.getLogin());
        vorhanden = emp != null;
        // If employee doesn't exists.
        if (!vorhanden) {
            emp = new Employee();
            emp.setLogin(worker.getLogin());
            emp.setFirst_name(worker.getVorname());
            emp.setLast_name(worker.getName());
            emp.setProject_leader(worker.getProjLeiter());
            emp.setPassword("1234".toCharArray());
            emp.setDaily_rate(worker.getTagessatz());
            DBModelManager.getEmployeesModel().addNewEmployee(emp);
        }
        if (vorhanden) {
            return false;
        } else {
            over.reload();
            WPOverviewGUI.setStatusText(messageStrings.userAdded());
            return true;
        }
    }

    /**
     * Get the actual employee.
     * @return The actual employee.
     */
    public final Worker getActualWorker() {
        return mitarbeiter;
    }

    /** Reset the password. */
    public final void passwordReset() {
        Employee employee = DBModelManager.getEmployeesModel().getEmployee(
            mitarbeiter.getId());
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
}
