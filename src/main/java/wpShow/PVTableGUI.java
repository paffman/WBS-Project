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

package wpShow;

import de.fhbingen.wbs.translation.LocalizedStrings;
import de.fhbingen.wbs.translation.Wbs;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import dbServices.ValuesService;

import globals.Controller;
import globals.Workpackage;

/**
 * The GUI of the planned value table.
 */
public class PVTableGUI extends JFrame {
    /** Constant serialized ID used for compatibility. */
    private static final long serialVersionUID = -3500922648912492816L;

    /** The panel which contains the GUI components. */
    private JPanel contentPane;

    /** The table which shows the planned values. */
    private JTable table;

    /**
     * Constructor.
     * @param wp
     *            The work package.
     */
    public PVTableGUI(final Workpackage wp) {
        Wbs wbsStrings = LocalizedStrings.getWbs();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JScrollPane scrollPane = new JScrollPane();
        contentPane.add(scrollPane, BorderLayout.CENTER);

        table = new JTable();
        table.setModel(new DefaultTableModel(new Object[][] {{null, null,
            null, null }, }, new String[] {
            LocalizedStrings.getGeneralStrings().date(), wbsStrings.pv(),
            wbsStrings.sv(), wbsStrings.spi() }));

        Calendar minCal = new GregorianCalendar();
        minCal.setTime(new calendar.Day(ValuesService.getPreviousFriday(
            wp.getStartDateCalc()).getTime()));

        Calendar maxCal = new GregorianCalendar();
        maxCal.setTime(new calendar.Day(ValuesService.getNextFriday(
            wp.getEndDateCalc()).getTime(), true));
        Map<Date, Double> pvs = ValuesService.getPVs(minCal.getTime(),
            maxCal.getTime());

        List<Date> orderedDates = new ArrayList<Date>(pvs.keySet());
        Collections.sort(orderedDates);

        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.removeRow(0);
        for (Date actualDate : orderedDates) {
            Calendar cal = new GregorianCalendar();
            cal.setTime(actualDate);
            double actualPV = ValuesService.getApPv(wp.getWpId(), cal);
            if (actualPV < 0) {
                actualPV = 0;
            }
            model.addRow(new Object[] {
                Controller.DATE_DAY.format(actualDate),
                Controller.DECFORM.format(actualPV) + " EUR",
                Controller.DECFORM.format(wp.getSv(actualDate)) + " EUR",
                Controller.DECFORM.format(wp.getSpi(actualDate)) });
        }

        scrollPane.setViewportView(table);

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }

}
