package dbaccess.models.mysql;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import dbaccess.data.Employee;
import dbaccess.models.EmployeesModel;

public class MySQLEmployeesModelTest {
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
    public final void testAddNewEmployee() {
        //EmployeeModel empModel=new MySQLEmployeeModel(con);
        //empModel.addNewEmployee(new Employee(10,"TestLogin","Test","Nur ein",true,"passwort",42.5,10));
        //TODO: implementieren
    }
    
    @Test
    public final void testGetEmployee() {
        EmployeesModel empModel=new MySQLEmployeesModel(con);
        List<Employee> empList=empModel.getEmployee();
        assertThat(empList, notNullValue());
        //TODO: implementieren
    }
    
    @Test
    public final void testGetEmployee1() {
        EmployeesModel empModel=new MySQLEmployeesModel(con);
        Employee employee=empModel.getEmployee("TestLogin");
        assertThat(employee, notNullValue());
        //TODO: implementieren
    }
    
    @Test
    public final void testGetEmployee2() {
        EmployeesModel empModel=new MySQLEmployeesModel(con);
        List<Employee> empList=empModel.getEmployee(true);
        assertThat(empList, notNullValue());
        //TODO: implementieren
    }
    
    @Test
    public final void testUpdateEmployee() {
        EmployeesModel empModel=new MySQLEmployeesModel(con);
        empModel.updateEmployee(new Employee(10,"NeuerLogin","Test","Immernoch ein",false,"1234",42.5,10));
        //TODO: implementieren
    }
    
}
