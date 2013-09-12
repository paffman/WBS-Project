package services.testService;
import static org.junit.Assert.*;

import globals.Workpackage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import jdbcConnection.MDBConnect;

import org.junit.Before;
import org.junit.Test;

import dbServices.WorkpackageService;



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
 * Diese Klasse dient zum Testen der Funktionalitaet der Klasse WorkpackageService.<br/>
 * 
 * @author Jens Eckes
 * @version 2.0 - 2012-08-21
 */
public class TestWorkpackageService {
	private final String path = "c:\\users\\vito\\desktop\\WBS_v2-0 - ADAC.mdb";
	private final String workerID = "Ascher";
	private final String apID0 = "1.3.1.0.0.0";
	private final String apID1 = "1.3.2.0.0.0";
//	Variablen fuer die Tests testGetFollowers testGetAncestors ebenfalls auskommentiert
//	private final String followerID = "1.3.16.0.0.0";
//	private final String ancestorID = "1.3.15.0.0.0";
	private final String setFollowerId = "1.3.16.0.0.0";
	private final String setAcestorID = "1.3.15.0.0.0";
	private final String setFollowerId2 = "1.3.17.0.0.0";
	private final String setAcestorID2 = "1.3.16.0.0.0";
	private final String insertID = "99.99.0.0.0.0";
	private final String userId = "Ascher";
	private Date from = null;
	private Date to = null;
	
	private Workpackage insertWp = new Workpackage(99, 99, 0, "0.0.0", "fu", "bar", 0.0, 100.0, 0.0, 0.0, 100, 100, 0.0, 0.0, 0.0, 0.0, new Date(), false, false, workerID, new ArrayList<String>(), null, null, null);
	
	
	@Before
	public void initTest(){
		MDBConnect.setPathDB(path);
		Calendar c = Calendar.getInstance();
		c.set(2012, 0, 1, 00, 00, 00);
		from = c.getTime();
		c.set(2012, 11, 1, 00, 00, 00);
		to = c.getTime();
	}
	
	public TestWorkpackageService(){
	}
	
//	@Test
//	public void testGetRootAp(){
//
//		Workpackage rootAp = WorkpackageService.getRootAP();
//		assertNotNull(rootAp);
//		char[] temp = rootAp.getStringID().toCharArray();
//		for (int i = 0; i < temp.length; i++) {
//			if(i == 0){
//				assertTrue(temp[i] != '1');
//			}else{
//				if(i % 2 == 0){
//					assertTrue(temp[i] == '0');
//				}else{
//					assertTrue(temp[i] == '.');
//				}
//			}
//		}
//	}
	
	@Test
	public void testGetWorkpackage(){
		Workpackage ergAP = WorkpackageService.getWorkpackage(apID0);
		assertNotNull(ergAP);
	}
	
	@Test
	public void testGetWorkpackages(){
		Set<String> ids = new HashSet<String>();
		ids.add(apID0);
		ids.add(apID1);
		
		Set<Workpackage> ergSet= WorkpackageService.getWorkpackages(ids);
		assertNotNull(ergSet);
		assertTrue(ergSet.size() == 2);
	}
	
	@Test
	public void testInsertWorkpackage(){
		assertTrue(WorkpackageService.insertWorkpackage(insertWp));
		Workpackage ergWp = WorkpackageService.getWorkpackage(insertID);
		teardownTestWp(ONLY_WP);
		assertNotNull(ergWp);
		assertTrue(ergWp.getStringID().equals(insertID));
	}
	
	@Test
	public void testUpdateWorkpackage(){
		initTestWp(ONLY_WP);
		String s = "Herr hilf las Abend werden";
		insertWp.setBeschreibung(s);
		assertTrue(WorkpackageService.updateWorkpackage(insertWp));
		Workpackage ergWp = WorkpackageService.getWorkpackage(insertID);
		teardownTestWp(ONLY_WP);
		assertNotNull(ergWp);
		assertTrue(ergWp.getBeschreibung().equals(s));
	}
	
