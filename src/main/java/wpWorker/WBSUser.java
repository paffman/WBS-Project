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
 * GUI zum Hinzufügen eines Aufwandes zum Arbeitspaket
 *
 * @author Daniel Metzler
 * @version 1.9 - 17.02.2011
 */

import de.fhbingen.wbs.translation.LocalizedStrings;
import de.fhbingen.wbs.translation.Messages;

import javax.swing.JOptionPane;

import dbaccess.DBModelManager;
import dbaccess.data.Employee;
import wpOverview.WPOverview;
import wpOverview.WPOverviewGUI;

public class WBSUser {

    private final Messages messageStrings;
    /**
     * Variablen für die WBSMitarbeiterGUI und den SQLExecuter Variable für
     * den Mitarbeiter und die WPOverview
     */

    protected WBSUserGUI gui = new WBSUserGUI();
    protected Worker mitarbeiter;
    protected WPOverview over;
    private WBSUser dies;

    /**
     * Konstruktoraufruf wenn vorhander Mitarbeiter geändert werden soll
     *
     * @param mit
     *            Mitarbeiter aus der WPOverview wird übergeben
     * @param over
     *            WPOverview wird übergeben initialisiert die Textfelder mit
     *            den Daten des ausgewählten Benutzers
     */
    public WBSUser(Worker mit, WPOverview over) {
        this.messageStrings = LocalizedStrings.getMessages();
        dies = this;
        mitarbeiter = mit;
        this.over = over;
        gui.btnhinzufuegen.setVisible(false);
        gui.txfLogin.setEnabled(false);
        new WBSUserButtonAction(dies);
        initialize();
        gui.setTitle(mit.getLogin() + " | " + mit.getName() + ", "
                + mit.getVorname());
    }

    /**
     * Konstruktoraufruf wenn neuer Mitarbeiter angelegt wird
     *
     * @param over
     *            WPOverview wird übergeben
     */
    public WBSUser(WPOverview over) {
        this.messageStrings = LocalizedStrings.getMessages();
        this.over = over;
        gui.btnedit.setVisible(false);
        gui.btnPwReset.setVisible(false);
        new WBSUserButtonAction(this);
        gui.setTitle(LocalizedStrings.getLogin().newUserWindowTitle());
    }

    /**
     * Setzt die Textfelder in der WBSMitarbeiterGUI mit den Daten des Benutzers
     * anhand des übergebenen Mitarbeiters
     */
    public void initialize() {
        gui.txfLogin.setText(mitarbeiter.getLogin());
        gui.txfVorname.setText(mitarbeiter.getVorname());
        gui.txfName.setText(mitarbeiter.getName());
        gui.cbBerechtigung
                .setSelected((mitarbeiter.getBerechtigung() == 1 ? true : false));
        gui.txfTagessatz.setText(Double.toString(mitarbeiter.getTagessatz()));
    }

    /**
     * wird von der Methode addMitarbeiter() und changeMitarbeiter() aufgerufen
     * Führt die PlausibilitätsPrüfung der einzelnen Felder in der
     * WBSMitarbeiterGUI durch
     *
     * @return true = alles OK, false = es sind Unstimmigkeiten aufgetreten
     */
    public boolean check() {
        // Login wurde nicht ausgefüllt
        if (gui.txfLogin.getText().equals("")) {
            JOptionPane.showMessageDialog(gui, "Bitte Login ausfüllen");
            return false;
        }
        // Name wurde nicht ausgefüllt
        if (gui.txfName.getText().equals("")
                || gui.txfVorname.getText().equals("")) {
            JOptionPane.showMessageDialog(gui, "Bitte Namen ausfüllen");
            return false;
        }

        try {
            if (gui.txfTagessatz.getText().equals("")
                    || Double.parseDouble(gui.txfTagessatz.getText()) <= 0) {
                JOptionPane
                        .showMessageDialog(gui, messageStrings.fillFieldError(
                                LocalizedStrings.getWbs().dailyRate()));
                return false;
            }
            JOptionPane
                    .showMessageDialog(gui,
                            messageStrings.valueInFieldIsNotANumber(
                                    LocalizedStrings.getWbs().dailyRate()));
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;

    }

    /**
     * wird beim Betätigen des Buttons "btnedit" aufgerufen Mitarbeiter-Daten
     * werden aktualisiert
     *
     * @return true = erfolgreiche übernahme der Änderungen, false =
     *         übernahme fehlgeschlagen
     */
    public boolean changeMitarbeiter(Worker worker) {
        // Mitarbeiter wird aus der Datenbank geholt
        Employee emp =
                DBModelManager.getEmployeesModel().getEmployee(
                        worker.getLogin());
        if (emp != null) {
            emp.setFirst_name(worker.getVorname());
            emp.setLast_name(worker.getName());
            emp.setProject_leader(worker.getBerechtigung() == 1);
            emp.setDaily_rate(worker.getTagessatz());
            boolean success =
                    DBModelManager.getEmployeesModel().updateEmployee(emp);
            if (success) {
                over.reload();
                WPOverviewGUI.setStatusText(messageStrings.userChanged());
            }
            return success;
        } else {
            return false;
        }
    }

    /**
     * wird beim Betätigen des Buttons "btnHinzufügen" aufgerufen fügt einen
     * neuen Mitarbeiter in die Datenbank ein
     *
     * @return true = erfolgreich, false = fehlgeschlagen
     */
    public boolean addMitarbeiter(Worker worker) {
        boolean vorhanden = false;
        // holt sich alle Mitarbeiter und prüft ob der neu eingegeben
        // Mitarbeiter bereits vorhanden ist
        Employee emp =
                DBModelManager.getEmployeesModel().getEmployee(
                        worker.getLogin());
        vorhanden = emp != null;
        // wenn Mitarbeiter nicht vorhanden ist wird er in die Datenbank
        // geschrieben
        if (!vorhanden) {
            emp = new Employee();
            emp.setLogin(worker.getLogin());
            emp.setFirst_name(worker.getVorname());
            emp.setLast_name(worker.getName());
            emp.setProject_leader(worker.getProjLeiter());
            emp.setPassword("1234");
            emp.setDaily_rate(worker.getTagessatz());
            DBModelManager.getEmployeesModel().addNewEmployee(emp);
        }
        if (vorhanden)
            return false;
        else {
            over.reload();
            WPOverviewGUI.setStatusText(messageStrings.userAdded());
            return true;
        }
    }

    public Worker getActualWorker() {
        return mitarbeiter;
    }

    public void passwordReset() {
        Employee employee =
                DBModelManager.getEmployeesModel().getEmployee(
                        mitarbeiter.getId());
        if (employee != null) {
            employee.setPassword("1234");
            if (DBModelManager.getEmployeesModel().updateEmployee(employee)) {
                JOptionPane.showMessageDialog(null,
                        messageStrings.passwordHasBeenReset());
            } else {
                JOptionPane.showMessageDialog(null,
                        messageStrings.passwordChangeError());
            }
        }
    }
}
