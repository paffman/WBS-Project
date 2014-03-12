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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import de.fhbingen.wbs.jdbcConnection.SQLExecuter;
import de.fhbingen.wbs.dbaccess.data.Baseline;
import de.fhbingen.wbs.dbaccess.models.BaselineModel;

/**
 * The <code>MySQLBaselineModel</code> class implements the
 * <code>BaselineModel</code> and handles all the database access concerning
 * baselines.
 */
public class MySQLBaselineModel implements BaselineModel {
    /**
     * The MySQL connection to use.
     */
    private Connection connection;

    @Override
    public boolean addNewBaseline(Baseline line) {
        connection = SQLExecuter.getConnection();
        boolean success = false;
        try {
            PreparedStatement stm = null;

            String storedProcedure = "CALL baseline_new (?,?,?)";

            stm = connection.prepareStatement(storedProcedure);
            stm.setInt(1, line.getFid_project());
            stm.setTimestamp(2, new Timestamp(line.getBl_date().getTime()));
            stm.setString(3, line.getDescription());

            stm.execute();
            success = true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }

    @Override
    public List<Baseline> getBaseline() {
        connection = SQLExecuter.getConnection();
        List<Baseline> blList = new ArrayList<Baseline>();
        try {
            ResultSet result = null;
            Baseline baseline = new Baseline();
            Statement stm = connection.createStatement();
            result = stm.executeQuery("CALL baseline_select(NULL)");

            while (result.next()) {
                baseline = Baseline.fromResultSet(result);
                blList.add(baseline);
            }

            return blList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Baseline getBaseline(int baselineID) {
        connection = SQLExecuter.getConnection();

        Baseline bl = null;
        ResultSet sqlResult = null;

        PreparedStatement stm = null;
        final String storedProcedure = "CALL baseline_select(?)";

        try {
            stm = connection.prepareStatement(storedProcedure);
            stm.setInt(1, baselineID);

            sqlResult = stm.executeQuery();

            if (sqlResult.next()) {
                bl = Baseline.fromResultSet(sqlResult);
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

        return bl;

    }
}
