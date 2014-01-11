package dbaccess.models.mysql;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.notNullValue;

import dbaccess.data.Workpackage;
import dbaccess.models.WorkpackageModel;
import org.hamcrest.CoreMatchers;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Collection;
import java.util.List;

public class MySQLWorkpackageModelTest {

    private static Connection con;

    @BeforeClass
    public static final void setupTest() {
        final String url = "jdbc:mysql://localhost:3306/";
        final String dbName = "mbtest";
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
    public final void testGetWorkpackage() {

        WorkpackageModel wpModel = new MySQLWorkpackageModel(con);

        List<Workpackage> wpList = wpModel.getWorkpackage();
        assertThat(wpList, notNullValue());

        assertThat("list should not be empty", wpList.size() > 0);
        assertThat("list should have 8 work packages", wpList.size() > 3);

    }

}
