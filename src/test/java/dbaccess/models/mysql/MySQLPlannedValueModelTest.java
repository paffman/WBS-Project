/**
 * 
 */
package dbaccess.models.mysql;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import jdbcConnection.SQLExecuter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sqlutils.TestData;
import dbaccess.data.PlannedValue;
import dbaccess.models.PlannedValueModel;

/**
 * @author Hendrik
 */
public class MySQLPlannedValueModelTest {

    private PlannedValueModel pvModel;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat(
            "yyyy-mm-dd hh:mm:ss");

    @Before
    public final void setup() {
        pvModel = new MySQLPlannedValueModel();
    }

    @After
    public final void cleanup() {
        pvModel = null;
    }

    /**
     * Test method for
     * {@link dbaccess.models.mysql.MySQLPlannedValueModel#addNewPlannedValue(dbaccess.data.PlannedValue)}
     * .
     */
    @Test
    public final void testAddNewPlannedValue() {
        PlannedValue pv = new PlannedValue();
        pv.setFid_wp(5);
        pv.setPv(5);
        try {
            pv.setPv_date(dateFormat.parse("2014-01-11 00:00:00"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        pvModel.addNewPlannedValue(pv);

        try {
            int pvVal =
                    pvModel.getPlannedValue(
                            dateFormat.parse("2014-01-11 00:00:00"), 5);
            assertThat(pvVal, equalTo(5));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        TestData.reloadData(SQLExecuter.getConnection());
    }

    /**
     * Test method for
     * {@link dbaccess.models.mysql.MySQLPlannedValueModel#getPlannedValue(java.util.Date, java.util.Date)}
     * .
     */
    @Test
    public final void testGetPlannedValueDateDate() {
        try {
            Date from = dateFormat.parse("2014-01-06 00:00:00");
            Date to = dateFormat.parse("2014-01-07 00:00:00");
            List<PlannedValue> pvList = pvModel.getPlannedValue(from, to);
            assertThat(pvList.size(), equalTo(2));

            from = dateFormat.parse("2014-01-01 00:00:00");
            to = dateFormat.parse("2014-01-30 00:00:00");
            pvList = pvModel.getPlannedValue(from, to);
            assertThat(pvList.size(), equalTo(5));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    /**
     * Test method for
     * {@link dbaccess.models.mysql.MySQLPlannedValueModel#getPlannedValue(java.util.Date, java.util.Date, int)}
     * .
     */
    @Test
    public final void testGetPlannedValueDateDateInt() {
        List<PlannedValue> pvList;
        try {
            Date from = dateFormat.parse("2014-01-04 00:00:00");
            Date to = dateFormat.parse("2014-01-07 00:00:00");
            pvList = pvModel.getPlannedValue(from, to, 7);
            assertThat(pvList.size(), equalTo(2));

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test method for
     * {@link dbaccess.models.mysql.MySQLPlannedValueModel#getPlannedValue(java.util.Date, int)}
     * .
     */
    @Test
    public final void testGetPlannedValueDateInt() {
        int pvVal;
        try {
            pvVal =
                    pvModel.getPlannedValue(
                            dateFormat.parse("2014-01-07 00:00:00"), 8);
            assertThat(pvVal, equalTo(4));

            pvVal =
                    pvModel.getPlannedValue(
                            dateFormat.parse("2014-01-07 00:00:00"), 7);
            assertThat(pvVal, equalTo(Integer.MIN_VALUE));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    /**
     * Test method for
     * {@link dbaccess.models.mysql.MySQLPlannedValueModel#updatePlannedValue(java.util.Date, int, int)}
     * .
     */
    @Test
    public final void testUpdatePlannedValue() {
        try {
            int pvVal =
                    pvModel.getPlannedValue(
                            dateFormat.parse("2014-01-08 00:00:00"), 8);
            assertThat(pvVal, equalTo(5));

            pvModel.updatePlannedValue(dateFormat.parse("2014-01-08 00:00:00"),
                    8, 10);

            pvVal =
                    pvModel.getPlannedValue(
                            dateFormat.parse("2014-01-08 00:00:00"), 8);
            assertThat(pvVal, equalTo(10));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        TestData.reloadData(SQLExecuter.getConnection());
    }

    /**
     * Test method for
     * {@link dbaccess.models.mysql.MySQLPlannedValueModel#deletePlannedValue()}
     * .
     */
    @Test
    public final void testDeletePlannedValue() {
        try {
            Date from = dateFormat.parse("2014-01-01 00:00:00");
            Date to = dateFormat.parse("2014-01-30 00:00:00");
            List<PlannedValue> pvList = pvModel.getPlannedValue(from, to);
            assertThat(pvList.size(), equalTo(5));

            pvModel.deletePlannedValue();

            pvList = pvModel.getPlannedValue(from, to);
            assertThat(pvList.size(), equalTo(0));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        TestData.reloadData(SQLExecuter.getConnection());
    }

    /**
     * Test method for
     * {@link dbaccess.models.mysql.MySQLPlannedValueModel#deletePlannedValue(int)}
     * .
     */
    @Test
    public final void testDeletePlannedValueInt() {

        try {
            Date from = dateFormat.parse("2014-01-01 00:00:00");
            Date to = dateFormat.parse("2014-01-30 00:00:00");
            List<PlannedValue> pvList = pvModel.getPlannedValue(from, to);
            assertThat(pvList.size(), equalTo(5));

            pvModel.deletePlannedValue(8);

            pvList = pvModel.getPlannedValue(from, to);
            assertThat(pvList.size(), equalTo(2));

            TestData.reloadData(SQLExecuter.getConnection());

            pvList = pvModel.getPlannedValue(from, to);
            assertThat(pvList.size(), equalTo(5));

            pvModel.deletePlannedValue(7);

            pvList = pvModel.getPlannedValue(from, to);
            assertThat(pvList.size(), equalTo(3));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        TestData.reloadData(SQLExecuter.getConnection());
    }

    /**
     * Test method for
     * {@link dbaccess.models.mysql.MySQLPlannedValueModel#deletePlannedValue(java.util.Date, int)}
     * .
     */
    @Test
    public final void testDeletePlannedValueDateInt() {
        try {
            Date from = dateFormat.parse("2014-01-01 00:00:00");
            Date to = dateFormat.parse("2014-01-30 00:00:00");
            List<PlannedValue> pvList = pvModel.getPlannedValue(from, to);
            assertThat(pvList.size(), equalTo(5));

            pvModel.deletePlannedValue(dateFormat.parse("2014-01-08 00:00:00"),
                    8);

            pvList = pvModel.getPlannedValue(from, to);
            assertThat(pvList.size(), equalTo(4));

            pvModel.deletePlannedValue(dateFormat.parse("2014-01-05 00:00:00"),
                    7);

            pvList = pvModel.getPlannedValue(from, to);
            assertThat(pvList.size(), equalTo(3));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        TestData.reloadData(SQLExecuter.getConnection());
    }

}
