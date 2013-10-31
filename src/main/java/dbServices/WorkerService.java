package dbServices;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;



import jdbcConnection.SQLExecuter;

import wpWorker.Worker;

/**
 * Studienprojekt: PSYS WBS 2.0<br/>
 * 
 * Kunde: Pentasys AG, Jens von Gersdorff<br/>
 * Projektmitglieder:<br/>
 * Michael Anstatt,<br/>
 * Marc-Eric Baumgärtner,<br/>
 * Jens Eckes,<br/>
 * Sven Seckler,<br/>
 * Lin Yang<br/>
 * 
 * Diese Klasse bietet Datenbank-Zugriffe fuer Anfragen bezueglich Mitarbeitern<br/>
 * 
 * @author Michael Anstatt, Sven Seckler
 * @version 2.0 - 2012-08-21
 */
public class WorkerService {

	/**
	 * wird beim initialisieren der Datenelemente aufgerufen
	 * speichert in eine ArrayListe alle im Projekt vorhandenen Mitarbeiter
	 * @return ArrayListe mit allen Mitarbeitern
	 */
	public static ArrayList<Worker> getAllWorkers() {
		ArrayList<Worker> AlleMit = new ArrayList<Worker>();
		ResultSet mit = SQLExecuter.executeQuery("Select * FROM Mitarbeiter");
		try {
			while (mit.next()) {
				String Login = mit.getString("Login");
				String Vorname = mit.getString("Vorname");
				String Name = mit.getString("Name");
				int Berechtigung = mit.getInt("Berechtigung");
				double Tagessatz = mit.getDouble("Tagessatz");
				AlleMit.add(new Worker(Login, Vorname, Name, Berechtigung, Tagessatz));
			}
			mit.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return AlleMit;
	}

	/**
	 * Gibt alle "echten" Mitarbeiter zurueck, also ohne "Leiter"
	 * @return alle Mitarbeiter ohne "Leiter"
	 */
	public static ArrayList<Worker> getRealWorkers() {
		ArrayList<Worker> AlleMit = new ArrayList<Worker>();
		ResultSet mit = SQLExecuter.executeQuery("Select * FROM Mitarbeiter WHERE Login <> 'Leiter'");
		try {
			while (mit.next()) {
				String Login = mit.getString("Login");
				String Vorname = mit.getString("Vorname");
				String Name = mit.getString("Name");
				int Berechtigung = mit.getInt("Berechtigung");
				double Tagessatz = mit.getDouble("Tagessatz");
				AlleMit.add(new Worker(Login, Vorname, Name, Berechtigung, Tagessatz));
			}
			mit.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return AlleMit;
	}

}
