package dbaccess.models.mysql;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.notNullValue;

import java.sql.Connection;
import java.sql.DriverManager;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import dbaccess.data.AnalyseData;
import dbaccess.models.AnalyseDataModel;

public class MySQLAnalyseDataModelTest {
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
    public final void testAddNewAnalyseData() {

        AnalyseDataModel adModel = new MySQLAnalyseDataModel(con);
        adModel.addNewAnalyseData(new AnalyseData(1,1,1,"TestData",2.5,2.6,2.7,2.8,2.9,1.0,2.3,1.2,1.2,3,4,5));
        AnalyseData aData = adModel.getAnalyseData(1);
        assertThat(aData, notNullValue());
    }
    
    //TODO: Tests müssen noch implementiert werden
    @Test
    public final void testGetAnalyseData() {

        AnalyseDataModel adModel = new MySQLAnalyseDataModel(con);

        AnalyseData aData = adModel.getAnalyseData(1);
        assertThat(aData, notNullValue());

        //assertThat("list should not be empty", wpList.size() > 0);
        //assertThat("list should have 8 work packages", wpList.size() > 3);

    }
    
    @Test
    public final void testGetAnalyseDataForBaseline() {

        AnalyseDataModel adModel = new MySQLAnalyseDataModel(con);
        System.out.println(adModel.getAnalyseDataForBaseline(1));
        AnalyseData aData = adModel.getAnalyseData(1);
        assertThat(aData, notNullValue());

        //assertThat("list should not be empty", wpList.size() > 0);
        //assertThat("list should have 8 work packages", wpList.size() > 3);

    }

}
