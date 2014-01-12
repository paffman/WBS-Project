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

import dbaccess.data.Workpackage;
import dbaccess.models.WorkpackageModel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The <code>MySQLWorkpackageModel</code> class implements the
 * <code>WorkpackageModel</code> and handles all the database access
 * concerning work packages.
 */
public class MySQLWorkpackageModel implements WorkpackageModel {

    /** The MySQL connection to use. */
    private Connection connection;

    /**
     * Constructor.
     *
     * @param con The MySQL connection to use.
     */
    public MySQLWorkpackageModel(final Connection con) {
        this.connection = con;
    }

    @Override
    public final void addNewWorkpackage(final Workpackage workpackage) {

    }

    @Override
    public final List<Workpackage> getWorkpackage() {
        List<Workpackage> wpList = new ArrayList<Workpackage>();
        ResultSet sqlResult = null;

        try {
            Statement stm = connection.createStatement();
            sqlResult = stm.executeQuery("CALL workpackage_select(NULL)");

            while (sqlResult.next()) {
                wpList.add(Workpackage.fromResultSet(sqlResult));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return wpList;
    }

    @Override
    public final List<Workpackage> getWorkpackage(final boolean onlyLeaves) {
        return null;
    }

    @Override
    public final Workpackage getWorkpackage(final String stringID) {

        final int projectID = 1;

        Workpackage wp = null;
        ResultSet sqlResult = null;

        try {
            Statement stm = connection.createStatement();
            final String storedProc = "CALL workpackage_select_by_id('"
                    + stringID + "', " + projectID + ")";

            sqlResult = stm.executeQuery(storedProc);

            if (sqlResult.next()) {
                wp = Workpackage.fromResultSet(sqlResult);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return wp;
    }

    @Override
    public final List<Workpackage> getWorkpackagesInDateRange(final Date from,
                                                              final Date to) {
        return null;
    }

    @Override
    public final void updateWorkpackage(final Workpackage wp) {

    }

    @Override
    public final void deleteWorkpackage(final int wpID) {

    }

}
