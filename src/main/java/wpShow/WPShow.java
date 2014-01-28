package wpShow;

/**
 * Studienprojekt:	WBS
 * 
 * Kunde:		Pentasys AG, Jens von Gersdorff
 * Projektmitglieder:	Andre Paffenholz, 
 * 			Peter Lange, 
 * 			Daniel Metzler,
 * 			Samson von Graevenitz
 * 
 * 
 * Studienprojekt:	PSYS WBS 2.0
 * 
 * Kunde:		Pentasys AG, Jens von Gersdorff
 * Projektmitglieder:	Michael Anstatt,
 *			Marc-Eric Baumgärtner,
 *			Jens Eckes,
 *			Sven Seckler,
 *			Lin Yang
 *
 * Allgemeine InfoBox, wird  über Menü->Hilfe->Info aufgerufen <ggf. erweitern>
 *
 * @author Samson von Graevenitz/Peter Lange/<ergänzen>
 * @version 2.0 - 28.06.2012
 */

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import chart.ChartCPIView;
import chart.ChartCompleteView;
import dbServices.WorkerService;
import dbaccess.DBModelManager;
import dbaccess.data.Employee;
import dbaccess.data.WorkEffort;
import wpAddAufwand.AddAufwand;
import wpConflict.Conflict;
import wpOverview.WPOverview;
import wpWorker.Worker;
import functions.CalcOAPBaseline;
import functions.WpManager;
import globals.Controller;
import globals.Workpackage;

public class WPShow {
    private WPOverview over;
    private WPShowGUI gui;
    private boolean newWp;
    private Workpackage wp;
    private boolean saved;
    private Set<String> actualWPWorkers;

    /**
     * Konstruktor
     * 
     * @param over
     *            Workpackage GUI
     * @param selected
     *            Ausgewaehltes AP
     * @param newWp
     *            boolean ob neues AP oder nur Modifizieren
     * @param parent
     *            ParentFrame
     */
    public WPShow(WPOverview over, Workpackage selected, boolean newWp,
            JFrame parent) {

        this.newWp = newWp;
        this.over = over;

        if (newWp) {
            this.wp = new Workpackage();
            this.gui =
                    new WPShowGUI("Neues Arbeitspaket anlegen", this, parent);
            String[] newID = getNextId(selected.getStringID()).split("\\.");
            wp.setLvlIDs(newID);
            wp.setEndDateHope(WpManager.getWorkpackage(wp.getOAPID())
                    .getEndDateHope());
            wp.setStartDateHope(WpManager.getWorkpackage(wp.getOAPID())
                    .getStartDateHope());
            actualWPWorkers = new HashSet<String>();
        } else {
            this.wp = selected;
            this.gui =
                    new WPShowGUI(wp.getStringID() + " | " + wp.getName(),
                            this, parent);
            wp.setwptagessatz(WpManager.calcTagessatz(wp.getWorkerLogins()));
            actualWPWorkers = new HashSet<String>(wp.getWorkerLogins());
        }

        if (wp.isIstOAP()) {
            gui.setOAPView(WPOverview.getUser().getProjLeiter());
        } else {
            gui.setUAPView(WPOverview.getUser().getProjLeiter());
        }
        gui.setNewView(newWp);
        gui.setValues(wp, getUser().getProjLeiter(), getAufwandArray(),
                WorkerService.getRealWorkers(), actualWPWorkers);

        if (newWp) {
            this.saved = false;
        } else {
            this.saved = true;
        }
    }

    public WPShow(WPOverview over, String curID, JFrame parent) {
        this(over, WpManager.getWorkpackage(curID), false, parent);
    }

