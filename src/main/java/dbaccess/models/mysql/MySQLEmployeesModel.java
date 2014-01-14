package dbaccess.models.mysql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dbaccess.data.Employee;
import dbaccess.models.EmployeesModel;

public class MySQLEmployeesModel implements EmployeesModel {

    private Connection connection;

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
                employee = new Employee(result.getInt(1), result.getString(2),
                        result.getString(3), result.getString(4),
                        result.getBoolean(5), result.getString(6),
                        result.getDouble(7), result.getInt(8));
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
                employee = new Employee(result.getInt(1), result.getString(2),
                        result.getString(3), result.getString(4),
                        result.getBoolean(5), result.getString(6),
                        result.getDouble(7), result.getInt(8));
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
                employee = new Employee(result.getInt(1), result.getString(2),
                        result.getString(3), result.getString(4),
                        result.getBoolean(5), result.getString(6),
                        result.getDouble(7), result.getInt(8));
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
