package chooseDB;

/**
 * Studienprojekt:	WBS
 * 
 * Kunde:				Pentasys AG, Jens von Gersdorff
 * Projektmitglieder:	Andre Paffenholz, 
 * 						Peter Lange, 
 * 						Daniel Metzler,
 * 						Samson von Graevenitz
 * 
 * Ruft die DBChooserGUI auf
 * setzt nach der Pfadeingabe den Pfad in der MDBConnect Klasse
 * 
 * 
 * @author Samson von Graevenitz und Daniel Metzler
 * @version 0.3 - 09.12.2010
 */

import java.util.Date;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;

import jdbcConnection.MDBConnect;
import jdbcConnection.SQLExecuter;
import login.Login;

public class DBChooser {	

	/**
	 * übergibt ein Object von der DBChooserGUI
	 */
	public DBChooserGUI gui;
	protected DBChooser dies;
	/**
	 * Bedeutung: wurde der Cancel Button im Projektebenen Fenster gedrückt?
	 */
	private boolean cancel=false;
	/**
	 * Bedeutung: Variable zum Auslesen der Ebenen aus dem Textfeld
	 */
	private String ebenen="";
	/**
	 * Konstruktor DBChooser()
	 * initialisiert die DBChooserGUI,
	 * und beeinhaltet die Listener der DBChooserGUI durch die Methode addButtonAction() 
	 */
	public DBChooser(){
		dies = this;
		gui = new DBChooserGUI();
		new DBChooserButtonAction(dies);
		
	}
	
	/**
	 * void weiter()
	 * wird durch das Betätigen des "weiter" Buttons ausgeführt
	 * prüft ob das Textfeld für den Pfad ausgefüllt ist.
	 * Und stellt bei erfolgreicher Ausfüllung des Textfeldes eine Verbindung zur MDB her per MDBConnect Klasse her
	 * und setzt den aktuell eingegeben Pfad.
	 * In der Methode dbInitial() wird geprüft, ob alle notwendigen Daten in der DB vorhanden sind, andernfalls
	 * werden diese angelegt.
	 * Danach wird der Login gestartet.
	 */
	public void weiter(){
		cancel=false;
		if(gui.cobDBAuswahl.getSelectedItem().equals("")){
			JOptionPane.showMessageDialog(gui, "Bitte geben Sie einen Pfad an!");
		}else if(!gui.cobDBAuswahl.getSelectedItem().equals("")&&!gui.cobDBAuswahl.getSelectedItem().toString().endsWith(".mdb")){
			JOptionPane.showMessageDialog(gui, "Bitte nur *.mdb Dateien verwenden!");
		}
		else{
			saveToHistory(gui.cobDBAuswahl.getSelectedItem().toString());
			MDBConnect.setPathDB(gui.cobDBAuswahl.getSelectedItem().toString());
			dbInitial();
			if(!cancel){
				gui.dispose();
				new Login();
			}
		}
	}
	
