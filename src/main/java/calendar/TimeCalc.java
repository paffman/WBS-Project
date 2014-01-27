package calendar;

import functions.WpManager;
import globals.Controller;
import globals.Loader;
import globals.Workpackage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import wpComparators.APBacComparator;
import wpComparators.APEndDateComparator;
import wpComparators.APFollowerComparator;
import wpConflict.Conflict;
import wpOverview.WPOverview;
import wpOverview.tabs.AvailabilityGraph;
import dbServices.ConflictService;
import dbServices.ValuesService;

/**
 * Studienprojekt: PSYS WBS 2.0<br/>
 * 
 * Kunde: Pentasys AG, Jens von Gersdorff<br/>
 * Projektmitglieder:<br/>
 * Michael Anstatt,<br/>
 * Marc-Eric Baumgärtner,<br/>
 * Jens Eckes,<br/>
 * Sven Seckler,<br/>
 * Lin Yang<br/>
 * 
 * Diese Klasse errechnet aus allen Arbeitspaketen Ihre Dauer<br/>
 * 
 * @author Michael Anstatt, Jens Eckes
 * @version 2.0 - 2012-08-20
 */
public class TimeCalc {

	/**
	 * alle UAP ohne Vorgaenger
	 */
	private static List<Workpackage> uapWithoutAncestors;
	
	/**
	 * enthaelt Zuordnung Level -> OAPs ohne Vorgaenger (z.B. 2 - (1.2.0.0, 3.1.0.0))
	 */
	private static Map<Integer, List<Workpackage>> levelOAP;

	/**
	 * enthaelt Zuordnung Tag -> Mitarbeiter, verbrauchte Stunden
	 */
	private static Map<Day, Map<String, Integer>> consumedWork;
	
	private AVManager avManager;
//	private Map<String, Double> dailyRates;

	public TimeCalc() {
		Loader.setLoadingText("initialisieren...");
		ConflictService.deleteAll(); // Alle bisherigen Konflikte verwerfen, sie werden waehrend der Berechnung neu angelegt, falls noch aktuell
		WPOverview.releaseAllConflicts(); // s.o. nur fuer GUI

		Set<Workpackage> allAp = WpManager.getAllAp();
		for (Workpackage actualWp : allAp) {
			// Das Setzen wird von Workpackage blockiert, falls es in der Vergangenheit liegt
			actualWp.setStartDateCalc(null);
			actualWp.setEndDateCalc(null);
		}

		this.avManager = new AVManager();

		uapWithoutAncestors = new LinkedList<Workpackage>();
		levelOAP = new HashMap<Integer, List<Workpackage>>();

		// Liste aller UAP ohne Vorgaenger anlegen, fuer jede Ebene Liste mit allen OAPs ohne Vorgaenger anlegen
		init(uapWithoutAncestors, levelOAP);


		// Hilfsdatenstruktur fuer die verbrauchten Arbeitsstunden fuer einen bestimmten Tag
		consumedWork = new HashMap<Day, Map<String, Integer>>();

		// Dauer jedes UAPs ohne Vorgaenger berechnen und Startdatum fuer eventuell vorhandene Nachfolgerpakete setzen
		Loader.setLoadingText("berechne UAP ohne Vorgaenger");
		while (!uapWithoutAncestors.isEmpty()) {
			calcUAP(uapWithoutAncestors.remove(0));
		}

		Workpackage root = WpManager.getRootAp();

		// Level - OAP Zuordnung erstellen
		Loader.setLoadingText("prüfe OAP-Ebenen");
		for (Workpackage actualWp : allAp) {

			if (actualWp.isIstOAP() && !actualWp.isIstInaktiv()) {
				List<Workpackage> allLevelWps;
				int actualLevel = actualWp.getlastRelevantIndex();
				if (levelOAP.get(actualLevel) == null) {
					allLevelWps = new ArrayList<Workpackage>();
					levelOAP.put(actualLevel, allLevelWps);
				}
				allLevelWps = levelOAP.get(actualLevel);
				allLevelWps.add(actualWp);
			}
		}

		int maxLevels = root.getLvlIDs().length - 1; // Anzahl der Ebenen in diesem Projekt

		boolean finish = false;
		while (!finish) {
			for (int actualLevel = maxLevels; actualLevel >= 0; actualLevel--) {
				Loader.setLoadingText("prüfe OAP auf Ebene " + (actualLevel + 1));
				if (levelOAP.containsKey(actualLevel)) {
					List<Workpackage> actualLevelOAPs = levelOAP.get(actualLevel);
					int i = 0;
					if (!actualLevelOAPs.isEmpty()) {
						Workpackage actualOAP;
						while (i < actualLevelOAPs.size()) {
							actualOAP = actualLevelOAPs.get(i++);
							calcOAP(actualOAP); // OAP berechnen, falls moeglich
						}
						
					}
				}
			}
			finish = true;
			for (int actualLevel = maxLevels; actualLevel >= 0; actualLevel--) {

				if (levelOAP.containsKey(actualLevel) && !levelOAP.get(actualLevel).isEmpty()) {
					finish = false;

				}
			}
		}
		Loader.setLoadingText("speichere errechnete Werte");
		for (Workpackage actualWp : allAp) {
			actualWp.setTempStartHope(null);
			if (!actualWp.isIstInaktiv()) {
				if (actualWp.getEndDateHope() != null && actualWp.getEndDateCalc().after(actualWp.getEndDateHope())) {
					// Gewuneschtes Enddatum kann nicht eingehalten werden
					WPOverview.throwConflict(new Conflict(new Date(System.currentTimeMillis()), Conflict.ENDWISH_FAIL, WPOverview.getUser().getId(),
							actualWp));
				}
				Calendar cal = new GregorianCalendar();
				cal.setTime(actualWp.getEndDateCalc());
			}
			WpManager.updateAP(actualWp);
		}

	}

