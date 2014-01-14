package dbaccess.models.mysql;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import dbaccess.data.Dependency;
import dbaccess.models.DependenciesModel;

public class MySQLDependenciesModelTest {
    private static Connection con;

    @BeforeClass
    public static final void setupTest() {
        final String url = "jdbc:mysql://localhost:3306/";
        final String dbName = "wbs_unittest_db";
        final String driver = "com.mysql.jdbc.Driver";
        final String userName = "root";
        final String password = "root";

        try {
            Class.forName(driver).newInstance();
            con = DriverManager.getConnection(url + dbName,
                    userName, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @AfterClass
    public static final void closeDBConnection() {
        try {
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public final void testAddNewDependency() {
        //DependenciesModel depModel = new MySQLDependenciesModel(con);
        //depModel.addNewDependency(new Dependency(1,2));
        //TODO: implementieren
    }
    
    @Test
    public final void testGetDependency() {
        DependenciesModel depModel = new MySQLDependenciesModel(con);

        List<Dependency> depList = depModel.getDependency();
        assertThat(depList, notNullValue());
        //TODO: implementieren
    }
    
    @Test
    public final void testDeleteDependency() {
        //DependenciesModel depModel = new MySQLDependenciesModel(con);
        //depModel.deleteDependency(1, 2);
        //TODO: implementieren
    }
    
}
