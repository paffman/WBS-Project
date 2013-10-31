package services.testService;

import java.sql.SQLException;
import java.util.Date;
import java.util.Set;

import jdbcConnection.MDBConnect;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import dbServices.ConflictService;


import wpConflict.Conflict;

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
 * Diese Klasse dient zum Testen der Klasse ConflictService.<br/>
 * 
 * @author Jens Eckes
 * @version 2.0 - 2012-08-21
 */
public class TestConflictService {
	//Pfad fuer Test-Datenbank angeben
	private final String path = "E:\\EigeneDateien\\MyDocuments\\Fh\\ss12\\Proj\\BDZumTesten\\WBS_v2-1 - ADAC Test.mdb";
	private Conflict conflict = null;
	
	@Before
	public void testInit(){
		MDBConnect.setPathDB(path);
		conflict = new Conflict(new Date(), 0, "Ascher", "1.3.17.0.0.0", "1.3.18.0.0.0");
	}
	
	@Test
	public void TestGetAllConflicts(){
		Set<Conflict> ergSet = ConflictService.getAllConflicts();
		assertNotNull(ergSet);
		System.out.println(ergSet.size());
		for (Conflict conflict : ergSet) {
			System.out.println(conflict);
		}
	}
	
	@Test
	public void testSetConflict(){
		assertTrue(ConflictService.setConflict(conflict));
		Set<Conflict> ergSet = ConflictService.getAllConflicts();
		assertNotNull(ergSet);
//		boolean contains = false;
//		for (Conflict conf : ergSet) {
////			System.out.println("A: " + conflict);
////			System.out.println("B: " + conf);
//			if(conf.equals(conflict)){
//				contains = true;
//			}
////			System.out.println("Hashcode A: " + conf.hashCode() + " Hashcode B: " + conflict.hashCode() );
////			System.out.println("schleife " + contains);
//		}
//		assertTrue(contains);
		assertTrue(ergSet.contains(conflict));
	}
	
	@Test
	public void testDeleteConflict(){
		testSetConflict();
		try {
			ConflictService.deleteConflict(conflict);
			Set<Conflict> ergSet = ConflictService.getAllConflicts();
			assertFalse(ergSet.contains(conflict));
		} catch (SQLException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testDeleteAll(){
		try {
			ConflictService.deleteAll();
			assertTrue(ConflictService.getAllConflicts().size() == 0);
		} catch (SQLException e) {
			e.printStackTrace();
			fail();
		}
	}
}
