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

    private final Messages messages;

    /**
     * Constructor.
     * @param changepw
     *            The functionality to change a password.
     */
    public ChangePWButtonAction(final ChangePW changepw) {
        messages = LocalizedStrings.getMessages();
        this.changepw = changepw;
        addButtonAction();
    }

    /**
     * Insert action listeners to the button "OK" and the button "Cancel".
     * Calls the needed procedures.
     */
    public final void addButtonAction() {
        changepw.gui.txfUser.setText(changepw.usr.getName());

        changepw.gui.btnOk.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                // Check if all fields are filled.
                if (changepw.checkFieldsFilled()) {

                    // Check if the old password is correct.
                    if (changepw.checkOldPW()) {
                        if (changepw.checkNewPW()) {
                            if (changepw.checkRules()) {
                                Employee emp = DBModelManager
                                    .getEmployeesModel().getEmployee(
                                        changepw.usr.getId());
                                changepw.setNewPassword(emp);
                                WPOverviewGUI.setStatusText(messages
                                    .passwordChangeConfirm());
                                changepw.gui.dispose();
                            } else {
                                JOptionPane.showMessageDialog(changepw.gui,
                                    messages.passwordInvalidError() + "\n"
                                        + messages.guidelinesPassword(),
                                    null, JOptionPane.INFORMATION_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(changepw.gui,
                                messages.passwordsNotMatchingError(), null,
                                JOptionPane.INFORMATION_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(changepw.gui,
                            messages.passwordOldWrong(), null,
                            JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(changepw.gui,
                        messages.fillAllFieldsError(), null,
                        JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        changepw.gui.btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                changepw.gui.dispose();
            }
        });

    }
}
