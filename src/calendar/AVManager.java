package calendar;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dbServices.CalendarService;
import dbServices.WorkerService;


import wpOverview.tabs.AvailabilityGraph;
import wpWorker.Worker;


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
 * Verfuegbarkeiten fuer Dauerberechnung verwalten<br/>
 * 
 * @author Michael Anstatt, Lin Yang
 * @version 2.0 - 2012-08-21
 */
public class AVManager {

	/**
	 * Zuordnung Tag -> (Mitarbeiter -> Availabilities)
	 */
	private static Map<Day, Map<String, List<Availability>>> completeAvailabilities;

	/**
	 * Zuordnung Tag -> (Mitarbeiter -> Verbrauchte Stunden)
	 */
	private static Map<Day, Map<String, Integer>> consumedWork;

	/**
	 * Zuordnung Tag -> (Mitarbeiter -> Noch nicht verbrauchte Stunden)
	 */
	private static Map<Day, Map<String, Integer>> availableWork;

	/**
	 * Alle Mitarbeiter
	 */
	private ArrayList<Worker> allProjectWorkers;

	/**
	 * enthaelt alle ausgelesenen Tage
	 */
	private Set<Day> readDays;

	/**
	 * Konstruktor
	 */
	public AVManager() {
		this.allProjectWorkers = WorkerService.getRealWorkers();
		availableWork = new HashMap<Day, Map<String, Integer>>();
		consumedWork = new HashMap<Day, Map<String, Integer>>();
		completeAvailabilities = new HashMap<Day, Map<String, List<Availability>>>();
		readDays = new HashSet<Day>();
	}

	/**
	 * Liest naechste Projektarbeitszeit aus
	 * 
	 * @param oldDate Datum mit Uhrzeit, zu dem die naechste Projektarbeitszeit gesucht wird
	 * @return naechste Projektarbeitszeit
	 */
	public Date getNextWorkDate(Date oldDate) {
		if(oldDate != null) {
			List<Availability> dayProjAvs = new ArrayList<Availability>();
		Day actualDay = new Day(oldDate);
		Date returnDate = null;
		boolean readNext = false;
		while(returnDate == null) {
			while(readNext || dayProjAvs.isEmpty()) {
				if (!readDays.contains(actualDay)) {
					fillData(actualDay);
				}
				dayProjAvs = completeAvailabilities.get(actualDay).get(AvailabilityGraph.PROJECT_WORKER.getLogin());
				GregorianCalendar cal = new GregorianCalendar();
				cal.setTime(actualDay);
				cal.add(Calendar.DATE, 1);
				actualDay = new Day(cal.getTime());
				readNext = false;
			}
			Collections.sort(dayProjAvs);
			for(Availability actualAv : dayProjAvs) {
				if(actualAv.getStartTime().before(oldDate) && actualAv.getEndTime().after(oldDate)) {
					returnDate = oldDate; // oldDate liegt innerhalb einer Availability
				} else if (actualAv.getStartTime().after(oldDate) || actualAv.getStartTime().equals(oldDate)) {
					returnDate = actualAv.getStartTime(); // es wurde die naechste availability gefunden
				}
				if(returnDate != null) {
					return returnDate;
				}
			}
			readNext = true;
		}
		return returnDate;
		} else {
			return null;
		}
		
	}

	/**
	 * Liest die naechste Projektarbeitszeit fuer einen bestimmten Tag aus wenn an diesem tag bereits workedToday Stunden gearbeitet wurden.
	 * 
	 * @param oldDate gewuenschter Tag ohne Uhrzeit
	 * @param workedToday Stunden
	 * @return
	 */
	public Date getNextWorkTime(Day actualDay, int workedToday) {
		List<Availability> dayProjAvs = new ArrayList<Availability>();
		Date returnDate = null;
		while (returnDate == null) {
			while (dayProjAvs.isEmpty()) {
				if (!readDays.contains(actualDay)) {
					fillData(actualDay);
				}
				dayProjAvs = completeAvailabilities.get(actualDay).get(AvailabilityGraph.PROJECT_WORKER.getLogin());
				GregorianCalendar cal = new GregorianCalendar();
				cal.setTime(actualDay);
				cal.add(Calendar.DATE, 1);
				actualDay = new Day(cal.getTime());
			}
			for (Availability actualAv : dayProjAvs) {
				if (actualAv.getDuration() < workedToday) {
					workedToday -= actualAv.getDuration();
				} else {
					GregorianCalendar cal = new GregorianCalendar();
					cal.setTime(actualAv.getStartTime());
					cal.add(Calendar.HOUR_OF_DAY, workedToday);
					returnDate = cal.getTime();
				}
				if (returnDate != null) { return returnDate; }
			}
		}

		return returnDate;
	}

