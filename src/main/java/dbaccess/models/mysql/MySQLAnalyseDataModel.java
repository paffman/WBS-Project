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
import java.util.ArrayList;
import java.util.List;

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

    /**
     * Constructor.
     *
     * @param con The MySQL connection to use.
     */
    public MySQLAnalyseDataModel(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addNewAnalyseData(AnalyseData data) {
        connection=SQLExecuter.getConnection();
        try {
            Statement stm = connection.createStatement();
            stm.execute("CALL analyse_data_new (" 
                    + data.getFid_wp() + "," + data.getFid_baseline()
                    + ", '" + data.getName() + "'," + data.getBac() + ","
                    + data.getAc() + "," + data.getEv() + "," + data.getEtc()
                    + "," + data.getEac() + "," + data.getCpi() + ","
                    + data.getBac_costs() + "," + data.getAc_costs() + ","
                    + data.getEtc_costs() + "," + data.getSv() + ","
                    + data.getSpi() + "," + data.getPv() + ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public AnalyseData getAnalyseData(int fid) {
        connection=SQLExecuter.getConnection();
        AnalyseData aData = new AnalyseData();
        try {
            ResultSet result = null;
            Statement stm = connection.createStatement();
            result = stm.executeQuery("CALL analyse_data_select_by("
                    + fid+")");

            if (result.next()){
                aData = AnalyseData.fromResultSet(result);
                       
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return aData;
    }

    @Override
    public List<?> getAnalyseDataForBaseline(int baseline) {
        connection=SQLExecuter.getConnection();
        List<AnalyseData> adList = new ArrayList<AnalyseData>();
        try {
            ResultSet result = null;
            AnalyseData aData=new AnalyseData();
            Statement stm = connection.createStatement();
            result = stm.executeQuery("CALL analyse_data_select_by("
                    + baseline+",true)");

            while (result.next()){
                aData = AnalyseData.fromResultSet(result);
                adList.add(aData);
            }
            
            return adList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
