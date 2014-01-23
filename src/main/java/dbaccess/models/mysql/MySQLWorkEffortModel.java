/*
 * The WBS-Tool is a project management tool combining the Work Breakdown
 * Structure and Earned Value Analysis
 * Copyright (C) 2013 FH-Bingen
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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import jdbcConnection.SQLExecuter;
import dbaccess.data.WorkEffort;
import dbaccess.models.WorkEffortModel;

/**
 * The <code>MySQLWorkEffortModel</code> class implements the
 * <code>WorkEffortModel</code> and handles all the database access concerning
 * work efforts.
 */
public class MySQLWorkEffortModel implements WorkEffortModel {

    @Override
    public final void addNewWorkEffort(final WorkEffort effort) {
        final Connection connection = SQLExecuter.getConnection();
        PreparedStatement stm = null;

        String storedProcedure = "CALL work_effort_new(?,?,?,?,?)";

        try {
            stm = connection.prepareStatement(storedProcedure);
            stm.setInt(1, effort.getFid_wp());
            stm.setInt(2, effort.getFid_emp());
            stm.setTimestamp(3, new Timestamp(effort.getRec_date().getTime()));
            stm.setDouble(4, effort.getEffort());
            stm.setString(5, effort.getDescription());

            stm.execute();
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
    }

    @Override
    public final List<WorkEffort> getWorkEffort() {
        final Connection connection = SQLExecuter.getConnection();
        List<WorkEffort> effortList = new ArrayList<WorkEffort>();

        ResultSet sqlResult = null;
        PreparedStatement stm = null;

        final String storedProcedure = "CALL work_effort_select(null)";

        try {
            stm = connection.prepareStatement(storedProcedure);
            sqlResult = stm.executeQuery();

            while (sqlResult.next()) {
                effortList.add(WorkEffort.fromResultSet(sqlResult));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (sqlResult != null) {
                    sqlResult.close();
                }
                if (stm != null) {
                    stm.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return effortList;
    }

    @Override
    public final List<WorkEffort> getWorkEffort(final int wpId) {
        final Connection connection = SQLExecuter.getConnection();
        List<WorkEffort> effortList = new ArrayList<WorkEffort>();

        ResultSet sqlResult = null;
        PreparedStatement stm = null;

        final String storedProcedure = "CALL work_effort_select(?)";

        try {
            stm = connection.prepareStatement(storedProcedure);
            stm.setInt(1, wpId);
            sqlResult = stm.executeQuery();

            while (sqlResult.next()) {
                effortList.add(WorkEffort.fromResultSet(sqlResult));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (sqlResult != null) {
                    sqlResult.close();
                }
                if (stm != null) {
                    stm.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return effortList;
    }

}
