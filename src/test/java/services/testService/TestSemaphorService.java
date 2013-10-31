package services.testService;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


import jdbcConnection.MDBConnect;

import org.junit.Before;
import org.junit.Test;

import dbServices.SemaphoreService;

import exception.SemaphorException;

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
 * Diese Klasse dient zum Testen der Funktionalitaet der SemaphoreService Klasse.<br/>
 * 
 * @author Jens Eckes
 * @version 2.0 - 2012-08-21
 */
public class TestSemaphorService {
	private final String path = "c:\\users\\vito\\desktop\\WBS_v1-9 - ADAC Test.mdb";
	private final String leaderID = "Leiter";
	
	@Before
	public void initTest(){
		MDBConnect.setPathDB(path);
	}
	
	
	@Test
	public void testAcquireLeaderSemaphore(){
		try {
			assertTrue(leaderID.equals(SemaphoreService.acquireLeaderSemaphore(leaderID)));
		} catch (SemaphorException e) {
			e.printStackTrace();
			fail();
		}
		try {
			assertFalse(leaderID.equals(SemaphoreService.acquireLeaderSemaphore(leaderID)));
		} catch (SemaphorException e) {
			e.printStackTrace();
			assertTrue(true);
		}
	}

	@Test
	public void testReleaseLeaderSemaphore(){
		assertTrue(SemaphoreService.releaseLeaderSemaphore(leaderID));
	}

}