	/**
	 * Liefert das fruehstmoegliche Startdatum zu einem Arbeitspaket
	 * @param wp Arbeitspaket zu dem fruehestmoegliches Startdatum benoetigt wird
	 * @return das fruehstmoegliche Startdatum des Arbeitspakets unter beruecksichtigung von Vorgaengern / OAPs
	 */
	private Date getPossibleStartDate(Workpackage wp) {
		if (wp.equals(WpManager.getRootAp())) { return avManager.getNextWorkDate(WpManager.getRootAp().getStartDateHope()); }
		if (WpManager.getAncestors(wp).isEmpty()) {
			return getPossibleStartDate(WpManager.getWorkpackage(wp.getOAPID()));
		} else {
			boolean ok = true;
			Set<Workpackage> ancestors = WpManager.getAncestors(wp);
			TreeSet<Date> dates = new TreeSet<Date>();
			for (Workpackage actualWp : ancestors) {
				if (!actualWp.isIstInaktiv()) {
					if (actualWp.getEndDateCalc() == null) {
						ok = false; // Es gibt unberechnete Vorgaenger
					} else {
						dates.add(actualWp.getEndDateCalc());
					}
				}
			}
			if (ok) {
				if(wp.getStartDateHope() != null && wp.getStartDateHope().after(avManager.getNextWorkDate(dates.last()))) {
					return avManager.getNextWorkDate(wp.getStartDateHope());
				} else {
					return avManager.getNextWorkDate(dates.last());
				}
				
			} else {
				return null;
			}

		}
	}

