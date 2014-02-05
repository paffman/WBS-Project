package globals;

import dbaccess.DBModelManager;
import de.fhbingen.wbs.translation.LocalizedStrings;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import jdbcConnection.SQLExecuter;
import wpOverview.WPOverview;

/**
 * Studienprojekt: PSYS WBS 2.0<br/>
 * Kunde: Pentasys AG, Jens von Gersdorff<br/>
 * Projektmitglieder:<br/>
 * Michael Anstatt,<br/>
 * Marc-Eric Baumgärtner,<br/>
 * Jens Eckes,<br/>
 * Sven Seckler,<br/>
 * Lin Yang<br/>
 * Verwaltung von Info-Messages, Fehlermeldungen und Debugging <br/>
 * Enthaelt ausserdem ausserdem Globale Statische Wert wie freie Tage
 * (Samstag/Sonntag), Arbetiszeiten und Formatierungen fuer Datum und Uhrzeit
 *
 * @author Michael Anstatt
 * @version 2.0 - 2012-08-20
 */
public class Controller {

    /**
     * Kann an showConsoleMessage uebergeben werden um die Nachricht als
     * Dauerberechnungs-Debugging zu kennzeichnen
     */
    public static final int TIME_CALCULATION_DEBUG = 0;

    /**
     * Kann an showConsoleMessage uebergeben werden um die Nachricht als
     * Vorgaenger/Nachfolger-Debugging zu kennzeichnen
     */
    public static final int HIERACHY_DEBUG = 1;

    public static final SimpleDateFormat DATE_DAY = new SimpleDateFormat(
            "dd.MM.yyyy");
    public static final SimpleDateFormat DATE_DAY_TIME = new SimpleDateFormat(
            "dd.MM.yyyy HH:mm");
    public static final SimpleDateFormat DATE_TIME = new SimpleDateFormat(
            "HH:mm");

    public final static DecimalFormat DECFORM = new DecimalFormat("#0.00");

    /**
     * Freie Tage, in der Regel Samstag und Sonntag
     */
    public static final int[] FREE_DAYS = new int[] { Calendar.SATURDAY,
            Calendar.SUNDAY };

    /**
     * Arbeitszeiten
     */
    public static final int[] WORKING_HOURS = new int[] { 8, 12, 14, 18 };

    /**
     * Wenn true, wird Debugging-Info zur Dauerberechnung in die Datei Log.txt
     * auf dem Desktop geschrieben
     */
    private static boolean time_calculation = false;

    /**
     * Wenn true, wird Debugging-Info zur Vorgaenger/Nachfolger-Berechnung in
     * die Datei Log.txt auf dem Desktop geschrieben
     */
    private static boolean hierachy = false;

    // private static PrintStream outErr = System.err;
    // private static PrintStream outNor = System.out;
    private static PrintStream outErr = null;
    private static PrintStream outNor = outErr;

    /**
     * Zeigt Popup mit uebergebener Nachricht an
     *
     * @param message
     */
    public static void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    /**
     * Zeigt Feheler-Popuo mit uebergebener Nachricht an
     *
     * @param message
     */
    public static void showError(String message) {
        JOptionPane.showMessageDialog(null, message, LocalizedStrings
                .getErrorMessages().error(), JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Gibt eine Nachricht auf der Konsole oder in die Debugging-Datei aus
     *
     * @param message
     * @param type
     *            siehe static final _DEBUG Datenelemente
     */
    public static void showConsoleMessage(Object message, int type) {
        showConsoleMessage(message.toString(), false, type);
    }

    /**
     * Gibt eine Nachricht auf der Konsole oder in die Debugging-Datei aus
     *
     * @param message
     * @param type
     *            siehe static final _DEBUG Datenelemente
     */
    public static void showConsoleMessage(String message, int type) {
        showConsoleMessage(message, false, type);
    }

    /**
     * Gibt eine Nachricht auf der Konsole oder in die Debugging-Datei aus
     *
     * @param object
     * @param error
     *            wenn true wird auf Error-Konsole oder Debug ausgegeben
     * @param type
     *            siehe static final _DEBUG Datenelemente
     */
    public static void
            showConsoleMessage(Object object, boolean error, int type) {
        showConsoleMessage(object.toString(), error, type);
    }

    /**
     * Gibt eine Nachricht auf der Konsole oder in die Debugging-Datei aus
     *
     * @param object
     * @param error
     *            wenn true wird auf Error-Konsole oder Debug ausgegeben
     * @param type
     *            siehe static final _DEBUG Datenelemente
     */
    public static void showConsoleMessage(String message, boolean error,
            int type) {
        if (outErr == null) {
            try {
                String path =
                        System.getProperty("user.home") + "/Desktop/Log.txt";
                path.replace("\\", "/");
                outErr = new PrintStream(new FileOutputStream(new File(path)));
                outNor = outErr;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        PrintStream out;
        if (error) {
            out = outErr;
        } else {
            out = outNor;
        }

        switch (type) {
        case TIME_CALCULATION_DEBUG:
            if (time_calculation) {
                out.println(message);
            }
            break;
        case HIERACHY_DEBUG:
            if (hierachy) {
                out.println(message);
            }
        }

    }

    /**
     * Datenbank-Verbindung beenden und Semaphor freigeben
     */
    public static void leaveDB() {
        if (WPOverview.getUser().getProjLeiter()) {
            DBModelManager.getSemaphoreModel().leaveSemaphore("pl",
                    WPOverview.getUser().getId());
        }
        SQLExecuter.closeConnection();
        System.out.println(LocalizedStrings.getMessages().logOut());
    }

    /**
     * Komponente mittig relativ zu ihrer Parent-Komponente platzieren
     *
     * @param parent
     * @param child
     */
    public static void centerComponent(JFrame parent, JFrame child) {
        child.setLocationRelativeTo(parent);
    }
}
