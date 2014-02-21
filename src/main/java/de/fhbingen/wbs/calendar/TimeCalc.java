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

package de.fhbingen.wbs.calendar;

import de.fhbingen.wbs.dbServices.ValuesService;
import de.fhbingen.wbs.dbaccess.DBModelManager;
import de.fhbingen.wbs.dbaccess.data.Employee;
import de.fhbingen.wbs.translation.LocalizedStrings;
import de.fhbingen.wbs.functions.WpManager;
import de.fhbingen.wbs.globals.Controller;
import de.fhbingen.wbs.globals.Loader;
import de.fhbingen.wbs.globals.Workpackage;
import de.fhbingen.wbs.wpConflict.ConflictCompat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import de.fhbingen.wbs.wpComparators.APBacComparator;
import de.fhbingen.wbs.wpComparators.APEndDateComparator;
import de.fhbingen.wbs.wpComparators.APFollowerComparator;
import de.fhbingen.wbs.wpOverview.WPOverview;
import de.fhbingen.wbs.wpOverview.tabs.AvailabilityGraph;

/**
 * Calculates the duration from all work packages.
 */
public class TimeCalc {

    /**
     * Contains assignment Level -> Upper work packages without successors
     * (e.g. 2 - (1.2.0.0, 3.1.0.0))
     */
    private static Map<Integer, List<Workpackage>> levelOAP;

    /**
     * Contains assignment Day -> Employee, worked hours.
     */
    //TODO see if this is needed anywhere
    private static Map<Day, Map<String, Integer>> consumedWork;

    /** Manages the availabilities. */
    private AVManager avManager;

    /**
     * Constructor.
     */
    public TimeCalc() {
        Loader.setLoadingText(LocalizedStrings.getStatus().initialize());
        DBModelManager.getConflictsModel().deleteConflicts(); // Delete all
                                    // conflicts => will be
                                     // recalculated
        WPOverview.releaseAllConflicts();

        Set<Workpackage> allAp = WpManager.getAllAp();
        for (Workpackage actualWp : allAp) {
            // Setting the work package is blocked, if it is in the past.
            actualWp.setStartDateCalc(null);
            actualWp.setEndDateCalc(null);
        }

        this.avManager = new AVManager();


        List<Workpackage> uapWithoutAncestors = new LinkedList<>();
        levelOAP = new HashMap<>();

        // Creates a list with all sub work package without predecessors.
        init(uapWithoutAncestors, levelOAP);

        // A map that helps by manage the worked hours.
        consumedWork = new HashMap<>();

        // Calculates the duration of the sub work packages without
        // predecessors and sets the start date for successors.
        Loader.setLoadingText(LocalizedStrings.getStatus()
            .calculateSubWpWihoutPredecessors());
        while (!uapWithoutAncestors.isEmpty()) {
            calcUAP(uapWithoutAncestors.remove(0));
        }

        Workpackage root = WpManager.getRootAp();

        // Create assignment: Level - OAP
        Loader.setLoadingText(LocalizedStrings.getStatus()
            .checkTopLevelWorkpackageLevels());
        for (Workpackage actualWp : allAp) {

            if (actualWp.isIstOAP() && !actualWp.isIstInaktiv()) {
                List<Workpackage> allLevelWps;
                int actualLevel = actualWp.getlastRelevantIndex();
                if (levelOAP.get(actualLevel) == null) {
                    allLevelWps = new ArrayList<>();
                    levelOAP.put(actualLevel, allLevelWps);
                }
                allLevelWps = levelOAP.get(actualLevel);
                allLevelWps.add(actualWp);
            }
        }

        int maxLevels = root.getLvlIDs().length - 1; // Count of levels in
                                                     // this project.

        boolean finish = false;
        while (!finish) {
            for (int actualLevel = maxLevels; actualLevel >= 0; actualLevel--) {
                Loader.setLoadingText(LocalizedStrings.getStatus()
                    .checkTopLevelWorkpackageOnLevel(actualLevel + 1));
                if (levelOAP.containsKey(actualLevel)) {
                    List<Workpackage> actualLevelOAPs = levelOAP
                        .get(actualLevel);
                    int i = 0;
                    if (!actualLevelOAPs.isEmpty()) {
                        Workpackage actualOAP;
                        while (i < actualLevelOAPs.size()) {
                            actualOAP = actualLevelOAPs.get(i++);
                            calcOAP(actualOAP); // If possible, calculates
                                                // upper work packages.
                        }

                    }
                }
            }
            finish = true;
            for (int actualLevel = maxLevels; actualLevel >= 0; actualLevel--) {

                if (levelOAP.containsKey(actualLevel)
                    && !levelOAP.get(actualLevel).isEmpty()) {
                    finish = false;

                }
            }
        }
        Loader.setLoadingText(LocalizedStrings.getStatus().saveValues());
        for (Workpackage actualWp : allAp) {
            actualWp.setTempStartHope(null);
            if (!actualWp.isIstInaktiv()) {
                if (actualWp.getEndDateHope() != null
                    && actualWp.getEndDateCalc().after(
                        actualWp.getEndDateHope())) {
                    // The wished end date is not possible.
                    WPOverview.throwConflict(new ConflictCompat(new Date(System
                        .currentTimeMillis()), ConflictCompat.ENDWISH_FAIL,
                        WPOverview.getUser().getId(), actualWp));
                }
                Calendar cal = new GregorianCalendar();
                cal.setTime(actualWp.getEndDateCalc());
            }
            WpManager.updateAP(actualWp);
        }

    }

