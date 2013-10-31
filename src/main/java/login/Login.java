package login;

import java.sql.*;

import javax.swing.JOptionPane;

import dbServices.SemaphoreService;


import exception.SemaphorException;
import functions.WpManager;
import globals.Controller;
import globals.Loader;
import wpOverview.WPOverview;
import wpWorker.User;
import jdbcConnection.SQLExecuter;

/**
 * Studienprojekt:	WBS
 * 
 * Kunde:				Pentasys AG, Jens von Gersdorff<br/>
 * Projektmitglieder:	Andre Paffenholz, <br/>
 * 						Peter Lange, <br/>
 * 						Daniel Metzler,<br/>
 * 						Samson von Graevenitz<br/>
 *
 * Studienprojekt:	PSYS WBS 2.0<br/>
 * 
 * Kunde:		Pentasys AG, Jens von Gersdorff<br/>
 * Projektmitglieder:	<br/>
 *			Michael Anstatt,<br/>
 *			Marc-Eric Baumgärtner,<br/>
 *			Jens Eckes,<br/>
 *			Sven Seckler,<br/>
 *			Lin Yang <br/>
 * 
 * Login-Fenster und überprüfung des Logins (User/Passwort)<br/>
 * 
 * @author Samson von Graevenitz,Daniel Metzler, Michael Anstatt
 * @version 2.0 2012-08-21
 */

public class Login {

	/**
	 * Variablen für die LoginGUI und den SQLExecuter
	 */
	public LoginGUI gui;

	/**
	 * Konstructor Login() initialisiert die LoginGUI und den SQLExecuter, und beeinhaltet die Listener der LoginGUI durch die Methode addButtonAction()
	 */
	public Login() {
		gui = new LoginGUI();
		new LoginButtonAction(this);
		gui.setTitle("Login für Projekt " + getProjektName());
	}

	/**
	 * Holt den Projektnamen aus der geöffneten MDB
	 * 
	 * @return Projektname - Reicht diesen als Fenster-Titel weiter
	 */
	private String getProjektName() {
		ResultSet proj = SQLExecuter.executeQuery("Select * FROM Projekt");
		String name = "";
		try {
			while (proj.next()) {
				name = proj.getString("Name");
			}
			proj.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return name;
	}

	/**
	 * void checkUser() liest den Login und das Password verbindet sich dann zu der Datenbank und überprüft den Namen des Users mit dem eingegebenen Loginnamen
	 * und Passwort. Wenn der User verfügbar und das Passwort korrekt ist, wird die eigentliche Anwendung gestartet
	 */
	protected void checkUser() {
		String login = gui.txfLogin.getText();
		// gibt das Passwort von dem JPasswordField zurück
		char[] passwort = gui.txfPassword.getPassword();

		String pass = String.valueOf(passwort);
		
		

		// Führt die SQL-Query aus um den user zu prüfen und identifizieren
		// wenn er auf der Datenbank vorhanden ist,
		// wird das ResultSet wird mit den Daten des LoginBenutzers gefüllt von der Datenbank gefüllt
		try {
			ResultSet rs = SQLExecuter.executeQuery("SELECT * FROM Mitarbeiter " + "WHERE Login = '" + login + "'" + "AND Passwort = '" + pass + "';");

			// wenn das ResultSet einen Eintrag beeinhaltet,
			// wird der Name des aktuellen users ausgegeben und ein neuer Benutzer der Klasse User angelegt
			// "Correct login: username"
			User user = null;
			String userString = null;
			boolean found = false;
			while (rs.next() && !found) {
				if ((userString = rs.getString("Login")) != null) {
					int berechtigung = rs.getInt("Berechtigung");
					String name = rs.getString("Name");
					String vorname = rs.getString("Vorname");
					if (gui.chbProjLeiter.isSelected()) {
						if (berechtigung == 1) {
							user = new User(userString, name, vorname, true);
						} else {
							JOptionPane.showMessageDialog(gui, "Keine Projektleiter-Berechtigung");
						}
					} else {
						user = new User(userString, name, vorname,false);
					}
					found = true;
				}
			}
			rs.getStatement().close();

			// Wenn der user immer noch "null" ist, ist der Login fehlgeschlagen
			if (user == null) {
				JOptionPane.showMessageDialog(gui, "Benutzername oder Passwort falsch!", null, JOptionPane.ERROR_MESSAGE);
				System.out.println("Incorrect login!");
			} else {
				// Erfolgreich angemeldet

				
				
				final User threadUser = user;
				Thread loader = new Thread() {
					public void run() {
						Loader splashScreen = new Loader(gui);
						WpManager.loadDB();
						new WPOverview(threadUser, gui);
						splashScreen.dispose();
					}
				};
				String dbUser = "";
				try {
					if(user.getProjLeiter()) {
						dbUser = SemaphoreService.acquireLeaderSemaphore(user.getLogin());
						if(dbUser.equals(user.getLogin())) {
							loader.start();
							gui.dispose();
						}
					} else {
						loader.start();
						gui.dispose();
					}
					
				} catch (SemaphorException e) {
					if (JOptionPane.showConfirmDialog(gui, e.getMessage()) == JOptionPane.YES_OPTION) {
						if(SemaphoreService.forceFreeSemaphore()){
							try {
								SemaphoreService.acquireLeaderSemaphore(user.getLogin());
							} catch (SemaphorException e1) {
								e1.printStackTrace();
							}
						}
//						SemaphoreService.releaseLeaderSemaphore(dbUser);
						gui.dispose();
						loader.start();
					}
				}
			}

		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
	}

	

	/**
	 * Datenbank-Verbindung beenden und Semaphor freigeben
	 */
	public static void exit() {
		Controller.leaveDB();
	}
}