	@Test
	public void testAddWpWorker(){
		initTestWp(ONLY_WP);
		assertTrue(WorkpackageService.addWpWorker(insertWp, workerID));
		Workpackage temp = WorkpackageService.getWorkpackage(insertID);
		teardownTestWp(DELETE_WORKER);
		assertNotNull(temp);
		String erg = temp.getStringZustaendige();
		System.out.println("=======> Mitarbeiter String: " + erg);
		assertNotNull(erg);
		assertFalse(erg.equals(""));
		assertTrue(erg.contains(workerID));
	}
	
	@Test
	public void testRemoveWpWorker(){
		initTestWp(SETWORKER);
		assertTrue(WorkpackageService.removeWpWorker(insertWp, workerID));
		Workpackage temp = WorkpackageService.getWorkpackage(insertID);
		teardownTestWp(ONLY_WP);
		assertNotNull(temp);
		String erg = temp.getStringZustaendige();
		System.out.println("=======> Mitarbeiter String: " + erg);
		assertNotNull(erg);
		assertFalse(erg.contains(workerID));
	}
	
	@Test
	public void testDeleteWorkpackage(){
		initTestWp(ONLY_WP);
		assertTrue(WorkpackageService.deleteWorkpackage(insertWp));
		Workpackage ergWp = WorkpackageService.getWorkpackage(insertID);
		assertNull(ergWp);
	}
	
	@Test
	public void testSetFollower(){
		WorkpackageService.setFollower(setAcestorID, setFollowerId);
		@SuppressWarnings("deprecation")
		Set<Workpackage> ergSet = WorkpackageService.getFollowers(setAcestorID);
		teardownTestWp(DELETE_FOLLOWER);
		assertNotNull(ergSet);
		boolean contains = false;
		for (Workpackage workpackage : ergSet) {
			if(workpackage.getStringID().equals(setFollowerId)){
				contains = true;
			}
		}
		assertTrue(contains);
	}
	
	@Test
	public void testSetAncestor(){
		WorkpackageService.setAncestor(setFollowerId2, setAcestorID2);
		@SuppressWarnings("deprecation")
		Set<Workpackage> ergSet = WorkpackageService.getAncestors(setFollowerId2);
		teardownTestWp(DELETE_ANCESTOR);
		assertNotNull(ergSet);
		boolean contains = false;
		for (Workpackage workpackage : ergSet) {
			if(workpackage.getStringID().equals(setAcestorID2)){
				contains = true;
			}
		}
		assertTrue(contains);
	}
	
//	@Test
//	public void testGetFollowers(){
//		System.out.println("follower");
//		Set<Workpackage> follower = WorkpackageService.getFollowers(ancestorID);
//		assertNotNull(follower);
//		boolean contains = false;
//		for (Workpackage workpackage : follower) {
//			System.out.println(workpackage.getStringID());
//			if(workpackage.getStringID().equals(followerID)){
//				contains = true;
//			}
//		}
//		assertTrue(contains);
//	}
//	
//	@Test
//	public void testGetAncestors(){
//		System.out.println("ancestor");
//		Set<Workpackage> ancestors;
//		ancestors = WorkpackageService.getAncestors(followerID);
//		assertNotNull(ancestors);
//		boolean contains = false;
//		for (Workpackage workpackage : ancestors) {
//			System.out.println(workpackage.getStringID());
//			if(workpackage.getStringID().equals(ancestorID)){
//				contains = true;
//			}
//		}
//		assertTrue(contains);
//	}
	
	@Test
	public void testGetAncestorToFollowersIdMap(){
		Map<String, Set<String>> ergmaMap = WorkpackageService.getAncestorToFollowersIdMap();
		assertNotNull(ergmaMap);
		assertTrue(ergmaMap.keySet().size()	> 0);
		for (String string : ergmaMap.keySet()) {
			for (String s : ergmaMap.get(string)) {
				System.out.println("Ancestor: " + string + " Follower: " + s);
			}
		}
	}
	
