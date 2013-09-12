package services.testService;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import jdbcConnection.MDBConnect;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import calendar.TimeCalc;


import functions.WpManager;
import globals.Workpackage;

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
 * Diese Klasse dient zum Testen.<br/>
 * 
 * @author Michael Anstatt, Jens Eckes
 * @version 2.0 - 2012-08-21
 */
public class TestTime {
	private final String path = "c:\\users\\vito\\Desktop\\WBS_v2-0 - ADAC.mdb";

	Workpackage oap;
	Workpackage child1;
	Workpackage child2;
	Workpackage oap2;
	Workpackage child1a;

	Date allOverStartHope;

	@Before
	public void initTest() {
		MDBConnect.setPathDB(path);
		WpManager.loadDB();

		GregorianCalendar cal = new GregorianCalendar();
		cal.set(Calendar.YEAR, 2012);
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.HOUR_OF_DAY, 14);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		allOverStartHope = cal.getTime();

		Workpackage root = WpManager.getRootAp();
		root.setStartDateHope(allOverStartHope);
		WpManager.updateAP(root);

		System.out.println(WpManager.getRootAp().getStartDateHope());

		oap = new Workpackage();
		oap.setIstOAP(true);
		oap.setName("Das OAP");
		oap.setFid_Leiter("michael");
		oap.setLvlIDs(new String[] { "1", "0", "0", "0", "0", "0" });
		oap.addWorker("michael");
		oap.addWorker("depp");
		
		child1 = new Workpackage();
		child1.setFid_Leiter("michael");
		child1.setName("Planung");
		child1.setBac(446.0);
		child1.setLvlIDs(new String[] { "1", "1", "0", "0", "0", "0" });
		child1.addWorker("michael");
		child1.addWorker("depp");

		child2 = new Workpackage();
		child2.setFid_Leiter("michael");
		child2.setName("Durchführung");
		child2.setBac(664.0);
		child2.setLvlIDs(new String[] { "1", "2", "0", "0", "0", "0" });
		child2.addWorker("michael");
		child2.addWorker("depp");
		
		oap2 = new Workpackage();
		oap2.setIstOAP(true);
		oap2.setName("Das OAP 2");
		oap2.setFid_Leiter("michael");
		oap2.setLvlIDs(new String[] { "2", "0", "0", "0", "0", "0" });
		oap2.addWorker("michael");
		
		child1a = new Workpackage();
		child1a.setFid_Leiter("michael");
		child1a.setName("Planung 2");
		child1a.setBac(435.0);
		child1a.setLvlIDs(new String[] { "2", "1", "0", "0", "0", "0" });
		child1a.addWorker("michael");
		

		WpManager.insertAP(oap);
		WpManager.insertAP(child1);
		WpManager.insertAP(child2);
		
		WpManager.insertAP(oap2);
		WpManager.insertAP(child1a);

		WpManager.insertFollower(child2, child1);
		WpManager.insertFollower(oap2, oap);

	}

	@Test
	public void testCalculationWithoutBAC() {

		new TimeCalc();

		System.out.println();
		System.out.println("TESTERGEBNISSE ==================================================================");
		
		System.out.println(oap + " hat errechnetes Startdatum " + oap.getStartDateCalc());
		System.out.println(oap + " hat errechnetes Enddatum " + oap.getEndDateCalc());

		System.out.println("--------------");

		System.out.println(child1 + " hat errechnetes Startdatum " + child1.getStartDateCalc());
		System.out.println(child1 + " hat errechnetes Enddatum " + child1.getEndDateCalc());

		System.out.println("--------------");

		System.out.println(child2 + " hat errechnetes Startdatum " + child2.getStartDateCalc());
		System.out.println(child2 + " hat errechnetes Enddatum " + child2.getEndDateCalc());
		
		System.out.println("--------------");

		System.out.println(oap2 + " hat errechnetes Startdatum " + child2.getStartDateCalc());
		System.out.println(oap2 + " hat errechnetes Enddatum " + child2.getEndDateCalc());
		
		System.out.println("--------------");

		System.out.println(child1a + " hat errechnetes Startdatum " + child2.getStartDateCalc());
		System.out.println(child1a + " hat errechnetes Enddatum " + child2.getEndDateCalc());
		
		assertTrue(child2.getEndDateCalc() != null && child2.getEndDateCalc().equals(oap.getEndDateCalc()));

	}


	@After
	public void cleartXtDB() {

		 oap.removeWorker("michael");
		 child1.removeWorker("michael");
		 child2.removeWorker("michael");

		 oap.removeWorker("depp");
		 child1.removeWorker("depp");
		 child2.removeWorker("depp");
		 
		 oap2.removeWorker("michael");
		 child1a.removeWorker("michael");
		
		 WpManager.removeFollower(child2, child1);
		 WpManager.removeFollower(oap2, oap);

		 WpManager.removeAP(child1);
		 WpManager.removeAP(child2);
		 WpManager.removeAP(oap);
		 
		 WpManager.removeAP(child1a);
		 WpManager.removeAP(oap2);
	}
}
