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
 * Represents a day (without the time).
 */
public class Day extends Date {

    /**
     * Constructor.
     * @param date
     *            Date to which the day is needed.
     */
    public Day(final Date date) {
        this(date, false);
    }

    /**
     * Constructor.
     * @param date
     *            Date to which the day is needed.
     * @param endOfDay
     *            If true, the created date has the end time 23:59:59.
     */
    public Day(final Date date, final boolean endOfDay) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        if (endOfDay) {
            cal.add(Calendar.HOUR, DateFunctions.MAX_HOUR_OF_DAY);
            cal.add(Calendar.MINUTE, DateFunctions.MAX_MINUTE_SECOND_OF_HOUR);
            cal.add(Calendar.SECOND, DateFunctions.MAX_MINUTE_SECOND_OF_HOUR);
        }
        super.setTime(cal.getTimeInMillis());
    }
}
