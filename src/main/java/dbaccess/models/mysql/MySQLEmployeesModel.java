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
     * @param con The MySQL connection to use.
     */
    public MySQLEmployeesModel(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addNewEmployee(Employee employee) {
        try {
            Statement stm = connection.createStatement();
            stm.execute("INSERT INTO employees VALUES (" + employee.getId()
                    + ",'" + employee.getLogin() + "','"
                    + employee.getLast_name() + "','"
                    + employee.getFirst_name() + "',"
                    + employee.isProject_leader() + ",'"
                    + employee.getPassword() + "'," + employee.getDaily_rate()
                    + "," + employee.getTime_preference() + ")");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
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
            result = stm.executeQuery("SELECT * FROM employee");

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
            result = stm.executeQuery("SELECT * FROM employee WHERE login = "
                    + login);

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
        List<Employee> empList = new ArrayList<Employee>();
        try {
            ResultSet result = null;
            Employee employee = null;
            Statement stm = connection.createStatement();
            result = stm
                    .executeQuery("SELECT * FROM employee WHERE project_leader = "
                            + isLeader);

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
        try {
            Statement stm = connection.createStatement();
            stm.execute("UPDATE employees SET login = '" + employee.getLogin()
                    + "', last_name = '" + employee.getLast_name()
                    + "', first_name = '" + employee.getFirst_name()
                    + "', project_leader = " + employee.isProject_leader()
                    + ", password = '" + employee.getPassword()
                    + "', daily_rate = " + employee.getDaily_rate()
                    + ", time_preference = " + employee.getTime_preference()
                    + " WHERE id = " + employee.getId());
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void deleteEmployee(int id) {
        try {
            Statement stm = connection.createStatement();
            stm.execute("DELETE * FROM employee WHERE id = " + id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
