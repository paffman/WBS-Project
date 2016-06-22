package de.fhbingen.wbs.dbServices;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import de.fhbingen.wbs.dbaccess.DBModelManager;
import de.fhbingen.wbs.dbaccess.data.PlannedValue;
import de.fhbingen.wbs.globals.Workpackage;
import sun.util.resources.cldr.aa.CalendarData_aa_ER;

/**
 * Diese Klasse dient als Verbindung zur Datenbank fuer das Speicher und
 * Loeschen verschiedener Werte.<br/>
 * In dies Klasse wurden Methoden des Vorgaenger Projekts ausgelagert.<br/>
 */
public class ValuesService {

    /**
     * Liefert eine Map mit allen PV, des Projekts, und deren Datum fuer einen
     * bestimmten Zeitraum.
     *
     * @param from
     *            Startdatum des Zeitraums
     * @param to
     *            Enddatum des Zeitraums
     * @return Map<Date, Double> Datum Date und den entsprechenden PV Double
     *         fuer diese Datum
     */
    public static Map<Date, Double> getPVs(Date from, Date to) {
        return fillPVMap(new HashMap<Date, Double>(), DBModelManager
                .getPlannedValueModel().getPlannedValue(from, to));
    }

    /**
     * Liefert die Planned Values fuer einen bestimmten Zeitraum fuer ein
     * bestimmtes Arbeitspaket
     *
     * @param from
     *            Startdatum des Zeitraums
     * @param to
     *            Enddatum des Zeitraums
     * @param wpID
     *            Arbeitspaket
     * @return Map mit Zuordnung Datum -> PV
     */
    public static Map<Date, Double> getWPPVs(Date from, Date to, int wpID) {
        return fillPVMap(new HashMap<Date, Double>(), DBModelManager
                .getPlannedValueModel().getPlannedValue(from, to, wpID));
    }

    /**
     * Uebertraegt den errechneten Planned Value an die Datenbank.
     *
     * @param apID
     *            konkatenierten hierarchisch StringID eines Arbeitspaketes.
     * @param date
     *            Datum fuer den der Planned Value erstellt wurde.
     * @param pv
     *            Errechneter Wert fuer den Planned Value.
     * @return Bestaetigt das erfolgreiche durchlaufen.
     */
    public static boolean savePv(int apID, Date date, double pv) {
        PlannedValue newPv = new PlannedValue();
        newPv.setFid_wp(apID);
        newPv.setPv_date(date);
        newPv.setPv(pv);
        return DBModelManager.getPlannedValueModel().addNewPlannedValue(newPv);
    }

    /**
     * Uebertraegt den errechneten Planned Value an die Datenbank.
     *
     * @param apID
     *            konkatenierten hierarchisch StringID eines Arbeitspaketes.
     * @param date
     *            Datum fuer den der Planned Value erstellt wurde.
     * @param pv
     *            Errechneter Wert fuer den Planned Value.
     * @return Bestaetigt das erfolgreiche durchlaufen.
     */
    public static boolean updatePv(int apID, Date date, double pv) {
        return DBModelManager.getPlannedValueModel().updatePlannedValue(date,
                apID, pv);
    }

    /**
     * Liefert den PV fuer ein bestimmtes Arbeitsparket
     *
     * @param apID
     *            ID des Arbeitsparkets
     * @return double PV des Arbeitsparketes
     */
    public static double getApPv(final int apID) {
        Calendar calendar = getNextFriday(new Date(System.currentTimeMillis()));
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        double ergPv =
                DBModelManager.getPlannedValueModel().getPlannedValue(
                        calendar.getTime(), apID, false);

        if (ergPv == Double.MIN_NORMAL) {
            ergPv =
                    DBModelManager.getPlannedValueModel().getPlannedValue(
                            calendar.getTime(), apID, true);
        }
        if (ergPv == Double.MIN_NORMAL) {
            ergPv = 0.0;
        }
        return ergPv;
    }

    /**
     * Liefert den PV fuer ein bestimmtes Arbeitsparket zu einem bestimmten
     * Datum
     *
     * @param apID
     *            ID des Arbeitsparkets
     * @param date
     *            Tag
     * @return double PV des AP
     */
    public static double getApPv(int apID, Calendar day) {
        day = getNextFriday(day.getTime());
        double ergPv =
                DBModelManager.getPlannedValueModel().getPlannedValue(
                        day.getTime(), apID, false);

        if (ergPv == Double.MIN_NORMAL) {
            ergPv =
                    DBModelManager.getPlannedValueModel().getPlannedValue(
                            day.getTime(), apID, true);
            if (ergPv == Double.MIN_NORMAL) {
                ergPv = 0.0;
            }
        }

        return ergPv;
    }

