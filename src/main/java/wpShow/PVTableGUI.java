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
 * Studienprojekt:	PSYS WBS 2.0<br/>
 *
 * Kunde:		Pentasys AG, Jens von Gersdorff<br/>
 * Projektmitglieder:<br/>
 *			Michael Anstatt,<br/>
 *			Marc-Eric Baumgärtner,<br/>
 *			Jens Eckes,<br/>
 *			Sven Seckler,<br/>
 *			Lin Yang<br/>
 *
 * WindowBuilder autogeneriert<br/>
 *
 * @author WindowBuilder(Michael Anstatt)
 * @version 2.0 - 18.08.2012
 */

public class PVTableGUI extends JFrame {
	private static final long serialVersionUID = -3500922648912492816L;
	private JPanel contentPane;
	private JTable table;
	/**
	 * Konstruktor
	 * @param wp Ausgewaehltes AP
	 */
	public PVTableGUI(Workpackage wp) {
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
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null},
			},
			new String[] {
				LocalizedStrings.getGeneralStrings().date(), wbsStrings.pv(),
                    wbsStrings.sv(), wbsStrings.spi()
			}
		));



		Calendar minCal = new GregorianCalendar();
		minCal.setTime(new calendar.Day(ValuesService.getPreviousFriday(wp.getStartDateCalc()).getTime()));

		Calendar maxCal = new GregorianCalendar();
		maxCal.setTime(new calendar.Day(ValuesService.getNextFriday(wp.getEndDateCalc()).getTime(), true));
		Map<Date, Double> pvs = ValuesService.getPVs(minCal.getTime(), maxCal.getTime());

		List<Date> orderedDates = new ArrayList<Date>(pvs.keySet());
		Collections.sort(orderedDates);

		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.removeRow(0);
		for (Date actualDate : orderedDates) {
			Calendar cal = new GregorianCalendar();
			cal.setTime(actualDate);
			double actualPV = ValuesService.getApPv(wp.getWpId(), cal);
			if(actualPV < 0) {
				actualPV = 0;
			}
			model.addRow(new Object[] {Controller.DATE_DAY.format(actualDate), Controller.DECFORM.format(actualPV)+" EUR", Controller.DECFORM.format(wp.getSv(actualDate))+" EUR", Controller.DECFORM.format(wp.getSpi(actualDate))});
		}

		scrollPane.setViewportView(table);

		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}

}
