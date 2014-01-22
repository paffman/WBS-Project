package dbaccess.models.mysql;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import jdbcConnection.SQLExecuter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sqlutils.TestData;
import dbaccess.data.EmployeeCalendar;
import dbaccess.models.EmployeeCalendarModel;

public class MySQLEmployeeCalendarModelTest {
    private EmployeeCalendarModel empCalModel;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

    @Before
    public final void setup() {
        empCalModel = new MySQLEmployeeCalendarModel();
    }

    @After
    public final void cleanup() {
        empCalModel = null;
    }

    @Test
    public final void testAddNewEmployeeCalendar() {
        // add a new employee calendar

        // Setup employee calendar
        EmployeeCalendar empCal = new EmployeeCalendar();

        try {

            empCal.setFid_emp(1);
            empCal.setBegin_time(dateFormat.parse("2014-01-13 00:00:00"));
            empCal.setEnd_time(dateFormat.parse("2014-01-14 11:30:45"));
            empCal.setDescription("Test Beschreibung");
            empCal.setAvailability(true);
            empCal.setFull_time(true);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        empCalModel.addNewEmployeeCalendar(empCal);
        List<EmployeeCalendar> empCalList = empCalModel.getEmployeeCalendar();
        try {
            assertThat(empCalList, notNullValue());
            assertThat(empCalList.size(), equalTo(2));
            assertThat(empCalList.get(1).getFid_emp(), equalTo(1));
            assertThat(dateFormat.parse(empCalList.get(1).getBegin_time().toString()), equalTo(dateFormat.parse("2014-01-13")));
            assertThat(dateFormat.parse(empCalList.get(1).getEnd_time().toString()), equalTo(dateFormat.parse("2014-01-14")));
            assertThat(empCalList.get(1).getDescription(), equalTo("Test Beschreibung"));
            assertThat(empCalList.get(1).isAvailability(), equalTo(true));
            assertThat(empCalList.get(1).isFull_time(), equalTo(true));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        TestData.reloadData(SQLExecuter.getConnection());
    }

    @Test
    public final void testGetEmployeeCalendar() {
        // get all employee calendars

        List<EmployeeCalendar> empCalList = empCalModel.getEmployeeCalendar();

        try {

            assertThat(empCalList, notNullValue());
            assertThat(empCalList.size(), equalTo(1));
            assertThat(empCalList.get(0).getFid_emp(), equalTo(3));
            assertThat(dateFormat.parse(empCalList.get(0).getBegin_time().toString()), equalTo(dateFormat.parse("2014-01-13")));
            assertThat(dateFormat.parse(empCalList.get(0).getEnd_time().toString()), equalTo(dateFormat.parse("2014-01-14")));
            assertThat(empCalList.get(0).getDescription(), equalTo("Muss in Berufsschule"));
            assertThat(empCalList.get(0).isAvailability(), equalTo(false));
            assertThat(empCalList.get(0).isFull_time(), equalTo(true));

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Test
    public final void testGetEmployeeCalendar1() {
        // get employee calendar with id

        EmployeeCalendar empCal = empCalModel.getEmployeeCalendar(1);

        try {

            assertThat(empCal, notNullValue());
            assertThat(empCal.getFid_emp(), equalTo(3));
            assertThat(dateFormat.parse(empCal.getBegin_time().toString()), equalTo(dateFormat.parse("2014-01-13")));
            assertThat(dateFormat.parse(empCal.getEnd_time().toString()), equalTo(dateFormat.parse("2014-01-14")));
            assertThat(empCal.getDescription(), equalTo("Muss in Berufsschule"));
            assertThat(empCal.isAvailability(), equalTo(false));
            assertThat(empCal.isFull_time(), equalTo(true));

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public final void testGetEmployeeCalendarForFID() {
        // get employee calendars with fid

        List<EmployeeCalendar> empCalList = empCalModel.getEmployeeCalendarForFID(3);

        try {

            assertThat(empCalList, notNullValue());
            assertThat(empCalList, notNullValue());
            assertThat(empCalList.size(), equalTo(1));
            assertThat(empCalList.get(0).getFid_emp(), equalTo(3));
            assertThat(dateFormat.parse(empCalList.get(0).getBegin_time().toString()), equalTo(dateFormat.parse("2014-01-13")));
            assertThat(dateFormat.parse(empCalList.get(0).getEnd_time().toString()), equalTo(dateFormat.parse("2014-01-14")));
            assertThat(empCalList.get(0).getDescription(), equalTo("Muss in Berufsschule"));
            assertThat(empCalList.get(0).isAvailability(), equalTo(false));
            assertThat(empCalList.get(0).isFull_time(), equalTo(true));

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public final void testGetEmployeeCalendarInDateRange() {
        //get all employee calendars in a specific period
        //TODO: generell wegen datumsangaben nachfragen und abändern
        //TODO: macht die stored procedure das was sie soll? ...test funktioniert noch nicht
        List<EmployeeCalendar> empCalList;
        try {
            empCalList = empCalModel.getEmployeeCalendarInDateRange(dateFormat.parse("2014-01-10 00:00:00"),dateFormat.parse("2014-01-15 00:00:00"));
            assertThat(empCalList, notNullValue());
            assertThat(empCalList.size(),equalTo(1));
            assertThat(empCalList.get(0).getFid_emp(),equalTo(3));
            
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
        empCalModel.deleteEmployeeCalendar(1);
        List<EmployeeCalendar> empCalList = empCalModel.getEmployeeCalendar();
        
        assertThat(empCalList,equalTo(null));
        
        TestData.reloadData(SQLExecuter.getConnection());
    }
}
