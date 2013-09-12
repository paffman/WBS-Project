package services.testService;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import jdbcConnection.MDBConnect;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import calendar.Availability;
import calendar.Day;
import dbServices.CalendarService;



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
 * Diese Klasse dient zum .<br/>
 * 
 * @author Michael Anstatt
 * @version 2.0 - 2012-08-21
 */

public class TestAvailabilityServices {

	private final String path = "c:\\users\\vito\\Desktop\\AVManagerTest.mdb";

	private Date startTime;
	private Date endTime;

	private Day startDay;
	private Day endDay;

	private Availability av;

	@Before
	public void initTest() {

		MDBConnect.setPathDB(path);
		WpManager.loadDB();

		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(new Date(System.currentTimeMillis()));
		cal.set(Calendar.DATE, 5);
		cal.set(Calendar.MONTH, 2);

		cal.set(Calendar.HOUR_OF_DAY, 13);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		startTime = cal.getTime(); // 5.3.2012 13:00

		cal.add(Calendar.HOUR, 3);

		endTime = cal.getTime(); // 5.3.2012 16:00

		startDay = new Day(startTime);
		endDay = new Day(startTime);

		System.out.println();

		av = new Availability("Test", true, false, startTime, endTime);
	}

	@Test
	public void testSameDay() {
		System.out.println("SAME DAY --------------------------------");
		System.out.println(av.getStartDay() + " == " + av.getEndDay());
		assertTrue(av.getStartDay().equals(startDay));
		assertTrue(av.getEndDay().equals(endDay));
		assertTrue(av.getStartDay().equals(av.getEndDay()));
		System.out.println();
	}

	@Test
	public void testOtherDay() {
		System.out.println("OTHER DAY -------------------------------");
		System.out.println(av.getStartTime() + " != " + av.getEndTime());
		assertTrue(!av.getStartTime().equals(av.getEndTime()));
		assertTrue(!av.getStartTime().equals(startTime));
		assertTrue(av.getStartTime().equals(startDay));
		assertTrue(!av.getEndTime().equals(endTime));
		System.out.println();
	}

	@Test
	public void testNextDay() {
		System.out.println("NEXT DAY -------------------------------");
//		System.out.println(av.getStartDay() + " != " + av.getEndDay(true));
//		assertTrue(av.getStartDay().equals(av.getEndDay(true)));
		assertTrue(av.getEndDay().equals(endDay));
		assertTrue(av.getStartDay().equals(av.getEndDay()));
		System.out.println();
	}

	@Test
	public void testGetRealWorkerAvailability() {
		GregorianCalendar cal = new GregorianCalendar();

		cal.set(Calendar.YEAR, 2012);
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DATE, 13);
		cal.set(Calendar.HOUR_OF_DAY, 10);
		Day checkDay = new Day(cal.getTime());
		System.out.println(CalendarService.getRealWorkerAvailability("michael", checkDay , new Day(checkDay, true)));
		assertTrue(CalendarService.getRealWorkerAvailability("michael", checkDay, new Day(checkDay, true)).isEmpty());
	}

	@After
	public void cleartXtDB() {

	}
}
