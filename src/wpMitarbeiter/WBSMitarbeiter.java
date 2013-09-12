package wpMitarbeiter;

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

public class WBSMitarbeiter {
	
	/**
	 * Variablen für die WBSMitarbeiterGUI und den SQLExecuter
	 * Variable für den Mitarbeiter und die WPOverview
	 */
	protected SQLExecuter sqlExec = new SQLExecuter();
	protected WBSMitarbeiterGUI gui = new WBSMitarbeiterGUI();
	protected Mitarbeiter mitarbeiter;
	protected WPOverview Overview;
	private WBSMitarbeiter dies;
		
	
	/**
	 * Konstruktoraufruf wenn vorhander Mitarbeiter geändert werden soll
	 * @param mit Mitarbeiter aus der WPOverview wird übergeben
	 * @param over WPOverview wird übergeben
	 * initialisiert die Textfelder mit den Daten des ausgewählten Benutzers
	 */
	public WBSMitarbeiter(Mitarbeiter mit, WPOverview over){
		dies = this;
		mitarbeiter = mit;
		Overview = over;
		gui.btnhinzufuegen.setVisible(false);
		gui.txfLogin.setEnabled(false);
		new WBSMitarbeiterButtonAction(dies);	
		initialize();
		gui.setTitle(mit.getLogin() + " | " + mit.getName() + ", " + mit.getVorname());
	}
	
	
	/**
	 * Konstruktoraufruf wenn neuer Mitarbeiter angelegt wird
	 * @param over WPOverview wird übergeben
	 */
	public WBSMitarbeiter(WPOverview over){
		dies = this;
		Overview = over;
		gui.btnedit.setVisible(false);
		gui.btnPwReset.setVisible(false);
		new WBSMitarbeiterButtonAction(dies);
		gui.setTitle("Neuer Mitarbeiter anlegen");
	}
	
	
	/**
	 * Setzt die Textfelder in der WBSMitarbeiterGUI mit den Daten des Benutzers
	 * anhand des übergebenen Mitarbeiters
	 */
	public void initialize(){	
		gui.txfLogin.setText(mitarbeiter.getLogin());
		gui.txfVorname.setText(mitarbeiter.getVorname());
		gui.txfName.setText(mitarbeiter.getName());
		gui.cbBerechtigung.setSelected((mitarbeiter.getBerechtigung()==1?true:false));
		gui.txfTagessatz.setText(Double.toString(mitarbeiter.getTagessatz()));	
	}	
	
	
	
	
	/**
	 * wird von der Methode addMitarbeiter() und changeMitarbeiter() aufgerufen
	 * Führt die PlausibilitätsPrüfung der einzelnen Felder in der WBSMitarbeiterGUI durch
	 * @return true = alles OK, false = es sind Unstimmigkeiten aufgetreten
	 */
	public boolean check(){
		//Login wurde nicht ausgefüllt
		if(gui.txfLogin.getText().equals("")){
			JOptionPane.showMessageDialog(gui, "Bitte Login ausfüllen");
			return false;
		}
		//Name wurde nicht ausgefüllt
		if(gui.txfName.getText().equals("") || gui.txfVorname.getText().equals("")){
			JOptionPane.showMessageDialog(gui, "Bitte Namen ausfüllen");
			return false;
		}		
		
		if(gui.txfTagessatz.getText().equals("")){
			JOptionPane.showMessageDialog(gui, "Bitte Tagessatz ausfüllen");
			return false;
		} 
		//Tagessatz hat nicht das Format eines Double-Wertes
		try {
			Double.parseDouble(gui.txfTagessatz.getText());
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(gui, "Tagessatz ist kein Double-Wert");
			return false;
		}
		return true;
		//Tagessatz ist nicht ausgefüllt
		
		
	}	
	
	/**
	 * wird beim Betätigen des Buttons "btnHinzufügen" aufgerufen
	 * fügt einen neuen Mitarbeiter in die Datenbank ein
	 * @return true = erfolgreich, false = fehlgeschlagen
	 */
	public boolean addMitarbeiter(){
		boolean vorhanden = false;
		if (!check()){
			return false;
		}
		else{
			//holt sich alle Mitarbeiter und prüft ob der neu eingegeben Mitarbeiter bereits vorhanden ist
			ResultSet rs = sqlExec.executeQuery("SELECT * FROM Mitarbeiter;");
			try {
				while(rs.next()){
					if(gui.txfLogin.getText().equals(rs.getString("Login"))){
						JOptionPane.showMessageDialog(gui, "Loginname schon besetzt");
						vorhanden = true;
					}
				}
				rs.close();
				//wenn Mitarbeiter nicht vorhanden ist wird er in die Datenbank geschrieben
				if(!vorhanden){
					ResultSet rs1 = sqlExec.executeQuery("SELECT * FROM Mitarbeiter ");
					rs1.moveToInsertRow();
					String login = gui.txfLogin.getText();
					String vorname = gui.txfVorname.getText();
					String name = gui.txfName.getText();
					int berechtigung = getCheckBox();
					String passwort = "1234";
					double tagessatz = Double.parseDouble(gui.txfTagessatz.getText());
					rs1.updateString("Login", login);
					rs1.updateString("Vorname", vorname);
					rs1.updateString("Name", name);
					rs1.updateInt("Berechtigung", berechtigung);
					rs1.updateString("Passwort", passwort);
					rs1.updateDouble("Tagessatz", tagessatz);
					rs1.insertRow();
					rs1.close();
					WPOverview.MitarbeiterListe.add(new Mitarbeiter(login, vorname, name, berechtigung, passwort, tagessatz));
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}finally{
				sqlExec.closeConnection();
			}
			if(vorhanden)
				return false;
			else{
				WPOverviewGUI.setStatusbar("Mitarbeiter hinzugefügt");
				return true;
			}
		}			
	}
	
	/**
	 * wird aus der Methode addMitarbeiter() und changeMitarbeiter() aufgerufen 
	 * gibt die Berechtigung aus der Checkbox zurück
	 * Berechtigung wird als Integer in die Tabelle geschreiben und somit muss der Boolean in den richtigen Integerwert umgewandelt werden
	 * @return Berechtigung 1=Projektleiterberechtigung 0=Mitarbeiterberechtigung
	 */
	public int getCheckBox(){
		if(gui.cbBerechtigung.isSelected()){
			return 1;
		}
		else{
			return 0;
		}
	}	
	
	/**
	 * wird beim Betätigen des Buttons "btnedit" aufgerufen
	 * Mitarbeiter-Daten werden aktualisiert
	 * @return true = erfolgreiche übernahme der Änderungen, false = übernahme fehlgeschlagen
	 */
	public boolean changeMitarbeiter(){	
		//Mitarbeiter wird aus der Datenbank geholt
		ResultSet rs1 = sqlExec.executeQuery("SELECT * FROM Mitarbeiter " +
				"WHERE Login = '" + mitarbeiter.getLogin() + "'");				
		try {
			while(rs1.next()){	
				//prüft ob Tex
				if(!check()){
					return false;					
				}
				//ändert den Mitarbeiter in der Mitarbeitertabelle der Datenbank
				else{
					rs1.updateString("Vorname", gui.txfVorname.getText());
					rs1.updateString("Name", gui.txfName.getText());
					rs1.updateInt("Berechtigung", getCheckBox());
					rs1.updateDouble("Tagessatz", Double.parseDouble(gui.txfTagessatz.getText()));
					rs1.updateRow();			
				}
				
			}
			rs1.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			sqlExec.closeConnection();
		}
		WPOverviewGUI.setStatusbar("Mitarbeiter geändert");
		return true;
	}	
	
}
