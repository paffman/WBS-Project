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

import de.fhbingen.wbs.gui.wpworker.ChangePwView;
import de.fhbingen.wbs.wpOverview.WPOverviewGUI;
import de.fhbingen.wbs.wpWorker.Worker;
import javax.swing.JOptionPane;

import de.fhbingen.wbs.jdbcConnection.MySqlConnect;
import de.fhbingen.wbs.jdbcConnection.SQLExecuter;
import de.fhbingen.wbs.dbaccess.DBModelManager;
import de.fhbingen.wbs.dbaccess.data.Employee;
import de.fhbingen.wbs.translation.LocalizedStrings;
import de.fhbingen.wbs.translation.Messages;

/**
 * Functionality for the ChangePwView class. Checks all needed conditions to
 * change the password.
 */
public class ChangePwViewController implements ChangePwView.Delegate {
    /**
     * The GUI to change the password.
     */
    private ChangePwView gui;

    /** The user from which the password is changed. */
    private Worker usr;

    /**
     * Translation interface.
     */
    private final Messages messages;

    /**
     * Constructor.
     * @param worker
     *            The user from which password is changed.
     */
    public ChangePwViewController(final Worker worker) {
        messages = LocalizedStrings.getMessages();
        this.usr = worker;
        gui = new ChangePwView(this);

        getGui().txfUser.setText(getUsr().getName());
    }

    /**
     * Check if all needed fields are filled out.
     * @return True: All fields are filled out. False: Min. one field isn't
     *         filled out.
     */
    protected final boolean checkFieldsFilled() {
        return getGui().txfOldPW.getPassword().length > 0
                && getGui().txfNewPW.getPassword().length > 0
                && getGui().txfNewPWConfirm.getPassword().length > 0;
    }

    /**
     * Check if the password matches with the password on the database.
     * @return True: If the password matches. False: If the password
     *         doesn't matches.
     */
    protected final boolean checkOldPW() {
        return MySqlConnect.comparePasswort(getGui().txfOldPW.getPassword());
    }

    /**
     * Check if the password matches with the confirmed password.
     * @return True: If passwords matches. False: Else.
     */
    protected final boolean checkNewPW() {
        return ProjectSetupAssistant.arePasswordsEqual(getGui().txfNewPW
                .getPassword(), getGui().txfNewPWConfirm.getPassword());
    }

    /**
     * Saves the changed password.
     * @param emp
     *            A result set which contains the user.
     */
    protected final void setNewPassword(final Employee emp) {
        emp.setPassword(getGui().txfNewPW.getPassword());
        boolean success = DBModelManager.getEmployeesModel()
            .updateEmployee(emp);
        if (!success) {
            JOptionPane.showMessageDialog(getGui(),
                messages.passwordChangeError(), null,
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            SQLExecuter.closeConnection();
            MySqlConnect.setPassword(getGui().txfNewPW.getPassword());
        }
    }

    /**
     * Checks the new Password for the password rules.
     * @return True if the password matches the rules.
     */

    protected final boolean checkRules() {
        return ProjectSetupAssistant.isPasswordValid(getGui().txfNewPW.getPassword());
    }

    /**
     * The GUI to change the password.
     * @return gui element.
     */
    public final ChangePwView getGui() {
        return gui;
    }

    /** The user from which the password is changed.
     * @return gui element.
     */
    public final Worker getUsr() {
        return usr;
    }

    @Override
    public void confirmPerformed() {

        // Check if all fields are filled.
        if (checkFieldsFilled()) {

            // Check if the old password is correct.
            if (checkOldPW()) {
                if (checkNewPW()) {
                    if (checkRules()) {
                        Employee emp = DBModelManager
                                .getEmployeesModel().getEmployee(
                                        getUsr().getId());
                        setNewPassword(emp);
                        WPOverviewGUI.setStatusText(messages.
                                passwordChangeConfirm());
                        getGui().dispose();
                    } else {
                        JOptionPane.showMessageDialog(getGui(),
                                messages.passwordInvalidError() + "\n"
                                        + messages.guidelinesPassword(),
                                null, JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(getGui(),
                            messages.passwordsNotMatchingError(), null,
                            JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(getGui(),
                        messages.passwordOldWrong(), null,
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(getGui(),
                    messages.fillAllFieldsError(), null,
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    @Override
    public void cancelPerformed() {
        gui.dispose();
    }
}
