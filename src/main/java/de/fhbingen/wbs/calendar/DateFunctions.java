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

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Contains methods to calculate the date.
 */
public final class DateFunctions {
    /**
     * Maximum hour of day.
     */
    public static final int MAX_HOUR_OF_DAY = 23;
    /**
     * Maximum minute or second of hour or minute.
     */
    public static final int MAX_MINUTE_SECOND_OF_HOUR = 59;
    /**
     * Value to divide milliseconds by and get hours.
     */
    public static final int FACTOR_MILLISECONDS_TO_HOURS = 3600000;
    /**
     * Date Format used throughout the application.
     */
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
            "dd.MM.yyyy HH:mm");

    /**
     * Prevent instantiation.
     */
    private DateFunctions() {

    }

    /**
     * Convert a Java date into a SQL date. Format: #MM/DD/YYYY HH:MM:SS#
     * @param d
     *            date which has to be converted.
     * @return The converted date as String.
     */
    public static String getDateString(final Date d) {
        if (d != null) {
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            int month = c.get(Calendar.MONTH) + 1;
            return "#" + month + "/" + c.get(Calendar.DAY_OF_MONTH) + "/"
                + c.get(Calendar.YEAR) + " " + c.get(Calendar.HOUR_OF_DAY)
                + ":" + c.get(Calendar.MINUTE) + ":00#";
        } else {
            return "Null"; //NON-NLS
        }
    }

    /**
     * Convert a Java date into a SQL date. Format: #MM/DD/YYYY#
     * @param d
     *            date which has to convert.
     * @return The converted date as String.
     */
    public static String getDateStringOnlyDay(final Date d) {
        if (d != null) {
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            int month = c.get(Calendar.MONTH) + 1;
            return "#" + month + "/" + c.get(Calendar.DAY_OF_MONTH) + "/"
                + c.get(Calendar.YEAR) + "#";
        } else {
            return "Null"; //NON-NLS
        }
    }

    /**
     * Compares two dates. Two dates are equal if they are identical up to
     * a minute.
     * @param d1
     *            The first date.
     * @param d2
     *            The second date which has to compare with the first one.
     * @return True: If both dates are identical up to a minute. False:
     *         Else.
     */
    public static boolean equalsDate(final Date d1, final Date d2) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(d2);
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
            && c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)
            && c1.get(Calendar.HOUR_OF_DAY) == c2.get(Calendar.HOUR_OF_DAY)
            && c1.get(Calendar.MINUTE) == c2.get(Calendar.MINUTE);
    }

    /**
     * Generates a Timestamp or Null from a Date. *
     * @param date
     *            a Date for which a Timestamp is returned.
     * @return A Timestamp or null, if the date was null.
     */
    public static Timestamp getTimesampOrNull(final Date date) {
        if (date == null) {
            return null;
        }
        return new Timestamp(date.getTime());
    }


    /**
     * calculates the Distance between two dates expressed in workdays
     * @param d1
     *             first Date
     * @param d2
     *             second Date
     * @return Number of Workdays
     *
     */
    public static int getWorkdayDistanceBetweenDates(Date d1, Date d2){
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(d1);

        Calendar endCal = Calendar.getInstance();
        endCal.setTime(d2);

        int workDays = 0;

        //Return 0 if start and end are the same
        if (startCal.getTimeInMillis() == endCal.getTimeInMillis()) {
            return 0;
        }

        if (startCal.getTimeInMillis() > endCal.getTimeInMillis()) {
            startCal.setTime(d2);
            endCal.setTime(d1);
        }

        do {
            if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                ++workDays;
            }
            startCal.add(Calendar.DAY_OF_MONTH, 1);
        } while (startCal.getTimeInMillis() < endCal.getTimeInMillis()); //excluding end date

        return workDays;
    }


    /**
     * @param date
     *          Date to add an amount of Workdays
     * @param workdays
     *          Number of Workdays to be added on the Date
     * @return calculated Date
     */
    public static Date calcDateByOffset(Date date, int workdays){

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        do {


            if (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                --workdays;
            }
            cal.add(Calendar.DAY_OF_MONTH, 1);
        } while (workdays > 0);

        return cal.getTime();
    }



}
