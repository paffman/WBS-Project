package chooseDB;

import java.util.ArrayList;
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

import functions.WpManager;
import globals.Controller;
import globals.Workpackage;


import jdbcConnection.MDBConnect;
import jdbcConnection.SQLExecuter;
import login.Login;

/**
 * Studienprojekt:	WBS
 * 
 * Kunde:				Pentasys AG, Jens von Gersdorff
 * Projektmitglieder:	Andre Paffenholz, 
 * 						Peter Lange, 
 * 						Daniel Metzler,
 * 						Samson von Graevenitz
 *
 * Studienprojekt:	PSYS WBS 2.0<br/>
 * 
 * Kunde:		Pentasys AG, Jens von Gersdorff<br/>
 * Projektmitglieder:	<br/>
 *			Michael Anstatt,<br/>
 *			Marc-Eric Baumgärtner,<br/>
 *			Jens Eckes,<br/>
 *			Sven Seckler,<br/>
 *			Lin Yang<br/>
 *
 * Ruft die DBChooserGUI auf<br/>
 * setzt nach der Pfadeingabe den Pfad in der MDBConnect Klasse<br/>
 * 
 * 
 * @author Samson von Graevenitz, Daniel Metzler, Michael Anstatt
 * @version 2.0 - 2012-08-20
 */
public class DBChooser {

	/**
	 * übergibt ein Object von der DBChooserGUI
	 */
	public DBChooserGUI gui;
	/**
	 * Bedeutung: wurde der Cancel Button im Projektebenen Fenster gedrückt?
	 */
	private boolean cancel = false;
	/**
	 * Bedeutung: Variable zum Auslesen der Ebenen aus dem Textfeld
	 */
	private String ebenen = "";

	/**
	 * Konstruktor DBChooser() initialisiert die DBChooserGUI, und beeinhaltet die Listener der DBChooserGUI durch die Methode addButtonAction()
	 */
	public DBChooser() {
		gui = new DBChooserGUI();
		new DBChooserButtonAction(this);

	}

	/**
	 * next() wird durch das Betätigen des "weiter" Buttons ausgeführt prüft ob das Textfeld für den Pfad ausgefüllt ist. Und stellt bei erfolgreicher
	 * Ausfüllung des Textfeldes eine Verbindung zur MDB her per MDBConnect Klasse her und setzt den aktuell eingegeben Pfad. In der Methode dbInitial() wird
	 * geprüft, ob alle notwendigen Daten in der DB vorhanden sind, andernfalls werden diese angelegt. Danach wird der Login gestartet.
	 */
	public void next() {
		cancel = false;
		if (gui.cobDBAuswahl.getSelectedItem().equals("")) {
			JOptionPane.showMessageDialog(gui, "Bitte geben Sie einen Pfad an!");
		} else if (!gui.cobDBAuswahl.getSelectedItem().equals("") && !gui.cobDBAuswahl.getSelectedItem().toString().endsWith(".mdb")) {
			JOptionPane.showMessageDialog(gui, "Bitte nur *.mdb Dateien verwenden!");
		} else {
			saveToHistory(gui.cobDBAuswahl.getSelectedItem().toString());
			MDBConnect.setPathDB(gui.cobDBAuswahl.getSelectedItem().toString());
			dbInitial();
			if (!cancel) {
				gui.dispose();
				new Login();
			}
		}

	}

