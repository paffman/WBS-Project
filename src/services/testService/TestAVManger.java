package services.testService;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;

import jdbcConnection.MDBConnect;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import calendar.AVManager;
import calendar.Day;

import wpOverview.tabs.AvailabilityGraph;

import functions.WpManager;

/**
 * Studienprojekt:	PSYS WBS 2.0<br/>
 * 
 * Kunde:		Pentasys AG, Jens von Gersdorff<br/>
 * Projektmitglieder:<br/>
 *			Michael Anstatt,<br/>
 *			Marc-Eric Baumgärtner,<br/>
 *			Jens Eckes,<br/>
 *			Sven Seckler,<br/>
 *			Lin Yang<br/>
 * 
 * Diese Klasse dient .<br/>
 * 
 * @author Michael Anstatt
 * @version 2.0 - 2012-08-21
 */
public class TestAVManger {
	
	private final String path = "c:\\users\\vito\\Desktop\\AVManagerTest.mdb";
	
	AVManager avManager;
	GregorianCalendar cal;
	
	@Before
	public void initTest() {
		MDBConnect.setPathDB(path);
		WpManager.loadDB();
		avManager = new AVManager();
		cal = new GregorianCalendar();
		cal.clear();		
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.YEAR, 2012);
	}
	
	@Test
	public void testGetCompleteDayWorks1() {
		cal.set(Calendar.DATE, 11);
		Day actualDay = new Day(cal.getTime());
		Map<String, Integer> map = avManager.getCompleteDayWorks(actualDay);
		assertTrue(map.get("michael") == 0);
		assertTrue(map.get("depp") == 8);
		assertTrue(map.get("worker") == 3);
		assertTrue(map.get(AvailabilityGraph.PROJECT_WORKER.getLogin()) == 8);		
	}
	
	@Test
	public void testGetCompleteDayWorks2() {
		cal.set(Calendar.DATE, 13);
		Day actualDay = new Day(cal.getTime());
		Map<String, Integer> map = avManager.getCompleteDayWorks(actualDay);
		assertTrue(map.get("michael") == 0);
		assertTrue(map.get("depp") == 8);
		assertTrue(map.get("worker") == 8);
		assertTrue(map.get(AvailabilityGraph.PROJECT_WORKER.getLogin()) == 8);
	}
	
	@Test 
	public void testNextWorkingHourWeekend() {
		cal.set(Calendar.DATE, 14); //Samstag
		GregorianCalendar newCal = new GregorianCalendar();
		newCal.setTime(avManager.getNextWorkDate(cal.getTime()));
		System.out.println(newCal.getTime());
		assertTrue(newCal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY);
		assertTrue(newCal.get(Calendar.HOUR_OF_DAY) == 8);
		
		System.out.println("here");
		cal.set(Calendar.DATE, 13); //Freitag
		cal.set(Calendar.HOUR_OF_DAY, 19);
		newCal.setTime(avManager.getNextWorkDate(cal.getTime()));
		assertTrue(newCal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY);
		assertTrue(newCal.get(Calendar.HOUR_OF_DAY) == 8);
	}
	
	@Test 
	public void testNextWorkingHourPause() {
		cal.set(Calendar.DATE, 17); //Dienstag
		cal.set(Calendar.HOUR_OF_DAY, 13);
		GregorianCalendar newCal = new GregorianCalendar();
		newCal.setTime(avManager.getNextWorkDate(cal.getTime()));
		System.out.println(newCal.getTime());
		assertTrue(newCal.get(Calendar.DATE) == cal.get(Calendar.DATE));
		assertTrue(newCal.get(Calendar.HOUR_OF_DAY) == 14);
		cal.set(Calendar.HOUR_OF_DAY, 14);
		newCal.setTime(avManager.getNextWorkDate(cal.getTime()));
		assertTrue(newCal.getTime().equals(cal.getTime()));
	}
	
	@Test 
	public void testNextWorkingHourProblem() {
		cal.set(Calendar.DATE, 18); //Mittwoch
		cal.set(Calendar.HOUR_OF_DAY, 16);
		GregorianCalendar newCal = new GregorianCalendar();
		newCal.setTime(avManager.getNextWorkDate(cal.getTime()));
		assertTrue(newCal.getTime().equals(cal.getTime()));
	}
	
	@Test
	public void testNextWorkTime() {
		cal.set(Calendar.DATE, 12); //Dienstag
		Day actualDay = new Day(cal.getTime());
		GregorianCalendar newCal = new GregorianCalendar();
		newCal.setTime(avManager.getNextWorkTime(actualDay, 8));
		System.out.println(newCal.getTime());
		assertTrue(newCal.get(Calendar.DATE) == 12);
		assertTrue(newCal.get(Calendar.HOUR_OF_DAY) == 18);
	}
	
	@After
	public void endTest() {
		
	}
}
