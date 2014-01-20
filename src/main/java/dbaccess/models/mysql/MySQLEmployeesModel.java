/*
 * The WBS-Â­Tool is a project managment tool combining the Work Breakdown
 * Structure and Earned Value Analysis
 * Copyright (C) 2013 FH-Â­Bingen
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY;Í¾ without even the implied warranty of
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

    /**
     * Constructor.
     * 
     * @param con
     *            The MySQL connection to use.
     */
    public MySQLEmployeesModel(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addNewEmployee(Employee employee) {
        // TODO: siehe db_name und "testhost"... wie wird das übergeben????
        // passwort wird zudem nicht richtig gesetzt
        try {
            Statement stm = connection.createStatement();
            stm.execute("CALL employees_new('" + employee.getLogin() + "','"
                    + employee.getLast_name() + "','"
                    + employee.getFirst_name() + "',"
                    + employee.isProject_leader() + ","
                    + employee.getDaily_rate() + ","
                    + employee.getTime_preference() + ",'"
                    + employee.getPassword()
                    + "','wbs_unittest_db','testhost')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Employee> getEmployee() {
        List<Employee> empList = new ArrayList<Employee>();
        try {
            ResultSet result = null;
            Employee employee = null;
            Statement stm = connection.createStatement();
            result = stm.executeQuery("CALL employees_select()");

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
        Employee employee = null;
        try {
            ResultSet result = null;
            Statement stm = connection.createStatement();
            result = stm.executeQuery("CALL employees_select_by_login ("
                    + login + ")");

            if (result.next()) {
                employee = Employee.fromResultSet(result);
            }

            return employee;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // TODO: die procedure macht nicht das was ich erwartet hätte...
    @Override
    public List<Employee> getEmployee(boolean isLeader) {
        List<Employee> empList = new ArrayList<Employee>();
        try {
            ResultSet result = null;
            Employee employee = null;
            Statement stm = connection.createStatement();
            result = stm.executeQuery("CALL employees_select (" + isLeader
                    + ")");

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

    //TODO: siehe wie oben: was ist mit db_name????
    @Override
    public void updateEmployee(Employee employee) {
        try {
            Statement stm = connection.createStatement();
            stm.execute("CALL employees_update_by_id(" + employee.getId() + ",'"
                    + employee.getLogin() + "','"
                    + employee.getLast_name() + "','"
                    + employee.getFirst_name() + "',"
                    + employee.isProject_leader() + ","
                    + employee.getDaily_rate() + ","
                    + employee.getTime_preference() + ",'" + employee.getPassword() + "','dbname')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteEmployee(int id) {
        try {
            Statement stm = connection.createStatement();
            stm.execute("CALL employees_delete_by_id(" + id + ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