    /**
     * Returns the start date as early as possible to a work package.
     * @param wp
     *            The work package.
     * @return The date as early as possible.
     */
    private Date getPossibleStartDate(final Workpackage wp) {
        if (wp.equals(WpManager.getRootAp())) {
            return avManager.getNextWorkDate(WpManager.getRootAp()
                .getStartDateHope());
        }
        if (WpManager.getAncestors(wp).isEmpty()) {
            return getPossibleStartDate(WpManager.getWorkpackage(wp
                .getOAPID()));
        } else {
            boolean ok = true;
            Set<Workpackage> ancestors = WpManager.getAncestors(wp);
            TreeSet<Date> dates = new TreeSet<>();
            for (Workpackage actualWp : ancestors) {
                if (!actualWp.isIstInaktiv()) {
                    if (actualWp.getEndDateCalc() == null) {
                        ok = false; // It exists non calculated
                                    // predecessors.
                    } else {
                        dates.add(actualWp.getEndDateCalc());
                    }
                }
            }
            if (ok) {
                if (wp.getStartDateHope() != null
                    && wp.getStartDateHope().after(
                        avManager.getNextWorkDate(dates.last()))) {
                    return avManager.getNextWorkDate(wp.getStartDateHope());
                } else {
                    return avManager.getNextWorkDate(dates.last());
                }

            } else {
                return null;
            }

        }
    }

    /**
     * Fills the list uapWithoutAncestors and levelOAP.
     * @param levelOap
     *            A list which contain all sub packages without ancestors.
     * @param uapWithoutAncestors
     *            A list with the upper work packages.
     */
    public final void init(final List<Workpackage> uapWithoutAncestors,
        final Map<Integer, List<Workpackage>> levelOap) {

        Set<Workpackage> allWithoutAncestors = WpManager.getNoAncestorWps();

        Workpackage root = WpManager.getRootAp();

        int maxLevels = root.getLvlIDs().length;

        for (int i = 1; i <= maxLevels; i++) {
            levelOap.put(i, new ArrayList<Workpackage>()); // Creates a set
                                                           // for each
                                                           // level which
                                                           // is filled
                                                           // with the
                                                           // upper work
                                                           // packages from
                                                           // this level
                                                           // later.
        }

        int actualLevel;

        for (Workpackage actualWp : allWithoutAncestors) {
            if (!actualWp.isIstInaktiv()) {
                actualWp.setStartDateCalc(avManager
                    .getNextWorkDate(WpManager.getRootAp()
                        .getStartDateHope())); // No predecessors exist =>
                                               // use ProjektstartHope.
                if (actualWp.isIstOAP()) {

                    actualWp
                        .setStartDateCalc(getPossibleStartDate(actualWp));
                    Date startHope = avManager.getNextWorkDate(actualWp
                        .getStartDateHope());
                    if (actualWp.getStartDateCalc() != null
                        && startHope != null
                        && actualWp.getStartDateCalc().before(startHope)) {
                        actualWp.setStartDateCalc(startHope);
                        fillUAPHopes(actualWp, startHope);
                    } else if (actualWp.getStartDateCalc() != null
                        && actualWp.getStartDateHope() != null
                        && actualWp.getStartDateCalc().before(startHope)) {
                        WPOverview.throwConflict(new ConflictCompat(new Date(
                            System.currentTimeMillis()),
                            ConflictCompat.STARTWISH_FAIL, WPOverview.getUser()
                                .getId(), actualWp));
                    }

                    List<Workpackage> allLevelWps;
                    actualLevel = actualWp.getlastRelevantIndex();
                    if (levelOap.get(actualLevel) == null) {
                        allLevelWps = new ArrayList<>();
                        levelOap.put(actualLevel, allLevelWps); // Insert
                                                                // the
                                                                // upper
                                                                // work
                                                                // package
                                                                // to the
                                                                // list.
                    }
                    allLevelWps = levelOap.get(actualLevel);
                    allLevelWps.add(actualWp);

                } else {
                    uapWithoutAncestors.add(actualWp); // Insert the sub
                                                       // work package into
                                                       // the list.
                }
            }

        }
        // Sorts the work packages.
        Collections.sort(uapWithoutAncestors, new APEndDateComparator());
        Collections.sort(uapWithoutAncestors, new APFollowerComparator());
        Collections.sort(uapWithoutAncestors, new APBacComparator());
    }