	/**
	 * Fuellt die uapWithoutAncestors und levelOAP
	 * 
	 * @param levelOAP
	 * @param uapWithoutAncestors
	 */
	public void init(List<Workpackage> uapWithoutAncestors, Map<Integer, List<Workpackage>> levelOAP) {

		Set<Workpackage> allWithoutAncestors = WpManager.getNoAncestorWps();

		Workpackage root = WpManager.getRootAp();

		int maxLevels = root.getLvlIDs().length;

		for (int i = 1; i <= maxLevels; i++) {
			levelOAP.put(i, new ArrayList<Workpackage>()); // fuer jede Ebene Set anlegen, dass spaeter mit den OAPs dieser Ebene gefuellt wird
		}

		int actualLevel;

		for (Workpackage actualWp : allWithoutAncestors) {
			if (!actualWp.isIstInaktiv()) {
				actualWp.setStartDateCalc(avManager.getNextWorkDate(WpManager.getRootAp().getStartDateHope())); // da keine Vorgaenger vorhanden, soll
																												// ProjektstartHope gelten
				if (actualWp.isIstOAP()) {

					actualWp.setStartDateCalc(getPossibleStartDate(actualWp));
					Date startHope = avManager.getNextWorkDate(actualWp.getStartDateHope());
					if (actualWp.getStartDateCalc() != null && startHope != null && actualWp.getStartDateCalc().before(startHope)) {
						actualWp.setStartDateCalc(startHope);
						fillUAPHopes(actualWp, startHope);
					} else if (actualWp.getStartDateCalc() != null && actualWp.getStartDateHope() != null && actualWp.getStartDateCalc().before(startHope)) {
						WPOverview.throwConflict(new Conflict(new Date(System.currentTimeMillis()), Conflict.STARTWISH_FAIL, WPOverview.getUser().getId(),
								actualWp));
					}

					List<Workpackage> allLevelWps;
					actualLevel = actualWp.getlastRelevantIndex();
					if (levelOAP.get(actualLevel) == null) {
						allLevelWps = new ArrayList<Workpackage>();
						levelOAP.put(actualLevel, allLevelWps); // OAP in seine Ebenenliste aufnehmen
					}
					allLevelWps = levelOAP.get(actualLevel);
					allLevelWps.add(actualWp);

				} else {
					uapWithoutAncestors.add(actualWp); // UAP zur Liste der UAPs ohne Vorgaenger aufnehmen
				}
			}

		}
		// Sortieren, um sinnvolle Reihenfolge der Abarbeitung der UAP zu erreichen
		Collections.sort(uapWithoutAncestors, new APEndDateComparator());
		Collections.sort(uapWithoutAncestors, new APFollowerComparator());
		Collections.sort(uapWithoutAncestors, new APBacComparator());
	}

	/**
	 * Fuellt alle UAPs rekursiv mit dem temporaeren gewuenschten Startdatum, d.h. dieses Datum wird nicht gespeichert, sondern nur fuer die Dauerberechnung verwendet und dann verworfen
	 * 
	 * @param wp aktuelles Arbeitspaket
	 * @param startHope
	 */
	private void fillUAPHopes(Workpackage wp, Date startHope) {
		if (wp.getStartDateHope() != null) {
			if (wp.getStartDateHope().before(startHope)) {
				wp.setTempStartHope(startHope);
			}
		} else {
			wp.setTempStartHope(startHope);
		}
		for (Workpackage actualUAP : WpManager.getUAPs(wp)) {
			if (!actualUAP.isIstInaktiv()) {
				fillUAPHopes(actualUAP, startHope);
			}
		}
	}

