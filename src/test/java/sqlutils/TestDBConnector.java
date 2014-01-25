/*
 * The WBS-­Tool is a project managment tool combining the Work Breakdown
 * Structure and Earned Value Analysis
 * Copyright (C) 2013 FH-­Bingen
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY;; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package sqlutils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * A utility class which connects to a test database.
 */
public class TestDBConnector {

    private static Connection connection;

    /**
     * Open the connection to the test database.
     *
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static final void openConnection() throws SQLException,
            ClassNotFoundException {

        final String url = "jdbc:mysql://localhost:3306/";
        final String dbName = "mbtest";
        final String driver = "com.mysql.jdbc.Driver";
        final String userName = "unittest";
        final String password = "junit411";

        Class.forName(driver);
        connection = DriverManager.getConnection(url + dbName, userName,
                password);
    }

    /**
     * Closes the connection to the test database.
     */
    public static final void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Returns the test database connection.
     *
     * @return A database connection
     */
    public static final Connection getConnection() {
        return connection;
    }

}
