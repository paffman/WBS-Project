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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The <code>MySQLWorkpackageModel</code> class implements the
 * <code>WorkpackageModel</code> and handles all the database access concerning
 * work packages.
 */
public class MySQLWorkpackageModel implements WorkpackageModel {

    /**
     * The MySQL connection to use.
     */
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
    public final void addNewWorkpackage(final Workpackage wp) {
        final int paramCount = 22;
        PreparedStatement stm = null;

        String storedProcedure = "CALL workpackage_new(";

        for (int i = 1; i < paramCount; i++) {
            storedProcedure += "?,";
        }

        storedProcedure += "?)";

        System.out.println(storedProcedure);

        try {
            stm = connection.prepareStatement(storedProcedure);
            stm.setString(1, wp.getStringID());
            stm.setInt(2, wp.getProjectID());
            stm.setInt(3, wp.getEmployeeID());
            stm.setInt(4, wp.getParentID());

            stm.setString(5, wp.getName());
            stm.setString(6, wp.getDescription());

            stm.setDouble(7, wp.getBac());
            stm.setDouble(8, wp.getAc());
            stm.setDouble(9, wp.getEv());
            stm.setDouble(10, wp.getEtc());
            stm.setDouble(11, wp.getEac());

            stm.setDouble(12, wp.getCpi());

            stm.setDouble(13, wp.getBacCosts());
            stm.setDouble(14, wp.getAcCosts());
            stm.setDouble(15, wp.getEtcCosts());

            stm.setDouble(16, wp.getDailyRate());

            stm.setTimestamp(17, new Timestamp(wp.getReleaseDate().getTime()));
            stm.setBoolean(18, wp.isTopLevel());
            stm.setBoolean(19, wp.isInactive());
            stm.setTimestamp(20, new Timestamp(wp.getStartDateCalc()
                    .getTime()));
            stm.setTimestamp(21, new Timestamp(wp.getStartDateWish()
                    .getTime()));
            stm.setTimestamp(22, new Timestamp(wp.getEndDateCalc().getTime()));

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
    public final List<Workpackage> getWorkpackage() {
        return getWorkpackage(false);
    }

    @Override
    public final List<Workpackage> getWorkpackage(final boolean onlyLeaves) {
        List<Workpackage> wpList = new ArrayList<Workpackage>();

        ResultSet sqlResult = null;
        Statement stm = null;

        try {
            stm = connection.createStatement();
            String sql = String.format("CALL workpackage_select(%b)",
                    onlyLeaves);
            sqlResult = stm.executeQuery(sql);

            while (sqlResult.next()) {
                wpList.add(Workpackage.fromResultSet(sqlResult));
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

        return wpList;
    }

    @Override
    public final Workpackage getWorkpackage(final String stringID) {

        final int projectID = 1;

        Workpackage wp = null;
        ResultSet sqlResult = null;

        PreparedStatement stm = null;
        final String storedProcedure = "CALL workpackage_select_by_id(?, ?)";

        try {
            stm = connection.prepareStatement(storedProcedure);
            stm.setString(1, stringID);
            stm.setInt(2, projectID);

            sqlResult = stm.executeQuery();

            if (sqlResult.next()) {
                wp = Workpackage.fromResultSet(sqlResult);
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

        return wp;
    }

    @Override
    public final List<Workpackage> getWorkpackagesInDateRange(final Date from,
                                                              final Date to) {
        final List<Workpackage> wpList = new ArrayList<Workpackage>();
        ResultSet sqlResult = null;
        PreparedStatement stm = null;
        final String storedProcedure = "CALL workpackage_select_by_date(?, ?)";
        final Timestamp fromTimestamp = new Timestamp(from.getTime());
        final Timestamp toTimestamp = new Timestamp(to.getTime());

        try {
            stm = connection.prepareStatement(storedProcedure);
            stm.setTimestamp(1, fromTimestamp);
            stm.setTimestamp(2, toTimestamp);
            sqlResult = stm.executeQuery();

            while (sqlResult.next()) {
                wpList.add(Workpackage.fromResultSet(sqlResult));
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

        return wpList;
    }

    @Override
    public final void updateWorkpackage(final Workpackage wp) {
        final int paramCount = 21;
        PreparedStatement stm = null;

        String storedProcedure = "CALL workpackage_update_by_id(";

        for (int i = 1; i < paramCount; i++) {
            storedProcedure += "?,";
        }

        storedProcedure += "?)";

        System.out.println(storedProcedure);

        try {
            stm = connection.prepareStatement(storedProcedure);
            stm.setString(1, wp.getStringID());
            stm.setInt(2, wp.getProjectID());
            stm.setInt(3, wp.getEmployeeID());

            stm.setString(4, wp.getName());
            stm.setString(5, wp.getDescription());

            stm.setDouble(6, wp.getBac());
            stm.setDouble(7, wp.getAc());
            stm.setDouble(8, wp.getEv());
            stm.setDouble(9, wp.getEtc());
            stm.setDouble(10, wp.getEac());

            stm.setDouble(11, wp.getCpi());

            stm.setDouble(12, wp.getBacCosts());
            stm.setDouble(13, wp.getAcCosts());
            stm.setDouble(14, wp.getEtcCosts());

            stm.setDouble(15, wp.getDailyRate());

            stm.setTimestamp(16, new Timestamp(wp.getReleaseDate().getTime()));
            stm.setBoolean(17, wp.isTopLevel());
            stm.setBoolean(18, wp.isInactive());
            stm.setTimestamp(19, new Timestamp(wp.getStartDateCalc()
                    .getTime()));
            stm.setTimestamp(20, new Timestamp(wp.getStartDateWish()
                    .getTime()));
            stm.setTimestamp(21, new Timestamp(wp.getEndDateCalc().getTime()));

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
    public final void deleteWorkpackage(String stringID) {
        final int projectID = 1;

        PreparedStatement stm = null;
        final String storedProcedure = "CALL workpackage_delete_by_id(?, ?)";

        try {
            stm = connection.prepareStatement(storedProcedure);
            stm.setString(1, stringID);
            stm.setInt(2, projectID);
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

}
