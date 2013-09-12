package dbServices;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import calendar.DateFunctions;



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
 * Diese Klasse dient als Verbindung zur Datenbank fuer das Speicher und Loeschen verschiedener Werte.<br/>
 * In dies Klasse wurden Methoden des Vorgaenger Projekts ausgelagert.<br/>
 * 
 * @author Jens Eckes, Sven Seckler
 * @version 2.0 - 2012-08-15
 */
public class ValuesService {

	/**
	 * Liefert eine Map mit allen PV, des Projekts, und deren Datum fuer einen bestimmten Zeitraum.
	 * 
	 * @param from Startdatum des Zeitraums
	 * @param to Enddatum des Zeitraums
	 * @return Map<Date, Double> Datum Date und den entsprechenden PV Double fuer diese Datum
	 */
	public static Map<Date, Double> getPVs(Date from, Date to) {
		try {
			return fillPVMap(
					new HashMap<Date, Double>(),
					SQLExecuter.executeQuery("SELECT * FROM PlannedValue WHERE Datum BETWEEN " + DateFunctions.getDateString(from) + " AND "
							+ DateFunctions.getDateString(to) + " ORDER BY Datum DESC"));
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Liefert die Planned Values fuer einen bestimmten Zeitraum fuer ein bestimmtes Arbeitspaket
	 * 
	 * @param from Startdatum des Zeitraums
	 * @param to Enddatum des Zeitraums
	 * @param wpID Arbeitspaket
	 * @return Map mit Zuordnung Datum -> PV
	 */
	public static Map<Date, Double> getWPPVs(Date from, Date to, String wpID) {
		try {
			return fillPVMap(
					new HashMap<Date, Double>(),
					SQLExecuter.executeQuery("SELECT * FROM PlannedValue WHERE Datum BETWEEN " + DateFunctions.getDateString(from) + " AND "
							+ DateFunctions.getDateString(to) + " AND FID_AP = '"+wpID+"' ORDER BY Datum DESC"));
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	

	/**
	 * Uebertraegt den errechneten Planned Value an die Datenbank.
	 * 
	 * @param apID konkatenierten hierarchisch StringID eines Arbeitspaketes.
	 * @param date Datum fuer den der Planned Value erstellt wurde.
	 * @param pv Errechneter Wert fuer den Planned Value.
	 * @return Bestaetigt das erfolgreiche durchlaufen.
	 */
	public static boolean savePv(String apID, Date date, double pv) {
		try {
			String query = "INSERT INTO PlannedValue (FID_AP, Datum, PV) VALUES ('" + apID + "', " + DateFunctions.getDateStringOnlyDay(date) + ", " + pv
					+ ")";
			SQLExecuter.executeUpdate(query);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Uebertraegt den errechneten Planned Value an die Datenbank.
	 * 
	 * @param apID konkatenierten hierarchisch StringID eines Arbeitspaketes.
	 * @param date Datum fuer den der Planned Value erstellt wurde.
	 * @param pv Errechneter Wert fuer den Planned Value.
	 * @return Bestaetigt das erfolgreiche durchlaufen.
	 */
	public static boolean updatePv(String apID, Date date, double pv) {
		try {
			String query = "UPDATE PlannedValue SET PV = "+pv+" WHERE Datum = " + DateFunctions.getDateStringOnlyDay(date) + " AND FID_AP = '" + apID + "'";;
			SQLExecuter.executeUpdate(query);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Liefert den PV fuer ein bestimmtes Arbeitsparket
	 * 
	 * @param apID ID des Arbeitsparkets
	 * @return double PV des Arbeitsparketes
	 */
	public static double getApPv(String apID) {
		Calendar calendar = getNextFriday(new Date(System.currentTimeMillis()));
		String query = "SELECT PV FROM PlannedValue WHERE Datum = " + DateFunctions.getDateStringOnlyDay(calendar.getTime()) + " AND FID_AP = '" + apID + "'";
		ResultSet resSet = SQLExecuter.executeQuery(query);
		ResultSet resSet2 = null;
		double ergPv = 0.0;
		try {
			if (resSet.next()) {
				ergPv = resSet.getDouble("PV");
			} else {
				String query2 = "SELECT PV FROM PlannedValue WHERE Datum < " + DateFunctions.getDateStringOnlyDay(calendar.getTime()) + " AND FID_AP = '"
						+ apID + "' ORDER BY Datum DESC";
				resSet2 = SQLExecuter.executeQuery(query2);
				if (resSet2.next()) {
					ergPv = resSet2.getDouble("PV");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				resSet.getStatement().close();
				if (resSet2 != null) {
					resSet2.close();
				}
			} catch (SQLException e) {
			}
		}

		return ergPv;
	}

	/**
	 * Liefert den PV fuer ein bestimmtes Arbeitsparket zu einem bestimmten Datum
	 * 
	 * @param apID ID des Arbeitsparkets
	 * @param apID AP-ID
	 * @param date Tag
	 * @return double PV des AP
	 */
	public static double getApPv(String apID, Calendar day) {
		day = getNextFriday(day.getTime());
		String query = "SELECT PV FROM PlannedValue WHERE Datum = " + DateFunctions.getDateStringOnlyDay(day.getTime()) + " AND FID_AP = '" + apID + "'";
		ResultSet resSet = SQLExecuter.executeQuery(query);
		ResultSet resSet2 = null;
		double ergPv = 0.0;
		try {
			if (resSet.next()) {
				ergPv = resSet.getDouble("PV");
			} else {
				String query2 = "SELECT PV FROM PlannedValue WHERE Datum < " + DateFunctions.getDateStringOnlyDay(day.getTime()) + " AND FID_AP = '"
						+ apID + "' ORDER BY Datum DESC";
				resSet2 = SQLExecuter.executeQuery(query2);
				if (resSet2.next()) {
					ergPv = resSet2.getDouble("PV");
				} else {
					ergPv = -1;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				resSet.getStatement().close();
				if (resSet2 != null) {
					resSet2.getStatement().close();
				}
			} catch (SQLException e) {
			}
		}

		return ergPv;
	}
	
	/**
	 * Prueft ob zu diesem Zeitpunkt schon ein Planned Value fuer dieses Arbeitspaket in der Datenbank eingetragen ist
	 * @param apID ID eines Arbeitspakets
	 * @param day Kalender mit Tag zu dem der PV gewuenscht wird
	 * @return
	 */
	public static boolean pvExists(String apID, Calendar day) {
		day = getNextFriday(day.getTime());
		String query = "SELECT PV FROM PlannedValue WHERE Datum = " + DateFunctions.getDateStringOnlyDay(day.getTime()) + " AND FID_AP = '" + apID + "'";
		ResultSet resSet = SQLExecuter.executeQuery(query);
		boolean exist = false;
		try {
			exist = resSet.next();
			resSet.getStatement().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return exist;
	}

	/**
	 * Loescht alle PlannedValues
	 * 
	 * @return true wenn alle PV erfolgreich geloescht wurde sonst false
	 */
	public static boolean deleteAllPV() {
		String query = "DELETE * FROM PlannedValue";
		try {
			SQLExecuter.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Loescht ein bestimmtes PlannedValue fuer ein Arbeitspaket
	 * 
	 * @param apID String stringID des Arbeitspakets
	 * @return true wenn das PV erfolgreich geloescht wurde sonst false
	 */
	public static boolean deletePV(String apID) {
		String query = "DELETE * FROM PlannedValue WHERE FID_AP = '" + apID + "'";
		try {
			SQLExecuter.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	/**
	 * Loescht ein bestimmtes PlannedValue fuer ein Arbeitspaket und ein bestimmtes Datum
	 * 
	 * @param apID String stringID des Arbeitspakets
	 * @param date Date Datum fuer welches der PV geloescht werden soll
	 * @return true wenn das PV erfolgreich geloescht wurde sonst false
	 */
	public static boolean deletePV(String apID, Date date) {
		String query = "DELETE * FROM PlannedValue WHERE Datum = " + DateFunctions.getDateStringOnlyDay(date) + " AND FID_AP = '" + apID + "'";
		try {
			SQLExecuter.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Diese Methode prueft ein Datum ob es ein Freitag ist<br/>
	 * wenn ja wir eine Calendar instance mit dem Datum zurueck gegeben<br/>
	 * wenn nein wird das Datum auf den naechsten Freitag gesetzt und eine Calendar instance mit dem Datum zurueck gegeben<br/>
	 * 
	 * @param date Date wenn null wir der naechste Freitag zurueck geliefert sont wir das Datum getestet ob es sich um einen Freitag handelt
	 * @return Calendar mit dem Datum des naechsten Freitags
	 */
	public static Calendar getNextFriday(Date date) {
		Calendar calendar = Calendar.getInstance();
		if (date != null) {
			calendar.setTime(date);
		}
		while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.FRIDAY) {
			calendar.add(Calendar.DATE, 1);
		}
		
		return calendar;
	}
	
	/**
	 * Diese Methode prueft ein Datum ob es ein Freitag ist<br/>
	 * wenn ja wir eine Calendar instance mit dem Datum zurueck gegeben<br/>
	 * wenn nein wird das Datum auf den naechsten Freitag gesetzt und eine Calendar instance mit dem Datum zurueck gegeben<br/>
	 * 
	 * @param date Date wenn null wir der vorherigen Freitag zurueck geliefert sont wir das Datum getestet ob es sich um einen Freitag handelt
	 * @return Calendar mit dem Datum des vorherigen Freitags
	 */
	public static Calendar getPreviousFriday(Date date) {
		Calendar calendar = Calendar.getInstance();
		if (date != null) {
			calendar.setTime(date);
		}
		while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.FRIDAY) {
			calendar.add(Calendar.DATE, -1);
		}
		return calendar;
	}

	/**
	 * 
	 * @param ergMap
	 * @param resSet
	 * @return
	 * @throws SQLException
	 */
	private static Map<Date, Double> fillPVMap(Map<Date, Double> ergMap, ResultSet resSet) throws SQLException {
		while (resSet.next()) {
			ergMap.put(resSet.getDate("Datum"), resSet.getDouble("PV"));
		}
		
		resSet.close();
		return ergMap;
	}
}
