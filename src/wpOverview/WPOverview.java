package wpOverview;
/**
 * Studienprojekt:	WBS
 * 
 * Kunde:				Pentasys AG, Jens von Gersdorff
 * Projektmitglieder:	Andre Paffenholz, 
 * 						Peter Lange, 
 * 						Daniel Metzler,
 * 						Samson von Graevenitz
 * 
 * 
 * GUI zum auswählen und Anzeigen der Arbeitspakete der USer
 * 
 * @author Samson von Graevenitz und Daniel Metzler
 * @version 0.1 - 30.11.2010
 */


import java.awt.dnd.DnDConstants;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.*;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import wpMitarbeiter.Mitarbeiter;
import jdbcConnection.SQLExecuter;
import login.User;

public class WPOverview{
	
	/**
	 * Variablen für die WPOverviewGUI,den SQLExecuter und dem User
	 */
	public WPOverviewGUI gui;
	protected User usr;
	public WPOverview dies;
	private final DecimalFormat decform = new DecimalFormat("#0.00");
	public static int ebenen = getEbenen();
	public static ArrayList<Mitarbeiter> MitarbeiterListe = getAllMitarbeiter();
	
	//Die Root-Knoten für die einzelnen Bäume (Aktiv, INaktiv und Fertig)
	protected DefaultMutableTreeNode rootAktiv, rootInaktiv, rootFertig, rootAlle;
	protected TreePath currentTP = null;
	
	//Projekt mit der ID 0.0.0.0.0 soll als Rootknoten im JTree dienen
	protected Workpackage projectWP;
	
