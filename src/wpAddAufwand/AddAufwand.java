/**
 * Studienprojekt:	WBS
 * 
 * Kunde:				Pentasys AG, Jens von Gersdorff
 * Projektmitglieder:	Andre Paffenholz, 
 * 						Peter Lange, 
 * 						Daniel Metzler,
 * 						Samson von Graevenitz
 * 
 * Funktionen zum Hinzufügen eines Arbeitspaketes
 * 
 * @author Daniel Metzler
 * @version 1.9 - 17.02.2011
 */

package wpAddAufwand;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.StringTokenizer;

import jdbcConnection.SQLExecuter;
import login.User;
import wpMitarbeiter.Mitarbeiter;
import wpOverview.WPOverview;
import wpShow.*;


public class AddAufwand {
	
	/**
	 * Variablen für die AddAufwandGui und den SQLExecuter
	 * Variablen für den User, WPID's und WPName
	 */
	public AddAufwandGui gui;
	public WPShow wpshow;
	private AddAufwand dies;
	private String wpID;
	private User usr;
	private String wpname;
	private String myaufwand;
	private int lvl1ID, lvl2ID, lvl3ID;
	private StringBuilder lvlxID = new StringBuilder();
	private ArrayList<Mitarbeiter> MitarbeiterListe = WPOverview.MitarbeiterListe;
	
	
	/**
	 * Default-Konstruktor
	 * @param wpID ID des Arbeitspakets
	 * @param usr Benutzer dessen Aufwand eingetragen werden soll
	 * @param show WPSHow GUI zur Referenzierung
	 * @param wpname Names des Arbeitspaketes
	 * Werte aus der WPShow werden übergeben
	 * 
	 */
	public AddAufwand(String wpID, User usr, WPShow show, String wpname){
		dies = this;
		gui = new AddAufwandGui();
		wpshow = show;
		this.wpID = wpID;
		new AddAufwandButtonAction(dies);
		this.usr = usr;
		this.wpname = wpname;
		initialize();
		gui.setTitle("Aufwand für " + wpID + " | " + wpname);
	}
	
	
	/**
	 * wird im Konstruktor aufgerufen
	 * Die LVLID's werden initialisiert durch die im Konstrukor übergebene wpID
	 * Textfelder für die ArbeitspaketID und den Arbeitspaket Name werden gefüllt
	 * wenn Projektleiterrechte vorliegen wird die ComboBox für die Benutzer mit allen zuständigen für dieses WP hinzugefügt
	 * ansonsten nur der Mitarbeiter selbst 
	 */
	public void initialize(){	
		int i = 0;
		StringTokenizer st = new StringTokenizer(wpID, ".");
		lvl1ID = Integer.parseInt(st.nextToken());
		lvl2ID = Integer.parseInt(st.nextToken());
		lvl3ID = Integer.parseInt(st.nextToken());
		while(st.hasMoreElements()){
			lvlxID.insert(i, st.nextToken(""));
			i++;
		}	
		lvlxID.deleteCharAt(0);
		gui.txfNr.setText(wpID);
		gui.txfName.setText(wpname);
		if(usr.getProjLeiter()){
			SQLExecuter sqlExec = new SQLExecuter();
			ResultSet rs1 = sqlExec.executeQuery(getSQLZustaendige());
			try {
				while(rs1.next()){
					String login = rs1.getString("FID_Ma");
					for(int j=0; j<MitarbeiterListe.size(); j++){					
						if(MitarbeiterListe.get(j).getLogin().equals(login)){
							gui.cobUser.addItem(MitarbeiterListe.get(j).getLogin() + " | " + MitarbeiterListe.get(j).getVorname() + " " + MitarbeiterListe.get(j).getName());
						}
					}

				}
				rs1.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			sqlExec.closeConnection();
		}
		else{
			gui.cobUser.addItem(usr.getLogin() + " | " + usr.getVorname() + " " + usr.getName());
			gui.cobUser.setEnabled(false);
		}	
	}	
	
	
	
	
	/**
	 * wird in der Methode initialize() aufgerufen wenn der User Projektleiterberechtigungen hat, um die Benutzer in die ComboBox zu füllen
	 * Alle zuständigen des Arbeitspakets werden aus der Paketzuweisungstabelle ausgelesen
	 * @return String aller zuständigen Mitarbeiter
	 */
	public String getSQLZustaendige(){		
		return "SELECT * FROM Paketzuweisung " +		
				"WHERE FID_LVL1ID = " + lvl1ID + " " +
				"AND FID_LVL2ID = " + lvl2ID + " " +
				"AND FID_LVL3ID = " + lvl3ID + " " +
				"AND FID_LVLxID = '" + lvlxID + "';";
	}
	
	

	/**
	 * prüft ob alle Eingabe ausgefüllt sind
	 * @return true/false, je nachdem ob Felder gefüllt oder nicht
	 */
	public boolean checkFieldsFilled(){
		if(gui.txfBeschreibung.getText().length() > 0 &&
				gui.txfAufwand.getText().length() > 0){
			return true;
		}
		return false;
	}
			
	
	
	/**
	 * wird vom Betätigen des Buttons für Editieren aufgerufen
	 * Der neue Aufwand wird in die "Aufwand"-Tabelle geschrieben
	 * @return true wenn Schreiben erfolgreich war, bei Fehler false
	 */
	public boolean addAufwand(){					
		try{
			
			//Prüfen, ob Datum valide ist
			String[] tmpDate;
			int month, day;
						
			if(!gui.txfDatum.getText().startsWith(".")){
				tmpDate = gui.txfDatum.getText().split("\\.");
				day = Integer.parseInt(tmpDate[0]);
				month = Integer.parseInt(tmpDate[1]);
				if(day < 1 || day > 31 || month < 1 || month > 12)
						return false;
			}
			else
				return false;
			
			//Datum wird vom Textfeld "Datum" geholt und als SQL Date umgewandelt (nötig für das Reinschreiben in die Datenbank-Tabelle)
			java.sql.Date dte=null;
			SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
			java.util.Date dt = formatter.parse(gui.txfDatum.getText());
			dte=new java.sql.Date(dt.getTime());
			//Kommas des Textfeldes Aufwand werden durch Punkte ersetzt, damit es zu keinen Fehlern kommt
			myaufwand = gui.txfAufwand.getText().replace(",", ".");
			
			String login = gui.cobUser.getSelectedItem().toString();   
		    int loginIndex = login.indexOf("|");
		    String userLogin = login.substring(0,loginIndex-1);
			//neuer Aufwand wird in die Datenbanktabelle Aufwand geschrieben
		    SQLExecuter sqlExec1 = new SQLExecuter();
			ResultSet rs1 = sqlExec1.executeQuery("SELECT * FROM  Aufwand");
			rs1.moveToInsertRow();
			rs1.updateInt("FID_Proj", 1);
			rs1.updateInt("LVL1ID", lvl1ID);
			rs1.updateInt("LVL2ID", lvl2ID);
			rs1.updateInt("LVL3ID", lvl3ID);
			rs1.updateString("LVLxID", lvlxID.toString());
			rs1.updateString("FID_Ma", userLogin);
			rs1.updateDate("Datum", dte);
			rs1.updateDouble("Aufwand", Double.parseDouble(myaufwand));
			rs1.updateString("Beschreibung", gui.txfBeschreibung.getText());	
			rs1.insertRow();
			rs1.close();	
			sqlExec1.closeConnection();
			WPShowGUI.setStatusbar("Aufwand hinzugefügt");
			return true;
			
		} catch (Exception e) {		
			e.printStackTrace();
			return false;
		}
				
	}
}
