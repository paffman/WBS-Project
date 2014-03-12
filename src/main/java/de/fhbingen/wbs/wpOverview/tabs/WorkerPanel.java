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
import de.fhbingen.wbs.translation.Login;
import de.fhbingen.wbs.translation.Wbs;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import de.fhbingen.wbs.dbServices.WorkerService;

import de.fhbingen.wbs.wpOverview.WPOverview;
import de.fhbingen.wbs.wpWorker.Worker;

/**
 * GUI: Table with employees.
 */
public class WorkerPanel extends JPanel {

    /** Constant serialized ID used for compatibility. */
    private static final long serialVersionUID = -7083734131909551094L;

    /** Localized wbs string. */
    private final Wbs wbsStrings;

    /** Localized login strings. */
    private final Login loginStrings;

    /** A vector which contains the workers. */
    private Vector<Vector<String>> workers;

    /** Table with the employees. */
    JTable tblMitarbeiter;

    /** The decimal format. */
    private final DecimalFormat decform = new DecimalFormat("#0.00");

    /**
     * Constructor.
     * 
     * @param over
     *            Functionality of WPOverview.
     */
    public WorkerPanel(final WPOverview over) {
        wbsStrings = LocalizedStrings.getWbs();
        loginStrings = LocalizedStrings.getLogin();

        // Defines the employees table
        workers = new Vector<Vector<String>>();

        init();

        Vector<String> colMitarbeiter;
        colMitarbeiter = new Vector<String>();
        colMitarbeiter.add(loginStrings.loginLong());
        colMitarbeiter.add(loginStrings.firstName());
        colMitarbeiter.add(loginStrings.surname());
        colMitarbeiter.add(LocalizedStrings.getGeneralStrings().permission());
        colMitarbeiter.add(wbsStrings.dailyRate());
        BorderLayout layout = new BorderLayout(2, 2);
        this.setLayout(layout);

        tblMitarbeiter = new JTable(workers, colMitarbeiter) {
            private static final long serialVersionUID = -5306057668023261636L;

            public boolean isCellEditable(final int row, final int column) {
                return false;
            }
        };
        tblMitarbeiter.setPreferredScrollableViewportSize(new Dimension(500,
                300));
        tblMitarbeiter.setFillsViewportHeight(true);

        // for (int i = 0; i < tblMitarbeiter.getColumnCount(); i++) {
        // tblMitarbeiter.getColumnModel().getColumn(i).setCellRenderer(ccr);
        // }

        this.add(new JScrollPane(tblMitarbeiter), BorderLayout.CENTER);

        new WorkerPanelAction(this, over);
    }

    /** Initialize the employee table. */
    private void init() {
        fillWorkerTable();
        this.repaint();
    }

    /**
     * Fills the employee table in the GUI. This method is called by fillTbls().
     */
    public final void fillWorkerTable() {
        ArrayList<Worker> allWorkers = WorkerService.getRealWorkers();
        Vector<Vector<String>> rows;
        workers.clear();
        rows = workers;

        for (Worker actualWorker : allWorkers) {
            rows = workers;
            Vector<String> row = new Vector<String>();

            row.addElement(actualWorker.getLogin());
            row.addElement(actualWorker.getVorname());
            row.addElement(actualWorker.getName());
            int ber = actualWorker.getBerechtigung();
            if (ber == 1) {
                row.addElement(LocalizedStrings.getProject().projectManager());
            } else {
                row.addElement(wbsStrings.staff());
            }

            row.addElement(decform.format(actualWorker.getTagessatz()).replace(
                    ".", ",")
                    + LocalizedStrings.getGeneralStrings().currencySymbol());
            rows.add(row);

        }
    }

    /**
     * Reloads the panel.
     */
    public final void reload() {
        init();
    }
}
