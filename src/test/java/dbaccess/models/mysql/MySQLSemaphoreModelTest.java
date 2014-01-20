package dbaccess.models.mysql;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.notNullValue;
import dbaccess.models.SemaphoreModel;

import org.junit.*;

import sqlutils.TestDBConnector;
import sqlutils.TestData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MySQLSemaphoreModelTest {

	private SemaphoreModel semaphoreModel;
	
	@Before
    public final void setup() {		
        semaphoreModel = new MySQLSemaphoreModel();
    }

    @After
    public final void cleanup() {
        semaphoreModel = null;
    }
    
    @Test
    public final void testSemaphore(){
    	boolean entered = semaphoreModel.enterSemaphore("pl", 1);    	
    	assertThat(entered, equalTo(true));
    	
    	entered = semaphoreModel.enterSemaphore("pl", 1);    	
    	assertThat(entered, equalTo(false));
    	
    	entered = semaphoreModel.enterSemaphore("pl2", 1);    	
    	assertThat(entered, equalTo(true));
    	
    	entered = semaphoreModel.enterSemaphore("pl2", 1);    	
    	assertThat(entered, equalTo(false));
    	
    	semaphoreModel.leaveSemaphore("pl2", 1);
    	
    	entered = semaphoreModel.enterSemaphore("pl2", 1);    	
    	assertThat(entered, equalTo(true));
    	
    	entered = semaphoreModel.enterSemaphore("pl", 2);    	
    	assertThat(entered, equalTo(false));
    	
    	entered = semaphoreModel.enterSemaphore("pl", 2, true);    	
    	assertThat(entered, equalTo(true));
    	
    	entered = semaphoreModel.enterSemaphore("pl", 1, true);    	
    	assertThat(entered, equalTo(true));
    	
    	entered = semaphoreModel.enterSemaphore("pl", 2, false);    	
    	assertThat(entered, equalTo(false));
    	
    	semaphoreModel.leaveSemaphore("pl", 1);
    	
    	entered = semaphoreModel.enterSemaphore("pl", 2, false);    	
    	assertThat(entered, equalTo(true));    	
    }   
  
    
}
