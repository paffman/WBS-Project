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
 * A simple container class representing a work effort. It mirrors all the
 * database fields in the work_effort table.
 */
public class WorkEffort {
	
	/** Unique id of the effort. */
	private int id;
	
	/** Foreign key of the affected workpackage. */
	private int fid_wp;
	
	/** Foreign key of the employee.*/
	private int fid_emp;
	
	/** Date of the work effort.*/
	private Date rec_date;
	
	/** Effort in 8h days. */
	private double effort;
	
	/** Description of the effort. */
	private String description;

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
	 * @return the fid_wp
	 */
	public final int getFid_wp() {
		return fid_wp;
	}

	/**
	 * @param fid_wp the fid_wp to set
	 */
	public final void setFid_wp(int fid_wp) {
		this.fid_wp = fid_wp;
	}

	/**
	 * @return the fid_emp
	 */
	public final int getFid_emp() {
		return fid_emp;
	}

	/**
	 * @param fid_emp the fid_emp to set
	 */
	public final void setFid_emp(int fid_emp) {
		this.fid_emp = fid_emp;
	}

	/**
	 * @return the rec_date
	 */
	public final Date getRec_date() {
		return rec_date;
	}

	/**
	 * @param rec_date the rec_date to set
	 */
	public final void setRec_date(Date rec_date) {
		this.rec_date = rec_date;
	}

	/**
	 * @return the effort
	 */
	public final double getEffort() {
		return effort;
	}

	/**
	 * @param effort the effort to set
	 */
	public final void setEffort(double effort) {
		this.effort = effort;
	}

	/**
	 * @return the description
	 */
	public final String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public final void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Creates a <code>WorkEffort</code> based on a <code>ResultSet</code>.
	 * 
	 * @param resSet
	 *            The result set containing the data
	 * @return A <code>WorkEffort</code> object
	 */
	public static final WorkEffort fromResultSet(
			final ResultSet resSet) {
		WorkEffort workEffort = new WorkEffort();

		try {
			workEffort.setId(resSet.getInt("id"));
			workEffort.setFid_wp(resSet.getInt("fid_wp"));
			workEffort.setFid_emp(resSet.getInt("fid_wp"));
			workEffort.setRec_date(resSet.getDate("rec_date"));
			workEffort.setEffort(resSet.getDouble("effort"));
			workEffort.setDescription(resSet.getString("description"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return workEffort;
	}
}
