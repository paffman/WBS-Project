package dbaccess.models.mysql;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import dbaccess.data.Baseline;
import dbaccess.models.BaselineModel;

public class MySQLBaselineModelTest {
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
    public final void testAddNewBaseline() {
        BaselineModel blModel = new MySQLBaselineModel(con);
        blModel.addNewBaseline(new Baseline(2,1,"2014-01-13 00:00:00","TestBeschreibung"));
        //TODO: implementieren
    }
    
    @Test
    public final void testGetBaseline() {
        BaselineModel blModel = new MySQLBaselineModel(con);

        List<Baseline> blList = blModel.getBaseline();
        assertThat(blList, notNullValue());
        //TODO: implementieren
    }
    
    @Test
    public final void testGetBaseline1() {
        BaselineModel blModel = new MySQLBaselineModel(con);

        Baseline baseline = blModel.getBaseline(1);
        assertThat(baseline, notNullValue());
        //TODO: implementieren
    }
}
