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
import de.fhbingen.wbs.functions.WpManager;
import de.fhbingen.wbs.globals.Controller;
import de.fhbingen.wbs.globals.Workpackage;
import de.fhbingen.wbs.translation.LocalizedStrings;

import java.util.*;

/**
 * Calculates the duration from all work packages.
 */
public class TimeCalc {

    /**
     * Constructor.
     */
    public TimeCalc() {
        calcCalculatedDates();
        recalcPVs(WpManager.getAllAp());
    }




    /***
     *
     * @param wps
     *             Set of workpackages which should get a new Planned Value
     */
    private void recalcPVs(Set<Workpackage> wps){
        ValuesService.deleteAllPV();
        for(Workpackage w : wps){
            if(!w.isIstOAP())
            savePV(ValuesService.calcPVs(w), w);
        }
    }


    /**
     *
     * @param predec
     *              The predecessor
     * @param follower
     *              The follower in terms of dependencies
     */
    private void adjustDates(Workpackage predec, Workpackage follower){
        Date newStartDate = DateFunctions.getNextWorkday(predec.getEndDateCalc(), true);
        int followerDuration = DateFunctions.getWorkdayDistanceBetweenDates(follower.getStartDateCalc(), follower.getEndDateCalc());
        Date newEndDate = DateFunctions.calcDateByOffset(newStartDate, followerDuration);
        follower.setStartDateCalc(newStartDate);
        follower.setEndDateCalc(newEndDate);
        WpManager.updateAP(follower);
    }


    /**
     *
     * @param wp
     *          Worpackage as startingpoint for semirecursive brute force dependency check
     * TODO: this method could be optimized in terms of efficiency
     *       --> this way to iterate the dependency tree should be replaced by a more elegant one
     */
    private void checkDependenciesRecursive(Workpackage wp){
       for(Workpackage fellow: wp.getFollowers()){

           if (wp.getEndDateCalc().after(fellow.getStartDateCalc())) {
               adjustDates(wp, fellow);
           }
           checkDependenciesRecursive(fellow);
       }

        for (Workpackage predecessor : wp.getAncestors()) {
            if (predecessor.getEndDateCalc().getTime() >= wp.getStartDateCalc().getTime()) {
                adjustDates(predecessor, wp);
                checkDependenciesRecursive(predecessor);
            }
        }
    }



    /**
     * calculate the calculated Dates with respect to dependencies
     *
     */
    private void calcCalculatedDates(){
        // set all calculated Dates similar to all chosen Dates
        Set<Workpackage> allAp = WpManager.getAllAp();
        for (Workpackage actualWp : allAp) {
            actualWp.setStartDateCalc(DateFunctions.getNextWorkday(actualWp.getStartDateHope(), false));
            actualWp.setEndDateCalc(DateFunctions.getNextWorkday(actualWp.getEndDateHope(), false));
            WpManager.updateAP(actualWp);
        }

        // get all WPs with no Predecessors
        Set<Workpackage> allWithoutAncestors = WpManager.getNoAncestorWps();

        // find WPs without followers
        Set<Workpackage> allWithoutAncestorsButFollowers = new HashSet<Workpackage>();
        for(Workpackage actWp : allWithoutAncestors){
            if(actWp.getFollowers().size()>0)
            allWithoutAncestorsButFollowers.add(actWp);
        }

        for(Workpackage w : allWithoutAncestorsButFollowers) {
            checkDependenciesRecursive(w);
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

        for (Day actualDay : days) {
            cal.setTime(actualDay);
            ValuesService.savePv(wpKey, actualDay, singlePVs.get(actualDay));
            Controller.showConsoleMessage(
                LocalizedStrings.getStatus().pvValueOnDate(
                        wpID, Controller.DATE_DAY.format(actualDay), actualPV),
                        Controller.TIME_CALCULATION_DEBUG);
        }
    }
}
