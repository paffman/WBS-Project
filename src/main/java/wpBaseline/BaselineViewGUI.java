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

package wpBaseline;

import globals.Controller;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import de.fhbingen.wbs.translation.General;
import de.fhbingen.wbs.translation.LocalizedStrings;
import de.fhbingen.wbs.translation.Wbs;

/**
 * GUI in which the baselines are shown.
 */
public class BaselineViewGUI extends JFrame {
    /** Constant serialized ID used for compatibility. */
    private static final long serialVersionUID = -2739030780541939599L;

    /** The table in which the information are shown. */
    private JTable table;

    /**
     * Constructor.
     * @param parent
     *            ParentFrame
     */
    public BaselineViewGUI(final JFrame parent) {
        Wbs wbsStrings = LocalizedStrings.getWbs();
        General generalStrings = LocalizedStrings.getGeneralStrings();
        table = new JTable();
        table.setModel(new DefaultTableModel(new Object[][] {{null, null,
            null, null, null, null, "", null, "", null, null, null, null,
            null, null }, }, new String[] {wbsStrings.workPackage(),
            wbsStrings.bac(), wbsStrings.ac(), wbsStrings.etc(),
            wbsStrings.cpi(),
            wbsStrings.bac() + " " + generalStrings.costs(),
            wbsStrings.ac() + " " + generalStrings.costs(),
            wbsStrings.etc() + " " + generalStrings.costs(),
            wbsStrings.eac(), wbsStrings.ev(), generalStrings.trend(),
            wbsStrings.pv(), wbsStrings.sv(), wbsStrings.spi(),
            generalStrings.status() }) {

            private static final long serialVersionUID = 5624811268311876732L;
            @SuppressWarnings("rawtypes")
            // auto generated code from Eclipse
            private Class[] columnTypes = new Class[] {String.class,
                String.class, String.class, String.class, String.class,
                String.class, String.class, String.class, String.class,
                String.class, String.class, String.class, String.class,
                String.class, Object.class };

            @SuppressWarnings({"unchecked", "rawtypes" })
            // auto generated code from Eclipse
            public Class getColumnClass(final int columnIndex) {
                return columnTypes[columnIndex];
            }

            private boolean[] columnEditables = new boolean[] {false,
                false, false, false, false, false, false, false, false,
                false, false, false, false, false, false };

            public boolean isCellEditable(final int row, final int column) {
                return columnEditables[column];
            }
        });
        table.getColumnModel().getColumn(0).setPreferredWidth(283);
        table.getColumnModel().getColumn(1).setPreferredWidth(45);
        table.getColumnModel().getColumn(2).setPreferredWidth(45);
        table.getColumnModel().getColumn(3).setPreferredWidth(45);
        table.getColumnModel().getColumn(4).setPreferredWidth(35);
        table.getColumnModel().getColumn(5).setPreferredWidth(100);
        table.getColumnModel().getColumn(6).setPreferredWidth(100);
        table.getColumnModel().getColumn(7).setPreferredWidth(100);
        table.getColumnModel().getColumn(8).setPreferredWidth(100);
        table.getColumnModel().getColumn(9).setPreferredWidth(100);
        table.getColumnModel().getColumn(10).setPreferredWidth(35);
        table.getColumnModel().getColumn(11).setPreferredWidth(100);
        table.getColumnModel().getColumn(12).setPreferredWidth(100);

        table.getColumnModel().getColumn(0)
            .setCellRenderer(new ValueCellRenderer(false, false));
        for (int i = 1; i < 14; i++) {
            if (i == 4 || i == 13) {
                table.getColumnModel().getColumn(i)
                    .setCellRenderer(new ValueCellRenderer(true, true));
            } else {
                table.getColumnModel().getColumn(i)
                    .setCellRenderer(new ValueCellRenderer(true, false));
            }
        }
        table.getColumnModel().getColumn(14)
            .setCellRenderer(new StatusCellRenderer()); // last column

        getContentPane().add(table, BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.removeRow(0);
        this.setSize(1100, 750);
        this.setVisible(true);
        Controller.centerComponent(parent, this);
    }

    /**
     * Insert a row into the table.
     * @param rowData The data which has to insert into the table.
     */
    protected final void addRow(final String[] rowData) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.addRow(rowData);
    }
}
