package dbaccess.models.mysql;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sqlutils.TestDBConnector;
import dbaccess.data.Baseline;
import dbaccess.models.BaselineModel;

public class MySQLBaselineModelTest {
    private BaselineModel blModel;

    @Before
    public final void setup() {
        blModel=new MySQLBaselineModel(TestDBConnector.getConnection());
    }
    
    @After
    public final void cleanup() {
        blModel = null;
    }
    
    @Test
    public final void testAddNewBaseline() {
        try {
            Baseline bl=new Baseline();
            bl.setId(2);
            bl.setFid_project(1);
            bl.setBl_date(DateFormat.getInstance().parse("2014-01-13 00:00:00"));
            bl.setDescription("TestBeschreibung");
   
            blModel.addNewBaseline(bl);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Test
    public final void testGetBaseline() {

        List<Baseline> blList = blModel.getBaseline();
        assertThat(blList, notNullValue());
        assertThat(blList.get(0).getDescription(), equalTo("TestBeschreibung"));
        assertThat(blList.get(0).getBl_date().toString(), equalTo("2014-01-13 00:00:00"));
        //TODO: implementieren
    }
    
    @Test
    public final void testGetBaseline1() {
        Baseline baseline = blModel.getBaseline(1);
        assertThat(baseline, notNullValue());
        //TODO: implementieren
    }
}
