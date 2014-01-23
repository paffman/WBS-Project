package dbaccess.models.mysql;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import jdbcConnection.SQLExecuter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mysql.jdbc.PreparedStatement;

import sqlutils.TestData;
import dbaccess.data.HolidayCalendar;
import dbaccess.data.PlannedValue;
import dbaccess.data.Workpackage;
import dbaccess.models.HolidaysCalendarModel;
import dbaccess.models.PlannedValueModel;

public class MySQLHolidaysCalendarModelTest {

	private HolidaysCalendarModel hcModel;
	private final SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-mm-dd hh:mm:ss");

	@Before
	public final void setup() {
		hcModel = new MySQLHolidaysCalendarModel();
	}

	@After
	public final void cleanup() {
		hcModel = null;
	}

	@Test
	public final void testAddNewHolidayCalendar() {
		HolidayCalendar hc = new HolidayCalendar();
		try {
			hc.setTitle("Mad Hatter Day");
			hc.setBegin_time(dateFormat.parse("2014-10-06 00:00:00"));
			hc.setEnd_time(dateFormat.parse("2014-10-06 23:59:59"));
			hc.setAvailability(false);
			hc.setFull_time(true);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		hcModel.addNewHolidayCalendar(hc);

		List<HolidayCalendar> hcList = hcModel.getHolidayCalendar();
		assertThat(hcList.size(), equalTo(4));

		hcModel.addNewHolidayCalendar(hc);

		hcList = hcModel.getHolidayCalendar();
		assertThat(hcList.size(), equalTo(4));

		TestData.reloadData(SQLExecuter.getConnection());
	}

	@Test
	public final void testGetHolidayCalendar() {
		List<HolidayCalendar> hcList = hcModel.getHolidayCalendar();
		assertThat(hcList.size(), equalTo(3));
	}

	@Test
	public final void testGetHolidayCalendarInt() {
		HolidayCalendar hc = hcModel.getHolidayCalendar(3);
		assertThat(hc.getTitle(), equalTo("Talk like a Pirate Day"));

		hc = hcModel.getHolidayCalendar(1);
		assertThat(hc.getTitle(), equalTo("Neujahr"));
	}

	@Test
	public final void testGetHolidayCalendarDateDateBoolean() {
		List<HolidayCalendar> hcList;

		try {
			Timestamp from = new Timestamp(dateFormat.parse(
					"2014-12-01 00:00:00").getTime());
			Timestamp to = new Timestamp(dateFormat
					.parse("2014-12-31 00:00:00").getTime());

			hcList = hcModel.getHolidayCalendar(
					dateFormat.parse("2014-12-01 00:00:00"),
					dateFormat.parse("2014-12-31 00:00:00"), false);

			String statement = "SELECT * FROM holidays_calendar " + "WHERE "
					+ "(begin_time BETWEEN ? AND ? ) "
					+ "OR (end_time BETWEEN ? AND ?)";
			java.sql.PreparedStatement stmt = SQLExecuter.getConnection()
					.prepareStatement(statement);

			stmt.setTimestamp(1, from);
			stmt.setTimestamp(2, to);
			stmt.setTimestamp(3, from);
			stmt.setTimestamp(4, to);
			stmt.setTimestamp(5, from);
			stmt.setTimestamp(6, to);
			ResultSet rslt = stmt.executeQuery();
			int rsltSize = 0;
			if (rslt != null) {
				while (rslt.next()) {
					rsltSize++;
				}
			}
			assertThat(hcList.size(), equalTo(rsltSize));

			hcList = hcModel.getHolidayCalendar(
					dateFormat.parse("2014-12-01 00:00:00"),
					dateFormat.parse("2014-12-31 00:00:00"), true);

			statement = "SELECT * FROM holidays_calendar "
					+ "WHERE (begin_time < ? AND end_time > ?) "
					+ "OR ((begin_time BETWEEN ? AND ? ) "
					+ "OR (end_time BETWEEN ? AND ?))";
			stmt = SQLExecuter.getConnection().prepareStatement(statement);

			stmt.setTimestamp(1, from);
			stmt.setTimestamp(2, to);
			stmt.setTimestamp(3, from);
			stmt.setTimestamp(4, to);
			stmt.setTimestamp(5, from);
			stmt.setTimestamp(6, to);
			rslt = stmt.executeQuery();
			rsltSize = 0;
			if (rslt != null) {
				while (rslt.next()) {
					rsltSize++;
				}
			}
			assertThat(hcList.size(), equalTo(rsltSize));
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public final void testUpdateHolidayCalendar() {
		HolidayCalendar hc = hcModel.getHolidayCalendar(3);
		assertThat(hc.getTitle(), equalTo("Talk like a Pirate Day"));
		hc.setTitle("tlapd");
		hcModel.updateHolidayCalendar(hc);

		hc = hcModel.getHolidayCalendar(hc.getId());
		assertThat(hc.getTitle(), equalTo("tlapd"));

		TestData.reloadData(SQLExecuter.getConnection());
	}

	@Test
	public final void testDeleteHolidayCalendar() {
		HolidayCalendar hc = hcModel.getHolidayCalendar(3);
		assertThat(hc.getTitle(), equalTo("Talk like a Pirate Day"));
		hcModel.deleteHolidayCalendar(3);

		hc = hcModel.getHolidayCalendar(3);
		if (hc != null) {
			fail("hc should be null!");
		}

		TestData.reloadData(SQLExecuter.getConnection());
	}

}
