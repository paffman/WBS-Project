package dbaccess.models.mysql;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import jdbcConnection.SQLExecuter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sqlutils.TestData;
import dbaccess.data.Baseline;
import dbaccess.models.BaselineModel;

public class MySQLBaselineModelTest {
    private BaselineModel blModel;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat(
            "yyyy-mm-dd hh:mm:ss");

    @Before
    public final void setup() {
        blModel = new MySQLBaselineModel();
    }

    @After
    public final void cleanup() {
        blModel = null;
    }

    @Test
    public final void testAddNewBaseline() {
        try {
            // Setup baseline
            Baseline bl = new Baseline();
            bl.setFid_project(1);
            bl.setBl_date(dateFormat.parse("2014-01-13 00:00:00"));
            bl.setDescription("TestBeschreibung");
            
            blModel.addNewBaseline(bl);

            Baseline baseline = blModel.getBaseline(2);
            assertThat(baseline, notNullValue());
            assertThat(baseline.getId(), equalTo(2));
            assertThat(baseline.getFid_project(), equalTo(1));
            assertThat(baseline.getDescription(), equalTo("TestBeschreibung"));
            assertThat(baseline.getBl_date().toString(),
                    equalTo("2014-01-13"));
            TestData.reloadData(SQLExecuter.getConnection());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public final void testGetBaseline() {
        // get all baselines
        try {
            List<Baseline> blList = blModel.getBaseline();
            assertThat(blList, notNullValue());
            assertThat(blList.size(), equalTo(1));
            assertThat(blList.get(0).getId(), equalTo(1));
            assertThat(blList.get(0).getFid_project(), equalTo(1));
            assertThat(blList.get(0).getDescription(),
                    equalTo("Projekt Beginn"));
            assertThat(dateFormat.parse(blList.get(0).getBl_date().toString()),
                    equalTo(dateFormat.parse("2013-12-23 00:00:00")));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public final void testGetBaseline1() {
        // get baseline with id
        Baseline baseline = blModel.getBaseline(1);
        assertThat(baseline, notNullValue());
        assertThat(baseline.getId(), equalTo(1));
        assertThat(baseline.getFid_project(), equalTo(1));
        assertThat(baseline.getDescription(), equalTo("Projekt Beginn"));
        assertThat(baseline.getBl_date().toString(),
                equalTo("2013-12-23"));
    }
}
