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

package de.fhbingen.wbs.wpBaseline;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import de.fhbingen.wbs.wpOverview.WPOverview;

/**
 * Defines the attributes from the value cells.
 */
public class ValueTableCellRenderer extends DefaultTableCellRenderer {
    /** Constant serialized ID used for compatibility. */
    private static final long serialVersionUID = 1L;

    /**
     * Sets the alignment of the label's contents along the X axis to:
     * right, if true. Else left.
     */
    private boolean right;

    /** Shows the labels in color, if true. Else in black and white. */
    private boolean colored;

    /**
     * Constructor.
     * @param right
     *            The alignment of the labels content.
     * @param colored
     *            Decides if the labels are shown in color or black and
     *            white.
     */
    public ValueTableCellRenderer(final boolean right, final boolean colored) {
        this.right = right;
        this.colored = colored;
    }

    @Override
    public final Component getTableCellRendererComponent(
        final JTable aTable, final Object value, final boolean aIsSelected,
        final boolean aHasFocus, final int aRow, final int aColumn) {
        Component renderer = super.getTableCellRendererComponent(aTable,
            value, aIsSelected, aHasFocus, aRow, aColumn);

        if (renderer instanceof JLabel) {
            JLabel l = (JLabel) renderer;
            if (right) {
                l.setHorizontalAlignment(JLabel.RIGHT);
            } else {
                l.setHorizontalAlignment(JLabel.LEFT);
            }

        }

        if (!colored) {
            if (aTable.getValueAt(aRow, 0).toString().substring(2, 3)
                .equals(" ")) {
                renderer.setBackground(Color.white);
                renderer.setForeground(Color.black);
            } else if (aTable.getValueAt(aRow, 0).toString()
                .substring(1, 2).equals(" ")) {
                renderer.setBackground(new Color(230, 230, 230));
                renderer.setForeground(Color.black);
            } else if (aTable.getValueAt(aRow, 0).toString()
                .substring(0, 1).equals(" ")) {
                renderer.setBackground(Color.LIGHT_GRAY);
                renderer.setForeground(Color.black);
            } else {
                renderer.setBackground(Color.DARK_GRAY);
                renderer.setForeground(Color.white);
            }

            if (aIsSelected) {
                renderer.setBackground(Color.black);
                renderer.setForeground(Color.white);
            }

        } else {
            double doubleValue = Double.parseDouble(value.toString()
                .replace(",", "."));
            renderer
                .setForeground(WPOverview.getSPIColor(doubleValue, 1)[0]);
            renderer
                .setBackground(WPOverview.getSPIColor(doubleValue, 1)[1]);
            if (renderer instanceof JLabel) {
                JLabel l = (JLabel) renderer;
                l.setHorizontalAlignment(JLabel.RIGHT);
            }
        }

        return this;
    }
}
