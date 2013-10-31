package wpWorker;

/**
 * Studienprojekt: WBS
 * 
 * Kunde: Pentasys AG, Jens von Gersdorff Projektmitglieder: Andre Paffenholz, Peter Lange, Daniel Metzler, Samson von Graevenitz
 * 
 * 
 * Studienprojekt: PSYS WBS 2.0
 * 
 * Kunde: Pentasys AG, Jens von Gersdorff Projektmitglieder: Michael Anstatt, Marc-Eric Baumgärtner, Jens Eckes, Sven Seckler, Lin Yang
 * 
 * @author Daniel Metzler / Michael Anstatt
 * @version 2.0 - 18.07.2012
 */

public class Worker {

	private String login;
	private String vorname;
	private String name;
	private int berechtigung;
	private double tagessatz;
	private boolean leiter;

	/**
	 * Default-Konstruktor für einen Mitarbeiter
	 * 
	 * @param Login Login-Name
	 * @param Name Name des Mitarbeiters
	 * @param Berechtigung Berechtigung auf der DB: 0 = Mitarbeiter, 1 = Projektleiter
	 * @param Passwort Passwort des Mitarbeiters
	 * @param Tagessatz Tagessatz des Mitarbeiters
	 */
	public Worker(String login, String vorname, String name, int berechtigung, double tagessatz) {
		this.login = login;
		this.vorname = vorname;
		this.name = name;
		this.berechtigung = berechtigung;
		this.tagessatz = tagessatz;
		if (berechtigung == 0) {
			leiter = false;
		} else {
			leiter = true;
		}
	}

	// public Mitarbeiter(String login, String vorname, String name, boolean leiter, String passwort, double tagessatz) {
	// this.login = login;
	// this.vorname = vorname;
	// this.name = name;
	// this.passwort = passwort;
	// this.tagessatz = tagessatz;
	// this.leiter = leiter;
	// if(leiter) {
	// this.berechtigung = 1;
	// } else {
	// this.berechtigung = 0;
	// }
	// }
	//
	public Worker(String login) {
		this.login = login;
		this.vorname = "";
		this.name = "";
		this.berechtigung = 0;
		this.tagessatz = 0;
	}

	//
	public Worker(String login, boolean leiter) {
		this.login = login;
		this.leiter = leiter;
		this.name = "";
		this.vorname = "";
		if (leiter) {
			this.berechtigung = 1;
		} else {
			this.berechtigung = 0;
		}
	}

	/**
	 * @return Login-Name
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * @return Name des Mitarbeiters
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return VOrname des Mitarbeiters
	 */
	public String getVorname() {
		return vorname;
	}

	/**
	 * @return Berechtigung des Mitarbeiters (0: Mitarbeiter / 1: Projektleiter)
	 */
	public int getBerechtigung() {
		return berechtigung;
	}

	public boolean getProjLeiter() {
		return leiter;
	}

	/**
	 * @return Tagessatz des Mitarbeiters
	 */
	public Double getTagessatz() {
		return tagessatz;
	}

	/**
	 * @see {@link Object#toString()}
	 */
	public String toString() {
		if (login.equals("")) {
			return "";
		} else if (vorname.equals("") && name.equals("")) {
			return login;
		} else if (!vorname.equals("") && name.equals("")) {
			return login + " | " + name;
		} else {
			return login + " | " + vorname + " " + name;
		}
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	public void setBerechtigung(int rights) {
		this.berechtigung = rights;
	}

	public void setTagessatz(double tagessatz) {
		this.tagessatz = tagessatz;
	}

	public boolean equals(Object o) {
		if (o instanceof Worker) {
			Worker other = (Worker) o;
			return other.getLogin().equals(this.getLogin());
		} else {
			return false;
		}
	}

	public int hashCode() {
		return this.getLogin().hashCode();
	}

}
