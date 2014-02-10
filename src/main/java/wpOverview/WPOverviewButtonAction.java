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

package wpOverview;

import chooseDB.DBChooser;
import dbaccess.DBModelManager;
import de.fhbingen.wbs.translation.LocalizedStrings;
import functions.CalcOAPBaseline;
import functions.WpManager;
import globals.Controller;
import globals.InfoBox;
import globals.Loader;
import importPrepare.PrepareImport;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import wpWorker.ChangePW;

/**
 * Manage the button actions from the WPOverviewGUI.
 */
public class WPOverviewButtonAction {

    /** The functionality of the WPOverview. */
    private WPOverview over;

    /** The GUI of the WPOverview. */
    private WPOverviewGUI gui;

    /**
     * Constructor.
     * @param over
     *            The functionality of the WPOverview
     * @param gui
     *            The GUI of the WPOverview
     */
    public WPOverviewButtonAction(final WPOverview over,
        final WPOverviewGUI gui) {
        this.over = over;
        this.gui = gui;
        addButtonAction();
    }

    /**
     * Insert the action listeners to the WPOverview buttons.
     */
    public final void addButtonAction() {
        gui.btnSchliessen.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                Controller.leaveDB();
                System.exit(0);
            }
        });

        if (WPOverview.getUser().getProjLeiter()) {

            gui.miAP.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    over.readAP(true);
                }
            });

            gui.miDelAp.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    if (over.getSelectedWorkpackage() != null) {
                        WpManager.removeAP(over.getSelectedWorkpackage());
                        over.reload();
                    } else {
                        JOptionPane.showMessageDialog(gui, LocalizedStrings
                            .getErrorMessages().markWorkPackageToDelete());
                    }
                }
            });

            gui.miImportInitial.addActionListener(new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    new PrepareImport(over);
                    over.reload();
                    WPOverviewGUI.setStatusText(LocalizedStrings
                        .getMessages().importedDbWasCalculated());
                    // JOptionPane.showMessageDialog(gui,
                    // "Berechnung abgeschlossen");
                }
            });
        }

        gui.miAktualisieren.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                over.reload();
                WPOverviewGUI.setStatusText(LocalizedStrings.getMessages()
                    .viewWasRefreshed());
            }
        });

        gui.miCalcDuration.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                new Thread() {
                    public void run() {
                        gui.setEnabled(false);
                        Loader loader = new Loader(gui);
                        new CalcOAPBaseline(true, over);
                        loader.dispose();
                        Loader.reset();
                        gui.setEnabled(true);
                        gui.requestFocus();
                    }
                } .start();

                over.reload();
            }
        });

        gui.miChangePW.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                new ChangePW(WPOverview.getUser());
            }
        });

        gui.miAbmelden.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                new DBChooser();
                gui.dispose();
                Controller.leaveDB();
            }
        });

        gui.miBeenden.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                Controller.leaveDB();
                System.exit(0);

            }
        });

        gui.miHilfe.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent arg0) {
                JOptionPane.showMessageDialog(gui, LocalizedStrings
                    .getMessages().clickOnWorkpackageToShowDetails());
            }
        });

        gui.miInfo.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent arg0) {
                new InfoBox();
            }
        });

        // close window
        gui.addWindowListener(new WindowAdapter() {
            public void windowClosing(final WindowEvent e) {
                gui.dispose();
                if (WPOverview.getUser().getProjLeiter()) {
                    DBModelManager.getSemaphoreModel().leaveSemaphore("pl",
                        WPOverview.getUser().getId());
                }
                // System.out.println("Benutzer wurde abgemeldet");
            }
        });

        // set the items from the tool bar.
        // setEnabled(true/false)
        gui.tabs.addChangeListener(new ChangeListener() {
            public void stateChanged(final ChangeEvent e) {
                switch (gui.tabs.getSelectedIndex()) {
                case 0: // all work packages
                    if (WPOverview.getUser().getProjLeiter()) {
                        gui.toolbar.setAllEnabled(true, true, false, true);
                    } else {
                        gui.toolbar
                            .setAllEnabled(false, false, false, true);
                    }
                    gui.showLegende(true);
                    break;
                case 1: // opened work packages
                    if (WPOverview.getUser().getProjLeiter()) {
                        gui.toolbar.setAllEnabled(true, true, false, true);
                    } else {
                        gui.toolbar
                            .setAllEnabled(false, false, false, true);
                    }
                    gui.showLegende(true);
                    break;
                case 2: // closed work packages
                    if (WPOverview.getUser().getProjLeiter()) {
                        gui.toolbar.setAllEnabled(true, true, false, true);
                    } else {
                        gui.toolbar
                            .setAllEnabled(false, false, false, true);
                    }
                    gui.showLegende(true);
                    break;
                case 3: // Employees
                    if (WPOverview.getUser().getProjLeiter()) {
                        gui.toolbar.setAllEnabled(false, false, true, true);
                    } else {
                        gui.toolbar
                            .setAllEnabled(false, false, false, true);
                    }
                    gui.showLegende(false);
                    break;
                case 4: // Baseline
                    if (WPOverview.getUser().getProjLeiter()) {
                        gui.toolbar
                            .setAllEnabled(false, false, false, true);
                    } else {
                        gui.toolbar
                            .setAllEnabled(false, false, false, true);
                    }
                    gui.showLegende(false);
                    break;
                case 5: // Avaialbilities
                    if (WPOverview.getUser().getProjLeiter()) {
                        gui.toolbar
                            .setAllEnabled(false, false, false, true);
                    } else {
                        gui.toolbar
                            .setAllEnabled(false, false, false, true);
                    }
                    gui.showLegende(false);
                    break;
                case 6: // Timeline
                    if (WPOverview.getUser().getProjLeiter()) {
                        gui.toolbar
                            .setAllEnabled(false, false, false, true);
                    } else {
                        gui.toolbar
                            .setAllEnabled(false, false, false, true);
                    }
                    gui.showLegende(false);
                    break;
                case 7: // Konflikte
                    if (WPOverview.getUser().getProjLeiter()) {
                        gui.toolbar
                            .setAllEnabled(false, false, false, true);
                    } else {
                        gui.toolbar
                            .setAllEnabled(false, false, false, true);
                    }
                    if (WPOverview.getUser().getProjLeiter()) {
                        gui.showLegende(false);
                    }
                    break;
                default:
                    break;
                }

            }
        });

    }

}
