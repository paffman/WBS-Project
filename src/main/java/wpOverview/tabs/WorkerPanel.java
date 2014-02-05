package wpOverview.tabs;

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

import dbServices.WorkerService;


import wpOverview.WPOverview;
import wpWorker.Worker;
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
 * GUI: Tabelle mit Mitarbeitern<br />
 *
 * @author WBS1.0 Team
 * @version 2.0
 */
public class WorkerPanel extends JPanel {
	private static final long serialVersionUID = -7083734131909551094L;
    private final Wbs wbsStrings;
    private final Login loginStrings;
    private Vector<Vector<String>> workers;
	JTable tblMitarbeiter;
	private final DecimalFormat decform = new DecimalFormat("#0.00");
	/**
	 * Konstruktor
	 * @param over WPOverview Funktionalität
	 */
	public WorkerPanel(WPOverview over) {
        wbsStrings = LocalizedStrings.getWbs();
        loginStrings = LocalizedStrings.getLogin();

		// Mitarbeiter Tabelle definieren
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

			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tblMitarbeiter.setPreferredScrollableViewportSize(new Dimension(500, 300));
		tblMitarbeiter.setFillsViewportHeight(true);


//		for (int i = 0; i < tblMitarbeiter.getColumnCount(); i++) {
//			tblMitarbeiter.getColumnModel().getColumn(i).setCellRenderer(ccr);
//		}

		this.add(new JScrollPane(tblMitarbeiter), BorderLayout.CENTER);

		new WorkerPanelAction(this, over);
	}

	private void init() {
		fillWorkerTable();
		this.repaint();
	}

	/**
	 * Methode zum füllen der Mitarbeiter Tabelle in der GUI Diese Methode wird von fillTbls() aufgerufen
	 *
	 * @param MitList Alle ausgelesenen Mitarbeiter als ArrayList
	 */
	public void fillWorkerTable() {
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

			row.addElement(decform.format(actualWorker.getTagessatz()).replace(".", ",") + " €");
			rows.add(row);

		}
	}
	/**
	 * Laedt Panel neu
	 */
	public void reload() {
		init();
	}
}
