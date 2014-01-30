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
import dbaccess.data.Workpackage;
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
    public void addNewEmployee(final Employee employee) {
        connection = SQLExecuter.getConnection();
        try {
            Statement stm = connection.createStatement();
            stm.execute("CALL employees_new('" + employee.getLogin() + "','"
                    + employee.getLast_name() + "','"
                    + employee.getFirst_name() + "',"
                    + employee.isProject_leader() + ","
                    + employee.getDaily_rate() + ","
                    + employee.getTime_preference() + ",'"
                    + employee.getPassword() + "','" + MySqlConnect.getDbName()
                    + "','" + MySqlConnect.getId() + "','"
                    + MySqlConnect.getHost() + "')");
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
    public Employee getEmployee(final String login) {
        connection = SQLExecuter.getConnection();
        Employee employee = null;
        try {
            ResultSet result = null;
            Statement stm = connection.createStatement();
            result =
                    stm.executeQuery("CALL employees_select_by_login ('"
                            + login + "')");

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
    public List<Employee> getEmployee(final boolean noLeaders) {
        connection = SQLExecuter.getConnection();

        List<Employee> emp = new ArrayList<Employee>();
        ResultSet sqlResult = null;

        PreparedStatement stm = null;
        final String storedProcedure = "CALL employees_select(?)";

        try {
            stm = connection.prepareStatement(storedProcedure);
            stm.setBoolean(1, noLeaders);

            sqlResult = stm.executeQuery();

            while (sqlResult.next()) {
                emp.add(Employee.fromResultSet(sqlResult));
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

        return emp;
    }

    @Override
    public boolean updateEmployee(final Employee employee) {
        connection = SQLExecuter.getConnection();
        boolean success = false;
        try {
            Statement stm = connection.createStatement();
            stm.execute("CALL employees_update_by_id("
                    + employee.getId()
                    + ",'"
                    + employee.getLast_name()
                    + "','"
                    + employee.getFirst_name()
                    + "',"
                    + employee.isProject_leader()
                    + ","
                    + employee.getDaily_rate()
                    + ","
                    + employee.getTime_preference()
                    + ",'"
                    + (employee.changePassword() ? employee.getPassword()
                            : "null") + "','" + MySqlConnect.getId() + "','"
                    + MySqlConnect.getHost() + "')");
            success = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }

    @Override
    public void deleteEmployee(final int id) {
        connection = SQLExecuter.getConnection();
        try {
            Statement stm = connection.createStatement();
            stm.execute("CALL employees_delete_by_id(" + id + ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Employee getEmployee(final int id) {
        connection = SQLExecuter.getConnection();

        Employee emp = null;
        ResultSet sqlResult = null;

        PreparedStatement stm = null;
        final String storedProcedure = "CALL employees_select_by_id(?)";

        try {
            stm = connection.prepareStatement(storedProcedure);
            stm.setInt(1, id);

            sqlResult = stm.executeQuery();

            if (sqlResult.next()) {
                emp = Employee.fromResultSet(sqlResult);
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

        return emp;
    }
}
