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

import dbaccess.data.Conflict;
import dbaccess.models.ConflictsModel;

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

    /**
     * Constructor.
     *
     * @param con The MySQL connection to use.
     */
    public MySQLConflictsModel(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addNewConflict(Conflict conflict) {
        try {
            Statement stm = connection.createStatement();
            stm.execute("INSERT INTO conflicts VALUES (" + conflict.getId()
                    + "," + conflict.getFid_wp() + ","
                    + conflict.getFid_wp_affected() + ","
                    + conflict.getFid_emp() + "," + conflict.getReason() + ","
                    + ",'" + conflict.getOccurence_date() + "')");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public List<Conflict> getConflicts() {
        List<Conflict> conList = new ArrayList<Conflict>();
        try {
            ResultSet result = null;
            Conflict conflict = new Conflict();
            Statement stm = connection.createStatement();
            result = stm.executeQuery("SELECT * FROM conflict");

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
        try {
            Statement stm = connection.createStatement();
            stm.execute("DELETE * FROM conflict");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteConflict(int id) {
        try {
            Statement stm = connection.createStatement();
            stm.execute("DELETE * FROM conflict WHERE id = " + id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
