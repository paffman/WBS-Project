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

package dbaccess.data;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A simple container class representing a Employee. It mirrors all the
 * employee fields in the employee table.
 */
public class Employee {
    
    /** baseline id. */
    private int id;
    
    /** Unique name for employee, used to login. */
    private String login;

    /** The last name of the employee. */
    private String last_name;
    
    /** The first name of the employee. */
    private String first_name;
    
    /** Information if the employee has project leader rights */
    private boolean project_leader;
    
    /** Password used to login. Encryption is handled in the Application. */
    private char[] password;
    
    /** The daily wage of the employee, needed for earned value analysis. */
    private double daily_rate;
    
    /** Preference of the employee to use and display times in minutes, hours or days. */
    private int time_preference;

    /** Tag if password was set for change.*/
    private boolean setPassword = false;
    
    /**
     * Creates a <code>Employee</code> based on a <code>ResultSet</code>.
     *
     * @param resSet The result set containing the data
     * @return A <code>Employee</code> object
     */
    public static final Employee fromResultSet(ResultSet resSet){
        Employee ep=new Employee();

        try {
            ep.setId(resSet.getInt("id"));
            ep.setLogin(resSet.getString("login"));
            ep.setLast_name(resSet.getString("last_name"));
            ep.setFirst_name(resSet.getString("first_name"));
            ep.setProject_leader(resSet.getBoolean("project_leader"));
            ep.setDaily_rate(resSet.getDouble("daily_rate"));
            ep.setTime_preference(resSet.getInt("time_preference"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return ep;
    }

    /**
     * Returns the ID.
     *
     * @return The ID
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id of the employee.
     *
     * @param id The id of the employee
     */
    private void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the login from the employee.
     *
     * @return The employees login
     */
    public String getLogin() {
        return login;
    }

    /**
     * Sets the login of the employee.
     *
     * @param login The login of the employee
     */
    public void setLogin(String login) {
        this.login = login;
    }
    
    /**
     * Returns the last name from the employee.
     *
     * @return The employees last name
     */
    public String getLast_name() {
        return last_name;
    }

    /**
     * Sets the last name of the employee.
     *
     * @param last_name The employees last name
     */
    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    /**
     * Returns the first name of the employee.
     *
     * @return The employees first name
     */
    public String getFirst_name() {
        return first_name;
    }

    /**
     * Sets the first name of the employee.
     *
     * @param first_name The employees first name
     */
    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    /**
     * Returns information if the employee is project leader or not
     *
     * @return true, if the employee is a project leader, else false
     */
    public boolean isProject_leader() {
        return project_leader;
    }

    /**
     * Sets the information if the employee is a project leader or not.
     *
     * @param project_leader true, if the employee should be a project leader, else false
     */
    public void setProject_leader(boolean project_leader) {
        this.project_leader = project_leader;
    }

    /**
     * Returns the password from the employee.
     *
     * @return The employees password
     */
    public char[] getPassword() {
        return password;
    }

    /**
     * Sets the password of the employees login.
     *
     * @param password The employees password
     */
    public void setPassword(char[] password) {
        this.password = password;
        setPassword = true;
    }

    /**
     * Returns the daily rate of the employee.
     *
     * @return The employees daily rate
     */
    public double getDaily_rate() {
        return daily_rate;
    }

    /**
     * Sets the daily rate of the employee.
     *
     * @param daily_rate The employees daily rate
     */
    public void setDaily_rate(double daily_rate) {
        this.daily_rate = daily_rate;
    }
    
    /**
     * Returns the time preference.
     *
     * @return The time preference
     */
    public int getTime_preference() {
        return time_preference;
    }

    /**
     * Sets the time preference. //TODO: welcher wert, welche einstellung?
     *
     * @param time_preference The wished time_preference
     */
    public void setTime_preference(int time_preference) {
        this.time_preference = time_preference;
    }
    
    /**
     * @return True if the password was set to change.
     */
    public final boolean changePassword(){
        return setPassword;
    }

    /**
     * Convert the employee to a String
     *
     * @return the employee as String
     */
    public String toString() {
        return id + " " + login + " " + last_name + " " + first_name + " "
                + project_leader + " " + password + " " + daily_rate + " "
                + time_preference;
    }
}
