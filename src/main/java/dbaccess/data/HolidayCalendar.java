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

package dbaccess.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * A simple container class representing a HolidayCalendar. It mirrors all the
 * holiday calendar fields in the holidays_calendar table.
 */
public class HolidayCalendar {

	/** Unique Identifier for row in the calendar. */
	private int id;

	/** Id of the referenced employee. */
	private String title;

	/** Begin of the event in the calendar entry. */
	private Date begin_time;

	/** End of the event in the calendar entry. */
	private Date end_time;

	/** Availability of the employee during the stated time. */
	private boolean availability;

	/**
	 * Information if the time has to be considered or only the date is
	 * relevant.
	 */
	private boolean full_time;

	/**
	 * Creates a <code>HolidayCalendar</code> based on a <code>ResultSet</code>.
	 * 
	 * @param resSet
	 *            The result set containing the data
	 * @return A <code>HolidayCalendar</code> object
	 */
	public static final HolidayCalendar fromResultSet(ResultSet resSet) {
		HolidayCalendar hc = new HolidayCalendar();

		try {
			hc.setId(resSet.getInt("id"));
			hc.setTitle(resSet.getString("title"));
			hc.setBegin_time(resSet.getDate("begin_time"));
			hc.setEnd_time(resSet.getDate("end_time"));
			hc.setAvailability(resSet.getBoolean("availability"));
			hc.setFull_time(resSet.getBoolean("full_time"));
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return hc;
	}

	/**
	 * @return the id
	 */
	public final int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	private final void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the title
	 */
	public final String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public final void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the begin_time
	 */
	public final Date getBegin_time() {
		return begin_time;
	}

	/**
	 * @param begin_time the begin_time to set
	 */
	public final void setBegin_time(Date begin_time) {
		this.begin_time = begin_time;
	}

	/**
	 * @return the end_time
	 */
	public final Date getEnd_time() {
		return end_time;
	}

	/**
	 * @param end_time the end_time to set
	 */
	public final void setEnd_time(Date end_time) {
		this.end_time = end_time;
	}

	/**
	 * @return the availability
	 */
	public final boolean isAvailability() {
		return availability;
	}

	/**
	 * @param availability the availability to set
	 */
	public final void setAvailability(boolean availability) {
		this.availability = availability;
	}

	/**
	 * @return the full_time
	 */
	public final boolean isFull_time() {
		return full_time;
	}

	/**
	 * @param full_time the full_time to set
	 */
	public final void setFull_time(boolean full_time) {
		this.full_time = full_time;
	}
}