	/**
	 * Berechnet rekursiv die Dauer des aktuellen OAPs und entfernt es bei Erfolg aus der Liste
	 * 
	 * @param actualOAP aktuelles OAP
	 * @param actualLevelOAPs Liste von OAPs einer bestimmten Ebene
	 */
	private void calcOAP(Workpackage actualOAP) {
		ValuesService.deletePV(actualOAP.getStringID());
		int oapLevel = actualOAP.getlastRelevantIndex();
		List<Workpackage> actualLevelOAPs = levelOAP.get(oapLevel);
		boolean done = true;

		Controller.showConsoleMessage("", Controller.TIME_CALCULATION_DEBUG);
		Controller.showConsoleMessage("OAP " + actualOAP.getStringID() + " wird versucht zu berechnen ====================================",
				Controller.TIME_CALCULATION_DEBUG);
		Controller.showConsoleMessage("Pruefe UAPs von " + actualOAP.getStringID(), Controller.TIME_CALCULATION_DEBUG);
		for (Workpackage actualUAP : WpManager.getUAPs(actualOAP)) { // Fuer alle UAP des OAP, SCHLEIFE b1
			if (!actualUAP.isIstInaktiv()) {
				actualUAP.setStartDateCalc(getPossibleStartDate(actualUAP));
				if (actualUAP.getStartDateCalc() == null) {
					
					if (actualUAP.getStartDateCalc() != null && actualUAP.getStartDateHope() != null
							&& actualUAP.getStartDateCalc().before(actualUAP.getStartDateHope())) {
						Date startHope = avManager.getNextWorkDate(actualUAP.getStartDateHope());
						actualUAP.setStartDateCalc(startHope);
					} else if (actualUAP.getStartDateCalc() != null && actualUAP.getStartDateHope() != null
							&& actualUAP.getStartDateCalc().before(avManager.getNextWorkDate(actualUAP.getStartDateHope()))) {
						WPOverview.throwConflict(new Conflict(new Date(System.currentTimeMillis()), Conflict.STARTWISH_FAIL, WPOverview.getUser().getId(),
								actualOAP, actualUAP));
					}
				}
				if (actualUAP.getStartDateCalc() != null && actualUAP.getEndDateCalc() == null) {
					if (!actualUAP.isIstOAP()) {
						Controller.showConsoleMessage(actualUAP.getStringID() + " wird berechnet", Controller.TIME_CALCULATION_DEBUG);
						calcUAP(actualUAP);
						Controller.showConsoleMessage(actualUAP.getStringID() + " wurde berechnet", Controller.TIME_CALCULATION_DEBUG);
					}
				}
				if (actualUAP.getEndDateCalc() == null || levelOAP.get(actualUAP.getlastRelevantIndex()).contains(actualUAP)) {
					done = false;
					Controller.showConsoleMessage(actualUAP.getStringID() + " kann noch nicht berechnet werden, weil noch UAPs oder Vorgaenger fehlen",
							Controller.TIME_CALCULATION_DEBUG);
				}
				if (actualOAP.getEndDateCalc() == null || (actualUAP.getEndDateCalc() != null && actualOAP.getEndDateCalc().before(actualUAP.getEndDateCalc()))) {

					if (actualUAP.getEndDateCalc() != null) {
						actualOAP.setEndDateCalc(actualUAP.getEndDateCalc());
						Controller.showConsoleMessage("Enddatum zum OAP " + actualOAP.getStringID() + " wurde von " + actualUAP.getStringID()
								+ " uebernommen (" + Controller.DATE_DAY_TIME.format(actualUAP.getEndDateCalc()) + ")", Controller.TIME_CALCULATION_DEBUG);
					}

				}
			}

		}
		if (WpManager.getUAPs(actualOAP).isEmpty()) {
			actualOAP.setEndDateCalc(actualOAP.getStartDateCalc());
			actualLevelOAPs.remove(actualOAP);
			Controller.showConsoleMessage("Enddatum zum OAP " + actualOAP.getStringID() + " existiert nicht, da keine UAPs vorhanden", true,
					Controller.TIME_CALCULATION_DEBUG);
		} else {			
			if (done && actualOAP.getEndDateCalc() != null) {
				actualLevelOAPs.remove(actualOAP);

				Controller.showConsoleMessage(
						"Die Dauer von " + actualOAP.getStringID() + " ist endgueltig berechnet, es endet am " + actualOAP.getEndDateCalc(),
						Controller.TIME_CALCULATION_DEBUG);
				Controller.showConsoleMessage("", Controller.TIME_CALCULATION_DEBUG);

				Date ancestorStartNew = avManager.getNextWorkDate(actualOAP.getEndDateCalc());

				// 6 SD Neu setzen
				Controller.showConsoleMessage("Setzte Enddatum von eventuell vorhandenen Nachfolgern von " + actualOAP.getStringID() + " auf "
						+ Controller.DATE_DAY_TIME.format(ancestorStartNew), Controller.TIME_CALCULATION_DEBUG);

				List<Workpackage> actualFollowers = new ArrayList<Workpackage>(actualOAP.getFollowers());
				Collections.sort(actualFollowers, new APEndDateComparator());
				Collections.sort(actualFollowers, new APFollowerComparator());
				Collections.sort(actualFollowers, new APBacComparator());
				for (Workpackage actualFollower : actualFollowers) {
					if (!actualFollower.isIstInaktiv()) {
						if (actualFollower.getStartDateCalc() == null || ancestorStartNew.after(actualFollower.getStartDateCalc())) {
							actualFollower.setStartDateCalc(ancestorStartNew);
						}

						if (actualFollower.getEndDateCalc() == null && actualFollower.canCalc()) {
							Controller.showConsoleMessage("Es wurde ein Nachfolger von " + actualOAP.getStringID()
									+ " gefunden, der jetzt berechnet werden kann", Controller.TIME_CALCULATION_DEBUG);
							if (actualFollower.isIstOAP()) {
								calcOAP(actualFollower);
							} else {
								calcUAP(actualFollower);
							}
						}
					}

				}
			}

		}

	}

