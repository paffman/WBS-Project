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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import dbaccess.DBModelManager;
import dbaccess.data.Employee;
import de.fhbingen.wbs.translation.Messages;
import wpOverview.WPOverviewGUI;

/** The listener for the ChangePW class. */
public class ChangePWButtonAction {

    /** A instance of the functionality. */
    private ChangePW changepw;

    /**
     * Translation interface.
     */
    private final Messages messages;

    /**
     * Constructor.
     * @param changePwInstance
     *            The functionality to change a password.
     */
    public ChangePWButtonAction(final ChangePW changePwInstance) {
        messages = LocalizedStrings.getMessages();
        this.changepw = changePwInstance;
        addButtonAction();
    }

    /**
     * Insert action listeners to the button "OK" and the button "Cancel".
     * Calls the needed procedures.
     */
    public final void addButtonAction() {
        changepw.getGui().txfUser.setText(changepw.getUsr().getName());

        changepw.getGui().btnOk.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                // Check if all fields are filled.
                if (changepw.checkFieldsFilled()) {

                    // Check if the old password is correct.
                    if (changepw.checkOldPW()) {
                        if (changepw.checkNewPW()) {
                            if (changepw.checkRules()) {
                                Employee emp = DBModelManager
                                    .getEmployeesModel().getEmployee(
                                        changepw.getUsr().getId());
                                changepw.setNewPassword(emp);
                                WPOverviewGUI.setStatusText(messages
                                    .passwordChangeConfirm());
                                changepw.getGui().dispose();
                            } else {
                                JOptionPane.showMessageDialog(changepw.getGui(),
                                    messages.passwordInvalidError() + "\n"
                                        + messages.guidelinesPassword(),
                                    null, JOptionPane.INFORMATION_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(changepw.getGui(),
                                messages.passwordsNotMatchingError(), null,
                                JOptionPane.INFORMATION_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(changepw.getGui(),
                            messages.passwordOldWrong(), null,
                            JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(changepw.getGui(),
                        messages.fillAllFieldsError(), null,
                        JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        changepw.getGui().btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                changepw.getGui().dispose();
            }
        });

    }
}
