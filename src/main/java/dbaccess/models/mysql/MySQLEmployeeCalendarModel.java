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
            PreparedStatement stm = null;

            String storedProcedure = "CALL employee_calendar_new (?,?,?,?,?,?)";

            stm = connection.prepareStatement(storedProcedure);
            stm.setInt(1, empCal.getFid_emp());
            stm.setTimestamp(2, new Timestamp(empCal.getBegin_time().getTime()));
            stm.setTimestamp(3, new Timestamp(empCal.getEnd_time().getTime()));
            stm.setString(4, empCal.getDescription());
            stm.setBoolean(5, empCal.isAvailability());
            stm.setBoolean(6, empCal.isFull_time());
            
            stm.execute();
            
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
            result = stm.executeQuery("CALL employee_calendar_select(NULL,NULL)");
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
            
            PreparedStatement stm = null;

            String storedProcedure = "CALL employee_calendar_select (?,NULL)";

            stm = connection.prepareStatement(storedProcedure);
            stm.setInt(1, id);
     
            result=stm.executeQuery();
            
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
            
            PreparedStatement stm = null;

            String storedProcedure = "CALL employee_calendar_select (?,true)";

            stm = connection.prepareStatement(storedProcedure);
            stm.setInt(1, fid);
     
            result=stm.executeQuery();
            
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
    public List<EmployeeCalendar> getEmployeeCalendarInDateRange(Date from, Date to) {
        connection = SQLExecuter.getConnection();
        List<EmployeeCalendar> empCalList = new ArrayList<EmployeeCalendar>();
        try {
           
            ResultSet result = null;
            EmployeeCalendar employeeCalendar = null;
            
            PreparedStatement stm = null;

            String storedProcedure = "CALL employee_calendar_select_by_date (?,?,NULL)";

            stm = connection.prepareStatement(storedProcedure);
            stm.setTimestamp(1, new Timestamp(from.getTime()));
            stm.setTimestamp(2, new Timestamp(to.getTime()));
          
            result=stm.executeQuery();
            
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
    public List<EmployeeCalendar> getEmployeeCalendarInDateRange(Date from, Date to, boolean mode2) {
        connection = SQLExecuter.getConnection();
      
        List<EmployeeCalendar> empCalList = new ArrayList<EmployeeCalendar>();
        try {
            ResultSet result = null;
            EmployeeCalendar employeeCalendar = null;
            
            PreparedStatement stm = null;

            String storedProcedure = "CALL employee_calendar_select_by_date (?,?,?)";

            stm = connection.prepareStatement(storedProcedure);
            stm.setTimestamp(1, new Timestamp(from.getTime()));
            stm.setTimestamp(2, new Timestamp(to.getTime()));
            stm.setBoolean(3, mode2);
            
            result=stm.executeQuery();
            
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
    public void deleteEmployeeCalendar(int id) {
        connection = SQLExecuter.getConnection();
        try {
            PreparedStatement stm = null;

            String storedProcedure = "CALL employee_calendar_delete_by_id (?)";

            stm = connection.prepareStatement(storedProcedure);
            stm.setInt(1,id);
          
            stm.execute();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
