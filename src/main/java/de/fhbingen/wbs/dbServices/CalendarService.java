package de.fhbingen.wbs.dbServices;

import de.fhbingen.wbs.globals.Controller;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import de.fhbingen.wbs.calendar.Availability;
import de.fhbingen.wbs.calendar.Day;
import de.fhbingen.wbs.dbaccess.DBModelManager;
import de.fhbingen.wbs.dbaccess.data.EmployeeCalendar;
import de.fhbingen.wbs.dbaccess.data.HolidayCalendar;
import de.fhbingen.wbs.wpOverview.tabs.AvailabilityGraph;

/**
 * Studienprojekt: PSYS WBS 2.0<br/>
 * Kunde: Pentasys AG, Jens von Gersdorff<br/>
 * Projektmitglieder:<br/>
 * Michael Anstatt,<br/>
 * Marc-Eric Baumg�rtner,<br/>
 * Jens Eckes,<br/>
 * Sven Seckler,<br/>
 * Lin Yang<br/>
 * Diese Klasse dient als Verbindung zur Datenbank fuer das Speicher und
 * Loeschen von allem Mitarbeiter- und Projektkalenderbetrifft betrifft.<br/>
 * In dies Klasse wurden Methoden des Vorgaenger Projekts ausgelagert.<br/>
 *
 * @author Jens Eckes, Sven Seckler
 * @version 2.0 - 2012-08-15
 */
public class CalendarService {

    // Formatierung fuer DB-Anfragen, Access benoetigt dieses Format
    private final static SimpleDateFormat SQL_DATE_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd HH:mm");

