package wpOverview.tabs;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

import jdbcConnection.SQLExecuter;
import wpOverview.WPOverview;
import wpWorker.Worker;
import wpWorker.WBSUser;
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
 * Funktionalitaet des WorkerPanel<br/>
 * 
 * @author WBS1.0 Team
 * @version 2.0
 */
public class WorkerPanelAction {
	/**
	 * Konstruktor
	 * @param gui Worker GUI
	 * @param over WPOverview Funktionalität
	 */
	WorkerPanelAction(final WorkerPanel gui, final WPOverview over) {
		gui.tblMitarbeiter.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				
				if (e.getClickCount() == 2 && gui.tblMitarbeiter.getSelectedRow() >= 0) {
					String login = gui.tblMitarbeiter.getValueAt(gui.tblMitarbeiter.getSelectedRow(), 0).toString();
					ResultSet rsMitarbeiter = SQLExecuter.executeQuery("SELECT * FROM Mitarbeiter WHERE Login = '" + login + "';");
					try {
						while (rsMitarbeiter.next()) {
							Worker mit = new Worker(rsMitarbeiter.getString("Login"), rsMitarbeiter.getString("Vorname"), rsMitarbeiter
									.getString("Name"), rsMitarbeiter.getInt("Berechtigung"), rsMitarbeiter
									.getDouble("Tagessatz"));
							new WBSUser(mit, over);
						}
						rsMitarbeiter.getStatement().close();

					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					over.reload();
				}
			}
		});

		//
	}
}
