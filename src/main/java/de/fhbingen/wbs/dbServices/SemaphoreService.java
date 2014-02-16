package de.fhbingen.wbs.dbServices;

import java.sql.ResultSet;
import java.sql.SQLException;

import de.fhbingen.wbs.jdbcConnection.SQLExecuter;
import de.fhbingen.wbs.exception.SemaphorException;

/**
 * Studienprojekt: PSYS WBS 2.0<br/>
 * Kunde: Pentasys AG, Jens von Gersdorff<br/>
 * Projektmitglieder:<br/>
 * Michael Anstatt,<br/>
 * Marc-Eric Baumgärtner,<br/>
 * Jens Eckes,<br/>
 * Sven Seckler,<br/>
 * Lin Yang<br/>
 * Diese Klasse dient als Semaphor damit immer nur ein Projekt-Leiter Zugriff
 * auf die Datenbank hat.<br/>
 * Dies schuetzt die Konsistenz der Datenbank.<br/>
 *
 * @author Jens Eckes, Sven Seckler
 * @version 2.0 - 2012-08-15
 *
 * @deprecated kann gel�scht werden, funktionen erfolgen direkt �ber das SemaphoreModel
 */
public class SemaphoreService {

    /**
     * Regelt den Zugriff von Projekt-Leitern auf die Datenbank.
     *
     * @param newLeaderID
     *            ist der Login eines Leiters der Zugriff haben moechte.
     * @return Gibt bei Erfolg den eigene Login zurueck.
     * @throws SemaphorException
     *             bei schon belegten Semaphor wird der Login des Besetzters aus
     *             gegeben.
     */
    public static String acquireLeaderSemaphore(String newLeaderID)
            throws SemaphorException {
        ResultSet resSet = null;
        try {
            resSet =
                    SQLExecuter
                            .executeQuery("SELECT FID_Leiter FROM Leitersemaphor");
            if (resSet.next()) {
                throw new SemaphorException(
                        "Der Semaphor ist von "
                                + resSet.getString("FID_Leiter")
                                + " belegt.\n"
                                + "Das weiter forsetzen kann zu Datenbank inkonsistenz führen.");
            } else {
                SQLExecuter
                        .executeUpdate("INSERT INTO Leitersemaphor (FID_Leiter) VALUES ('"
                                + newLeaderID + "')");
                return newLeaderID;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SemaphorException(
                    "Bei dem Belegen des Semaphors ist ein Fehler aufgetreten.");
        } finally {
            try {
                resSet.close();
            } catch (SQLException e) {
            }
        }
    }

    /**
     * Gibt den Zugriff von Projekt-Leitern auf die Datenbank wieder frei.
     *
     * @param newLeaderID
     *            ist der Login eines Leiters der Zugriff haben moechte.
     * @return Bestaetigt das erfolgreiche durchlaufen.
     */
    public static boolean releaseLeaderSemaphore(String newLeaderID) {
        ResultSet resSet = null;
        try {
            String query = "SELECT FID_Leiter FROM Leitersemaphor";
            resSet = SQLExecuter.executeQuery(query);
            if (resSet.next()) {
                String oldLeaderID = resSet.getString("FID_Leiter");
                if (oldLeaderID.equals(newLeaderID)) { // Nur der sperrende
                                                       // Benutzer darf
                                                       // freigeben.
                    String query2 =
                            "DELETE FROM Leitersemaphor WHERE FID_Leiter = '"
                                    + newLeaderID + "'";
                    SQLExecuter.executeUpdate(query2);
                    return true;
                }
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (resSet != null) {
                try {
                    resSet.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    /**
     * Dies Methode wir benutzt um das Freigeben des Leitersemaphors
     * zuerzwingen.
     *
     * @return true wenn kein Vebindungsfehler auftritt sonst false
     */
    public static boolean forceFreeSemaphore() {
        String query = "DELETE * FROM Leitersemaphor";
        try {
            SQLExecuter.executeUpdate(query);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
