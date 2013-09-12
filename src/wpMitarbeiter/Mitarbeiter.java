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
 *  Klasse zum erstellen eines Mitarbeiterobjektes
 * 
 * @author Daniel Metzler
 * @version 1.9 - 17.02.2011
 */

public class Mitarbeiter {
	
	private String Login;
	private String vorname;
	private String Name;
	private int Berechtigung;
	private String Passwort;
	private double Tagessatz;

	
	
	/**
	 * Default-Konstruktor für einen Mitarbeiter
	 * @param Login Login-Name
	 * @param Name Name des Mitarbeiters
	 * @param Berechtigung Berechtigung auf der DB: 0 = Mitarbeiter, 1 = Projektleiter
	 * @param Passwort Passwort des Mitarbeiters 
	 * @param Tagessatz Tagessatz des Mitarbeiters
	 */
	public Mitarbeiter(String Login, String Vorname, String Name, int Berechtigung, String Passwort, double Tagessatz){						
				this.Login=Login;
				this.vorname = Vorname;
				this.Name=Name;
				this.Berechtigung=Berechtigung;
				this.Passwort=Passwort;
				this.Tagessatz = Tagessatz;		
	}	
	
	
	/**
	 * Login-Getter
	 * @return Login-Name
	 */
	public String getLogin() {
		return Login;
	}

	
	
	/**
	 * Name-Getter
	 * @return Name Name des Mitarbeiters
	 */
	public String getName() {
		return Name;
	}
	
	public String getVorname(){
		return vorname;
	}

	
	/**
	 * Berechtigung-Getter
	 * @return Berechtigung
	 */
	public int getBerechtigung() {
		return Berechtigung;
	}


	
	/**
	 * Tagessatz-Getter
	 * @return Tagessatz
	 */
	public Double getTagessatz() {
		return Tagessatz;
	}

}
