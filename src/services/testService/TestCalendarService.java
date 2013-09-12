package services.testService;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import jdbcConnection.MDBConnect;

import org.junit.Before;
import org.junit.Test;

import calendar.Availability;
import calendar.DateFunctions;
import calendar.Day;
import dbServices.CalendarService;


import wpOverview.tabs.AvailabilityGraph;

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
 * Diese Klasse dient zum Testen der Klasse CalendarService.<br/>
 * 
 * @author Jens Eckes
 * @version 2.0 - 2012-08-21
 */
public class TestCalendarService {
	//Pfad fuer Test-Datenbank angeben
	private final String path = "c:\\users\\vito\\desktop\\TimeTestProject.mdb";
	private Date from = null;
	private Date to = null;
	private String userId = null;
	
	@Before
	public void testInit(){
		MDBConnect.setPathDB(path);
		
		Calendar c = Calendar.getInstance();
		c.set(2012, 0, 1, 00, 00, 00);
		from = c.getTime();
		c.set(2012, 11, 1, 00, 00, 00);
		to = c.getTime();
		
		userId = "Ascher";
	}
	
	@Test
	public void testGetAllAvailability(){
		Set<Availability> ergSet = CalendarService.getAllAvailability();
		assertNotNull(ergSet);
		System.out.println(ergSet.size());
		for (Availability availability : ergSet) {
			System.out.println(availability);
		}
	}
	
	@Test
	public void testGetAllAvailabilityFromTo(){
		Set<Availability> ergSet = CalendarService.getAllAvailability(from, to);
		assertNotNull(ergSet);
		System.out.println(ergSet.size());
		for (Availability availability : ergSet) {
			System.out.println(availability);
		}
	}
	
	@Test
	public void testGetAllWorkerAvailability(){
		Set<Availability> ergSet = CalendarService.getAllWorkerAvailability(userId);
		assertNotNull(ergSet);
		System.out.println(ergSet.size());
		for (Availability availability : ergSet) {
			System.out.println(availability);
		}
	}
	
	@Test
	public void testGetAllWorkerAvailabilityFromTo(){
		Set<Availability> ergSet = CalendarService.getAllWorkerAvailability(userId, from, to);
		assertNotNull(ergSet);
		System.out.println(ergSet.size());
		for (Availability availability : ergSet) {
			System.out.println(availability);
		}
	}
	
	@Test
	public void testGetProjectAvailability(){
		Set<Availability> ergSet = CalendarService.getProjectAvailability();
		System.out.println(ergSet.size());
		for (Availability availability : ergSet) {
			System.out.println(availability);
		}
	}
	
	@Test
	public void testGetProjectAvailabilitFromTo(){
		Set<Availability> ergSet = CalendarService.getProjectAvailability(new Day(from), new Day(to));
		System.out.println(ergSet.size());
		for (Availability availability : ergSet) {
			System.out.println(availability);
		}
	}
	
	@Test
	public void testSetWorkerAvailability(){
		Calendar c = Calendar.getInstance();
		c.set(2012,6,20,14,30);
		from = c.getTime();
		c.set(2012,6,20,15,30);
		to = c.getTime();
		boolean allDay = false;
		boolean available = false;
		boolean found = false;
		Availability avWorker = new Availability(userId, allDay, available, from, to);
		try {
			CalendarService.setWorkerAvailability(userId, avWorker);
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Beim Speichern der Verfuegbarkeit ist ein Fehler aufgetreten.");
		}
		Set<Availability> allAv = CalendarService.getAllWorkerAvailability(userId);
		for (Availability av : allAv) {
			if(av.getUserID().equals(userId)){
				if(av.isAllDay() == allDay && av.isAvailabe() == available && 
						DateFunctions.equalsDate(from, av.getStartTime()) && DateFunctions.equalsDate(to, av.getEndTime())){
					found = true;
				}
			}else{
				fail("Eine Verfuegbarkeit eins Falschen Mitarbeits wurde aus der DB gelesen.");
			}
		}
		assertTrue(found);
	}
	
	@Test
	public void testSetProjectAvailability(){
		Calendar c = Calendar.getInstance();
		c.set(2012,6,20,14,30);
		from = c.getTime();
		c.set(2012,6,20,15,30);
		to = c.getTime();
		boolean allDay = false;
		boolean available = false;
		boolean found = false;
		Availability avProjekt = new Availability("Projekt", allDay, available, from, to, "blablub");
		assertTrue("Beim Speichern der Verfuegbarkeit ist ein Fehler aufgetreten.", CalendarService.setProjectAvailability(avProjekt));
		Set<Availability> allAv = CalendarService.getProjectAvailability();
		for (Availability av : allAv) {
			if(av.getUserID().equals(AvailabilityGraph.PROJECT_WORKER.getLogin())){
				if(av.isAllDay() == allDay && av.isAvailabe() == available && 
						DateFunctions.equalsDate(from, av.getStartTime()) && DateFunctions.equalsDate(to, av.getEndTime())){
					found = true;
				}
			}else{
				fail("Eine Verfuegbarkeit eins Falschen Mitarbeits wurde aus der DB gelesen.");
			}
		}
		assertTrue(found);
	}
}