	//Variable zum speichern des aktuell ausgewählten knoten
	protected TreePath tp;
	
	
	/**
	 * Konstruktor WPOverview()
	 * initialisiert die WPOverviewGUI,
	 * und beeinhaltet die Listener der WPOverviewGUI durch die Methode addButtonAction() 
	 */
	public WPOverview(User usr){
		//Projekt auslesen
		setProjectWP();

		//Rootknoten des Baumes definieren
		rootAktiv= new DefaultMutableTreeNode(projectWP);
		rootInaktiv = new DefaultMutableTreeNode(projectWP);
		rootFertig = new DefaultMutableTreeNode(projectWP);
		rootAlle = new DefaultMutableTreeNode(projectWP);
		
		this.usr = usr;
		//Baum anlegen
		createTrees();
		//GUI anzeigen
		gui = new WPOverviewGUI(usr.getProjLeiter(), rootAktiv, rootInaktiv, rootFertig, rootAlle);	
		//Mitarbeiter eintragen
		fillTbls();
		dies = this;
		new WPOverviewButtonAction(dies);
		getBaselines();
		aktualisieren();
		gui.setTitle("Übersicht Projekt " + getProjektName());
		
		//Tree Drag&Drop Funktionalität Hinzufügen
		if(usr.getProjLeiter()){
	        gui.dsAktiv = new TreeDragSource(gui.treeAktiv, DnDConstants.ACTION_MOVE);
	        gui.dtAktiv = new TreeDropTarget(gui.treeAktiv, this);
	        gui.dsAlle = new TreeDragSource(gui.treeAlle, DnDConstants.ACTION_MOVE);
	        gui.dtAlle = new TreeDropTarget(gui.treeAlle, this);	
		}
	
        ColorCellRenderer ccr = new ColorCellRenderer();
		
		for (int i=0; i<gui.tblMitarbeiter.getColumnCount();i++){		
			gui.tblMitarbeiter.getColumnModel().getColumn(i).setCellRenderer(ccr);		
		}	
	}
	
	
	/**
	 * Gibt den Namen des Projekts zurück
	 * @return Projektname
	 */
	private String getProjektName(){
		SQLExecuter sqlExecgetprojekt = new SQLExecuter();
		ResultSet proj = sqlExecgetprojekt.executeQuery("Select * FROM Projekt");
		String name = "";
		try {
			while(proj.next()){
				name = proj.getString("Name");
			}
			proj.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		sqlExecgetprojekt.closeConnection();
		
		return name;
	}
	
	
	/**
	 * wird beim initialisieren der Datenelemente aufgerufen
	 * speichert in eine ArrayListe alle im Projekt vorhandenen Mitarbeiter
	 * @return ArrayListe mit allen Mitarbeitern
	 */
	public static ArrayList<Mitarbeiter> getAllMitarbeiter(){
		SQLExecuter sqlExecistOAP = new SQLExecuter();
		ArrayList<Mitarbeiter> AlleMit = new ArrayList<Mitarbeiter>();
		ResultSet mit = sqlExecistOAP.executeQuery("Select * FROM Mitarbeiter");
		try {
			while(mit.next()){
				String Login = mit.getString("Login");
				String Vorname = mit.getString("Vorname");
				String Name = mit.getString("Name");
				int Berechtigung = mit.getInt("Berechtigung");
				String Passwort = mit.getString("Passwort");
				double Tagessatz = mit.getDouble("Tagessatz");			
				AlleMit.add(new Mitarbeiter(Login, Vorname, Name, Berechtigung, Passwort, Tagessatz));
			}
			mit.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		sqlExecistOAP.closeConnection();
		return AlleMit;
	}
	
	
	/**
	 * Liefert einen String zurück mit der Anzahl der passenden Stellen für LVLxID
	 * anhand der definierten Ebenen im Projekt 
	 * Wird von generateAnalyse() in der Baseline aufgerufen
	 * @return String der LVLxID
	 */
	public static String generateXEbene(){
		String Nullen = "0";
		for (int i=4;i<ebenen;i++){
			Nullen += ".0";
		}
		return Nullen;
		
	}
	
	
	/**
	 * Liefert die Anzahl der Festgelegten Ebenen im Projekt zurück
	 * Wird vom statischen Datenelement ebenen aufgerufen, um dies zu instanziieren
	 * @return Anzahl der Ebenen in einem Projekt
	 */
	public static int getEbenen(){
		SQLExecuter sqlExecProjekt = new SQLExecuter();
		ResultSet rsProjekt = sqlExecProjekt.executeQuery("SELECT * FROM Projekt");
		try {
			if(rsProjekt.next()){
				ebenen = rsProjekt.getInt("Ebenen");
			}
			rsProjekt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			sqlExecProjekt.closeConnection();
		}
		return ebenen;
	}
	
	
	/**
	 * Liest das erste Arbeitspaket in der Datenbank aus und setzt dies als Root-Knoten für alle JTrees
	 * Wird direkt im Konstruktor dieser Klasse aufgerufen
	 */
	private void setProjectWP(){
		SQLExecuter sqlExecLVL1 = new SQLExecuter();
		try {
			ResultSet rsLVL1 = sqlExecLVL1.executeQuery("SELECT * FROM Arbeitspaket where LVL1ID=0");	
			while(rsLVL1.next()){
				projectWP = createWP(rsLVL1);	
			}
			rsLVL1.close();
			}							
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			sqlExecLVL1.closeConnection();
		}
	}
	
	
	
	/**
	 * Erstellt eine Baseline mit aktuellem Datum
	 * Diese Methode wird aufgerufen von 
	 * 	- Dem Button im Menü
	 * 	- Der Methode aktualisieren()
	 *  - im überladenen Konstruktor der WPOverview
	 */
	protected void getBaselines(){
		if(usr.getProjLeiter()){
			gui.cobChooseBaseline.removeAllItems();
			SQLExecuter sqlExecBaseline = new SQLExecuter();
			try {
				ResultSet rsBaseline = sqlExecBaseline.executeQuery("SELECT * FROM Baseline");	
				while(rsBaseline.next()){
					String Beschreibung = rsBaseline.getString("Beschreibung");
					java.sql.Date dte= rsBaseline.getDate("Datum");	
					int number = rsBaseline.getInt("ID");
					gui.cobChooseBaseline.addItem(number + " | " + dte + " | " + Beschreibung);				
				}							
				rsBaseline.close();
				gui.cobChooseBaseline.setSelectedIndex(gui.cobChooseBaseline.getItemCount()-1);
				if(gui.cobChooseBaseline.getItemCount() == 0){
					gui.btnShowBaseline.setEnabled(false);
					gui.btnChart.setEnabled(false);
					gui.btnComp.setEnabled(false);
				}else{
					gui.btnShowBaseline.setEnabled(true);
					gui.btnChart.setEnabled(true);
					gui.btnComp.setEnabled(true);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				sqlExecBaseline.closeConnection();
			}
		}
	}
	
	
	/**
	 * Diese Methode aktualisiert die Mitarbeiter Tabelle in der GUI
	 * Diese Methode wird beim Hinzufügen oder Bearbeiten eines Mitarbeiters aufgerufen
	 * addButtonAction() in der WBS Mitarbeiter
	 * 
	 */
	public void mitarbeiterAktualisieren(){
		fillTbls();
		gui.tblMitarbeiter.repaint();
	}
	
	
	
	
	/**
	 * 	Nachdem Werte in der GUI geändert wurden, muss die fürbung des Baumes angepasst werden -
	 *	daher werden alle Bäume hier aktualisiert
	 *  Weiterhin werden die Baselines neu ausgelesen und die Mitarbeiter-Tabelle aktualisiert
	 */
	public void aktualisieren(){
		resetTrees();
		getBaselines();
		gui.cobChooseBaseline.repaint();
		fillTbls();
		WPOverviewGUI.setStatusbar("Ansicht aktualisiert");
	}
	
	
	
	/**
	 * Diese Methode aktualisert alle Bäume in der GUI, in dem die Wurzeln resettet werden,
	 * die Bäume insgesamt neu ausgelesen, und das Baum-Model anSchließend neu geladen wird
	 */
	protected void resetTrees(){
		//Projekt neu einlesen
		setProjectWP();
		
		//Wurzeln zurücksetzen
		rootAktiv= new DefaultMutableTreeNode(projectWP);
		rootInaktiv = new DefaultMutableTreeNode(projectWP);
		rootFertig = new DefaultMutableTreeNode(projectWP);
		rootAlle = new DefaultMutableTreeNode(projectWP);	

		//Baum neu anlegen
		createTrees();
		
		//Neue Models erzeugen und zuweisen
		DefaultTreeModel model = new DefaultTreeModel(rootAktiv);
		gui.treeAktiv.setModel(model);
		
		model = new DefaultTreeModel(rootInaktiv);
		gui.treeInaktiv.setModel(model);
		
		model = new DefaultTreeModel(rootFertig);
		gui.treeFertig.setModel(model);
		
		model = new DefaultTreeModel(rootAlle);
		gui.treeAlle.setModel(model);
		
		
	}
	
	
	
	
	/**
	 * Setter für den Pfad zum Paket
	 * @param tp Pfad zu aktuell ausgewähltem Knoten
	 */
	public void setTreepath(TreePath tp){
		this.currentTP=tp;
	}
	
	
	/**
	 * erzeugt alle JTRees durch Aufruf im Konstruktor
	 */
	public void createTrees(){
		ArrayList<Workpackage> wps = getUsersWPs();
		TreeBuilder.createTreeAktiv(rootAktiv, wps);
		TreeBuilder.createTreeFertig(rootFertig, wps);
		TreeBuilder.createTreeInaktiv(rootInaktiv, wps);
		TreeBuilder.createTreeAlle(rootAlle, wps);
	}
	
	

	/**
	 * Mehtode, die alle Mitarbeiter als ArrayListe zurückgibt
	 * Diese wird von fillTbls() aufgerufen
	 * @return ArrayList mit allen Mitarbeitern
	 */
	private ArrayList<Mitarbeiter> getMitarbeiter(){
		
		SQLExecuter sqlExecAllMitarbeiter = new SQLExecuter(); 
		ResultSet rsAllMitarbeiter = sqlExecAllMitarbeiter.executeQuery("SELECT * FROM Mitarbeiter;");
		
		ArrayList<Mitarbeiter> wps = new ArrayList<Mitarbeiter>();
		
		try {			
			
			if(usr.getProjLeiter()){
				
				while(rsAllMitarbeiter.next()){
	
					String Login = rsAllMitarbeiter.getString("Login");
					String Vorname = rsAllMitarbeiter.getString("Vorname");
					String Name = rsAllMitarbeiter.getString("Name");
					int Berechtigung = rsAllMitarbeiter.getInt("Berechtigung");
					String Passwort = rsAllMitarbeiter.getString("Passwort");
					Double Tagessatz = rsAllMitarbeiter.getDouble("Tagessatz");
					wps.add(new Mitarbeiter(Login, Vorname, Name, Berechtigung, Passwort, Tagessatz));
	
				}
				rsAllMitarbeiter.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			sqlExecAllMitarbeiter.closeConnection();
		}
		return wps;
		
	}
	
	
	
	/**
	 * Alle Arbeitspakete des aktuellen Benutzers werden ausgelesen und zurückgegeben
	 * Die Methode wird bei createTree() aufgerufen, um die Arbeitspakete direkt als Parameter
	 * bei der Erstellung des Baumes verwenden zu können
	 * @return ArrayList mit allen Arbeitspaketen des aktuellen Benutzers
	 */
	private ArrayList<Workpackage> getUsersWPs(){
		
		SQLExecuter sqlExecWPs = new SQLExecuter();
		
		ArrayList<Workpackage> wps = new ArrayList<Workpackage>();
		
		try {
			
			ResultSet rsWPs = null;
			if(usr.getProjLeiter()){
				rsWPs = sqlExecWPs.executeQuery("SELECT * FROM Arbeitspaket;");
			}else{
				rsWPs = sqlExecWPs.executeQuery("SELECT a.* FROM Arbeitspaket As a, Paketzuweisung As p " +
											"WHERE p.FID_LVL1ID = a.LVL1ID and p.FID_LVL2ID = a.LVL2ID and p.FID_LVL3ID = a.LVL3ID and p.FID_LVLxID = a.LVLxID and p.FID_Ma = '" + usr.getLogin() + "' " +
											"ORDER BY FID_LVL1ID ASC, FID_LVL2ID ASC, FID_LVL3ID ASC, FID_LVLxID ASC; ");
			}
		
			while(rsWPs.next()){
				//Only for use with multiproject-functionality
				//wps.add(new Workpackage(FID_Proj, LVL1ID, LVL2ID, LVL3ID, LVLxID, name, beschreibung, planaufwand, release, istOAP, istInaktiv, FID_Ma));
				wps.add(createWP(rsWPs));
			}
			rsWPs.close();
		
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			sqlExecWPs.closeConnection();
		}
		return wps;
		
	}
	
	
	/**
	 * Aufruf Methode zum füllen der Mitarbeiter Tabelle in der GUI
	 * Diese Methode wird von folgenden Methoden aufgerufen:
	 * 	- Aktualisieren()
	 * 	- Mitarbeiter Aktualisieren()
	 * 	- dem überladenen Konstruktor der WPOverview
	 */
	public void fillTbls(){
		fillTblWPs(getMitarbeiter());		
	}
	
	
	/**
	 * Methode zum füllen der Mitarbeiter Tabelle in der GUI
	 * Diese Methode wird von fillTbls() aufgerufen
	 * @param MitList Alle ausgelesenen Mitarbeiter als ArrayList
	 */
	public void fillTblWPs(ArrayList<Mitarbeiter> MitList){
		Vector<Vector<String>> rows;
		gui.Mitarbeiter.clear();		
		rows = gui.Mitarbeiter;
		
		for(Mitarbeiter Mit: MitList){		
			rows = gui.Mitarbeiter;		
			Vector<String> row = new Vector<String>();
			
			row.addElement(Mit.getLogin());
			row.addElement(Mit.getVorname());
			row.addElement(Mit.getName());
			int ber = Mit.getBerechtigung();
			if(ber == 1){
				row.addElement("Projektleiter");
			}
			else{
				row.addElement("Mitarbeiter");
			}	
			
			row.addElement(decform.format(Mit.getTagessatz()).replace(".", ",") + " €");
			rows.add(row);
			
		}
	}	
	
	
	/**
	 * Löscht ein Arbeitspaket aus der Datenbank (und dem Tree) sofern zu dem Paket bzw. den Unterpaketen kein AC eingetragen ist
	 * Soll ein OAP gelöscht werden, so wird geprüft, ob die Unterarbeitspakete einen Aufwand eingetragen haben. Falls nicht,
	 * wird das OAP inklusive der Unterpakete gelöscht.
	 * Diese Methode wird durch das Menü aufgerufen, bzw. den ActionLister von miDelAp.
	 * @param id ID des Pakets welches gelöscht werden soll
	 */
	protected void deleteWP(String id){		
		Workpackage wp = null;
		String[] tmpId = id.split("\\.");
		String lvlx = "";
		String whereClause;
		String neuLvlx = "";
		
		for(int i=3;i<tmpId.length;i++){
			if(lvlx.equals(""))
				lvlx = tmpId[i];
			else
				lvlx += "." + tmpId[i];
		}

		SQLExecuter sqlExecCurrentAP = new SQLExecuter();
		SQLExecuter sqlExecAufwand = new SQLExecuter(); 
		try{
			//Arbeitspaket auslesen
			ResultSet rsCurrentAP = sqlExecCurrentAP.executeQuery("SELECT * FROM Arbeitspaket WHERE FID_Proj = 1 AND LVL1ID = " + Integer.parseInt(tmpId[0]) + " AND LVL2ID = " + Integer.parseInt(tmpId[1]) + " AND LVL3ID = " + Integer.parseInt(tmpId[2]) + " AND LVLxID = '" + lvlx + "';");
			if(rsCurrentAP.next()){
				wp = createWP(rsCurrentAP);
			}
			rsCurrentAP.close();
			//Arbeitspaket löschen, sofern erlaubt --> OAP nur löschen wenn alle UAP ohne Aufwand
			if(wp != null){
				
				//suchID zusammenbauen
				tmpId = getOapId(id);
								
				//je nach Anzahl der Stellen eine entsprechende WHERE Klausel formulieren
				if(tmpId.length==1)
						whereClause= " where LVL1ID = " + Integer.parseInt(tmpId[0]);
				else
					if(tmpId.length==2)
						whereClause= " where LVL1ID = " + Integer.parseInt(tmpId[0]) + " AND LVL2ID = " + Integer.parseInt(tmpId[1]);
					else
						if(tmpId.length==3)
							whereClause= " where LVL1ID = " + Integer.parseInt(tmpId[0]) + " AND LVL2ID = " + Integer.parseInt(tmpId[1]) + 
							" AND LVL3ID = " + Integer.parseInt(tmpId[2]);
						else{
							//lvlx anhand der Stellen zusamensetzen
							for(int i=3;i<tmpId.length;i++){
								if(neuLvlx.equals(""))
									neuLvlx = tmpId[i];
								else
									neuLvlx = "." + tmpId[i];
							}
							
							whereClause= " where LVL1ID = " + Integer.parseInt(tmpId[0]) + " AND LVL2ID = " + Integer.parseInt(tmpId[1]) + 
							" AND LVL3ID = " + Integer.parseInt(tmpId[2])  + "  AND LVLxID = '" + neuLvlx + "'";	
						}
											
				
				//UAPs lesen, sofern vorhanden
				ResultSet rsAufwand = sqlExecAufwand.executeQuery("SELECT * FROM Aufwand "+ whereClause + ";");
				 if(rsAufwand.next()){
					 JOptionPane.showMessageDialog(gui, "Das gewünschte Arbeitspaket kann nicht gelöscht werden, da bereits Auwände dazu oder zu Unterpaketen erfasst wurde!");
				 }
				 else{
					//Arbeitspaket löschen
					SQLExecuter sqlExecDelete = new SQLExecuter();
					//Prüfen ob OAP oder normales AP
					if(wp.isIstOAP()){
						//alle Pakete drunter auch entfernen
						sqlExecDelete.executeUpdate("DELETE * FROM Arbeitspaket " + whereClause +";");
					}
					
					sqlExecDelete.executeUpdate("DELETE * FROM Arbeitspaket WHERE FID_Proj = 1 AND LVL1ID = " + wp.getLvl1ID() + " AND LVL2ID = " + wp.getLvl2ID() + " AND LVL3ID = " + wp.getLvl3ID() + " AND LVLxID = '" + wp.getLvlxID() + "';");
					WPOverviewGUI.setStatusbar("Arbeitspaket gelöscht");
					//JOptionPane.showMessageDialog(gui, "Das Arbeitspaket wurde erfolgreich gelöscht!");
					sqlExecDelete.closeConnection();
					aktualisieren();						

				 }
				 
				rsAufwand.close();
				sqlExecAufwand.closeConnection();
			}
			
		}
		catch(Exception ex){
			JOptionPane.showMessageDialog(gui, "Das gewünschte Arbeitspaket kann nicht gelöscht werden, da bereits Auwände dazu erfasst wurden oder weil es ein Oberarbeitspaket in der Baseline ist.");
			//ex.printStackTrace();
		}
		sqlExecCurrentAP.closeConnection();
	}
	
	
	
	/**
	 * Erstellt ein Arbeitspaket anhand des übergebenen (gefüllten) ResultSets
	 * Wrid von folgenden Methoden aufgerufen:
	 * 	- deleteWP()
	 * 	- getUserWPs()
	 * 	- setProjectWP()
	 * 
	 * @param rs gefülltes ResultSet
	 * @return Arbeitspaket
	 */
	private Workpackage createWP(ResultSet rs){	
		//in dieser Methode wird ein bereits gefülltes ResultSet übergeben, von daher muss dieses auch wieder
		// außerhalb geschlossen werden!
		Workpackage wp = null;
		try {
				//Only for use with multiproject-functionality
				//int FID_Proj = rs.getInt("FID_Proj");
				int LVL1ID = rs.getInt("LVL1ID");
				int LVL2ID = rs.getInt("LVL2ID");
				int LVL3ID = rs.getInt("LVL3ID");
				String LVLxID = rs.getString("LVLxID");
				String name = rs.getString("Name");
				String beschreibung = rs.getString("Beschreibung");
				Double cpi = rs.getDouble("CPI");
				Double bac = rs.getDouble("BAC");
				Double ac = rs.getDouble("AC");
				Double ev = rs.getDouble("EV");
				Double etc = rs.getDouble("ETC");
				Double wpTagessatz = rs.getDouble("WP_Tagessatz");
				Double eac = rs.getDouble("EAC");
				Double bacKosten = rs.getDouble("BAC_Kosten");
				Double acKosten = rs.getDouble("AC_Kosten");
				Double etcKosten = rs.getDouble("ETC_Kosten");			
				String release = rs.getString("Release");
				boolean istOAP = rs.getBoolean("istOAP");
				boolean istInaktiv = rs.getBoolean("istInaktiv");
				String fid_Leiter = rs.getString("FID_Leiter");
				ArrayList<String> zustaendige = new ArrayList<String>();
				
				wp = new Workpackage(LVL1ID, LVL2ID, LVL3ID, LVLxID, name, beschreibung, cpi, bac, 
						ac, ev, etc, wpTagessatz, eac, bacKosten, acKosten, etcKosten, release, istOAP, istInaktiv, fid_Leiter, zustaendige);
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return wp;
	}
	
	
	
	/**
	 * Liefert zu einer gegebenen AP-ID die passende OAP ID
	 * Wird von deleteWp() und setUapsInaktiv() verwendet
	 * @param id AP ID als String
	 * @return Array bestehend aus den OAP Leveln
	 */
	public String[] getOapId(String id){
		String[] tmpId = id.split("\\.");
		String suchID ="";

		//OAP-ID suchen
		for(int i=0;i<tmpId.length-1;i++){
			if(Integer.parseInt(tmpId[i].trim()) == 0 && Integer.parseInt(tmpId[i+1].trim()) == 0) {
				break;
			}
			else {
				if(suchID.equals(""))
					suchID = tmpId[i].trim();
				else 
					suchID += "." + tmpId[i].trim();
			}
		}

		//suchID zusammenbauen
		tmpId = suchID.split("\\.");
		
		return tmpId;
	}
	
	
	
	/**
	 * Setzt alle Unterarbeitspakete zu einem bestehenden OAP inaktiv, falls das OAP als inaktiv markiert wird
	 * Wird von WPShow.istOAPsetChanges() aufgerufen
	 */
	public void setUapsInaktiv(String id, boolean isInak){
		
		//Projekt ignorieren
		if(!id.startsWith("0")){
			String[] oap = getOapId(id);
			String whereCl = "";
			
			switch(oap.length){
				case 1: whereCl = " where LVL1ID = " + oap[0] + ";";
					break;
				case 2: whereCl = " where LVL1ID = " + oap[0] + " AND LVL2ID = " + oap[1] + ";";
					break;
				case 3: whereCl = " where LVL1ID = " + oap[0] + " AND LVL2ID = " + oap[1] + " AND LVL3ID = " + oap[2] +";";
					break;
				case 4: whereCl = " where LVL1ID = " + oap[0] + " AND LVL2ID = " + oap[1] + " AND LVL3ID = " + oap[2] + " AND LVLxID = '" + oap[3] +"';";
				break;
			}
			
			SQLExecuter sqlExec= new SQLExecuter(); 
			ResultSet rs = sqlExec.executeQuery("SELECT * FROM Arbeitspaket " + whereCl);
			try {
				while(rs.next()){
					rs.updateBoolean("istInaktiv", isInak);
					rs.updateRow();
				}
				rs.close();
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			sqlExec.closeConnection();	
		}		
	}
}
