package dbaccess.models.mysql;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sqlutils.TestDBConnector;
import dbaccess.data.Dependency;
import dbaccess.models.DependenciesModel;

public class MySQLDependenciesModelTest {
    private DependenciesModel dpModel;

    @Before
    public final void setup() {
        dpModel=new MySQLDependenciesModel(TestDBConnector.getConnection());
    }
    
    @After
    public final void cleanup() {
        dpModel = null;
    }
    
    @Test
    public final void testAddNewDependency() {
        //DependenciesModel depModel = new MySQLDependenciesModel(con);
        //depModel.addNewDependency(new Dependency(1,2));
        //TODO: implementieren
    }
    
    @Test
    public final void testGetDependency() {
        List<Dependency> dpList = dpModel.getDependency();
        assertThat(dpList, notNullValue());
        //TODO: implementieren
    }
    
    @Test
    public final void testDeleteDependency() {
        //DependenciesModel depModel = new MySQLDependenciesModel(con);
        //depModel.deleteDependency(1, 2);
        //TODO: implementieren
    }
    
}
