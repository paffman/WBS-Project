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
import dbaccess.data.HolidayCalendar;
import dbaccess.models.HolidaysCalendarModel;

public class MySQLHolidaysCalendarModel implements HolidaysCalendarModel {

	@Override
	public void addNewHolidayCalendar(HolidayCalendar holCal) {
		final Connection connection = SQLExecuter.getConnection();
		PreparedStatement stm = null;

		String storedProcedure = "CALL holidays_calendar_new(?,?,?,?,?)";

		try {
			stm = connection.prepareStatement(storedProcedure);
			stm.setString(1, holCal.getTitle());
			stm.setTimestamp(2, new Timestamp(holCal.getBegin_time().getTime()));
			stm.setTimestamp(3, new Timestamp(holCal.getEnd_time().getTime()));
			stm.setBoolean(4, holCal.isAvailability());
			stm.setBoolean(5, holCal.isFull_time());

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
	public List<HolidayCalendar> getHolidayCalendar() {
		final Connection connection = SQLExecuter.getConnection();
		List<HolidayCalendar> hcList = new ArrayList<HolidayCalendar>();

		ResultSet sqlResult = null;
		PreparedStatement stm = null;

		final String storedProcedure = "CALL holidays_calendar_select(null)";

		try {
			stm = connection.prepareStatement(storedProcedure);
			sqlResult = stm.executeQuery();

			while (sqlResult.next()) {
				hcList.add(HolidayCalendar.fromResultSet(sqlResult));
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
		return hcList;
	}

	@Override
	public HolidayCalendar getHolidayCalendar(int calID) {
		final Connection connection = SQLExecuter.getConnection();
		HolidayCalendar rslt = null;

		ResultSet sqlResult = null;
		PreparedStatement stm = null;

		final String storedProcedure = "CALL holidays_calendar_select(?)";

		try {
			stm = connection.prepareStatement(storedProcedure);
			stm.setInt(1, calID);

			sqlResult = stm.executeQuery();

			while (sqlResult.next()) {
				rslt = HolidayCalendar.fromResultSet(sqlResult);
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
	public List<HolidayCalendar> getHolidayCalendar(Date from, Date to,
			boolean mode2) {
		final Connection connection = SQLExecuter.getConnection();
		List<HolidayCalendar> hcList = new ArrayList<HolidayCalendar>();

		ResultSet sqlResult = null;
		PreparedStatement stm = null;

		final String storedProcedure = "CALL holidays_calendar_select_by_date(?,?,?)";

		try {
			stm = connection.prepareStatement(storedProcedure);
			stm.setTimestamp(1, new Timestamp(from.getTime()));
			stm.setTimestamp(2, new Timestamp(to.getTime()));
			stm.setBoolean(3, mode2);

			sqlResult = stm.executeQuery();

			while (sqlResult.next()) {
				hcList.add(HolidayCalendar.fromResultSet(sqlResult));
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
		return hcList;
	}

	@Override
	public void updateHolidayCalendar(HolidayCalendar hc) {
		final Connection connection = SQLExecuter.getConnection();
		PreparedStatement stm = null;

		String storedProcedure = "CALL holidays_calendar_update_by_id(?,?,?,?,?,?)";

		try {
			stm = connection.prepareStatement(storedProcedure);
			stm.setInt(1, hc.getId());
			stm.setString(2, hc.getTitle());
			stm.setTimestamp(3, new Timestamp(hc.getBegin_time().getTime()));
			stm.setTimestamp(4, new Timestamp(hc.getEnd_time().getTime()));
			stm.setBoolean(5, hc.isAvailability());
			stm.setBoolean(6, hc.isFull_time());

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
	public void deleteHolidayCalendar(int calID) {
		final Connection connection = SQLExecuter.getConnection();
		PreparedStatement stm = null;

		String storedProcedure = "CALL holidays_calendar_delete_by_id(?)";

		try {
			stm = connection.prepareStatement(storedProcedure);
			stm.setInt(1, calID);

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
