package dbaccess.models.mysql;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sqlutils.TestDBConnector;
import dbaccess.data.EmployeeCalendar;
import dbaccess.models.EmployeeCalendarModel;

public class MySQLEmployeeCalendarModelTest {
    private EmployeeCalendarModel empCalModel;

    @Before
    public final void setup() {
        empCalModel=new MySQLEmployeeCalendarModel(TestDBConnector.getConnection());
    }
    
    @After
    public final void cleanup() {
        empCalModel = null;
    }

    @Test
    public final void testAddNewEmployeeCalendar() {
    
        // empCalModel.addNewEmployeeCalendar(new
        // EmployeeCalendar(1,1,DateFormat.getInstance().parse("2014-01-13 00:00:00"),DateFormat.getInstance().parse("2014-01-14 00:00:00"),"Test_Beschreibung",true,false));
        // TODO: implementieren
    }

    @Test
    public final void testGetEmployeeCalendar() {
        List<EmployeeCalendar> empCalList = empCalModel.getEmployeeCalendar();
        assertThat(empCalList, notNullValue());
        // TODO: implementieren
    }

    @Test
    public final void testGetEmployeeCalendar1() {
        EmployeeCalendar empCal = empCalModel.getEmployeeCalendar(1);
        assertThat(empCal, notNullValue());
        // TODO: implementieren
    }

    @Test
    public final void testGetEmployeeCalendarForFID() {
        List<EmployeeCalendar> empCalList = empCalModel
                .getEmployeeCalendarForFID(1);
        assertThat(empCalList, notNullValue());
        // TODO: implementieren
    }

    @Test
    public final void testGetEmployeeCalendarInDateRange() {
        // EmployeeCalendarModel empCalModel=new
        // MySQLEmployeeCalendarModel(con);
        // List<EmployeeCalendar>
        // empCalList=empCalModel.getEmployeeCalendarInDateRange(DateFormat.getInstance().parse("2014-01-13"),DateFormat.getInstance().parse("2014-01-14"));
        // assertThat(empCalList, notNullValue());
        // TODO: implementieren
    }

    @Test
    public final void testGetEmployeeCalendarInDateRange1() {
        // EmployeeCalendarModel empCalModel=new
        // MySQLEmployeeCalendarModel(con);
        // List<EmployeeCalendar>
        // empCalList=empCalModel.getEmployeeCalendarInDateRange(DateFormat.getInstance().parse("2014-01-13"),DateFormat.getInstance().parse("2014-01-14");
        // boolean);
        // assertThat(empCalList, notNullValue());
        // TODO: implementieren
    }

    @Test
    public final void testDeleteEmployeeCalendar() {
        // EmployeeCalendarModel empCalModel=new
        // MySQLEmployeeCalendarModel(con);
        // empCalModel.deleteEmployeeCalendar(1);
        // TODO: implementieren
    }
}
