package dbaccess.models.mysql;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import dbaccess.data.EmployeeCalendar;
import dbaccess.models.EmployeeCalendarModel;

public class MySQLEmployeeCalendarModelTest {
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
    public final void testAddNewEmployeeCalendar() {
        //EmployeeCalendarModel empCalModel=new MySQLEmployeeCalendarModel(con);
        //empCalModel.addNewEmployeeCalendar(new EmployeeCalendar(1,1,"2014-01-13 00:00:00","2014-01-14 00:00:00","Test_Beschreibung",true,false));
        //TODO: implementieren
    }
    
    @Test
    public final void testGetEmployeeCalendar() {
        EmployeeCalendarModel empCalModel=new MySQLEmployeeCalendarModel(con);
        List<EmployeeCalendar> empCalList=empCalModel.getEmployeeCalendar();
        assertThat(empCalList, notNullValue());
        //TODO: implementieren
    }
        
    @Test
    public final void testGetEmployeeCalendar1() {
        EmployeeCalendarModel empCalModel=new MySQLEmployeeCalendarModel(con);
        EmployeeCalendar empCal=empCalModel.getEmployeeCalendar(1);
        assertThat(empCal, notNullValue());
        //TODO: implementieren
    }
    
    @Test
    public final void testGetEmployeeCalendarForFID() {
        EmployeeCalendarModel empCalModel=new MySQLEmployeeCalendarModel(con);
        List<EmployeeCalendar> empCalList=empCalModel.getEmployeeCalendarForFID(1);
        assertThat(empCalList, notNullValue());
        //TODO: implementieren
    }
    
    @Test
    public final void testGetEmployeeCalendarInDateRange() {
        //EmployeeCalendarModel empCalModel=new MySQLEmployeeCalendarModel(con);
        //TODO: welches Format hat datum?
        //List<EmployeeCalendar> empCalList=empCalModel.getEmployeeCalendarInDateRange("datum","datum");
        //assertThat(empCalList, notNullValue());
        //TODO: implementieren
    }
    
    @Test
    public final void testGetEmployeeCalendarInDateRange1() {
        //EmployeeCalendarModel empCalModel=new MySQLEmployeeCalendarModel(con);
        //TODO: welches Format hat datum?
        //List<EmployeeCalendar> empCalList=empCalModel.getEmployeeCalendarInDateRange("datum","datum", boolean);
        //assertThat(empCalList, notNullValue());
        //TODO: implementieren
    }
    
    @Test
    public final void testDeleteEmployeeCalendar() {
        //EmployeeCalendarModel empCalModel=new MySQLEmployeeCalendarModel(con);
        //empCalModel.deleteEmployeeCalendar(1);
        //TODO: implementieren
    }
}


