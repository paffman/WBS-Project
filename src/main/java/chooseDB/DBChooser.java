package chooseDB;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

import wpOverview.WPOverview;
import wpWorker.User;
import functions.WpManager;
import globals.Loader;
import jdbcConnection.MySqlConnect;
import jdbcConnection.SQLExecuter;

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
	private String lastDbHost = null, lastDbName = null, lastDbIndexPw = null,
			lastDbUser = null;

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
		loadLastDB();
		gui = new DBChooserGUI(this);
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

		// get input from gui
		String host = gui.getHostField().getText();
		String db = gui.getDbNameField().getText();
		String user = gui.getUserField().getText();
		String indexDbPw = new String(gui.getDbPwPasswordField().getPassword());
		String userPw = new String(gui.getPwPasswordField().getPassword());
		Boolean pl = gui.getPlCheckBox().isSelected();

		// check input
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

		// get index of database
		String dbIndex = getDatabaseIndex(host, db, indexDbPw);
		if (dbIndex == null) {
			return;
		}

		// test connection
		MySqlConnect.setDbConncetion(host, db, dbIndex, dbIndex + "_" + user,
				userPw);
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

		// save database as last accessed db
		saveLastDB(host, db, user, indexDbPw);

		// %% get userData
		User userData = null;
		
		// %% Check project-leader
		if ( pl ){
			
			// %% Check Semaphore if project-leader
		}		

		// start WBS-Tool
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
		MySqlConnect.setDbConncetion(host, "id_wbs", "", "idxUser", indexDbPw);
		try {
			ResultSet rslt = SQLExecuter
					.executeQuery("call db_identifier_select_by_dbname('" + db
							+ "');");
			rslt.next();
			return rslt.getString("id");
		} catch (SQLException e) {
			JOptionPane
					.showMessageDialog(
							gui,
							"Verbindung konnte nicht aufgebaut werden! Es wurde kein Index-Eintrag f�r die Datenbank gefunden.");
			return null;
		}
	}

	private boolean tryConnection() throws Exception {
		try {
			// direct use and not use through SQLExecuter to circumvent
			// Exception Handling
			Connection c = MySqlConnect.getConnection();
			Statement stmt = c.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			stmt.executeQuery("call project_select()");
			return true;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * saveLastDB: writes the login data, except the user password, into a file,
	 * which is loaded on the next startup.
	 * 
	 * @param host
	 *            host of the database.
	 * @param db
	 *            name of the database.
	 * @param user
	 *            user of the database, without database index pr�fix.
	 * @param indexPw
	 *            password for the index database.
	 */
	private void saveLastDB(final String host, final String db,
			final String user, final String indexPw) {
		File dbConfig = new File("DbConfig.txt");
		try {
			PrintWriter out = new PrintWriter(dbConfig);
			out.println(host);
			out.println(db);
			out.println(indexPw);
			out.println(user);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * loadLastDB(): loads the data of the last used db into the data elements
	 * of this class.
	 */
	private void loadLastDB() {
		File dbConfig = new File("DbConfig.txt");
		if (dbConfig.canRead()) {
			try {
				BufferedReader in = new BufferedReader(new FileReader(dbConfig));
				this.lastDbHost = in.readLine();
				this.lastDbName = in.readLine();
				this.lastDbIndexPw = in.readLine();
				this.lastDbUser = in.readLine();
				in.close();
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

	/**
	 * @return the lastDbHost
	 */
	public final String getLastDbHost() {
		return lastDbHost;
	}

	/**
	 * @return the lastDbName
	 */
	public final String getLastDbName() {
		return lastDbName;
	}

	/**
	 * @return the lastDbIndexPw
	 */
	public final String getLastDbIndexPw() {
		return lastDbIndexPw;
	}

	/**
	 * @return the lastDbUser
	 */
	public final String getLastDbUser() {
		return lastDbUser;
	}
}
