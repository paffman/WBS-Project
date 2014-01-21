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

/**
 * A simple container class representing a wp_allocation. It mirrors all the
 * database fields in the wp_allocation table.
 */
public class WorkpackageAllocation {
	/** Foreign id of the workpackage. */
	private int fid_wp;

	/** Foreign id of the employee. */
	private int fid_emp;

	/**
	 * Creates a <code>WorkpackageAllocation</code> based on a <code>ResultSet</code>.
	 * 
	 * @param resSet
	 *            The result set containing the data
	 * @return A <code>WorkpackageAllocation</code> object
	 */
	public static final WorkpackageAllocation fromResultSet(
			final ResultSet resSet) {
		WorkpackageAllocation wp_alloc = new WorkpackageAllocation();

		try {
			wp_alloc.setFid_wp(resSet.getInt("fid_wp"));
			wp_alloc.setFid_emp(resSet.getInt("fid_emp"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return wp_alloc;
	}

	/**
	 * @return the fid_wp
	 */
	public final int getFid_wp() {
		return fid_wp;
	}

	/**
	 * @param fid_wp
	 *            the fid_wp to set
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
	 * @param fid_emp
	 *            the fid_emp to set
	 */
	public final void setFid_emp(int fid_emp) {
		this.fid_emp = fid_emp;
	}

}
