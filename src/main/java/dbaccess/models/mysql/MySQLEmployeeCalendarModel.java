package dbaccess.models.mysql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dbaccess.data.EmployeeCalendar;
import dbaccess.models.EmployeeCalendarModel;

public class MySQLEmployeeCalendarModel implements EmployeeCalendarModel {

    private Connection connection;

    public MySQLEmployeeCalendarModel(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addNewEmployeeCalendar(EmployeeCalendar empCal) {
        try {
            Statement stm = connection.createStatement();
            stm.execute("INSERT INTO employee_calendar VALUES (" + empCal.getId()
                    + "," + empCal.getFid_emp() + ",'" + empCal.getBegin_time()
                    + "','" + empCal.getEnd_time() + "','"
                    + empCal.getDescription() + "'," + empCal.isAvailability()
                    + "," + empCal.isFull_time() + ")");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public List<EmployeeCalendar> getEmployeeCalendar() {
        List<EmployeeCalendar> empCalList = new ArrayList<EmployeeCalendar>();
        try {
            ResultSet result = null;
            EmployeeCalendar employeeCalendar = null;
            Statement stm = connection.createStatement();
            result = stm.executeQuery("SELECT * FROM employee_calendar");

            while (result.next()) {
                employeeCalendar = new EmployeeCalendar(result.getInt(1),
                        result.getInt(2), result.getDate(3),
                        result.getDate(4), result.getString(5),
                        result.getBoolean(6), result.getBoolean(7));
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
        EmployeeCalendar employeeCalendar = null;
        try {
            ResultSet result = null;
            Statement stm = connection.createStatement();
            result = stm
                    .executeQuery("SELECT * FROM employee_calendar WHERE id = "
                            + id);

            if (result.next()) {
                employeeCalendar = new EmployeeCalendar(result.getInt(1),
                        result.getInt(2), result.getDate(3),
                        result.getDate(4), result.getString(5),
                        result.getBoolean(6), result.getBoolean(7));
            }

            return employeeCalendar;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<EmployeeCalendar> getEmployeeCalendarForFID(int fid) {
        List<EmployeeCalendar> empCalList = new ArrayList<EmployeeCalendar>();
        try {
            ResultSet result = null;
            EmployeeCalendar employeeCalendar = null;
            Statement stm = connection.createStatement();
            result = stm
                    .executeQuery("SELECT * FROM employee_calendar WHERE fid_emp = "
                            + fid);

            while (result.next()) {
                employeeCalendar = new EmployeeCalendar(result.getInt(1),
                        result.getInt(2), result.getDate(3),
                        result.getDate(4), result.getString(5),
                        result.getBoolean(6), result.getBoolean(7));
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
        List<EmployeeCalendar> empCalList = new ArrayList<EmployeeCalendar>();
        try {
            ResultSet result = null;
            EmployeeCalendar employeeCalendar = null;
            Statement stm = connection.createStatement();
            result = stm
                    .executeQuery("SELECT * FROM employee_calendar WHERE begin_time > "
                            + from + " AND end_time < " + to);

            while (result.next()) {
                employeeCalendar = new EmployeeCalendar(result.getInt(1),
                        result.getInt(2), result.getDate(3),
                        result.getDate(4), result.getString(5),
                        result.getBoolean(6), result.getBoolean(7));
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
            Date to, boolean mode2) {
        // TODO was macht es?
        return null;
    }

    @Override
    public void deleteEmployeeCalendar(int id) {
        try {
            Statement stm = connection.createStatement();
            stm.execute("DELETE * FROM employee_calendar WHERE id = " + id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
