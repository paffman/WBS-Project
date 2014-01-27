package functions;

import globals.Loader;
import globals.Workpackage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;

import jdbcConnection.SQLExecuter;

import dbServices.WorkpackageService;



import wpComparators.APLevelComparator;
import wpConflict.Conflict;
import wpOverview.WPOverview;
import wpWorker.Worker;
import wpWorker.User;

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
 * Schnittstelle zur Verwaltung wie loeschen/anlegen/aktualisieren von Arbeitspaketen und Vorgaengern/Nachfolgern<br/>
 * synchron auf Datenbank und Java-Ebene<br/>
 * 
 * @author Michael Anstatt, Marc-Eric Baumgärtner, Jens Eckes
 * @version 2.0 - 2012-08-20
 */
public class WpManager {

	private static APList list;
	private static Workpackage rootWp;

	/**
	 * Gibt das Objekt zum Arbeitspaket mit der uebergebenen ID zurueck
	 * 
	 * @param id ID der Form x.x.x...
	 * @return Workpackage-Objekt mit Werten aus der Datenbank
	 */
	public static Workpackage getWorkpackage(String id) {
		return list.getWorkpackage(id);
	}

	/**
	 * Liest Arbeitspakete und Bezeihungsstruktur aus Datenbank aus. Laengere Laufzeit, sollte einmalig bei Login aufgerufen werden
	 */
	public static void loadDB() {
		Loader.reset();
		Loader.setLoadingText("lade Arbeitspakete...");
		list = new APList();
		Loader.setLoadingText("lade Beziehungen...");
		list.setAncestorsAndFollowers();
	}
	

