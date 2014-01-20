package dbaccess.models.mysql;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sqlutils.TestDBConnector;
import dbaccess.data.Conflict;
import dbaccess.models.ConflictsModel;

public class MySQLConflictsModelTest {
    private ConflictsModel cfModel;

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
            Conflict cf=new Conflict();
            cf.setId(2);
            cf.setFid_wp(1);
            cf.setFid_wp_affected(1);
            cf.setFid_emp(1);
            cf.setReason(1);
            cf.setOccurence_date(DateFormat.getInstance().parse("2014-01-13 00:00:00"));
            
            cfModel.addNewConflict(cf);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Test
    public final void testGetConflicts() {
    
        List<Conflict> cfList = cfModel.getConflicts();
        assertThat(cfList, notNullValue());
        //TODO: implementieren
    }
    
    @Test
    public final void testDeleteConflict() {
        cfModel.deleteConflict(1);
        //TODO: implementieren
    }
    
    @Test
    public final void testDeleteConflicts() {
        cfModel.deleteConflicts();
        //TODO: implementieren
    }
}
