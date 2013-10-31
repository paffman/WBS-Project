package wpConflict;

import globals.Workpackage;

import java.util.Date;

import calendar.DateFunctions;

/**
 * Studienprojekt: PSYS WBS 2.0<br/>
 * 
 * Kunde:	Pentasys AG, Jens von Gersdorff<br/>
 * 
 * Projektmitglieder:<br/>
 *			Michael Anstatt,<br/>
 *			Marc-Eric Baumgärtner,<br/>
 *			Jens Eckes,<br/>
 *			Sven Seckler,<br/>
 *			Lin Yang<br/>
 * 
 * Diese Klasse wir benutzt um Konflikt-Daten zu handhaben.<br/>
 * 
 * @author Michael Anstatt, Marc-Eric Baumgaertner, Jens Eckes, Sven Seckler
 * @version 2.0 - 2012-08-19
 */
public class Conflict {
	public static final int STARTWISH_FAIL = 0;
	public static final int ENDWISH_FAIL = 1;
	public static final int CHANGED_RESOURCES = 2;
	public static final int CHANGED_WISHDATES = 3;
	public static final int NEW_WP = 4;
	public static final int CHANGED_DEPENDECIES = 5;
	public static final int CHANGED_BAC = 6;
	public static final int DELETED_WP = 7;
	public static final int CHANGED_ACTIVESTATE = 8;

	public static final int TRIGGER = 0;
	public static final int AFFECTED = 1;

	private Date date;
	private int reason;
	private String userId;
	private String triggerAp;
	private String affectedAP;

	// ctor

	/**
	 * Konstruktor
	 * @param date Datum an dem der Konflikt ausgeloest wurde
	 * @param reason Integer Wert des ausgeloesten Konflikts
	 * @param userId User der den Konflikt ausgeloest hat
	 * @param triggerAP Arbeitspaket welches den Konflikt ausgeloest hat
	 * @param affectedAP Arbeitspaket welches vom Konflikt betroffen ist(optional)
	 */
	public Conflict(Date date, int reason, String userId, Workpackage triggerAP, Workpackage affectedAP) {
		this.date = date;
		this.reason = reason;
		this.userId = userId;
		this.triggerAp = triggerAP.getStringID();
		this.affectedAP = affectedAP.getStringID();
	}
	
	/**
	 * Konstruktor
	 * @param date Datum an dem der Konflikt ausgeloest wurde
	 * @param reason Integer Wert des ausgeloesten Konflikts
	 * @param userId User der den Konflikt ausgeloest hat
	 * @param triggerAP Arbeitspaket welches den Konflikt ausgeloest hat
	 */
	public Conflict(Date date, int reason, String userId, Workpackage triggerAP) {
		this.date = date;
		this.reason = reason;
		this.userId = userId;
		this.triggerAp = triggerAP.getStringID();
	}
	
	/**
	 * Konstruktor
	 * @param date Datum an dem der Konflikt ausgeloest wurde
	 * @param reason Integer Wert des ausgeloesten Konflikts
	 * @param userId User der den Konflikt ausgeloest hat
	 */
	public Conflict(Date date, int reason, String userId) {
		this.date = date;
		this.reason = reason;
		this.userId = userId;
	}

	/**
	 * Konstruktor
	 * @param date Datum an dem der Konflikt ausgeloest wurde
	 * @param reason Integer Wert des ausgeloesten Konflikts
	 * @param userId User der den Konflikt ausgeloest hat
	 * @param triggerAP String ID des Arbeitspaket, welches den Konflikt ausgeloest hat
	 * @param affectedAP String ID des Arbeitspaket, welches vom Konflikt betroffen ist
	 */
	public Conflict(Date date, int reason, String userId, String triggerAp, String affectedAP) {
		super();
		this.date = date;
		this.reason = reason;
		this.userId = userId;
		this.triggerAp = triggerAp;
		this.affectedAP = affectedAP;
	}

	
	