    /**
     * Liefert ein Set mit allen Verfuegbarkeiten der Mitarbeiter.
     *
     * @return Set<Availability> alle Verfuegbarkeiten
     */
    static public Set<Availability> getAllAvailability() {

        Set<Availability> tempSet = new HashSet<Availability>();

        try {
            tempSet =
                    fillMaAvailability(tempSet, DBModelManager
                            .getEmployeeCalendarModel().getEmployeeCalendar());
            tempSet =
                    fillHolidayAvailability(tempSet, DBModelManager
                            .getHolidaysCalendarModel().getHolidayCalendar());
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return tempSet;
    }

    /**
     * Liefert ein Set mit allen Verfuegbarkeiten der Mitarbeiter, in einem
     * bestimmten Zeitraum.
     *
     * @param from
     *            Startdatum des Zeitraums
     * @param to
     *            Enddatum des Zeitraums
     * @return Set<Availability> alle Verfuegbarkeiten
     */
    static public Set<Availability> getAllAvailability(Date from, Date to) {

        Set<Availability> tempSet = new HashSet<Availability>();

        try {
            tempSet =
                    fillMaAvailability(tempSet, DBModelManager
                            .getEmployeeCalendarModel()
                            .getEmployeeCalendarInDateRange(from, to));
            tempSet =
                    fillHolidayAvailability(tempSet,
                            DBModelManager.getHolidaysCalendarModel()
                                    .getHolidayCalendar(from, to, false));
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return tempSet;
    }

    /**
     * Liefert ein Set mit allen Verfuegbarkeiten fuer einen bestimmten
     * Mitarbeiter.
     *
     * @param workerID
     *            ID des Mitarbeiters fuer den alle Verfuegbarkeiten geladen
     *            werden soll.
     * @return Set<Availability> alle Verfuegbarkeiten
     */
    static public Set<Availability> getAllWorkerAvailability(int workerID) {

        Set<Availability> tempSet = new HashSet<Availability>();

        tempSet =
                fillMaAvailability(tempSet,
                        DBModelManager.getEmployeeCalendarModel()
                                .getEmployeeCalendarForFID(workerID));

        return tempSet;
    }

    /**
     * Liefert ein Set mit allen Verfuegbarkeiten fuer einen bestimmten
     * Mitarbeiter in einem bestimmten Zeitraum.
     *
     * @param workerID
     *            ID des Mitarbeiters fuer den alle Verfuegbarkeiten geladen
     *            werden soll.
     * @param from
     *            Startdatum des Zeitraums
     * @param to
     *            Enddatum des Zeitraums
     * @return Set<Availability> alle Verfuegbarkeiten
     */
    public static Set<Availability> getAllWorkerAvailability(int workerID,
            Date from, Date to) {

        Set<Availability> tempSet = new HashSet<Availability>();
        tempSet =
                fillMaAvailability(tempSet, DBModelManager
                        .getEmployeeCalendarModel()
                        .getEmployeeCalendarInDateRange(from, to, workerID));
        return tempSet;
    }

    /**
     * Liefert ein Set mit allen Verfuegbarkeiten des Projekts (Feiertage).
     *
     * @return Set<Availability> alle Verfuegbarkeiten
     */
    public static Set<Availability> getProjectAvailability() {

        Set<Availability> tempSet = new HashSet<Availability>();

        try {
            tempSet =
                    fillHolidayAvailability(tempSet, DBModelManager
                            .getHolidaysCalendarModel().getHolidayCalendar());
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return tempSet;
    }

    /**
     * Liefert ein Set mit allen Verfuegbarkeiten des Projekts (Feiertage), fuer
     * einen bestimmten Zeitraum.
     *
     * @return Set<Availability> alle Verfuegbarkeiten
     */
    static public Set<Availability>
            getProjectAvailability(Date start, Date end) {

        Set<Availability> tempSet = new HashSet<Availability>();
        try {
            tempSet =
                    fillHolidayAvailability(tempSet,
                            DBModelManager.getHolidaysCalendarModel()
                                    .getHolidayCalendar(start, end, true));
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return tempSet;
    }

    /**
     * Setzt die Verfuegbarkeit fuer einen bestimmten Mitarbeiter.
     *
     * @param workerID
     *            ID des Mitarbeiters fuer den die Verfuegbarkeit gestzt werden
     *            soll.
     * @param availability
     *            Verfuegbarkeit des Mitarbeiters.
     * @throws SQLException
     */
    public static void setWorkerAvailability(String workerID,
            Availability availability) {

        EmployeeCalendar empCal =
                DBModelManager.getEmployeeCalendarModel().getEmployeeCalendar(
                        availability.getId());

        if (empCal != null) {
            empCal.setFid_emp(availability.getUserID());
            empCal.setBegin_time(availability.getStartTime());
            empCal.setEnd_time(availability.getEndTime());
            empCal.setDescription(availability.getDescription());
            empCal.setAvailability(availability.isAvailabe());
            empCal.setFull_time(availability.isAllDay());
            DBModelManager.getEmployeeCalendarModel().updateEmployeeCalendar(
                    empCal);
        } else {
            empCal = new EmployeeCalendar();
            empCal.setFid_emp(availability.getUserID());
            empCal.setBegin_time(availability.getStartTime());
            empCal.setEnd_time(availability.getEndTime());
            empCal.setDescription(availability.getDescription());
            empCal.setAvailability(availability.isAvailabe());
            empCal.setFull_time(availability.isAllDay());
            DBModelManager.getEmployeeCalendarModel().addNewEmployeeCalendar(
                    empCal);
        }

    }

    /**
     * Setzt die Verfuegbarkeit fuer das Projekt (Feiertage).
     *
     * @param availability
     *            Verfuegbarkeit des Projekts
     * @return true wenn die Projektverfuegbarkeit erfolgreich gespeichert wurde
     *         sonst false
     */
    public static boolean setProjectAvailability(Availability availability) {
        HolidayCalendar holCal =
                DBModelManager.getHolidaysCalendarModel().getHolidayCalendar(
                        availability.getId());

        if (holCal != null) {
            holCal.setBegin_time(availability.getStartTime());
            holCal.setEnd_time(availability.getEndTime());
            holCal.setTitle(availability.getDescription());
            holCal.setFull_time(availability.isAllDay());
            holCal.setAvailability(availability.isAvailabe());
            DBModelManager.getHolidaysCalendarModel().updateHolidayCalendar(
                    holCal);
        } else {
            holCal = new HolidayCalendar();
            holCal.setBegin_time(availability.getStartTime());
            holCal.setEnd_time(availability.getEndTime());
            holCal.setTitle(availability.getDescription());
            holCal.setFull_time(availability.isAllDay());
            holCal.setAvailability(availability.isAvailabe());
            DBModelManager.getHolidaysCalendarModel().addNewHolidayCalendar(
                    holCal);
        }

        return true;
    }

    /**
     * Diese Methode loescht eine Verfuegbarkeit aus dem Feiertagskalender.
     *
     * @param av
     *            zu loeschende Verfuegbarkeit vom Typ Availability
     * @return ture wenn es bei Dem Loeschen keien Fehler gab sonst false
     */
    public static boolean deleteProjectAvailability(final Availability av) {
        return DBModelManager.getHolidaysCalendarModel().deleteHolidayCalendar(
                av.getId());
    }

    /**
     * Diese Methode loescht eine Verfuegbarkeit aus dem MitarbeiterKalender.
     *
     * @param av
     *            zu loeschende Verfuegbarkeit vom Typ Availability
     * @return ture wenn es bei Dem Loeschen keien Fehler gab sonst false
     */
    public static boolean deleteWorkerAvailability(Availability av) {
        return DBModelManager.getEmployeeCalendarModel()
                .deleteEmployeeCalendar(av.getId());
    }

    /**
     * Dies Methode fuellt ein Set vom Typ Availability mit allen Daten aus
     * einem ResultSet.<br/>
     * Das ResultSet beinhaltet alle Eintraege des Mitarbeiter Kalenders.
     *
     * @param avlSet
     *            zufuellendes Set vom Typ Availability
     * @param maResultSet
     *            ResultSet mit allen daten
     * @return Set vom Typ Availability mit allen Daten des Resultsets
     * @throws SQLException
     */
    private static Set<Availability> fillMaAvailability(
            Set<Availability> avlSet, List<EmployeeCalendar> empCal) {
        for (EmployeeCalendar empCalEntry : empCal) {
            avlSet.add(new Availability(empCalEntry.getFid_emp(), empCalEntry
                    .isFull_time(), empCalEntry.isAvailability(), empCalEntry
                    .getBegin_time(), empCalEntry.getEnd_time(), empCalEntry
                    .getDescription(), empCalEntry.getId()));
        }
        return avlSet;
    }

    /**
     * Dies Methode fuellt ein Set vom Typ Availability mit allen Daten aus
     * einem ResultSet.<br/>
     * Das ResultSet beinhaltet alle Eintraege des Feiertagskalenders.
     *
     * @param avlSet
     *            zufuellendes Set vom Typ Availability
     * @param maResultSet
     *            ResultSet mit allen daten
     * @return Set vom Typ Availability mit allen Daten des Resultsets
     * @throws SQLException
     */
    private static Set<Availability> fillHolidayAvailability(
            Set<Availability> avlSet, List<HolidayCalendar> holCal)
            throws SQLException {
        for (HolidayCalendar holiday : holCal) {
            avlSet.add(new Availability(AvailabilityGraph.PROJECT_WORKER
                    .getId(), holiday.isFull_time(), holiday.isAvailability(),
                    holiday.getBegin_time(), holiday.getEnd_time(), holiday
                            .getTitle(), holiday.getId()));
        }
        return avlSet;
    }

    /**
     * Liefert einen TreeSet mit den Verfuegbarkeiten eines Mitarbeiters in
     * einem gegebenen Zeitpunkt
     *
     * @param workerID
     *            String ID des gewuenschten Mitarbeiters
     * @param start
     *            Startdatum des gewuenschten Zeitraums
     * @param end
     *            Enddatum des gewuenschten Zeitraums
     * @return TreeSet mit Verfuegbarkeiten
     */
    public static TreeSet<Availability> getRealWorkerAvailability(int workerID,
            Date start, Date end) {
        Set<Availability> workerAv =
                getAllWorkerAvailability(workerID, start, end);

        List<Availability> stdAv =
                new ArrayList<Availability>(getRealProjectAvailability(start,
                        end));
        List<Availability> notAv = new ArrayList<Availability>();
        List<Availability> addAv = new ArrayList<Availability>();

        for (Availability actualAv : workerAv) {
            if (actualAv.isAvailabe()) {
                addAv.add(actualAv);
            } else {
                notAv.add(actualAv);
            }
        }

        return merge(stdAv, notAv, addAv);
    }

    /**
     * Liefert einen TreeSet mit den Verfuegbarkeiten des Projektes in einem
     * gegebenen Zeitpunkt
     *
     * @param start
     *            Startdatum des gewuenschten Zeitraums
     * @param end
     *            Enddatum des gewuenschten Zeitraums
     * @return TreeSet mit Verfuegbarkeiten
     */
    public static TreeSet<Availability> getRealProjectAvailability(Date start,
            Date end) {
        Set<Availability> projectAv = getProjectAvailability(start, end);

        List<Availability> stdAv =
                new ArrayList<Availability>(createStdAvailability(start, end));
        List<Availability> notAv = new ArrayList<Availability>();
        List<Availability> addAv = new ArrayList<Availability>();

        for (Availability actualAv : projectAv) {
            if (actualAv.isAvailabe()) {
                addAv.add(actualAv);
            } else {
                notAv.add(actualAv);
            }
        }

        return merge(stdAv, notAv, addAv);
    }

    /**
     * Liefert entweder alle Verf�gbarkeiten oder alle Nicht-Verfuegbarkeiten
     * eines Mitarbeiters
     *
     * @param workerID
     *            gewuenschter Mitarbeiter
     * @param which
     *            Verfuegbarkeit oder nicht Verfuegbarkeit
     * @return Set mit den Verfuegbarkeiten
     */
    static public Set<Availability> getAllWorkerAvailability(int workerID,
            boolean which) {
        Set<Availability> avs = getAllWorkerAvailability(workerID);
        Set<Availability> wishAvs = new HashSet<Availability>();
        for (Availability actualAv : avs) {
            if (actualAv.isAvailabe() == which) {
                wishAvs.add(actualAv);
            }
        }
        return wishAvs;
    }

    /**
     * Liefert alle Verfuegbarkeiten oder alle Nicht-Verfuegbarkeiten des
     * Projekts
     *
     * @param which
     *            Verfuegbarkeit oder nicht Verfuegbarkeit
     * @return Set mit den Verfuegbarkeiten
     */
    static public Set<Availability> getProjectAvailability(boolean which) {
        Set<Availability> avs = getProjectAvailability();
        Set<Availability> wishAvs = new HashSet<Availability>();
        for (Availability actualAv : avs) {
            if (actualAv.isAvailabe() == which) {
                wishAvs.add(actualAv);
            }
        }
        return wishAvs;
    }

    /**
     * Fuehrt manuell eingetragene Verfuegbarkeiten zur realen Verfuegbarkeit
     * zusammen
     *
     * @param stdAv
     *            Verfuegbarkeit, die gesetzt wird, wenn nichts anderes
     *            angegeben wird
     * @param notAv
     *            Manuell gesetzte Nicht-Verfuegbarkeit, schliesst
     *            Standard-Verfuegbarkeiten aus
     * @param addAv
     *            Manuell gesetzte Verfuegbarkeit, ergaenzt
     *            Standard-Verfuegbarkeit
     * @return TreeSet mit den realen Verfuegbarkeiten
     */
    private static TreeSet<Availability> merge(List<Availability> stdAv,
            List<Availability> notAv, List<Availability> addAv) {

        Collections.sort(stdAv);
        Collections.sort(notAv);
        Collections.sort(addAv);

        TreeSet<Availability> mergedAv = new TreeSet<Availability>();

        List<Day> potentialFirstDays = new ArrayList<Day>();
        List<Day> potentialLastDays = new ArrayList<Day>();

        if (!stdAv.isEmpty()) {
            potentialFirstDays.add(stdAv.get(0).getStartDay());
            potentialLastDays.add(stdAv.get(stdAv.size() - 1).getStartDay());
        }
        if (!notAv.isEmpty()) {
            potentialFirstDays.add(notAv.get(0).getStartDay());
            potentialLastDays
                    .add((Day) notAv.get(notAv.size() - 1).getEndDay());
        }
        if (!addAv.isEmpty()) {
            potentialFirstDays.add(addAv.get(0).getStartDay());
            potentialLastDays.add(addAv.get(addAv.size() - 1).getEndDay());
        }

        if (!potentialFirstDays.isEmpty() && !potentialLastDays.isEmpty()) {
            Collections.sort(potentialFirstDays);
            Collections.sort(potentialLastDays);

            Day firstDay = potentialFirstDays.get(0);
            Day lastDay = potentialLastDays.get(potentialLastDays.size() - 1);

            GregorianCalendar actualDay = new GregorianCalendar();
            actualDay.setTime(firstDay);
            boolean additional = false;
            boolean dayLock = false;
            while (actualDay.getTime().before(lastDay)
                    || actualDay.getTime().equals(lastDay)) {
                if (!notAv.isEmpty()) {
                    Day startNotAv = notAv.get(0).getStartDay();
                    Day endNotAv = notAv.get(0).getEndDay();
                    if (inTimeSpan(startNotAv, actualDay, endNotAv)) {
                        // System.out.println(" in timespan "+actualDay.getTime());
                        dayLock = true;
                    } else if (endNotAv.before(actualDay.getTime())) {
                        dayLock = false;
                        notAv.remove(0);
                    }
                } else {
                    dayLock = false;
                }

                additional = false;
                while (!addAv.isEmpty()
                        && addAv.get(0).getStartDay()
                                .equals(new Day(actualDay.getTime()))) {
                    mergedAv.add(addAv.remove(0));
                    additional = true;
                }

                if (!additional && !dayLock) {
                    while (!stdAv.isEmpty()
                            && (stdAv.get(0).getStartDay()).equals(new Day(
                                    actualDay.getTime()))) {
                        mergedAv.add(stdAv.remove(0));
                    }
                } else {
                    while (!stdAv.isEmpty()
                            && (stdAv.get(0).getStartDay()).equals(new Day(
                                    actualDay.getTime()))) {
                        stdAv.remove(0);
                    }
                }

                actualDay.add(Calendar.DATE, 1);
            }
        }
        return mergedAv;
    }

    /**
     * Gibt an ob ein uebergebener Calender im angegebenen Zeitraum liegt
     *
     * @param start
     *            Gewuenschter Starttag
     * @param actualDay
     *            zu pruefender Tag
     * @param end
     *            Gewuenschter Endtag
     * @return Boolean ob Tag im uebergebenen Zeitraum liegt
     */
    private static boolean inTimeSpan(Day start, GregorianCalendar actualDay,
            Day end) {
        boolean startBefore =
                start.before(actualDay.getTime())
                        || start.equals(actualDay.getTime());
        boolean endAfter =
                (end.after(actualDay.getTime()) || end.equals(actualDay
                        .getTime()));
        return (startBefore && endAfter);
    }

    /**
     * Erzeugt ein Set mit Standart Verf�gbarkeiten (5 Tage a 8 Stunden) fuer
     * einen gewuenschten Zeitraum
     *
     * @param startDate
     *            Gewuenschtes Startdatum
     * @param endDate
     *            Gewuenschtes Enddatum
     * @return Set mit den Verfuegbarkeiten
     */
    private static Set<Availability> createStdAvailability(Date startDate,
            Date endDate) {

        int[] freeDayArray = Controller.FREE_DAYS;

        List<Integer> freeDays = new ArrayList<Integer>();
        for (int i : freeDayArray) {
            freeDays.add(i);
        }

        int[] workingHours = Controller.WORKING_HOURS;

        Set<Availability> av = new HashSet<Availability>();

        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(startDate);

        while (cal.getTime().before(endDate)) {

            cal.set(Calendar.HOUR_OF_DAY, workingHours[0]);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            Date startAv = cal.getTime();

            cal.set(Calendar.HOUR_OF_DAY, workingHours[1]);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            Date endAv = cal.getTime();

            cal.set(Calendar.HOUR_OF_DAY, workingHours[2]);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            Date startAv2 = cal.getTime();

            cal.set(Calendar.HOUR_OF_DAY, workingHours[3]);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            Date endAv2 = cal.getTime();

            if (!freeDays.contains(cal.get(Calendar.DAY_OF_WEEK))) {
                av.add(new Availability(AvailabilityGraph.PROJECT_WORKER
                        .getId(), false, true, startAv, endAv));
                av.add(new Availability(AvailabilityGraph.PROJECT_WORKER
                        .getId(), false, true, startAv2, endAv2));
            }

            cal.add(Calendar.DATE, 1);
        }

        return av;
    }
}