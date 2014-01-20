package dbaccess.models.mysql;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sqlutils.TestDBConnector;
import dbaccess.data.Employee;
import dbaccess.models.EmployeesModel;

public class MySQLEmployeesModelTest {
    private EmployeesModel empModel;

    @Before
    public final void setup() {
        empModel=new MySQLEmployeesModel();
    }
    
    @After
    public final void cleanup() {
        empModel = null;
    }
    
    @Test
    public final void testAddNewEmployee() {
        Employee ep=new Employee();
        ep.setId(10);
        ep.setLogin("TestLogin");
        ep.setLast_name("Test");
        ep.setFirst_name("Nur ein");
        ep.setProject_leader(true);
        ep.setPassword("passwort");
        ep.setDaily_rate(42.5);
        ep.setTime_preference(10);
        //empModel.addNewEmployee(ep);
        //TODO: implementieren
    }
    
    @Test
    public final void testGetEmployee() {
        List<Employee> empList=empModel.getEmployee();
        assertThat(empList, notNullValue());
        //TODO: implementieren
    }
    
    @Test
    public final void testGetEmployee1() {
        Employee employee=empModel.getEmployee("TestLogin");
        assertThat(employee, notNullValue());
        //TODO: implementieren
    }
    
    @Test
    public final void testGetEmployee2() {
        List<Employee> empList=empModel.getEmployee(true);
        assertThat(empList, notNullValue());
        //TODO: implementieren
    }
    
    @Test
    public final void testUpdateEmployee() {
        Employee ep=new Employee();
        ep.setId(10);
        ep.setLogin("NeuerLogin");
        ep.setLast_name("Test");
        ep.setFirst_name("Immernoch ein");
        ep.setProject_leader(false);
        ep.setPassword("1234");
        ep.setDaily_rate(42.5);
        ep.setTime_preference(10);
        
        
        empModel.updateEmployee(ep);
        //TODO: implementieren
    }
    
}
