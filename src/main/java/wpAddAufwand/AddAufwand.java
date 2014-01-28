package wpAddAufwand;

import globals.Workpackage;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import dbServices.WorkerService;
import dbaccess.DBModelManager;
import dbaccess.data.WorkEffort;
import wpShow.*;
import wpWorker.Worker;

/**
 * Studienprojekt: WBS Kunde: Pentasys AG, Jens von Gersdorff<br/>
 * Projektmitglieder: Andre Paffenholz, <br/>
 * Peter Lange, <br/>
 * Daniel Metzler,<br/>
 * Samson von Graevenitz<br/>
 * Studienprojekt: PSYS WBS 2.0<br/>
 * Kunde: Pentasys AG, Jens von Gersdorff<br/>
 * Projektmitglieder: <br/>
 * Michael Anstatt,<br/>
 * Marc-Eric Baumgärtner,<br/>
 * Jens Eckes,<br/>
 * Sven Seckler,<br/>
 * Lin Yang<br/>
 * Funktionen zum Hinzufügen eines Arbeitspaketes<br/>
 * 
 * @author Daniel Metzler, Michael Anstatt
 * @version 2.0 2012-08-21
 */
public class AddAufwand {

    /**
     * Variablen für die AddAufwandGui und den SQLExecuter Variablen für den
     * User, WPID's und WPName
     */
    public AddAufwandGui gui;
    public WPShow wpshow;
    private Workpackage wp;

    /**
     * Default-Konstruktor
     * 
     * @param workpackage
     * @param wpID
     *            ID des Arbeitspakets
     * @param usr
     *            Benutzer dessen Aufwand eingetragen werden soll
     * @param show
     *            WPSHow GUI zur Referenzierung
     * @param wpname
     *            Names des Arbeitspaketes Werte aus der WPShow werden
     *            übergeben
     */
    public AddAufwand(WPShow wpshow, Workpackage wp) {
        gui = new AddAufwandGui();
        this.wp = wp;
        this.wpshow = wpshow;
        new AddAufwandButtonAction(this);
        initialize();
        gui.setTitle("Aufwand für " + wp);
        gui.setLocationRelativeTo(wpshow.getMainFrame());
        gui.setLocation((int) gui.getLocation().getX() - gui.getHeight() / 2,
                (int) (gui.getLocation().getY() - gui.getWidth() / 2));
    }

    /**
     * wird im Konstruktor aufgerufen Die LVLID's werden initialisiert durch die
     * im Konstrukor übergebene wpID Textfelder für die ArbeitspaketID und den
     * Arbeitspaket Name werden gefüllt wenn Projektleiterrechte vorliegen wird
     * die ComboBox für die Benutzer mit allen zuständigen für dieses WP
     * hinzugefügt ansonsten nur der Mitarbeiter selbst
     */
    public void initialize() {

        gui.txfNr.setText(wp.getStringID());
        gui.txfName.setText(wp.getName());

        List<Worker> allWorkers = WorkerService.getRealWorkers();
        List<String> workerIDs = wp.getWorkerLogins();
        List<Worker> dummies = new ArrayList<Worker>();

        for (String actualID : workerIDs) {
            dummies.add(new Worker(actualID));
        }

        if (wpshow.getUser().getProjLeiter()) {
            for (Worker actualWorker : allWorkers) {
                if (dummies.contains(actualWorker)) {
                    gui.cobUser.addItem(actualWorker);
                }
            }
        } else {
            for (Worker actualWorker : allWorkers) {
                if (dummies.contains(actualWorker)
                        && wpshow.getUser().getLogin()
                                .equals(actualWorker.getLogin())) {
                    gui.cobUser.addItem(actualWorker);
                    gui.cobUser.setSelectedItem(actualWorker);
                }
            }

            gui.cobUser.setEnabled(false);
        }
    }

    /**
     * prüft ob alle Eingabe ausgefüllt sind
     * 
     * @return true/false, je nachdem ob Felder gefüllt oder nicht
     */
    public boolean checkFieldsFilled() {
        if (gui.txfBeschreibung.getText().length() > 0
                && gui.txfAufwand.getText().length() > 0) {
            return true;
        }
        return false;
    }

    /**
     * wird vom Betätigen des Buttons für Editieren aufgerufen Der neue
     * Aufwand wird in die "Aufwand"-Tabelle geschrieben
     * 
     * @return true wenn Schreiben erfolgreich war, bei Fehler false
     */
    public boolean addAufwand() {
        try {

            // Prüfen, ob Datum valide ist
            String[] tmpDate;
            int month, day;

            if (!gui.txfDatum.getText().startsWith(".")) {
                tmpDate = gui.txfDatum.getText().split("\\.");
                day = Integer.parseInt(tmpDate[0]);
                month = Integer.parseInt(tmpDate[1]);
                if (day < 1 || day > 31 || month < 1 || month > 12)
                    return false;
            } else
                return false;

            // Datum wird vom Textfeld "Datum" geholt und als SQL Date
            // umgewandelt (nötig für das Reinschreiben in die
            // Datenbank-Tabelle)
            java.sql.Date dte = null;
            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
            java.util.Date dt = formatter.parse(gui.txfDatum.getText());
            dte = new java.sql.Date(dt.getTime());
            // Kommas des Textfeldes Aufwand werden durch Punkte ersetzt, damit
            // es zu keinen Fehlern kommt
            String myaufwand = gui.txfAufwand.getText().replace(",", ".");

            String login = gui.cobUser.getSelectedItem().toString();
            int loginIndex = login.indexOf("|");
            String userLogin = login.substring(0, loginIndex - 1);

            // neuer Aufwand wird in die Datenbanktabelle Aufwand geschrieben
            WorkEffort effort = new WorkEffort();
            effort.setFid_wp(wp.getWpId());
            effort.setFid_emp(DBModelManager.getEmployeesModel()
                    .getEmployee(userLogin).getId());
            effort.setRec_date(dte);
            effort.setDescription(gui.txfBeschreibung.getText());
            effort.setEffort(Double.parseDouble(myaufwand));
            return DBModelManager.getWorkEffortModel().addNewWorkEffort(effort);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}