	/**
	 * Liefert eine Beschreibung des Konflikts
	 * @return String mit Beschreibung
	 */
	public String getReasonString() {
		switch (reason) {
		case STARTWISH_FAIL:
			return "Das gewünschte Startdatum kann nicht eingehalten werden";
		case ENDWISH_FAIL:
			return "Das gewünschte Enddatum kann nicht eingehalten werden";
		case CHANGED_RESOURCES:
			return "Die Ressourcen wurden geändert, Neuberechnung starten";
		case CHANGED_WISHDATES:
			return "Ein Wunschdatum wurde geändert, Neuberechnung starten";
		case NEW_WP:
			return "Neue APs wurden erstellt, Neuberechnung starten";
		case CHANGED_DEPENDECIES:
			return "Abhängigkeiten wurden geändert, Neuberechnung starten";
		case CHANGED_BAC:
			return "Der BAC eines APs wurde geändert, Neuberechnung starten";
		case DELETED_WP:
			return "Es wurde ein AP gelöscht, Neuberechnung starten";
		case CHANGED_ACTIVESTATE:
			return "Es wurde ein AP aktiv/inaktiv gesetzt, Neuberechnung starten";
		}
		return null;

	}
	
	// getter/setter
	
	/**
	 * Gibt Datum des Konflikts
	 * @return Datum des Konflikts
	 */
	public Date getDate() {
		return date;
	}
	/**
	 * Setzt das Datum des Konflikts
	 * @param Datum des Konflikts
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	/**
	 * Gibt den Fehlercode zurueck
	 * @return int des Fehlercodes
	 */
	public int getReason() {
		return reason;
	}
	/**
	 * Setzt den Fehlercode
	 * @param reason int des Fehlercodes
	 */
	public void setReason(int reason) {
		this.reason = reason;
	}
	
	/**
	 * Liefert das ausloesende AP
	 * @return String ID des Ausloesenden APs
	 */
	public String getTriggerAp() {
		return triggerAp;
	}
	/**
	 * Setzt den ausloesende AP
	 * @param triggerAp String ID des betroffenen AP
	 */
	public void setTriggerAp(String triggerAp) {
		this.triggerAp = triggerAp;
	}
	/**
	 * Liefert das betroffene AP
	 * @return String ID des betroffenen AP
	 */
	public String getAffectedAP() {
		return affectedAP;
	}
	/**
	 * Setzt das betroffene AP 
	 * @param affectedAP String id des betroffenen AP
	 */
	public void setAffectedAP(String affectedAP) {
		this.affectedAP = affectedAP;
	}
	/**
	 * Liefert die UserID zurueck
	 * @return String der UserID
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * Setzt den ausloesenden User
	 * @param userId String ID des User
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * Vergleicht ein Conflictobjekt mit sich selbst
	 * @return
	 */
	@Override
	public boolean equals(Object obj) {
		
		if (obj == null) return false;
		if (!(obj instanceof Conflict)) return false;
		Conflict other = (Conflict) obj;
		if (reason < 2) {
			if(this.affectedAP != null) {
				return DateFunctions.equalsDate(this.date, other.date) && this.reason == other.reason && 
					this.userId.equals(other.userId) && this.triggerAp.equals(other.getTriggerAp()) &&
					this.affectedAP.equals(other.getAffectedAP());
			} else {
				return DateFunctions.equalsDate(this.date, other.date) && this.reason == other.reason && 
						this.userId.equals(other.userId) && this.triggerAp.equals(other.getTriggerAp());
			}
		} else {
			return other.getReason() == this.getReason();
		}
		
		
	}
	/**
	 * Liefert einen String des Conflict Objekts
	 * @return String mit Informationen des Objekts
	 */
	@Override
	public String toString() {
		return triggerAp + ", " + userId + ", " + reason + ", " + DateFunctions.getDateString(date) + ", " + affectedAP;
	}
	/**
	 * Liefert einen HashCode des Objekts
	 * Wenn Reason 1 oder 2 wird ein richtiger HashCode erstellt sonst einfach die Reason ID zurueckgegeben
	 * @return
	 */
	@Override
	public int hashCode() {
		if (reason < 2) {
			String s = triggerAp + userId + reason + DateFunctions.getDateString(date) + affectedAP;
			return s.hashCode();
		} else {
			return reason;
		}
	}
}
