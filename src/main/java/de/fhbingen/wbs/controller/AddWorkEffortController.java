package de.fhbingen.wbs.controller;

import java.awt.event.KeyEvent;
import java.awt.geom.Arc2D;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import de.fhbingen.wbs.dbServices.WorkerService;
import de.fhbingen.wbs.dbaccess.DBModelManager;
import de.fhbingen.wbs.dbaccess.data.WorkEffort;
import de.fhbingen.wbs.globals.Workpackage;
import de.fhbingen.wbs.gui.workeffort.AddWorkEffortView;
import de.fhbingen.wbs.translation.LocalizedStrings;
import de.fhbingen.wbs.wpShow.WPShow;
import de.fhbingen.wbs.wpWorker.Worker;

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
public class AddWorkEffortController implements
        AddWorkEffortView.ActionsDelegate {

    /**
     * Variablen für die AddAufwandGui und den SQLExecuter Variablen für den
     * User, WPID's und WPName.
     */
    public AddWorkEffortView gui;
    public WPShow wpshow;
    private Workpackage wp;
    private String myaufwand;
    /**
     * Constructor.
     *
     * @param wpshow WPSHow GUI for reference.
     * @param wp The work package.
     */
    public AddWorkEffortController(WPShow wpshow, Workpackage wp) {
        gui = new AddWorkEffortView(this);
        this.wp = wp;
        this.wpshow = wpshow;
        
        initialize();
        gui.setTitle(LocalizedStrings.getWbs().addAufwandWindowTitle() + wp);
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

        gui.setId(wp.getStringID());
        gui.setName(wp.getName());

        List<Worker> allWorkers = WorkerService.getRealWorkers();
        List<String> workerIDs = wp.getWorkerLogins();
        List<Worker> dummies = new ArrayList<Worker>();

        for (String actualID : workerIDs) {
            dummies.add(new Worker(actualID));
        }

        if (wpshow.getUser().getProjLeiter()) {
            for (Worker actualWorker : allWorkers) {
                if (dummies.contains(actualWorker)) {
                    gui.getWorker().addItem(actualWorker);
                }
            }
        } else {
            for (Worker actualWorker : allWorkers) {
                if (dummies.contains(actualWorker)
                        && wpshow.getUser().getLogin()
                                .equals(actualWorker.getLogin())) {
                    gui.getWorker().addItem(actualWorker);
                    gui.getWorker().setSelectedItem(actualWorker);
                }
            }

            gui.getWorker().setEnabled(false);
        }
    }

    /**
     * prüft ob alle Eingabe ausgefüllt sind
     *
     * @return true/false, je nachdem ob Felder gefüllt oder nicht
     */
    public boolean checkFieldsFilled() {
        if (gui.getDescription().length() > 0
                && gui.getWorkEffort().length() > 0) {
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

            if (!gui.getDate().startsWith(".")) {
                tmpDate = gui.getDate().split("\\.");
                day = Integer.parseInt(tmpDate[0]);
                month = Integer.parseInt(tmpDate[1]);
                if (day < 1 || day > 31 || month < 1 || month > 12) {
                    return false;
                }
            } else {
                return false;
            }

            // Datum wird vom Textfeld "Datum" geholt und als SQL Date
            // umgewandelt (nötig für das Reinschreiben in die
            // Datenbank-Tabelle)
            java.sql.Date dte = null;
            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
            java.util.Date dt = formatter.parse(gui.getDate());
            dte = new java.sql.Date(dt.getTime());
            // Kommas des Textfeldes Aufwand werden durch Punkte ersetzt, damit
            // es zu keinen Fehlern kommt
            myaufwand = gui.getWorkEffort().replace(",", ".");
            String login = gui.getWorker().getSelectedItem().toString();
            int loginIndex = login.indexOf("|");
            String userLogin = login.substring(0, loginIndex - 1);

            // neuer Aufwand wird in die Datenbanktabelle Aufwand geschrieben
            WorkEffort effort = new WorkEffort();
            effort.setFid_wp(wp.getWpId());
            effort.setFid_emp(DBModelManager.getEmployeesModel().getEmployee
                    (userLogin).getId());
            effort.setRec_date(dte);
            effort.setDescription(gui.getDescription());
            if(gui.getBookingTime().getSelectedItem().equals(LocalizedStrings.getGeneralStrings().hours())) {
                myaufwand = String.valueOf(Double.parseDouble(myaufwand)/8);
                gui.setWorkEffort(myaufwand);
                gui.getBookingTime().setSelectedItem(LocalizedStrings.getGeneralStrings().days());
            }
            effort.setEffort(Double.parseDouble(myaufwand));

            return DBModelManager.getWorkEffortModel().addNewWorkEffort(effort);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public void confirmPerformed() {
        if (checkFieldsFilled()) {
            try {
                if (!addAufwand()) {
                    JOptionPane.showMessageDialog(gui,
                            LocalizedStrings.getErrorMessages().checkInputs());
                } else {
                    //berechnet den neuen ETC und schreibt den Wert in das Textfeld ETC der WPShow damit die Werte mit dem neu errechneten ETC berechnet werden
                    double ETC = wpshow.getETCfromGUI() - Double.parseDouble(gui.getWorkEffort());
                    wpshow.updateETCInGUI(ETC);
                    //Methode setChanges der Klasse WPShow aktualisiert die neuen Werte und berechnet alles neu mit dem neuen Aufwand und ETC
                    //JOptionPane.showMessageDialog(addaufwand.gui, "Aufwand wurde erfolgreich eingetragen");
                    wpshow.save();
                    gui.dispose();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(gui,
                    LocalizedStrings.getMessages().fillAllFieldsError(),null,
                    JOptionPane.INFORMATION_MESSAGE);
        }
        
    }

    @Override
    public void cancelPerformed() {
        gui.dispose();
    }

    @Override
    public void workEffortPressPerformed(final KeyEvent e) {
        //Kommas werden direkt durch Punkte ersetzt, damit es zu keine Fehlereingaben kommt
        gui.setWorkEffort(gui.getWorkEffort().replace(",", "."));
    }

    @Override
    public void workEffortReleasePerformed(final KeyEvent e) {
      //Es darf nur ein Punkt gesetzt sein, ansonsten wird der letzte Punkt gelöscht
        boolean vorhanden = false;
        final StringBuffer test = new StringBuffer(gui.getWorkEffort());
        for (int i = 0; i < test.length(); i++) {
            if (test.charAt(i) == '.') {
                if (vorhanden) {
                    test.deleteCharAt(i);
                }
                vorhanden = true;

            }
        }
        gui.setWorkEffort(test.toString());
        char c = e.getKeyChar();
        //es werden nur Ziffern, Kommas und Punkte bei der Eingabe aktzepiert
        if (!(Character.isDigit(e.getKeyChar()) || c == KeyEvent.VK_COMMA
                || c== KeyEvent.VK_PERIOD)) {
            gui.setWorkEffort(gui.getWorkEffort().substring(0,
                    gui.getWorkEffort().length()-1));
        }
        gui.setWorkEffort(gui.getWorkEffort().replace(",", "."));
    }

}
