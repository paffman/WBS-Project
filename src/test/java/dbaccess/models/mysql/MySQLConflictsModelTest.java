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
import dbaccess.data.Conflict;
import dbaccess.models.BaselineModel;
import dbaccess.models.ConflictsModel;

public class MySQLConflictsModelTest {
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
    public final void testAddNewConflict() {
        ConflictsModel conModel = new MySQLConflictsModel(con);
        conModel.addNewConflict(new Conflict(2,1,1,1,1,"2014-01-13 00:00:00"));
        //TODO: implementieren
    }
    
    @Test
    public final void testGetConflicts() {
        ConflictsModel conModel = new MySQLConflictsModel(con);

        List<Conflict> conList = conModel.getConflicts();
        assertThat(conList, notNullValue());
        //TODO: implementieren
    }
    
    @Test
    public final void testDeleteConflict() {
        ConflictsModel conModel = new MySQLConflictsModel(con);
        conModel.deleteConflict(1);
        //TODO: implementieren
    }
    
    @Test
    public final void testDeleteConflicts() {
        ConflictsModel conModel = new MySQLConflictsModel(con);
        conModel.deleteConflicts();
        //TODO: implementieren
    }
}
