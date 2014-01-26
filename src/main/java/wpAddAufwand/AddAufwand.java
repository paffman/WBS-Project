package wpAddAufwand;

import dbServices.WorkerService;
import globals.Workpackage;
import java.sql.Date;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import jdbcConnection.SQLExecuter;
import wpShow.WPShow;
import wpWorker.Worker;

/**
 * Funktionen zum Hinzufügen eines Arbeitspaketes.
 */
public class AddAufwand {

    /**
     * UI element for aufwand entry.
     */
    private final AddAufwandGui gui;
    /**
     * Work package window.
     */
    private final WPShow wpshow;
    /**
     * Current work package.
     */
    private final Workpackage wp;

    /**
     * Maximum days a month can have.
     */
    private static final int MAX_DAY_OF_MONTH = 31;
    /**
     * Maximum month a year can have.
     */
    private static final int MAX_MONTH_OF_YEAR = 12;

    /**
     * Default-Konstruktor.
     *
     * @param workpackage            workpackage.
     * @param show        WPSHow GUI zur Referenzierung
     */
    public AddAufwand(final WPShow show, final Workpackage workpackage) {
        gui = new AddAufwandGui();
        this.wp = workpackage;
        this.wpshow = show;
        new AddAufwandButtonAction(this);
        initialize();
        getGui().setTitle("Aufwand für " + workpackage);
        getGui().setLocationRelativeTo(show.getMainFrame());
        getGui().setLocation((int) getGui().getLocation().getX() - getGui()
                .getHeight() / 2, (int) (getGui().getLocation().getY()
                - getGui().getWidth() / 2));
    }

    /**
     * Wird im Konstruktor aufgerufen.
     * Die LVLID's werden initialisiert durch die im Konstrukor übergebene wpID
     * Textfelder für die ArbeitspaketID und den Arbeitspaket Name werden
     * gefüllt
     * wenn Projektleiterrechte vorliegen wird die ComboBox für die Benutzer
     * mit allen zuständigen für dieses WP hinzugefügt
     * ansonsten nur der Mitarbeiter selbst
     */
    private void initialize() {

        getGui().getTxfNr().setText(wp.getStringID());
        getGui().getTxfName().setText(wp.getName());

        List<Worker> allWorkers = WorkerService.getRealWorkers();
        List<String> workerIDs = wp.getWorkers();
        List<Worker> dummies = new ArrayList<>();

        for (String actualID : workerIDs) {
            dummies.add(new Worker(actualID));
        }

        if (getWpshow().getUser().getProjLeiter()) {
            for (Worker actualWorker : allWorkers) {
                if (dummies.contains(actualWorker)) {
                    getGui().getCobUser().addItem(actualWorker);
                }
            }
        } else {
            for (Worker actualWorker : allWorkers) {
                if (dummies.contains(actualWorker) && getWpshow().getUser()
                        .getLogin().equals(actualWorker.getLogin())) {
                    getGui().getCobUser().addItem(actualWorker);
                    getGui().getCobUser().setSelectedItem(actualWorker);
                }
            }

            getGui().getCobUser().setEnabled(false);
        }
    }

    /**
     * Prüft ob alle Eingabe ausgefüllt sind.
     *
     * @return true/false, je nachdem ob Felder gefüllt oder nicht
     */
    public final boolean checkFieldsFilled() {
        return getGui().getTxfBeschreibung().getText().length() > 0 && getGui().getTxfAufwand().getText().length() > 0;
    }

    /**
     * Wird vom Betätigen des Buttons für Editieren aufgerufen.
     * Der neue Aufwand wird in die "Aufwand"-Tabelle geschrieben.
     *
     * @return true wenn Schreiben erfolgreich war, bei Fehler false
     */
    public final boolean addAufwand() {
        try {

            //Prüfen, ob Datum valide ist
            String[] tmpDate;
            int month, day;

            if (!getGui().getTxfDatum().getText().startsWith(".")) {
                tmpDate = getGui().getTxfDatum().getText().split("\\.");
                day = Integer.parseInt(tmpDate[0]);
                month = Integer.parseInt(tmpDate[1]);
                if (day < 1 || day > MAX_DAY_OF_MONTH || month < 1 || month
                        > MAX_MONTH_OF_YEAR) {
                    return false;
                }
            } else {
                return false;
            }

            //Datum wird vom Textfeld "Datum" geholt und als SQL Date
            // umgewandelt (nötig für das Reinschreiben in die
            // Datenbank-Tabelle)
            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
            java.util.Date dt = formatter.parse(getGui().getTxfDatum().getText());
            Date dte = new Date(dt.getTime());
            //Kommas des Textfeldes Aufwand werden durch Punkte ersetzt,
            // damit es zu keinen Fehlern kommt
            String myaufwand = getGui().getTxfAufwand().getText().replace(",", ".");

            String login = getGui().getCobUser().getSelectedItem().toString();
            int loginIndex = login.indexOf("|");
            String userLogin = login.substring(0, loginIndex - 1);
            //neuer Aufwand wird in die Datenbanktabelle Aufwand geschrieben
            ResultSet rs1 = SQLExecuter.executeQuery("SELECT * FROM  Aufwand");
            rs1.moveToInsertRow();
            rs1.updateInt("FID_Proj", 1);
            rs1.updateInt("LVL1ID", wp.getLvl1ID());
            rs1.updateInt("LVL2ID", wp.getLvl2ID());
            rs1.updateInt("LVL3ID", wp.getLvl3ID());
            rs1.updateString("LVLxID", wp.getLvlxID());
            rs1.updateString("FID_Ma", userLogin);
            rs1.updateDate("Datum", dte);
            rs1.updateDouble("Aufwand", Double.parseDouble(myaufwand));
            rs1.updateString("Beschreibung",
                    getGui().getTxfBeschreibung().getText());
            rs1.insertRow();
            rs1.close();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * Gets GUI.
     * @return gui instance.
     */
    public final AddAufwandGui getGui() {
        return gui;
    }

    /**
     * Gets work package ui element.
     * @return work package ui element.
     */
    public final WPShow getWpshow() {
        return wpshow;
    }
}
