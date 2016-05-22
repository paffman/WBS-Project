/*
 * The WBS-Tool is a project management tool combining the Work Breakdown
 * Structure and Earned Value Analysis Copyright (C) 2013 FH-Bingen This
 * program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your
 * option) any later version. This program is distributed in the hope that
 * it will be useful, but WITHOUT ANY WARRANTY;; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with this program. If not,
 * see <http://www.gnu.org/licenses/>.
 */

package de.fhbingen.wbs.wpShow;

import de.fhbingen.wbs.controller.TestCaseController;
import de.fhbingen.wbs.dbaccess.data.TestCase;
import de.fhbingen.wbs.testcases.TestcaseExecutionShow;
import de.fhbingen.wbs.testcases.TestcaseShow;
import de.fhbingen.wbs.translation.General;
import de.fhbingen.wbs.wpConflict.ConflictCompat;
import java.awt.Component;
import java.awt.event.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.*;

import de.fhbingen.wbs.chart.ChartCPIView;
import de.fhbingen.wbs.chart.ChartCompleteView;
import de.fhbingen.wbs.controller.AddWorkEffortController;
import de.fhbingen.wbs.dbServices.WorkerService;
import de.fhbingen.wbs.dbaccess.DBModelManager;
import de.fhbingen.wbs.dbaccess.data.Employee;
import de.fhbingen.wbs.dbaccess.data.WorkEffort;
import de.fhbingen.wbs.functions.CalcOAPBaseline;
import de.fhbingen.wbs.functions.WpManager;
import de.fhbingen.wbs.globals.Controller;
import de.fhbingen.wbs.globals.Workpackage;
import de.fhbingen.wbs.translation.LocalizedStrings;
import de.fhbingen.wbs.translation.Messages;
import de.fhbingen.wbs.translation.Wbs;
import de.fhbingen.wbs.wpOverview.WPOverview;
import de.fhbingen.wbs.wpWorker.Worker;

/** The GUI to show a work package. */
public class WPShow {

    private final Wbs wbsStrings;
    private final Messages messageStrings;
    private final General generalStrings;

    /** The functionality of the WPOverview GUI. */
    private WPOverview over;

    /** The GUI of this class. */
    private WPShowGUI gui;

    /** Check if it is a new work package or an old work package is edit. */
    private boolean newWp;

    /** The work package. */
    private Workpackage wp;

    /** Check if the changes are saved. */
    private boolean saved;

    /** A set with all employees which work on the work package. */
    private Set<String> actualWPWorkers;

    /**
     * Controler for the testcase
     */
    private TestCaseController testCaseController;

    /**
     * Parent Frame
     */
    private final JFrame parent;

    private int actualTestCaseIndex;

    /**
     * Constructor.
     * @param over
     *            The work package GUI.
     * @param selected
     *            The selected work package.
     * @param newWp
     *            True: The work package is a new work package. False: It
     *            is an old work package which is change.
     * @param parent
     *            The parent frame.
     */
    public WPShow(final WPOverview over, final Workpackage selected,
        final boolean newWp, final JFrame parent) {
        wbsStrings = LocalizedStrings.getWbs();
        messageStrings = LocalizedStrings.getMessages();
        generalStrings = LocalizedStrings.getGeneralStrings();

        this.newWp = newWp;
        this.over = over;
        this.parent = parent;

        if (newWp) {
            this.wp = new Workpackage();
            this.gui = new WPShowGUI(
                wbsStrings.addNewWorkPackageWindowTitle(), this, parent);
            String[] newID = getNextId(selected.getStringID()).split("\\.");
            wp.setLvlIDs(newID);
            wp.setEndDateHope(WpManager.getWorkpackage(wp.getOAPID())
                .getEndDateHope());
            wp.setStartDateHope(WpManager.getWorkpackage(wp.getOAPID())
                .getStartDateHope());
            actualWPWorkers = new HashSet<String>();
        } else {
            this.wp = selected;
            this.gui = new WPShowGUI(wp.getStringID() + " | "
                + wp.getName(), this, parent);
            wp.setwptagessatz(WpManager.calcTagessatz(wp.getWorkerLogins()));
            actualWPWorkers = new HashSet<String>(wp.getWorkerLogins());
        }

        if (wp.isIstOAP()) {
            gui.setOAPView(WPOverview.getUser().getProjLeiter());
        } else {
            gui.setUAPView(WPOverview.getUser().getProjLeiter());
        }

        this.testCaseController = new TestCaseController(this.wp);

        gui.setNewView(newWp);
        gui.setValues(wp, getUser().getProjLeiter(), getAufwandArray(),
            WorkerService.getRealWorkers(), actualWPWorkers, testCaseController);

        if (newWp) {
            this.saved = false;
        } else {
            this.saved = true;
        }
    }

