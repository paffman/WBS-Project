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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jdbcConnection.SQLExecuter;
import dbaccess.data.PlannedValue;
import dbaccess.models.PlannedValueModel;

/**
 * The <code>MySQLPlannedValueModel</code> class implements the
 * <code>PlannedValueModel</code> and handles all the database access concerning
 * planned values.
 */
public class MySQLPlannedValueModel implements PlannedValueModel {

	@Override
	public void addNewPlannedValue(PlannedValue pValue) {
		final Connection connection = SQLExecuter.getConnection();
		PreparedStatement stm = null;

		String storedProcedure = "CALL planned_value_new(?,?,?)";

		try {
			stm = connection.prepareStatement(storedProcedure);
			stm.setInt(1, pValue.getFid_wp());
			stm.setTimestamp(2, new Timestamp(pValue.getPv_date().getTime()));
			stm.setInt(3, pValue.getPv());

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
	public List<PlannedValue> getPlannedValue(Date from, Date to) {
		final Connection connection = SQLExecuter.getConnection();
		List<PlannedValue> pvList = new ArrayList<PlannedValue>();

		ResultSet sqlResult = null;
		PreparedStatement stm = null;

		final String storedProcedure = "CALL planned_value_select_by_date(?,?, null)";

		try {
			stm = connection.prepareStatement(storedProcedure);
			stm.setTimestamp(1, new Timestamp(from.getTime()));
			stm.setTimestamp(2, new Timestamp(to.getTime()));
			sqlResult = stm.executeQuery();

			while (sqlResult.next()) {
				pvList.add(PlannedValue.fromResultSet(sqlResult));
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
		return pvList;
	}

	@Override
	public List<PlannedValue> getPlannedValue(Date from, Date to, int wpID) {
		final Connection connection = SQLExecuter.getConnection();
		List<PlannedValue> pvList = new ArrayList<PlannedValue>();

		ResultSet sqlResult = null;
		PreparedStatement stm = null;

		final String storedProcedure = "CALL planned_value_select_by_date(?,?,?)";

		try {
			stm = connection.prepareStatement(storedProcedure);
			stm.setTimestamp(1, new Timestamp(from.getTime()));
			stm.setTimestamp(2, new Timestamp(to.getTime()));
			stm.setInt(3, wpID);
			sqlResult = stm.executeQuery();

			while (sqlResult.next()) {
				pvList.add(PlannedValue.fromResultSet(sqlResult));
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
		return pvList;
	}

	@Override
	public int getPlannedValue(Date aDate, int wpID) {
		final Connection connection = SQLExecuter.getConnection();

		ResultSet sqlResult = null;
		PreparedStatement stm = null;

		final String storedProcedure = "CALL planned_value_select_by_wp_and_date(?,?)";
		int rslt = Integer.MIN_VALUE;
		try {
			stm = connection.prepareStatement(storedProcedure);
			stm.setTimestamp(1, new Timestamp(aDate.getTime()));
			stm.setInt(2, wpID);
			sqlResult = stm.executeQuery();
			if (sqlResult.next()) {
				rslt = sqlResult.getInt("pv");
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
		return rslt;
	}

	@Override
	public void updatePlannedValue(Date aDate, int wpID, int newValue) {
		final Connection connection = SQLExecuter.getConnection();
		PreparedStatement stm = null;

		String storedProcedure = "CALL planned_value_update_by_wp_and_date(?,?,?)";

		try {
			stm = connection.prepareStatement(storedProcedure);
			stm.setInt(1, wpID);
			stm.setTimestamp(2, new Timestamp(aDate.getTime()));
			stm.setInt(3, newValue);

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
	public void deletePlannedValue() {
		final Connection connection = SQLExecuter.getConnection();
		PreparedStatement stm = null;

		String storedProcedure = "CALL planned_value_delete()";

		try {
			stm = connection.prepareStatement(storedProcedure);

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
	public void deletePlannedValue(int wpID) {
		final Connection connection = SQLExecuter.getConnection();
		PreparedStatement stm = null;

		String storedProcedure = "CALL planned_value_delete_by_wp(?, null)";

		try {
			stm = connection.prepareStatement(storedProcedure);
			stm.setInt(1, wpID);

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
	public void deletePlannedValue(Date aDate, int wpID) {
		final Connection connection = SQLExecuter.getConnection();
		PreparedStatement stm = null;

		String storedProcedure = "CALL planned_value_delete_by_wp(?, ?)";

		try {
			stm = connection.prepareStatement(storedProcedure);
			stm.setInt(1, wpID);
			stm.setTimestamp(2, new Timestamp(aDate.getTime()));

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
