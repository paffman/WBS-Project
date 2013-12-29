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

package interfaces;

import java.util.Date;
import java.util.List;

/*The interface for the employee calendar model*/
public interface EmployeeCalendarModel {

    /*
     * TODO: ist das richtig?
     * A method to get all employee calendars.
     * @return Returns a list with all employee calendars.
     */
    public List<?> getEmployeeCalendar();
    
    /*
     * A method to get a specific calendar.
     * @param id The id from the specific calendar
     * @return Returns the selected employee calendar.
     */
    public EmployeeCalendar getEmployeeCalendar(int id);
    
    /*
     * TODO: ist das richtig?
     * A method to get the calendar from a employee.
     * @param fid The id of the referenced employee.
     * @return Returns a list with the calendar of the selected employee.
     */
    public List<?> getEmployeeCalendarForFID(int fid);
    
    /*
     * A method to get the employee calendars in a specific period.
     * @param from The start date from the period.
     * @param to The end date from the period
     * @return Returns a list with all calendars in the specific period.
     */
    public List<?> getEmployeeCalendarInDateRange(Date from, Date to);
    
    /*
     * TODO: Was macht mode2? wo ist der unterschied zur obigen methode?
     * A method to get the employee calendars in a specific period.
     * @param from The start date from the period.
     * @param to The end date from the period
     * @return Returns a list with all calendars in the specific period.
     */
    public List<?> getEmployeeCalendarInDateRange(Date from, Date to, boolean mode2);

    /*
     * A method to delete e a specific calendar.
     * @param id The id from the calendar which has to be deleted.
     */
    public void deleteEmployeeCalendar(int id);
    
}
