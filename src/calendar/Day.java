package calendar;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
 * Repraesentiert einen Tag (ohne Beruecksichtigung von Uhrzeiten)<br/>
 * 
 * @author Michael Anstatt
 * @version 2.0 - 2012-08-20
 */
public class Day extends Date {

	private static final long serialVersionUID = 4043833541398751602L;

	/**
	 * Konstruktor
	 * @param date Datum, zu dem der Tag (ohne Uhrzeit) benoetigt wird
	 */
	public Day(Date date) {
		this(date, false);
	}
	
	/**
	 * Konstruktor
	 * 
	 * @param date Datum, zu dem der Tag (ohne Uhrzeit) benoetigt wird
	 * @param endOfDay wenn true springt Datum zum Ende des Tages, 23:59:59 Uhr
	 */
	public Day(Date date, boolean endOfDay) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		if(endOfDay) {
			cal.add(Calendar.HOUR, 23);
			cal.add(Calendar.MINUTE, 59);
			cal.add(Calendar.SECOND, 59);
		}
		super.setTime(cal.getTimeInMillis());
	}
}