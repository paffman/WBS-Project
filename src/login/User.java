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
 * 
 * Hält Instanz eines Users
 * 
 * @author Samson von Graevenitz und Daniel Metzler
 * @version 0.1 - 30.11.2010
 */


public class User {
	private String vorname;
	private String name;
	private int berechtigung;
	private String login;
	private double tagessatz;
	private boolean projLeiter;

	/**
	 * Default-Konstruktor
	 * @param name Benutzer
	 * @param berechtigung Berechtigung auf der DB: 0=Mitarbeiter, 1=Projektleiter
	 * @param login Login-Name
	 * @param tagessatz Tagessatz des Mitarbeiters
	 * @param projLeiter ist der aktuelle Benutzer Projektleiter?
	 */
	public User(String vorname, String name, int berechtigung, String login, double tagessatz, boolean projLeiter){
		this.vorname = vorname;
		this.name = name;
		this.berechtigung = berechtigung;
		this.login = login;
		this.tagessatz = tagessatz;
		this.projLeiter = projLeiter;
	}
	
	
	/**
	 * Getter für den Namen
	 * @return Name des Mitarbeiters
	 */
	public String getName() {
		return name;
	}
	
	public String getVorname(){
		return vorname;
	}


	
	/**
	 * Getter für den Loginn
	 * @return Login-Name
	 */
	public String getLogin() {
		return login;
	}
	
	/**
	 * Getter für den Tagessatz
	 * @return Tagessatz des Mitarbeiters
	 */
	public double gettagessatz(){
		return tagessatz;
	}
	
	
	/**
	 * Getter - ist der Benutzer Projektleiter?
	 * @return true = Projektleiter, false = kein Projektleiter
	 */
	public boolean getProjLeiter(){
		return projLeiter;
	}
	
	
	/**
	 * Gibt alle Daten des Benutzers als String zurück
	 */
	public String toString(){
		return "Name: " + name + "\nBerechtigung: " + berechtigung + "\nLogin: " + login + "\nProjektleiter: " + projLeiter;
	}
}
