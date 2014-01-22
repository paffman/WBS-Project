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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import jdbcConnection.SQLExecuter;
import dbaccess.data.Baseline;
import dbaccess.models.BaselineModel;

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
    public void addNewBaseline(Baseline line) {
        connection=SQLExecuter.getConnection();
        try {
            Statement stm = connection.createStatement();
            
            stm.execute("CALL baseline_new ("
                    + line.getFid_project() + ",'" + new Timestamp(new java.sql.Date(line.getBl_date().getTime()).getTime()) + "','"
                    + line.getDescription() + "')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Baseline> getBaseline() {
        connection=SQLExecuter.getConnection();
        List<Baseline> blList = new ArrayList<Baseline>();
        try {
            ResultSet result = null;
            Baseline baseline=new Baseline();
            Statement stm = connection.createStatement();
            result = stm.executeQuery("CALL baseline_select(NULL)");

            while (result.next()){
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
        connection=SQLExecuter.getConnection();
        Baseline baseline = new Baseline();
        try {
            ResultSet result = null;
            Statement stm = connection.createStatement();
            result = stm.executeQuery("CALL baseline_select("
                    + baselineID + ")");

            if (result.next()){
                baseline = Baseline.fromResultSet(result);
            }
            
            return baseline;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
