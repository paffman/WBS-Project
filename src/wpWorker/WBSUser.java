package wpWorker;

/**
 * Studienprojekt:	WBS
 * 
 * Kunde:				Pentasys AG, Jens von Gersdorff
 * Projektmitglieder:	Andre Paffenholz, 
 * 						Peter Lange, 
 * 						Daniel Metzler,
 * 						Samson von Graevenitz
 * 
 * GUI zum Hinzufügen eines Aufwandes zum Arbeitspaket
 * 
 * @author Daniel Metzler
 * @version 1.9 - 17.02.2011
 */

import java.sql.*;

import javax.swing.JOptionPane;
import wpOverview.WPOverview;
import wpOverview.WPOverviewGUI;
import jdbcConnection.SQLExecuter;

public class WBSUser {

	/**
	 * Variablen für die WBSMitarbeiterGUI und den SQLExecuter Variable für den Mitarbeiter und die WPOverview
	 */
	
	protected WBSUserGUI gui = new WBSUserGUI();
	protected Worker mitarbeiter;
	protected WPOverview over;
	private WBSUser dies;

	/**
	 * Konstruktoraufruf wenn vorhander Mitarbeiter geändert werden soll
	 * 
	 * @param mit Mitarbeiter aus der WPOverview wird übergeben
	 * @param over WPOverview wird übergeben initialisiert die Textfelder mit den Daten des ausgewählten Benutzers
	 */
	public WBSUser(Worker mit, WPOverview over) {
		dies = this;
		mitarbeiter = mit;
		this.over = over;
		gui.btnhinzufuegen.setVisible(false);
		gui.txfLogin.setEnabled(false);
		new WBSUserButtonAction(dies);
		initialize();
		gui.setTitle(mit.getLogin() + " | " + mit.getName() + ", " + mit.getVorname());
	}

	/**
	 * Konstruktoraufruf wenn neuer Mitarbeiter angelegt wird
	 * 
	 * @param over WPOverview wird übergeben
	 */
	public WBSUser(WPOverview over) {
		this.over = over;
		gui.btnedit.setVisible(false);
		gui.btnPwReset.setVisible(false);
		new WBSUserButtonAction(this);
		gui.setTitle("Neuer Mitarbeiter anlegen");
	}

	/**
	 * Setzt die Textfelder in der WBSMitarbeiterGUI mit den Daten des Benutzers anhand des übergebenen Mitarbeiters
	 */
	public void initialize() {
		gui.txfLogin.setText(mitarbeiter.getLogin());
		gui.txfVorname.setText(mitarbeiter.getVorname());
		gui.txfName.setText(mitarbeiter.getName());
		gui.cbBerechtigung.setSelected((mitarbeiter.getBerechtigung() == 1 ? true : false));
		gui.txfTagessatz.setText(Double.toString(mitarbeiter.getTagessatz()));
	}

	/**
	 * wird von der Methode addMitarbeiter() und changeMitarbeiter() aufgerufen Führt die PlausibilitätsPrüfung der einzelnen Felder in der WBSMitarbeiterGUI
	 * durch
	 * 
	 * @return true = alles OK, false = es sind Unstimmigkeiten aufgetreten
	 */
	public boolean check() {
		// Login wurde nicht ausgefüllt
		if (gui.txfLogin.getText().equals("")) {
			JOptionPane.showMessageDialog(gui, "Bitte Login ausfüllen");
			return false;
		}
		// Name wurde nicht ausgefüllt
		if (gui.txfName.getText().equals("") || gui.txfVorname.getText().equals("")) {
			JOptionPane.showMessageDialog(gui, "Bitte Namen ausfüllen");
			return false;
		}

		try {
			if (gui.txfTagessatz.getText().equals("") || Double.parseDouble(gui.txfTagessatz.getText()) <= 0) {
				JOptionPane.showMessageDialog(gui, "Bitte Tagessatz ausfüllen");
				return false;
			}
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(gui, "Tagessatz ist kein Double-Wert");
			return false;
		}
		return true;

	}

	/**
	 * wird beim Betätigen des Buttons "btnedit" aufgerufen Mitarbeiter-Daten werden aktualisiert
	 * 
	 * @return true = erfolgreiche übernahme der Änderungen, false = übernahme fehlgeschlagen
	 */
	public boolean changeMitarbeiter(Worker worker) {
		// Mitarbeiter wird aus der Datenbank geholt
		ResultSet rs1 = SQLExecuter.executeQuery("SELECT * FROM Mitarbeiter " + "WHERE Login = '" + worker.getLogin() + "'");
		
		try {
			if(rs1.next()) {
				rs1.updateString("Vorname", worker.getVorname());
				rs1.updateString("Name", worker.getName());
				rs1.updateInt("Berechtigung", worker.getBerechtigung());
				rs1.updateDouble("Tagessatz", worker.getTagessatz());
				rs1.updateRow();
				rs1.close();
				over.reload();
				WPOverviewGUI.setStatusText("Mitarbeiter geändert");
				return true;
			} else {
				rs1.close();
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * wird beim Betätigen des Buttons "btnHinzufügen" aufgerufen fügt einen neuen Mitarbeiter in die Datenbank ein
	 * 
	 * @return true = erfolgreich, false = fehlgeschlagen
	 */
	public boolean addMitarbeiter(Worker worker) {
		boolean vorhanden = false;
		// holt sich alle Mitarbeiter und prüft ob der neu eingegeben Mitarbeiter bereits vorhanden ist
		ResultSet rs = SQLExecuter.executeQuery("SELECT * FROM Mitarbeiter " + "WHERE Login = '" + worker.getLogin() + "'");
		try {
			vorhanden = rs.next();
			rs.close();
			// wenn Mitarbeiter nicht vorhanden ist wird er in die Datenbank geschrieben
			if (!vorhanden) {
				ResultSet rs1 = SQLExecuter.executeQuery("SELECT * FROM Mitarbeiter ");
				rs1.moveToInsertRow();
				rs1.updateString("Login", worker.getLogin());
				rs1.updateString("Vorname", worker.getVorname());
				rs1.updateString("Name", worker.getName());
				rs1.updateInt("Berechtigung", worker.getBerechtigung());
				rs1.updateString("Passwort", "1234");
				rs1.updateDouble("Tagessatz", worker.getTagessatz());
				rs1.insertRow();
				rs1.close();
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		if (vorhanden) return false;
		else {
			over.reload();
			WPOverviewGUI.setStatusText("Mitarbeiter hinzugefügt");
			return true;
		}
	}
	
	public Worker getActualWorker() {
		return mitarbeiter;
	}

	public void passwordReset() {
		ResultSet rs = SQLExecuter.executeQuery("SELECT * FROM Mitarbeiter WHERE Login = '" + mitarbeiter.getLogin() + "';");
		try {
			//Passwort wird auf "1234" zurückgesetzt
			while(rs.next()){
				rs.updateString("Passwort", "1234");
				rs.updateRow();
				JOptionPane.showMessageDialog(null, "Das Passwort wurde auf \"1234\" zurückgesetzt!");
			}
			rs.close();
		} catch (SQLException e) {				
			e.printStackTrace();
		}
	}
}