    /**
     * Fills all sub work packages with the temporary wished start date.
     * This means that the date is not saved, it is only used to calculate
     * the duration.
     * @param wp
     *          The actual work package.
     * @param startHope
     *          Date you hope to start.
     */
    private void fillUAPHopes(final Workpackage wp, final Date startHope) {
        if (wp.getStartDateHope() != null) {
            if (wp.getStartDateHope().before(startHope)) {
                wp.setTempStartHope(startHope);
            }
        } else {
            wp.setTempStartHope(startHope);
        }
        for (Workpackage actualUAP : WpManager.getUAPs(wp)) {
            if (!actualUAP.isIstInaktiv()) {
                fillUAPHopes(actualUAP, startHope);
            }
        }
    }

    /**
     * Calculates the duration of the actual upper work package and deletes
     * it, if successful, out of the list.
     * @param actualOAP
     *            The actual upper work package.
     */
    private void calcOAP(final Workpackage actualOAP) {
        ValuesService.deletePV(actualOAP.getWpId());
        int oapLevel = actualOAP.getlastRelevantIndex();
        List<Workpackage> actualLevelOAPs = levelOAP.get(oapLevel);
        boolean done = true;

        Controller
            .showConsoleMessage("", Controller.TIME_CALCULATION_DEBUG);
        Controller.showConsoleMessage(LocalizedStrings.getStatus()
            .tryCalcualteTopLevelWp(actualOAP.getStringID()),
            Controller.TIME_CALCULATION_DEBUG);
        Controller.showConsoleMessage(LocalizedStrings.getStatus()
            .checkSubWps(actualOAP.getStringID()),
            Controller.TIME_CALCULATION_DEBUG);

        // For all sub work packages from the upper work package.
        for (Workpackage actualUAP : WpManager.getUAPs(actualOAP)) {
            if (!actualUAP.isIstInaktiv()) {
                actualUAP.setStartDateCalc(getPossibleStartDate(actualUAP));
                if (actualUAP.getStartDateCalc() == null) {

                    if (actualUAP.getStartDateCalc() != null
                        && actualUAP.getStartDateHope() != null
                        && actualUAP.getStartDateCalc().before(
                            actualUAP.getStartDateHope())) {
                        Date startHope = avManager
                            .getNextWorkDate(actualUAP.getStartDateHope());
                        actualUAP.setStartDateCalc(startHope);
                    } else if (actualUAP.getStartDateCalc() != null
                        && actualUAP.getStartDateHope() != null
                        && actualUAP.getStartDateCalc().before(
                            avManager.getNextWorkDate(actualUAP
                                .getStartDateHope()))) {
                        WPOverview.throwConflict(new ConflictCompat(new Date(
                            System.currentTimeMillis()),
                            ConflictCompat.STARTWISH_FAIL, WPOverview.getUser()
                                .getId(), actualOAP, actualUAP));
                    }
                }
                if (actualUAP.getStartDateCalc() != null
                    && actualUAP.getEndDateCalc() == null) {
                    if (!actualUAP.isIstOAP()) {
                        Controller.showConsoleMessage(
                            LocalizedStrings.getStatus().calculateWp(
                                actualUAP.getStringID()),
                            Controller.TIME_CALCULATION_DEBUG);
                        calcUAP(actualUAP);
                        Controller.showConsoleMessage(
                            LocalizedStrings.getStatus().wpWasCalculated(
                                actualUAP.getStringID()),
                            Controller.TIME_CALCULATION_DEBUG);
                    }
                }
                if (actualUAP.getEndDateCalc() == null
                    || levelOAP.get(actualUAP.getlastRelevantIndex())
                        .contains(actualUAP)) {
                    done = false;
                    Controller
                        .showConsoleMessage(
                            LocalizedStrings.getStatus()
                                .wpCouldNotBeCalculated(
                                    actualUAP.getStringID()),
                            Controller.TIME_CALCULATION_DEBUG);
                }
                if (actualOAP.getEndDateCalc() == null
                    || (actualUAP.getEndDateCalc() != null && actualOAP
                        .getEndDateCalc()
                        .before(actualUAP.getEndDateCalc()))) {

                    if (actualUAP.getEndDateCalc() != null) {
                        actualOAP
                            .setEndDateCalc(actualUAP.getEndDateCalc());
                        Controller.showConsoleMessage(
                            LocalizedStrings.getStatus().endDateAssumed(
                                actualOAP.getStringID(),
                                actualUAP.getStringID(),
                                Controller.DATE_DAY_TIME.format(actualUAP
                                    .getEndDateCalc())),
                            Controller.TIME_CALCULATION_DEBUG);
                    }

                }
            }

        }
        if (WpManager.getUAPs(actualOAP).isEmpty()) {
            actualOAP.setEndDateCalc(actualOAP.getStartDateCalc());
            actualLevelOAPs.remove(actualOAP);
            Controller.showConsoleMessage(LocalizedStrings.getStatus()
                .endDateDoesNotExist(actualOAP.getStringID()), true,
                Controller.TIME_CALCULATION_DEBUG);
        } else {
            if (done && actualOAP.getEndDateCalc() != null) {
                actualLevelOAPs.remove(actualOAP);

                Controller.showConsoleMessage(
                    LocalizedStrings.getStatus().durationFinallyCalculated(
                        actualOAP.getStringID(),
                        actualOAP.getEndDateCalc().toString()),
                    Controller.TIME_CALCULATION_DEBUG);
                Controller.showConsoleMessage("",
                    Controller.TIME_CALCULATION_DEBUG);

                Date ancestorStartNew = avManager.getNextWorkDate(actualOAP
                    .getEndDateCalc());

                // 6 SD Set new.
                Controller.showConsoleMessage(
                    LocalizedStrings.getStatus().setEndDateOfSuccessors(
                        actualOAP.getStringID(),
                        Controller.DATE_DAY_TIME.format(ancestorStartNew)),
                    Controller.TIME_CALCULATION_DEBUG);

                List<Workpackage> actualFollowers = new ArrayList<>(
                    actualOAP.getFollowers());
                Collections
                    .sort(actualFollowers, new APEndDateComparator());
                Collections.sort(actualFollowers,
                    new APFollowerComparator());
                Collections.sort(actualFollowers, new APBacComparator());
                for (Workpackage actualFollower : actualFollowers) {
                    if (!actualFollower.isIstInaktiv()) {
                        if (actualFollower.getStartDateCalc() == null
                            || ancestorStartNew.after(actualFollower
                                .getStartDateCalc())) {
                            actualFollower
                                .setStartDateCalc(ancestorStartNew);
                        }

                        if (actualFollower.getEndDateCalc() == null
                            && actualFollower.canCalc()) {
                            Controller.showConsoleMessage(LocalizedStrings
                                    .getStatus().successorFound(actualOAP
                                            .getStringID()),
                                    Controller.TIME_CALCULATION_DEBUG);
                            if (actualFollower.isIstOAP()) {
                                calcOAP(actualFollower);
                            } else {
                                calcUAP(actualFollower);
                            }
                        }
                    }

                }
            }

        }

    }