	/**
	 * Gibt fuer alle Mitarbeiter die Anzahl der Stunden an, die sie an diesem Tag leisten koennen ohne Beruecksichtigung bereits verbruachter Stunden
	 * 
	 * @param actualDay Tag, zu dem die verfuegbaren Stunden benoetigt werden
	 * @return Map mit Zuordnung MitarbeiterID -> ggesamt Verfuegbare Stunden an diesem Tag
	 */
	public Map<String, Integer> getCompleteDayWorks(Day actualDay) {

		if (!readDays.contains(actualDay)) {
			fillData(actualDay);
		}

		return availableWork.get(actualDay);
	}
	/**
	 * Liest Verfuegbarkeiten der Mitarbeiter aus der DB
	 * @param actualDay gewuenschter Tag
	 */
	private void fillData(Day actualDay) {
		List<Availability> avs;
		Map<String, Integer> availableDayWorks = new HashMap<String, Integer>();
		Map<String, Integer> consumedDayWorks = new HashMap<String, Integer>();
		completeAvailabilities.put(actualDay, new HashMap<String, List<Availability>>());
		for (Worker actualWorker : allProjectWorkers) {
			avs = new ArrayList<Availability>();
			avs.addAll(CalendarService.getRealWorkerAvailability(actualWorker.getLogin(), actualDay, new Day(actualDay, true)));
			completeAvailabilities.get(actualDay).put(actualWorker.getLogin(), avs);
			availableDayWorks.put(actualWorker.getLogin(), getCompleteWorkerDayWork(actualWorker.getLogin(), actualDay));
			consumedDayWorks.put(actualWorker.getLogin(), 0);
		}
		avs = new ArrayList<Availability>();
		avs.addAll(CalendarService.getRealProjectAvailability(actualDay, new Day(actualDay, true)));
		completeAvailabilities.get(actualDay).put(AvailabilityGraph.PROJECT_WORKER.getLogin(), avs);
		availableDayWorks.put(AvailabilityGraph.PROJECT_WORKER.getLogin(), getCompleteWorkerDayWork(AvailabilityGraph.PROJECT_WORKER.getLogin(), actualDay));
		consumedDayWorks.put(AvailabilityGraph.PROJECT_WORKER.getLogin(), 0);

		availableWork.put(actualDay, availableDayWorks);
		consumedWork.put(actualDay, consumedDayWorks);
		readDays.add(actualDay);
	}

	/**
	 * Gibt eine Map mit noch nicht verbrauchten Stunden fuer einen bestimmten Arbeitstag zurueck
	 * 
	 * @param actualDay gewuenschter Tag
	 * @param workers Liste von Mitarbeitern, die beruecksichtigt werden sollen, also diejenigen die einem AP zugeordnet sind
	 * @return Map mit Zuordnung Mitarbeiter -> noch Verfuegbare Arbveitszeit fuer diesen Tag
	 */
	public Map<String, Integer> getRemainingDayWorks(Day actualDay, List<String> workers) {
		if (!readDays.contains(actualDay)) {
			fillData(actualDay);
		}
		Map<String, Integer> remainingDayWorks = new HashMap<String, Integer>();
		int remaining;
		for (String actualWorker : workers) {
			remaining = getCompleteDayWorks(actualDay).get(actualWorker) - getConsumedDayWork(actualDay).get(actualWorker);
			remainingDayWorks.put(actualWorker, remaining);
		}
		return remainingDayWorks;
	}

	/**
	 * Gibt die gesamte Arbeitszeit fuer einen Tag zurueck die noch nicht verbraucht ist
	 * 
	 * @param actualDay gewuenschter Tag
	 * @param workers Liste von Mitarbeitern, die beruecksichtigt werden, also diejenigen die einem AP zugeordnet sind
	 * @return Zeit in Stunden
	 */
	public int getTogetherRemainingDayWork(Day actualDay, List<String> workers) {
		if (!readDays.contains(actualDay)) {
			fillData(actualDay);
		}
		int remaining = 0;
		for (String actualWorker : workers) {
			remaining += getCompleteDayWorks(actualDay).get(actualWorker) - getConsumedDayWork(actualDay).get(actualWorker);
		}
		return remaining;
	}

	/**
	 * Gibt fuer die uebergebenen Mitarbeiter die Anzahl der Stunden an, die sie an diesem Tag bereits geleistet haben
	 * 
	 * @param actualWorkerID
	 * @param actualDay
	 * @return Map mit Zuorrdnung Mitarbeiter -> bereits gearbeitete Stunden
	 */
	public Map<String, Integer> getConsumedDayWork(Day actualDay) {
		if (!readDays.contains(actualDay)) {
			fillData(actualDay);
		}
		return consumedWork.get(actualDay);
	}

	/**
	 * Fuegt fuer einen bestimmten Mitarbeiter geleistete Arbeitszeit hinzu
	 * 
	 * @param workerID Mitarbeiter
	 * @param actualDay gewuenschter Tag
	 * @param hours Anzahl zu arbeitender Stunden
	 */
	public void addWork(String workerID, Day actualDay, int hours) {
		if (!readDays.contains(actualDay)) {
			fillData(actualDay);
		}
		Map<String, Integer> old = consumedWork.get(actualDay);
		old.put(workerID, old.get(workerID) + hours); 
	}

	/**
	 * Liest fuer einen Mitarbeiter die Stunden aus, die er an einem Tag leisten kann ohne Beruecksichtigung bereits verbrauchter Stunden
	 * 
	 * @param actualWorkerID
	 * @param actualDay
	 * @return Zeit in Stunden
	 */
	private int getCompleteWorkerDayWork(String actualWorkerID, Day actualDay) {
		int availableDayWork = 0;
		if (!completeAvailabilities.containsKey(actualDay)) {
			fillData(actualDay);
		}
		if (completeAvailabilities.get(actualDay).get(actualWorkerID) != null) {
			for (Availability actualAv : completeAvailabilities.get(actualDay).get(actualWorkerID)) {
				availableDayWork += actualAv.getDuration();
			}
		}
		return availableDayWork;
	}
}
