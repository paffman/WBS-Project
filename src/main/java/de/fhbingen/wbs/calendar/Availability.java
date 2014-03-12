/*
 * The WBS-Tool is a project management tool combining the Work Breakdown
 * Structure and Earned Value Analysis Copyright (C) 2013 FH-Bingen This
 * program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your
 * option) any later version. This program is distributed in the hope that
 * it will be useful, but WITHOUT ANY WARRANTY;; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with this program. If not,
 * see <http://www.gnu.org/licenses/>.
 */

package de.fhbingen.wbs.calendar;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Represents the availability/ non-availability of a employee or the
 * project.
 */
public class Availability implements Comparable<Availability> {

    /**
     *  The id of the employee.
     */
    private int userID;

    /** The description why the employee is not available. */
    private String description;

    /**
     * True: If an employee is not available the whole day. False: If an
     * employee is not available only a few hours.
     */
    private boolean allDay;

    /**
     * True: The employee is available. False: The employee is not
     * available.
     */
    private boolean availabe;

    /** The start date of the availability. */
    private Date startDate;

    /** The end date of the availability. */
    private Date endDate;

    /** Identifier for the availability. */
    private int id;

    /**
     * Constructor.
     * @param userID
     *            The id of the employee.
     * @param allDay
     *            True: If an employee is not available the whole day.
     *            False: If an employee is not available only a few hours.
     * @param available
     *            True: The employee is available. False: The employee is
     *            not available.
     * @param startDate
     *            The start date of the availability.
     * @param endDate
     *            The end date of the availability.
     */
    public Availability(final int userID, final boolean allDay,
        final boolean available, final Date startDate, final Date endDate) {
        this(userID, allDay, available, startDate, endDate, "",
            Integer.MIN_VALUE);
    }

    /**
     * Constructor.
     * @param userID
     *            The id of the employee.
     * @param allDay
     *            True: If an employee is not available the whole day.
     *            False: If an employee is not available only a few hours.
     * @param available
     *            True: The employee is available. False: The employee is
     *            not available.
     * @param startDate
     *            The start date of the availability.
     * @param endDate
     *            The end date of the availability.
     * @param description
     *            The description why the employee is not available.
     */
    public Availability(final int userID, final boolean allDay,
        final boolean available, final Date startDate, final Date endDate,
        final String description) {
        this(userID, allDay, available, startDate, endDate, description,
            Integer.MIN_VALUE);
    }

    /**
     * Constructor.
     * @param userID
     *            The id of the employee.
     * @param allDay
     *            True: If an employee is not available the whole day.
     *            False: If an employee is not available only a few hours.
     * @param available
     *            True: The employee is available. False: The employee is
     *            not available.
     * @param startDate
     *            The start date of the availability.
     * @param endDate
     *            The end date of the availability.
     * @param id
     *            The identifier of the availability.
     */
    public Availability(final int userID, final boolean allDay,
        final boolean available, final Date startDate, final Date endDate,
        final int id) {
        this(userID, allDay, available, startDate, endDate, "", id);
    }

    /**
     * Constructor.
     * @param userID
     *            The id of the employee.
     * @param allDay
     *            True: If an employee is not available the whole day.
     *            False: If an employee is not available only a few hours.
     * @param available
     *            True: The employee is available. False: The employee is
     *            not available.
     * @param startDate
     *            The start date of the availability.
     * @param endDate
     *            The end date of the availability.
     * @param description
     *            The description why the employee is not available.
     * @param id
     *            The identifier of the availability.
     */
    public Availability(final int userID, final boolean allDay,
        final boolean available, final Date startDate, final Date endDate,
        final String description, final int id) {
        this.userID = userID;
        this.description = description;
        this.allDay = allDay;
        this.availabe = available;

        this.startDate = calcStart(startDate);
        this.endDate = calcEnd(endDate);

        this.setId(id);
    }

    /**
     * Calculates the start date. The begin of the day is: 00:00:00.
     * @param date
     *            The date with a specific time.
     * @return The start date (without the specific time).
     */
    private Date calcStart(final Date date) {
        if (allDay) {
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(date);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            return cal.getTime();
        } else {
            return date;
        }
    }

