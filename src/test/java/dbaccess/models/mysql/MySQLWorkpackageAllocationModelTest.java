/**
 * 
 */
package dbaccess.models.mysql;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.notNullValue;

import java.util.Date;
import java.util.List;

import jdbcConnection.SQLExecuter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sqlutils.TestData;
import dbaccess.data.WorkpackageAllocation;
import dbaccess.data.Workpackage;
import dbaccess.models.WorkpackageAllocationModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author Hendrik
 * 
 */
public class MySQLWorkpackageAllocationModelTest {

	private WorkpackageAllocationModel wpAllocModel;
	private final SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-mm-dd hh:mm:ss");

	@Before
	public final void setup() {
		wpAllocModel = new MySQLWorkpackageAllocationModel();
	}

	@After
	public final void cleanup() {
		wpAllocModel = null;
	}

	/**
	 * Test method for
	 * {@link dbaccess.models.mysql.MySQLWorkpackageAllocationModel#addNewWorkpackageAllocation(dbaccess.data.WorkpackageAllocation)}
	 * .
	 */
	@Test
	public final void testAddNewWorkpackageAllocation() {
		WorkpackageAllocation wpa = new WorkpackageAllocation();
		wpa.setFid_emp(1);
		wpa.setFid_wp(1);
		wpAllocModel.addNewWorkpackageAllocation(wpa);
		List<WorkpackageAllocation> wpaList = wpAllocModel
				.getWorkpackageAllocation(1);
		assertThat(wpaList.size(), equalTo(4));

		wpa = new WorkpackageAllocation();
		wpa.setFid_emp(1);
		wpa.setFid_wp(3);
		wpAllocModel.addNewWorkpackageAllocation(wpa);
		wpaList = wpAllocModel.getWorkpackageAllocation(3);
		assertThat(wpaList.size(), equalTo(3));

		wpa = new WorkpackageAllocation();
		wpa.setFid_emp(4);
		wpa.setFid_wp(5);
		wpAllocModel.addNewWorkpackageAllocation(wpa);
		wpaList = wpAllocModel.getWorkpackageAllocation(5);
		assertThat(wpaList.size(), equalTo(3));

		TestData.reloadData(SQLExecuter.getConnection());
	}

	/**
	 * Test method for
	 * {@link dbaccess.models.mysql.MySQLWorkpackageAllocationModel#getWorkpackageAllocation()}
	 * .
	 */
	@Test
	public final void testGetWorkpackageAllocation() {
		List<WorkpackageAllocation> wpaList = wpAllocModel
				.getWorkpackageAllocation();
		assertThat(wpaList.size(), equalTo(20));
	}

	/**
	 * Test method for
	 * {@link dbaccess.models.mysql.MySQLWorkpackageAllocationModel#getWorkpackageAllocation(int)}
	 * .
	 */
	@Test
	public final void testGetWorkpackageAllocationInt() {
		List<WorkpackageAllocation> wpaList = wpAllocModel
				.getWorkpackageAllocation(1);
		assertThat(wpaList.size(), equalTo(3));
		int wpId = 1;
		for (int i = 0; i < wpaList.size(); i++) {
			assertThat(wpaList.get(0).getFid_wp(), equalTo(wpId));
		}

		wpaList = wpAllocModel.getWorkpackageAllocation(5);
		assertThat(wpaList.size(), equalTo(2));
		wpId = 5;
		for (int i = 0; i < wpaList.size(); i++) {
			assertThat(wpaList.get(0).getFid_wp(), equalTo(wpId));
		}

		wpaList = wpAllocModel.getWorkpackageAllocation(55);
		assertThat(wpaList.size(), equalTo(0));
	}

	/**
	 * Test method for
	 * {@link dbaccess.models.mysql.MySQLWorkpackageAllocationModel#getWorkpackageAllocationJoinWP()}
	 * .
	 */
	@Test
	public final void testGetWorkpackageAllocationJoinWP() {
		List<Workpackage> wpList = wpAllocModel
				.getWorkpackageAllocationJoinWP(2);
		String statement = "SELECT * "
				+ "FROM wp_allocation wpa INNER JOIN workpackage wp ON ( wpa.fid_wp = wp.id )"
				+ "WHERE wpa.fid_emp = 2;";
		ResultSet rslt = SQLExecuter.executeQuery(statement);
		int rsltSize = 0;
		try {
			while (rslt.next()) {
				rsltSize++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		assertThat(wpList.size(), equalTo(rsltSize));
	}

	/**
	 * Test method for
	 * {@link dbaccess.models.mysql.MySQLWorkpackageAllocationModel#getWorkpackageAllocationJoinWP(java.util.Date, java.util.Date)}
	 * .
	 */
	@Test
	public final void testGetWorkpackageAllocationJoinWPDateDate() {
		String startDateCalcStr = "2014-01-01 08:01:00";
		String endDateCalcStr = "2014-01-03 10:01:00";

		try {
			Date startDateCalc = dateFormat.parse(startDateCalcStr);
			Date endDateCalc = dateFormat.parse(endDateCalcStr);

			List<Workpackage> wpList = wpAllocModel
					.getWorkpackageAllocationJoinWP(2, startDateCalc,
							endDateCalc);

			String statement = "SELECT * "
					+ "FROM wp_allocation wpa INNER JOIN workpackage wp ON ( wpa.fid_wp = wp.id )"
					+ "WHERE wpa.fid_emp = 2 AND wp.end_date_calc >= '"
					+ startDateCalcStr + "' AND wp.start_date_calc <= '"
					+ endDateCalcStr + "';";
			ResultSet rslt = SQLExecuter.executeQuery(statement);
			int rsltSize = 0;
			try {
				while (rslt.next()) {
					rsltSize++;
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
			assertThat(wpList.size(), equalTo(rsltSize));

		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test method for
	 * {@link dbaccess.models.mysql.MySQLWorkpackageAllocationModel#deleteWorkpackageAllocation(int, int)}
	 * .
	 */
	@Test
	public final void testDeleteWorkpackageAllocation() {
		wpAllocModel.deleteWorkpackageAllocation(1, 2);
		List<WorkpackageAllocation> wpaList = wpAllocModel
				.getWorkpackageAllocation(1);
		assertThat(wpaList.size(), equalTo(2));
		wpAllocModel.deleteWorkpackageAllocation(1, 4);
		wpaList = wpAllocModel.getWorkpackageAllocation(1);
		assertThat(wpaList.size(), equalTo(1));

		wpaList = wpAllocModel.getWorkpackageAllocation();
		assertThat(wpaList.size(), equalTo(18));

		TestData.reloadData(SQLExecuter.getConnection());
	}

}