    /**
     * Calculates the duration of the work package and it`s successor.
     * @param uap
     *            The work package.
     */
    private void calcUAP(final Workpackage uap) {
        Controller
            .showConsoleMessage("", Controller.TIME_CALCULATION_DEBUG);
        Controller.showConsoleMessage(LocalizedStrings.getStatus()
            .calculateSubWp(uap.getStringID()),
            Controller.TIME_CALCULATION_DEBUG);

        // 3 Calculate end date of the sub work package.
        Date startDate = uap.getStartDateCalc();

        if (uap.getStartDateHope() != null
            && startDate.before(uap.getStartDateHope())) {
            startDate = avManager.getNextWorkDate(uap.getStartDateHope());
            uap.setStartDateCalc(startDate);
        } else if (startDate != null && startDate.after(startDate)) {
            WPOverview.throwConflict(new ConflictCompat(new Date(System
                .currentTimeMillis()), ConflictCompat.STARTWISH_FAIL, WPOverview
                .getUser().getId(), uap));
        }

        int bac = uap.getBacStunden().intValue();
        List<Employee> employees = uap.getWorkers();
        List<String> workers = new ArrayList<>();
        for (Employee emp : employees) {
            workers.add(emp.getLogin());
        }
        Calendar actualDayTime = new GregorianCalendar();
        actualDayTime.setTime(startDate);

        Day actualDay = new Day(actualDayTime.getTime());

        consumedWork.put(actualDay, new HashMap<String, Integer>());

        boolean stillWork = true;

        int allWorkedToday = 0;
        int possibleWorkToday = 0;

        Map<Day, Double> pvs = new HashMap<Day, Double>();
        while (stillWork) {

            if (actualDay.after(new Day(
                new Date(System.currentTimeMillis())))) {
                ValuesService.deletePV(uap.getWpId(), actualDay);
                Map<String, Integer> actualDayRemaining = avManager
                    .getRemainingDayWorks(actualDay, workers);
                possibleWorkToday = avManager.getTogetherRemainingDayWork(
                    actualDay, workers);
                allWorkedToday = 0;
                Controller.showConsoleMessage(
                    "  "
                        + LocalizedStrings.getStatus().worksHoursOnDay(
                            Controller.DATE_DAY.format(actualDay),
                            possibleWorkToday, bac),
                    Controller.TIME_CALCULATION_DEBUG);
                if (possibleWorkToday < bac) { // The work package have to
                                               // be finished by all
                                               // employees.

                    for (String actualWorker : workers) {
                        int workerWorkedToday = actualDayRemaining
                            .get(actualWorker);
                        bac -= workerWorkedToday;
                        avManager.addWork(actualWorker, actualDay,
                            workerWorkedToday);
                        if (!pvs.containsKey(actualDay)) {
                            pvs.put(actualDay,
                                workerWorkedToday * uap.getWpStundensatz());
                        } else {
                            double pvTillNow = pvs.get(actualDay);
                            pvs.put(
                                actualDay,
                                pvTillNow + workerWorkedToday
                                    * uap.getWpStundensatz());
                        }
                        Controller.showConsoleMessage(
                            "     "
                                + LocalizedStrings.getStatus()
                                    .workerWorkHoursToday(actualWorker,
                                        workerWorkedToday),
                            Controller.TIME_CALCULATION_DEBUG);
                    }
                    allWorkedToday = possibleWorkToday;
                } else {
                    int optimalWork = bac / workers.size();
                    while (bac > 0) {
                        for (String actualWorker : workers) {
                            if (bac > 0) {
                                if (actualDayRemaining.get(actualWorker)
                                        >= optimalWork) {
                                    bac -= optimalWork;
                                    avManager.addWork(actualWorker,
                                        actualDay, optimalWork);
                                    if (!pvs.containsKey(actualDay)) {
                                        pvs.put(actualDay, optimalWork
                                            * uap.getWpStundensatz());
                                    } else {
                                        double pvTillNow = pvs
                                            .get(actualDay);
                                        pvs.put(
                                            actualDay,
                                            pvTillNow + optimalWork
                                                * uap.getWpStundensatz());
                                    }
                                    allWorkedToday += optimalWork;
                                    Controller.showConsoleMessage(
                                        "     "
                                            + LocalizedStrings.getStatus()
                                                .workerWorkHoursToday(
                                                    actualWorker,
                                                    optimalWork),
                                        Controller.TIME_CALCULATION_DEBUG);
                                } else {
                                    int workerRemaining = actualDayRemaining
                                        .get(actualWorker);
                                    if (workerRemaining < 0) {
                                        workerRemaining = 0;
                                    }
                                    bac -= workerRemaining;
                                    if (!pvs.containsKey(actualDay)) {
                                        pvs.put(actualDay, workerRemaining
                                            * uap.getWpStundensatz());
                                    } else {
                                        double pvTillNow = pvs
                                            .get(actualDay);
                                        pvs.put(actualDay,
                                            pvTillNow + workerRemaining
                                                * uap.getWpStundensatz());
                                    }
                                    avManager.addWork(actualWorker,
                                        actualDay, workerRemaining);
                                    allWorkedToday += workerRemaining;
                                    if (workerRemaining < 0) {
                                        throw new RuntimeException(bac
                                            + " " + workerRemaining + " "
                                            + allWorkedToday);
                                    }
                                    Controller.showConsoleMessage(
                                        "     "
                                            + LocalizedStrings.getStatus()
                                                .workerAdditionalWork(
                                                    actualWorker,
                                                    workerRemaining),
                                        Controller.TIME_CALCULATION_DEBUG);
                                }
                            }

                        }
                        optimalWork = 1;
                    }
                }
                if (bac == 0) {
                    double percent = (double) allWorkedToday
                        / (double) possibleWorkToday;
                    double hoursConsumed = (percent * avManager
                        .getCompleteDayWorks(actualDay).get(
                            AvailabilityGraph.PROJECT_WORKER.getLogin()));
                    avManager.addWork(
                        AvailabilityGraph.PROJECT_WORKER.getLogin(),
                        actualDay, (int) hoursConsumed);
                    actualDayTime.setTime(avManager.getNextWorkTime(
                        actualDay,
                        avManager.getConsumedDayWork(actualDay).get(
                            AvailabilityGraph.PROJECT_WORKER.getLogin())));
                    stillWork = false;

                    savePV(pvs, uap);

                    Controller.showConsoleMessage(
                        LocalizedStrings.getStatus().finishedHoursOfWp(
                            uap.getStringID(),
                            Controller.DATE_DAY.format(actualDay),
                            (int) hoursConsumed, possibleWorkToday),
                        Controller.TIME_CALCULATION_DEBUG);
                } else {
                    actualDayTime.add(Calendar.DATE, 1);
                    actualDay = new Day(actualDayTime.getTime());
                }

                consumedWork.put(actualDay, new HashMap<String, Integer>());
            } else {

                Calendar actualDayCal = new GregorianCalendar();
                actualDayCal.setTime(actualDay);

                Day nextFriday = new Day(ValuesService.getNextFriday(
                    new Date(System.currentTimeMillis())).getTime());
                Calendar nextFridayCal = new GregorianCalendar();
                nextFridayCal.setTime(nextFriday);

                if (actualDay.equals(new Day(new Date(System
                    .currentTimeMillis())))) {
                    bac -= ValuesService.getApPv(uap.getWpId(),
                        nextFridayCal) / uap.getWpStundensatz();
                    actualDay = nextFriday;
                    actualDayCal.setTime(actualDay);
                    pvs.put(nextFriday,
                        ValuesService.getApPv(uap.getWpId(), nextFridayCal));

                    actualDayCal.add(Calendar.DATE, 1);
                    actualDayTime = actualDayCal;
                    actualDay = new Day(actualDayCal.getTime());
                    Controller.showConsoleMessage(
                        LocalizedStrings.getStatus().lastDayWithPV(
                            Controller.DATE_DAY.format(actualDay),
                            ValuesService.getApPv(uap.getWpId(),
                                nextFridayCal)),
                        Controller.TIME_CALCULATION_DEBUG);
                } else {
                    Controller.showConsoleMessage(
                        LocalizedStrings.getStatus().currentDayInPast(
                            Controller.DATE_DAY.format(actualDay),
                            (ValuesService.getApPv(uap.getWpId(),
                                actualDayCal) / uap.getWpStundensatz()),
                            bac), Controller.TIME_CALCULATION_DEBUG);
                    actualDayTime.add(Calendar.DATE, 1);
                    actualDay = new Day(actualDayTime.getTime());

                    if (uap.getEndDateCalc() != null) {
                        stillWork = false;
                    }
                }
            }

            // Controller.showConsoleMessage(" Arbeit ist geleistet,
            // es sind fuer die naechsten Tage noch "
            // + bac + " Stunden uebrig",
            // Controller.TIME_CALCULATION_DEBUG);

        }

        uap.setEndDateCalc(actualDayTime.getTime());

        Controller.showConsoleMessage(
            LocalizedStrings.getStatus().calculationFinished(
                uap.getStringID(),
                Controller.DATE_DAY_TIME.format(uap.getEndDateCalc())),
            Controller.TIME_CALCULATION_DEBUG);
        Controller
            .showConsoleMessage("", Controller.TIME_CALCULATION_DEBUG);

        // SD new in successor.
        Date ancestorStartNew = avManager.getNextWorkDate(uap
            .getEndDateCalc());

        Controller.showConsoleMessage(
            LocalizedStrings.getStatus().setStartDateOfSuccessors(
                uap.getStringID(),
                Controller.DATE_DAY_TIME.format(ancestorStartNew)),
            Controller.TIME_CALCULATION_DEBUG);

        List<Workpackage> actualFollowers = new ArrayList<>(
            uap.getFollowers());
        Collections.sort(actualFollowers, new APEndDateComparator());
        Collections.sort(actualFollowers, new APFollowerComparator());
        Collections.sort(actualFollowers, new APBacComparator());
        for (Workpackage actualFollower : actualFollowers) {
            if (!actualFollower.isIstInaktiv()) {
                if (actualFollower.getStartDateCalc() == null
                    || ancestorStartNew.after(actualFollower
                        .getStartDateCalc())) {
                    actualFollower.setStartDateCalc(ancestorStartNew);
                }
                if (actualFollower.canCalc() && !actualFollower.isIstOAP()) {
                    Controller.showConsoleMessage(LocalizedStrings
                        .getStatus().successorFound(uap.getStringID()),
                        Controller.TIME_CALCULATION_DEBUG);
                    calcUAP(actualFollower);
                }
            }

        }

    }

