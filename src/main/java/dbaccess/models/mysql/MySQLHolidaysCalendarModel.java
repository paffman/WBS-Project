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
import java.util.Date;
import java.util.List;

import jdbcConnection.SQLExecuter;
import dbaccess.data.HolidayCalendar;
import dbaccess.models.HolidaysCalendarModel;

/**
 * The <code>MySQLHolidaysCalendarModel</code> class implements the
 * <code>HolidaysCalendarModel</code> and handles all the database access
 * concerning the holidays calendar.
 */
public class MySQLHolidaysCalendarModel implements HolidaysCalendarModel {

    @Override
    public final void addNewHolidayCalendar(final HolidayCalendar holCal) {
        final Connection connection = SQLExecuter.getConnection();
        PreparedStatement stm = null;

        String storedProcedure = "CALL " + "holidays_calendar_new(?,?,?,?,?)";

        try {
            stm = connection.prepareStatement(storedProcedure);
            stm.setString(1, holCal.getTitle());
            stm.setTimestamp(2, new Timestamp(holCal.getBegin_time().getTime()));
            stm.setTimestamp(3, new Timestamp(holCal.getEnd_time().getTime()));
            stm.setBoolean(4, holCal.isAvailability());
            stm.setBoolean(5, holCal.isFull_time());

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
    public final List<HolidayCalendar> getHolidayCalendar() {
        final Connection connection = SQLExecuter.getConnection();
        List<HolidayCalendar> hcList = new ArrayList<HolidayCalendar>();

        ResultSet sqlResult = null;
        PreparedStatement stm = null;

        final String storedProcedure = "CALL holidays_calendar_select(null)";

        try {
            stm = connection.prepareStatement(storedProcedure);
            sqlResult = stm.executeQuery();

            while (sqlResult.next()) {
                hcList.add(HolidayCalendar.fromResultSet(sqlResult));
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
        return hcList;
    }

    @Override
    public final HolidayCalendar getHolidayCalendar(final int calID) {
        final Connection connection = SQLExecuter.getConnection();
        HolidayCalendar rslt = null;

        ResultSet sqlResult = null;
        PreparedStatement stm = null;

        final String storedProcedure = "CALL holidays_calendar_select(?)";

        try {
            stm = connection.prepareStatement(storedProcedure);
            stm.setInt(1, calID);

            sqlResult = stm.executeQuery();

            while (sqlResult.next()) {
                rslt = HolidayCalendar.fromResultSet(sqlResult);
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
        return rslt;
    }

    @Override
    public final List<HolidayCalendar> getHolidayCalendar(final Date from,
            final Date to, final boolean mode2) {
        final Connection connection = SQLExecuter.getConnection();
        List<HolidayCalendar> hcList = new ArrayList<HolidayCalendar>();

        ResultSet sqlResult = null;
        PreparedStatement stm = null;

        final String storedProcedure =
                "CALL holidays_calendar_select_by_date(?,?,?)";

        try {
            stm = connection.prepareStatement(storedProcedure);
            stm.setTimestamp(1, new Timestamp(from.getTime()));
            stm.setTimestamp(2, new Timestamp(to.getTime()));
            stm.setBoolean(3, mode2);

            sqlResult = stm.executeQuery();

            while (sqlResult.next()) {
                hcList.add(HolidayCalendar.fromResultSet(sqlResult));
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
        return hcList;
    }

    @Override
    public final void updateHolidayCalendar(final HolidayCalendar hc) {
        final Connection connection = SQLExecuter.getConnection();
        PreparedStatement stm = null;

        String storedProcedure =
                "CALL holidays_calendar_update_by_id(?,?,?,?,?,?)";

        try {
            stm = connection.prepareStatement(storedProcedure);
            stm.setInt(1, hc.getId());
            stm.setString(2, hc.getTitle());
            stm.setTimestamp(3, new Timestamp(hc.getBegin_time().getTime()));
            stm.setTimestamp(4, new Timestamp(hc.getEnd_time().getTime()));
            stm.setBoolean(5, hc.isAvailability());
            stm.setBoolean(6, hc.isFull_time());

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
    public final boolean deleteHolidayCalendar(final int calID) {
        final Connection connection = SQLExecuter.getConnection();
        PreparedStatement stm = null;
        boolean success = false;
        String storedProcedure = "CALL holidays_calendar_delete_by_id(?)";

        try {
            stm = connection.prepareStatement(storedProcedure);
            stm.setInt(1, calID);

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
}
