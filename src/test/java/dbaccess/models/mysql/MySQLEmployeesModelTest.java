package dbaccess.models.mysql;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
        //get all employees
        List<Employee> empList=empModel.getEmployee();
        assertThat(empList, notNullValue());
        assertThat(empList.size(), equalTo(4));
        assertThat(empList.get(1).getId(),equalTo(2));
        assertThat(empList.get(1).getLast_name(),equalTo("Müller"));
        assertThat(empList.get(1).getFirst_name(),equalTo("Hans"));
        assertThat(empList.get(1).isProject_leader(),equalTo(true));
        assertThat(empList.get(1).getPassword(),equalTo("1234"));
        assertThat(empList.get(1).getDaily_rate(),equalTo(500.0));
        assertThat(empList.get(1).getTime_preference(),equalTo(0));
    }
    
    @Test
    public final void testGetEmployee1() {
        //get employee with login name
        Employee employee=empModel.getEmployee("h.mueller");
        assertThat(employee, notNullValue());
        assertThat(employee.getId(),equalTo(2));
        assertThat(employee.getLast_name(),equalTo("Müller"));
        assertThat(employee.getFirst_name(),equalTo("Hans"));
        assertThat(employee.isProject_leader(),equalTo(true));
        assertThat(employee.getPassword(),equalTo("1234"));
        assertThat(employee.getDaily_rate(),equalTo(500.0));
        assertThat(employee.getTime_preference(),equalTo(0));
    }
    
    @Test
    public final void testGetEmployee2() {
        //get employees without leader rights
        List<Employee> empList=empModel.getEmployee(true);
        assertThat(empList, notNullValue());
        assertThat(empList.size(),equalTo(2));
        assertThat(empList.get(0).getLogin(),equalTo("p.pan"));
        assertThat(empList.get(0).getLogin(),equalTo("b.schmidt"));
    }
    
    @Test
    public final void testUpdateEmployee() {
        
        //Setup employee
        Employee ep=empModel.getEmployee("h.mueller");
        ep.setProject_leader(false);
            
        empModel.updateEmployee(ep);
        ep=empModel.getEmployee("h.mueller");
        assertThat(ep.isProject_leader(),equalTo(false));
        //TODO: implementieren
    }
    
}