    /**
     * Liefert eine neue eindeutige ID für ein Arbeitspaket
     * 
     * @param currentID
     *            aktuelle ID des markieren Arbeitspaketes im Baum - bzw. leeres
     *            Arbeitspaket bei Aufruf über das Menü
     * @return die neue -eindeutige- Nummer des neuen Arbeitspaketes
     */
    public static String getNextId(String currentID) {
        Workpackage actualWp = WpManager.getWorkpackage(currentID);
        Integer[] ids = actualWp.getLvlIDs();
        int newValue = actualWp.getlastRelevantIndex();

        String actualID = "";
        while (actualWp != null) {
            ids[newValue] = ids[newValue] + 1;
            actualID = ids[0] + "";
            for (int i = 1; i < ids.length; i++) {
                actualID += "." + ids[i];
            }
            actualWp = WpManager.getWorkpackage(actualID);
        }
        return actualID;
    }

    /**
     * prüft, ob ein AP mit der ID bereits in der Datenbank existiert Wird von
     * addWp() und aus WPReassign.setRekPakete() aufgerufen
     * 
     * @param newId
     *            AP-ID des neu zu erstellenden Arbeitspakets
     * @return true = Paket-ID ist noch frei, false = ID ist bereits in der
     *         Datenbank vorhanden
     */
    public boolean newIdIsOK(String newId) {

        String[] ids = newId.split("\\.");

        int levels = WpManager.getRootAp().getLvlIDs().length;

        if (ids.length != levels) {
            JOptionPane.showMessageDialog(null,
                    "Bitte geben Sie die richtige Anzahl an Ebenen ein \n Ebenenanzahl = "
                            + levels + "!");
            return false;
        }

        if (WpManager.getWorkpackage(newId) != null) {
            JOptionPane
                    .showMessageDialog(
                            null,
                            "Es existiert bereits ein Arbeitspaket mit dieser ID. Bitte geben Sie eine neue ID ein!");
            return false;
        }

        Workpackage actualCheckWp = new Workpackage();
        actualCheckWp.setLvlIDs(ids); // DUMMY!

        while (actualCheckWp != null
                && !actualCheckWp.equals(WpManager.getRootAp())) {
            actualCheckWp = WpManager.getWorkpackage(actualCheckWp.getOAPID());
        }
        if (actualCheckWp == null) {
            JOptionPane.showMessageDialog(null,
                    "Es sind keine Oberpakete zu dieser ID vorhanden!");
            return false;
        } else {
            wp.setLvlIDs(ids);
            return true;
        }
    }

    public Worker getUser() {
        return WPOverview.getUser();
    }

    public boolean isNewWp() {
        return this.newWp;
    }

    protected Workpackage getWorkpackage() {
        return wp;
    }

    public void setGUIChanged() {
        this.saved = false;
    }

    public boolean isSaved() {
        if (saved) {
            return true;
        } else {
            JOptionPane.showMessageDialog(gui,
                    "Bitte erst das Arbeitspaket speichern");
            return false;
        }
    }

    protected void reload() {
        gui.setValues(wp, getUser().getProjLeiter(), getAufwandArray(),
                WorkerService.getRealWorkers(), actualWPWorkers);
    }

    private String[][] getAufwandArray() {
        List<String[]> all = new ArrayList<String[]>();
        List<WorkEffort> efforts =
                DBModelManager.getWorkEffortModel().getWorkEffort(wp.getWpId());
        for (WorkEffort effort : efforts) {
            String[] row = new String[4];
            row[0] =
                    DBModelManager.getEmployeesModel()
                            .getEmployee(effort.getFid_emp()).getLogin();
            row[1] = Controller.DECFORM.format(effort.getEffort());
            row[2] = Controller.DATE_DAY.format(effort.getRec_date());
            row[3] = effort.getDescription();
            all.add(row);
        }
        return all.toArray(new String[1][1]);
    }