	/**
	 * Berechnet rekursiv fuer das aktuelle AP und dessen Nachfolger soweit moeglich die Dauer. Erst aufgerufen wenn UAP startDatum hat
	 * 
	 * @param uap zu berechnendes Arbeitspaket
	 */
	private void calcUAP(Workpackage uap) {
		Controller.showConsoleMessage("", Controller.TIME_CALCULATION_DEBUG);
		Controller.showConsoleMessage("UAP " + uap.getStringID() + " wird berechnet ======================================================",
				Controller.TIME_CALCULATION_DEBUG);

		// 3 Enddatum von UAP berechnen
		Date startDate = uap.getStartDateCalc();

		if (uap.getStartDateHope() != null && startDate.before(uap.getStartDateHope())) {
			startDate = avManager.getNextWorkDate(uap.getStartDateHope());
			uap.setStartDateCalc(startDate);
		} else if (startDate != null && startDate.after(startDate)) {
			WPOverview.throwConflict(new Conflict(new Date(System.currentTimeMillis()), Conflict.STARTWISH_FAIL, WPOverview.getUser().getId(), uap));
		}

		int bac = (int) uap.getBacStunden().doubleValue();
		List<String> workers = uap.getWorkers();
		Calendar actualDayTime = new GregorianCalendar();
		actualDayTime.setTime(startDate);

		Day actualDay = new Day(actualDayTime.getTime());

		consumedWork.put(actualDay, new HashMap<String, Integer>());

		boolean stillWork = true;

		int allWorkedToday = 0;
		int possibleWorkToday = 0;

		Map<Day, Double> pvs = new HashMap<Day, Double>();

		while (stillWork) {
			
			if (actualDay.after(new Day(new Date(System.currentTimeMillis())))) {
				ValuesService.deletePV(uap.getStringID(), actualDay);
				Map<String, Integer> actualDayRemaining = avManager.getRemainingDayWorks(actualDay, workers);
				possibleWorkToday = avManager.getTogetherRemainingDayWork(actualDay, workers);
				allWorkedToday = 0;
				Controller.showConsoleMessage("  Aktueller Tag: " + Controller.DATE_DAY.format(actualDay) + ", heute koennen zusammen " + possibleWorkToday
						+ " Std. gerabeitet werden" + bac, Controller.TIME_CALCULATION_DEBUG);
				if (possibleWorkToday < bac) { // Es muss den ganzen Tag von allen gearbeitet werden um das AP abzuschliessen
					for (String actualWorker : workers) {
						int workerWorkedToday = actualDayRemaining.get(actualWorker);
						bac -= workerWorkedToday;
						avManager.addWork(actualWorker, actualDay, workerWorkedToday);
						if (!pvs.containsKey(actualDay)) {
							pvs.put(actualDay, workerWorkedToday * uap.getWpStundensatz());
						} else {
							double pvTillNow = pvs.get(actualDay);
							pvs.put(actualDay, pvTillNow + workerWorkedToday * uap.getWpStundensatz());
						}
						Controller.showConsoleMessage("     " + actualWorker + " muss heute " + workerWorkedToday + " Stunden arbeiten",
								Controller.TIME_CALCULATION_DEBUG);
					}
					allWorkedToday = possibleWorkToday;
				} else {
					int optimalWork = (int) bac / workers.size();
					while ((int) bac > 0) {
						for (String actualWorker : workers) {
							if ((int) bac > 0) {
								if (actualDayRemaining.get(actualWorker) >= optimalWork) {
									bac -= optimalWork;
									avManager.addWork(actualWorker, actualDay, optimalWork);
									if (!pvs.containsKey(actualDay)) {
										pvs.put(actualDay, optimalWork * uap.getWpStundensatz());
									} else {
										double pvTillNow = pvs.get(actualDay);
										pvs.put(actualDay, pvTillNow + optimalWork * uap.getWpStundensatz());
									}
									allWorkedToday += optimalWork;
									Controller.showConsoleMessage("     " + actualWorker + " muss heute " + optimalWork + " Stunden arbeiten",
											Controller.TIME_CALCULATION_DEBUG);
								} else {
									int workerRemaining = actualDayRemaining.get(actualWorker);
									if (workerRemaining < 0) {
										workerRemaining = 0;
									}
									bac -= workerRemaining;
									if (!pvs.containsKey(actualDay)) {
										pvs.put(actualDay, workerRemaining * uap.getWpStundensatz());
									} else {
										double pvTillNow = pvs.get(actualDay);
										pvs.put(actualDay, pvTillNow + workerRemaining * uap.getWpStundensatz());
									}
									avManager.addWork(actualWorker, actualDay, workerRemaining);
									allWorkedToday += workerRemaining;
									if (workerRemaining < 0) { throw new RuntimeException(bac + " " + workerRemaining + " " + allWorkedToday); }
									Controller.showConsoleMessage("     " + actualWorker + " muss heute zusaetzlich " + workerRemaining
											+ " Stunden arbeiten (weil ein anderer Mitarbeiter weniger Zeit hat)", Controller.TIME_CALCULATION_DEBUG);
								}
							}

						}
						optimalWork = 1;
					}
				}
				if ((int) bac == 0) {
					double percent = (double) allWorkedToday / (double) possibleWorkToday;
					double hoursConsumed = (percent * avManager.getCompleteDayWorks(actualDay).get(AvailabilityGraph.PROJECT_WORKER.getLogin()));
					avManager.addWork(AvailabilityGraph.PROJECT_WORKER.getLogin(), actualDay, (int) hoursConsumed);
					actualDayTime.setTime(avManager.getNextWorkTime(actualDay,
							avManager.getConsumedDayWork(actualDay).get(AvailabilityGraph.PROJECT_WORKER.getLogin())));
					stillWork = false;

					savePV(pvs, uap);

					Controller.showConsoleMessage(
							"Alle Stunden von " + uap.getStringID() + " werden am " + Controller.DATE_DAY.format(actualDay) + " abgearbeitet, es werden "
									+ (int) hoursConsumed + " von " + possibleWorkToday + " verfügbaren Stunden an diesem Tag verbraucht",
							Controller.TIME_CALCULATION_DEBUG);
				} else {
					actualDayTime.add(Calendar.DATE, 1);
					actualDay = new Day(actualDayTime.getTime());
				}

				consumedWork.put(actualDay, new HashMap<String, Integer>());
			} else {

				Calendar actualDayCal = new GregorianCalendar();
				actualDayCal.setTime(actualDay);

				Day nextFriday = new Day(ValuesService.getNextFriday(new Date(System.currentTimeMillis())).getTime());
				Calendar nextFridayCal = new GregorianCalendar();
				nextFridayCal.setTime(nextFriday);

				if (actualDay.equals(new Day(new Date(System.currentTimeMillis())))) {
					bac -= ValuesService.getApPv(uap.getStringID(), nextFridayCal) / uap.getWpStundensatz();
					actualDay = nextFriday;
					actualDayCal.setTime(actualDay);
					pvs.put(nextFriday, ValuesService.getApPv(uap.getStringID(), nextFridayCal));
					actualDayCal.add(Calendar.DATE, 1);
					actualDayTime = actualDayCal;
					actualDay = new Day(actualDayCal.getTime());
					Controller.showConsoleMessage("Aktueller Tag ist letzter Tag mit PV " + Controller.DATE_DAY.format(actualDay) + "PV " +ValuesService.getApPv(uap.getStringID(), nextFridayCal), Controller.TIME_CALCULATION_DEBUG);
				} else {
					Controller.showConsoleMessage(
							"Aktueller Tag: " + Controller.DATE_DAY.format(actualDay)
									+ ", liegt in der Vergangenheit, hier darf nichts mehr geändert werden, es werden "
									+ (ValuesService.getApPv(uap.getStringID(), actualDayCal) / uap.getWpStundensatz()) + " Stunden gearbeitet" + bac,
							Controller.TIME_CALCULATION_DEBUG);
					actualDayTime.add(Calendar.DATE, 1);
					actualDay = new Day(actualDayTime.getTime());

					if (uap.getEndDateCalc() != null) {
						stillWork = false;
					}
				}			
			}

			// Controller.showConsoleMessage("     Arbeit ist geleistet, es sind fuer die naechsten Tage noch " + bac + " Stunden uebrig",
			// Controller.TIME_CALCULATION_DEBUG);

		}

		uap.setEndDateCalc(actualDayTime.getTime());

		Controller.showConsoleMessage(
				"Das Arbeitspaket " + uap.getStringID() + " wurde fertig berechnet, Enddatum " + Controller.DATE_DAY_TIME.format(uap.getEndDateCalc()),
				Controller.TIME_CALCULATION_DEBUG);
		Controller.showConsoleMessage("", Controller.TIME_CALCULATION_DEBUG);


		// SD neu in Nachfolger
		Date ancestorStartNew = avManager.getNextWorkDate(uap.getEndDateCalc());

		Controller.showConsoleMessage(
				"Das Startdatum eventuell vorhandener Nachfolger von " + uap.getStringID() + " wird auf " + Controller.DATE_DAY_TIME.format(ancestorStartNew)
						+ " gesetzt", Controller.TIME_CALCULATION_DEBUG);

		List<Workpackage> actualFollowers = new ArrayList<Workpackage>(uap.getFollowers());
		Collections.sort(actualFollowers, new APEndDateComparator());
		Collections.sort(actualFollowers, new APFollowerComparator());
		Collections.sort(actualFollowers, new APBacComparator());
		for (Workpackage actualFollower : actualFollowers) {
			if (!actualFollower.isIstInaktiv()) {
				if (actualFollower.getStartDateCalc() == null || ancestorStartNew.after(actualFollower.getStartDateCalc())) {
					actualFollower.setStartDateCalc(ancestorStartNew);
				}
				if (actualFollower.canCalc() && !actualFollower.isIstOAP()) {
					Controller.showConsoleMessage("Nachfolger von " + uap.getStringID() + " gefunden, der jetzt berechnet werden kann",
							Controller.TIME_CALCULATION_DEBUG);
					calcUAP(actualFollower);
				}
			}

		}

	}

