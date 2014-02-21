package de.fhbingen.wbs.wpConflict;

import de.fhbingen.wbs.calendar.DateFunctions;
import de.fhbingen.wbs.dbaccess.DBModelManager;
import de.fhbingen.wbs.translation.LocalizedStrings;
import de.fhbingen.wbs.translation.Messages;
import de.fhbingen.wbs.globals.Workpackage;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *  Class that represents a single conflict.
 */
public class Conflict {
    /**
     * Date Format.
     * TODO find new home for this poor little thing.
     */
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
            "dd.MM.yyyy HH:mm");

    /**
     * Conflict code for start date can't be achieved.
     */
    public static final int STARTWISH_FAIL = 0;

    /**
     * Conflict code for end date can't be achieved.
     */
    public static final int ENDWISH_FAIL = 1;

    /**
     * Conflict code for changes resources.
     */
    public static final int CHANGED_RESOURCES = 2;

    /**
     * Conflict code for changed dates.
     */
    public static final int CHANGED_WISHDATES = 3;

    /**
     * Conflict code for new work package.
     */
    public static final int NEW_WP = 4;

    /**
     * Conflict code for changed dependencies.
     */
    public static final int CHANGED_DEPENDENCIES = 5;

    /**
     * Conflict code for changed bac.
     */
    public static final int CHANGED_BAC = 6;

    /**
     * Conflict code for deleted work package.
     */
    public static final int DELETED_WP = 7;

    /**
     * Conflict code for changed active state.
     */
    public static final int CHANGED_ACTIVESTATE = 8;

    /**
     * Translation interface.
     */
    private final Messages messageStrings;

    /**
     * Date when the conflict occured.
     */
    private Date date;

    /**
     * Reason id.
     */
    private int reason;

    /**
     * Id of user that caused the conflict.
     */
    private int userId;

    /**
     * AP that triggered the conflict.
     */
    private int triggerWp;

    /**
     * AP that is affected by the conflict.
     */
    private int affectedWp;

    /**
     * Conflict id.
     */
    private int id;

    /**
     * String Ids of triggering and affected wp.
     */
    private String triggerApStringId, affectedApStringId;

    /**
     * Constructor.
     *
     * @param conflictDate
     *          Date the conflict occurred.
     * @param conflictReason
     *            ID of the reason for the conflict.
     * @param conflictUserId
     *            Id of user that caused the conflict.
     * @param conflictTriggeringWp
     *            AP that triggered the conflict.
     * @param conflictAffectedWp
     *            AP that is affected by the conflict.
     */
    public Conflict(final Date conflictDate, final int conflictReason,
                    final int conflictUserId,
                    final Workpackage conflictTriggeringWp,
                    final Workpackage conflictAffectedWp) {
        this.date = conflictDate;
        this.reason = conflictReason;
        this.userId = conflictUserId;
        if (conflictTriggeringWp != null) {
            this.triggerWp = conflictTriggeringWp.getWpId();
            this.triggerApStringId = conflictTriggeringWp.getStringID();
        }
        if (conflictAffectedWp != null) {
            this.affectedWp = conflictAffectedWp.getWpId();
            this.affectedApStringId = conflictAffectedWp.getStringID();
        }
        this.messageStrings = LocalizedStrings.getMessages();
    }

    /**
     * Constructor.
     *
     * @param conflictDate
     *          Date the conflict occurred.
     * @param conflictReason
     *            ID of the reason for the conflict.
     * @param conflictUserId
     *            Id of user that caused the conflict.
     * @param conflictTriggeringAP
     *            AP that triggered the conflict.
     */
    public Conflict(final Date conflictDate,
                    final int conflictReason,
                    final int conflictUserId,
                    final Workpackage conflictTriggeringAP) {
        this(conflictDate, conflictReason, conflictUserId, conflictTriggeringAP,
                null);
    }

    /**
     * Constructor.
     *
     * @param conflictDate Date the conflict occurred.
     * @param conflictReason
     *            ID of the reason for the conflict.
     * @param conflictUserId
     *            Id of user that caused the conflict.
     */
    public Conflict(final Date conflictDate,
                    final int conflictReason,
                    final int conflictUserId) {
        this(conflictDate, conflictReason, conflictUserId, null, null);
    }

    /**
     * Constructor.
     *
     * @param conflictDate
     *          Date the conflict occurred.
     * @param conflictReason
     *            ID of the reason for the conflict.
     * @param conflictUserId
     *            Id of user that caused the conflict.
     * @param conflictTriggeringWpId
     *            AP that triggered the conflict.
     * @param conflictAffectedWpId
     *            AP that is affected by the conflict.
     */
    public Conflict(final Date conflictDate,
                    final int conflictReason,
                    final int conflictUserId,
                    final int conflictTriggeringWpId,
                    final int conflictAffectedWpId) {
        super();
        this.date = conflictDate;
        this.reason = conflictReason;
        this.userId = conflictUserId;
        this.triggerWp = conflictTriggeringWpId;
        this.affectedWp = conflictAffectedWpId;

        if (conflictTriggeringWpId <= 0) {
            this.triggerApStringId = "";
        } else {
            this.triggerApStringId = DBModelManager.getWorkpackageModel()
                    .getWorkpackage(conflictTriggeringWpId).getStringID();
        }

        if (conflictAffectedWpId <= 0) {
            this.affectedApStringId = "";
        } else {
            this.affectedApStringId = DBModelManager.getWorkpackageModel()
                    .getWorkpackage(conflictAffectedWpId).getStringID();
        }
        this.messageStrings = LocalizedStrings.getMessages();
    }

    /**
     * Description of the conflict reason.
     *
     * @return Description of the conflict reason.
     */
    public final String getReasonString() {
        switch (reason) {
        case STARTWISH_FAIL:
            return messageStrings.startDateCanNotBeAchieved();
        case ENDWISH_FAIL:
            return messageStrings.endDateCanNotBeAchieved();
        case CHANGED_RESOURCES:
            return messageStrings.resourcesChanged() + " " + messageStrings
                    .recalculate();
        case CHANGED_WISHDATES:
            return messageStrings.targetDateChanged() + " " + messageStrings
                    .recalculate();
        case NEW_WP:
            return messageStrings.newApsWereCreated() + " " + messageStrings
                    .recalculate();
        case CHANGED_DEPENDENCIES:
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

    @Override
    public final boolean equals(final Object obj) {

        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Conflict)) {
            return false;
        }
        Conflict other = (Conflict) obj;
        if (reason < 2) {
            if (this.affectedApStringId != null) {
                return DateFunctions.equalsDate(this.date, other.date)
                        && this.reason == other.reason
                        && this.userId == other.userId
                        && this.triggerWp == other.getTriggerWp()
                        && this.affectedWp == other.getAffectedWp();
            } else {
                return DateFunctions.equalsDate(this.date, other.date)
                        && this.reason == other.reason
                        && this.userId == other.userId
                        && this.triggerWp == other.getTriggerWp();
            }
        } else {
            return other.getReason() == this.getReason();
        }

    }


    @Override
    public final String toString() {
        return triggerApStringId
                + ", "
                + DBModelManager.getEmployeesModel().getEmployee(userId)
                        .getLogin() + ", " + reason + ", "
                + DateFunctions.getDateString(date) + ", " + affectedApStringId;
    }

    /**
     * Liefert einen HashCode des Objekts Wenn Reason 1 oder 2 wird ein
     * richtiger HashCode erstellt sonst einfach die Reason ID zurueckgegeben.
     *
     * @return Hash code.
     */
    @Override
    public final int hashCode() {
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
     * Date the conflict occurred.
     *
     * @return Date of the conflict.
     */
    public final Date getDate() {
        return date;
    }

    /**
     * Returns the code for the reason.
     *
     * @return int of the reason.
     */
    public final int getReason() {
        return reason;
    }

    /**
     * @return The ID of the user that caused the conflict.
     */
    public final int getUserId() {
        return userId;
    }

    /**
     * @return the triggerWp.
     */
    public final int getTriggerWp() {
        return triggerWp;
    }

    /**
     * @return the affectedWp.
     */
    public final int getAffectedWp() {
        return affectedWp;
    }

    /**
     * @return the triggerApStringId.
     */
    public final String getTriggerApStringId() {
        return triggerApStringId;
    }

    /**
     * @return the affectedApStringId.
     */
    public final String getAffectedApStringId() {
        return affectedApStringId;
    }

    /**
     * @return the dbId.
     */
    public final int getId() {
        return id;
    }

    /**
     * Lists the triggering and affected work package as a string if
     * applicable.
     * This message was moved from ConflictTable and works exactly like the
     * method that was defined there.
     *
     * @return String of affected work packages.
     */
    public final String createAffectedString() {
        String affectedAPs = getTriggerApStringId();
        if (getAffectedApStringId() != null) {
            affectedAPs += "; " + getAffectedApStringId();
        }

        if (affectedAPs != null) {
            return affectedAPs;
        }
        return "";
    }

    /**
     * Creates a string array containing all the data relevant to a conflict
     * for display in the table.
     * @return A string array containing all the data relevant to a conflict.
     */

    public final String[] createStringArray() {

        return new String[] {
        DATE_FORMAT.format(getDate()),
                getReasonString(),
                DBModelManager.getEmployeesModel().getEmployee(getUserId())
                        .getLogin(),
                createAffectedString() };
    }
}
