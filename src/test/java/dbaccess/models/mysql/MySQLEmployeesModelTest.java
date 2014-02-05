package dbaccess.models.mysql;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import jdbcConnection.SQLExecuter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sqlutils.TestData;
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
    public final void testAddNewEmployeeAndDeleteEmployee() {
        Employee ep = new Employee();
        ep.setLogin("test");
        ep.setPassword("1234");
        ep.setLast_name("Test");
        ep.setFirst_name("Nur ein");
        ep.setProject_leader(true);
        ep.setDaily_rate(42.5);
        ep.setTime_preference(10);

        empModel.addNewEmployee(ep);

        Employee employee = empModel.getEmployee("test");
        assertThat(employee, notNullValue());
        assertThat(employee.getId(),equalTo(5));
        assertThat(employee.getLast_name(),equalTo("Test"));
        assertThat(employee.getFirst_name(),equalTo("Nur ein"));
        assertThat(employee.isProject_leader(),equalTo(true));
        assertThat(employee.getDaily_rate(),equalTo(42.5));
        assertThat(employee.getTime_preference(),equalTo(10));

        empModel.deleteEmployee(employee.getId());

        Employee deletedEmpl = empModel.getEmployee("test");
        assertThat(deletedEmpl, nullValue());

        TestData.reloadData(SQLExecuter.getConnection());
    }
    
    @Test
    public final void testGetEmployee() {
        //get all employees
        List<Employee> empList=empModel.getEmployee();
        assertThat(empList, notNullValue());
        assertThat(empList.size(), equalTo(4));
        assertThat(empList.get(0).getLogin(),equalTo("Leiter"));
        assertThat(empList.get(1).getLogin(),equalTo("h.mueller"));
        assertThat(empList.get(2).getLogin(),equalTo("p.pan"));
        assertThat(empList.get(3).getId(),equalTo(4));
        assertThat(empList.get(3).getLogin(),equalTo("b.schmidt"));
        assertThat(empList.get(3).getLast_name(),equalTo("Schmidt"));
        assertThat(empList.get(3).getFirst_name(),equalTo("Bernd"));
        assertThat(empList.get(3).isProject_leader(),equalTo(false));
        assertThat(empList.get(3).getDaily_rate(),equalTo(300.0));
        assertThat(empList.get(3).getTime_preference(),equalTo(0));
    }
    
    @Test
    public final void testGetEmployee1() {
        //get employee with login name
        Employee employee=empModel.getEmployee("b.schmidt");
        assertThat(employee, notNullValue());
        assertThat(employee.getId(),equalTo(4));
        assertThat(employee.getLast_name(),equalTo("Schmidt"));
        assertThat(employee.getFirst_name(),equalTo("Bernd"));
        assertThat(employee.isProject_leader(),equalTo(false));
        assertThat(employee.getDaily_rate(),equalTo(300.0));
        assertThat(employee.getTime_preference(),equalTo(0));
    }
    
    @Test
    public final void testGetEmployee2() {
        //get employees without leader rights
        List<Employee> empList=empModel.getEmployee(true);
        assertThat(empList, notNullValue());
        assertThat(empList.size(),equalTo(2));
        assertThat(empList.get(0).getLogin(),equalTo("p.pan"));
        assertThat(empList.get(1).getLogin(),equalTo("b.schmidt"));
    }
    
    @Test
    public final void testUpdateEmployee() {
        //Setup employee
        Employee ep=empModel.getEmployee("p.pan");
        ep.setProject_leader(true);
        ep.setDaily_rate(122.25);
            
        empModel.updateEmployee(ep);
        ep=empModel.getEmployee("p.pan");
        
        assertThat(ep.isProject_leader(),equalTo(true));
        assertThat(ep.getDaily_rate(),equalTo(122.25));
        
        TestData.reloadData(SQLExecuter.getConnection());
    }
    
}
