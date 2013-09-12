package dbServices;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import calendar.DateFunctions;

import functions.WpManager;

import wpConflict.Conflict;

import jdbcConnection.SQLExecuter;

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
 * Diese Klasse dient als Verbindung zur Datenbank fuer das Speicher und Loeschen von Konflikten.<br/>
 * 
 * @author Jens Eckes, Sven Seckler
 * @version 2.0 - 2012-08-20
 */
public class ConflictService {

	/**s
	 * Liefert ein Set mit allen Konflikten des Projekts
	 * 
	 * @return Set<Conflict> alle Konflikte
	 */
	public static Set<Conflict> getAllConflicts() {
		try {
			String query = "SELECT * FROM Konflikte";
			return fillConflicts(new HashSet<Conflict>(), SQLExecuter.executeQuery(query));
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Speichert einen aufgetreten Konflikt
	 * 
	 * @param conflict Conflict der zu speichernde Konflikt
	 * @throws SQLException
	 */
	public static boolean setConflict(Conflict conflict) {
		String trigger = conflict.getTriggerAp();
		if (trigger == null || (trigger != null && trigger.equals(""))) {
			trigger = WpManager.getRootAp().getStringID();
		}
		String query = "INSERT INTO Konflikte (FID_AP, FID_Betroffen, FID_Ma, Grund, Datum) VALUES ( " + "'" + trigger + "' , " + ""
				+ (conflict.getAffectedAP() == null ? "Null" : "'" + conflict.getAffectedAP() + "'") + ", " + // FID_Betroffen
				"'" + conflict.getUserId() + "', " + "" + conflict.getReason() + ", " + "" + DateFunctions.getDateString(conflict.getDate()) + " )";
		try {
			SQLExecuter.executeUpdate(query);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * Loescht einen behobenen Konflikt
	 * 
	 * @param conflict Conflict der zu loeschende Konflikt
	 * @throws SQLException
	 */
	public static void deleteConflict(Conflict conflict) throws SQLException {

		String query = "DELETE FROM Konflikte WHERE FID_AP = '" + conflict.getTriggerAp() + "'" + " AND FID_MA = '" + conflict.getUserId() + "' AND Grund = "
				+ conflict.getReason() + " AND Datum = " + DateFunctions.getDateString(conflict.getDate())
				+ (conflict.getAffectedAP() == null ? "" : " AND FID_Betroffen = '" + conflict.getAffectedAP() + "'") + "";
		SQLExecuter.executeUpdate(query);
	}

	/**
	 * Loescht alle Konflikte
	 * 
	 * @throws SQLException
	 */
	public static void deleteAll() throws SQLException {
		SQLExecuter.executeUpdate("DELETE * FROM Konflikte");
	}

	/**
	 * Private Methode zum fuellen eines Sets von Conflicts mit den Daten eines ResultSets
	 * 
	 * @param conSet das zu fuellende Set vom Typ Conflict
	 * @param resSet das ResultSet mit den Daten
	 * @return das gefuellte Set
	 * @throws SQLException
	 */
	private static Set<Conflict> fillConflicts(Set<Conflict> conSet, ResultSet resSet) throws SQLException {
		while (resSet.next()) {
			conSet.add(new Conflict(resSet.getTimestamp("Datum"), resSet.getInt("Grund"), resSet.getString("FID_Ma"), resSet.getString("FID_AP"), resSet
					.getString(("FID_Betroffen"))));
		}
		resSet.getStatement().close();
		return conSet;
	}

}
