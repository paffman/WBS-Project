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
import java.util.ArrayList;
import java.util.List;

import jdbcConnection.SQLExecuter;
import dbaccess.data.AnalyseData;
import dbaccess.models.AnalyseDataModel;

/**
 * The <code>MySQLAnalyseDataModel</code> class implements the
 * <code>AnalyseDataModel</code> and handles all the database access concerning
 * analyse data.
 */
public class MySQLAnalyseDataModel implements AnalyseDataModel {

    /**
     * The MySQL connection to use.
     */
    private Connection connection;

    @Override
    public void addNewAnalyseData(AnalyseData data) {
        connection = SQLExecuter.getConnection();
        final int paramCount = 15;
        PreparedStatement stm = null;
        try {
            String storedProcedure = "CALL analyse_data_new (";

            for (int i = 1; i < paramCount; i++) {
                storedProcedure += "?,";
            }

            storedProcedure += "?)";

            stm = connection.prepareStatement(storedProcedure);
            stm.setInt(1, data.getFid_wp());
            stm.setInt(2, data.getFid_baseline());
            stm.setString(3, data.getName());
            stm.setDouble(4, data.getBac());
            stm.setDouble(5, data.getAc());
            stm.setDouble(6, data.getEv());
            stm.setDouble(7, data.getEtc());
            stm.setDouble(8, data.getEac());
            stm.setDouble(9, data.getCpi());
            stm.setDouble(10, data.getBac_costs());
            stm.setDouble(11, data.getAc_costs());
            stm.setDouble(12, data.getEtc_costs());
            stm.setDouble(13, data.getSv());
            stm.setDouble(14, data.getSpi());
            stm.setDouble(15, data.getPv());

            stm.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public AnalyseData getAnalyseData(int fid) {
        connection = SQLExecuter.getConnection();
        AnalyseData aData = new AnalyseData();
        try {
            ResultSet result = null;
            PreparedStatement stm = null;

            String storedProcedure = "CALL analyse_data_select_by(?,?)";

            stm = connection.prepareStatement(storedProcedure);
            stm.setInt(1, fid);
            stm.setBoolean(2, false);

            result = stm.executeQuery();

            if (result.next()) {
                aData = AnalyseData.fromResultSet(result);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return aData;
    }

    @Override
    public List<AnalyseData> getAnalyseDataForBaseline(int baseline) {
        connection = SQLExecuter.getConnection();
        List<AnalyseData> adList = new ArrayList<AnalyseData>();
        try {
            ResultSet result = null;
            AnalyseData aData = new AnalyseData();
            
            PreparedStatement stm = null;

            String storedProcedure = "CALL analyse_data_select_by(?,?)";

            stm = connection.prepareStatement(storedProcedure);
            stm.setInt(1, baseline);
            stm.setBoolean(2, true);

            result = stm.executeQuery();
            
            while (result.next()) {
                aData = AnalyseData.fromResultSet(result);
                adList.add(aData);
            }

            return adList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return adList;
    }

}
