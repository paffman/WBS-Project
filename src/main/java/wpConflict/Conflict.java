package wpConflict;

import de.fhbingen.wbs.translation.LocalizedStrings;
import de.fhbingen.wbs.translation.Messages;
import globals.Workpackage;

import java.util.Date;

import calendar.DateFunctions;
import dbaccess.DBModelManager;

/**
 * Studienprojekt: PSYS WBS 2.0<br/>
 * Kunde: Pentasys AG, Jens von Gersdorff<br/>
 * Projektmitglieder:<br/>
 * Michael Anstatt,<br/>
 * Marc-Eric Baumgärtner,<br/>
 * Jens Eckes,<br/>
 * Sven Seckler,<br/>
 * Lin Yang<br/>
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
    private final Messages messageStrings;

    private Date date;
    private int reason;
    private int userId;
    private int triggerAp;
    private int affectedAP;
    private int id;

    private String triggerApStringId, affectedApStringId;

    // ctor

    /**
     * Konstruktor
     *
     * @param date
     *            Datum an dem der Konflikt ausgeloest wurde
     * @param reason
     *            Integer Wert des ausgeloesten Konflikts
     * @param userId
     *            User der den Konflikt ausgeloest hat
     * @param triggerAP
     *            Arbeitspaket welches den Konflikt ausgeloest hat
     * @param affectedAP
     *            Arbeitspaket welches vom Konflikt betroffen ist(optional)
     */
    public Conflict(Date date, int reason, int userId, Workpackage triggerAP,
            Workpackage affectedAP) {
        this.date = date;
        this.reason = reason;
        this.userId = userId;
        this.triggerAp = triggerAP.getWpId();
        this.affectedAP = affectedAP.getWpId();
        this.triggerApStringId = triggerAP.getStringID();
        this.affectedApStringId = affectedAP.getStringID();
        this.messageStrings = LocalizedStrings.getMessages();
    }

    /**
     * Konstruktor
     *
     * @param date
     *            Datum an dem der Konflikt ausgeloest wurde
     * @param reason
     *            Integer Wert des ausgeloesten Konflikts
     * @param userId
     *            User der den Konflikt ausgeloest hat
     * @param triggerAP
     *            Arbeitspaket welches den Konflikt ausgeloest hat
     */
    public Conflict(Date date, int reason, int userId, Workpackage triggerAP) {
        this.date = date;
        this.reason = reason;
        this.userId = userId;
        this.triggerAp = triggerAP.getWpId();
        this.triggerApStringId = triggerAP.getStringID();
        this.messageStrings = LocalizedStrings.getMessages();
    }

    /**
     * Konstruktor
     *
     * @param date
     *            Datum an dem der Konflikt ausgeloest wurde
     * @param reason
     *            Integer Wert des ausgeloesten Konflikts
     * @param userId
     *            User der den Konflikt ausgeloest hat
     */
    public Conflict(Date date, int reason, int userId) {
        this.date = date;
        this.reason = reason;
        this.userId = userId;
        this.messageStrings = LocalizedStrings.getMessages();
    }

    /**
     * Konstruktor
     *
     * @param date
     *            Datum an dem der Konflikt ausgeloest wurde
     * @param reason
     *            Integer Wert des ausgeloesten Konflikts
     * @param userId
     *            User der den Konflikt ausgeloest hat
     * @param triggerAP
     *            String ID des Arbeitspaket, welches den Konflikt ausgeloest
     *            hat
     * @param affectedAP
     *            String ID des Arbeitspaket, welches vom Konflikt betroffen ist
     */
    public Conflict(Date date, int reason, int userId, int triggerAp,
            int affectedAP) {
        super();
        this.date = date;
        this.reason = reason;
        this.userId = userId;
        this.triggerAp = triggerAp;
        this.affectedAP = affectedAP;
        this.triggerApStringId =
                triggerAp <= 0 ? "" : DBModelManager.getWorkpackageModel()
                        .getWorkpackage(triggerAp).getStringID();
        this.affectedApStringId =
                affectedAP <= 0 ? "" : DBModelManager.getWorkpackageModel()
                        .getWorkpackage(affectedAP).getStringID();
        this.messageStrings = LocalizedStrings.getMessages();
    }

    /**
     * Liefert eine Beschreibung des Konflikts
     *
     * @return String mit Beschreibung
     */
    public String getReasonString() {
        switch (reason) {
        case STARTWISH_FAIL:
            return messageStrings.startDateCantBeAchieved();
        case ENDWISH_FAIL:
            return messageStrings.endDateCantBeAchieved();
        case CHANGED_RESOURCES:
            return messageStrings.resourcesChanged() + " " + messageStrings
                    .recalculate();
        case CHANGED_WISHDATES:
            return messageStrings.wishDateChanged() + " " + messageStrings
                    .recalculate();
        case NEW_WP:
            return messageStrings.newApsWereCreated() + " " + messageStrings
                    .recalculate();
        case CHANGED_DEPENDECIES:
            return messageStrings.dependenciesHaveChanged() + " "
                    + messageStrings.recalculate();
        case CHANGED_BAC:
            return messageStrings.bacHasChanged() + " " + messageStrings
                    .recalculate();
        case DELETED_WP:
            return messageStrings.apWasDeleted() + " " + messageStrings
                    .recalculate();
        case CHANGED_ACTIVESTATE:
            return messageStrings.apActiveStateChanged() + " "
                    + messageStrings.recalculate();
        default:
            return null;
        }
    }

    // getter/setter

    /**
     * Gibt Datum des Konflikts
     *
     * @return Datum des Konflikts
     */
    public Date getDate() {
        return date;
    }

    /**
     * Setzt das Datum des Konflikts
     *
     * @param Datum
     *            des Konflikts
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Gibt den Fehlercode zurueck
     *
     * @return int des Fehlercodes
     */
    public int getReason() {
        return reason;
    }

    /**
     * Setzt den Fehlercode
     *
     * @param reason
     *            int des Fehlercodes
     */
    public void setReason(int reason) {
        this.reason = reason;
    }

    /**
     * Liefert die UserID zurueck
     *
     * @return String der UserID
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Setzt den ausloesenden User
     *
     * @param userId
     *            String ID des User
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Vergleicht ein Conflictobjekt mit sich selbst
     *
     * @return
     */
    @Override
    public boolean equals(Object obj) {

        if (obj == null)
            return false;
        if (!(obj instanceof Conflict))
            return false;
        Conflict other = (Conflict) obj;
        if (reason < 2) {
            if (this.affectedApStringId != null) {
                return DateFunctions.equalsDate(this.date, other.date)
                        && this.reason == other.reason
                        && this.userId == other.userId
                        && this.triggerAp == other.getTriggerAp()
                        && this.affectedAP == other.getAffectedAP();
            } else {
                return DateFunctions.equalsDate(this.date, other.date)
                        && this.reason == other.reason
                        && this.userId == other.userId
                        && this.triggerAp == other.getTriggerAp();
            }
        } else {
            return other.getReason() == this.getReason();
        }

    }

    /**
     * Liefert einen String des Conflict Objekts
     *
     * @return String mit Informationen des Objekts
     */
    @Override
    public String toString() {
        return triggerApStringId
                + ", "
                + DBModelManager.getEmployeesModel().getEmployee(userId)
                        .getLogin() + ", " + reason + ", "
                + DateFunctions.getDateString(date) + ", " + affectedApStringId;
    }

    /**
     * Liefert einen HashCode des Objekts Wenn Reason 1 oder 2 wird ein
     * richtiger HashCode erstellt sonst einfach die Reason ID zurueckgegeben
     *
     * @return
     */
    @Override
    public int hashCode() {
        if (reason < 2) {
            String s =
                    affectedApStringId
                            + DBModelManager.getEmployeesModel()
                                    .getEmployee(userId).getLogin() + reason
                            + DateFunctions.getDateString(date)
                            + affectedApStringId;
            return s.hashCode();
        } else {
            return reason;
        }
    }

    /**
     * @return the triggerAp
     */
    public final int getTriggerAp() {
        return triggerAp;
    }

    /**
     * @param triggerAp
     *            the triggerAp to set
     */
    public final void setTriggerAp(int triggerAp) {
        this.triggerAp = triggerAp;
    }

    /**
     * @return the affectedAP
     */
    public final int getAffectedAP() {
        return affectedAP;
    }

    /**
     * @param affectedAP
     *            the affectedAP to set
     */
    public final void setAffectedAP(int affectedAP) {
        this.affectedAP = affectedAP;
    }

    /**
     * @return the triggerApStringId
     */
    public final String getTriggerApStringId() {
        return triggerApStringId;
    }

    /**
     * @param triggerApStringId
     *            the triggerApStringId to set
     */
    public final void setTriggerApStringId(String triggerApStringId) {
        this.triggerApStringId = triggerApStringId;
    }

    /**
     * @return the affectedApStringId
     */
    public final String getAffectedApStringId() {
        return affectedApStringId;
    }

    /**
     * @param affectedApStringId
     *            the affectedApStringId to set
     */
    public final void setAffectedApStringId(String affectedApStringId) {
        this.affectedApStringId = affectedApStringId;
    }

    /**
     * @return the dbId
     */
    public final int getId() {
        return id;
    }

    /**
     * @param dbId
     *            the dbId to set
     */
    public final void setId(int id) {
        this.id = id;
    }
}
