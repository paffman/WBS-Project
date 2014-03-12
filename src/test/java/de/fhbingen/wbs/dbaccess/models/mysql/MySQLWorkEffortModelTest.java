/**
 *
 */
package de.fhbingen.wbs.dbaccess.models.mysql;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import de.fhbingen.wbs.jdbcConnection.SQLExecuter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sqlutils.TestData;
import de.fhbingen.wbs.dbaccess.data.WorkEffort;
import de.fhbingen.wbs.dbaccess.models.WorkEffortModel;

/**
 * @author Hendrik
 */
public class MySQLWorkEffortModelTest {

    private WorkEffortModel workEffortModel;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat(
            "yyyy-mm-dd hh:mm:ss");

    @Before
    public final void setup() {
        workEffortModel = new MySQLWorkEffortModel();
    }

    @After
    public final void cleanup() {
        workEffortModel = null;
    }

    /**
     * Test method for
     * {@link de.fhbingen.wbs.dbaccess.models.mysql.MySQLWorkEffortModel#addNewWorkEffort(de.fhbingen.wbs.dbaccess.data.WorkEffort)}
     * .
     */
    @Test
    public final void testAddNewWorkEffort() {
        WorkEffort newEffort = new WorkEffort();
        newEffort.setFid_emp(1);
        newEffort.setFid_wp(2);
        try {
            newEffort.setRec_date(dateFormat.parse("2014-01-09 08:00:00"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        newEffort.setDescription("test");
        newEffort.setEffort(0.5);
        workEffortModel.addNewWorkEffort(newEffort);
        List<WorkEffort> effortList = workEffortModel.getWorkEffort();
        assertThat(effortList.size(), equalTo(7));

        newEffort = new WorkEffort();
        newEffort.setFid_emp(1);
        newEffort.setFid_wp(2);
        try {
            newEffort.setRec_date(dateFormat.parse("2014-01-09 08:00:00"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        newEffort.setDescription("test");
        newEffort.setEffort(0.5);
        workEffortModel.addNewWorkEffort(newEffort);
        effortList = workEffortModel.getWorkEffort();
        assertThat(effortList.size(), equalTo(8));

        newEffort = new WorkEffort();
        newEffort.setFid_emp(3);
        newEffort.setFid_wp(5);
        try {
            newEffort.setRec_date(dateFormat.parse("2014-01-09 08:00:00"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        newEffort.setDescription("test");
        newEffort.setEffort(0.5);
        workEffortModel.addNewWorkEffort(newEffort);
        effortList = workEffortModel.getWorkEffort();
        assertThat(effortList.size(), equalTo(9));

        TestData.reloadData(SQLExecuter.getConnection());
    }

    /**
     * Test method for
     * {@link de.fhbingen.wbs.dbaccess.models.mysql.MySQLWorkEffortModel#getWorkEffort()}.
     */
    @Test
    public final void testGetWorkEffort() {
        List<WorkEffort> effortList = workEffortModel.getWorkEffort();
        assertThat(effortList.size(), equalTo(6));

    }

    /**
     * Test method for
     * {@link de.fhbingen.wbs.dbaccess.models.mysql.MySQLWorkEffortModel#getWorkEffort(int)}.
     */
    @Test
    public final void testGetWorkEffortInt() {
        List<WorkEffort> effortList = workEffortModel.getWorkEffort(3);
        assertThat(effortList.size(), equalTo(2));

        WorkEffort newEffort = new WorkEffort();
        newEffort.setFid_emp(1);
        newEffort.setFid_wp(3);
        try {
            newEffort.setRec_date(dateFormat.parse("2014-01-09 08:00:00"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        newEffort.setDescription("test");
        newEffort.setEffort(0.5);
        workEffortModel.addNewWorkEffort(newEffort);
        effortList = workEffortModel.getWorkEffort(3);
        assertThat(effortList.size(), equalTo(3));
    }

}
