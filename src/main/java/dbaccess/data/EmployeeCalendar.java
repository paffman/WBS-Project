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
import java.util.Date;

/**
 * A simple container class representing a EmployeeCalendar. It mirrors all the
 * employee calendar fields in the employee_calendar table.
 */
public class EmployeeCalendar {
    
    /** Unique Identifier for row in the calendar. */
    private int id;
    
    /** Id of the referenced employee. */
    private int fid_emp;
    
    /** Begin of the event in the calendar entry. */
    private Date begin_time;
    
    /** End of the event in the calendar entry. */
    private Date end_time;
    
    /** Description of the event. */
    private String description;
    
    /** Availability of the employee during the stated time. */
    private boolean availability;
    
    /** Information if the time has to be considered or only the date is relevant. */
    private boolean full_time;

    /**
     * Creates a <code>EmployeeCalendar</code> based on a <code>ResultSet</code>.
     *
     * @param resSet The result set containing the data
     * @return A <code>EmployeeCalendar</code> object
     */
    public static final EmployeeCalendar fromResultSet(ResultSet resSet){
        EmployeeCalendar ec=new EmployeeCalendar();

        try {
            ec.setId(resSet.getInt("id"));
            ec.setFid_emp(resSet.getInt("fid_emp"));
            ec.setBegin_time(resSet.getDate("begin_time"));
            ec.setEnd_time(resSet.getDate("end_time"));
            ec.setDescription(resSet.getString("description"));
            ec.setAvailability(resSet.getBoolean("availability"));
            ec.setFull_time(resSet.getBoolean("full_time"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return ec;
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
     * Sets the id.
     *
     * @param id The employee calendars id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the referenced employee.
     *
     * @return The employees id
     */
    public int getFid_emp() {
        return fid_emp;
    }

    /**
     * Sets the referenced employee.
     *
     * @param fid_emp The employees id
     */
    public void setFid_emp(int fid_emp) {
        this.fid_emp = fid_emp;
    }

    /**
     * Returns the begin time of the calendar.
     *
     * @return The date of the begin time
     */
    public Date getBegin_time() {
        return begin_time;
    }

    /**
     * Sets the begin time of the calendar.
     *
     * @param begin_time The begin time
     */
    public void setBegin_time(Date begin_time) {
        this.begin_time = begin_time;
    }

    /**
     * Returns the end time of the calendar.
     *
     * @return The date of the end time
     */
    public Date getEnd_time() {
        return end_time;
    }

    /**
     * Sets the end time of the calendar.
     *
     * @param end_time The end time
     */
    public void setEnd_time(Date end_time) {
        this.end_time = end_time;
    }

    /**
     * Returns the description of the calendar.
     *
     * @return The calendars description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the employee calendar.
     *
     * @param description The description of the calendar
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the availability.
     *
     * @return true, if available. Else false
     */
    public boolean isAvailability() {
        return availability;
    }

    /**
     * Sets the availability of the employee.
     *
     * @param availability information if the employee is available or not
     */
    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    /**
     * Returns the information if the time has to be considered or only the date is relevant
     *
     * @return true, if full time available. Else false
     */
    public boolean isFull_time() {
        return full_time;
    }

    /**
     * Sets the information if the time has to be considered or only the date is relevant
     *
     * @param full_time The information if the employee is full time available or not
     */
    public void setFull_time(boolean full_time) {
        this.full_time = full_time;
    }

    /**
     * Convert the employee calendar to a String
     *
     * @return the employee calendar as String
     */
    public String toString(){
        return id+" "+fid_emp+" "+begin_time+" "+end_time+" "+description+" "+availability+" "+full_time;
    }
}