	/**
	 * Kopiert die leere Datenbank und gibt ihr den Namen welcher als Projekt-Name
	 * angegeben wurde, zusätlich werden die notwendigen Daten in die Datenbank
	 * kopiert, z. B.:
	 * 		Projekt:				Das Projekt
	 * 		Default-Mitarbeiter:	Leiter	(PW: 1234, Projektleiterberechtigung)
	 * 		Default-AP:				ID 0.0.0.x
	 */
	public void dbInitial(){
		int myEbenen = 0;
		String projname = "";
		SQLExecuter sqlExecProjekt = new SQLExecuter();
		
		ResultSet rsProjekt = sqlExecProjekt.executeQuery("SELECT * FROM Projekt");
		try {
			if(!rsProjekt.next()){
				boolean gueltig=false;
				while(!gueltig){
					projname = JOptionPane.showInputDialog(null,"Geben Sie den Projektnamen an.",
	                        "Projektname",
	                        JOptionPane.PLAIN_MESSAGE);
					ebenen = JOptionPane.showInputDialog(null,"Geben Sie die Maximale Ebenenanzahl an.",
	                        "Anzahl Projektebenen",
	                        JOptionPane.PLAIN_MESSAGE);
					
					//Kein leerer Projektname erlaubt, wenn leer oder nach Cancel wird Programm geschlossen
					if(projname==null||projname.equals("")){
						sqlExecProjekt.closeConnection();
						System.out.println("Durch Benutzer abgebrochen");
						System.exit(0);
					}
					else {
						if(ebenen!=null){
							try{
								int anz = Integer.valueOf(ebenen).intValue();
								if(anz > 3){
									gueltig=true;
								} else{
									JOptionPane.showMessageDialog(null,"Mindestens 4 Ebenen", "Es müssen mindestens 4 Ebenen angelegt werden.",
					                        JOptionPane.ERROR_MESSAGE);
								}
							}catch(NumberFormatException e){
								JOptionPane.showMessageDialog(null,"Bitte geben sie eine gültige Zahl an!",
				                        "Anzahl Projektebenen",
				                        JOptionPane.ERROR_MESSAGE);
							}
						}else{
							gueltig=true;
							cancel=true;
						}						
					}
				}
			}
			rsProjekt.close();	
		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally{
			sqlExecProjekt.closeConnection();
		}
		if(ebenen!=null&&projname!=null&&!projname.equals("")){
			//Kopiert die leere MDB und ändern den Namen
			copyAndRename(MDBConnect.getPathDB(), projname);
			SQLExecuter sqlExecMitarbeiter = new SQLExecuter();
			ResultSet rsMitarbeiter = sqlExecMitarbeiter.executeQuery("SELECT * FROM Mitarbeiter");
			try {
				if(!rsMitarbeiter.next()){
					rsMitarbeiter.moveToInsertRow();
					rsMitarbeiter.updateString("Login", "Leiter");
					rsMitarbeiter.updateString("Vorname", "DO NOT");
					rsMitarbeiter.updateString("Name", "USE");
					rsMitarbeiter.updateInt("Berechtigung", 1);
					rsMitarbeiter.updateString("Passwort", "1234");
					rsMitarbeiter.updateDouble("Tagessatz", 100);
					rsMitarbeiter.insertRow();
					System.out.println("Leiter wurde angelegt \n User: Leiter\nPasswort: 1234");
				}
				rsMitarbeiter.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			} finally{
				sqlExecMitarbeiter.closeConnection();
			}
			
			sqlExecProjekt = new SQLExecuter();
			rsProjekt = sqlExecProjekt.executeQuery("SELECT * FROM Projekt");
			try {
				if(!rsProjekt.next()){

					myEbenen = Integer.parseInt(ebenen);
					rsProjekt.moveToInsertRow();
					rsProjekt.updateString("FID_Leiter", "Leiter");
					rsProjekt.updateString("Name", projname);
					rsProjekt.updateInt("Ebenen", Integer.parseInt(ebenen));
					rsProjekt.insertRow();
					System.out.println("Projekt wurde angelegt");
				}
				rsProjekt.close();	
			} catch (SQLException e1) {
				e1.printStackTrace();
			} finally{
				sqlExecProjekt.closeConnection();
			}
			
			SQLExecuter sqlExecAPs = new SQLExecuter();
			ResultSet rsAPs = sqlExecAPs.executeQuery("SELECT * FROM Arbeitspaket");
			try {
				if(!rsAPs.next()){
					java.util.Date dt = new Date();
					java.sql.Date date = new java.sql.Date(dt.getTime());
					
					rsAPs.moveToInsertRow();
					rsAPs.updateInt("FID_Proj", 1);
					rsAPs.updateInt("LVL1ID", 0);
					rsAPs.updateInt("LVL2ID", 0);
					rsAPs.updateInt("LVL3ID", 0);
					
					String Nullen = "0";
					for (int i=4;i<myEbenen;i++){
						Nullen += ".0";
					}
					
					rsAPs.updateString("LVLxID", Nullen);
					rsAPs.updateString("FID_Leiter", "Leiter");
					rsAPs.updateString("Name", projname);
					rsAPs.updateString("Beschreibung", "Projekt");
					rsAPs.updateDouble("BAC", 0);
					rsAPs.updateDouble("AC", 0);
					rsAPs.updateDouble("EV", 0);
					rsAPs.updateDouble("ETC", 0);
					rsAPs.updateDouble("EAC", 0);
					rsAPs.updateDouble("CPI", 1.00);
					rsAPs.updateDouble("BAC_Kosten", 0);
					rsAPs.updateDouble("AC_Kosten", 0);
					rsAPs.updateDouble("ETC_Kosten", 0);
					rsAPs.updateDouble("WP_Tagessatz", 0);
					rsAPs.updateDate("Release", date);
					rsAPs.updateBoolean("istOAP", true);
					rsAPs.updateBoolean("istInaktiv", false);
					rsAPs.insertRow();
					
					SQLExecuter sqlExecAPZuweisung = new SQLExecuter();
					ResultSet rsAPZuweisung = sqlExecAPZuweisung.executeQuery("SELECT * FROM Paketzuweisung");
					rsAPZuweisung.moveToInsertRow();
					rsAPZuweisung.updateInt("FID_Proj", 1);
					rsAPZuweisung.updateInt("FID_LVL1ID", 0);
					rsAPZuweisung.updateInt("FID_LVL2ID", 0);
					rsAPZuweisung.updateInt("FID_LVL3ID", 0);
					rsAPZuweisung.updateString("FID_LVLxID", Nullen);
					rsAPZuweisung.updateString("FID_Ma", "Leiter");
					rsAPZuweisung.insertRow();
					rsAPZuweisung.close();
					System.out.println("AP wurde angelegt");
					sqlExecAPZuweisung.closeConnection();
				}
				rsAPs.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			} finally{
				sqlExecAPs.closeConnection();
			}
	
		}
	}
	
	/**
	 * void oeffnen()
	 * wird durch das Betätigen des "öffnen" Buttons ausgeführt
	 * erstellt einen JFileChooser in der der Benutzer eine MDB-Datei auf seinem Filesystem auswählen kann
	 * diese schreibt wird dann in das Textfeld für die Pfadangabe geschrieben
	 */
	public void oeffnen(){
		gui.fileIn= new JFileChooser(".");
		gui.fileIn.setFileFilter(new FileFilter() {
		   
		    public String getDescription () { 
		    	return "MDB's"; 
		    }
			public boolean accept(File f) {
				if (f.isDirectory()) return true;
			    	return f.getName().toLowerCase().endsWith(".mdb");
			}  
		  });
		gui.fileIn.setMultiSelectionEnabled(false);
		if (gui.fileIn.showOpenDialog(gui) == JFileChooser.APPROVE_OPTION){
			String file = gui.fileIn.getSelectedFile().toString();
			gui.cobDBAuswahl.setSelectedItem(file);
		}
	}
	
	/**
	 * Kopiert die leere MDB und ändern deren Namen in den angegebenen Projektnamen
	 * wenn bereits eine Datei mit dem Namen besteht, wird fortlaufend in Klammern
	 * die Anzahl der gleichnamigen Dateien hochgezählt
	 * 
	 * @param dbPath - Pfad zur MDB-Datenbank
	 * @param projname - Projektname
	 */
	public void copyAndRename(String dbPath, String projname) {
		String infile = dbPath.subSequence(4, dbPath.length()) + "";
		String outfile = dbPath.subSequence(4, dbPath.lastIndexOf("/") + 1)
				+ projname + ".mdb";
		System.out.println(infile);
		System.out.println(outfile);
		File outFile = new File(outfile);
		int i = 0;
		while (outFile.isFile()) {
			i++;
			outfile = outfile.subSequence(0, outfile.lastIndexOf(".")) + "(" + i + ").mdb";
			outFile = new File(outfile);
		}
		try {
			FileInputStream in = new FileInputStream(infile);
			FileOutputStream out = new FileOutputStream(outfile);
			byte[] c = new byte[4096];
			while (in.available() > 0) {
				in.read(c);
				out.write(c);
			}
			in.close();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JOptionPane.showMessageDialog(gui, "Es wurde eine neue DB-File angelegt\n" + outfile);
		saveToHistory(outfile.replace("/", "\\"));
		MDBConnect.setPathDB(outfile);
	}
	
	/**
	 * füllt die ComboBox mit Pfaden zu bereits bekannten MDBs
	 */
	public void fillDBAuswahl(){
		File dbPaths = new File("DBHistory.txt");
		if(!dbPaths.canRead()){
			try {
				FileWriter out = new FileWriter(dbPaths);
				File wbsEmpty = new File("WBS leer.mdb");
				if (wbsEmpty.exists()){
					out.write("\n" + wbsEmpty.getAbsolutePath() + "\n");
				} else{
					out.write("\n");
				}
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			BufferedReader in = new BufferedReader(new FileReader("DBHistory.txt"));
			while(in.ready()){
				gui.cobDBAuswahl.addItem(in.readLine());
			}
			in.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
		System.out.println(dbPaths.canRead());
	}
	
	/**
	 * speichert noch nicht bekannte MDB-Pfade in die Datei DBHistory.txt
	 * @param str - Pfad zur DB
	 */
	public void saveToHistory(String str){
		File dbPaths = new File("DBHistory.txt");
		if(dbPaths.canRead()){
			try {
				BufferedReader in = new BufferedReader(new FileReader(dbPaths));
				while(in.ready()){
					if(in.readLine().equals(str)){
						in.close();
						return;
					}
				}
				in.close();
				BufferedWriter out = new BufferedWriter(new FileWriter(dbPaths, true));
				out.write(str);
				out.newLine();
				out.flush();
				out.close();
				System.out.println(str);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * erstellt ein Objekt von DBChooser() 
	 * und beginnt somit das Programm durch Konstruktoraufruf von DBChooser()
	 * @param args
	 */
	public static void main(String[] args){
		new DBChooser();
	}
}