    public boolean save() {

        boolean success = true;

        boolean startInaktiv = wp.isIstInaktiv();

        if (gui.getIsOAP() && !wp.isIstOAP() && WpManager.calcAC(wp) > 0) {
            JOptionPane
                    .showMessageDialog(gui,
                            "Das Arbeitspaket muss UAP bleiben, da bereits Aufwände eingetragen wurden");
            success = false;
        }

        if (!gui.getIsOAP() && wp.isIstOAP()
                && WpManager.getUAPs(wp).size() > 0) {
            JOptionPane.showMessageDialog(gui,
                    "Das Arbeitspaket muss OAP bleiben, da UAPs existieren");
            success = false;
        }

        if (success) {
            if (gui.getIsOAP()) {
                success = saveOAP();
            } else {
                success = saveUAP();
            }
        }

        if (success) {
            if (startInaktiv != wp.isIstInaktiv() && wp.isIstOAP()) {
                WpManager.setUapsInaktiv(wp.getStringID(), wp.isIstInaktiv());
            }
            checkConflicts();
            if (wp.isIstOAP()) {
                gui.setOAPView(WPOverview.getUser().getProjLeiter());
            } else {
                gui.setUAPView(WPOverview.getUser().getProjLeiter());
            }
            gui.setNewView(false);

            saved = true;
            newWp = false;
            reload();
            new CalcOAPBaseline(wp, over);
            over.reload();
        }

        saved = success;
        return success;
    }

    private void checkConflicts() {

        if (newWp) {
            WPOverview.throwConflict(new Conflict(new Date(System
                    .currentTimeMillis()), Conflict.NEW_WP, WPOverview
                    .getUser().getId(), wp));
        }

        if (gui.getIsInaktiv() != wp.isIstInaktiv()) {
            WPOverview.throwConflict(new Conflict(new Date(System
                    .currentTimeMillis()), Conflict.CHANGED_ACTIVESTATE,
                    WPOverview.getUser().getId(), wp));
        }

        try {
            if (!gui.getBAC().equals(wp.getBac())) {
                WPOverview.throwConflict(new Conflict(new Date(System
                        .currentTimeMillis()), Conflict.CHANGED_BAC, WPOverview
                        .getUser().getId(), wp));
            }
            if (gui.getStartHope() != null
                    && !gui.getStartHope().equals(wp.getStartDateHope())) {
                WPOverview.throwConflict(new Conflict(new Date(System
                        .currentTimeMillis()), Conflict.CHANGED_WISHDATES,
                        WPOverview.getUser().getId(), wp));
            }
        } catch (ParseException e) {/* Wurde bereits ausgeschlossen */
        }

        List<String> wpWorkers = wp.getWorkerLogins();
        String[] guiWorkers = gui.getWorkers();
        Collections.sort(wpWorkers);
        Arrays.sort(guiWorkers);

        for (int i = 0; i < wpWorkers.size(); i++) {
            if (guiWorkers.length != wpWorkers.size()
                    || wpWorkers.get(i).equals(guiWorkers[i])) {
                WPOverview.throwConflict(new Conflict(new Date(System
                        .currentTimeMillis()), Conflict.CHANGED_RESOURCES,
                        WPOverview.getUser().getId(), wp));
            }
        }
    }

