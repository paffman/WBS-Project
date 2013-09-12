package login;

/**
 * Studienprojekt:	WBS
 * 
 * Kunde:				Pentasys AG, Jens von Gersdorff
 * Projektmitglieder:	Andre Paffenholz, 
 * 						Peter Lange, 
 * 						Daniel Metzler,
 * 						Samson von Graevenitz
 * 
 * Login-Fenster und überprüfung des Logins (User/Passwort)
 * 
 * @author Samson von Graevenitz und Daniel Metzler
 * @version 0.1 - 30.11.2010
 */

import java.sql.*;

import javax.swing.JOptionPane;
import wpOverview.WPOverview;
import jdbcConnection.SQLExecuter;

public class Login {

	/**
	 * Variablen für die LoginGUI und den SQLExecuter
	 */
	public LoginGUI gui;
	private Login dies;
	private SQLExecuter sqlExec;
	
	/**
	 * Konstructor Login()
	 * initialisiert die LoginGUI und den SQLExecuter,
	 * und beeinhaltet die Listener der LoginGUI durch die Methode addButtonAction()    
	 */
	public Login(){
		dies = this;
		gui = new LoginGUI();
		sqlExec = new SQLExecuter();
		new LoginButtonAction(dies);
		gui.setTitle("Login für Projekt " + getProjektName());
	}
	
	/**
	 * Holt den Projektnamen aus der geöffneten MDB
	 * @return Projektname - Reicht diesen als Fenster-Titel weiter
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
	 * void checkUser()
	 * liest den Login und das Password
	 * verbindet sich dann zu der Datenbank und überprüft den Namen des Users
	 * mit dem eingegebenen Loginnamen und Passwort.
	 * Wenn der User verfügbar und das Passwort korrekt ist, wird die eigentliche
	 * Anwendung gestartet
	 */
	protected void checkUser(){
		String login = gui.txfLogin.getText();
		//	gibt das Passwort von dem JPasswordField zurück
		char[] passwort = gui.txfPassword.getPassword();

		String pass = String.valueOf(passwort);
		
		//	Führt die SQL-Query aus um den user zu prüfen und identifizieren
		//	wenn er auf der Datenbank vorhanden ist, 
		//	wird das ResultSet wird mit den Daten des LoginBenutzers gefüllt von der Datenbank gefüllt
		try {
			ResultSet rs = sqlExec.executeQuery("SELECT * FROM Mitarbeiter " +
													"WHERE Login = '" + login +"'" +
													"AND Passwort = '" + pass + "';");
			
			// 	wenn das ResultSet einen Eintrag beeinhaltet, 
			//	wird der Name des aktuellen users ausgegeben und ein neuer Benutzer der Klasse User angelegt
			//	"Correct login: username"
			String user = null;
			while(rs.next()){
				if((user = rs.getString("Login"))!= null){
					String vorname = rs.getString("Vorname");
					String name = rs.getString("Name");
					int berechtigung = rs.getInt("Berechtigung");
					double tagessatz = rs.getDouble("Tagessatz");
					User usr;
					if(gui.chbProjLeiter.isSelected()){
						if(berechtigung == 1){
							usr = new User(vorname, name, berechtigung, user, tagessatz, true);
							new WPOverview(usr);
							gui.dispose();
						} else{
							JOptionPane.showMessageDialog(gui, "Keine Projektleiter-Berechtigung");
						}
					}
					if(!gui.chbProjLeiter.isSelected()){
						usr = new User(vorname, name, berechtigung, user, tagessatz, false);
						new WPOverview(usr);
						gui.dispose();
					}
					//System.out.println("Correct login:\n" + usr.toString());
				}
			}
			rs.close();
			sqlExec.closeConnection();

			//Wenn der user immer noch "null" ist, ist der Login fehlgeschlagen
			if(user == null){
				JOptionPane.showMessageDialog(gui, "Benutzername oder Passwort falsch!",null,JOptionPane.ERROR_MESSAGE);
				System.out.println("Incorrect login!");
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally{
			sqlExec.closeConnection();
		}
	}
	
}
