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

import java.awt.Component;

import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * Sets a JProgressBar into a cell and sets the progress bar's value on the
 * basis of the value of the cell. This class is called by the constructor
 * of the class WBSBaseline.
 */
public class ProgressBarTableCellRenderer implements TableCellRenderer {
    /** The progress bar which is set into the cell. */
    private JProgressBar progress;

    public ProgressBarTableCellRenderer() {
        super();
        progress = new JProgressBar();
    }

    @Override
    public final Component getTableCellRendererComponent(
        final JTable table, final Object value, final boolean isSelected,
        final boolean hasFocus, final int row, final int column) {
        int intValue = Integer.parseInt(value.toString().replace(',', '.'));

        // JProgressBar is set by the value of the cell.
        progress.setValue(intValue);
        return progress;
    }
}
