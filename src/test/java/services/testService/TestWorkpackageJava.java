package services.testService;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import jdbcConnection.MDBConnect;

import org.junit.Before;
import org.junit.Test;

import functions.WpManager;
import globals.Workpackage;

import wpShow.WPShow;

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
 * Diese Klasse dient zum Testen der Funktionalitaet der WpManager Klasse (Komplett Java seitig).<br/>
 * 
 * @author Jens Eckes
 * @version 2.0 - 2012-08-21
 */
public class TestWorkpackageJava {
	private final String path = "c:\\users\\vito\\desktop\\WBS_v1-9 - ADAC Test.mdb";
	
	private final String workerID = "Ascher";
	private final String paremtID = "99.99.0.0.0.0";
	private final String nextID = "99.99.5.0.0.0";
	private final String ebeneId = "9.9.9.9.9.9";
	private final String[] basdartID = new String[]{"100","0", "0", "0", "0", "0"};
	private final int anzahlEbene = 6;
	
	private String[] ids = new String[]{"99","99", "0", "0", "0", "0"};
	private Workpackage ebenenWp = new Workpackage(9, 9, 9, "9.9.9", "fu", "bar", 0.0, 100.0, 0.0, 0.0, 100, 100, 0.0, 0.0, 0.0, 0.0, new Date(), false, false, workerID, new ArrayList<String>(), null, null, null);
	private Workpackage parent;
	private Workpackage child1;
	private Workpackage child2;
	private Workpackage child3;
	private Workpackage child4;
	private Workpackage basdart;
	
	@Before
	public void initTest(){
		MDBConnect.setPathDB(path);
		WpManager.loadDB();
	}

	@Test
	public void testGetLvlIDs(){
		assertTrue("Bei dem Einfuegen des EbenenTest Wps ist ein Fehler aufgetreten", WpManager.insertAP(ebenenWp));
		Integer[] rootArray = WpManager.getRootAp().getLvlIDs();
		Integer[] otherArray = WpManager.getWorkpackage(ebeneId).getLvlIDs();
		assertTrue("Bei dem Loeschen des EbenenTest Wps ist ein Fehler aufgetreten", WpManager.removeAP(ebenenWp));

		assertNotNull(rootArray);
		assertNotNull(otherArray);
		assertTrue(rootArray.length == otherArray.length);
		assertTrue("Die laenge des RootArray stimmt nicth mit der eben tiefe ueberein root: " + rootArray.length + " Ebene: " + anzahlEbene,
				rootArray.length == anzahlEbene);
		assertTrue("Die laenge eines anderen Array stimmt nicth mit der eben tiefe ueberein anderes: " + rootArray.length + " Ebene: " + anzahlEbene,
				otherArray.length == anzahlEbene);
		System.out.println("RootArray");
		for (Integer integer : rootArray) {
			System.out.println(integer);
		}
		System.out.println("Anderes Array");
		for (Integer integer : otherArray) {
			System.out.println(integer);
		}
	}
	
	@Test
	public void testGetUaps(){
		initTestWps();
		Set<Workpackage> uapSet = WpManager.getUAPs(parent);
		System.out.println("Size: " + uapSet.size());
		for (Workpackage workpackage : uapSet) {
			if(workpackage.getStringID().equals(basdart.getStringID())){
				System.out.println("A basdart has been found: " + workpackage.getStringID());
			}else{
				System.out.println("WpID " + workpackage.getStringID());
			}
			
		}
		teardownTestWps();
		assertNotNull(uapSet);
		assertTrue(uapSet.size() == 4);
	}
	
	@Test
	public void testGetOAP(){
		initTestWps();
		String oapC1 = child1.getOAPID();
		String oapC2 = child2.getOAPID();
		teardownTestWps();
		System.out.println("parent: " + paremtID);
		System.out.println("oapC1: " + oapC1 );
		System.out.println("oapC2: " + oapC2 );
		assertTrue("oapC1 != parent", paremtID.equals(oapC1));
		assertTrue("oapC2 != parent", paremtID.equals(oapC2));
	}
	
	@Test
	public void testGetNextId(){
		initTestWps();
		String parentIDNext = WPShow.getNextId(paremtID);
		teardownTestWps();
		System.out.println("nextID: " + nextID);
		System.out.println("parentIDNext: " + parentIDNext);
		assertTrue("parentIDNext != nextID",parentIDNext.equals(nextID));
	}
	
	private void initTestWps(){
		parent = new Workpackage();
		child1 = new Workpackage();
		child2 = new Workpackage();
		child3 = new Workpackage();
		child4 = new Workpackage();
		basdart = new Workpackage();
		
		parent.setName("parent");
		child1.setName("child1");
		child2.setName("child2");
		child3.setName("child3");
		child4.setName("child4");
		basdart.setName("basdart");

		parent.setBeschreibung("parent");
		child1.setBeschreibung("child1");
		child2.setBeschreibung("child2");
		child3.setBeschreibung("child3");
		child4.setBeschreibung("child4");
		basdart.setBeschreibung("basdart");
		
		parent.setLvlIDs(ids);
		parent.setFid_Leiter(workerID);
		parent.setIstOAP(true);
		ids[2] = "1";
		child1.setLvlIDs(ids);
		child1.setFid_Leiter(workerID);
		ids[2] = "2";
		child2.setLvlIDs(ids);
		child2.setFid_Leiter(workerID);
		ids[2] = "3";
		child3.setLvlIDs(ids);
		child3.setFid_Leiter(workerID);
		ids[2] = "4";
		child4.setFid_Leiter(workerID);
		child4.setLvlIDs(ids);
		basdart.setLvlIDs(basdartID);
		basdart.setFid_Leiter(workerID);
		WpManager.insertAP(parent);
		WpManager.insertAP(child1);
		WpManager.insertAP(child2);
		WpManager.insertAP(child3);
		WpManager.insertAP(child4);
		WpManager.insertAP(basdart);
	}
	
	private void teardownTestWps(){
		WpManager.removeAP(basdart);
		WpManager.removeAP(child1);
		WpManager.removeAP(child2);
		WpManager.removeAP(child3);
		WpManager.removeAP(child4);
		WpManager.removeAP(parent);
	}
}
