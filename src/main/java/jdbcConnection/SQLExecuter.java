package jdbcConnection;

import java.sql.*;

/**
 * Studienprojekt: WBS
 * 
 * Kunde: Pentasys AG, Jens von Gersdorff Projektmitglieder: Andre Paffenholz,
 * Peter Lange, Daniel Metzler, Samson von Graevenitz
 * 
 * Führt SQL-Statements aus. Hält ein Connection-Objekt auf dem SQL-Updates
 * und SQL-Querys ausgeführt werden können.
 * 
 * @author Samson von Graevenitz
 * @version - 0.1 30.11.2010
 */
public class SQLExecuter {

    private static Connection openConnection = null;

    /**
     * Default-Konstruktor
     */
    public SQLExecuter() {
    }

    /**
     * Opens a connection to the DB.
     */
    private static void openConnection() {
	try {
	    if (openConnection == null) {
		openConnection = MySqlConnect.getConnection();
	    }
	} catch (Exception e) {
	    System.err.println("no Connection");

	}
    }

    /**
     * Closes the connection to the db.
     */
    public static void closeConnection() {
	try {
	    if (openConnection != null) {
		openConnection.close();
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	openConnection = null;
    }

    /**
     * @param sql
     *            SQL Statement as String
     * @throws SQLException
     */
    public static void executeUpdate(final String sql) throws SQLException {
	openConnection();
	Statement stmt;
	try {
	    stmt = openConnection.createStatement();
	    stmt.executeUpdate(sql);
	    openConnection.commit();
	    stmt.close();
	} catch (SQLException e) {
	    if (e.getErrorCode() == -1612) {
		throw new SQLException(e);
	    } else {
		throw e;
	    }
	}
    }

    /**
     * 
     * @param sql
     *            SQL Statement as String
     * @return returns a ResultSet
     */
    public static ResultSet executeQuery(final String sql) {
	openConnection();
	Statement stmt;
	ResultSet rs;
	try {
	    stmt = openConnection.createStatement(
		    ResultSet.TYPE_SCROLL_INSENSITIVE,
		    ResultSet.CONCUR_UPDATABLE);
	    rs = stmt.executeQuery(sql);
	    return rs;
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return null;
    }

}
