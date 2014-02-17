package de.fhbingen.wbs.jdbcConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Executes SQL statements. Has an connection object which can be used for SQL
 * updates and queries.
 */
public final class SQLExecuter {

    /**
     * holds an opened connection.
     */
    private static Connection openConnection = null;

    /**
     * timeout in seconds for checking validity of a connection.
     */
    private static final int VALIDITY_CHECK_TIMEOUT = 3;

    /**
     * count of tries to reconnect lost connection.
     */
    private static final int RECONNECT_TRIES = 3;

    /**
     * private default constructor.
     */
    private SQLExecuter() {
    }

    /**
     * Opens a connection to the DB.
     * 
     * @return returns true if connection is established.
     */
    private static boolean openConnection() {
        try {

            // checks if existing connection is still valid.
            if (openConnection != null) {
                int reconnectTries = 0;
                while (!checkConnection() && reconnectTries < RECONNECT_TRIES) {
                    openConnection = null;
                    try {
                        openConnection = MySqlConnect.getConnection();
                    } catch (SQLException e) {
                    }
                    reconnectTries++;
                }
                if (reconnectTries >= RECONNECT_TRIES) {
                    System.exit(0);
                    return false;
                }
            }

            // open connection
            if (openConnection == null) {
                openConnection = MySqlConnect.getConnection();
            }
        } catch (Exception e) {
            System.err.println("no Connection");
        }
        return openConnection != null;
    }

    /**
     * Controls if connection still works
     * 
     * @return true if connection works, false when otherwise
     */
    public static boolean checkConnection() {
        if (openConnection == null) {
            return false;
        }
        try {
            Statement stmt = openConnection.createStatement();
            stmt.executeQuery("call project_select()");
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Closes the connection to the db.
     */
    public static void closeConnection() {
        try {
            if (openConnection != null) {
                if (openConnection.isValid(VALIDITY_CHECK_TIMEOUT)) {
                    openConnection.close();
                }
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
     *             an SQLException
     */
    public static void executeUpdate(final String sql) throws SQLException {
        if (!openConnection()) {
            return;
        }
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
     * Executes the given query on the database.
     * 
     * @param sql
     *            SQL Statement as String
     * @return returns a read only ResultSet
     */
    public static ResultSet executeQuery(final String sql) {
        if (!openConnection()) {
            return null;
        }
        Statement stmt;
        ResultSet rs;
        try {
            stmt =
                    openConnection.createStatement(
                            ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql);
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return Connection to the database
     */
    public static Connection getConnection() {
        if (!openConnection()) {
            return null;
        }
        return openConnection;
    }

}