    private boolean saveOAP() {

        boolean save = true;
        Date endHope = null;
        Date startHope = null;

        String[] newLvlIDs = new String[1];
        if (newWp) {
            if (newIdIsOK(gui.getNr())) {
                newLvlIDs = gui.getNr().split(".");
            } else {
                JOptionPane.showMessageDialog(gui, "Falsche ID eingegeben");
                save = false;
            }
        }

        String name = gui.getWpName();
        if (name.equals("")) {
            JOptionPane.showMessageDialog(null, "Name darf nicht leer sein");
            save = false;
        }

        if (getWorkpackage().equals(WpManager.getRootAp())) {
            if (!gui.getIsOAP()) {
                JOptionPane.showMessageDialog(null, "Projekt muss OAP sein");
                save = false;
            }
            if (gui.getIsInaktiv()) {
                JOptionPane.showMessageDialog(null, "Projekt muss aktiv sein");
                save = false;
            }
        }

        try {
            endHope = gui.getEndHope();
            startHope = gui.getStartHope();
            if (wp.equals(WpManager.getRootAp()) && startHope == null) {
                JOptionPane.showMessageDialog(gui,
                        "Das Projekt braucht ein Startdatum");
                save = false;
            }
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(gui,
                    "Datum ist nicht im korrekten Format dd.mm.yyyy");
            save = false;
        }

        if (save) {
            if (endHope != null && startHope != null) {
                if (endHope.before(startHope)) {
                    JOptionPane
                            .showMessageDialog(gui,
                                    "Gewünschtes Enddatum liegt vor dem gewünschten Startdatum");
                    save = false;
                }
            }
        }
        String leiter = "";
        int leiterId = -1;
        if (gui.getLeiter() != null) {
            leiter = gui.getLeiter().getLogin();
            leiterId = gui.getLeiter().getId();
        }
        if (leiter.equals("")) {
            JOptionPane.showMessageDialog(gui, "Bitte Leiter auswählen");
            save = false;
        }

        if (save) {
            if (newWp && gui.getIsOAP()) {
                Integer[] levelIDcache = wp.getLvlIDs();
                wp.setLvlIDs(newLvlIDs);
                if (WpManager.getRootAp().getLvlIDs().length == wp
                        .getlastRelevantIndex()) {
                    JOptionPane
                            .showMessageDialog(gui,
                                    "Auf unterster Ebene koennen keine OAP angelegt werden");
                    wp.setLvlIDs(levelIDcache);
                    save = false;
                }
            }
        }

        if (save) {
            if (endHope != null && !endHope.equals(wp.getEndDateHope())) {
                if (JOptionPane
                        .showConfirmDialog(
                                gui,
                                "Wollen Sie das geänderte Enddatum in die UAP übernehmen?",
                                "Geändertes Datum", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    setUAPEndHope(wp, endHope);

                }
            }
            if (startHope != null && !startHope.equals(wp.getStartDateHope())) {
                if (JOptionPane
                        .showConfirmDialog(
                                gui,
                                "Wollen Sie das geänderte Startdatum in die UAP übernehmen?",
                                "Geändertes Datum", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    setUAPStartHope(wp, startHope);
                }
            }

            wp.setStartDateHope(startHope);
            wp.setEndDateHope(endHope);
            wp.setName(name);
            wp.setFid_Leiter(leiterId);
            wp.setBeschreibung(gui.getDescription());
            wp.setIstInaktiv(gui.getIsInaktiv());

            if (gui.getIsOAP() != wp.isIstOAP()) { // WAR VORHER KEIN OAP
                wp.setBac(0.);
                wp.setEtc(0.);
                wp.setAc(0.);
                wp.setEv(0.);
                wp.setbac_kosten(0.);
                wp.setAc_kosten(0.);
                wp.setEv(0.);
                wp.setEac(0.);
                wp.setEtc_kosten(0.);
                wp.setwptagessatz(0.);
                wp.setIstOAP(true);
                for (Employee actualWorker : wp.getWorkers()) {
                    wp.removeWorker(actualWorker);
                }
            }

            if (newWp) {
                WpManager.insertAP(wp);
            } else {
                WpManager.updateAP(wp);
            }
            return true;
        } else {
            return false;
        }

    }

    private boolean saveUAP() {
        boolean save = true;

        String name, description, leiterLogin;
        int fid_Leiter = -1;
        boolean istOAP = false, istInaktiv = gui.getIsInaktiv();
        Date startDateHope = null, endDateHope = null;
        Double bac = 0.0;
        double etc = 0, ac = 0, ev, bacKosten = 0, acKosten = 0, eac, etcKosten =
                0, wptagessatz;

        String[] newLvlIDs = new String[1];
        if (newWp) {
            if (newIdIsOK(gui.getNr())) {
                newLvlIDs = gui.getNr().split(".");
            } else {
                JOptionPane.showMessageDialog(gui, "Falsche ID eingegeben");
                save = false;
            }
        }

        name = gui.getWpName();
        if (name == null || name.equals("")) {
            save = false;
            JOptionPane.showMessageDialog(gui, "Bitte Name angeben");
        }

        description = gui.getDescription();
        if (gui.getLeiter() == null) {
            save = false;
            leiterLogin = "";
        } else {
            leiterLogin = gui.getLeiter().getLogin();
            fid_Leiter = gui.getLeiter().getId();

        }
        if (leiterLogin.equals("")) {
            save = false;
            JOptionPane.showMessageDialog(gui, "Bitte Leiter angeben");
        }

        actualWPWorkers.clear();
        for (String actualWorkerID : gui.getWorkers()) {
            if (!actualWorkerID.equals("")) {
                actualWPWorkers.add(actualWorkerID);
            }
        }
        if (!actualWPWorkers.contains(leiterLogin)) {
            actualWPWorkers.add(leiterLogin);
        }
        try {
            startDateHope = gui.getStartHope();
            endDateHope = gui.getEndHope();
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(gui,
                    "Datum ist nicht im korrekten Format dd.mm.yyyy");
            save = false;
        }

        wptagessatz =
                WpManager.calcTagessatz(new ArrayList<String>(actualWPWorkers));

        try {
            bac = gui.getBAC();
            if ((bac == null || bac == 0) && save) {

                String bacString = null;
                while (bacString == null) {
                    bacString =
                            JOptionPane.showInputDialog(gui,
                                    "Bitte BAC eingeben");
                }
                bac = Double.parseDouble(bacString.replace(",", "."));

            }
            if (bac < 0) {
                throw new ParseException("to small to parse", 0);
            }
            bacKosten = bac * wptagessatz;
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(gui, "BAC muss eine Zahl > 0 sein");
            save = false;
        }

        if (newWp || gui.getIsOAP() != wp.isIstOAP()) { // Wenn dieses AP vorher
                                                        // OAP war
            etc = bac;
            etcKosten = etc * wptagessatz;
        } else {
            try {
                etc = gui.getETC();
                if (etc < 0) {
                    throw new ParseException("to small to parse", 0);
                }
                etcKosten = etc * wptagessatz;
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(gui,
                        "ETC muss eine Zahl > 0 sein");
                save = false;
            }

        }

        ac = WpManager.calcAC(wp);
        ev =
                WpManager.calcEV(bacKosten,
                        WpManager.calcPercentComplete(bac, etc, ac));
        eac = WpManager.calcEAC(bacKosten, acKosten, etcKosten);

        if (save) {

            for (Employee actualWorker : wp.getWorkers()) {
                wp.removeWorker(actualWorker);
            }
            for (String actualWorker : actualWPWorkers) {
                wp.addWorker(DBModelManager.getEmployeesModel().getEmployee(
                        actualWorker));
            }
            acKosten = WpManager.calcACKosten(wp);

            double cpi = WpManager.calcCPI(acKosten, etcKosten, bacKosten);

            wp.setName(name);
            wp.setIstOAP(istOAP);
            wp.setIstInaktiv(istInaktiv);
            wp.setBeschreibung(description);
            wp.setFid_Leiter(fid_Leiter);
            wp.setStartDateHope(startDateHope);
            wp.setEndDateHope(endDateHope);
            wp.setBac(bac);
            wp.setEtc(etc);
            wp.setAc(ac);
            wp.setEv(ev);
            wp.setbac_kosten(bacKosten);
            wp.setAc_kosten(acKosten);
            wp.setEv(ev);
            wp.setEac(eac);
            wp.setEtc_kosten(etcKosten);
            wp.setwptagessatz(wptagessatz);
            wp.setcpi(cpi);

            if (newWp) {
                wp.setLvlIDs(newLvlIDs);
                WpManager.insertAP(wp);
            } else {
                WpManager.updateAP(wp);
            }
            return true;
        }

        return false;
    }

    protected KeyListener getChangeListenerTextfield() {
        return new KeyListener() {
            @Override
            public void keyPressed(KeyEvent arg0) {
                setGUIChanged();
            }

            @Override
            public void keyReleased(KeyEvent arg0) {
                setGUIChanged();
            }

            @Override
            public void keyTyped(KeyEvent arg0) {
                setGUIChanged();
            }
        };
    }

    protected ItemListener getChangeItemListener() {
        return new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent arg0) {
                setGUIChanged();
            }
        };
    }