	/**
	 * Speichert alle PVs fuer ein bestimmtes Arbeitspaket in die Datenbank
	 * @param singlePVs Map mit Zuordnung Tag -> PV
	 * @param wp
	 */
	private void savePV(Map<Day, Double> singlePVs, Workpackage wp) {
		String wpID = wp.getStringID();
		List<Day> days = new ArrayList<Day>(singlePVs.keySet());
		Collections.sort(days);
		double actualPV = 0.0;
		Calendar cal = new GregorianCalendar();
		if (!days.isEmpty()) {
			cal.setTime(days.get(0));
			cal = ValuesService.getPreviousFriday(cal.getTime());
			ValuesService.savePv(wpID, cal.getTime(), 0.0);
		}

		for (Day actualDay : days) {
			if (actualDay.after(new Date(System.currentTimeMillis()))) {
				cal.setTime(actualDay);
				actualPV += singlePVs.get(actualDay);
				if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
					ValuesService.savePv(wpID, actualDay, actualPV);
					Controller.showConsoleMessage("PV von " + wpID + " ist am " + Controller.DATE_DAY.format(actualDay) + " " + actualPV,
							Controller.TIME_CALCULATION_DEBUG);
				}
			}

		}
		if (cal.get(Calendar.DAY_OF_WEEK) != Calendar.FRIDAY) {
			while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.FRIDAY) {
				cal.add(Calendar.DATE, 1);
			}
			if (cal.getTime().after(new Date(System.currentTimeMillis()))) {
				Controller.showConsoleMessage("PV von " + wpID + " ist am " + Controller.DATE_DAY.format(cal.getTime()) + " " + actualPV,
						Controller.TIME_CALCULATION_DEBUG);
				ValuesService.savePv(wpID, cal.getTime(), actualPV);
			}

		}

	}
}
