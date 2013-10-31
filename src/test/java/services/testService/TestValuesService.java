package services.testService;

import static org.junit.Assert.*;
import jdbcConnection.MDBConnect;

import org.junit.Before;
import org.junit.Test;

import dbServices.ValuesService;


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
 * Diese Klasse dient zum Testen der Klasse ValuesService.<br/>
 * 
 * @author Jens Eckes
 * @version 2.0 - 2012-08-21
 */
public class TestValuesService {
	//Pfad fuer Test-Datenbank angeben
	private final String path = "c:\\users\\vito\\desktop\\WBS_v1-9 - ADAC Test.mdb";
	private final String apID = "1.3.18.0.0.0";
	private final double pv = 50.0;
	
	@Before
	public void testInit(){
		MDBConnect.setPathDB(path);
	}
	
	@Test
	public void testGetPVs(){
		
	}
	
	@Test
	public void testSavePV(){
		
	}
	
	@Test
	public void testDeleteAllPV(){
		
	}
	
	@Test
	public void testDeletePV(){
		
	}
	
	@Test
	public void tesGetApPv(){
		assertTrue(pv == ValuesService.getApPv(apID));
	}
}
