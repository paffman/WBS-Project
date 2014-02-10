package wpWorker;

/**
 * Studienprojekt:	WBS
 *
 * Kunde:				Pentasys AG, Jens von Gersdorff
 * Projektmitglieder:	Andre Paffenholz,
 * 						Peter Lange,
 * 						Daniel Metzler,
 * 						Samson von Graevenitz
 *
 *  Funktionalität zur GUI ChangePW
 *  prüfung aller notwendigen Bedingungen zum Passwortwechsel
 *  und Änderung des Passwortes in der DB
 *
 * @author Samson von Graevenitz
 * @version 0.5 - 28.12.2010
 */

import de.fhbingen.wbs.controller.ProjectSetupAssistant;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Arrays;

import javax.swing.JOptionPane;

import jdbcConnection.MySqlConnect;
import jdbcConnection.SQLExecuter;
import dbaccess.DBModelManager;
import dbaccess.data.Employee;
import c10n.C10N;
import de.fhbingen.wbs.translation.LocalizedStrings;
import de.fhbingen.wbs.translation.Login;
import de.fhbingen.wbs.translation.Messages;

public class ChangePW {

    /**
     * gui - Hält eine Instanz der GUI
     * usr - Hält den aktuellen User
     * sqlExec - stellt die Verknüpfung zur DB her
     */
    protected ChangePWGUI gui;
    private ChangePW dies;
    protected Worker usr;

    private final Messages messages;
    /**
     * Konstruktoraufruf zum ändern des Passworts des aktuellen Users
     * @param usr - aktuell eingeloggter User
     */
    public ChangePW(Worker usr){
        messages = LocalizedStrings.getMessages();
        dies = this;
        this.usr = usr;
        gui = new ChangePWGUI();
        new ChangePWButtonAction(dies);
    }

    /**
     * prüft ob alle Passwortfelder ausgefüllt sind
     * 
     * @return true/false, je nachdem ob Felder gefüllt oder nicht
     */
    protected boolean checkFieldsFilled() {
        if (gui.txfOldPW.getPassword().length > 0
                && gui.txfNewPW.getPassword().length > 0
                && gui.txfNewPWConfirm.getPassword().length > 0) {
            return true;
        }
        return false;
    }

    /**
     * prüft, ob das "alte Passwort" des Users mit dem übereinstimmt was in
     * der Datenbank hinterlegt ist.
     * 
     * @param user
     *            - ResultSet welches aktuellen User Hält
     * @return true/false, je nach dem ob "altes Passwort" korrekt eingegeben
     *         wurde
     */
    protected boolean checkOldPW() {
        return MySqlConnect.comparePasswort(gui.txfOldPW.getPassword());
    }

    /**
     * prüft, ob das "Neue Passwort" und "Passwort Confirm" übereinstimmen
     * 
     * @return true/false je nachdem, ob prüfung positiv oder negativ
     */
    protected boolean checkNewPW() {
        return ProjectSetupAssistant.arePasswordsEqual(
                gui.txfNewPW.getPassword(), gui.txfNewPWConfirm.getPassword());
    }

    /**
     * Speichert das geänderte Passwort in der Datenbank ab
     * 
     * @param user
     *            - ResultSet welches den aktuellen User Hält
     */
    protected void setNewPassword(Employee emp) {
        emp.setPassword(gui.txfNewPW.getPassword());
        boolean success =
                DBModelManager.getEmployeesModel().updateEmployee(emp);
        if (!success) {
            JOptionPane.showMessageDialog(gui, messages.passwordChangeError(),
                    null, JOptionPane.INFORMATION_MESSAGE);
        } else {
            SQLExecuter.closeConnection();
            MySqlConnect.setPassword(gui.txfNewPW.getPassword());
        }
    }

    /**
     * Checks the new Password for the password rules
     * 
     * @return True if the password matches the rules.
     */

    protected final boolean checkRules() {
        return ProjectSetupAssistant
                .isPasswordValid(gui.txfNewPW.getPassword());
    }
}
