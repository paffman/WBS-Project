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

package dbaccess.models;

import java.util.Date;
import java.util.List;

import dbaccess.data.EmployeeCalendar;

/** The interface for the employee calendar model. */
public interface EmployeeCalendarModel {

    /**
     * A method to add a new employee calendar to the project.
     * 
     * @param empCal
     *            The calendar which is added to the project.
     */
    void addNewEmployeeCalendar(EmployeeCalendar empCal);

    /**
     * A method to get the employee calendars from all employees.
     * 
     * @return Returns a list with a employee calendar for each employee.
     */
    List<EmployeeCalendar> getEmployeeCalendar();

    /**
     * A method to get a specific calendar.
     * 
     * @param id
     *            The id from the specific calendar
     * @return Returns the selected employee calendar.
     */
    EmployeeCalendar getEmployeeCalendar(int id);

    /**
     * A method to get the calendar from an employee.
     * 
     * @param fid
     *            The id of the referenced employee.
     * @return Returns a list with the calendar of the selected employee.
     */
    List<EmployeeCalendar> getEmployeeCalendarForFID(int fid);

    /**
     * A method to get the employee calendars in a specific period.
     * 
     * @param from
     *            The start date from the period.
     * @param to
     *            The end date from the period
     * @return Returns a list with all calendars in the specific period.
     */
    List<EmployeeCalendar> getEmployeeCalendarInDateRange(Date from, Date to);

    /**
     * TODO: Was macht mode2? wo ist der unterschied zur obigen methode? A
     * method to get the employee calendars in a specific period.
     * 
     * @param from
     *            The start date from the period.
     * @param to
     *            The end date from the period
     * @param empId
     *            ID of the employee for which the calendar is selected.
     * @return Returns a list with all calendars in the specific period.
     */
    List<EmployeeCalendar> getEmployeeCalendarInDateRange(Date from, Date to,
            int empId);

    /**
     * A method to delete e a specific calendar.
     * 
     * @param id
     *            The id from the calendar which is deleted from the project.
     * @return Success of delete action.
     */
    boolean deleteEmployeeCalendar(int id);

    /**
     * Updates a specific calendar entry.
     * 
     * @param empCal
     *            The Entry to update.
     */
    void updateEmployeeCalendar(EmployeeCalendar empCal);

}