    /**
     * Saves all planned values from a specific work package into the data
     * base.
     * @param singlePVs
     *            Map with assignment Tag -> PV.
     * @param wp
     *            The work package.
     */
    private void savePV(final Map<Day, Double> singlePVs,
        final Workpackage wp) {
        final String wpID = wp.getStringID();
        final int wpKey = wp.getWpId();
        List<Day> days = new ArrayList<Day>(singlePVs.keySet());
        Collections.sort(days);
        double actualPV = 0.0;
        Calendar cal = new GregorianCalendar();
        if (!days.isEmpty()) {
            cal.setTime(days.get(0));
            cal = ValuesService.getPreviousFriday(cal.getTime());
            ValuesService.savePv(wpKey, cal.getTime(), 0.0);
        }

        for (Day actualDay : days) {
            if (actualDay.after(new Date(System.currentTimeMillis()))) {
                cal.setTime(actualDay);
                actualPV += singlePVs.get(actualDay);
                if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
                    ValuesService.savePv(wpKey, actualDay, actualPV);
                    Controller.showConsoleMessage(
                        LocalizedStrings.getStatus()
                            .pvValueOnDate(wpID,
                                Controller.DATE_DAY.format(actualDay),
                                actualPV),
                        Controller.TIME_CALCULATION_DEBUG);
                }
            }

        }
        if (cal.get(Calendar.DAY_OF_WEEK) != Calendar.FRIDAY) {
            while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.FRIDAY) {
                cal.add(Calendar.DATE, 1);
            }
            if (cal.getTime().after(new Date(System.currentTimeMillis()))) {
                Controller.showConsoleMessage(
                    LocalizedStrings.getStatus()
                        .pvValueOnDate(wpID,
                            Controller.DATE_DAY.format(cal.getTime()),
                            actualPV), Controller.TIME_CALCULATION_DEBUG);
                ValuesService.savePv(wpKey, cal.getTime(), actualPV);
            }

        }

    }
}