    /**
     * Constructor.
     * @param over
     *            The work package GUI.
     * @param curID
     *            The current id from the marked work package in the tree.
     * @param parent
     *            The parent frame.
     */
    public WPShow(final WPOverview over, final String curID,
        final JFrame parent) {
        this(over, WpManager.getWorkpackage(curID), false, parent);
    }

    /**
     * Returns a definitely id for the work package.
     * @param currentID
     *            The current id from the marked work package in the tree.
     * @return The definitely id of the work package.
     */
    public static String getNextId(final String currentID) {
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
     * Checks if the id is okay or the id is already exist. This method is
     * called by the method addWp() and WPReassign.setRekPakete().
     * @param newId
     *            The new id of the work package.
     * @return True: The id is okay. False: The id already exists.
     */
    public final boolean newIdIsOK(final String newId) {

        String[] ids = newId.split("\\.");

        int levels = WpManager.getRootAp().getLvlIDs().length;

        if (ids.length != levels) {
            JOptionPane.showMessageDialog(null,
                messageStrings.workPackageWrongLevel(levels));
            return false;
        }

        if (WpManager.getWorkpackage(newId) != null) {
            JOptionPane.showMessageDialog(null,
                messageStrings.workPackageIdAlreadyExists());
            return false;
        }

        Workpackage actualCheckWp = new Workpackage();
        actualCheckWp.setLvlIDs(ids); // DUMMY!

        while (actualCheckWp != null
            && !actualCheckWp.equals(WpManager.getRootAp())) {
            actualCheckWp = WpManager.getWorkpackage(actualCheckWp
                .getOAPID());
        }
        if (actualCheckWp == null) {
            JOptionPane
                .showMessageDialog(null, messageStrings
                    .workPackageNoTopLevelWorkPackagesForThisId());
            return false;
        } else {
            wp.setLvlIDs(ids);
            return true;
        }
    }

    /**
     * Gets the user.
     * @return The user.
     */
    public final Worker getUser() {
        return WPOverview.getUser();
    }

    /**
     * Checks if the work package is a new work package.
     * @return True: It's a new one. False: It's not new.
     */
    public final boolean isNewWp() {
        return this.newWp;
    }

    /**
     * Gets the work package.
     * @return The work package.
     */
    protected final Workpackage getWorkpackage() {
        return wp;
    }

    /** Doesn't saves the changes. */
    public final void setGUIChanged() {
        this.saved = false;
    }

    /**
     * Returns if the changes should be saved.
     * @return True: If the changes are saved. False: If the changes aren't
     *         saved.
     */
    public final boolean isSaved() {
        if (saved) {
            return true;
        } else {
            JOptionPane.showMessageDialog(gui,
                messageStrings.workPackagePleaseSaveToContinue());
            return false;
        }
    }

    /** Reloads the GUI. */
    protected final void reload() {
        gui.setValues(wp, getUser().getProjLeiter(), getAufwandArray(),
            WorkerService.getRealWorkers(), actualWPWorkers, testCaseController);
    }

    /**
     * Returns an String array with the work efforts.
     * @return An array with the work efforts.
     */
    private String[][] getAufwandArray() {
        List<String[]> all = new ArrayList<String[]>();
        List<WorkEffort> efforts = DBModelManager.getWorkEffortModel()
            .getWorkEffort(wp.getWpId());
        for (WorkEffort effort : efforts) {
            String[] row = new String[4];
            row[0] = DBModelManager.getEmployeesModel()
                .getEmployee(effort.getFid_emp()).getLogin();
            row[1] = Controller.DECFORM.format(effort.getEffort());
            row[2] = Controller.DATE_DAY.format(effort.getRec_date());
            row[3] = effort.getDescription();
            all.add(row);
        }
        return all.toArray(new String[1][1]);
    }

    /**
     * Saves the changes.
     * @return True: If the changes are saved successful. False: Else.
     */
    public final boolean save() {

        boolean success = true;

        boolean startInaktiv = wp.isIstInaktiv();

        if (gui.getIsOAP() && !wp.isIstOAP() && WpManager.calcAC(wp) > 0) {
            JOptionPane.showMessageDialog(gui, messageStrings
                .workPackageCanNotBeTopLevelBecauseOfWorkEfforts());
            success = false;
        }

        if (!gui.getIsOAP() && wp.isIstOAP()
            && WpManager.getUAPs(wp).size() > 0) {
            JOptionPane.showMessageDialog(gui, messageStrings
                .workPackageCanNotBeSubWorkPackageBecauseItHasChildren());
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
                WpManager.setUapsInaktiv(wp.getStringID(),
                    wp.isIstInaktiv());
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

    /** Checks if conflicts exist on the work package. */
    private void checkConflicts() {

        if (newWp) {
            WPOverview.throwConflict(new ConflictCompat(new Date(System
                .currentTimeMillis()), ConflictCompat.NEW_WP, WPOverview
                .getUser().getId(), wp));
        }

        if (gui.getIsInaktiv() != wp.isIstInaktiv()) {
            WPOverview.throwConflict(new ConflictCompat(new Date(System
                .currentTimeMillis()), ConflictCompat.CHANGED_ACTIVESTATE,
                WPOverview.getUser().getId(), wp));
        }

        try {
            if (!gui.getBAC().equals(wp.getBac())) {
                WPOverview.throwConflict(new ConflictCompat(new Date(System
                    .currentTimeMillis()), ConflictCompat.CHANGED_BAC, WPOverview
                    .getUser().getId(), wp));
            }
            if (gui.getStartHope() != null
                && !gui.getStartHope().equals(wp.getStartDateHope())) {
                WPOverview.throwConflict(new ConflictCompat(new Date(System
                    .currentTimeMillis()), ConflictCompat.CHANGED_WISHDATES,
                    WPOverview.getUser().getId(), wp));
            }
        } catch (ParseException e) {
            /* Can not be reached. */
            e.printStackTrace();
        }

        List<String> wpWorkers = wp.getWorkerLogins();
        String[] guiWorkers = gui.getWorkers();
        Collections.sort(wpWorkers);
        Arrays.sort(guiWorkers);

        for (int i = 0; i < wpWorkers.size(); i++) {
            if (guiWorkers.length != wpWorkers.size()
                || wpWorkers.get(i).equals(guiWorkers[i])) {
                WPOverview.throwConflict(new ConflictCompat(new Date(System
                    .currentTimeMillis()), ConflictCompat.CHANGED_RESOURCES,
                    WPOverview.getUser().getId(), wp));
            }
        }
    }

    /**
     * Saves the upper work package.
     * @return True: If the save was successful. False: Else.
     */
    private boolean saveOAP() {

        boolean save = true;
        Date endHope = null;
        Date startHope = null;

        String[] newLvlIDs = new String[1];
        if (newWp) {
            if (newIdIsOK(gui.getNr())) {
                newLvlIDs = gui.getNr().split(".");
            } else {
                JOptionPane.showMessageDialog(gui,
                    messageStrings.workPackageWrongId());
                save = false;
            }
        }

        String name = gui.getWpName();
        if (name.equals("")) {
            JOptionPane.showMessageDialog(null,
                messageStrings.workPackageNeedsName());
            save = false;
        }

        if (getWorkpackage().equals(WpManager.getRootAp())) {
            if (!gui.getIsOAP()) {
                JOptionPane.showMessageDialog(null,
                    messageStrings.workPackageRootHasToBeTopLevel());
                save = false;
            }
            if (gui.getIsInaktiv()) {
                JOptionPane.showMessageDialog(null,
                    messageStrings.workPackageRootHasToBeActive());
                save = false;
            }
        }

        try {
            endHope = gui.getEndHope();
            startHope = gui.getStartHope();
            if (wp.equals(WpManager.getRootAp()) && startHope == null) {
                JOptionPane.showMessageDialog(gui,
                    messageStrings.workPackageRootNeedsDate());
                save = false;
            }
        } catch (ParseException e) {
            JOptionPane
                .showMessageDialog(gui, messageStrings.dateInvalid());
            save = false;
        }

        if (save) {
            if (endHope != null && startHope != null) {
                if (endHope.before(startHope)) {
                    JOptionPane.showMessageDialog(gui,
                        messageStrings.endDateCanNotBeBeforeStartDate());
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
            JOptionPane.showMessageDialog(gui,
                messageStrings.workPackageSelectManager());
            save = false;
        }

        if (save) {
            if (newWp && gui.getIsOAP()) {
                Integer[] levelIDcache = wp.getLvlIDs();
                wp.setLvlIDs(newLvlIDs);
                if (WpManager.getRootAp().getLvlIDs().length == wp
                    .getlastRelevantIndex()) {
                    JOptionPane.showMessageDialog(gui,
                        messageStrings.workPackageOutOfLevels());
                    wp.setLvlIDs(levelIDcache);
                    save = false;
                }
            }
        }

        if (save) {
            if (endHope != null && !endHope.equals(wp.getEndDateHope())) {
                if (JOptionPane.showConfirmDialog(gui, messageStrings
                    .workPackageApplyDateOnSubWorkPackages(LocalizedStrings
                        .getProject().endDate().toLowerCase()), wbsStrings
                    .dateChangedWindowTitle(), JOptionPane.YES_NO_OPTION)
                    == JOptionPane.YES_OPTION) {
                    setUAPEndHope(wp, endHope);

                }
            }
            if (startHope != null
                && !startHope.equals(wp.getStartDateHope())) {
                if (JOptionPane.showConfirmDialog(gui, messageStrings
                    .workPackageApplyDateOnSubWorkPackages(LocalizedStrings
                        .getProject().startDate().toLowerCase()),
                    wbsStrings.dateChangedWindowTitle(),
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    setUAPStartHope(wp, startHope);
                }
            }

            wp.setStartDateHope(startHope);
            wp.setEndDateHope(endHope);
            wp.setName(name);
            wp.setFid_Leiter(leiterId);
            wp.setBeschreibung(gui.getDescription());
            wp.setIstInaktiv(gui.getIsInaktiv());

            if (gui.getIsOAP() != wp.isIstOAP()) { // Wasn't a upper work
                                                   // package before.
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

    /**
     * Saves the under work package.
     * @return True: If successful. False: If not successful.
     */
    private boolean saveUAP() {
        boolean save = true;

        String name, description, leiterLogin;
        int fid_Leiter = -1;
        boolean istOAP = false, istInaktiv = gui.getIsInaktiv();
        Date startDateHope = null, endDateHope = null;
        Double bac = 0.0;
        double etc = 0, ac = 0, ev, bacKosten = 0;
        double acKosten = 0, eac, etcKosten = 0, wptagessatz;

        String[] newLvlIDs = new String[1];
        if (newWp) {
            if (newIdIsOK(gui.getNr())) {
                newLvlIDs = gui.getNr().split(".");
            } else {
                JOptionPane.showMessageDialog(gui,
                    messageStrings.workPackageWrongId());
                save = false;
            }
        }

        name = gui.getWpName();
        if (name == null || name.equals("")) {
            save = false;
            JOptionPane.showMessageDialog(gui,
                messageStrings.workPackageNeedsName());
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
            JOptionPane.showMessageDialog(gui,
                messageStrings.workPackageSelectManager());
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
            JOptionPane
                .showMessageDialog(gui, messageStrings.dateInvalid());
            save = false;
        }

        wptagessatz = WpManager.calcTagessatz(new ArrayList<String>(
            actualWPWorkers));

        try {
            bac = gui.getBAC();
            if ((bac == null || bac == 0) && save) {

                String bacString = null;
                while (bacString == null) {
                    bacString = JOptionPane.showInputDialog(gui,
                        messageStrings.insertBac());
                }
                bac = Double.parseDouble(bacString.replace(",", "."));

            }
            if (bac < 0) {
                throw new ParseException("to small to parse", 0);
            }
            bacKosten = bac * wptagessatz;
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(gui,
                messageStrings.numberMustBePositive(wbsStrings.bac()));
            save = false;
        }

        if (newWp || gui.getIsOAP() != wp.isIstOAP()) { // If this work
                                                        // package wasn't a
                                                        // upper work
                                                        // package before
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
                    messageStrings.numberMustBePositive(wbsStrings.etc()));
                save = false;
            }

        }

        ac = WpManager.calcAC(wp);
        ev = WpManager.calcEV(bacKosten,
            WpManager.calcPercentComplete(bac, etc, ac));

        if (save) {

            for (Employee actualWorker : wp.getWorkers()) {
                wp.removeWorker(actualWorker);
            }
            acKosten = WpManager.calcACKosten(wp);

            double cpi = WpManager.calcCPI(acKosten, etcKosten, bacKosten);
            eac = WpManager.calcEAC(bacKosten, acKosten, etcKosten);

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

            for (String actualWorker : actualWPWorkers) {
                wp.addWorker(DBModelManager.getEmployeesModel()
                    .getEmployee(actualWorker));
            }
            return true;
        }

        return false;
    }

    /**
     * Returns the key listener of the text field.
     * @return The key listener of the text field.
     */
    protected final KeyListener getChangeListenerTextfield() {
        return new KeyListener() {
            @Override
            public void keyPressed(final KeyEvent arg0) {
                setGUIChanged();
            }

            @Override
            public void keyReleased(final KeyEvent arg0) {
                setGUIChanged();
            }

            @Override
            public void keyTyped(final KeyEvent arg0) {
                setGUIChanged();
            }
        };
    }

    /**
     * Returns the item listener.
     * @return the item listener.
     */
    protected final ItemListener getChangeItemListener() {
        return new ItemListener() {
            @Override
            public void itemStateChanged(final ItemEvent arg0) {
                setGUIChanged();
            }
        };
    }

    /**
     * Returns the action listener for the OK button.
     * @return The action listener of the OK button.
     */
    protected final ActionListener getBtnOKListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                if (save()) {
                    gui.dispose();
                }
            }
        };
    }

    /**
     * Returns the action listener for the ancestor button.
     * @return The action listener.
     */
    protected final ActionListener getBtnAddAncestorListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                if (isSaved()) {
                    new SequencerGUI(getWorkpackage(),
                        SequencerGUI.MODE_ADD_ANCHESTOR, gui);
                }
            }
        };
    }

    /**
     * Returns the action listener for the button to add a follower.
     * @return The action listener.
     */
    protected final ActionListener getBtnAddFollowerListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                if (isSaved()) {
                    new SequencerGUI(getWorkpackage(),
                        SequencerGUI.MODE_ADD_FOLLOWER, gui);
                }
            }
        };
    }

    /**
     * Returns the action listener for the button to edit a ancestor.
     * @return The action listener.
     */
    protected final ActionListener getBtnEditAncestorListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                if (isSaved()) {
                    new SequencerGUI(getWorkpackage(),
                        SequencerGUI.MODE_DELETE_ANCHESTOR, gui);
                }

            }
        };
    }

    /**
     * Returns the action listener for the button to edit a follower.
     * @return The action listener.
     */
    protected final ActionListener getBtnEditFollowerListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                if (isSaved()) {
                    new SequencerGUI(getWorkpackage(),
                        SequencerGUI.MODE_DELETE_FOLLOWER, gui);
                }

            }
        };
    }

    /**
     * Returns the action listener for the button to add a work effort.
     * @return The action listener.
     */
    protected final ActionListener getBtnAddAufwandListener() {
        return new ActionListener() {
            public void actionPerformed(final ActionEvent arg0) {
                if (isSaved()) {
                    new AddWorkEffortController(getThis(), getWorkpackage());
                }
            }
        };
    }

    /**
     * Returns the action listener for the button to save the changes.
     * @return The action listener.
     */
    protected final ActionListener getBtnSaveListener() {
        return new ActionListener() {
            public void actionPerformed(final ActionEvent arg0) {
                save();
            }
        };
    }

    /**
     * Returns the action listener for the button to cancel the changes.
     * @return The action listener.
     */
    protected final ActionListener getBtnCancelListener() {
        return new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                gui.dispose();
            }
        };
    }

    /**
     * Returns the action listener for the button to add a worker.
     * @param cobAddWorker
     *            The combo box which contains the workers.
     * @return The action listener.
     */
    protected final ActionListener getBtnAddWorkerListener(
        final JComboBox<Worker> cobAddWorker) {
        return new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                if (((Worker) cobAddWorker.getSelectedItem()) != null) {
                    actualWPWorkers.add(((Worker) cobAddWorker
                        .getSelectedItem()).getLogin());
                }
                gui.fillWorkerCombos(actualWPWorkers);
            }
        };
    }

    /**
     * Returns the action listener for the button to remove a worker.
     * @param cobRemoveWorker
     *            The combo box which contains the workers.
     * @return The action listener.
     */
    protected final ActionListener getBtnRemoveWorkerListener(
        final JComboBox<Worker> cobRemoveWorker) {
        return new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                if (((Worker) cobRemoveWorker.getSelectedItem()) != null) {
                    actualWPWorkers.remove(((Worker) cobRemoveWorker
                        .getSelectedItem()).getLogin());
                }
                gui.fillWorkerCombos(actualWPWorkers);
            }
        };
    }

    /**
     * Returns the action listener for the button to close the window.
     * @return The action listener.
     */
    protected final ActionListener getBtnBeendenListener() {
        return new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                gui.dispose();
                Controller.leaveDB();
            }
        };
    }

    /**
     * Returns the bac and etc listener.
     * @return The focus listener.
     */
    protected final FocusListener getBACETCListener() {
        return new FocusAdapter() {

            public void focusGained(final FocusEvent e) {
                JTextField txfTmp = (JTextField) e.getSource();
                if (txfTmp != null) {
                    txfTmp.setSelectionStart(0);
                    txfTmp.setSelectionEnd(txfTmp.getText().length());
                }
            }

            public void focusLost(final FocusEvent e) {
                JTextField txfTmp = (JTextField) e.getSource();
                if (txfTmp != null) {
                    txfTmp.setText(txfTmp.getText().replace(",", "."));
                }
            }

        };
    }

    /**
     * Returns the action listener for the button for the cpi.
     * @return The action listener.
     */
    protected final ActionListener getBtnCPIListener() {
        return new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                new ChartCPIView(wp, gui);
            }
        };

    }

    /**
     * Returns the action listener for the button for the spi.
     * @return The action listener.
     */
    protected final ActionListener getBtnSPIListener() {
        return new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                new ChartCompleteView(wp, gui);
            }
        };
    }

    /**
     * Returns the action listener for the button to close the window.
     * @return The action listener.
     */
    protected final ActionListener getBtnAllPVListener() {
        return new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent arg0) {
                new PVTableGUI(wp);
            }
        };
    }

    /**
     * Return the action listener for the button to add an testcase
     * @return The action listener
     */
    protected final ActionListener getBtnAddTestcase() {
        return new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e){
                String name = gui.getTxfTestTestcase().getText().trim();
                if(!name.equals("")){
                    testCaseController.addTestCase(new TestCase(wp.getWpId(), name));
                    gui.setTestcases(testCaseController);
                }
            }
        };
    }

    /**
     * Returns the mouse listener to show the testcase gui.
     * @return The mouse listener
     */
    protected final MouseListener getTblTestcaseListener() {
        return new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2) {
                    new TestcaseShow(testCaseController.getAllTestCases().get(gui.getTblTestcase().getSelectedRow()),
                            testCaseController, gui, getOuterClass());
                }
            }
        };
    }

    /**
     * Returns the action listener to execute the testcases.
     * @return The action listener
     */
    protected final ActionListener getBtnTestExecute(){
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(testCaseController.getAllTestCases().size() > 0){
                    new TestcaseExecutionShow(testCaseController, testCaseController.getAllTestCases().get(0), gui, true, getOuterClass());
                    actualTestCaseIndex = 0;
                }
                else{
                    gui.showMessage(generalStrings.testcaseMessage());
                }
            }
        };
    }

    /**
     * Return it self.
     * @return A reference to it self.
     */
    private WPShow getThis() {
        return this;
    }

    /**
     * Returns the main frame.
     * @return The main frame.
     */
    public final Component getMainFrame() {
        return gui;
    }

    /**
     * Returns the etc from the GUI.
     * @return The value of the etc.
     */
    public final double getETCfromGUI() {
        try {
            return gui.getETC();
        } catch (ParseException e) {
            return 0;
        }
    }

    /**
     * Updates the etc in the GUI.
     * @param etc
     *            The new value of the etc.
     */
    public final void updateETCInGUI(final double etc) {
        gui.setETC(etc);
        save();
    }

    /**
     * Set the start of the under work package.
     * @param oap
     *            The work package.
     * @param date
     *            The start date.
     */
    private void setUAPStartHope(final Workpackage oap, final Date date) {
        oap.setStartDateHope(date);
        WpManager.updateAP(oap);
        for (Workpackage actualUAP : WpManager.getUAPs(oap)) {
            setUAPStartHope(actualUAP, date);
        }
    }

    /**
     * Set the end of the under work package.
     * @param oap
     *            The work package.
     * @param date
     *            The end date.
     */
    private void setUAPEndHope(final Workpackage oap, final Date date) {
        oap.setEndDateHope(date);
        WpManager.updateAP(oap);
        for (Workpackage actualUAP : WpManager.getUAPs(oap)) {
            setUAPEndHope(actualUAP, date);
        }
    }

    private WPShow getOuterClass(){
        return this;
    }

    public void next(){
        if(actualTestCaseIndex < testCaseController.getAllTestCases().size() - 1){
            actualTestCaseIndex++;
            new TestcaseExecutionShow(testCaseController, testCaseController.getAllTestCases().get(actualTestCaseIndex), gui, true, getOuterClass());
        }
    }

    public WPShowGUI getWPShowGUI(){
        return gui;
    }
}