	/**
	 * Kopiert die leere Datenbank und gibt ihr den Namen welcher als Projekt-Name angegeben wurde, zusätlich werden die notwendigen Daten in die Datenbank
	 * kopiert, z. B.: Projekt: Das Projekt Default-Mitarbeiter: Leiter (PW: 1234, Projektleiterberechtigung) Default-AP: ID 0.0.0.x
	 */
	public void dbInitial() {
		int myEbenen = 0;
		String projname = "";
		Date projectStart = null;

		ResultSet rsProjekt = SQLExecuter.executeQuery("SELECT * FROM Projekt");
		try {
			if (!rsProjekt.next()) {
				boolean gueltig = false;
				while (!gueltig) {
					projname = JOptionPane.showInputDialog(null, "Geben Sie den Projektnamen an.", "Projektname", JOptionPane.PLAIN_MESSAGE);
					ebenen = JOptionPane.showInputDialog(null, "Geben Sie die Maximale Ebenenanzahl an.", "Anzahl Projektebenen", JOptionPane.PLAIN_MESSAGE);

					while (projectStart == null) {
						try {
							projectStart = Controller.DATE_DAY.parse(JOptionPane.showInputDialog(null,
									"Geben Sie das geplante Projektstartdatum in der form dd.mm.yyyy an.", "Startdatum", JOptionPane.PLAIN_MESSAGE));
						} catch (Exception e) {
							JOptionPane.showMessageDialog(null, "Falsches Datumsformat", "Bitte geben Sie das Startdatum in der Form dd.mm.yyyy an!", JOptionPane.ERROR_MESSAGE);
						}
					}

					// Kein leerer Projektname erlaubt, wenn leer oder nach Cancel wird Programm geschlossen
					if (projname == null || projname.equals("")) {
						System.out.println("Durch Benutzer abgebrochen");
						System.exit(0);
					} else {
						if (ebenen != null) {
							try {
								int anz = Integer.valueOf(ebenen).intValue();
								if (anz > 3) {
									gueltig = true;
								} else {
									JOptionPane.showMessageDialog(null, "Mindestens 4 Ebenen", "Es müssen mindestens 4 Ebenen angelegt werden.",
											JOptionPane.ERROR_MESSAGE);
								}
							} catch (NumberFormatException e) {
								JOptionPane.showMessageDialog(null, "Bitte geben sie eine gültige Zahl an!", "Anzahl Projektebenen", JOptionPane.ERROR_MESSAGE);
							}
						} else {
							gueltig = true;
							cancel = true;
						}
					}
				}
			}
			rsProjekt.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		if (ebenen != null && projname != null && !projname.equals("")) {
			// Kopiert die leere MDB und ändern den Namen
			copyAndRename(MDBConnect.getPathDB(), projname);
			ResultSet rsMitarbeiter = SQLExecuter.executeQuery("SELECT * FROM Mitarbeiter");
			try {
				if (!rsMitarbeiter.next()) {
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
			}

			rsProjekt = SQLExecuter.executeQuery("SELECT * FROM Projekt");
			try {
				if (!rsProjekt.next()) {

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
			}

			String nullen = "0";
				for (int i = 4; i < myEbenen; i++) {
					nullen += ".0";
				}
				WpManager.loadDB();
			if(WpManager.getAllAp().size() == 0) {					
				Workpackage rootAp = new Workpackage(0, 0, 0, nullen, projname, "Projekt", 1.0, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, null, true, false, "Leiter", new ArrayList<String>(), null, projectStart, null);
				WpManager.insertAP(rootAp);
			}
			try {
					ResultSet rsAPZuweisung = SQLExecuter.executeQuery("SELECT * FROM Paketzuweisung");
					rsAPZuweisung.moveToInsertRow();
					rsAPZuweisung.updateInt("FID_Proj", 1);
					rsAPZuweisung.updateInt("FID_LVL1ID", 0);
					rsAPZuweisung.updateInt("FID_LVL2ID", 0);
					rsAPZuweisung.updateInt("FID_LVL3ID", 0);
					rsAPZuweisung.updateString("FID_LVLxID", nullen);
					rsAPZuweisung.updateString("FID_Ma", "Leiter");
					rsAPZuweisung.insertRow();
					rsAPZuweisung.close();
					System.out.println("AP wurde angelegt");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * void open() wird durch das Betätigen des "öffnen" Buttons ausgeführt erstellt einen JFileChooser in der der Benutzer eine MDB-Datei auf seinem
	 * Filesystem auswählen kann diese schreibt wird dann in das Textfeld für die Pfadangabe geschrieben
	 */
	public void open() {
		gui.fileIn = new JFileChooser(".");
		gui.fileIn.setFileFilter(new FileFilter() {

			public String getDescription() {
				return "MDB's";
			}

			public boolean accept(File f) {
				if (f.isDirectory()) return true;
				return f.getName().toLowerCase().endsWith(".mdb");
			}
		});
		gui.fileIn.setMultiSelectionEnabled(false);
		if (gui.fileIn.showOpenDialog(gui) == JFileChooser.APPROVE_OPTION) {
			String file = gui.fileIn.getSelectedFile().toString();
			gui.cobDBAuswahl.setSelectedItem(file);
		}
	}

	/**
	 * Kopiert die leere MDB und ändern deren Namen in den angegebenen Projektnamen wenn bereits eine Datei mit dem Namen besteht, wird fortlaufend in Klammern
	 * die Anzahl der gleichnamigen Dateien hochgezählt
	 * 
	 * @param dbPath - Pfad zur MDB-Datenbank
	 * @param projname - Projektname
	 */
	public void copyAndRename(String dbPath, String projname) {
		String infile = dbPath.subSequence(4, dbPath.length()) + "";
		String outfile = dbPath.subSequence(4, dbPath.lastIndexOf("/") + 1) + projname + ".mdb";
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
			e.printStackTrace();
		}
		JOptionPane.showMessageDialog(gui, "Es wurde eine neue DB-File angelegt\n" + outfile);
		saveToHistory(outfile.replace("/", "\\"));
		MDBConnect.setPathDB(outfile);
	}

	/**
	 * füllt die ComboBox mit Pfaden zu bereits bekannten MDBs
	 */
	public void fillCobDB() {
		File dbPaths = new File("DBHistory.txt");
		if (!dbPaths.canRead()) {
			try {
				FileWriter out = new FileWriter(dbPaths);
				File wbsEmpty = new File("WBS_leer.mdb");
				if (wbsEmpty.exists()) {
					out.write("\n" + wbsEmpty.getAbsolutePath() + "\n");
				} else {
					out.write("\n");
				}
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			BufferedReader in = new BufferedReader(new FileReader("DBHistory.txt"));
			while (in.ready()) {
				gui.cobDBAuswahl.addItem(in.readLine());
			}
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(dbPaths.canRead());
	}

	/**
	 * speichert noch nicht bekannte MDB-Pfade in die Datei DBHistory.txt
	 * 
	 * @param str - Pfad zur DB
	 */
	public void saveToHistory(String str) {
		File dbPaths = new File("DBHistory.txt");
		if (dbPaths.canRead()) {
			try {
				BufferedReader in = new BufferedReader(new FileReader(dbPaths));
				while (in.ready()) {
					if (in.readLine().equals(str)) {
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
				e.printStackTrace();
			}
		}
	}

	/**
	 * erstellt ein Objekt von DBChooser() und beginnt somit das Programm durch Konstruktoraufruf von DBChooser()
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new DBChooser();
	}
}
