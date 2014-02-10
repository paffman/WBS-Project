/*
 * The WBS-Tool is a project management tool combining the Work Breakdown
 * Structure and Earned Value Analysis
 * Copyright (C) 2013 FH-Bingen
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

import dbaccess.data.HolidayCalendar;

/** The interface for the holidays calendar model. */
public interface HolidaysCalendarModel {

    /**
     * A method to add a new holiday calendar to the project.
     * 
     * @param holCal
     *            The calendar which is added to the project.
     */
    void addNewHolidayCalendar(HolidayCalendar holCal);

    /**
     * A method to get the holiday calendars.
     * 
     * @return Returns a list with all holiday calendars entries.
     */
    List<HolidayCalendar> getHolidayCalendar();

    /**
     * A method to get a specific holiday calendar.
     * 
     * @param calID
     *            The id from the calendar.
     * @return Returns the selected holiday calendar
     */
    HolidayCalendar getHolidayCalendar(int calID);

    /**
     * A method to get the holiday calendar from a specific period.
     * 
     * @param from
     *            The begin of the holiday calendar.
     * @param to
     *            The end of the holiday calendar.
     * @param mode2
     *            If mode2 is true the executed query has one where option more.
     *            See documentation for details.
     * @return Returns a list with the specific holiday calendar.
     */
    List<HolidayCalendar> getHolidayCalendar(Date from, Date to, boolean mode2);

    /**
     * A method to update the holiday calendar.
     * 
     * @param hc
     *            The calendar which is updated.
     */
    void updateHolidayCalendar(HolidayCalendar hc);

    /**
     * A method to delete a holiday calendar.
     * 
     * @param calID
     *            The id from the calendar which is deleted.
     * @return success of delete action.
     */
    boolean deleteHolidayCalendar(int calID);
}
