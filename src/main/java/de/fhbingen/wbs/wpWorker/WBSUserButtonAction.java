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

package de.fhbingen.wbs.wpWorker;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The action listener for the WBSUser class.
 */
public class WBSUserButtonAction {

    /** The employee. */
    private WBSUser mitarbeiter;

    /**
     * Constructor.
     * @param wbsUser
     *            The employee.
     */
    public WBSUserButtonAction(final WBSUser wbsUser) {
        this.mitarbeiter = wbsUser;
        addButtonAction();
    }

    /**
     * Add the action listener to the buttons "Close","Reset", "Add",
     * "Edit". Also add the key listener to the text field "Daily rate".
     */
    public final void addButtonAction() {
        mitarbeiter.getGui().btnedit.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent arg0) {
                if (mitarbeiter.check()) {
                    int rights;
                    if (mitarbeiter.getGui().cbBerechtigung.isSelected()) {
                        rights = 1;
                    } else {
                        rights = 0;
                    }
                    Worker actualWorker = mitarbeiter.getActualWorker();
                    actualWorker.setName(mitarbeiter.getGui().txfName.getText());
                    actualWorker.setVorname(mitarbeiter.getGui().txfVorname
                        .getText());
                    actualWorker.setBerechtigung(rights);
                    actualWorker.setTagessatz(Double
                        .parseDouble(mitarbeiter.getGui().txfTagessatz.getText()));
                    mitarbeiter.changeMitarbeiter(actualWorker);
                    mitarbeiter.getGui().dispose();
                }
            }
        });

        mitarbeiter.getGui().btnschliessen
            .addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent arg0) {
                    mitarbeiter.getGui().dispose();
                }
            });

        mitarbeiter.getGui().btnPwReset.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent arg0) {
                // Get employee from database.
                mitarbeiter.passwordReset();
                mitarbeiter.getGui().dispose();
            }
        });

        mitarbeiter.getGui().btnhinzufuegen
            .addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent arg0) {
                    if (mitarbeiter.check()) {
                        int rights;
                        if (mitarbeiter.getGui().cbBerechtigung.isSelected()) {
                            rights = 1;
                        } else {
                            rights = 0;
                        }
                        mitarbeiter.addMitarbeiter(new Worker(
                            mitarbeiter.getGui().txfLogin.getText(),
                            mitarbeiter.getGui().txfVorname.getText(),
                            mitarbeiter.getGui().txfName.getText(), rights,
                            Double.parseDouble(mitarbeiter.getGui().txfTagessatz
                                .getText())));
                        mitarbeiter.getGui().dispose();
                    }
                }
            });

    }
}
