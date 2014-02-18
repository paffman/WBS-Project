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

package de.fhbingen.wbs.wpOverview.tabs;

import de.fhbingen.wbs.translation.LocalizedStrings;
import de.fhbingen.wbs.wpBaseline.BaselineViewController;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import de.fhbingen.wbs.chart.ChartCPIView;
import de.fhbingen.wbs.chart.ChartCompleteView;
import de.fhbingen.wbs.dbServices.ConflictService;
import de.fhbingen.wbs.functions.CalcOAPBaseline;
import de.fhbingen.wbs.globals.Loader;

import de.fhbingen.wbs.wpOverview.WPOverview;

/**
 * Functionality of the baseline panel.
 */
public class BaselinePanelAction {

    /**
     * Constructor.
     * @param gui
     *            The baseline panel.
     * @param over
     *            The functionality of the WPOverview.
     * @param parent
     *            The parent frame.
     */
    BaselinePanelAction(final BaselinePanel gui, final WPOverview over,
        final JFrame parent) {
        gui.btnAddBaseline.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                new Thread() {
                    public void run() {

                        boolean calc;
                        if (ConflictService.getAllConflicts().isEmpty()) {
                            calc = true;
                        } else {
                            if (JOptionPane.showConfirmDialog(null,
                                LocalizedStrings.getMessages()
                                    .durationCalculationNotUpToDate(),
                                LocalizedStrings.getWbs()
                                    .baselineCalculation(),
                                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                                calc = true;
                            } else {
                                calc = false;
                            }
                        }

                        if (calc) {
                            gui.setEnabled(false);
                            Loader loader = new Loader(parent);
                            new CalcOAPBaseline(gui.txfBeschreibung
                                .getText(), over);
                            loader.dispose();
                            Loader.reset();
                            gui.cobChooseBaseline
                                .setSelectedIndex(gui.cobChooseBaseline
                                    .getItemCount() - 1);
                            gui.setEnabled(true);
                            gui.requestFocus();
                            over.reload();
                        }

                    }
                } .start();
            }
        });

        gui.btnShowBaseline.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                String selectedBaseline = gui.cobChooseBaseline
                    .getSelectedItem().toString();
                int pos = selectedBaseline.indexOf("|");
                String curBaseline = selectedBaseline.substring(0, pos - 1);
                new BaselineViewController(Integer.parseInt(curBaseline), parent);
            }
        });

        gui.btnChart.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                new ChartCPIView(parent);
            }
        });

        gui.btnComp.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                new ChartCompleteView(parent);
            }
        });
    }

}
