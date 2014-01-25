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
import java.util.ArrayList;
import java.util.List;

import jdbcConnection.MySqlConnect;
import jdbcConnection.SQLExecuter;
import dbaccess.data.Employee;
import dbaccess.models.EmployeesModel;

/**
 * The <code>MySQLEmployeesModel</code> class implements the
 * <code>EmployeesModel</code> and handles all the database access concerning
 * employees.
 */
public class MySQLEmployeesModel implements EmployeesModel {

    /**
     * The MySQL connection to use.
     */
    private Connection connection;

    @Override
    public void addNewEmployee(Employee employee) {
        connection = SQLExecuter.getConnection();
        try {
            PreparedStatement stm = null;
            final int paramCount = 10;
            
            String storedProcedure = "CALL employees_new(";

            for(int i=1;i<paramCount;i++){
                storedProcedure += "?,";
            }
            
            storedProcedure += "?)";
            
            stm = connection.prepareStatement(storedProcedure);
            stm.setString(1, employee.getLogin());
            stm.setString(2, employee.getLast_name());
            stm.setString(3, employee.getFirst_name());
            stm.setBoolean(4, employee.isProject_leader());
            stm.setDouble(5, employee.getDaily_rate());
            stm.setInt(6, employee.getTime_preference());
            stm.setString(7, employee.getPassword());
            stm.setString(8, MySqlConnect.getDbName());
            stm.setString(9, MySqlConnect.getId());
            stm.setString(10, MySqlConnect.getHost());
            
            stm.execute();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Employee> getEmployee() {
        connection = SQLExecuter.getConnection();
        List<Employee> empList = new ArrayList<Employee>();
        try {
            ResultSet result = null;
            Employee employee = null;
            Statement stm = connection.createStatement();
            result = stm.executeQuery("CALL employees_select(false)");

            while (result.next()) {
                employee = Employee.fromResultSet(result);
                empList.add(employee);
            }

            return empList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Employee getEmployee(String login) {
        connection = SQLExecuter.getConnection();
        Employee employee = null;
        try {
            ResultSet result = null;
            
            PreparedStatement stm = null;

            String storedProcedure = "CALL employees_select_by_login (?)";

            stm = connection.prepareStatement(storedProcedure);
            stm.setString(1, login);
            
            result=stm.executeQuery();

            if (result.next()) {
                employee = Employee.fromResultSet(result);
            }

            return employee;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Employee> getEmployee(boolean isLeader) {
        connection = SQLExecuter.getConnection();
        List<Employee> empList = new ArrayList<Employee>();
        try {
            ResultSet result = null;
            Employee employee = null;
            
            PreparedStatement stm = null;

            String storedProcedure = "CALL employees_select (?)";

            stm = connection.prepareStatement(storedProcedure);
            stm.setBoolean(1, isLeader);

            result = stm.executeQuery();

            while (result.next()) {
                employee = Employee.fromResultSet(result);
                empList.add(employee);
            }

            return empList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void updateEmployee(Employee employee) {
        connection = SQLExecuter.getConnection();
        try {
            PreparedStatement stm = null;
            final int paramCount=9;
            
            String storedProcedure = "CALL employees_update_by_id(";

            for(int i=1;i<paramCount;i++){
                storedProcedure+="?,";
            }
            
            storedProcedure+="?)";
            
            stm = connection.prepareStatement(storedProcedure);
            stm.setInt(1, employee.getId());
            stm.setString(2, employee.getLast_name());
            stm.setString(3, employee.getFirst_name());
            stm.setBoolean(4, employee.isProject_leader());
            stm.setDouble(5, employee.getDaily_rate());
            stm.setInt(6, employee.getTime_preference());
            stm.setString(7, employee.getPassword());
            stm.setString(8, MySqlConnect.getId());
            stm.setString(9, MySqlConnect.getHost());
            
            stm.execute();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteEmployee(int id) {
        connection = SQLExecuter.getConnection();
        try {
            PreparedStatement stm = null;

            String storedProcedure = "CALL employees_delete_by_id(?, ?, ?)";

            stm = connection.prepareStatement(storedProcedure);
            stm.setInt(1, id);
            stm.setString(2, MySqlConnect.getId());
            stm.setString(3, MySqlConnect.getHost());
            
            stm.execute();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