    /**
     * Calculates the end date. The end of the day is: 23:59:59.
     * @param date
     *            The date which has to be calculated.
     * @return The calculated date.
     */
    private Date calcEnd(final Date date) {
        if (allDay) {
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(date);
            cal.set(Calendar.HOUR_OF_DAY, DateFunctions.MAX_HOUR_OF_DAY);
            cal.set(Calendar.MINUTE, DateFunctions.MAX_MINUTE_SECOND_OF_HOUR);
            cal.set(Calendar.SECOND, DateFunctions.MAX_MINUTE_SECOND_OF_HOUR);
            cal.set(Calendar.MILLISECOND, 0);
            return cal.getTime();
        } else {
            return date;
        }

    }

    /**
     * Returns the id from the employee.
     * @return The employees id.
     */
    public final int getUserID() {
        return userID;
    }

    /**
     * Adds a employee to an availability.
     * @param newUserID
     *            The id from the employee.
     */
    public final void setUserID(final int newUserID) {
        this.userID = newUserID;
    }

    /**
     * Returns the availability of the employee.
     * @return True: If the employee is available the whole day. False: If
     *         the employee is available only a few hours.
     */
    public final boolean isAllDay() {
        return allDay;
    }

    /**
     * Returns if the employee is available or is not available.
     * @return True: Available. False: Not available.
     */
    public final boolean isAvailabe() {
        return availabe;
    }

    /**
     * Sets the availability of the employee.
     * @param isAvailable
     *            True: The employee is available. False: The employee is
     *            not available.
     */
    public final void setAvailabe(final boolean isAvailable) {
        this.availabe = isAvailable;
    }

    /**
     * Returns the begin of the available inclusive time.
     * @return The begin of the available.
     */
    public final Date getStartTime() {
        return startDate;
    }

    /**
     * Sets the begin of the availability inclusive time.
     * @param newStartDate
     *            The start date.
     */
    public final void setStartTime(final Date newStartDate) {
        this.startDate = calcStart(newStartDate);
    }

    /**
     * Sets the end of the availability inclusive time.
     * @param newEndDate
     *            The end date.
     */
    public final void setEndTime(final Date newEndDate) {
        this.endDate = calcEnd(newEndDate);
    }

    /**
     * Returns the end of the availability inclusive time.
     * @return The end of the availability.
     */
    public final Date getEndTime() {
        return endDate;
    }

    /**
     * Returns the description of the availability.
     * @return The description of the availability.
     */
    public final String getDescription() {
        return description;
    }

    /**
     * Sets the description of the availability.
     * @param newDescription
     *            The description of the availability.
     */
    public final void setDescription(final String newDescription) {
        this.description = newDescription;
    }

    /**
     * Returns the identifier of the availability.
     * @return The id of the availability.
     */
    public final int getId() {
        return id;
    }

    /**
     * Sets the identifier of the availability.
     * @param newId
     *            The id of the availability.
     */
    public final void setId(final Integer newId) {
        this.id = newId;
    }

    /**
     * Returns the start date of the availability.
     * @return The day on which the availability starts.
     */
    public final Day getStartDay() {
        return new Day(startDate);
    }

    /**
     * Returns the end date of the availability.
     * @return The day on which the availability ends.
     */
    public final Day getEndDay() {
        return new Day(endDate);
    }

    /**
     * Returns the duration of the availability.
     * @return The duration in hours.
     */
    public final int getDuration() {
        long difference = this.getEndTime().getTime()
            - this.getStartTime().getTime();
        return (int) (difference / DateFunctions.FACTOR_MILLISECONDS_TO_HOURS);
    }

    @Override
    public final String toString() {
        return "UserId: " + userID + " description: " + description //NON-NLS
            + " allDay: " + allDay + " available: " + availabe //NON-NLS
            + " startDate: " + startDate + " endDate: " + endDate; //NON-NLS
    }

    @Override
    public final int compareTo(final Availability other) {

        if (this.startDate.compareTo(other.startDate) != 0) {
            return this.startDate.compareTo(other.startDate);
        } else {
            return this.endDate.compareTo(other.endDate);
        }

    }
}
