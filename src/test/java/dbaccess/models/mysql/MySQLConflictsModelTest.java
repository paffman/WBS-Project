package dbaccess.models.mysql;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import jdbcConnection.SQLExecuter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sqlutils.TestData;
import dbaccess.data.Conflict;
import dbaccess.models.ConflictsModel;

public class MySQLConflictsModelTest {
    private ConflictsModel cfModel;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

    @Before
    public final void setup() {
        cfModel=new MySQLConflictsModel();
    }
    
    @After
    public final void cleanup() {
        cfModel = null;
    }
    
    @Test
    public final void testAddNewConflict() {
        try {
            //Setup conflict
            Conflict cf=new Conflict();
            cf.setFid_wp(1);
            cf.setFid_wp_affected(2);
            cf.setFid_emp(2);
            cf.setReason(1);
            cf.setOccurence_date(dateFormat.parse("2014-01-13 00:00:00"));
            
            cfModel.addNewConflict(cf);
            
            List<Conflict> cfList=cfModel.getConflicts();
            assertThat(cfList,notNullValue());
            assertThat(cfList.size(), equalTo(4));
            assertThat(cfList.get(3).getFid_wp_affected(), equalTo(2));

            
        } catch (ParseException e) {
            e.printStackTrace();
        }
        TestData.reloadData(SQLExecuter.getConnection());
    }
    
    @Test
    public final void testGetConflicts() {
    
        List<Conflict> cfList = cfModel.getConflicts();
        assertThat(cfList, notNullValue());
        assertThat(cfList.size(), equalTo(3));
        assertThat(cfList.get(0).getFid_emp(),equalTo(3));
        assertThat(cfList.get(1).getFid_emp(),equalTo(3));
        assertThat(cfList.get(2).getFid_emp(),equalTo(3));
        
    }
    
    @Test
    public final void testDeleteConflict() {
        //delete a conflict with id
        cfModel.deleteConflict(1);
        List<Conflict> cfList = cfModel.getConflicts();
        assertThat(cfList, notNullValue());
        assertThat(cfList.size(), equalTo(2));
        assertThat(cfList.get(0).getFid_emp(),equalTo(3));
        assertThat(cfList.get(1).getFid_emp(),equalTo(3));
        
        TestData.reloadData(SQLExecuter.getConnection());
    }
    
    @Test
    public final void testDeleteConflicts() {
        //Delete all conflicts
        cfModel.deleteConflicts();
        List<Conflict> cfList = cfModel.getConflicts();
        assertThat(cfList, notNullValue());
        assertThat(cfList.size(), equalTo(0));
        
        TestData.reloadData(SQLExecuter.getConnection());
    }
}