    protected ActionListener getBtnOKListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (save()) {
                    gui.dispose();
                }
            }
        };
    }

    protected ActionListener getBtnAddAncestorListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isSaved()) {
                    new SequencerGUI(getWorkpackage(),
                            SequencerGUI.MODE_ADD_ANCHESTOR, gui);
                }
            }
        };
    }

    protected ActionListener getBtnAddFollowerListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isSaved()) {
                    new SequencerGUI(getWorkpackage(),
                            SequencerGUI.MODE_ADD_FOLLOWER, gui);
                }
            }
        };
    }

    protected ActionListener getBtnEditAncestorListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isSaved()) {
                    new SequencerGUI(getWorkpackage(),
                            SequencerGUI.MODE_DELETE_ANCHESTOR, gui);
                }

            }
        };
    }

    protected ActionListener getBtnEditFollowerListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isSaved()) {
                    new SequencerGUI(getWorkpackage(),
                            SequencerGUI.MODE_DELETE_FOLLOWER, gui);
                }

            }
        };
    }

    protected ActionListener getBtnAddAufwandListener() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (isSaved()) {
                    new AddAufwand(getThis(), getWorkpackage());
                }
            }
        };
    }

    protected ActionListener getBtnSaveListener() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                save();
            }
        };
    }

    protected ActionListener getBtnCancelListener() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gui.dispose();
            }
        };
    }

    protected ActionListener getBtnAddWorkerListener(
            final JComboBox<Worker> cobAddWorker) {
        return new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (((Worker) cobAddWorker.getSelectedItem()) != null) {
                    actualWPWorkers.add(((Worker) cobAddWorker
                            .getSelectedItem()).getLogin());
                }
                gui.fillWorkerCombos(actualWPWorkers);
            }
        };
    }

    protected ActionListener getBtnRemoveWorkerListener(
            final JComboBox<Worker> cobRemoveWorker) {
        return new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (((Worker) cobRemoveWorker.getSelectedItem()) != null) {
                    actualWPWorkers.remove(((Worker) cobRemoveWorker
                            .getSelectedItem()).getLogin());
                }
                gui.fillWorkerCombos(actualWPWorkers);
            }
        };
    }

    protected ActionListener getBtnBeendenListener() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gui.dispose();
                Controller.leaveDB();
            }
        };
    }

    protected FocusListener getBACETCListener() {
        return new FocusAdapter() {

            public void focusGained(FocusEvent e) {
                JTextField txfTmp = (JTextField) e.getSource();
                if (txfTmp != null) {
                    txfTmp.setSelectionStart(0);
                    txfTmp.setSelectionEnd(txfTmp.getText().length());
                }
            }

            public void focusLost(FocusEvent e) {
                JTextField txfTmp = (JTextField) e.getSource();
                if (txfTmp != null) {
                    txfTmp.setText(txfTmp.getText().replace(",", "."));
                }
            }

        };
    }

    protected ActionListener getBtnCPIListener() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ChartCPIView(wp, gui);
            }
        };

    }

    protected ActionListener getBtnSPIListener() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ChartCompleteView(wp, gui);
            }
        };
    }

    protected ActionListener getBtnAllPVListener() {
        return new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                new PVTableGUI(wp);
            }
        };
    }

    private WPShow getThis() {
        return this;
    }

    public Component getMainFrame() {
        return gui;
    }

    public double getETCfromGUI() {
        try {
            return gui.getETC();
        } catch (ParseException e) {
            return 0;
        }
    }

    public void updateETCInGUI(double etc) {
        gui.setETC(etc);
        save();
    }

    private void setUAPStartHope(Workpackage oap, Date date) {
        oap.setStartDateHope(date);
        WpManager.updateAP(oap);
        for (Workpackage actualUAP : WpManager.getUAPs(oap)) {
            setUAPStartHope(actualUAP, date);
        }
    }

    private void setUAPEndHope(Workpackage oap, Date date) {
        oap.setEndDateHope(date);
        WpManager.updateAP(oap);
        for (Workpackage actualUAP : WpManager.getUAPs(oap)) {
            setUAPEndHope(actualUAP, date);
        }
    }
}