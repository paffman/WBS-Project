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

package de.fhbingen.wbs.wpOverview;

import de.fhbingen.wbs.translation.Button;
import de.fhbingen.wbs.translation.General;
import de.fhbingen.wbs.translation.LocalizedStrings;
import de.fhbingen.wbs.translation.Wbs;
import de.fhbingen.wbs.controller.WBSUserViewController;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;

import de.fhbingen.wbs.chart.ChartCPIView;
import de.fhbingen.wbs.functions.CalcOAPBaseline;
import de.fhbingen.wbs.functions.WpManager;
import de.fhbingen.wbs.globals.Loader;

/**
 * Creates a freely movable tool bar, contains symbols for a faster/easier
 * use of the tool.
 */
public class ToolBar extends JToolBar {
    /** Duration calculation button in the tool bar. */
    private JButton calcDuration;
    
    /** Duration and PV calculation button in the tool bar. */
    private JButton calcPvDuration;

    /** New work package button in the tool bar. */
    private JButton neuesAP;

    /** Delete work package button in the tool bar. */
    private JButton delAP;

    /** New user button in the tool bar. */
    private JButton neuerMa;

    /** Show cpi graph button in the tool bar. */
    private JButton cpiGraph;

    /** Constant serialized ID used for compatibility. */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor: Insert items to the tool bar.
     * @param over
     *            The wished WPOverview.
     * @param gui
     *            The wished WPOverviewGUI
     */
    public ToolBar(final WPOverview over, final WPOverviewGUI gui) {
        super();

        Wbs wbsStrings = LocalizedStrings.getWbs();
        Button buttonStrings = LocalizedStrings.getButton();
        General generalStrings = LocalizedStrings.getGeneralStrings();

        /* The single buttons in the tool bar. */
        JButton aktualisiereTree = new JButton();
        aktualisiereTree.setIcon(new ImageIcon(getClass().getResource(
                "/_icons/aktualisieren.png"))); // NON-NLS
        aktualisiereTree.setToolTipText(
                buttonStrings.refresh(generalStrings.views()));
        aktualisiereTree.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                over.reload();
            }
        });

        calcDuration = new JButton();
        calcDuration.setIcon(new ImageIcon(getClass().getResource(
            "/_icons/OAPaktualisieren_gross.png"))); //NON-NLS
        calcDuration.setToolTipText(buttonStrings.calculate(generalStrings
            .duration()));
        calcDuration.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                new Thread() {
                    public void run() {
                        gui.setEnabled(false);
                        Loader loader = new Loader(gui);
                        new CalcOAPBaseline(false, over);
                        loader.dispose();
                        Loader.reset();
                        gui.setEnabled(true);
                        gui.requestFocus();
                    }
                } .start();

                over.reload();

            }
        });

        calcPvDuration = new JButton();
        calcPvDuration.setIcon(new ImageIcon(getClass().getResource(
            "/_icons/pv_berechnen_gross.png"))); //NON-NLS
        calcPvDuration.setToolTipText(buttonStrings.calculate(generalStrings
            .duration() + " " + generalStrings.and() + " "
                + LocalizedStrings.getWbs().pv()));
        calcPvDuration.addActionListener(new ActionListener() {
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

        neuesAP = new JButton();
        neuesAP.setIcon(new ImageIcon(getClass().getResource(
            "/_icons/newAP_gross.png"))); //NON-NLS
        neuesAP.setToolTipText(buttonStrings.addNewNeutral(wbsStrings
            .workPackage()));
        neuesAP.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                over.readAP(true);
            }
        });

        delAP = new JButton();
        delAP.setIcon(new ImageIcon(getClass().getResource(
            "/_icons/delAP_gross.png"))); //NON-NLS
        delAP
            .setToolTipText(buttonStrings.delete(wbsStrings.workPackage()));
        delAP.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                WpManager.removeAP(over.getSelectedWorkpackage());
                over.reload();
            }
        });

        neuerMa = new JButton();
        neuerMa.setIcon(new ImageIcon(getClass().getResource(
            "/_icons/newMa_gross.png"))); //NON-NLS
        neuerMa.setToolTipText(buttonStrings.addNewNeutral(LocalizedStrings
            .getLogin().user()));
        neuerMa.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                new WBSUserViewController(over);
            }
        });

        cpiGraph = new JButton();
        cpiGraph.setIcon(new ImageIcon(getClass().getResource(
            "/_icons/cpiGraph_gross.png"))); //NON-NLS
        cpiGraph.setToolTipText(wbsStrings.cpiGraph());
        cpiGraph.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                new ChartCPIView(gui);
            }
        });

        if (WPOverview.getUser().getProjLeiter()) {
            setAllEnabled(true, true, false, true);
        } else {
            setAllEnabled(false, false, false, true);
        }

        add(aktualisiereTree);
        add(calcDuration);
        add(calcPvDuration);
        add(new Separator());
        add(neuesAP);
        add(delAP);
        add(new Separator());
        add(neuerMa);
        add(new Separator());
        add(cpiGraph);
    }

    /**
     * Activate all icons on the tool bar according to the named
     * parameters.
     * @param newAP
     *            True: activate the "New AP" icon. False: deactivate it.
     * @param deleteAP
     *            True: activate the "Delete AP" icon. False: deactivate
     *            it.
     * @param newMa
     *            True: activate the "New Employee" icon. False: deactivate
     *            it.
     * @param cpi
     *            True: activate the "CPI" icon. False: deactivate it.
     */
    public final void setAllEnabled(final boolean newAP,
        final boolean deleteAP, final boolean newMa, final boolean cpi) {
        calcDuration.setEnabled(WPOverview.getUser().getProjLeiter());
        neuesAP.setEnabled(newAP);
        delAP.setEnabled(deleteAP);
        neuerMa.setEnabled(newMa);
        cpiGraph.setEnabled(cpi);
    }

}