    /**
     * Prueft ob zu diesem Zeitpunkt schon ein Planned Value fuer dieses
     * Arbeitspaket in der Datenbank eingetragen ist
     *
     * @param apID
     *            ID eines Arbeitspakets
     * @param day
     *            Kalender mit Tag zu dem der PV gewuenscht wird
     * @return
     */
    public static boolean pvExists(int apID, Calendar day) {
        day = getNextFriday(day.getTime());
        boolean exists = DBModelManager.getPlannedValueModel().getPlannedValue(day.getTime(), apID) != Double.NaN;
        return exists;
    }

    /**
     * Loescht alle PlannedValues
     *
     * @return true wenn alle PV erfolgreich geloescht wurde sonst false
     */
    public static boolean deleteAllPV() {
        return DBModelManager.getPlannedValueModel().deletePlannedValue();
    }

    /**
     * Loescht ein bestimmtes PlannedValue fuer ein Arbeitspaket
     *
     * @param apID
     *            String stringID des Arbeitspakets
     * @return true wenn das PV erfolgreich geloescht wurde sonst false
     */
    public static boolean deletePV(int apID) {
        return DBModelManager.getPlannedValueModel().deletePlannedValue(apID);
    }

    /**
     * Loescht ein bestimmtes PlannedValue fuer ein Arbeitspaket und ein
     * bestimmtes Datum
     *
     * @param apID
     *            String stringID des Arbeitspakets
     * @param date
     *            Date Datum fuer welches der PV geloescht werden soll
     * @return true wenn das PV erfolgreich geloescht wurde sonst false
     */
    public static boolean deletePV(int apID, Date date) {
        return DBModelManager.getPlannedValueModel().deletePlannedValue(date,
                apID);
    }

    /**
     * Diese Methode prueft ein Datum ob es ein Freitag ist<br/>
     * wenn ja wir eine Calendar instance mit dem Datum zurueck gegeben<br/>
     * wenn nein wird das Datum auf den naechsten Freitag gesetzt und eine
     * Calendar instance mit dem Datum zurueck gegeben.<br/>
     *
     * @param date
     *            Date wenn null wir der naechste Freitag zurueck geliefert sont
     *            wir das Datum getestet ob es sich um einen Freitag handelt
     * @return Calendar mit dem Datum des naechsten Freitags
     * @deprecated function disabled
     */
    public static Calendar getNextFriday(final Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
       // while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.FRIDAY) {
       //     calendar.add(Calendar.DATE, 1);
       // }

        return calendar;
    }

    /**
     * Diese Methode prueft ein Datum ob es ein Freitag ist<br/>
     * wenn ja wir eine Calendar instance mit dem Datum zurueck gegeben<br/>
     * wenn nein wird das Datum auf den naechsten Freitag gesetzt und eine
     * Calendar instance mit dem Datum zurueck gegeben.<br/>
     *
     * @param date
     *            Date wenn null wir der vorherigen Freitag zurueck geliefert
     *            sont wir das Datum getestet ob es sich um einen Freitag
     *            handelt
     * @return Calendar mit dem Datum des vorherigen Freitags
     * @deprecated function disabled
     */
    public static Calendar getPreviousFriday(final Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
       // while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.FRIDAY) {
       //     calendar.add(Calendar.DATE, -1);
       // }
        return calendar;
    }

    /**
     * @param ergMap
     * @param resSet
     * @return
     * @throws SQLException
     */
    private static Map<Date, Double> fillPVMap(Map<Date, Double> ergMap,
            List<PlannedValue> pvList) {
        for (PlannedValue pv : pvList) {
            ergMap.put(pv.getPv_date(), pv.getPv() + 0.0);
        }
        return ergMap;
    }


    /**
     * Diese Methode errechnet PVs vom Start- bis Enddatum für ein Workpackage.
     * Verwendet wird das errechnete Start-/Enddatum des Workpackages.
     * Für jeden Arbeitstag wird der PV-Intervall aufsummiert, für Samstage und Sonntage
     * wird der vorherige PV übernommen und nicht aufsummiert.
     * @param wp
     * @return pvMap
     */
    public static Map<Date, Double> calcPVs(Workpackage wp) {
        Map<Date, Double> pvMap = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(wp.getStartDateCalc());

        int workDays = getWorkingDaysBetweenTwoDates(wp.getStartDateCalc(), wp.getEndDateCalc());
        double interval = wp.getBac_kosten() / workDays;
        double pv = 0;
        while(calendar.getTimeInMillis() <= wp.getEndDateCalc().getTime() ); {
            if(! (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY  || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY ) ) {
                pv += interval;
            }

            pvMap.put(calendar.getTime(), pv);
            calendar.add(Calendar.DATE,1);
        }
        return pvMap;
    }

    //Errechnet Arbeitstage zwischen Start- und Enddatum, Dauer ist mindestens 1 Tag
    private static int getWorkingDaysBetweenTwoDates(Date startDate, Date endDate) {
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);

        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);

        int workDays = 0;

        do {
            //excluding start date
            startCal.add(Calendar.DAY_OF_MONTH, 1);
            if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                ++workDays;
            }
        } while (startCal.getTimeInMillis() < endCal.getTimeInMillis());

        if(workDays == 0)
            return 1;

        return workDays;
    }

}
