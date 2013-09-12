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
 * Repraesentiert eine Verfuegbarkeit oder Nicht-Verfuegbarkeit eines Mitarbeiters oder des Projekts<br/>
 * 
 * @author Jens Eckes, Michael Anstatt
 * @version 2.0 - 20.08.2012
 */
public class Availability implements Comparable<Availability> {
	private String userID;
	private String description;
	private boolean allDay;
	private boolean availabe;
	private Date startDate;
	private Date endDate;
	private Integer id;

	/**
	 * Konstruktor
	 * 
	 * @param userID
	 * @param allDay
	 * @param available
	 * @param startDate
	 * @param endDate
	 */
	public Availability(String userID, boolean allDay, boolean available, Date startDate, Date endDate) {
		this(userID, allDay, available, startDate, endDate, "", null);
	}

	/**
	 * Konstruktor
	 * 
	 * @param userID
	 * @param allDay
	 * @param availabe
	 * @param startDate
	 * @param endDate
	 * @param description
	 */
	public Availability(String userID, boolean allDay, boolean availabe, Date startDate, Date endDate, String description) {
		this(userID, allDay, availabe, startDate, endDate, description, null);
	}

	/**
	 * Konstruktor
	 * 
	 * @param userID
	 * @param allDay
	 * @param available
	 * @param startDate
	 * @param endDate
	 * @param id
	 */
	public Availability(String userID, boolean allDay, boolean available, Date startDate, Date endDate, int id) {
		this(userID, allDay, available, startDate, endDate, "", id);
	}

	/**
	 * Konstruktor
	 * 
	 * @param userID
	 * @param allDay
	 * @param availabe
	 * @param startDate
	 * @param endDate
	 * @param description
	 * @param id
	 */
	public Availability(String userID, boolean allDay, boolean availabe, Date startDate, Date endDate, String description, Integer id) {
		this.userID = userID;
		this.description = description;
		this.allDay = allDay;
		this.availabe = availabe;

		this.startDate = calcStart(startDate);
		this.endDate = calcEnd(endDate);

		this.setId(id);
	}

	/**
	 * Berechnet den Startzeitpunkt, also den Anfang des Tages, 0 Uhr
	 * 
	 * @param date Startdatum mit Zeitangabe
	 * @return Startdatum ohne Zeitangabe (0 Uhr)
	 */
	private Date calcStart(Date date) {
		if (allDay) {
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(date);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			return cal.getTime();
		} else {
			return date;
		}
	}

	/**
	 * Berechnet das tatsaechliche "Ende eines Datums", also bei ganztaegigen Daten 23:59:59
	 * @param date Datum, dessen Ende berechnet werden soll
	 * @return Enddatum (entweder selbe wie Parameter oder 23:59:59 wenn ganztaegige Availability vorliegt)
	 */
	private Date calcEnd(Date date) {
		if (allDay) {
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(date);
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
			cal.set(Calendar.MILLISECOND, 0);
			return cal.getTime();
		} else {
			return date;
		}

	}

	/**
	 * Getter fuer die UserID (entspricht dem Login-Name des Besitzers der Availability)
	 * @return
	 */
	public String getUserID() {
		return userID;
	}

	/**
	 * Setter fuer User-ID (entspricht dem Login-Name des Besitzers der Availability)
	 * @param userID
	 */
	public void setUserID(String userID) {
		this.userID = userID;
	}

	/**
	 * Getter fuer ganztaegige Availability
	 * @return true wenn ganztaegig
	 */
	public boolean isAllDay() {
		return allDay;
	}

	/**
	 * Getter fuer available
	 * @return false wenn BEsitzer zu dieser Zeit nicht verfuegbar ist
	 */
	public boolean isAvailabe() {
		return availabe;
	}

	/**
	 * Setter fuer die Availability
	 * 
	 * @param availabe wenn true ist der Besitzer dieser Availability verfuegbar ansonsten ist er zu dieser Zeit nicht verfuegbar
	 */
	public void setAvailabe(boolean availabe) {
		this.availabe = availabe;
	}

	/**
	 * Getter fuer den Beginn der Availability mit Uhrzeit
	 * 
	 * @return Beginn der Availability mit Uhrzeit
	 */
	public Date getStartTime() {
		return startDate;
	}

	/**
	 * Setter fuer den Beginn der Availability mit Uhrzeit
	 * 
	 * @param endDate
	 */
	public void setStartTime(Date startDate) {
		this.startDate = calcStart(startDate);
	}

	/**
	 * Setter fuer das Ende der Availability mit Uhrzeit
	 * 
	 * @param endDate
	 */
	public void setEndTime(Date endDate) {
		this.endDate = calcEnd(endDate);
	}

	/**
	 * Gibt das Ende der Availability mit Uhrzeit zurueck
	 * 
	 * @return Ende der Availability mit Uhrzeit
	 */
	public Date getEndTime() {
		return endDate;
	}

	/**
	 * Getter Description
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Setter Description
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Getter ID
	 * 
	 * @return
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Setter fuer die ID
	 * 
	 * @param id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return der Tag an dem die Availability beginnt
	 */
	public Day getStartDay() {
		return new Day(startDate);
	}

	/**
	 * @return der Tag an dem die Availability endet
	 */
	public Day getEndDay() {
		return new Day(endDate);
	}

	/**
	 * Gibt die Laenge der Availability
	 * 
	 * @return die Dauer in Stunden
	 */
	public int getDuration() {
		long difference = this.getEndTime().getTime() - this.getStartTime().getTime();
		return (int) (difference / (1000 * 60 * 60));
	}

	@Override
	public String toString() {
		return "UserId: " + userID + " description: " + description + " allDay: " + allDay + " available: " + availabe + " startDate: " + startDate
				+ " endDate: " + endDate;
	}

	/**
	 * Standardmaessig nach Datum sortieren
	 */
	@Override
	public int compareTo(Availability other) {

		if (this.startDate.compareTo(other.startDate) != 0) {
			return this.startDate.compareTo(other.startDate);
		} else {
			return this.endDate.compareTo(other.endDate);
		}

	}
}
