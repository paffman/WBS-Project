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

import javax.swing.JOptionPane;

import jdbcConnection.MySqlConnect;
import jdbcConnection.SQLExecuter;
import dbaccess.DBModelManager;
import dbaccess.data.Employee;
import de.fhbingen.wbs.controller.ProjectSetupAssistant;
import de.fhbingen.wbs.translation.LocalizedStrings;
import de.fhbingen.wbs.translation.Messages;

/**
 * Functionality for the ChangePWGUI class. Checks all needed conditions to
 * change the password.
 */
public class ChangePW {

    /**
     * The GUI to change the password.
     */
    protected ChangePWGUI gui;

    /** A instance of it self. */
    private ChangePW dies;

    /** The user from which the password is changed. */
    protected Worker usr;

    private final Messages messages;

    /**
     * Constructor.
     * @param usr
     *            The user from which password is changed.
     */
    public ChangePW(final Worker usr) {
        messages = LocalizedStrings.getMessages();
        dies = this;
        this.usr = usr;
        gui = new ChangePWGUI();
        new ChangePWButtonAction(dies);
    }

    /**
     * Check if all needed fields are filled out.
     * @return True: All fields are filled out. False: Min. one field isn't
     *         filled out.
     */
    protected final boolean checkFieldsFilled() {
        if (gui.txfOldPW.getPassword().length > 0
            && gui.txfNewPW.getPassword().length > 0
            && gui.txfNewPWConfirm.getPassword().length > 0) {
            return true;
        }
        return false;
    }

    /**
     * Check if the password matches with the password on the database.
     * @return True: If the password matches. False: If the password
     *         doesn't matches.
     */
    protected final boolean checkOldPW() {
        return MySqlConnect.comparePasswort(gui.txfOldPW.getPassword());
    }

    /**
     * Check if the password matches with the confirmed password.
     * @return True: If passwords matches. False: Else.
     */
    protected final boolean checkNewPW() {
        return ProjectSetupAssistant.arePasswordsEqual(
            gui.txfNewPW.getPassword(), gui.txfNewPWConfirm.getPassword());
    }

    /**
     * Saves the changed password.
     * @param emp
     *            A result set which contains the user.
     */
    protected final void setNewPassword(final Employee emp) {
        emp.setPassword(gui.txfNewPW.getPassword());
        boolean success = DBModelManager.getEmployeesModel()
            .updateEmployee(emp);
        if (!success) {
            JOptionPane.showMessageDialog(gui,
                messages.passwordChangeError(), null,
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            SQLExecuter.closeConnection();
            MySqlConnect.setPassword(gui.txfNewPW.getPassword());
        }
    }

    /**
     * Checks the new Password for the password rules.
     * @return True if the password matches the rules.
     */

    protected final boolean checkRules() {
        return ProjectSetupAssistant.isPasswordValid(gui.txfNewPW
            .getPassword());
    }
}