	/**
	 * Versucht einen Vorgaenger einzufuegen. Wenn Schleifen auftreten wird der Vorgaenger nicht gesetzt. Treten keine Schleifen auf wird die Beziehung gesetzt
	 * und in die Datenbank eingetragen. Wenn eine Schleife auftritt wird ein Dialogfenster angezeigt
	 * 
	 * @param anchestor zu setzender Vorgaenger
	 * @param main Arbeitspaket zu dem der Vorgaenger gesetzt werden soll
	 * @return false wenn Vorgaenger aufgrund von Schleifen nicht gesetzt wurde
	 */
	public static boolean insertAncestor(Workpackage anchestor, Workpackage main) {
		if (list.setAncestor(anchestor, main)) {
			// System.out.println("System: Setze " + anchestor.getStringID() + " als Vorgänger von " + main);
			WorkpackageService.setAncestor(main.getStringID(), anchestor.getStringID());
			WPOverview.throwConflict(new Conflict(new Date(System.currentTimeMillis()), Conflict.CHANGED_DEPENDECIES, WPOverview.getUser().getId(), main));
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Versucht einen Nachfolger einzufuegen. Wenn Schleifen auftreten wird der Nachfolger nicht gesetzt. Treten keine Schleifen auf wird die Beziehung gesetzt
	 * und in die Datenbank eingetragen. Wenn eine Schleife auftritt wird ein Dialogfenster angezeigt
	 * 
	 * @param follower zu setzender Nachfolger
	 * @param main Arbeitspaket zu dem der Nachfolger gesetzt werden soll
	 * @return false wenn Nachfolger aufgrund von Schleifen nicht gesetzt wurde
	 */
	public static boolean insertFollower(Workpackage follower, Workpackage main) {
		if (list.setFollower(follower, main)) {
			// System.out.println("System: Setze " + follower.getStringID() + " als Nachfolger von " + main);
			WorkpackageService.setFollower(main.getStringID(), follower.getStringID());
			WPOverview.throwConflict(new Conflict(new Date(System.currentTimeMillis()), Conflict.CHANGED_DEPENDECIES, WPOverview.getUser().getId(), main));
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Loescht einen Vorgaenger
	 * 
	 * @param ancestor zu loeschender Vorgaenger
	 * @param main Arbeitspaket dessen Vorgaenger geloescht werden soll
	 * @return false, wenn es Probleme beim loeschen in der Datenbank gab und die Beziehung nicht geloescht werden konnte
	 */
	public static boolean removeAncestor(Workpackage ancestor, Workpackage main) {
		list.removeAncestor(ancestor, main);
		if (WorkpackageService.deleteAncestor(main.getStringID(), ancestor.getStringID())) {
			WPOverview.throwConflict(new Conflict(new Date(System.currentTimeMillis()), Conflict.CHANGED_DEPENDECIES, WPOverview.getUser().getId(), main));
			return true;
		} else {
			System.out.println("Fehler beim loeschen aus der Datenbank");
			list.setAncestor(ancestor, main);
			return false;
		}
	}

	/**
	 * Loescht einen Nachfolger
	 * 
	 * @param follower zu loeschender Nachfolger
	 * @param main Arbeitspaket dessen Nachfolger geloescht werden soll
	 * @return false, wenn es Probleme beim loeschen in der Datenbank gab und die Beziehung nicht geloescht werden konnte
	 */
	public static boolean removeFollower(Workpackage follower, Workpackage main) {
		list.removeFollower(follower, main);
		if (WorkpackageService.deleteFollower(main.getStringID(), follower.getStringID())) {
			WPOverview.throwConflict(new Conflict(new Date(System.currentTimeMillis()), Conflict.CHANGED_DEPENDECIES, WPOverview.getUser().getId(), main));
			return true;
		} else {
			System.out.println("Fehler beim loeschen aus der Datenbank");
			list.setFollower(follower, main);
			return false;
		}
	}

	/**
	 * Versucht ein Arbeitspaket komplett zu loeschen. Wenn dies moeglich ist wird es aus der Datenbank entfernt. Dies ist nur moeglich wenn keine
	 * Unterarbeitspakete vorhanden sind und keine Beziehungen mehr bestehen. Es wird ein Dialogfenster mit
	 * Fehlerbeschreibung angezeigt wenn nicht geloescht werden kann
	 * 
	 * @param removeWp zu loeschendes Arbeitspaket
	 * @return false wenn loeschen nicht moeglich
	 */
	public static boolean removeAP(Workpackage removeWp) {
		if (list.getAncestors(removeWp)!= null && removeWp!=null && !removeWp.equals(getRootAp())) {
			if (list.getAncestors(removeWp).isEmpty() && list.getFollowers(removeWp).isEmpty()) {
				if (WpManager.getUAPs(removeWp).isEmpty()) {
					if ((int) (double) removeWp.getAc() == 0) {
						if (WorkpackageService.deleteWorkpackage(removeWp)) {
							for (String actualWorker : removeWp.getWorkers()) {
								removeWp.removeWorker(actualWorker);
							}
							WPOverview.throwConflict(new Conflict(new Date(System.currentTimeMillis()), Conflict.DELETED_WP, WPOverview.getUser().getId(), WpManager.getRootAp()));
							list.removeWp(removeWp);
						} else {
							JOptionPane.showMessageDialog(null, "Paket kann nicht aus der Datenbank gelöscht werden!");
							return false;
						}
					} else {
						JOptionPane.showMessageDialog(null, "Es sind bereits Aufwände eingegeben worden, AP kann nicht gelöscht werden");
						return false;
					}
				} else {
					JOptionPane.showMessageDialog(null, "Es sind noch Unterarbeitspakete vorhanden, diese müssen zuerst gelöscht werden");
					return false;
				}
			} else {
				JOptionPane.showMessageDialog(null, "Bitte erst alle Vorgänger / Nachfolger-Beziehungen löschen!");
				return false;
			}
		} else {
			if(removeWp == null) {
				JOptionPane.showMessageDialog(null, "Bitte AP auswählen");
			} else {
				JOptionPane.showMessageDialog(null, "Hauptpaket darf nicht gelöscht werden");
			}
			
			return false;
		}

		return true;
	}

	/**
	 * Fuegt ein Arbeitspaket in die Datenbank ein und legt Datenstruktur zum spaeteren EIntragen von Beziehungen an.
	 * 
	 * @param newWp das einzufuegende Arbeitspaket
	 * @return false wenn es Probleme mit dem EInfuegen in die DB gab
	 */
	public static boolean insertAP(Workpackage newWp) {
		if (WorkpackageService.insertWorkpackage(newWp)) {
			list.insertWp(newWp);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Gibt alle Vorgaenger zurueck
	 * 
	 * @param workpackage Arbeitspaket zu dem die Vorgaenger gewuenscht werden
	 * @return Set mit Vorgaengern
	 */
	public static Set<Workpackage> getAncestors(Workpackage workpackage) {
		return list.getAncestors(workpackage);
	}

	/**
	 * Gibt alle Nachfolger zurueck
	 * 
	 * @param workpackage Arbeitspaket zu dem die Nachfolger gewuenscht werden
	 * @return Set mit Nachfolgern
	 */
	public static Set<Workpackage> getFollowers(Workpackage workpackage) {
		return list.getFollowers(workpackage);
	}

	/**
	 * Aktualisiert Werte eines vorhandenen Arbeitspakets
	 * 
	 * @param wp zu aktualisierendes Arbeitspaket
	 */
	public static void updateAP(Workpackage wp) {
		list.updateWp(wp);
		WorkpackageService.updateWorkpackage(wp);
	}

	/**
	 * Gibt alle Arbeitspakete als Set zurueck
	 * @return Set mit allen Arbeitspaketen
	 */
	public static Set<Workpackage> getAllAp() {
		return list.getAllAp();
	}

	public static Workpackage getRootAp() {
		if (rootWp == null) {
			for (Workpackage actualWp : list.getAllAp()) {
				if (actualWp.getLvlID(1) == 0) {
					rootWp = actualWp;
				}
			}
		}

		return rootWp;
	}

	/**
	 * Setzt alle Unterarbeitspakete zu einem bestehenden OAP inaktiv, falls das OAP als inaktiv markiert wird Wird von WPShow.istOAPsetChanges() aufgerufen
	 */
	public static void setUapsInaktiv(String id, boolean isInak) {
		Set<Workpackage> actualUAPs = WpManager.getUAPs(WpManager.getWorkpackage(id));

		for (Workpackage actualUAP : actualUAPs) {
			actualUAP.setIstInaktiv(isInak);
			WpManager.updateAP(actualUAP);
			setUapsInaktiv(actualUAP.getStringID(), isInak);
		}

	}

	/**
	 * Gibt die Arbeitspakete zurueck, denen ein bestimmter Mitarbeiter als Leiter oder Zustaendiger eingetragen ist
	 * @param user
	 * @return Arbeitspakete in denen ein bestimmter Mitarbeiter als Leiter oder Zustaendiger eingetragen ist
	 */
	public static List<Workpackage> getUserWp(Worker user) {
		List<Workpackage> userWp = new ArrayList<Workpackage>();
		if(!user.getProjLeiter()) {
			for (Workpackage actualWp : WpManager.getAllAp()) {
				if (actualWp.getWorkers().contains(user.getLogin())) {
					userWp.add(actualWp);
				}
			}
		} else {
			userWp = new ArrayList<Workpackage>(WpManager.getAllAp());
		}
		
		return userWp;
	}

	/**
	 * Gibt alle Arbeitspakete ohne Vorganger zurueck, wichtig fuer die Dauer-/PV-Berechnung
	 * @return alle AP ohne Vorgaenger
	 */
	public static Set<Workpackage> getNoAncestorWps() {
		return list.getNoAncestorWps();
	}
	
	/**
	 * Liefert alle UAP fuer ein gegebenes OAP
	 * @param oap Oberarbeitspaket
	 * @return alle Unterarbeitspakete
	 */
	public static Set<Workpackage> getUAPs(Workpackage oap) {
		List<Workpackage> wpList = new ArrayList<Workpackage>(WpManager.getAllAp());
		Collections.sort(wpList, new APLevelComparator());

		Set<Workpackage> returnSet = new HashSet<Workpackage>();
		boolean found = false;

		int i = 0;

		for (Workpackage actualWP : wpList) {
			if (found) {

				if (oap.getLvlID(oap.getlastRelevantIndex()) != actualWP.getLvlID(oap.getlastRelevantIndex())) {
					return returnSet;
				} else if (i < actualWP.getLvlID(oap.getlastRelevantIndex() + 1)) {
					i = actualWP.getLvlID(oap.getlastRelevantIndex() + 1);
					returnSet.add(actualWP);
				}

			} else {
				if (actualWP.equals(oap)) {
					found = true;
				}
			}

		}

		return returnSet;

	}

	/**
	 * Gibt alle offenen (also noch nicht abgeschlossenen) Arbeitspakete eines Benutzer und deren OAP (fuer Baumansicht) zurueck.
	 * @param user
	 * @return offene Arbeitspkaete mit OAPs fuer den angegebenen Mitarbeiter
	 */
	public static List<Workpackage> getUserWpOpen(User user) {
		List<Workpackage> allUserWp = WpManager.getUserWp(user);
		Set<Workpackage> returnWp = new HashSet<Workpackage>();
		for(Workpackage actualWp : allUserWp) {
			if(WpManager.calcPercentComplete(actualWp.getBac(), actualWp.getEtc(), actualWp.getAc()) < 100) {
				addWpToList(actualWp, returnWp);
			}
		}
		return new ArrayList<Workpackage>(returnWp);
	}

	/**
	 * Gibt alle abgeschlossenen Arbeitspakete eines Benutzer und deren OAP (fuer Baumansicht) zurueck.
	 * @param user
	 * @return abgeschlossene Arbeitspkaete mit OAPs fuer den angegebenen Mitarbeiter
	 */
	public static List<Workpackage> getUserWpFinished(User user) {
		List<Workpackage> allUserWp = WpManager.getUserWp(user);
		Set<Workpackage> returnWp = new HashSet<Workpackage>();
		for(Workpackage actualWp : allUserWp) {
			if(WpManager.calcPercentComplete(actualWp.getBac(), actualWp.getEtc(), actualWp.getAc()) == 100 && actualWp.getAc() > 0) {
				addWpToList(actualWp, returnWp);
			}
		}
		return new ArrayList<Workpackage>(returnWp);
	}

	/**
	 * Fuegt ein Arbeitspaket und alle OAPs rekursiv zu einer Liste hinzu
	 * @param wp Arbeitspaket
	 * @param wps Liste mit Arbeitspaketen, die um alle OAPs des Arbeitspakets des ersten Aufrufs ergaenzt wird
	 */
	private static void addWpToList(Workpackage wp, Set<Workpackage> wps) {
		wps.add(wp);
		if(wp!=null && !wp.equals(WpManager.getRootAp())) {
			addWpToList(WpManager.getWorkpackage(wp.getOAPID()), wps);
		}
	}

	/**
	 * Berechnet den Aufwand pro Arbeitspaket Wird aufgerufen durch: - addWp() - getAndShowValues() - setChanges()
	 * 
	 * @return Aufwand des Arbeitspakets als Double
	 * @throws SQLException Falls die Abfrage fehlschägt
	 */
	public static double calcAC(Workpackage wp) {
		Double summe = 0.;
		try {
			ResultSet rsAPAufwand = SQLExecuter.executeQuery("SELECT * FROM Aufwand " + "WHERE LVL1ID = " + wp.getLvl1ID() + "AND LVL2ID = " + wp.getLvl2ID()
					+ "AND LVL3ID = " + wp.getLvl3ID() + "AND LVLxID = '" + wp.getLvlxID() + "';");
			while (rsAPAufwand.next()) {
				summe += rsAPAufwand.getDouble("Aufwand");
			}
	
			rsAPAufwand.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
		return summe;
	}

	/**
	 * Errechnet den CPI aus gegebenen Werten
	 * 
	 * @param acCost
	 * @param etcCost
	 * @param bacCost
	 * @return
	 */
	public static double calcCPI(double acCost, double etcCost, double bacCost) {
		double cpi = 0.0;
		if (acCost + etcCost == 0.) {
			if (bacCost == 0.) {
				cpi = 1.0;
			}
		} else {
			cpi = bacCost / (acCost + etcCost);
		}
		if (cpi > 10.0) cpi = 10.0;
		return cpi;
	}

	/**
	 * Berechnet die AC Kosten fuer ein Arbeitspaket mithilfe der in der Datenbank zugewiesenen Mitarbeitern
	 * 
	 * @param wp Arbeitspaket, dessen AC-Kosten berechnet weerden sollen
	 * @return AC Kosten
	 * @throws SQLException Falls Abfrage fehlschägt
	 */
	public static double calcACKosten(Workpackage wp) throws SQLException {
		double ACKosten = 0.;
		ResultSet rsMitarbeiter = null;
		ResultSet rsAPAufwand = SQLExecuter.executeQuery("SELECT * FROM Aufwand WHERE LVL1ID = " + wp.getLvl1ID() + "AND LVL2ID = " + wp.getLvl2ID()
				+ "AND LVL3ID = " + wp.getLvl3ID() + "AND LVLxID = '" + wp.getLvlxID() + "';");
		while (rsAPAufwand.next()) {
			rsMitarbeiter = SQLExecuter.executeQuery("SELECT * FROM Mitarbeiter WHERE Login= '" + rsAPAufwand.getString("FID_Ma") + "';");
			while (rsMitarbeiter.next()) {
				ACKosten += rsAPAufwand.getDouble("Aufwand") * rsMitarbeiter.getDouble("Tagessatz");
			}
			rsMitarbeiter.getStatement().close();
		}
		rsAPAufwand.getStatement().close();
		return ACKosten;
	}

	/**
	 * Berechnet den Fertigstellungsgrad eines Arbeitspakets mithilfe der uebergebenen Werte
	 * 
	 * @param bac
	 * @param etc
	 * @param ac
	 * @return Fertigstellung in Prozent (0-100)
	 */
	public static int calcPercentComplete(double bac, double etc, double ac) {
		int percentComplete = 0;
		if (bac > 0 && etc == 0) {
			percentComplete = 100;
		} else {
			if (etc > 0 && ac > 0) {
				percentComplete = (int) (ac * 100 / (ac + etc));
			} else {
				percentComplete = 0;
			}
		}
		return percentComplete;
	}

	/**
	 * Errechnet den EV aus gegebenen Werten
	 * 
	 * @param bacCost BAC-Kosten in Euro
	 * @param percentComplete
	 * @return
	 */
	public static double calcEV(double bacCost, int percentComplete) {
		return bacCost * ((double) percentComplete / 100.);
	}

	/**
	 * Berechnet den EAC aus gegebenen Werten
	 * 
	 * @param bacCost BAC Kosten in Euro
	 * @param acCost AC Kosten in Euro
	 * @param etcCost ETC Kosten in Euro
	 * @return
	 */
	public static double calcEAC(double bacCost, double acCost, double etcCost) {
		double calcEAC = 0.0;
		if (bacCost > 0) {
			calcEAC = acCost + etcCost;
		}
		return calcEAC;
	}

	/**
	 * Errechnet den durschnittlichen Tagessatz der Mitarbeiter in der uebergebenen Liste
	 * 
	 * @param workers 
	 * @return durschnisttlicher Tagesssatz in EUR
	 */
	public static double calcTagessatz(List<String> workers) {
		Double wptagessatz = 0.0;
	
		for (int j = 0; j < workers.size(); j++) {
			String login = workers.get(j);
	
			try {
				ResultSet rsCurrentZustaendigWPs = SQLExecuter.executeQuery("SELECT * FROM Mitarbeiter " + "WHERE Login = '" + login + "';");
				while (rsCurrentZustaendigWPs.next()) {
					wptagessatz += rsCurrentZustaendigWPs.getDouble("Tagessatz");
				}
				rsCurrentZustaendigWPs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		wptagessatz /= workers.size();
		if (Double.isNaN(wptagessatz)) {
			wptagessatz = 0.0;
		}
	
		return wptagessatz;
	}

	/**
	 * Errechn et den Trend (Alternative zu CPI)
	 * 
	 * @param ev EV in EUR
	 * @param acCost AC-Kosten in EUR
	 * @return
	 */
	public static double calcTrend(double ev, double acCost) {
		if(acCost > 0) {
			return ev/acCost;
		} else {
			return 0;
		}
	}
}
