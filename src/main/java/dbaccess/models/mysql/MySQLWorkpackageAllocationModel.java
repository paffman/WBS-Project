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

package dbaccess.models.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jdbcConnection.SQLExecuter;
import dbaccess.data.Workpackage;
import dbaccess.data.WorkpackageAllocation;
import dbaccess.models.WorkpackageAllocationModel;

/**
 * The <code>MySQLWorkpackagAllocationeModel</code> class implements the
 * <code>WorkpackageAllocationModel</code> and handles all the database access concerning
 * workpackage allocation.
 */
public class MySQLWorkpackageAllocationModel implements
		WorkpackageAllocationModel {

	@Override
	public void addNewWorkpackageAllocation(WorkpackageAllocation wpAllocation) {
		final Connection connection = SQLExecuter.getConnection();
		PreparedStatement stm = null;

		String storedProcedure = "CALL wp_allocation_new(?,?)";

		try {
			stm = connection.prepareStatement(storedProcedure);
			stm.setInt(1, wpAllocation.getFid_wp());
			stm.setInt(2, wpAllocation.getFid_emp());

			stm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stm != null) {
					stm.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public List<WorkpackageAllocation> getWorkpackageAllocation() {
		final Connection connection = SQLExecuter.getConnection();
		List<WorkpackageAllocation> wpaList = new ArrayList<WorkpackageAllocation>();

		ResultSet sqlResult = null;
		Statement stm = null;

		try {
			stm = connection.createStatement();
			String sql = "CALL wp_allocation_select(null)";
			sqlResult = stm.executeQuery(sql);

			while (sqlResult.next()) {
				wpaList.add(WorkpackageAllocation.fromResultSet(sqlResult));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (sqlResult != null) {
					sqlResult.close();
				}
				if (stm != null) {
					stm.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return wpaList;
	}

	@Override
	public List<WorkpackageAllocation> getWorkpackageAllocation(int fidWP) {
		final Connection connection = SQLExecuter.getConnection();
		List<WorkpackageAllocation> wpaList = new ArrayList<WorkpackageAllocation>();

		ResultSet sqlResult = null;
		Statement stm = null;

		try {
			stm = connection.createStatement();
			String sql = String.format("CALL wp_allocation_select(%d)", fidWP);
			sqlResult = stm.executeQuery(sql);

			while (sqlResult.next()) {
				wpaList.add(WorkpackageAllocation.fromResultSet(sqlResult));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (sqlResult != null) {
					sqlResult.close();
				}
				if (stm != null) {
					stm.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return wpaList;
	}

	@Override
	public List<Workpackage> getWorkpackageAllocationJoinWP(
			int fidEmp) {
		final Connection connection = SQLExecuter.getConnection();
		List<Workpackage> wpaList = new ArrayList< Workpackage>();

		ResultSet sqlResult = null;
		Statement stm = null;

		try {
			stm = connection.createStatement();
			String sql = String
					.format("CALL wp_alloc_workpackage_select(%d, false, null, null)", fidEmp);
			System.out.println(sql);
			sqlResult = stm.executeQuery(sql);

			while (sqlResult.next()) {
				wpaList.add(Workpackage.fromResultSet(sqlResult));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (sqlResult != null) {
					sqlResult.close();
				}
				if (stm != null) {
					stm.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return wpaList;
	}

	@Override
	public List<Workpackage> getWorkpackageAllocationJoinWP(
			int fidEmp, Date from, Date to) {
		final Connection connection = SQLExecuter.getConnection();
		List<Workpackage> wpaList = new ArrayList< Workpackage>();

		ResultSet sqlResult = null;
		PreparedStatement stm = null;

		final String storedProcedure = "CALL wp_alloc_workpackage_select(?, true, ?, ?)";
		final Timestamp fromTimestamp = new Timestamp(from.getTime());
		final Timestamp toTimestamp = new Timestamp(to.getTime());

		try {
			stm = connection.prepareStatement(storedProcedure);
			stm.setInt(1, fidEmp);
			stm.setTimestamp(2, fromTimestamp);
			stm.setTimestamp(3, toTimestamp);
			sqlResult = stm.executeQuery();

			while (sqlResult.next()) {
				wpaList.add(Workpackage.fromResultSet(sqlResult));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (sqlResult != null) {
					sqlResult.close();
				}
				if (stm != null) {
					stm.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return wpaList;
	}

	@Override
	public void deleteWorkpackageAllocation(int employeeID, int workpackageID) {
		final Connection connection = SQLExecuter.getConnection();
		PreparedStatement stm = null;

		String storedProcedure = "CALL wp_allocation_delete_by_key(?,?)";

		try {
			stm = connection.prepareStatement(storedProcedure);
			stm.setInt(1, workpackageID);
			stm.setInt(2, employeeID);

			stm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stm != null) {
					stm.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

}
