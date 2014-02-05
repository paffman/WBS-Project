package calendar;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

/**
 * Studienprojekt: PSYS WBS 2.0<br/>
 * Kunde: Pentasys AG, Jens von Gersdorff<br/>
 * Projektmitglieder:<br/>
 * Michael Anstatt,<br/>
 * Marc-Eric Baumgärtner,<br/>
 * Jens Eckes,<br/>
 * Sven Seckler,<br/>
 * Lin Yang<br/>
 * Funktionen zur Datumsberechnung<br/>
 * 
 * @author Michael Anstatt, Lin Yang
 * @version 2.0 - 2012-08-21
 */
public class DateFunctions {

    /**
     * Dies Methode lifert ein Datum als String so formatiert, dass es direkt in
     * eine SQL Query fuer eine Access Datenbank verwendet werden kann.<br/>
     * Der Datums-String besteht aus dem Tag, den Stunden, den Minuten und den
     * Sekunden (Sekunden sind immer 00).<br/>
     * Fromat: #MM/DD/YYYY HH:MM:SS#
     * 
     * @param d
     *            Datum, das in einen Sting umgewandelt werden soll
     * @return Datums-Sting fuer Access Datenbank auf Minuten genau
     */
    public static String getDateString(Date d) {
        if (d != null) {
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            int month = c.get(Calendar.MONTH) + 1;
            return "#" + month + "/" + c.get(Calendar.DAY_OF_MONTH) + "/"
                    + c.get(Calendar.YEAR) + " " + c.get(Calendar.HOUR_OF_DAY)
                    + ":" + c.get(Calendar.MINUTE) + ":00#";
        } else {
            return "Null";
        }
    }

    /**
     * Dies Methode lifert ein Datum als String so formatiert, dass es direkt in
     * eine SQL Query fuer eine Access Datenbank verwendet werden kann.<br/>
     * Der Datums-String besteht aus dem Tag<br/>
     * Fromat: #MM/DD/YYYY#
     * 
     * @param d
     *            Datum, das in einen Sting umgewandelt werden soll
     * @return Datums-Sting fuer Access Datenbank auf den Tag genau
     */
    public static String getDateStringOnlyDay(Date d) {
        if (d != null) {
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            int month = c.get(Calendar.MONTH) + 1;
            return "#" + month + "/" + c.get(Calendar.DAY_OF_MONTH) + "/"
                    + c.get(Calendar.YEAR) + "#";
        } else {
            return "Null";
        }
    }

    /**
     * Dies Methode vergleicht zwei Date Objekte auf Gleicheit.<br/>
     * Ein Datum ist gleich einem anderen wenn es bis auf die Minute gleich ist.
     * 
     * @param d1
     *            Erstes zu vergleichendes Datum vom Typ Date
     * @param d2
     *            Zweites zu vergleichendes Datum vom Typ Date
     * @return true wenn beite Dati bis auf die Minute gleich sind sonst false
     */
    public static boolean equalsDate(Date d1, Date d2) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(d2);
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                && c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)
                && c1.get(Calendar.HOUR_OF_DAY) == c2.get(Calendar.HOUR_OF_DAY)
                && c1.get(Calendar.MINUTE) == c2.get(Calendar.MINUTE);
    }

    /**
     * Generates a Timestamp or Null from a Date. *
     * 
     * @param date
     *            a Date for which a Timestamp is returned.
     * @return A Timestamp or null, if the date was null.
     */
    public static Timestamp getTimesampOrNull(final Date date) {
        if (date == null) {
            return null;
        }
        return new Timestamp(date.getTime());
    }

}
