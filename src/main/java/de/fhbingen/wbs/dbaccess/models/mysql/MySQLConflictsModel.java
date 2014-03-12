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

package de.fhbingen.wbs.dbaccess.models.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import de.fhbingen.wbs.jdbcConnection.SQLExecuter;
import de.fhbingen.wbs.dbaccess.data.Conflict;
import de.fhbingen.wbs.dbaccess.models.ConflictsModel;

/**
 * The <code>MySQLConflictsModel</code> class implements the
 * <code>ConflictsModel</code> and handles all the database access concerning
 * conflicts.
 */
public class MySQLConflictsModel implements ConflictsModel {

    /**
     * The MySQL connection to use.
     */
    private Connection connection;

    @Override
    public boolean addNewConflict(Conflict conflict) {
        connection = SQLExecuter.getConnection();
        final int paramCount = 22;
        PreparedStatement stm = null;
        boolean success = false;

        String storedProcedure = "CALL conflicts_new(?,?,?,?,?)";

        System.out.println(storedProcedure);

        try {
            stm = connection.prepareStatement(storedProcedure);
            if (conflict.getFid_wp() <= 0) {
                stm.setNull(1, java.sql.Types.INTEGER);
            } else {
                stm.setInt(1, conflict.getFid_wp());
            }
            if (conflict.getFid_wp_affected() <= 0) {
                stm.setNull(2, java.sql.Types.INTEGER);
            } else {
                stm.setInt(2, conflict.getFid_wp_affected());
            }
            stm.setInt(3, conflict.getFid_emp());
            stm.setInt(4, conflict.getReason());
            stm.setTimestamp(5, de.fhbingen.wbs.calendar.DateFunctions
                    .getTimesampOrNull(conflict.getOccurence_date()));

            stm.execute();
            success = true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stm != null) {
                    stm.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return success;
    }

    @Override
    public List<Conflict> getConflicts() {
        connection = SQLExecuter.getConnection();
        List<Conflict> conList = new ArrayList<Conflict>();
        try {
            ResultSet result = null;
            Conflict conflict = new Conflict();
            Statement stm = connection.createStatement();
            result = stm.executeQuery("CALL conflicts_select()");

            while (result.next()) {
                conflict = Conflict.fromResultSet(result);
                conList.add(conflict);
            }

            return conList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deleteConflicts() {
        connection = SQLExecuter.getConnection();
        try {
            Statement stm = connection.createStatement();
            stm.execute("CALL conflicts_delete()");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteConflict(int id) {
        connection = SQLExecuter.getConnection();
        try {
            PreparedStatement stm = null;

            String storedProcedure = "CALL conflicts_delete_by_id (?)";

            stm = connection.prepareStatement(storedProcedure);
            stm.setInt(1, id);

            stm.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
