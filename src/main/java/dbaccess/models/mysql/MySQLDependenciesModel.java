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

package dbaccess.models.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import jdbcConnection.SQLExecuter;
import dbaccess.data.Dependency;
import dbaccess.models.DependenciesModel;

/**
 * The <code>MySQLDependenciesModel</code> class implements the
 * <code>Dependency</code> and handles all the database access concerning
 * dependencies.
 */
public class MySQLDependenciesModel implements DependenciesModel {

    /**
     * The MySQL connection to use.
     */
    private Connection connection;

    @Override
    public boolean addNewDependency(Dependency dependency) {
        connection = SQLExecuter.getConnection();
        boolean success = false;
        try {
<<<<<<< HEAD
            Statement stm = connection.createStatement();
            stm.execute("CALL dependencies_new ("
                    + dependency.getFid_wp_predecessor() + ","
                    + dependency.getFid_wp_successor() + ")");
            success = true;
=======
            PreparedStatement stm = null;

            String storedProcedure = "CALL dependencies_new (?,?)";

            stm = connection.prepareStatement(storedProcedure);
            stm.setInt(1, dependency.getFid_wp_predecessor());
            stm.setInt(2, dependency.getFid_wp_successor());
            
            stm.execute();
            
>>>>>>> PROJ-WS1314-WBS-DEV
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }

    @Override
    public List<Dependency> getDependency() {
        connection = SQLExecuter.getConnection();
        List<Dependency> depList = new ArrayList<Dependency>();
        try {
            ResultSet result = null;
            Dependency dependency = null;
            Statement stm = connection.createStatement();
            result = stm.executeQuery("CALL dependencies_select()");

            while (result.next()) {
                dependency = Dependency.fromResultSet(result);
                depList.add(dependency);
            }

            return depList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean deleteDependency(int predecessorWpID, int successorWpID) {
        connection = SQLExecuter.getConnection();
        boolean success = false;
        try {
<<<<<<< HEAD
            Statement stm = connection.createStatement();
            stm.execute("CALL dependencies_delete_by_key(" + predecessorWpID
                    + "," + successorWpID + ")");
            success = true;
=======
            PreparedStatement stm = null;

            String storedProcedure = "CALL dependencies_delete_by_key (?,?)";

            stm = connection.prepareStatement(storedProcedure);
            stm.setInt(1, predecessorWpID);
            stm.setInt(2, successorWpID);
            
            stm.execute();
            
>>>>>>> PROJ-WS1314-WBS-DEV
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }

}
