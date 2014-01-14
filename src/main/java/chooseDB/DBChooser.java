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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;

import wpOverview.WPOverview;
import wpWorker.User;
import functions.WpManager;
import globals.Controller;
import globals.Loader;
import globals.Workpackage;
import jdbcConnection.MySqlConnect;
import jdbcConnection.SQLExecuter;
import login.Login;

/**
 * Studienprojekt: WBS
 * 
 * Kunde: Pentasys AG, Jens von Gersdorff Projektmitglieder: Andre Paffenholz,
 * Peter Lange, Daniel Metzler, Samson von Graevenitz
 * 
 * Studienprojekt: PSYS WBS 2.0<br/>
 * 
 * Kunde: Pentasys AG, Jens von Gersdorff<br/>
 * Projektmitglieder: <br/>
 * Michael Anstatt,<br/>
 * Marc-Eric Baumgärtner,<br/>
 * Jens Eckes,<br/>
 * Sven Seckler,<br/>
 * Lin Yang<br/>
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
	// private boolean cancel = false;
	/**
	 * Bedeutung: Variable zum Auslesen der Ebenen aus dem Textfeld
	 */
	// private String ebenen = "";

	/**
	 * Konstruktor DBChooser() initialisiert die DBChooserGUI, und beeinhaltet
	 * die Listener der DBChooserGUI durch die Methode addButtonAction()
	 */
	public DBChooser() {
		gui = new DBChooserGUI();
		new DBChooserButtonAction(this);

	}

	/**
	 * next() wird durch das Betätigen des "weiter" Buttons ausgeführt prüft
	 * ob das Textfeld für den Pfad ausgefüllt ist. Und stellt bei
	 * erfolgreicher Ausfüllung des Textfeldes eine Verbindung zur MDB her per
	 * MDBConnect Klasse her und setzt den aktuell eingegeben Pfad. In der
	 * Methode dbInitial() wird geprüft, ob alle notwendigen Daten in der DB
	 * vorhanden sind, andernfalls werden diese angelegt. Danach wird der Login
	 * gestartet.
	 */
	public void next() {
		// cancel = false;
		// %%
		String host = gui.hostField.getText();
		String db = gui.dbNameField.getText();
		String user = gui.userField.getText();
		String indexDbPw = new String(gui.dbPwPasswordField.getPassword());
		String userPw = new String(gui.pwPasswordField.getPassword());
		String dbIndex = getDatabaseIndex(host, db, indexDbPw);
		Boolean pl = gui.plCheckBox.isSelected();

		if (host.equals("")) {
			JOptionPane.showMessageDialog(gui, "Bitte einen Host eintragen!");
			return;
		}
		if (db.equals("")) {
			JOptionPane.showMessageDialog(gui,
					"Bitte einen Datenbanknamen eintragen!");
			return;
		}
		if (user.equals("")) {
			JOptionPane.showMessageDialog(gui,
					"Bitte einen Benutzer eintragen!");
			return;
		}
		MySqlConnect.setDbConncetion(host, db, dbIndex + "_" + user, userPw);

		try {
			if (!tryConnection()) {
				JOptionPane.showMessageDialog(gui,
						"Verbindung konnte nicht aufgebaut werden!");
				return;
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(
					gui,
					"Verbindung konnte nicht aufgebaut werden! Exception: "
							+ e.toString());
			return;
		}

		// %% Check project-leader
		User userData = null;

		// %% Check Semaphore if project-leader

		// Start WBS-Tool
		final User threadUser = userData;
		Thread loader = new Thread() {
			public void run() {
				Loader splashScreen = new Loader(gui);
				WpManager.loadDB();
				new WPOverview(threadUser, gui);
				splashScreen.dispose();
			}
		};
		loader.start();
		gui.dispose();
	}

	private String getDatabaseIndex(String host, String db, String indexDbPw) {
		// %%
		return "002";
	}

	private boolean tryConnection() throws Exception {
		try {
			// direct use and not use through SQLExecuter to circumvent
			// Exception Handling
			Connection c = MySqlConnect.getConnection();
			Statement stmt = c.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			stmt.executeQuery("call project_select()");
			return true;		
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * @deprecated -- muss neu gemacht werden f�r neue Funktionen speichert noch
	 *             nicht bekannte MDB-Pfade in die Datei DBHistory.txt
	 * 
	 * @param str
	 *            - Pfad zur DB
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
				BufferedWriter out = new BufferedWriter(new FileWriter(dbPaths,
						true));
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
	 * erstellt ein Objekt von DBChooser() und beginnt somit das Programm durch
	 * Konstruktoraufruf von DBChooser()
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new DBChooser();
	}
}
