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
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jdbcConnection.SQLExecuter;
import dbaccess.data.EmployeeCalendar;
import dbaccess.data.HolidayCalendar;
import dbaccess.models.EmployeeCalendarModel;

/**
 * The <code>MySQLEmployeeCalendarModel</code> class implements the
 * <code>EmployeeCalendarModel</code> and handles all the database access
 * concerning employee calendars.
 */
public class MySQLEmployeeCalendarModel implements EmployeeCalendarModel {

    /**
     * The MySQL connection to use.
     */
    private Connection connection;

    @Override
    public void addNewEmployeeCalendar(EmployeeCalendar empCal) {
        connection = SQLExecuter.getConnection();
        try {
            Statement stm = connection.createStatement();
            stm.execute("CALL employee_calendar_new (" + empCal.getFid_emp()
                    + ",'" + new Timestamp(empCal.getBegin_time().getTime())
                    + "','" + new Timestamp(empCal.getEnd_time().getTime())
                    + "','" + empCal.getDescription() + "',"
                    + empCal.isAvailability() + "," + empCal.isFull_time()
                    + ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<EmployeeCalendar> getEmployeeCalendar() {
        connection = SQLExecuter.getConnection();
        List<EmployeeCalendar> empCalList = new ArrayList<EmployeeCalendar>();
        try {
            ResultSet result = null;
            EmployeeCalendar employeeCalendar = null;
            Statement stm = connection.createStatement();
            result =
                    stm.executeQuery("CALL employee_calendar_select(NULL,NULL)");
            while (result.next()) {
                employeeCalendar = EmployeeCalendar.fromResultSet(result);
                empCalList.add(employeeCalendar);
            }

            return empCalList;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public EmployeeCalendar getEmployeeCalendar(int id) {
        connection = SQLExecuter.getConnection();
        EmployeeCalendar employeeCalendar = null;
        try {
            ResultSet result = null;
            Statement stm = connection.createStatement();
            result =
                    stm.executeQuery("CALL employee_calendar_select(" + id
                            + ", NULL)");

            if (result.next()) {
                employeeCalendar = EmployeeCalendar.fromResultSet(result);
            }

            return employeeCalendar;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<EmployeeCalendar> getEmployeeCalendarForFID(int fid) {
        connection = SQLExecuter.getConnection();
        List<EmployeeCalendar> empCalList = new ArrayList<EmployeeCalendar>();
        try {
            ResultSet result = null;
            EmployeeCalendar employeeCalendar = null;
            Statement stm = connection.createStatement();
            result =
                    stm.executeQuery("CALL employee_calendar_select(" + fid
                            + ", true)");

            while (result.next()) {
                employeeCalendar = EmployeeCalendar.fromResultSet(result);
                empCalList.add(employeeCalendar);
            }

            return empCalList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<EmployeeCalendar> getEmployeeCalendarInDateRange(Date from,
            Date to) {
        connection = SQLExecuter.getConnection();
        List<EmployeeCalendar> empCalList = new ArrayList<EmployeeCalendar>();
        try {

            ResultSet result = null;
            EmployeeCalendar employeeCalendar = null;
            Statement stm = connection.createStatement();
            result =
                    stm.executeQuery("CALL employee_calendar_select_by_date(' "
                            + new Timestamp(from.getTime()) + "','"
                            + new Timestamp(to.getTime()) + "',NULL)");

            while (result.next()) {
                employeeCalendar = EmployeeCalendar.fromResultSet(result);
                empCalList.add(employeeCalendar);
            }

            return empCalList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<EmployeeCalendar> getEmployeeCalendarInDateRange(Date from,
            Date to, int empId ) {
        connection = SQLExecuter.getConnection();
        List<EmployeeCalendar> ecList = new ArrayList<EmployeeCalendar>();

        ResultSet sqlResult = null;
        PreparedStatement stm = null;

        final String storedProcedure =
                "CALL employee_calendar_select_by_date_and_emp(?,?,?)";

        try {
            stm = connection.prepareStatement(storedProcedure);
            stm.setTimestamp(1, new Timestamp(from.getTime()));
            stm.setTimestamp(2, new Timestamp(to.getTime()));
            stm.setInt(3, empId);

            sqlResult = stm.executeQuery();

            while (sqlResult.next()) {
                ecList.add(EmployeeCalendar.fromResultSet(sqlResult));
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
        return ecList;        
    }

    @Override
    public boolean deleteEmployeeCalendar(int id) {
        connection = SQLExecuter.getConnection();
        boolean success = false;
        try {
            Statement stm = connection.createStatement();
            stm.execute("CALL employee_calendar_delete_by_id( " + id + ")");
            success = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }

    @Override
    public void updateEmployeeCalendar(EmployeeCalendar empCal) {
        final Connection connection = SQLExecuter.getConnection();

        PreparedStatement stm = null;

        String storedProcedure =
                "CALL employee_calendar_update_by_id(?,?,?,?,?,?,?)";

        try {
            stm = connection.prepareStatement(storedProcedure);
            stm.setInt(1, empCal.getId());
            stm.setInt(2, empCal.getFid_emp());
            stm.setTimestamp(3, new Timestamp(empCal.getBegin_time().getTime()));
            stm.setTimestamp(4, new Timestamp(empCal.getEnd_time().getTime()));
            stm.setString(5, empCal.getDescription());
            stm.setBoolean(6, empCal.isAvailability());
            stm.setBoolean(7, empCal.isFull_time());

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
}