	@Test
	public void testGetFollowerToAncestorsIdMap(){
		Map<String, Set<String>> ergmaMap = WorkpackageService.getFollowerToAncestorsIdMap();
		assertNotNull(ergmaMap);
		assertTrue(ergmaMap.keySet().size()	> 0);
		for (String string : ergmaMap.keySet()) {
			for (String s : ergmaMap.get(string)) {
				System.out.println("Follower: " + string + " Ancestor: " + s);
			}
		}
	}
	
	@Test
	public void testDeleteFollower(){
		initTestWp(SET_FOLLOWER);
		assertTrue(WorkpackageService.deleteFollower(setAcestorID, setFollowerId));
		@SuppressWarnings("deprecation")
		Set<Workpackage> ergSet = WorkpackageService.getFollowers(setAcestorID);
		System.out.println(ergSet.size());
		boolean contains = false;
		for (Workpackage workpackage : ergSet) {
			if(workpackage.getStringID().equals(setFollowerId)){
				contains = true;
			}
		}
		assertFalse(contains);
	}
	
	@Test
	public void testDeleteAncestor(){
		initTestWp(SET_ANCESTOR);
		assertTrue(WorkpackageService.deleteAncestor(setFollowerId2, setAcestorID2));
		@SuppressWarnings("deprecation")
		Set<Workpackage> ergSet = WorkpackageService.getAncestors(setFollowerId2);
		assertNotNull(ergSet);
		boolean contains = false;
		for (Workpackage workpackage : ergSet) {
			if(workpackage.getStringID().equals(setAcestorID2)){
				contains = true;
			}
		}
		assertFalse(contains);
	}
	
	@Test
		public void testGetAllAp(){
			Set<Workpackage> ergSet = WorkpackageService.getAllAp();
			assertNotNull(ergSet);
			System.out.println(ergSet.size());
	//		for (Workpackage workpackage : ergSet) {
	//			System.out.println(workpackage);
	//		}
		}

	@Test
	public void testGetAllApFromTo(){
		Set<Workpackage> ergSet = WorkpackageService.getAllAp(from, to);
		assertNotNull(ergSet);
		System.out.println(ergSet.size());
		for (Workpackage workpackage : ergSet) {
			System.out.println(workpackage);
		}
	}

	@Test
	public void testGetAllWorkerAp(){
		Set<Workpackage> ergSet = WorkpackageService.getAllWorkerAp(userId);
		assertNotNull(ergSet);
	}

	@Test
	public void testGetAllWorkerApFromTo(){
		Set<Workpackage> ergSet = WorkpackageService.getAllWorkerAp(userId, from, to);
		assertNotNull(ergSet);
	}

	private void initTestWp(int value){
		switch (value) {
		case ONLY_WP:
			WorkpackageService.insertWorkpackage(insertWp);
			break;
		case SETWORKER:
			WorkpackageService.insertWorkpackage(insertWp);
			WorkpackageService.addWpWorker(insertWp, workerID);
			break;
		case SET_FOLLOWER:
			WorkpackageService.setFollower(setAcestorID, setFollowerId);
			break;
		case SET_ANCESTOR:
			WorkpackageService.setAncestor(setFollowerId2, setAcestorID2);
			break;
		default:
			break;
		}
	}
	
	private void teardownTestWp(int value){
		switch (value) {
		case ONLY_WP:
			WorkpackageService.deleteWorkpackage(insertWp);
			break;
		case DELETE_WORKER:
			WorkpackageService.deleteWorkpackage(insertWp);
			WorkpackageService.removeWpWorker(insertWp, workerID);
			break;
		case DELETE_FOLLOWER:
			WorkpackageService.deleteFollower(setAcestorID, setFollowerId);
			break;
		case DELETE_ANCESTOR:
			WorkpackageService.deleteAncestor(setFollowerId2, setAcestorID2);
			break;
		default:
			break;
		}
	}
	
	private final int ONLY_WP = 0;
	private final int DELETE_WORKER = 1; 
	private final int SETWORKER = 3;
	private final int DELETE_FOLLOWER = 4;
	private final int DELETE_ANCESTOR = 5;
	private final int SET_FOLLOWER = 6;
	private final int SET_ANCESTOR = 7;
}
